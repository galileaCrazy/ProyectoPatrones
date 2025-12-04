"use client"

import { useEffect, useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Trophy, Award, Lock, Download, Loader2, AlertCircle } from "lucide-react"

interface CourseRewardsBannerProps {
  cursoId: string
  estudianteId: number
}

interface DecoradorInfo {
  cursoId: number
  modalidadInscripcion: string
  puntosDisponibles: number
  puntosObtenidos: number
  badgesDisponibles: Array<{
    nombre: string
    moduloNombre: string
    obtenido: boolean
  }>
  badgesObtenidos: string[]
  certificadoDisponible: boolean
  tipoCertificado: string
  puedeDescargarCertificado: boolean
  mensajeCertificado: string
  modulosCompletados: number
  totalModulos: number
  cursoCompletado: boolean
}

export function CourseRewardsBanner({ cursoId, estudianteId }: CourseRewardsBannerProps) {
  const [decoradores, setDecoradores] = useState<DecoradorInfo | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    cargarDecoradores()
  }, [cursoId, estudianteId])

  const cargarDecoradores = async () => {
    try {
      setLoading(true)
      setError(null)

      const url = `http://localhost:8080/api/modulos/curso/${cursoId}/estudiante/${estudianteId}/decoradores`
      console.log("=== CARGANDO DECORADORES ===")
      console.log("URL:", url)
      console.log("Curso ID:", cursoId)
      console.log("Estudiante ID:", estudianteId)

      const response = await fetch(url)

      console.log("Response status:", response.status)
      console.log("Response OK:", response.ok)

      if (!response.ok) {
        const errorText = await response.text()
        console.error("Error del backend:", errorText)
        throw new Error(`Error ${response.status}: ${errorText || "Error al cargar información de recompensas"}`)
      }

      const data = await response.json()
      console.log("Decoradores cargados:", data)
      setDecoradores(data)
    } catch (err) {
      console.error("Error al cargar decoradores:", err)
      setError(err instanceof Error ? err.message : "Error desconocido")
    } finally {
      setLoading(false)
    }
  }

  const handleDescargarCertificado = () => {
    // TODO: Implementar descarga de certificado
    alert("Función de descarga de certificado en desarrollo")
  }

  if (loading) {
    return (
      <Card className="mb-6 border-primary/20">
        <CardContent className="py-6 flex items-center justify-center gap-3">
          <Loader2 className="w-5 h-5 animate-spin text-primary" />
          <span className="text-muted-foreground">Cargando recompensas del curso...</span>
        </CardContent>
      </Card>
    )
  }

  if (error || !decoradores) {
    return null // No mostrar nada si hay error
  }

  // Si no hay gamificación ni certificación, no mostrar el banner
  const tieneGamificacion = decoradores.puntosDisponibles > 0 || decoradores.badgesDisponibles.length > 0
  const tieneCertificacion = decoradores.certificadoDisponible

  if (!tieneGamificacion && !tieneCertificacion) {
    return null
  }

  return (
    <Card className="mb-6 border-primary/30 bg-gradient-to-r from-primary/5 via-purple-50/50 to-blue-50/50 dark:from-primary/10 dark:via-purple-900/20 dark:to-blue-900/20">
      <CardContent className="py-6">
        <div className="grid md:grid-cols-2 gap-6">
          {/* Gamificación */}
          {tieneGamificacion && (
            <div className="space-y-4">
              <div className="flex items-center gap-3">
                <div className="p-2 rounded-lg bg-amber-500/10">
                  <Trophy className="w-6 h-6 text-amber-500" />
                </div>
                <div>
                  <h3 className="font-semibold text-foreground">Gamificación</h3>
                  <p className="text-sm text-muted-foreground">
                    Gana puntos y badges completando módulos
                  </p>
                </div>
              </div>

              {/* Puntos */}
              {decoradores.puntosDisponibles > 0 && (
                <div className="flex items-center justify-between p-3 rounded-lg bg-white/50 dark:bg-gray-800/30 border border-amber-200 dark:border-amber-800">
                  <div>
                    <p className="text-sm font-medium text-foreground">Puntos Totales</p>
                    <p className="text-xs text-muted-foreground">
                      Completa todos los módulos
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="text-2xl font-bold text-amber-600 dark:text-amber-400">
                      {decoradores.puntosObtenidos}
                      <span className="text-sm text-muted-foreground">/{decoradores.puntosDisponibles}</span>
                    </p>
                    <p className="text-xs text-muted-foreground">pts</p>
                  </div>
                </div>
              )}

              {/* Badges */}
              {decoradores.badgesDisponibles.length > 0 && (
                <div>
                  <p className="text-sm font-medium text-foreground mb-2">
                    Badges Disponibles ({decoradores.badgesDisponibles.length})
                  </p>
                  <div className="flex flex-wrap gap-2">
                    {decoradores.badgesDisponibles.map((badge, index) => (
                      <Badge
                        key={index}
                        variant={badge.obtenido ? "default" : "outline"}
                        className={badge.obtenido ? "bg-amber-500 hover:bg-amber-600" : "opacity-50"}
                      >
                        {badge.obtenido ? "✓ " : "🔒 "}
                        {badge.nombre}
                      </Badge>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Certificación */}
          {tieneCertificacion && (
            <div className="space-y-4">
              <div className="flex items-center gap-3">
                <div className="p-2 rounded-lg bg-blue-500/10">
                  <Award className="w-6 h-6 text-blue-500" />
                </div>
                <div>
                  <h3 className="font-semibold text-foreground">Certificación</h3>
                  <p className="text-sm text-muted-foreground">
                    {decoradores.tipoCertificado || "Certificado de Finalización"}
                  </p>
                </div>
              </div>

              {/* Estado del Certificado */}
              <div className="p-4 rounded-lg bg-white/50 dark:bg-gray-800/30 border border-blue-200 dark:border-blue-800">
                {/* Progreso */}
                <div className="mb-4">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm font-medium text-foreground">Progreso del Curso</span>
                    <span className="text-sm text-muted-foreground">
                      {decoradores.modulosCompletados}/{decoradores.totalModulos} módulos
                    </span>
                  </div>
                  <div className="h-2 bg-gray-200 dark:bg-gray-700 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-blue-500 transition-all"
                      style={{
                        width: `${(decoradores.modulosCompletados / decoradores.totalModulos) * 100}%`,
                      }}
                    />
                  </div>
                </div>

                {/* Mensaje y Acción */}
                <div className="space-y-3">
                  {decoradores.puedeDescargarCertificado ? (
                    <>
                      {decoradores.cursoCompletado ? (
                        <>
                          <p className="text-sm text-green-600 dark:text-green-400 flex items-center gap-2">
                            <Award className="w-4 h-4" />
                            ¡Felicidades! Has completado el curso
                          </p>
                          <Button
                            onClick={handleDescargarCertificado}
                            className="w-full bg-blue-600 hover:bg-blue-700"
                          >
                            <Download className="w-4 h-4 mr-2" />
                            Descargar Certificado
                          </Button>
                        </>
                      ) : (
                        <p className="text-sm text-blue-600 dark:text-blue-400">
                          {decoradores.mensajeCertificado}
                        </p>
                      )}
                    </>
                  ) : (
                    <div className="space-y-2">
                      {decoradores.modalidadInscripcion === "GRATUITA" ? (
                        <>
                          <p className="text-sm text-amber-600 dark:text-amber-400 flex items-center gap-2">
                            <Lock className="w-4 h-4" />
                            Certificado bloqueado (Inscripción gratuita)
                          </p>
                          <p className="text-xs text-muted-foreground">
                            {decoradores.mensajeCertificado}
                          </p>
                          <Button
                            variant="outline"
                            size="sm"
                            className="w-full border-amber-500 text-amber-600 hover:bg-amber-50 dark:hover:bg-amber-900/20"
                          >
                            Actualizar a Inscripción de Pago
                          </Button>
                        </>
                      ) : (
                        <p className="text-sm text-muted-foreground flex items-center gap-2">
                          <AlertCircle className="w-4 h-4" />
                          {decoradores.mensajeCertificado}
                        </p>
                      )}
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
