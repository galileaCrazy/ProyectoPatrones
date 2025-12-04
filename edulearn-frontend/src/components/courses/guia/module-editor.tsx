"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Plus, Edit2, Trash2, Save, X, Upload } from "lucide-react"
import { AddContentModal } from "./add-content-modal"

interface ContentItem {
  id: string
  name: string
  type: "video" | "lecture" | "task" | "quiz" | "supplement" | "pdf" | "form" | "practice"
  duration: number
  file?: string
  isCompleted?: boolean
}

interface ModuleEditorProps {
  moduleId: string
  moduleName: string
  estimatedTime: number
  description: string
  initialContent: ContentItem[]
  onSave: (data: any) => void
  onClose: () => void
  role: "professor" | "admin"
}

export function ModuleEditor({
  moduleId,
  moduleName,
  estimatedTime,
  description,
  initialContent,
  onSave,
  onClose,
  role,
}: ModuleEditorProps) {
  const [editingName, setEditingName] = useState(moduleName)
  const [editingTime, setEditingTime] = useState(estimatedTime)
  const [editingDescription, setEditingDescription] = useState(description)
  const [content, setContent] = useState(initialContent)
  const [showAddModal, setShowAddModal] = useState(false)
  const [editingContentId, setEditingContentId] = useState<string | null>(null)

  const handleAddContent = (newContent: ContentItem) => {
    if (editingContentId) {
      setContent(content.map((c) => (c.id === editingContentId ? newContent : c)))
      setEditingContentId(null)
    } else {
      setContent([...content, newContent])
    }
    setShowAddModal(false)
  }

  const handleDeleteContent = (id: string) => {
    setContent(content.filter((c) => c.id !== id))
  }

  const handleEditContent = (id: string) => {
    setEditingContentId(id)
    setShowAddModal(true)
  }

  const handleSave = () => {
    onSave({
      moduleId,
      name: editingName,
      estimatedTime: editingTime,
      description: editingDescription,
      content,
    })
  }

  const contentGrouped = {
    videos: content.filter((c) => c.type === "video"),
    lectures: content.filter((c) => c.type === "lecture"),
    tasks: content.filter((c) => c.type === "task"),
    quizzes: content.filter((c) => c.type === "quiz"),
    pdfs: content.filter((c) => c.type === "pdf"),
    forms: content.filter((c) => c.type === "form"),
    practices: content.filter((c) => c.type === "practice"),
  }

  if (role !== "professor" && role !== "admin") {
    return (
      <Card className="border-border/50">
        <CardHeader>
          <CardTitle>No tienes permisos para editar este módulo</CardTitle>
        </CardHeader>
      </Card>
    )
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <Card className="w-full max-w-4xl max-h-[90vh] overflow-y-auto border-border/50">
        {/* Header */}
        <CardHeader className="border-b border-border/50 sticky top-0 bg-background z-10">
          <div className="flex items-center justify-between">
            <CardTitle>Editar Módulo</CardTitle>
            <Button variant="ghost" size="sm" onClick={onClose}>
              <X className="w-4 h-4" />
            </Button>
          </div>
        </CardHeader>

        <CardContent className="space-y-6 p-6">
          {/* Información del Módulo */}
          <div className="space-y-4">
            <h3 className="font-semibold text-foreground">Información del Módulo</h3>

            <div>
              <label className="text-sm font-medium text-foreground mb-2 block">Nombre del Módulo</label>
              <input
                type="text"
                value={editingName}
                onChange={(e) => setEditingName(e.target.value)}
                className="w-full px-3 py-2 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                placeholder="Ej: Introducción a POO"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="text-sm font-medium text-foreground mb-2 block">Tiempo Estimado (horas)</label>
                <input
                  type="number"
                  value={editingTime}
                  onChange={(e) => setEditingTime(Number(e.target.value))}
                  className="w-full px-3 py-2 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                  min="1"
                  max="100"
                />
              </div>
            </div>

            <div>
              <label className="text-sm font-medium text-foreground mb-2 block">Descripción</label>
              <textarea
                value={editingDescription}
                onChange={(e) => setEditingDescription(e.target.value)}
                className="w-full px-3 py-2 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary resize-none"
                placeholder="Describe el contenido y objetivos del módulo..."
                rows={4}
              />
            </div>
          </div>

          {/* Contenido del Módulo */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="font-semibold text-foreground">Contenido del Módulo ({content.length} items)</h3>
              <Button onClick={() => setShowAddModal(true)} size="sm" className="gap-2">
                <Plus className="w-4 h-4" />
                Agregar Contenido
              </Button>
            </div>

            {/* Contenido agrupado por tipo */}
            <div className="space-y-4">
              {Object.entries(contentGrouped).map(([type, items]) => {
                if (items.length === 0) return null
                return (
                  <div key={type} className="space-y-2">
                    <h4 className="text-sm font-medium text-foreground capitalize">
                      {type === "pdfs"
                        ? "PDFs"
                        : type === "quizzes"
                          ? "Quizzes"
                          : type === "forms"
                            ? "Formularios"
                            : type === "practices"
                              ? "Prácticas"
                              : type === "lectures"
                                ? "Lecturas"
                                : type}
                      ({items.length})
                    </h4>
                    <div className="space-y-2 pl-4 border-l border-border/50">
                      {items.map((item) => (
                        <div
                          key={item.id}
                          className="flex items-center justify-between p-3 rounded-lg bg-muted/30 border border-border/50 group hover:bg-muted/50 transition-colors"
                        >
                          <div className="flex-1 min-w-0">
                            <p className="font-medium text-foreground truncate">{item.name}</p>
                            <p className="text-xs text-muted-foreground mt-1">
                              {item.duration} min • {item.type}
                            </p>
                          </div>
                          <div className="flex gap-2 ml-4">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleEditContent(item.id)}
                              className="opacity-0 group-hover:opacity-100 transition-opacity"
                            >
                              <Edit2 className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDeleteContent(item.id)}
                              className="opacity-0 group-hover:opacity-100 transition-opacity text-red-500 hover:text-red-600 hover:bg-red-50"
                            >
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )
              })}

              {content.length === 0 && (
                <div className="text-center py-8 text-muted-foreground">
                  <Upload className="w-8 h-8 mx-auto mb-2 opacity-50" />
                  <p>No hay contenido agregado aún</p>
                </div>
              )}
            </div>
          </div>

          {/* Botones de acción */}
          <div className="flex gap-3 justify-end border-t border-border/50 pt-6">
            <Button variant="outline" onClick={onClose}>
              Cancelar
            </Button>
            <Button onClick={handleSave} className="gap-2">
              <Save className="w-4 h-4" />
              Guardar Cambios
            </Button>
          </div>
        </CardContent>

        {/* Modal para agregar contenido */}
        {showAddModal && (
          <AddContentModal
            onAdd={handleAddContent}
            onClose={() => {
              setShowAddModal(false)
              setEditingContentId(null)
            }}
            editingItem={editingContentId ? content.find((c) => c.id === editingContentId) : undefined}
          />
        )}
      </Card>
    </div>
  )
}
