"use client"

import { useState, useEffect } from "react"
import { ContentTreeNode } from "./content-tree-node"
import { ModuleEditor } from "./module-editor"
import { MaterialViewer } from "./material-viewer"
import { ModuleDecorators } from "./module-decorators"
import { CourseRewardsBanner } from "./course-rewards-banner"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Book, CheckCircle2, Circle, Lock, Loader2, AlertCircle, Plus, FolderPlus, Sparkles } from "lucide-react"
import { obtenerModulosPorCurso, type ModuloCursoDTO } from "@/lib/api"

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

interface TreeNode {
  id: string
  name: string
  type: "module" | "submodule" | "lesson" | "activity"
  status?: "completed" | "in-progress" | "locked"
  children?: TreeNode[]
  completedItems?: number
  totalItems?: number
  estimatedTime?: number
  description?: string
  content?: ContentItem[]
}

interface CourseContentTreeProps {
  courseId: string
  role: "ESTUDIANTE" | "DOCENTE" | "ADMIN"
}

export function CourseContentTree({ courseId, role }: CourseContentTreeProps) {
  // ═══════════════════════════════════════════════════════════════
  // ESTADOS DE NAVEGACIÓN Y DATOS
  // ═══════════════════════════════════════════════════════════════
  const [editingModuleId, setEditingModuleId] = useState<string | null>(null)
  const [selectedMaterial, setSelectedMaterial] = useState<ContentItem | null>(null)
  const [courseContent, setCourseContent] = useState<TreeNode[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showCreateModuleDialog, setShowCreateModuleDialog] = useState(false)
  const [newModuleName, setNewModuleName] = useState("")
  const [newModuleDescription, setNewModuleDescription] = useState("")
  const [creatingModule, setCreatingModule] = useState(false)

  // Estados para el patrón Decorator
  const [showDecoratorsDialog, setShowDecoratorsDialog] = useState(false)
  const [selectedModuleForDecorators, setSelectedModuleForDecorators] = useState<TreeNode | null>(null)

  // Usuario mock - En producción vendría del contexto
  const usuarioId = 1

  // Estados para Patrón Memento (solo para estudiantes)
  const [showMementoButtons, setShowMementoButtons] = useState(false)
  const [guardando, setGuardando] = useState(false)
  const [progresoData, setProgresoData] = useState<any>(null)
  const [historialData, setHistorialData] = useState<any[]>([])
  const [rewardsBannerKey, setRewardsBannerKey] = useState(0) // Para forzar recarga del banner

  // Cargar progreso del estudiante (para Memento)
  useEffect(() => {
    if (role === "ESTUDIANTE") {
      cargarProgresoEstudiante()
    }
  }, [courseId, usuarioId, role])

  const cargarProgresoEstudiante = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/progreso/${usuarioId}/${courseId}`)
      if (response.ok) {
        const data = await response.json()
        setProgresoData(data)

        // Cargar historial
        const historialResponse = await fetch(`http://localhost:8080/api/progreso/historial/${usuarioId}/${courseId}`)
        if (historialResponse.ok) {
          const historialData = await historialResponse.json()
          setHistorialData(historialData)
        }
      }
    } catch (error) {
      console.error('Error al cargar progreso:', error)
    }
  }

  const handleGuardarProgreso = async () => {
    setGuardando(true)
    try {
      const response = await fetch(`http://localhost:8080/api/progreso/guardar/${usuarioId}/${courseId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ descripcion: `Checkpoint: ${new Date().toLocaleString('es-MX')}` })
      })

      if (response.ok) {
        await cargarProgresoEstudiante()
        alert('✅ Progreso guardado exitosamente')
      } else {
        alert('❌ Error al guardar el progreso')
      }
    } catch (error) {
      console.error('Error al guardar progreso:', error)
      alert('❌ Error al guardar el progreso')
    } finally {
      setGuardando(false)
    }
  }

  const handleRestaurarProgreso = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/progreso/restaurar/${usuarioId}/${courseId}`, {
        method: 'POST'
      })

      if (response.ok) {
        await cargarProgresoEstudiante()
        alert('✅ Progreso restaurado exitosamente')
        // Recargar módulos
        await cargarModulos()
      } else {
        alert('❌ Error al restaurar el progreso')
      }
    } catch (error) {
      console.error('Error al restaurar progreso:', error)
      alert('❌ Error al restaurar el progreso')
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // CARGAR MÓDULOS DESDE EL BACKEND
  // ═══════════════════════════════════════════════════════════════
  useEffect(() => {
    cargarModulos()
  }, [courseId])

  const cargarModulos = async () => {
    try {
      setLoading(true)
      setError(null)

      const modulos = await obtenerModulosPorCurso(courseId)
      const modulosTransformados = transformarModulosATreeNodes(modulos)

      setCourseContent(modulosTransformados)
    } catch (err) {
      console.error("Error al cargar módulos:", err)
      setError(err instanceof Error ? err.message : "Error al cargar módulos")
    } finally {
      setLoading(false)
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // TRANSFORMAR MÓDULOS DEL BACKEND A TREE NODES
  // ═══════════════════════════════════════════════════════════════
  const transformarModulosATreeNodes = (modulos: ModuloCursoDTO[]): TreeNode[] => {
    return modulos
      .filter(m => !m.moduloPadreId) // Solo módulos raíz
      .map(modulo => ({
        id: String(modulo.id),
        name: modulo.nombre,
        type: "module" as const,
        status: modulo.estado === "ACTIVO" ? "in-progress" : "locked" as const,
        completedItems: 0,
        totalItems: modulo.materiales?.length || 0,
        estimatedTime: modulo.duracionHoras || 0,
        description: modulo.descripcion || "",
        content: modulo.materiales?.map(m => ({
          id: String(m.id),
          name: m.nombre,
          type: m.tipo as any,
          duration: m.duracion || 0,
          file: m.file,
          size: m.size,
          materialId: Number(m.id),
          isCompleted: false,
        })) || [],
        children: obtenerHijosModulo(modulo.id!, modulos),
      }))
  }

  const obtenerHijosModulo = (moduloId: number, todosModulos: ModuloCursoDTO[]): TreeNode[] => {
    return todosModulos
      .filter(m => m.moduloPadreId === moduloId)
      .map(submodulo => ({
        id: String(submodulo.id),
        name: submodulo.nombre,
        type: submodulo.esHoja ? "lesson" : "submodule" as const,
        status: submodulo.estado === "ACTIVO" ? "completed" : "locked" as const,
        completedItems: 0,
        totalItems: submodulo.materiales?.length || 0,
        description: submodulo.descripcion,
        content: submodulo.materiales?.map(m => ({
          id: String(m.id),
          name: m.nombre,
          type: m.tipo as any,
          duration: m.duracion || 0,
          file: m.file,
          materialId: Number(m.id),
        })) || [],
        children: obtenerHijosModulo(submodulo.id!, todosModulos),
      }))
  }

  // ═══════════════════════════════════════════════════════════════
  // FUNCIONES DE NAVEGACIÓN
  // ═══════════════════════════════════════════════════════════════
  const getModuleById = (id: string): TreeNode | undefined => {
    return courseContent.find((m) => m.id === id)
  }

  const handleEditModule = (moduleId: string) => {
    console.log("📝 [CourseContentTree] Solicitud de edición del módulo:", moduleId)
    console.log("👤 [CourseContentTree] Rol del usuario:", role)

    if (role === "DOCENTE" || role === "ADMIN") {
      setEditingModuleId(moduleId)
      setSelectedMaterial(null)
      console.log("✅ [CourseContentTree] Editor de módulo activado")
    } else {
      console.log("❌ [CourseContentTree] Acceso denegado. Solo DOCENTE/ADMIN pueden editar")
    }
  }

  const handleSaveModule = async (data: any) => {
    console.log("💾 [CourseContentTree] Guardando cambios del módulo:", data)
    setEditingModuleId(null)

    // Recargar módulos después de guardar
    await cargarModulos()

    console.log("✅ [CourseContentTree] Cambios guardados y módulos recargados")
  }

  const handleMaterialClick = (material: ContentItem) => {
    console.log("🎬 [CourseContentTree] Material seleccionado:", material.name)
    setSelectedMaterial(material)
  }

  // ═══════════════════════════════════════════════════════════════
  // CREAR NUEVO MÓDULO
  // ═══════════════════════════════════════════════════════════════
  const handleCreateModule = async () => {
    if (!newModuleName.trim()) {
      alert("Por favor ingresa un nombre para el módulo")
      return
    }

    try {
      setCreatingModule(true)

      const nuevoModulo = {
        cursoId: parseInt(courseId),
        nombre: newModuleName.trim(),
        descripcion: newModuleDescription.trim() || "Sin descripción",
        orden: courseContent.length + 1,
        duracionHoras: 0,
        moduloPadreId: null,
        esHoja: false,
        nivel: 0,
        estado: "ACTIVO",
      }

      const response = await fetch("http://localhost:8080/api/modulos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(nuevoModulo),
      })

      if (!response.ok) {
        throw new Error(`Error al crear el módulo: ${response.status}`)
      }

      const moduloCreado = await response.json()
      console.log("✅ Módulo creado exitosamente:", moduloCreado)

      // Limpiar formulario
      setNewModuleName("")
      setNewModuleDescription("")
      setShowCreateModuleDialog(false)

      // Recargar módulos
      await cargarModulos()
    } catch (err) {
      console.error("Error al crear módulo:", err)
      alert(`Error al crear el módulo: ${err instanceof Error ? err.message : "Error desconocido"}`)
    } finally {
      setCreatingModule(false)
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // FUNCIONES PARA EL PATRÓN DECORATOR
  // ═══════════════════════════════════════════════════════════════
  const handleOpenDecorators = (moduleId: string) => {
    const module = getModuleById(moduleId)
    if (module) {
      setSelectedModuleForDecorators(module)
      setShowDecoratorsDialog(true)
    }
  }

  const handleApplyDecorators = async () => {
    setShowDecoratorsDialog(false)
    setSelectedModuleForDecorators(null)
    // Recargar módulos para reflejar los cambios
    await cargarModulos()
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZADO CONDICIONAL: MaterialViewer con navegación mejorada
  // ═══════════════════════════════════════════════════════════════
  if (selectedMaterial && selectedMaterial.materialId) {
    // Encontrar el material en el árbol de contenido para navegación
    let todosLosMateriales: ContentItem[] = []
    courseContent.forEach(modulo => {
      if (modulo.content) {
        todosLosMateriales = [...todosLosMateriales, ...modulo.content]
      }
    })

    const indiceActual = todosLosMateriales.findIndex(m => m.materialId === selectedMaterial.materialId)
    const materialAnterior = indiceActual > 0 ? todosLosMateriales[indiceActual - 1] : null
    const materialSiguiente = indiceActual < todosLosMateriales.length - 1 ? todosLosMateriales[indiceActual + 1] : null

    return (
      <div className="space-y-4">
        <MaterialViewer
          materialId={selectedMaterial.materialId}
          usuarioId={usuarioId}
          rolUsuario={role}
          onClose={() => setSelectedMaterial(null)}
        />

        {/* Navegación para estudiantes */}
        {role === "ESTUDIANTE" && (
          <Card className="border-border/50">
            <CardContent className="pt-6">
              <div className="flex items-center justify-between">
                <Button
                  variant="outline"
                  onClick={() => materialAnterior && setSelectedMaterial(materialAnterior)}
                  disabled={!materialAnterior}
                >
                  ← Anterior
                </Button>

                {materialSiguiente ? (
                  <Button
                    onClick={async () => {
                      // Marcar material como completado en el backend
                      try {
                        const response = await fetch(`http://localhost:8080/api/materiales/${selectedMaterial.materialId}/completar`, {
                          method: 'POST',
                          headers: { 'Content-Type': 'application/json' },
                          body: JSON.stringify({
                            estudianteId: usuarioId,
                            cursoId: parseInt(courseId)
                          })
                        })

                        if (response.ok) {
                          console.log('✅ Material completado')
                          // Recargar progreso para actualizar badges
                          await cargarProgresoEstudiante()
                          // Forzar recarga del banner de recompensas
                          setRewardsBannerKey(prev => prev + 1)
                          // Avanzar al siguiente material
                          setSelectedMaterial(materialSiguiente)
                        } else {
                          alert('Error al completar el material')
                        }
                      } catch (error) {
                        console.error('Error:', error)
                        alert('Error al completar el material')
                      }
                    }}
                    className="bg-blue-600 hover:bg-blue-700"
                  >
                    Completar y Continuar →
                  </Button>
                ) : (
                  <Button
                    onClick={async () => {
                      // Marcar último material como completado
                      try {
                        const response = await fetch(`http://localhost:8080/api/materiales/${selectedMaterial.materialId}/completar`, {
                          method: 'POST',
                          headers: { 'Content-Type': 'application/json' },
                          body: JSON.stringify({
                            estudianteId: usuarioId,
                            cursoId: parseInt(courseId)
                          })
                        })

                        if (response.ok) {
                          await cargarProgresoEstudiante()
                          // Forzar recarga del banner de recompensas
                          setRewardsBannerKey(prev => prev + 1)
                          alert('¡Felicidades! Has completado todos los materiales')
                          setSelectedMaterial(null)
                        }
                      } catch (error) {
                        console.error('Error:', error)
                      }
                    }}
                    className="bg-green-600 hover:bg-green-700"
                  >
                    ¡Curso Completado! ✓
                  </Button>
                )}
              </div>

              <div className="mt-4 text-center text-sm text-muted-foreground">
                Material {indiceActual + 1} de {todosLosMateriales.length}
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    )
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZADO CONDICIONAL: Editor de Módulo
  // ═══════════════════════════════════════════════════════════════
  if (editingModuleId && (role === "DOCENTE" || role === "ADMIN")) {
    const moduleToEdit = getModuleById(editingModuleId)

    if (moduleToEdit) {
      console.log("🎨 [CourseContentTree] Renderizando ModuleEditor para:", moduleToEdit.name)

      return (
        <ModuleEditor
          moduleId={moduleToEdit.id}
          moduleName={moduleToEdit.name}
          estimatedTime={moduleToEdit.estimatedTime || 3}
          description={moduleToEdit.description || ""}
          initialContent={moduleToEdit.content || []}
          onSave={handleSaveModule}
          onClose={() => {
            console.log("❌ [CourseContentTree] Editor cerrado sin guardar")
            setEditingModuleId(null)
          }}
          role={role}
          cursoId={courseId}
        />
      )
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // ESTADO DE CARGA
  // ═══════════════════════════════════════════════════════════════
  if (loading) {
    return (
      <Card className="border-border/50">
        <CardContent className="flex items-center justify-center py-12">
          <Loader2 className="w-8 h-8 animate-spin text-primary mr-3" />
          <span className="text-muted-foreground">Cargando módulos del curso...</span>
        </CardContent>
      </Card>
    )
  }

  // ═══════════════════════════════════════════════════════════════
  // ESTADO DE ERROR
  // ═══════════════════════════════════════════════════════════════
  if (error) {
    return (
      <Card className="border-red-200 bg-red-50 dark:bg-red-900/20">
        <CardContent className="flex items-center gap-3 py-6">
          <AlertCircle className="w-6 h-6 text-red-500" />
          <div>
            <p className="font-semibold text-red-700 dark:text-red-300">Error al cargar módulos</p>
            <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
          </div>
        </CardContent>
      </Card>
    )
  }

  // ═══════════════════════════════════════════════════════════════
  // ESTADO VACÍO
  // ═══════════════════════════════════════════════════════════════
  if (courseContent.length === 0) {
    return (
      <>
        <Card className="border-border/50">
          <CardContent className="text-center py-12">
            <Book className="w-16 h-16 mx-auto text-muted-foreground mb-4" />
            <p className="text-lg font-semibold text-foreground mb-2">
              No hay módulos en este curso
            </p>
            <p className="text-sm text-muted-foreground mb-6">
              {role === "DOCENTE" || role === "ADMIN"
                ? "Comienza agregando módulos y contenido al curso"
                : "El profesor aún no ha agregado contenido a este curso"}
            </p>

            {(role === "DOCENTE" || role === "ADMIN") && (
              <Button
                onClick={() => setShowCreateModuleDialog(true)}
                className="bg-primary hover:bg-primary/90"
              >
                <Plus className="w-4 h-4 mr-2" />
                Agregar Primer Módulo
              </Button>
            )}
          </CardContent>
        </Card>

        {/* Dialog para crear módulo */}
        <Dialog open={showCreateModuleDialog} onOpenChange={setShowCreateModuleDialog}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Crear Nuevo Módulo</DialogTitle>
              <DialogDescription>
                Agrega un módulo al curso. Luego podrás agregar materiales y contenido.
              </DialogDescription>
            </DialogHeader>

            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label htmlFor="module-name">Nombre del Módulo *</Label>
                <Input
                  id="module-name"
                  placeholder="Ej: Introducción a la Programación"
                  value={newModuleName}
                  onChange={(e) => setNewModuleName(e.target.value)}
                  disabled={creatingModule}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="module-description">Descripción</Label>
                <Textarea
                  id="module-description"
                  placeholder="Describe los objetivos y contenido de este módulo"
                  value={newModuleDescription}
                  onChange={(e) => setNewModuleDescription(e.target.value)}
                  disabled={creatingModule}
                  rows={4}
                />
              </div>
            </div>

            <DialogFooter>
              <Button
                variant="outline"
                onClick={() => {
                  setShowCreateModuleDialog(false)
                  setNewModuleName("")
                  setNewModuleDescription("")
                }}
                disabled={creatingModule}
              >
                Cancelar
              </Button>
              <Button
                onClick={handleCreateModule}
                disabled={creatingModule || !newModuleName.trim()}
              >
                {creatingModule ? (
                  <>
                    <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                    Creando...
                  </>
                ) : (
                  <>
                    <FolderPlus className="w-4 h-4 mr-2" />
                    Crear Módulo
                  </>
                )}
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </>
    )
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZADO PRINCIPAL: Árbol de Contenido (Patrón Composite)
  // ═══════════════════════════════════════════════════════════════
  return (
    <>
      {/* Banner de Recompensas (Gamificación y Certificación) - Solo para Estudiantes */}
      {role === "ESTUDIANTE" && (
        <CourseRewardsBanner
          key={rewardsBannerKey}
          cursoId={courseId}
          estudianteId={usuarioId}
          onGuardarProgreso={handleGuardarProgreso}
          onRestaurarProgreso={handleRestaurarProgreso}
          historialCheckpoints={historialData.length}
          guardando={guardando}
        />
      )}

      <Card className="border-border/50">
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <Book className="w-5 h-5 text-primary" />
              Contenido del Curso
              {(role === "DOCENTE" || role === "ADMIN") && (
                <span className="ml-4 text-xs text-muted-foreground">
                  Modo edición activado - Pasa el cursor sobre los módulos
                </span>
              )}
            </div>

            {/* Botones de acción */}
            {(role === "DOCENTE" || role === "ADMIN") && (
              <div className="flex gap-2">
                <Button
                  onClick={() => setShowCreateModuleDialog(true)}
                  size="sm"
                  variant="outline"
                  className="gap-2"
                >
                  <Plus className="w-4 h-4" />
                  Agregar Módulo
                </Button>
              </div>
            )}
          </CardTitle>
        </CardHeader>
        <CardContent>
        <div className="space-y-1">
          {courseContent.map((node) => (
            <ContentTreeNode
              key={node.id}
              contentItem={node}
              level={0}
              role={role}
              onEditModule={handleEditModule}
              onMaterialClick={handleMaterialClick}
              onOpenDecorators={handleOpenDecorators}
            />
          ))}
        </div>

        {/* Legend */}
        <div className="mt-6 pt-6 border-t border-border/50">
          <h4 className="text-sm font-semibold text-foreground mb-3">Leyenda:</h4>
          <div className="flex flex-wrap gap-6">
            <div className="flex items-center gap-2 text-sm">
              <CheckCircle2 className="w-4 h-4 text-green-500" />
              <span className="text-muted-foreground">Completado</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <Circle className="w-4 h-4 text-blue-500" />
              <span className="text-muted-foreground">En Progreso</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <Lock className="w-4 h-4 text-gray-400" />
              <span className="text-muted-foreground">Bloqueado</span>
            </div>
          </div>

          {/* Información de Lazy Loading */}
          <div className="mt-4 p-3 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg">
            <p className="text-sm text-blue-700 dark:text-blue-300">
              <span className="font-semibold">💡 Patrón Proxy Activo:</span> Los materiales pesados (videos, PDFs) solo
              se cargarán cuando hagas clic en "Cargar Contenido". Esto optimiza el rendimiento y ahorra ancho de
              banda.
            </p>
          </div>
        </div>
      </CardContent>
    </Card>

    {/* Dialog para crear módulo */}
    <Dialog open={showCreateModuleDialog} onOpenChange={setShowCreateModuleDialog}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Crear Nuevo Módulo</DialogTitle>
          <DialogDescription>
            Agrega un módulo al curso. Luego podrás agregar materiales y contenido.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4 py-4">
          <div className="space-y-2">
            <Label htmlFor="module-name">Nombre del Módulo *</Label>
            <Input
              id="module-name"
              placeholder="Ej: Introducción a la Programación"
              value={newModuleName}
              onChange={(e) => setNewModuleName(e.target.value)}
              disabled={creatingModule}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="module-description">Descripción</Label>
            <Textarea
              id="module-description"
              placeholder="Describe los objetivos y contenido de este módulo"
              value={newModuleDescription}
              onChange={(e) => setNewModuleDescription(e.target.value)}
              disabled={creatingModule}
              rows={4}
            />
          </div>
        </div>

        <DialogFooter>
          <Button
            variant="outline"
            onClick={() => {
              setShowCreateModuleDialog(false)
              setNewModuleName("")
              setNewModuleDescription("")
            }}
            disabled={creatingModule}
          >
            Cancelar
          </Button>
          <Button
            onClick={handleCreateModule}
            disabled={creatingModule || !newModuleName.trim()}
          >
            {creatingModule ? (
              <>
                <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                Creando...
              </>
            ) : (
              <>
                <FolderPlus className="w-4 h-4 mr-2" />
                Crear Módulo
              </>
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>

    {/* Dialog para aplicar decoradores (Patrón Decorator) */}
    {selectedModuleForDecorators && (
      <ModuleDecorators
        moduleId={selectedModuleForDecorators.id}
        moduleName={selectedModuleForDecorators.name}
        open={showDecoratorsDialog}
        onClose={() => {
          setShowDecoratorsDialog(false)
          setSelectedModuleForDecorators(null)
        }}
        onApply={handleApplyDecorators}
      />
    )}

  </>
  )
}
