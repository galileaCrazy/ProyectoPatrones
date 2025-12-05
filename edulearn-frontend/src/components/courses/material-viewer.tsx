"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { AlertCircle, Download, FileText, Loader2, Play, X, File, Image as ImageIcon } from "lucide-react"
import { API_URL, obtenerUrlArchivo } from "@/lib/api"

interface MaterialViewerProps {
  materialId: number
  usuarioId: number
  rolUsuario: string
  onClose?: () => void
}

interface MaterialData {
  id: number
  titulo: string
  descripcion: string
  tipoMaterial: string
  tamanoBytes: number
  duracionSegundos: number | null
  estado: string
  urlRecurso: string
  archivoPath: string
}

/**
 * Componente MaterialViewer REAL con carga de archivos
 *
 * Implementa el patrón Proxy en el frontend:
 * 1. Carga inicial solo muestra información básica del material
 * 2. El contenido pesado se carga solo cuando el usuario hace clic
 * 3. Soporte para VIDEO, PDF, IMAGEN, DOCUMENTO
 *
 * Patrón de Diseño: Proxy (Estructural) - Integración Frontend REAL
 */
export function MaterialViewer({ materialId, usuarioId, rolUsuario, onClose }: MaterialViewerProps) {
  const [material, setMaterial] = useState<MaterialData | null>(null)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [mostrarContenido, setMostrarContenido] = useState(false)
  const [tieneAcceso, setTieneAcceso] = useState(true)

  // Cargar información básica al montar
  useEffect(() => {
    cargarMaterialBasico()
  }, [materialId])

  const cargarMaterialBasico = async () => {
    try {
      setCargando(true)
      setError(null)

      // Cargar desde el API con Proxy
      const res = await fetch(`${API_URL}/materiales/proxy`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          materialId: materialId,
          usuarioId: usuarioId,
          rolUsuario: rolUsuario,
        }),
      })

      if (!res.ok) {
        throw new Error(`Error al cargar material: ${res.statusText}`)
      }

      const data = await res.json()

      setMaterial({
        id: data.id,
        titulo: data.titulo,
        descripcion: data.descripcion || "Sin descripción",
        tipoMaterial: data.tipoMaterial,
        tamanoBytes: data.tamanoBytes || 0,
        duracionSegundos: data.duracionSegundos,
        estado: data.estado || "activo",
        urlRecurso: data.urlRecurso,
        archivoPath: data.archivoPath,
      })

      setTieneAcceso(data.tieneAcceso !== false)
    } catch (err) {
      console.error("Error al cargar material:", err)
      setError(err instanceof Error ? err.message : "Error desconocido")
    } finally {
      setCargando(false)
    }
  }

  const handleCargarContenido = () => {
    setMostrarContenido(true)
  }

  const formatearTamano = (bytes: number): string => {
    if (!bytes) return "Desconocido"
    const mb = bytes / (1024 * 1024)
    if (mb >= 1) return `${mb.toFixed(2)} MB`
    const kb = bytes / 1024
    return `${kb.toFixed(2)} KB`
  }

  const formatearDuracion = (segundos: number | null): string => {
    if (!segundos) return "N/A"
    const minutos = Math.floor(segundos / 60)
    const segs = segundos % 60
    return `${minutos}:${segs.toString().padStart(2, "0")}`
  }

  const obtenerIconoTipo = (tipo: string) => {
    switch (tipo?.toUpperCase()) {
      case "VIDEO":
        return <Play className="w-5 h-5 text-blue-500" />
      case "PDF":
        return <FileText className="w-5 h-5 text-red-500" />
      case "IMAGE":
      case "IMAGEN":
        return <ImageIcon className="w-5 h-5 text-green-500" />
      default:
        return <File className="w-5 h-5 text-gray-500" />
    }
  }

  const renderizarContenido = () => {
    if (!material || !material.urlRecurso) {
      return (
        <div className="text-center py-8">
          <AlertCircle className="w-12 h-12 mx-auto text-yellow-500 mb-3" />
          <p className="text-muted-foreground">No hay contenido disponible para este material</p>
        </div>
      )
    }

    const url = material.urlRecurso.startsWith('http')
      ? material.urlRecurso
      : `${API_URL.replace('/api', '')}${material.urlRecurso}`

    switch (material.tipoMaterial?.toUpperCase()) {
      case "VIDEO":
        // Detectar si es un embed de YouTube o Vimeo
        const esEmbed = url.includes('youtube.com/embed') || url.includes('vimeo.com')

        return (
          <div className="space-y-3">
            <h3 className="font-semibold text-lg text-foreground">Reproductor de Video</h3>
            {esEmbed ? (
              <div className="aspect-video bg-background rounded-lg overflow-hidden border border-border">
                <iframe
                  src={url}
                  className="w-full h-full"
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen
                />
              </div>
            ) : (
              <video
                controls
                className="w-full rounded-lg border border-border shadow-lg bg-background"
                style={{ maxHeight: "500px" }}
              >
                <source src={url} type="video/mp4" />
                <source src={url} type="video/webm" />
                Tu navegador no soporta la reproducción de video.
              </video>
            )}
            <p className="text-sm text-muted-foreground">
              Utiliza los controles del reproductor para reproducir, pausar y ajustar el volumen.
            </p>
          </div>
        )

      case "PDF":
        return (
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="font-semibold text-lg">Visualizador de PDF</h3>
              <Button variant="outline" size="sm" onClick={() => window.open(url, "_blank")}>
                <Download className="w-4 h-4 mr-2" />
                Abrir en nueva pestaña
              </Button>
            </div>
            <iframe
              src={url}
              className="w-full rounded-lg border border-border shadow-lg"
              style={{ height: "600px" }}
              title={material.titulo}
            />
          </div>
        )

      case "IMAGE":
      case "IMAGEN":
        return (
          <div className="space-y-3">
            <h3 className="font-semibold text-lg">Vista de Imagen</h3>
            <div className="flex justify-center">
              <img
                src={url}
                alt={material.titulo}
                className="max-w-full rounded-lg border border-border shadow-lg"
                style={{ maxHeight: "600px" }}
              />
            </div>
          </div>
        )

      case "DOCUMENTO":
      case "AUDIO":
      default:
        return (
          <div className="space-y-3">
            <h3 className="font-semibold text-lg">Descargar Archivo</h3>
            <div className="text-center py-8 space-y-4">
              <File className="w-16 h-16 mx-auto text-blue-500" />
              <p className="text-muted-foreground">
                Este tipo de archivo debe descargarse para visualizarlo
              </p>
              <Button onClick={() => window.open(url, "_blank")} size="lg" className="gap-2">
                <Download className="w-4 h-4" />
                Descargar Archivo
              </Button>
            </div>
          </div>
        )
    }
  }

  if (cargando) {
    return (
      <Card className="border-border/50">
        <CardContent className="flex items-center justify-center py-12">
          <Loader2 className="w-8 h-8 animate-spin text-primary" />
          <span className="ml-3 text-muted-foreground">Cargando información del material...</span>
        </CardContent>
      </Card>
    )
  }

  if (error) {
    return (
      <Card className="border-red-200 bg-red-50 dark:bg-red-900/20">
        <CardContent className="flex items-center gap-3 py-6">
          <AlertCircle className="w-6 h-6 text-red-500" />
          <div>
            <p className="font-semibold text-red-700 dark:text-red-300">Error al cargar material</p>
            <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
          </div>
        </CardContent>
      </Card>
    )
  }

  if (!material) {
    return null
  }

  return (
    <div className="space-y-4">
      {/* Header del Material */}
      <Card className="border-border/50">
        <CardHeader>
          <div className="flex items-start justify-between">
            <div className="flex-1">
              <CardTitle className="flex items-center gap-2">
                {obtenerIconoTipo(material.tipoMaterial)}
                {material.titulo}
              </CardTitle>
              <CardDescription className="mt-2">{material.descripcion}</CardDescription>
            </div>
            {onClose && (
              <Button variant="ghost" size="sm" onClick={onClose}>
                <X className="w-4 h-4" />
              </Button>
            )}
          </div>
        </CardHeader>

        <CardContent>
          {/* Información del Material */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            <div className="space-y-1">
              <p className="text-xs text-muted-foreground">Tipo</p>
              <p className="font-semibold text-foreground">{material.tipoMaterial}</p>
            </div>
            <div className="space-y-1">
              <p className="text-xs text-muted-foreground">Tamaño</p>
              <p className="font-semibold text-foreground">{formatearTamano(material.tamanoBytes)}</p>
            </div>
            {material.duracionSegundos && (
              <div className="space-y-1">
                <p className="text-xs text-muted-foreground">Duración</p>
                <p className="font-semibold text-foreground">{formatearDuracion(material.duracionSegundos)}</p>
              </div>
            )}
            <div className="space-y-1">
              <p className="text-xs text-muted-foreground">Estado</p>
              <span
                className={`inline-block px-2 py-1 text-xs font-semibold rounded ${
                  material.estado === "activo"
                    ? "bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-300"
                    : "bg-gray-100 text-gray-700 dark:bg-gray-800 dark:text-gray-300"
                }`}
              >
                {material.estado}
              </span>
            </div>
          </div>

          {/* Botón para cargar contenido */}
          {!mostrarContenido && tieneAcceso && material.urlRecurso && (
            <div className="space-y-3">
              <div className="p-4 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg">
                <div className="flex items-start gap-3">
                  <AlertCircle className="w-5 h-5 text-blue-500 flex-shrink-0 mt-0.5" />
                  <div className="flex-1">
                    <p className="text-sm font-semibold text-blue-700 dark:text-blue-300">Patrón Proxy - Lazy Loading</p>
                    <p className="text-sm text-blue-600 dark:text-blue-400 mt-1">
                      El contenido se cargará solo cuando lo solicites, optimizando el uso de recursos.
                    </p>
                  </div>
                </div>
              </div>
              <Button onClick={handleCargarContenido} className="w-full gap-2">
                <Play className="w-4 h-4" />
                Cargar y Visualizar Contenido
              </Button>
            </div>
          )}

          {/* Sin archivo disponible */}
          {!material.urlRecurso && (
            <div className="p-4 bg-yellow-50 dark:bg-yellow-900/20 border border-yellow-200 dark:border-yellow-800 rounded-lg">
              <div className="flex items-center gap-2">
                <AlertCircle className="w-5 h-5 text-yellow-500" />
                <p className="text-sm font-semibold text-yellow-700 dark:text-yellow-300">
                  No hay archivo asociado a este material
                </p>
              </div>
            </div>
          )}

          {/* Sin acceso */}
          {!tieneAcceso && (
            <div className="p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg">
              <div className="flex items-center gap-2">
                <AlertCircle className="w-5 h-5 text-red-500" />
                <p className="text-sm font-semibold text-red-700 dark:text-red-300">No tienes acceso a este material</p>
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Visualización del contenido REAL */}
      {mostrarContenido && tieneAcceso && material.urlRecurso && (
        <Card className="border-border/50 bg-background">
          <CardContent className="pt-6">{renderizarContenido()}</CardContent>
        </Card>
      )}
    </div>
  )
}
