"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { CheckCircle2, XCircle, Clock, Award, CreditCard, BookOpen, AlertCircle } from "lucide-react"

interface Curso {
  id: number
  nombre: string
  descripcion: string
  cupo_maximo: number
  precio?: number
}

interface TipoInscripcion {
  tipo: string
  nombre: string
  descripcion: string
}

export default function StudentInscripcionView() {
  const [activeTab, setActiveTab] = useState("nueva")
  const [estudiante, setEstudiante] = useState<any>(null)
  const [cursosDisponibles, setCursosDisponibles] = useState<Curso[]>([])
  const [tiposInscripcion, setTiposInscripcion] = useState<TipoInscripcion[]>([])
  const [cursoSeleccionado, setCursoSeleccionado] = useState<Curso | null>(null)

  const [tipoSeleccionado, setTipoSeleccionado] = useState("")
  const [formData, setFormData] = useState({
    cursoId: "",
    tipoInscripcion: "",
    aceptaTerminos: false,
    metodoPago: "",
    numeroTarjeta: "",
    monto: "",
    codigoDescuento: "",
    tipoBeca: "",
    codigoBeca: "",
    porcentajeBeca: "",
    documentoSoporte: "",
  })

  const [procesando, setProcesando] = useState(false)
  const [resultado, setResultado] = useState<any>(null)
  const [misInscripciones, setMisInscripciones] = useState<any[]>([])
  const [todasSolicitudes, setTodasSolicitudes] = useState<any[]>([])

  useEffect(() => {
    const usuario = localStorage.getItem("usuario")
    if (usuario) {
      const user = JSON.parse(usuario)
      setEstudiante(user)
    }
  }, [])

  useEffect(() => {
    fetchCursosDisponibles()
    fetchTiposInscripcion()
    if (estudiante) {
      fetchMisInscripciones()
      fetchTodasSolicitudes()
    }
  }, [estudiante])

  const fetchCursosDisponibles = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/inscripciones/proceso/cursos-disponibles")
      if (!response.ok) throw new Error("Error al obtener cursos")
      const data = await response.json()
      setCursosDisponibles(Array.isArray(data) ? data : data.cursos || [])
    } catch (error) {
      console.error("Error al cargar cursos:", error)
      setCursosDisponibles([])
    }
  }

  const fetchTiposInscripcion = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/inscripciones/proceso/tipos")
      const data = await response.json()
      setTiposInscripcion(data)
    } catch (error) {
      console.error("Error al cargar tipos:", error)
    }
  }

  const fetchMisInscripciones = async () => {
    if (!estudiante?.id) return
    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/estudiante/${estudiante.id}`)
      if (!response.ok) return
      const data = await response.json()
      setMisInscripciones(Array.isArray(data) ? data : [])
    } catch (error) {
      console.error("Error al cargar inscripciones:", error)
      setMisInscripciones([])
    }
  }

  const fetchTodasSolicitudes = async () => {
    if (!estudiante?.id) return
    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/estudiante/${estudiante.id}/todas`)
      if (!response.ok) return
      const data = await response.json()
      setTodasSolicitudes(Array.isArray(data) ? data : [])
    } catch (error) {
      console.error("Error al cargar todas las solicitudes:", error)
      setTodasSolicitudes([])
    }
  }

  const handleCursoChange = (cursoId: string) => {
    const curso = cursosDisponibles.find((c) => c.id === Number.parseInt(cursoId))
    setCursoSeleccionado(curso || null)
    setFormData({ ...formData, cursoId })
  }

  const handleTipoChange = (tipo: string) => {
    setTipoSeleccionado(tipo)
    setFormData({ ...formData, tipoInscripcion: tipo })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!estudiante) {
      alert("No se pudo identificar al estudiante")
      return
    }

    // Verificar si ya tiene una solicitud para este curso (activa, pendiente o rechazada)
    const solicitudExistente = todasSolicitudes.find(
      (solicitud) => solicitud.cursoId === Number.parseInt(formData.cursoId)
    )

    if (solicitudExistente) {
      let mensaje = ""
      if (solicitudExistente.estadoInscripcion === "Activa") {
        mensaje = "Ya estás inscrito en este curso. Puedes ver el curso en 'Mis Cursos'."
      } else if (solicitudExistente.estadoInscripcion === "Pendiente de Aprobación/Documentación") {
        mensaje = "Ya tienes una solicitud de beca pendiente para este curso. Revisa 'Mis Solicitudes' para ver el estado."
      } else if (solicitudExistente.estadoInscripcion === "Rechazada") {
        mensaje = `Tu solicitud anterior fue rechazada. Motivo: ${solicitudExistente.motivoRechazo || "No especificado"}. Contacta al administrador si deseas inscribirte.`
      }

      setResultado({
        exitoso: false,
        mensaje: mensaje,
      })
      setActiveTab("resultado")
      return
    }

    setProcesando(true)
    setResultado(null)

    try {
      const payload: any = {
        estudianteId: estudiante.id || 4,
        cursoId: Number.parseInt(formData.cursoId),
        tipoInscripcion: formData.tipoInscripcion,
        aceptaTerminos: formData.aceptaTerminos,
      }

      if (formData.tipoInscripcion === "PAGA") {
        payload.metodoPago = formData.metodoPago
        payload.numeroTarjeta = formData.numeroTarjeta
        payload.monto = 500.00 // Monto fijo de 500 pesos
        if (formData.codigoDescuento) {
          payload.codigoDescuento = formData.codigoDescuento
        }
      } else if (formData.tipoInscripcion === "BECA") {
        payload.tipoBeca = formData.tipoBeca
        payload.codigoBeca = formData.codigoBeca
        payload.porcentajeBeca = 100 // Siempre 100% por convenio institucional
        if (formData.documentoSoporte) {
          payload.documentoSoporte = formData.documentoSoporte
        }
      }

      const response = await fetch("http://localhost:8080/api/inscripciones/proceso", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      })

      const data = await response.json()
      setResultado(data)

      if (data.exitoso) {
        setActiveTab("resultado")
        // Recargar las inscripciones del estudiante
        await fetchMisInscripciones()
        await fetchTodasSolicitudes()

        // Mostrar mensaje de éxito y recargar después de 3 segundos
        setTimeout(() => {
          if (data.detalles?.estadoInscripcion === "Activa") {
            alert("¡Inscripción exitosa! Recarga la página para ver el curso en 'Mis Cursos'")
          } else if (data.detalles?.estadoInscripcion === "Pendiente de Aprobación/Documentación") {
            alert("¡Solicitud enviada! Tu beca está pendiente de aprobación del administrador. Te notificaremos cuando sea aprobada.")
          }
          window.location.href = "/dashboard"
        }, 2000)
      }
    } catch (error) {
      console.error("Error al procesar inscripción:", error)
      alert("Error al procesar la inscripción. Por favor, intenta de nuevo.")
    } finally {
      setProcesando(false)
    }
  }

  const resetForm = () => {
    setFormData({
      cursoId: "",
      tipoInscripcion: "",
      aceptaTerminos: false,
      metodoPago: "",
      numeroTarjeta: "",
      monto: "",
      codigoDescuento: "",
      tipoBeca: "",
      codigoBeca: "",
      porcentajeBeca: "",
      documentoSoporte: "",
    })
    setTipoSeleccionado("")
    setCursoSeleccionado(null)
    setResultado(null)
    setActiveTab("nueva")
  }

  const getIconoTipo = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case "GRATUITA":
        return <BookOpen className="h-5 w-5" />
      case "PAGA":
        return <CreditCard className="h-5 w-5" />
      case "BECA":
        return <Award className="h-5 w-5" />
      default:
        return <BookOpen className="h-5 w-5" />
    }
  }

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-foreground mb-2">Inscripciones a Cursos</h1>
        <p className="text-muted-foreground">Elige un curso y completa el formulario para inscribirte</p>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
        <TabsList className="grid w-full max-w-3xl grid-cols-3 mb-8">
          <TabsTrigger value="nueva" className="text-base">
            Nueva Inscripción
          </TabsTrigger>
          <TabsTrigger value="solicitudes" className="text-base">
            Mis Solicitudes
          </TabsTrigger>
          <TabsTrigger value="resultado" disabled={!resultado} className="text-base">
            Resultado
          </TabsTrigger>
        </TabsList>

        {/* Nueva Inscripción Tab */}
        <TabsContent value="nueva" className="space-y-8">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            {/* Main Form - 2 columns */}
            <div className="lg:col-span-2">
              <Card className="border-border/50 shadow-sm">
                <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-border/30">
                  <CardTitle className="text-xl">Formulario de Inscripción</CardTitle>
                  <CardDescription>Completa la información para inscribirte al curso</CardDescription>
                </CardHeader>
                <CardContent className="pt-8">
                  <form onSubmit={handleSubmit} className="space-y-8">
                    {estudiante && (
                      <Alert className="bg-primary/5 border-primary/30 rounded-lg">
                        <AlertCircle className="h-4 w-4 text-primary" />
                        <AlertTitle className="text-primary font-semibold">Estudiante Verificado</AlertTitle>
                        <AlertDescription className="text-primary/80 mt-1">
                          {estudiante.nombre || estudiante.username} (ID: {estudiante.id || "N/A"})
                        </AlertDescription>
                      </Alert>
                    )}

                    <Separator className="my-2" />

                    <div className="space-y-3">
                      <Label htmlFor="curso" className="text-base font-semibold text-foreground">
                        Selecciona un Curso
                      </Label>
                      <Select value={formData.cursoId} onValueChange={handleCursoChange} required>
                        <SelectTrigger className="h-11 bg-background border-input">
                          <SelectValue placeholder="Elige un curso disponible" />
                        </SelectTrigger>
                        <SelectContent>
                          {cursosDisponibles && cursosDisponibles.length > 0 ? (
                            cursosDisponibles.map((curso) => (
                              <SelectItem key={curso.id} value={curso.id.toString()}>
                                <div className="flex items-center gap-2">
                                  <BookOpen className="h-4 w-4 text-muted-foreground" />
                                  {curso.nombre}
                                </div>
                              </SelectItem>
                            ))
                          ) : (
                            <SelectItem value="no-disponible" disabled>
                              No hay cursos disponibles
                            </SelectItem>
                          )}
                        </SelectContent>
                      </Select>
                      {cursoSeleccionado && (
                        <>
                          <p className="text-sm text-muted-foreground mt-2">{cursoSeleccionado.descripcion}</p>
                          {(() => {
                            const solicitudExistente = todasSolicitudes.find(
                              (s) => s.cursoId === cursoSeleccionado.id
                            )
                            if (!solicitudExistente) return null

                            if (solicitudExistente.estadoInscripcion === "Activa") {
                              return (
                                <Alert className="mt-3 bg-green-50 border-green-300">
                                  <CheckCircle2 className="h-4 w-4 text-green-600" />
                                  <AlertTitle className="text-green-800 font-semibold">
                                    Ya estás inscrito
                                  </AlertTitle>
                                  <AlertDescription className="text-green-700">
                                    Ya tienes una inscripción activa en este curso. Puedes verlo en "Mis Cursos".
                                  </AlertDescription>
                                </Alert>
                              )
                            } else if (
                              solicitudExistente.estadoInscripcion === "Pendiente de Aprobación/Documentación"
                            ) {
                              return (
                                <Alert className="mt-3 bg-yellow-50 border-yellow-300">
                                  <Clock className="h-4 w-4 text-yellow-600" />
                                  <AlertTitle className="text-yellow-800 font-semibold">
                                    Solicitud Pendiente
                                  </AlertTitle>
                                  <AlertDescription className="text-yellow-700">
                                    Ya tienes una solicitud de beca pendiente para este curso. Revisa "Mis
                                    Solicitudes" para ver el estado.
                                  </AlertDescription>
                                </Alert>
                              )
                            } else if (solicitudExistente.estadoInscripcion === "Rechazada") {
                              return (
                                <Alert className="mt-3 bg-red-50 border-red-300">
                                  <XCircle className="h-4 w-4 text-red-600" />
                                  <AlertTitle className="text-red-800 font-semibold">Solicitud Rechazada</AlertTitle>
                                  <AlertDescription className="text-red-700">
                                    Tu solicitud anterior fue rechazada.{" "}
                                    {solicitudExistente.motivoRechazo && (
                                      <>
                                        Motivo: <strong>{solicitudExistente.motivoRechazo}</strong>.{" "}
                                      </>
                                    )}
                                    Contacta al administrador si deseas inscribirte.
                                  </AlertDescription>
                                </Alert>
                              )
                            }
                            return null
                          })()}
                        </>
                      )}
                    </div>

                    <div className="space-y-3">
                      <Label className="text-base font-semibold text-foreground">Tipo de Inscripción</Label>
                      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {tiposInscripcion.map((tipo) => (
                          <Card
                            key={tipo.tipo}
                            className={`cursor-pointer transition-all border-2 ${
                              tipoSeleccionado === tipo.tipo
                                ? "border-primary bg-primary/5 shadow-md"
                                : "border-border/50 hover:border-primary/50 hover:shadow-sm"
                            }`}
                            onClick={() => handleTipoChange(tipo.tipo)}
                          >
                            <CardHeader className="pb-3">
                              <div className="flex items-start justify-between gap-2">
                                <div className="flex items-center gap-2">
                                  <div className="p-2 bg-primary/10 rounded-lg text-primary">
                                    {getIconoTipo(tipo.tipo)}
                                  </div>
                                  <div>
                                    <CardTitle className="text-sm font-semibold">{tipo.nombre}</CardTitle>
                                    <CardDescription className="text-xs mt-1">{tipo.descripcion}</CardDescription>
                                  </div>
                                </div>
                                {tipoSeleccionado === tipo.tipo && (
                                  <CheckCircle2 className="h-5 w-5 text-primary flex-shrink-0" />
                                )}
                              </div>
                            </CardHeader>
                          </Card>
                        ))}
                      </div>
                    </div>

                    {tipoSeleccionado === "PAGA" && (
                      <div className="space-y-4 p-4 bg-secondary/30 rounded-lg border border-border/50">
                        <h3 className="font-semibold text-foreground flex items-center gap-2">
                          <CreditCard className="h-5 w-5 text-primary" />
                          Información de Pago
                        </h3>
                        <div className="grid grid-cols-2 gap-4">
                          <div className="space-y-2">
                            <Label htmlFor="metodoPago" className="text-sm font-medium">
                              Método de Pago
                            </Label>
                            <Select
                              value={formData.metodoPago}
                              onValueChange={(value) => setFormData({ ...formData, metodoPago: value })}
                              required
                            >
                              <SelectTrigger className="h-10 bg-background border-input">
                                <SelectValue placeholder="Selecciona" />
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value="TARJETA">Tarjeta de Crédito</SelectItem>
                                <SelectItem value="PSE">PSE</SelectItem>
                                <SelectItem value="EFECTIVO">Efectivo</SelectItem>
                              </SelectContent>
                            </Select>
                          </div>
                          <div className="space-y-2">
                            <Label htmlFor="monto" className="text-sm font-medium">
                              Monto
                            </Label>
                            <Input
                              id="monto"
                              type="text"
                              value="500.00 MXN"
                              readOnly
                              disabled
                              className="h-10 bg-muted border-input"
                            />
                            <p className="text-xs text-muted-foreground">
                              Monto fijo: 500 pesos para todos los cursos
                            </p>
                          </div>
                          <div className="col-span-2 space-y-2">
                            <Label htmlFor="numeroTarjeta" className="text-sm font-medium">
                              Número de Tarjeta
                            </Label>
                            <Input
                              id="numeroTarjeta"
                              type="text"
                              placeholder="4111111111111111"
                              value={formData.numeroTarjeta}
                              onChange={(e) => setFormData({ ...formData, numeroTarjeta: e.target.value })}
                              className="h-10 bg-background border-input"
                              required
                            />
                          </div>
                          <div className="col-span-2 space-y-2">
                            <Label htmlFor="codigoDescuento" className="text-sm font-medium">
                              Código de Descuento (Opcional)
                            </Label>
                            <Input
                              id="codigoDescuento"
                              type="text"
                              placeholder="PROMO10"
                              value={formData.codigoDescuento}
                              onChange={(e) => setFormData({ ...formData, codigoDescuento: e.target.value })}
                              className="h-10 bg-background border-input"
                            />
                          </div>
                        </div>
                      </div>
                    )}

                    {tipoSeleccionado === "BECA" && (
                      <div className="space-y-4 p-4 bg-secondary/30 rounded-lg border border-border/50">
                        <h3 className="font-semibold text-foreground flex items-center gap-2">
                          <Award className="h-5 w-5 text-primary" />
                          Información de Beca
                        </h3>
                        <div className="grid grid-cols-2 gap-4">
                          <div className="space-y-2">
                            <Label htmlFor="tipoBeca" className="text-sm font-medium">
                              Tipo de Beca
                            </Label>
                            <Select
                              value={formData.tipoBeca}
                              onValueChange={(value) => setFormData({ ...formData, tipoBeca: value })}
                              required
                            >
                              <SelectTrigger className="h-10 bg-background border-input">
                                <SelectValue placeholder="Selecciona" />
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value="TECNM">TECNM (Convenio)</SelectItem>
                                <SelectItem value="UNAM">UNAM (Convenio)</SelectItem>
                                <SelectItem value="IPN">IPN (Convenio)</SelectItem>
                                
                                
                                
                                
                              </SelectContent>
                            </Select>
                          </div>
                          <div className="space-y-2">
                            <Label htmlFor="porcentajeBeca" className="text-sm font-medium">
                              Porcentaje de Beca
                            </Label>
                            <Input
                              id="porcentajeBeca"
                              type="text"
                              value="100%"
                              readOnly
                              disabled
                              className="h-10 bg-muted border-input"
                            />
                            <p className="text-xs text-muted-foreground">
                              Todas las becas son del 100% por convenio institucional
                            </p>
                          </div>
                          <div className="col-span-2 space-y-2">
                            <Label htmlFor="codigoBeca" className="text-sm font-medium">
                              Código de Beca
                            </Label>
                            <Input
                              id="codigoBeca"
                              type="text"
                              placeholder="BECA-2024-001"
                              value={formData.codigoBeca}
                              onChange={(e) => setFormData({ ...formData, codigoBeca: e.target.value })}
                              className="h-10 bg-background border-input"
                              required
                            />
                          </div>
                          <div className="col-span-2 space-y-2">
                            <Label htmlFor="documentoSoporte" className="text-sm font-medium">
                              Documento de Soporte (Opcional)
                            </Label>
                            <Input
                              id="documentoSoporte"
                              type="text"
                              placeholder="URL o ruta del documento"
                              value={formData.documentoSoporte}
                              onChange={(e) => setFormData({ ...formData, documentoSoporte: e.target.value })}
                              className="h-10 bg-background border-input"
                            />
                          </div>
                        </div>
                      </div>
                    )}

                    {tipoSeleccionado && (
                      <div className="space-y-4 border-t border-border/50 pt-4">
                        <div className="flex items-start gap-3 p-3 bg-secondary/20 rounded-lg">
                          <input
                            type="checkbox"
                            id="aceptaTerminos"
                            checked={formData.aceptaTerminos}
                            onChange={(e) => setFormData({ ...formData, aceptaTerminos: e.target.checked })}
                            required
                            className="rounded mt-1 cursor-pointer"
                          />
                          <Label htmlFor="aceptaTerminos" className="cursor-pointer text-sm flex-1">
                            Acepto los <span className="font-semibold text-primary">términos y condiciones</span> de
                            inscripción en el curso
                          </Label>
                        </div>

                        <div className="flex gap-3 pt-4">
                          <Button
                            type="submit"
                            disabled={
                              procesando ||
                              !formData.cursoId ||
                              !formData.tipoInscripcion ||
                              !formData.aceptaTerminos ||
                              misInscripciones.some((i) => i.cursoId === Number.parseInt(formData.cursoId))
                            }
                            className="flex-1 h-11 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold"
                          >
                            {procesando ? (
                              <>
                                <Clock className="h-4 w-4 mr-2 animate-spin" />
                                Procesando...
                              </>
                            ) : !formData.cursoId ? (
                              "Selecciona un curso"
                            ) : !formData.tipoInscripcion ? (
                              "Selecciona tipo de inscripción"
                            ) : misInscripciones.some((i) => i.cursoId === Number.parseInt(formData.cursoId)) ? (
                              "Ya inscrito en este curso"
                            ) : (
                              "✓ Inscribirse Ahora"
                            )}
                          </Button>
                          <Button type="button" variant="outline" className="h-11 bg-transparent" onClick={resetForm}>
                            Limpiar
                          </Button>
                        </div>
                      </div>
                    )}
                  </form>
                </CardContent>
              </Card>
            </div>

            {/* Sidebar - Información del Curso Seleccionado */}
            {cursoSeleccionado && (
              <div className="lg:col-span-1">
                <Card className="border-border/50 shadow-sm sticky top-8">
                  <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-border/30">
                    <CardTitle className="text-lg">Detalles del Curso</CardTitle>
                  </CardHeader>
                  <CardContent className="pt-6 space-y-6">
                    <div>
                      <Label className="text-xs font-semibold text-muted-foreground uppercase">Nombre del Curso</Label>
                      <p className="font-semibold text-foreground mt-2">{cursoSeleccionado.nombre}</p>
                    </div>

                    <Separator />

                    <div>
                      <Label className="text-xs font-semibold text-muted-foreground uppercase">Descripción</Label>
                      <p className="text-sm text-foreground mt-2">{cursoSeleccionado.descripcion}</p>
                    </div>

                    <Separator />

                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <Label className="text-xs font-semibold text-muted-foreground uppercase">Cupo</Label>
                        <p className="font-semibold text-foreground mt-2 text-lg">{cursoSeleccionado.cupo_maximo}</p>
                      </div>
                      {cursoSeleccionado.precio && (
                        <div>
                          <Label className="text-xs font-semibold text-muted-foreground uppercase">Precio</Label>
                          <p className="font-semibold text-primary mt-2 text-lg">
                            ${cursoSeleccionado.precio.toFixed(2)}
                          </p>
                        </div>
                      )}
                    </div>

                    <Separator />

                    <div className="p-3 bg-primary/5 rounded-lg border border-primary/20">
                      <p className="text-sm text-foreground">
                        <span className="font-semibold text-primary">Tip:</span> Revisa bien los requisitos del tipo de
                        inscripción antes de confirmar.
                      </p>
                    </div>
                  </CardContent>
                </Card>
              </div>
            )}
          </div>
        </TabsContent>

        {/* Mis Solicitudes Tab */}
        <TabsContent value="solicitudes" className="space-y-6">
          <Card className="border-border/50 shadow-sm">
            <CardHeader className="bg-gradient-to-r from-primary/5 to-primary/10 border-b border-border/30">
              <CardTitle className="text-xl">Estado de Mis Solicitudes</CardTitle>
              <CardDescription>
                Revisa el estado de todas tus solicitudes de inscripción (activas, pendientes y rechazadas)
              </CardDescription>
            </CardHeader>
            <CardContent className="pt-6">
              {todasSolicitudes.length === 0 ? (
                <div className="text-center py-12">
                  <AlertCircle className="h-16 w-16 mx-auto text-muted-foreground mb-4" />
                  <h3 className="text-lg font-semibold mb-2">No tienes solicitudes</h3>
                  <p className="text-muted-foreground mb-4">
                    Aún no te has inscrito a ningún curso
                  </p>
                  <Button onClick={() => setActiveTab("nueva")}>Inscribirse a un curso</Button>
                </div>
              ) : (
                <div className="space-y-4">
                  {todasSolicitudes.map((solicitud) => (
                    <Card
                      key={solicitud.id}
                      className={`border-2 ${
                        solicitud.estadoInscripcion === "Activa"
                          ? "border-green-500/30 bg-green-50/30"
                          : solicitud.estadoInscripcion === "Pendiente de Aprobación/Documentación"
                            ? "border-yellow-500/30 bg-yellow-50/30"
                            : solicitud.estadoInscripcion === "Rechazada"
                              ? "border-red-500/30 bg-red-50/30"
                              : "border-border/50"
                      }`}
                    >
                      <CardHeader className="pb-3">
                        <div className="flex items-start justify-between">
                          <div>
                            <CardTitle className="text-lg flex items-center gap-2">
                              <BookOpen className="h-5 w-5" />
                              {solicitud.cursoNombre}
                            </CardTitle>
                            <CardDescription>Código: {solicitud.cursoCodigo}</CardDescription>
                          </div>
                          <div className="flex flex-col items-end gap-2">
                            <Badge
                              className={
                                solicitud.estadoInscripcion === "Activa"
                                  ? "bg-green-100 text-green-800 border-green-300"
                                  : solicitud.estadoInscripcion === "Pendiente de Aprobación/Documentación"
                                    ? "bg-yellow-100 text-yellow-800 border-yellow-300"
                                    : solicitud.estadoInscripcion === "Rechazada"
                                      ? "bg-red-100 text-red-800 border-red-300"
                                      : ""
                              }
                            >
                              {solicitud.estadoInscripcion === "Activa" && <CheckCircle2 className="h-3 w-3 mr-1" />}
                              {solicitud.estadoInscripcion === "Pendiente de Aprobación/Documentación" && (
                                <Clock className="h-3 w-3 mr-1" />
                              )}
                              {solicitud.estadoInscripcion === "Rechazada" && <XCircle className="h-3 w-3 mr-1" />}
                              {solicitud.estadoInscripcion}
                            </Badge>
                            <Badge variant="outline">{solicitud.modalidad}</Badge>
                          </div>
                        </div>
                      </CardHeader>
                      <CardContent>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                          <div>
                            <Label className="text-xs text-muted-foreground">Fecha de Solicitud</Label>
                            <p className="text-sm font-medium">
                              {new Date(solicitud.fechaInscripcion).toLocaleDateString("es-MX")}
                            </p>
                          </div>
                          <div>
                            <Label className="text-xs text-muted-foreground">Modalidad</Label>
                            <p className="text-sm font-medium">{solicitud.modalidad}</p>
                          </div>
                          {solicitud.modalidad === "BECA" && solicitud.tipoBeca && (
                            <div>
                              <Label className="text-xs text-muted-foreground">Tipo de Beca</Label>
                              <p className="text-sm font-medium">{solicitud.tipoBeca}</p>
                            </div>
                          )}
                          {solicitud.certificadoGarantizado && (
                            <div>
                              <Label className="text-xs text-muted-foreground">Beneficio</Label>
                              <p className="text-sm font-medium text-green-600">Certificado Garantizado</p>
                            </div>
                          )}
                        </div>

                        {/* Mensaje específico según el estado */}
                        {solicitud.estadoInscripcion === "Pendiente de Aprobación/Documentación" && (
                          <Alert className="mt-4 bg-yellow-50 border-yellow-300">
                            <Clock className="h-4 w-4 text-yellow-600" />
                            <AlertTitle className="text-yellow-800 font-semibold">Beca Pendiente</AlertTitle>
                            <AlertDescription className="text-yellow-700">
                              Tu solicitud de beca está siendo revisada por el administrador. Te notificaremos cuando
                              sea aprobada o rechazada.
                            </AlertDescription>
                          </Alert>
                        )}

                        {solicitud.estadoInscripcion === "Rechazada" && (
                          <Alert variant="destructive" className="mt-4">
                            <XCircle className="h-4 w-4" />
                            <AlertTitle>Solicitud Rechazada</AlertTitle>
                            <AlertDescription>
                              {solicitud.motivoRechazo ? (
                                <>
                                  <strong>Motivo:</strong> {solicitud.motivoRechazo}
                                </>
                              ) : (
                                "Tu solicitud de beca fue rechazada. Contacta al administrador para más información."
                              )}
                            </AlertDescription>
                          </Alert>
                        )}

                        {solicitud.estadoInscripcion === "Activa" && (
                          <Alert className="mt-4 bg-green-50 border-green-300">
                            <CheckCircle2 className="h-4 w-4 text-green-600" />
                            <AlertTitle className="text-green-800 font-semibold">Inscripción Activa</AlertTitle>
                            <AlertDescription className="text-green-700">
                              Ya puedes acceder al curso desde "Mis Cursos" en el menú principal.
                            </AlertDescription>
                          </Alert>
                        )}
                      </CardContent>
                    </Card>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* Resultado Tab */}
        <TabsContent value="resultado" className="space-y-6">
          {resultado && (
            <Card
              className={`border-2 ${resultado.exitoso ? "border-green-500/50 bg-green-50/50" : "border-red-500/50 bg-red-50/50"}`}
            >
              <CardHeader className="pb-3">
                <div className="flex items-center gap-3">
                  {resultado.exitoso ? (
                    <>
                      <div className="p-2 bg-green-100 rounded-full">
                        <CheckCircle2 className="h-6 w-6 text-green-600" />
                      </div>
                      <div>
                        <CardTitle className="text-green-900">Inscripción Exitosa</CardTitle>
                        <CardDescription className="text-green-700">
                          Tu inscripción ha sido procesada correctamente
                        </CardDescription>
                      </div>
                    </>
                  ) : (
                    <>
                      <div className="p-2 bg-red-100 rounded-full">
                        <XCircle className="h-6 w-6 text-red-600" />
                      </div>
                      <div>
                        <CardTitle className="text-red-900">Error en la Inscripción</CardTitle>
                        <CardDescription className="text-red-700">
                          {resultado.mensaje || "No se pudo procesar tu inscripción"}
                        </CardDescription>
                      </div>
                    </>
                  )}
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {resultado.exitoso && resultado.detalles && (
                  <div className="bg-muted/50 p-4 rounded-lg space-y-2">
                    <p className="text-sm font-semibold">Detalles de la inscripción:</p>
                    {resultado.detalles.inscripcionId && (
                      <p className="text-sm">
                        <span className="font-medium">ID:</span> {resultado.detalles.inscripcionId}
                      </p>
                    )}
                    {resultado.detalles.modalidad && (
                      <p className="text-sm">
                        <span className="font-medium">Modalidad:</span> {resultado.detalles.modalidad}
                      </p>
                    )}
                    {resultado.detalles.estadoInscripcion && (
                      <p className="text-sm">
                        <span className="font-medium">Estado:</span> {resultado.detalles.estadoInscripcion}
                      </p>
                    )}
                    {resultado.detalles.certificadoGarantizado === "true" && (
                      <p className="text-sm text-green-600 font-medium">
                        ✓ Certificado garantizado al finalizar el curso
                      </p>
                    )}
                  </div>
                )}

                <div className="flex gap-3">
                  <Button
                    onClick={() => window.location.href = "/dashboard"}
                    className="flex-1 bg-primary hover:bg-primary/90"
                  >
                    Ir a Mis Cursos
                  </Button>
                  <Button
                    onClick={() => setActiveTab("nueva")}
                    variant="outline"
                    className="flex-1"
                  >
                    Nueva Inscripción
                  </Button>
                </div>
              </CardContent>
            </Card>
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
