"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card } from "@/components/ui/card"
import { Play, BookOpen, CheckSquare, Award, Eye, FileText, Clipboard, Dumbbell, Upload, X, Link as LinkIcon } from "lucide-react"
import { FileUploadButton } from "./file-upload-button"

interface ContentItem {
  id: string
  name: string
  type: "video" | "lecture" | "task" | "quiz" | "supplement" | "pdf" | "form" | "practice"
  duration: number
  file?: string | null
  isCompleted?: boolean
  size?: number
}

interface AddContentModalProps {
  onAdd: (content: ContentItem) => void
  onClose: () => void
  editingItem?: ContentItem
}

const CONTENT_TYPES = [
  { value: "video", label: "Video", icon: Play, color: "text-blue-500" },
  { value: "lecture", label: "Lectura", icon: BookOpen, color: "text-purple-500" },
  { value: "task", label: "Tarea", icon: CheckSquare, color: "text-orange-500" },
  { value: "quiz", label: "Quiz", icon: Award, color: "text-red-500" },
  { value: "pdf", label: "PDF", icon: FileText, color: "text-pink-500" },
  { value: "form", label: "Formulario", icon: Clipboard, color: "text-green-500" },
  { value: "practice", label: "Práctica", icon: Dumbbell, color: "text-yellow-500" },
  { value: "supplement", label: "Complemento", icon: Eye, color: "text-cyan-500" },
]

export function AddContentModal({ onAdd, onClose, editingItem }: AddContentModalProps) {
  const [selectedType, setSelectedType] = useState<string>(editingItem?.type || "video")
  const [name, setName] = useState(editingItem?.name || "")
  const [duration, setDuration] = useState(editingItem?.duration || 5)
  const [file, setFile] = useState(editingItem?.file || "")
  const [fileSize, setFileSize] = useState<number>(editingItem?.size || 0)
  const [useUrl, setUseUrl] = useState(false)
  const [uploadedFileName, setUploadedFileName] = useState<string>("")

  const selectedTypeObj = CONTENT_TYPES.find((t) => t.value === selectedType)
  const Icon = selectedTypeObj?.icon

  const handleFileUploaded = (fileInfo: {
    filename: string
    originalFilename: string
    size: number
    url: string
    urlRecurso: string
  }) => {
    console.log("📎 Archivo subido:", fileInfo)
    setFile(fileInfo.urlRecurso)
    setFileSize(fileInfo.size)
    setUploadedFileName(fileInfo.originalFilename)

    // Auto-rellenar nombre si está vacío
    if (!name.trim()) {
      const nombreSinExtension = fileInfo.originalFilename.replace(/\.[^/.]+$/, "")
      setName(nombreSinExtension)
    }
  }

  const handleSubmit = () => {
    if (!name.trim()) {
      alert("Por favor ingresa un nombre para el contenido")
      return
    }

    // Validar que tenga archivo o URL para ciertos tipos
    if (
      (selectedType === "video" || selectedType === "pdf") &&
      !file.trim()
    ) {
      alert(`Por favor ${useUrl ? "ingresa una URL" : "sube un archivo"} para el ${selectedTypeObj?.label}`)
      return
    }

    const newContent: ContentItem = {
      id: editingItem?.id || `content_${Date.now()}`,
      name,
      type: selectedType as ContentItem["type"],
      duration,
      file: file || undefined,
      size: fileSize || undefined,
    }

    console.log("✅ Contenido agregado:", newContent)
    onAdd(newContent)
  }

  const tiposSoportanArchivos = ["video", "pdf", "form", "practice"]
  const puedeSubirArchivo = tiposSoportanArchivos.includes(selectedType)

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <Card className="w-full max-w-2xl max-h-[90vh] overflow-y-auto border-border/50">
        {/* Header */}
        <div className="border-b border-border/50 sticky top-0 bg-background z-10 p-6 flex items-center justify-between">
          <h2 className="text-xl font-bold text-foreground">
            {editingItem ? "Editar" : "Agregar"} Contenido al Módulo
          </h2>
          <Button variant="ghost" size="sm" onClick={onClose}>
            <X className="w-4 h-4" />
          </Button>
        </div>

        <div className="p-6 space-y-6">
          {/* Seleccionar Tipo */}
          <div>
            <label className="text-sm font-semibold text-foreground mb-3 block">Tipo de Contenido</label>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
              {CONTENT_TYPES.map((type) => {
                const TypeIcon = type.icon
                return (
                  <button
                    key={type.value}
                    onClick={() => setSelectedType(type.value)}
                    className={`flex flex-col items-center gap-2 p-4 rounded-lg border-2 transition-all ${
                      selectedType === type.value
                        ? "border-primary bg-primary/5"
                        : "border-border hover:border-primary/50 bg-muted/30"
                    }`}
                  >
                    <TypeIcon className={`w-6 h-6 ${type.color}`} />
                    <span className="text-xs font-medium text-foreground text-center">{type.label}</span>
                  </button>
                )
              })}
            </div>
          </div>

          {/* Formulario */}
          <div className="space-y-4">
            <div>
              <label className="text-sm font-medium text-foreground mb-2 block">
                Nombre del Contenido {selectedTypeObj && `(${selectedTypeObj.label})`}
              </label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder={`Ej: ${selectedType === "video" ? "Introducción a Conceptos" : "Lectura: Fundamentos"}`}
                className="w-full px-3 py-2 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="text-sm font-medium text-foreground mb-2 block">Duración (minutos)</label>
                <input
                  type="number"
                  value={duration}
                  onChange={(e) => setDuration(Number(e.target.value))}
                  className="w-full px-3 py-2 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                  min="1"
                  max="480"
                />
              </div>
            </div>

            {/* Campo de archivo/URL condicional */}
            {puedeSubirArchivo && (
              <div className="space-y-3">
                <label className="text-sm font-medium text-foreground block">
                  {selectedType === "video"
                    ? "Video"
                    : selectedType === "pdf"
                      ? "Documento PDF"
                      : selectedType === "form"
                        ? "Formulario"
                        : "Archivo de Práctica"}
                </label>

                {/* Tabs: Subir Archivo o Usar URL */}
                <div className="flex gap-2 border-b border-border">
                  <button
                    onClick={() => setUseUrl(false)}
                    className={`px-4 py-2 text-sm font-medium transition-colors ${
                      !useUrl
                        ? "border-b-2 border-primary text-primary"
                        : "text-muted-foreground hover:text-foreground"
                    }`}
                  >
                    <Upload className="w-4 h-4 inline-block mr-2" />
                    Subir Archivo
                  </button>
                  <button
                    onClick={() => setUseUrl(true)}
                    className={`px-4 py-2 text-sm font-medium transition-colors ${
                      useUrl
                        ? "border-b-2 border-primary text-primary"
                        : "text-muted-foreground hover:text-foreground"
                    }`}
                  >
                    <LinkIcon className="w-4 h-4 inline-block mr-2" />
                    URL Externa
                  </button>
                </div>

                {/* Contenido según tab */}
                {!useUrl ? (
                  <div className="space-y-2">
                    <FileUploadButton
                      onFileUploaded={handleFileUploaded}
                      tipoMaterial={selectedType.toUpperCase()}
                      acceptedTypes={
                        selectedType === "video"
                          ? "video/*"
                          : selectedType === "pdf"
                            ? "application/pdf,.pdf"
                            : "*"
                      }
                      label={`Subir ${selectedTypeObj?.label}`}
                      className="w-full"
                    />
                    {uploadedFileName && (
                      <div className="bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800 rounded-lg p-3">
                        <p className="text-sm text-green-700 dark:text-green-300">
                          ✓ Archivo subido: <strong>{uploadedFileName}</strong>
                        </p>
                        <p className="text-xs text-green-600 dark:text-green-400 mt-1">
                          Tamaño: {(fileSize / 1024 / 1024).toFixed(2)} MB
                        </p>
                      </div>
                    )}
                  </div>
                ) : (
                  <div>
                    <input
                      type="text"
                      value={file}
                      onChange={(e) => setFile(e.target.value)}
                      placeholder={selectedType === "video" ? "https://youtube.com/..." : "https://ejemplo.com/archivo"}
                      className="w-full px-3 py-2 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                    <p className="text-xs text-muted-foreground mt-2">
                      {selectedType === "video"
                        ? "Puedes usar URLs de YouTube, Vimeo u otros servicios"
                        : "Ingresa la URL completa del archivo"}
                    </p>
                  </div>
                )}
              </div>
            )}

            {/* Descripción por tipo */}
            <div className="bg-muted/30 border border-border/50 rounded-lg p-3 text-sm text-muted-foreground">
              {selectedType === "video" &&
                "Sube videos explicativos o tutoriales para que los estudiantes aprendan visualmente"}
              {selectedType === "lecture" && "Agrega lecturas en texto para proporcionar material de referencia"}
              {selectedType === "task" && "Crea tareas que los estudiantes deben completar y entregar"}
              {selectedType === "quiz" && "Diseña quizzes interactivos para evaluar el aprendizaje"}
              {selectedType === "pdf" && "Sube documentos PDF como material complementario"}
              {selectedType === "form" && "Añade formularios para recopilar información o feedback"}
              {selectedType === "practice" && "Crea ejercicios prácticos para que los estudiantes practiquen"}
              {selectedType === "supplement" && "Proporciona material complementario y recursos adicionales"}
            </div>
          </div>

          {/* Botones */}
          <div className="flex gap-3 justify-end border-t border-border/50 pt-6">
            <Button variant="outline" onClick={onClose}>
              Cancelar
            </Button>
            <Button onClick={handleSubmit} className="gap-2">
              <Upload className="w-4 h-4" />
              {editingItem ? "Actualizar" : "Agregar"} Contenido
            </Button>
          </div>
        </div>
      </Card>
    </div>
  )
}
