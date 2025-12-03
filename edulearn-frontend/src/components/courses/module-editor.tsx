"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { X, Save } from "lucide-react"

interface ContentItem {
  id: string
  name: string
  type: "VIDEO" | "PDF" | "LECTURE" | "TASK" | "QUIZ" | "SUPPLEMENT" | "FORM" | "PRACTICE"
  duration: number
  file?: string
  isCompleted?: boolean
  size?: string
}

interface ModuleEditorProps {
  moduleId: string
  moduleName: string
  estimatedTime: number
  description: string
  initialContent: ContentItem[]
  onSave: (moduleId: string, content: ContentItem[]) => void
  onClose: () => void
  role: string
  cursoId: string
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
  cursoId
}: ModuleEditorProps) {
  const [content, setContent] = useState<ContentItem[]>(initialContent)

  const handleSave = () => {
    onSave(moduleId, content)
  }

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <Card className="w-full max-w-4xl max-h-[90vh] overflow-y-auto">
        <CardHeader className="flex flex-row items-center justify-between border-b">
          <div>
            <CardTitle>Editar Módulo: {moduleName}</CardTitle>
            <p className="text-sm text-muted-foreground mt-1">{description}</p>
          </div>
          <Button variant="ghost" size="icon" onClick={onClose}>
            <X className="h-4 w-4" />
          </Button>
        </CardHeader>
        <CardContent className="p-6">
          <div className="space-y-4">
            <div>
              <Label>Tiempo estimado: {estimatedTime} horas</Label>
            </div>

            <div className="border-t pt-4">
              <h3 className="font-semibold mb-3">Contenido del módulo</h3>
              {content.length === 0 ? (
                <p className="text-sm text-muted-foreground">No hay contenido agregado</p>
              ) : (
                <div className="space-y-2">
                  {content.map((item) => (
                    <div key={item.id} className="border rounded p-3">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="font-medium">{item.name}</p>
                          <p className="text-sm text-muted-foreground">
                            {item.type} - {item.duration} min
                          </p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            <div className="flex justify-end gap-3 pt-4 border-t">
              <Button variant="outline" onClick={onClose}>
                Cancelar
              </Button>
              <Button onClick={handleSave} className="gap-2">
                <Save className="h-4 w-4" />
                Guardar Cambios
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
