"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Card, CardContent } from "@/components/ui/card"
import { Trophy, Award, Loader2, X, Sparkles } from "lucide-react"

interface ModuleDecoratorsProps {
  moduleId: string
  moduleName: string
  open: boolean
  onClose: () => void
  onApply: () => void
}

interface DecoradorData {
  gamificacionHabilitada: boolean
  gamificacionPuntos: number
  gamificacionBadge: string
  certificacionHabilitada: boolean
  certificacionTipo: string
  certificacionActiva: boolean
}

export function ModuleDecorators({
  moduleId,
  moduleName,
  open,
  onClose,
  onApply,
}: ModuleDecoratorsProps) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [preview, setPreview] = useState<string | null>(null)
  const [puedeTenerCertificacion, setPuedeTenerCertificacion] = useState(false)
  const [verificandoCertificacion, setVerificandoCertificacion] = useState(true)

  // Estados para gamificación
  const [gamificacionHabilitada, setGamificacionHabilitada] = useState(false)
  const [gamificacionPuntos, setGamificacionPuntos] = useState(100)
  const [gamificacionBadge, setGamificacionBadge] = useState("Estudiante Destacado")

  // Estados para certificación
  const [certificacionHabilitada, setCertificacionHabilitada] = useState(false)
  const [certificacionTipo, setCertificacionTipo] = useState("Certificado de Finalización")
  const [certificacionActiva, setCertificacionActiva] = useState(true)

  // Verificar si el módulo puede tener certificación cuando se abre el diálogo
  useEffect(() => {
    if (open) {
      verificarSiPuedeTenerCertificacion()
    }
  }, [open, moduleId])

  const verificarSiPuedeTenerCertificacion = async () => {
    try {
      setVerificandoCertificacion(true)
      const response = await fetch(
        `http://localhost:8080/api/modulos/${moduleId}/decoradores/puede-certificar`
      )

      if (response.ok) {
        const puede = await response.json()
        setPuedeTenerCertificacion(puede)
      } else {
        setPuedeTenerCertificacion(false)
      }
    } catch (err) {
      console.error("Error al verificar certificación:", err)
      setPuedeTenerCertificacion(false)
    } finally {
      setVerificandoCertificacion(false)
    }
  }

  const handleApplyDecorators = async () => {
    if (!gamificacionHabilitada && !certificacionHabilitada) {
      setError("Debes seleccionar al menos un decorador (Gamificación o Certificación)")
      return
    }

    try {
      setLoading(true)
      setError(null)

      const decoradorData: DecoradorData = {
        gamificacionHabilitada,
        gamificacionPuntos: gamificacionHabilitada ? gamificacionPuntos : 0,
        gamificacionBadge: gamificacionHabilitada ? gamificacionBadge : "",
        certificacionHabilitada,
        certificacionTipo: certificacionHabilitada ? certificacionTipo : "",
        certificacionActiva: certificacionHabilitada ? certificacionActiva : false,
      }

      const response = await fetch(`http://localhost:8080/api/modulos/${moduleId}/decoradores`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(decoradorData),
      })

      if (!response.ok) {
        // Intentar obtener el mensaje de error del backend
        const errorText = await response.text()
        throw new Error(errorText || `Error al aplicar decoradores: ${response.status}`)
      }

      const result = await response.json()
      setPreview(result.contenidoDecorado)

      setTimeout(() => {
        onApply()
        handleClose()
      }, 2000)
    } catch (err) {
      console.error("Error al aplicar decoradores:", err)
      setError(err instanceof Error ? err.message : "Error desconocido")
    } finally {
      setLoading(false)
    }
  }

  const handleClose = () => {
    setGamificacionHabilitada(false)
    setGamificacionPuntos(100)
    setGamificacionBadge("Estudiante Destacado")
    setCertificacionHabilitada(false)
    setCertificacionTipo("Certificado de Finalización")
    setCertificacionActiva(true)
    setError(null)
    setPreview(null)
    setPuedeTenerCertificacion(false)
    setVerificandoCertificacion(true)
    onClose()
  }

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-primary" />
            Extender Funcionalidad del Módulo (Patrón Decorator)
          </DialogTitle>
          <DialogDescription>
            Módulo: <span className="font-semibold">{moduleName}</span>
            <br />
            Añade gamificación y/o certificación sin modificar la estructura original del módulo.
          </DialogDescription>
        </DialogHeader>

        {error && (
          <Card className="border-red-200 bg-red-50 dark:bg-red-900/20">
            <CardContent className="pt-4 pb-4">
              <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
            </CardContent>
          </Card>
        )}

        {preview && (
          <Card className="border-green-200 bg-green-50 dark:bg-green-900/20">
            <CardContent className="pt-4 pb-4">
              <h4 className="font-semibold text-green-700 dark:text-green-300 mb-2">
                Vista Previa del Contenido Decorado:
              </h4>
              <pre className="text-xs text-green-600 dark:text-green-400 whitespace-pre-wrap">
                {preview}
              </pre>
              <p className="text-sm text-green-600 dark:text-green-400 mt-2">
                ✓ Decoradores aplicados exitosamente
              </p>
            </CardContent>
          </Card>
        )}

        <div className="space-y-6 py-4">
          {/* Gamificación */}
          <Card className="border-purple-200 dark:border-purple-800">
            <CardContent className="pt-6 pb-6">
              <div className="flex items-start gap-3 mb-4">
                <Trophy className="w-5 h-5 text-purple-500 mt-1" />
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-2">
                    <Checkbox
                      id="gamificacion"
                      checked={gamificacionHabilitada}
                      onCheckedChange={(checked) => setGamificacionHabilitada(checked as boolean)}
                    />
                    <Label htmlFor="gamificacion" className="text-base font-semibold cursor-pointer">
                      Gamificación
                    </Label>
                  </div>
                  <p className="text-sm text-muted-foreground mb-4">
                    Añade puntos y badges para motivar a los estudiantes
                  </p>

                  {gamificacionHabilitada && (
                    <div className="space-y-4 pl-7">
                      <div className="space-y-2">
                        <Label htmlFor="puntos">Puntos por completar</Label>
                        <Input
                          id="puntos"
                          type="number"
                          min="0"
                          value={gamificacionPuntos}
                          onChange={(e) => setGamificacionPuntos(parseInt(e.target.value) || 0)}
                          placeholder="100"
                        />
                      </div>

                      <div className="space-y-2">
                        <Label htmlFor="badge">Badge/Insignia</Label>
                        <Input
                          id="badge"
                          value={gamificacionBadge}
                          onChange={(e) => setGamificacionBadge(e.target.value)}
                          placeholder="Ej: Maestro de Java"
                        />
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Certificación */}
          <Card className={`${
            puedeTenerCertificacion
              ? "border-blue-200 dark:border-blue-800"
              : "border-gray-300 dark:border-gray-700 opacity-60"
          }`}>
            <CardContent className="pt-6 pb-6">
              <div className="flex items-start gap-3 mb-4">
                <Award className={`w-5 h-5 mt-1 ${
                  puedeTenerCertificacion ? "text-blue-500" : "text-gray-400"
                }`} />
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-2">
                    <Checkbox
                      id="certificacion"
                      checked={certificacionHabilitada}
                      onCheckedChange={(checked) => setCertificacionHabilitada(checked as boolean)}
                      disabled={!puedeTenerCertificacion || verificandoCertificacion}
                    />
                    <Label
                      htmlFor="certificacion"
                      className={`text-base font-semibold ${
                        puedeTenerCertificacion ? "cursor-pointer" : "cursor-not-allowed"
                      }`}
                    >
                      Certificación
                    </Label>
                  </div>

                  {verificandoCertificacion ? (
                    <p className="text-sm text-muted-foreground mb-4">
                      Verificando si este módulo puede tener certificación...
                    </p>
                  ) : puedeTenerCertificacion ? (
                    <p className="text-sm text-muted-foreground mb-4">
                      Otorga un certificado al completar este módulo (último módulo del curso)
                    </p>
                  ) : (
                    <p className="text-sm text-amber-600 dark:text-amber-400 mb-4">
                      ⚠️ La certificación solo está disponible en el último módulo del curso
                    </p>
                  )}

                  {certificacionHabilitada && puedeTenerCertificacion && (
                    <div className="space-y-4 pl-7">
                      <div className="space-y-2">
                        <Label htmlFor="tipoCertificado">Tipo de Certificado</Label>
                        <Input
                          id="tipoCertificado"
                          value={certificacionTipo}
                          onChange={(e) => setCertificacionTipo(e.target.value)}
                          placeholder="Ej: Certificado Profesional"
                        />
                      </div>

                      <div className="flex items-center gap-2">
                        <Checkbox
                          id="certificadoActivo"
                          checked={certificacionActiva}
                          onCheckedChange={(checked) => setCertificacionActiva(checked as boolean)}
                        />
                        <Label htmlFor="certificadoActivo" className="cursor-pointer">
                          Certificado activo (disponible para descarga)
                        </Label>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Información del Patrón */}
          <Card className="border-amber-200 bg-amber-50 dark:bg-amber-900/20">
            <CardContent className="pt-4 pb-4">
              <h4 className="font-semibold text-amber-700 dark:text-amber-300 mb-2">
                Patrón Decorator
              </h4>
              <p className="text-sm text-amber-600 dark:text-amber-400">
                Este patrón permite añadir nuevas funcionalidades al módulo de forma dinámica, sin
                modificar su estructura original. Los decoradores se pueden combinar libremente.
              </p>
            </CardContent>
          </Card>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={handleClose} disabled={loading}>
            <X className="w-4 h-4 mr-2" />
            Cancelar
          </Button>
          <Button onClick={handleApplyDecorators} disabled={loading || preview !== null}>
            {loading ? (
              <>
                <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                Aplicando...
              </>
            ) : (
              <>
                <Sparkles className="w-4 h-4 mr-2" />
                Aplicar Decoradores
              </>
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
