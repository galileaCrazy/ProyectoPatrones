"use client"

import { useRef, useState } from "react"
import { Button } from "@/components/ui/button"
import { Upload, Loader2, Check, X } from "lucide-react"
import { subirArchivo } from "@/lib/api"
import { Progress } from "@/components/ui/progress"

interface FileUploadButtonProps {
  onFileUploaded: (fileInfo: {
    filename: string
    originalFilename: string
    size: number
    url: string
    urlRecurso: string
    type: string
  }) => void
  materialId?: number
  tipoMaterial?: string
  acceptedTypes?: string
  maxSizeMB?: number
  className?: string
  label?: string
}

/**
 * Componente de botón para subir archivos
 * Soporta progreso, validación y preview
 */
export function FileUploadButton({
  onFileUploaded,
  materialId,
  tipoMaterial,
  acceptedTypes = "video/*,application/pdf,.pdf,.doc,.docx,image/*",
  maxSizeMB = 500,
  className = "",
  label = "Subir Archivo",
}: FileUploadButtonProps) {
  const fileInputRef = useRef<HTMLInputElement>(null)
  const [uploading, setUploading] = useState(false)
  const [uploadProgress, setUploadProgress] = useState(0)
  const [uploadSuccess, setUploadSuccess] = useState(false)
  const [uploadError, setUploadError] = useState<string | null>(null)

  const handleButtonClick = () => {
    fileInputRef.current?.click()
  }

  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    if (!file) return

    // Validar tamaño
    const maxBytes = maxSizeMB * 1024 * 1024
    if (file.size > maxBytes) {
      setUploadError(`El archivo excede el tamaño máximo de ${maxSizeMB}MB`)
      setTimeout(() => setUploadError(null), 5000)
      return
    }

    // Resetear estados
    setUploading(true)
    setUploadProgress(0)
    setUploadSuccess(false)
    setUploadError(null)

    try {
      console.log("📤 Subiendo archivo:", file.name, `(${(file.size / 1024 / 1024).toFixed(2)} MB)`)

      const result = await subirArchivo(
        file,
        materialId,
        tipoMaterial,
        (progress) => {
          setUploadProgress(progress)
          console.log(`📊 Progreso de subida: ${progress.toFixed(0)}%`)
        }
      )

      console.log("✅ Archivo subido exitosamente:", result)

      setUploadSuccess(true)
      setUploadProgress(100)

      // Llamar callback con la información del archivo
      onFileUploaded({
        filename: result.filename,
        originalFilename: result.originalFilename,
        size: result.size,
        url: result.url,
        urlRecurso: result.urlRecurso,
        type: result.type,
      })

      // Limpiar input
      if (fileInputRef.current) {
        fileInputRef.current.value = ""
      }

      // Resetear éxito después de 3 segundos
      setTimeout(() => {
        setUploadSuccess(false)
        setUploadProgress(0)
      }, 3000)
    } catch (error) {
      console.error("❌ Error al subir archivo:", error)
      setUploadError(error instanceof Error ? error.message : "Error al subir archivo")
      setTimeout(() => setUploadError(null), 5000)
    } finally {
      setUploading(false)
    }
  }

  return (
    <div className={`space-y-2 ${className}`}>
      <input
        ref={fileInputRef}
        type="file"
        onChange={handleFileSelect}
        accept={acceptedTypes}
        className="hidden"
      />

      <Button
        type="button"
        onClick={handleButtonClick}
        disabled={uploading}
        variant={uploadSuccess ? "default" : uploadError ? "destructive" : "outline"}
        className="gap-2"
      >
        {uploading && <Loader2 className="w-4 h-4 animate-spin" />}
        {uploadSuccess && <Check className="w-4 h-4" />}
        {uploadError && <X className="w-4 h-4" />}
        {!uploading && !uploadSuccess && !uploadError && <Upload className="w-4 h-4" />}

        {uploading
          ? `Subiendo... ${uploadProgress.toFixed(0)}%`
          : uploadSuccess
            ? "¡Subido!"
            : uploadError
              ? "Error"
              : label}
      </Button>

      {uploading && (
        <div className="space-y-1">
          <Progress value={uploadProgress} className="h-2" />
          <p className="text-xs text-muted-foreground text-center">{uploadProgress.toFixed(0)}%</p>
        </div>
      )}

      {uploadError && <p className="text-xs text-red-500">{uploadError}</p>}
    </div>
  )
}
