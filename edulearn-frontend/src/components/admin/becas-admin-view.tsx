"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { Separator } from "@/components/ui/separator"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { CheckCircle2, XCircle, Clock, Award, AlertCircle, User, BookOpen } from "lucide-react"

interface BecaPendiente {
  id: number
  estudianteId: number
  estudianteNombre: string
  estudianteMatricula: string
  estudianteEmail: string
  cursoId: number
  cursoNombre: string
  cursoCodigo: string
  tipoBeca: string
  codigoBeca: string
  fechaInscripcion: string
  estadoInscripcion: string
  certificadoGarantizado: boolean
}

export default function BecasAdminView() {
  const [becasPendientes, setBecasPendientes] = useState<BecaPendiente[]>([])
  const [loading, setLoading] = useState(true)
  const [procesando, setProcesando] = useState<number | null>(null)
  const [motivoRechazo, setMotivoRechazo] = useState<{ [key: number]: string }>({})
  const [resultado, setResultado] = useState<{ tipo: "success" | "error"; mensaje: string } | null>(null)

  useEffect(() => {
    fetchBecasPendientes()
  }, [])

  const fetchBecasPendientes = async () => {
    setLoading(true)
    try {
      const response = await fetch("http://localhost:8080/api/inscripciones/becas/pendientes")
      if (!response.ok) throw new Error("Error al obtener becas pendientes")
      const data = await response.json()
      setBecasPendientes(Array.isArray(data) ? data : [])
    } catch (error) {
      console.error("Error al cargar becas pendientes:", error)
      setBecasPendientes([])
    } finally {
      setLoading(false)
    }
  }

  const aprobarBeca = async (id: number) => {
    setProcesando(id)
    setResultado(null)
    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/becas/${id}/aprobar`, {
        method: "PUT",
      })

      if (!response.ok) throw new Error("Error al aprobar la beca")

      const data = await response.json()
      setResultado({
        tipo: "success",
        mensaje: data.mensaje || "Beca aprobada exitosamente",
      })

      // Recargar lista
      await fetchBecasPendientes()
    } catch (error) {
      console.error("Error al aprobar beca:", error)
      setResultado({
        tipo: "error",
        mensaje: "Error al aprobar la beca. Intenta de nuevo.",
      })
    } finally {
      setProcesando(null)
    }
  }

  const rechazarBeca = async (id: number) => {
    const motivo = motivoRechazo[id] || ""
    if (!motivo.trim()) {
      setResultado({
        tipo: "error",
        mensaje: "Debes especificar un motivo de rechazo",
      })
      return
    }

    setProcesando(id)
    setResultado(null)
    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/becas/${id}/rechazar`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ motivo }),
      })

      if (!response.ok) throw new Error("Error al rechazar la beca")

      const data = await response.json()
      setResultado({
        tipo: "success",
        mensaje: data.mensaje || "Beca rechazada",
      })

      // Limpiar motivo
      setMotivoRechazo((prev) => {
        const newState = { ...prev }
        delete newState[id]
        return newState
      })

      // Recargar lista
      await fetchBecasPendientes()
    } catch (error) {
      console.error("Error al rechazar beca:", error)
      setResultado({
        tipo: "error",
        mensaje: "Error al rechazar la beca. Intenta de nuevo.",
      })
    } finally {
      setProcesando(null)
    }
  }

  const getTipoBecaBadge = (tipo: string) => {
    const colores: { [key: string]: string } = {
      TECNM: "bg-blue-100 text-blue-800 border-blue-300",
      UNAM: "bg-purple-100 text-purple-800 border-purple-300",
      IPN: "bg-green-100 text-green-800 border-green-300",
    }
    return colores[tipo] || "bg-gray-100 text-gray-800 border-gray-300"
  }

  if (loading) {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-center py-12">
              <Clock className="h-8 w-8 animate-spin text-muted-foreground" />
              <span className="ml-3 text-muted-foreground">Cargando becas pendientes...</span>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-foreground mb-2">Administración de Becas</h1>
        <p className="text-muted-foreground">Revisa y aprueba las solicitudes de beca pendientes</p>
      </div>

      {resultado && (
        <Alert variant={resultado.tipo === "success" ? "default" : "destructive"}>
          {resultado.tipo === "success" ? (
            <CheckCircle2 className="h-4 w-4" />
          ) : (
            <XCircle className="h-4 w-4" />
          )}
          <AlertTitle>{resultado.tipo === "success" ? "Éxito" : "Error"}</AlertTitle>
          <AlertDescription>{resultado.mensaje}</AlertDescription>
        </Alert>
      )}

      {becasPendientes.length === 0 ? (
        <Card>
          <CardContent className="pt-6">
            <div className="text-center py-12">
              <Award className="h-16 w-16 mx-auto text-muted-foreground mb-4" />
              <h3 className="text-lg font-semibold mb-2">No hay becas pendientes</h3>
              <p className="text-muted-foreground">Todas las solicitudes de beca han sido procesadas</p>
            </div>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-6">
          {becasPendientes.map((beca) => (
            <Card key={beca.id} className="border-border/50 shadow-sm">
              <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-border/30">
                <div className="flex items-start justify-between">
                  <div className="space-y-1">
                    <div className="flex items-center gap-3">
                      <Award className="h-5 w-5 text-primary" />
                      <CardTitle className="text-lg">Solicitud de Beca #{beca.id}</CardTitle>
                      <Badge className={`${getTipoBecaBadge(beca.tipoBeca)} border`}>{beca.tipoBeca}</Badge>
                    </div>
                    <CardDescription>
                      Código de beca: <span className="font-mono font-semibold">{beca.codigoBeca}</span>
                    </CardDescription>
                  </div>
                  <Badge variant="outline" className="bg-yellow-50 text-yellow-700 border-yellow-300">
                    <Clock className="h-3 w-3 mr-1" />
                    Pendiente
                  </Badge>
                </div>
              </CardHeader>

              <CardContent className="pt-6 space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  {/* Información del Estudiante */}
                  <div className="space-y-4">
                    <div className="flex items-center gap-2 text-sm font-semibold text-foreground">
                      <User className="h-4 w-4 text-primary" />
                      Información del Estudiante
                    </div>
                    <Separator />
                    <div className="space-y-3">
                      <div>
                        <Label className="text-xs text-muted-foreground">Nombre Completo</Label>
                        <p className="font-medium text-foreground">{beca.estudianteNombre}</p>
                      </div>
                      <div>
                        <Label className="text-xs text-muted-foreground">Matrícula</Label>
                        <p className="font-mono text-sm">{beca.estudianteMatricula}</p>
                      </div>
                      <div>
                        <Label className="text-xs text-muted-foreground">Email</Label>
                        <p className="text-sm">{beca.estudianteEmail}</p>
                      </div>
                    </div>
                  </div>

                  {/* Información del Curso */}
                  <div className="space-y-4">
                    <div className="flex items-center gap-2 text-sm font-semibold text-foreground">
                      <BookOpen className="h-4 w-4 text-primary" />
                      Información del Curso
                    </div>
                    <Separator />
                    <div className="space-y-3">
                      <div>
                        <Label className="text-xs text-muted-foreground">Nombre del Curso</Label>
                        <p className="font-medium text-foreground">{beca.cursoNombre}</p>
                      </div>
                      <div>
                        <Label className="text-xs text-muted-foreground">Código</Label>
                        <p className="font-mono text-sm">{beca.cursoCodigo}</p>
                      </div>
                      <div>
                        <Label className="text-xs text-muted-foreground">Fecha de Solicitud</Label>
                        <p className="text-sm">{new Date(beca.fechaInscripcion).toLocaleDateString("es-MX")}</p>
                      </div>
                    </div>
                  </div>
                </div>

                <Separator />

                {/* Detalles de la Beca */}
                <div className="bg-secondary/30 p-4 rounded-lg space-y-3">
                  <div className="flex items-center gap-2 text-sm font-semibold text-foreground">
                    <Award className="h-4 w-4 text-primary" />
                    Detalles de la Beca
                  </div>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                    <div>
                      <Label className="text-xs text-muted-foreground">Tipo de Beca</Label>
                      <p className="font-medium">{beca.tipoBeca}</p>
                    </div>
                    <div>
                      <Label className="text-xs text-muted-foreground">Cobertura</Label>
                      <p className="font-medium text-green-600">100%</p>
                    </div>
                    <div>
                      <Label className="text-xs text-muted-foreground">Certificado Garantizado</Label>
                      <p className="font-medium">
                        {beca.certificadoGarantizado ? (
                          <span className="text-green-600">Sí</span>
                        ) : (
                          <span className="text-gray-600">No</span>
                        )}
                      </p>
                    </div>
                  </div>
                </div>

                <Separator />

                {/* Acciones */}
                <div className="space-y-4">
                  <div className="space-y-3">
                    <Label htmlFor={`motivo-${beca.id}`} className="text-sm font-medium">
                      Motivo de Rechazo (opcional si vas a aprobar)
                    </Label>
                    <Textarea
                      id={`motivo-${beca.id}`}
                      placeholder="Especifica el motivo del rechazo si decides rechazar la solicitud..."
                      value={motivoRechazo[beca.id] || ""}
                      onChange={(e) =>
                        setMotivoRechazo((prev) => ({
                          ...prev,
                          [beca.id]: e.target.value,
                        }))
                      }
                      className="min-h-20"
                      disabled={procesando === beca.id}
                    />
                  </div>

                  <div className="flex gap-3">
                    <Button
                      onClick={() => aprobarBeca(beca.id)}
                      disabled={procesando !== null}
                      className="flex-1 bg-green-600 hover:bg-green-700 text-white"
                    >
                      {procesando === beca.id ? (
                        <>
                          <Clock className="h-4 w-4 mr-2 animate-spin" />
                          Procesando...
                        </>
                      ) : (
                        <>
                          <CheckCircle2 className="h-4 w-4 mr-2" />
                          Aprobar Beca
                        </>
                      )}
                    </Button>
                    <Button
                      onClick={() => rechazarBeca(beca.id)}
                      disabled={procesando !== null}
                      variant="destructive"
                      className="flex-1"
                    >
                      {procesando === beca.id ? (
                        <>
                          <Clock className="h-4 w-4 mr-2 animate-spin" />
                          Procesando...
                        </>
                      ) : (
                        <>
                          <XCircle className="h-4 w-4 mr-2" />
                          Rechazar Beca
                        </>
                      )}
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
