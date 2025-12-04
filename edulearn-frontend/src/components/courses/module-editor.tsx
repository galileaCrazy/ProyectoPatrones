"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import {
  X,
  Save,
  Plus,
  Edit2,
  Trash2,
  Play,
  BookOpen,
  CheckSquare,
  Award,
  FileText,
  Clipboard,
  Dumbbell,
  Eye,
  Clock,
} from "lucide-react"
import { AddContentModal } from "./add-content-modal"

interface ContentItem {
  id: string
  name: string
  type: "VIDEO" | "PDF" | "LECTURE" | "TASK" | "QUIZ" | "SUPPLEMENT" | "FORM" | "PRACTICE"
  duration: number
  file?: string | null
  isCompleted?: boolean
  size?: string | number
  materialId?: number
}

interface ModuleEditorProps {
  moduleId: string
  moduleName: string
  estimatedTime: number
  description: string
  initialContent: ContentItem[]
  onSave: (data: any) => void
  onClose: () => void
  role: string
  cursoId: string
}

const getContentIcon = (type: string) => {
  const iconClass = "w-5 h-5"
  switch (type) {
    case "VIDEO":
      return <Play className={`${iconClass} text-blue-500`} />
    case "PDF":
      return <FileText className={`${iconClass} text-pink-500`} />
    case "LECTURE":
      return <BookOpen className={`${iconClass} text-purple-500`} />
    case "TASK":
      return <CheckSquare className={`${iconClass} text-orange-500`} />
    case "QUIZ":
      return <Award className={`${iconClass} text-red-500`} />
    case "FORM":
      return <Clipboard className={`${iconClass} text-green-500`} />
    case "PRACTICE":
      return <Dumbbell className={`${iconClass} text-yellow-500`} />
    case "SUPPLEMENT":
      return <Eye className={`${iconClass} text-cyan-500`} />
    default:
      return <Eye className={`${iconClass} text-gray-500`} />
  }
}

const getTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    VIDEO: "Video",
    PDF: "PDF",
    LECTURE: "Lectura",
    TASK: "Tarea",
    QUIZ: "Quiz",
    FORM: "Formulario",
    PRACTICE: "Práctica",
    SUPPLEMENT: "Complemento",
  }
  return labels[type] || type
}

export function ModuleEditor({
  moduleId,
  moduleName: initialModuleName,
  estimatedTime: initialEstimatedTime,
  description: initialDescription,
  initialContent,
  onSave,
  onClose,
  role,
  cursoId,
}: ModuleEditorProps) {
  // ═══════════════════════════════════════════════════════════════
  // ESTADOS DEL EDITOR
  // ═══════════════════════════════════════════════════════════════
  const [moduleName, setModuleName] = useState(initialModuleName)
  const [estimatedTime, setEstimatedTime] = useState(initialEstimatedTime)
  const [description, setDescription] = useState(initialDescription)
  const [content, setContent] = useState<ContentItem[]>(initialContent)
  const [showAddContentModal, setShowAddContentModal] = useState(false)
  const [editingContent, setEditingContent] = useState<ContentItem | null>(null)

  // ═══════════════════════════════════════════════════════════════
  // AGRUPACIÓN DE CONTENIDO POR TIPO
  // ═══════════════════════════════════════════════════════════════
  const groupedContent = {
    videos: content.filter((c) => c.type === "VIDEO"),
    pdfs: content.filter((c) => c.type === "PDF"),
    lectures: content.filter((c) => c.type === "LECTURE"),
    tasks: content.filter((c) => c.type === "TASK"),
    quizzes: content.filter((c) => c.type === "QUIZ"),
    forms: content.filter((c) => c.type === "FORM"),
    practices: content.filter((c) => c.type === "PRACTICE"),
    supplements: content.filter((c) => c.type === "SUPPLEMENT"),
  }

  // ═══════════════════════════════════════════════════════════════
  // MANEJADORES DE CONTENIDO
  // ═══════════════════════════════════════════════════════════════
  const handleAddContent = (newContent: any) => {
    // Convertir el tipo de minúsculas a mayúsculas
    const contentItem: ContentItem = {
      id: newContent.id,
      name: newContent.name,
      type: newContent.type.toUpperCase() as ContentItem["type"],
      duration: newContent.duration,
      file: newContent.file,
      isCompleted: newContent.isCompleted,
      size: newContent.size,
      materialId: newContent.materialId,
    }

    if (editingContent) {
      // Editar contenido existente
      setContent((prev) => prev.map((item) => (item.id === contentItem.id ? contentItem : item)))
      setEditingContent(null)
    } else {
      // Agregar nuevo contenido
      setContent((prev) => [...prev, contentItem])
    }
    setShowAddContentModal(false)
  }

  const handleEditContent = (item: ContentItem) => {
    setEditingContent(item)
    setShowAddContentModal(true)
  }

  const handleDeleteContent = (itemId: string) => {
    if (confirm("¿Estás seguro de eliminar este contenido?")) {
      setContent((prev) => prev.filter((item) => item.id !== itemId))
    }
  }

  const handleSave = async () => {
    console.log("💾 [ModuleEditor] Guardando cambios del módulo")
    console.log("📝 Nombre:", moduleName)
    console.log("⏱️ Tiempo estimado:", estimatedTime, "horas")
    console.log("📄 Descripción:", description)
    console.log("📦 Contenido:", content.length, "items")

    try {
      // Importar las funciones necesarias
      const { actualizarModulo, obtenerModuloPorId } = await import("@/lib/api")

      console.log("🔍 Obteniendo módulo existente del backend...")

      // Primero obtener el módulo existente para preservar campos requeridos
      const moduloExistente = await obtenerModuloPorId(Number(moduleId))

      console.log("📋 Módulo existente obtenido:", moduloExistente)

      // Preparar materiales para el backend con formato correcto
      const materiales = content.map((item, index) => {
        // Convertir el ID si es string que empieza con "content_" o si ya es número
        const materialId = typeof item.id === 'string' && item.id.startsWith('content_')
          ? null
          : (typeof item.id === 'number' ? item.id : parseInt(item.id, 10))

        // Convertir tipo de frontend a tipo de backend
        const tipoMaterial = item.type.toUpperCase()

        return {
          id: materialId,
          nombre: item.name,
          titulo: item.name,
          descripcion: item.name, // Usar el nombre como descripción por defecto
          tipo: tipoMaterial,
          tipoMaterial: tipoMaterial,
          file: item.file || null,
          urlRecurso: item.file || null,
          duracion: item.duration || 5,
          orden: index + 1,
          tamanoBytes: (typeof item.size === 'number' ? item.size : null) as number | null,
          estado: "activo",
          esObligatorio: false,
        }
      })

      // Actualizar en la base de datos preservando todos los campos requeridos
      console.log("💾 Actualizando módulo con todos los campos...")

      await actualizarModulo(Number(moduleId), {
        id: Number(moduleId),
        cursoId: Number(cursoId),
        nombre: moduleName,
        descripcion: description,
        duracionHoras: estimatedTime,
        orden: moduloExistente.orden,
        moduloPadreId: moduloExistente.moduloPadreId,
        esHoja: moduloExistente.esHoja,
        nivel: moduloExistente.nivel,
        estado: moduloExistente.estado || "ACTIVO",
        materiales: materiales,
      })

      console.log("✅ [ModuleEditor] Cambios guardados en la base de datos")

      // Llamar callback de éxito
      onSave({
        moduleId,
        moduleName,
        estimatedTime,
        description,
        content,
      })
    } catch (error) {
      console.error("❌ [ModuleEditor] Error al guardar:", error)
      alert("Error al guardar los cambios: " + (error instanceof Error ? error.message : "Error desconocido"))
    }
  }

  const handleCloseModal = () => {
    setShowAddContentModal(false)
    setEditingContent(null)
  }

  // Calcular tiempo total del contenido
  const totalContentDuration = content.reduce((sum, item) => sum + item.duration, 0)

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZADO: Modal de pantalla completa
  // ═══════════════════════════════════════════════════════════════
  return (
    <>
      <div className="fixed inset-0 bg-background z-40 overflow-y-auto">
        <div className="max-w-6xl mx-auto p-6 space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-foreground">Editar Módulo</h1>
              <p className="text-sm text-muted-foreground mt-1">
                Curso ID: {cursoId} • Módulo ID: {moduleId}
              </p>
            </div>
            <Button variant="ghost" size="sm" onClick={onClose} className="gap-2">
              <X className="w-4 h-4" />
              Cerrar
            </Button>
          </div>

          {/* Información del Módulo */}
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Información del Módulo</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="md:col-span-2 space-y-2">
                  <Label htmlFor="moduleName">Nombre del Módulo</Label>
                  <Input
                    id="moduleName"
                    value={moduleName}
                    onChange={(e) => setModuleName(e.target.value)}
                    placeholder="Ej: Patrón Proxy - Lazy Loading"
                    className="bg-background"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="estimatedTime">Tiempo Estimado (horas)</Label>
                  <Input
                    id="estimatedTime"
                    type="number"
                    value={estimatedTime}
                    onChange={(e) => setEstimatedTime(Number(e.target.value))}
                    min="1"
                    max="100"
                    className="bg-background"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="description">Descripción</Label>
                <Textarea
                  id="description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="Describe brevemente el contenido y objetivos del módulo..."
                  rows={3}
                  className="bg-background resize-none"
                />
              </div>

              <div className="flex items-center gap-4 pt-2">
                <div className="flex items-center gap-2 text-sm">
                  <Clock className="w-4 h-4 text-muted-foreground" />
                  <span className="text-muted-foreground">
                    Tiempo total del contenido: <strong className="text-foreground">{totalContentDuration} min</strong>
                  </span>
                </div>
                <div className="flex items-center gap-2 text-sm">
                  <FileText className="w-4 h-4 text-muted-foreground" />
                  <span className="text-muted-foreground">
                    Total de items: <strong className="text-foreground">{content.length}</strong>
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Contenido del Módulo */}
          <Card className="border-border/50">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle>Contenido del Módulo</CardTitle>
              <Button onClick={() => setShowAddContentModal(true)} className="gap-2">
                <Plus className="w-4 h-4" />
                Agregar Contenido
              </Button>
            </CardHeader>
            <CardContent>
              {content.length === 0 ? (
                <div className="text-center py-12">
                  <FileText className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
                  <p className="text-muted-foreground mb-4">No hay contenido agregado a este módulo</p>
                  <Button onClick={() => setShowAddContentModal(true)} className="gap-2">
                    <Plus className="w-4 h-4" />
                    Agregar Primer Contenido
                  </Button>
                </div>
              ) : (
                <div className="space-y-6">
                  {/* Videos */}
                  {groupedContent.videos.length > 0 && (
                    <ContentGroup
                      title="Videos"
                      icon={<Play className="w-5 h-5 text-blue-500" />}
                      items={groupedContent.videos}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* PDFs */}
                  {groupedContent.pdfs.length > 0 && (
                    <ContentGroup
                      title="PDFs"
                      icon={<FileText className="w-5 h-5 text-pink-500" />}
                      items={groupedContent.pdfs}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* Lecturas */}
                  {groupedContent.lectures.length > 0 && (
                    <ContentGroup
                      title="Lecturas"
                      icon={<BookOpen className="w-5 h-5 text-purple-500" />}
                      items={groupedContent.lectures}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* Tareas */}
                  {groupedContent.tasks.length > 0 && (
                    <ContentGroup
                      title="Tareas"
                      icon={<CheckSquare className="w-5 h-5 text-orange-500" />}
                      items={groupedContent.tasks}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* Quizzes */}
                  {groupedContent.quizzes.length > 0 && (
                    <ContentGroup
                      title="Quizzes"
                      icon={<Award className="w-5 h-5 text-red-500" />}
                      items={groupedContent.quizzes}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* Formularios */}
                  {groupedContent.forms.length > 0 && (
                    <ContentGroup
                      title="Formularios"
                      icon={<Clipboard className="w-5 h-5 text-green-500" />}
                      items={groupedContent.forms}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* Prácticas */}
                  {groupedContent.practices.length > 0 && (
                    <ContentGroup
                      title="Prácticas"
                      icon={<Dumbbell className="w-5 h-5 text-yellow-500" />}
                      items={groupedContent.practices}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}

                  {/* Complementos */}
                  {groupedContent.supplements.length > 0 && (
                    <ContentGroup
                      title="Complementos"
                      icon={<Eye className="w-5 h-5 text-cyan-500" />}
                      items={groupedContent.supplements}
                      onEdit={handleEditContent}
                      onDelete={handleDeleteContent}
                    />
                  )}
                </div>
              )}
            </CardContent>
          </Card>

          {/* Botones de Acción */}
          <div className="flex justify-end gap-3 sticky bottom-0 bg-background py-4 border-t border-border/50">
            <Button variant="outline" onClick={onClose} size="lg">
              Cancelar
            </Button>
            <Button onClick={handleSave} size="lg" className="gap-2">
              <Save className="w-4 h-4" />
              Guardar Cambios
            </Button>
          </div>
        </div>
      </div>

      {/* Modal para Agregar/Editar Contenido */}
      {showAddContentModal && (
        <AddContentModal
          onAdd={handleAddContent}
          onClose={handleCloseModal}
          editingItem={
            editingContent
              ? {
                  id: editingContent.id,
                  name: editingContent.name,
                  type: editingContent.type.toLowerCase() as any,
                  duration: editingContent.duration,
                  file: editingContent.file,
                }
              : undefined
          }
        />
      )}
    </>
  )
}

// ═══════════════════════════════════════════════════════════════
// COMPONENTE: Grupo de Contenido
// ═══════════════════════════════════════════════════════════════
interface ContentGroupProps {
  title: string
  icon: React.ReactNode
  items: ContentItem[]
  onEdit: (item: ContentItem) => void
  onDelete: (itemId: string) => void
}

function ContentGroup({ title, icon, items, onEdit, onDelete }: ContentGroupProps) {
  const totalDuration = items.reduce((sum, item) => sum + item.duration, 0)

  return (
    <div className="space-y-3">
      <div className="flex items-center gap-2 font-semibold text-foreground">
        {icon}
        <span>
          {title} ({items.length})
        </span>
        <span className="text-sm text-muted-foreground font-normal ml-2">• {totalDuration} min total</span>
      </div>
      <div className="space-y-2 pl-7">
        {items.map((item) => (
          <ContentItemRow key={item.id} item={item} onEdit={() => onEdit(item)} onDelete={() => onDelete(item.id)} />
        ))}
      </div>
    </div>
  )
}

// ═══════════════════════════════════════════════════════════════
// COMPONENTE: Fila de Item de Contenido
// ═══════════════════════════════════════════════════════════════
interface ContentItemRowProps {
  item: ContentItem
  onEdit: () => void
  onDelete: () => void
}

function ContentItemRow({ item, onEdit, onDelete }: ContentItemRowProps) {
  return (
    <div className="flex items-center gap-3 p-3 rounded-lg border border-border/50 bg-muted/30 hover:bg-muted/50 transition-colors group">
      <div className="flex-shrink-0">{getContentIcon(item.type)}</div>
      <div className="flex-1 min-w-0">
        <p className="font-medium text-foreground truncate">{item.name}</p>
        <div className="flex items-center gap-2 text-xs text-muted-foreground mt-1">
          <span className="bg-primary/10 text-primary px-2 py-0.5 rounded font-medium">{getTypeLabel(item.type)}</span>
          <span>•</span>
          <Clock className="w-3 h-3" />
          <span>{item.duration} min</span>
          {item.size && (
            <>
              <span>•</span>
              <span className="bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-300 px-2 py-0.5 rounded font-medium">
                {item.size}
              </span>
            </>
          )}
        </div>
      </div>
      <div className="flex-shrink-0 flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
        <Button variant="ghost" size="sm" onClick={onEdit} className="h-8 w-8 p-0">
          <Edit2 className="w-4 h-4" />
        </Button>
        <Button variant="ghost" size="sm" onClick={onDelete} className="h-8 w-8 p-0 text-red-500 hover:text-red-700">
          <Trash2 className="w-4 h-4" />
        </Button>
      </div>
    </div>
  )
}
