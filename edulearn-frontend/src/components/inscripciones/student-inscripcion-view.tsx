"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"
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
  }, [])

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
        payload.monto = Number.parseFloat(formData.monto)
        if (formData.codigoDescuento) {
          payload.codigoDescuento = formData.codigoDescuento
        }
      } else if (formData.tipoInscripcion === "BECA") {
        payload.tipoBeca = formData.tipoBeca
        payload.codigoBeca = formData.codigoBeca
        payload.porcentajeBeca = Number.parseInt(formData.porcentajeBeca)
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
        <TabsList className="grid w-full max-w-md grid-cols-2 mb-8">
          <TabsTrigger value="nueva" className="text-base">
            Nueva Inscripción
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
                        <p className="text-sm text-muted-foreground mt-2">{cursoSeleccionado.descripcion}</p>
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
                              type="number"
                              step="0.01"
                              placeholder="500.00"
                              value={formData.monto}
                              onChange={(e) => setFormData({ ...formData, monto: e.target.value })}
                              className="h-10 bg-background border-input"
                              required
                            />
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
                                <SelectItem value="ACADEMICA">Académica</SelectItem>
                                <SelectItem value="DEPORTIVA">Deportiva</SelectItem>
                                <SelectItem value="SOCIAL">Social</SelectItem>
                              </SelectContent>
                            </Select>
                          </div>
                          <div className="space-y-2">
                            <Label htmlFor="porcentajeBeca" className="text-sm font-medium">
                              Porcentaje de Beca
                            </Label>
                            <Input
                              id="porcentajeBeca"
                              type="number"
                              min="0"
                              max="100"
                              placeholder="100"
                              value={formData.porcentajeBeca}
                              onChange={(e) => setFormData({ ...formData, porcentajeBeca: e.target.value })}
                              className="h-10 bg-background border-input"
                              required
                            />
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
                            disabled={procesando || !formData.aceptaTerminos}
                            className="flex-1 h-11 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold"
                          >
                            {procesando ? (
                              <>
                                <Clock className="h-4 w-4 mr-2 animate-spin" />
                                Procesando...
                              </>
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
                        <CardDescription className="text-red-700">No se pudo procesar tu inscripción</CardDescription>
                      </div>
                    </>
                  )}
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {resultado.mensaje && <p className="text-sm text-foreground">{resultado.mensaje}</p>}
                <Button onClick={() => setActiveTab("nueva")} className="w-full bg-primary hover:bg-primary/90">
                  Realizar Otra Inscripción
                </Button>
              </CardContent>
            </Card>
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
