'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Badge } from '@/components/ui/badge'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { Progress } from '@/components/ui/progress'
import { Separator } from '@/components/ui/separator'
import { CheckCircle2, XCircle, Clock, Info, FileText, CreditCard, Award } from 'lucide-react'

interface TipoInscripcion {
  tipo: string
  nombre: string
  descripcion: string
  icono: string
}

interface PasoInscripcion {
  nombre: string
  exitoso: boolean
  mensaje: string
  detalles?: Record<string, string>
  duracionMs?: number
}

interface ResultadoInscripcion {
  estudianteId: number
  cursoId: number
  tipoInscripcion: string
  exitoso: boolean
  estado: string
  mensaje: string
  pasos: PasoInscripcion[]
  fechaInicio: string
  fechaFin: string
  duracionTotalMs: number
  numeroInscripcion?: string
  montoTotal?: number
  descuentoAplicado?: number
  urlComprobante?: string
  urlRecibo?: string
}

interface Curso {
  id: number
  nombre: string
  descripcion: string
  cupo_maximo: number
  precio?: number
}

export default function TemplateMethodInscripcion() {
  const [activeTab, setActiveTab] = useState('demo')

  // Estado para los tipos de inscripción
  const [tiposInscripcion, setTiposInscripcion] = useState<TipoInscripcion[]>([])

  // Estado para el formulario de inscripción
  const [tipoSeleccionado, setTipoSeleccionado] = useState('')
  const [pasosTipo, setPasosTipo] = useState<string[]>([])
  const [cursosDisponibles, setCursosDisponibles] = useState<Curso[]>([])

  // Datos del formulario
  const [formData, setFormData] = useState({
    estudianteId: 0,
    cursoId: '',
    tipoInscripcion: '',
    aceptaTerminos: false,
    // Para inscripción paga
    metodoPago: '',
    numeroTarjeta: '',
    monto: '',
    codigoDescuento: '',
    // Para inscripción con beca
    tipoBeca: '',
    codigoBeca: '',
    porcentajeBeca: '',
    documentoSoporte: ''
  })

  // Estado para el resultado
  const [resultado, setResultado] = useState<ResultadoInscripcion | null>(null)
  const [procesando, setProcesando] = useState(false)

  // Estado para la demo
  const [demoInfo, setDemoInfo] = useState<any>(null)
  const [usuarioInvalido, setUsuarioInvalido] = useState(false)

  // Cargar tipos de inscripción al montar el componente
  useEffect(() => {
    fetchTiposInscripcion()
    fetchCursosDisponibles()
    fetchDemoInfo()

    // Obtener estudiante ID del usuario autenticado
    const usuarioStr = localStorage.getItem('usuario')
    if (usuarioStr) {
      const usuario = JSON.parse(usuarioStr)
      setFormData(prev => ({ ...prev, estudianteId: usuario.id }))

      // Validar que el estudiante existe en el backend
      fetch(`http://localhost:8080/api/estudiantes/${usuario.id}`)
        .then(response => {
          if (!response.ok) {
            setUsuarioInvalido(true)
            // Auto-logout después de 3 segundos si el usuario es inválido
            setTimeout(() => {
              localStorage.clear()
              window.location.href = '/'
            }, 3000)
          }
        })
        .catch(() => {
          setUsuarioInvalido(true)
          setTimeout(() => {
            localStorage.clear()
            window.location.href = '/'
          }, 3000)
        })
    } else {
      setUsuarioInvalido(true)
    }
  }, [])

  // Cargar pasos cuando se selecciona un tipo
  useEffect(() => {
    if (tipoSeleccionado) {
      fetchPasosPorTipo(tipoSeleccionado)
    }
  }, [tipoSeleccionado])

  const fetchTiposInscripcion = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/inscripciones/proceso/tipos')
      const data = await response.json()
      setTiposInscripcion(data)
    } catch (error) {
      console.error('Error al cargar tipos de inscripción:', error)
    }
  }

  const fetchPasosPorTipo = async (tipo: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/inscripciones/proceso/pasos/${tipo}`)
      const data = await response.json()
      setPasosTipo(data.pasos || [])
    } catch (error) {
      console.error('Error al cargar pasos:', error)
    }
  }

  const fetchCursosDisponibles = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/inscripciones/proceso/cursos-disponibles')
      const data = await response.json()
      setCursosDisponibles(data)
    } catch (error) {
      console.error('Error al cargar cursos:', error)
    }
  }

  const fetchDemoInfo = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/inscripciones/proceso/demo')
      const data = await response.json()
      setDemoInfo(data)
    } catch (error) {
      console.error('Error al cargar demo:', error)
    }
  }

  const handleTipoChange = (tipo: string) => {
    setTipoSeleccionado(tipo)
    setFormData({ ...formData, tipoInscripcion: tipo })
    setResultado(null)
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setProcesando(true)
    setResultado(null)

    try {
      const payload: any = {
        estudianteId: formData.estudianteId,
        cursoId: parseInt(formData.cursoId),
        tipoInscripcion: formData.tipoInscripcion,
        aceptaTerminos: formData.aceptaTerminos
      }

      // Agregar campos específicos según el tipo
      if (formData.tipoInscripcion === 'PAGA') {
        payload.metodoPago = formData.metodoPago
        payload.numeroTarjeta = formData.numeroTarjeta
        payload.monto = parseFloat(formData.monto)
        if (formData.codigoDescuento) {
          payload.codigoDescuento = formData.codigoDescuento
        }
      } else if (formData.tipoInscripcion === 'BECA') {
        payload.tipoBeca = formData.tipoBeca
        payload.codigoBeca = formData.codigoBeca
        payload.porcentajeBeca = parseInt(formData.porcentajeBeca)
        if (formData.documentoSoporte) {
          payload.documentoSoporte = formData.documentoSoporte
        }
      }

      const response = await fetch('http://localhost:8080/api/inscripciones/proceso', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })

      const data = await response.json()
      setResultado(data)
    } catch (error) {
      console.error('Error al procesar inscripción:', error)
    } finally {
      setProcesando(false)
    }
  }

  const getIconoTipo = (icono: string) => {
    switch (icono) {
      case 'gift':
        return <Award className="h-5 w-5" />
      case 'credit-card':
        return <CreditCard className="h-5 w-5" />
      case 'award':
        return <Award className="h-5 w-5" />
      default:
        return <FileText className="h-5 w-5" />
    }
  }

  const getIconoPaso = (exitoso: boolean) => {
    return exitoso ? (
      <CheckCircle2 className="h-5 w-5 text-green-600" />
    ) : (
      <XCircle className="h-5 w-5 text-red-600" />
    )
  }

  return (
    <div className="container mx-auto py-6 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Sistema de Inscripciones</h1>
          <p className="text-muted-foreground">Patrón de Diseño: Template Method</p>
        </div>
        <Badge variant="outline" className="text-sm">
          <Info className="h-4 w-4 mr-1" />
          Patrón Comportamiento
        </Badge>
      </div>

      {usuarioInvalido && (
        <Alert variant="destructive">
          <XCircle className="h-4 w-4" />
          <AlertTitle>Sesión no válida</AlertTitle>
          <AlertDescription>
            Tu usuario no está registrado como estudiante en el sistema.
            Serás redirigido al inicio de sesión en 3 segundos...
            <div className="mt-2">
              <Button
                onClick={() => {
                  localStorage.clear()
                  window.location.href = '/'
                }}
                variant="outline"
                size="sm"
              >
                Ir al Login Ahora
              </Button>
            </div>
          </AlertDescription>
        </Alert>
      )}

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="demo">
            <Info className="h-4 w-4 mr-2" />
            Demo del Patrón
          </TabsTrigger>
          <TabsTrigger value="inscripcion" disabled={usuarioInvalido}>
            <FileText className="h-4 w-4 mr-2" />
            Nueva Inscripción
          </TabsTrigger>
          <TabsTrigger value="resultado" disabled={!resultado}>
            <CheckCircle2 className="h-4 w-4 mr-2" />
            Resultado
          </TabsTrigger>
        </TabsList>

        {/* Tab: Demo del Patrón */}
        <TabsContent value="demo" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Template Method Pattern</CardTitle>
              <CardDescription>
                Define el esqueleto de un algoritmo en un método, delegando algunos pasos a las subclases
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {demoInfo && (
                <>
                  <Alert>
                    <Info className="h-4 w-4" />
                    <AlertTitle>Descripción del Patrón</AlertTitle>
                    <AlertDescription>{demoInfo.descripcion}</AlertDescription>
                  </Alert>

                  <div>
                    <h3 className="font-semibold mb-2">Estructura del Proceso de Inscripción:</h3>
                    <div className="bg-muted p-4 rounded-lg space-y-2">
                      {demoInfo.estructuraProceso && demoInfo.estructuraProceso.map((paso: string, idx: number) => (
                        <div key={idx} className="flex items-start gap-2">
                          <Badge variant="outline" className="mt-0.5">{idx + 1}</Badge>
                          <span className="text-sm">{paso}</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  <Separator />

                  <div>
                    <h3 className="font-semibold mb-3">Tipos de Inscripción Disponibles:</h3>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                      {tiposInscripcion.map((tipo) => (
                        <Card key={tipo.tipo}>
                          <CardHeader>
                            <CardTitle className="text-lg flex items-center gap-2">
                              {getIconoTipo(tipo.icono)}
                              {tipo.nombre}
                            </CardTitle>
                            <CardDescription>{tipo.descripcion}</CardDescription>
                          </CardHeader>
                        </Card>
                      ))}
                    </div>
                  </div>
                </>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* Tab: Nueva Inscripción */}
        <TabsContent value="inscripcion" className="space-y-4">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Formulario */}
            <Card className="lg:col-span-2">
              <CardHeader>
                <CardTitle>Nueva Inscripción</CardTitle>
                <CardDescription>Complete el formulario según el tipo de inscripción</CardDescription>
              </CardHeader>
              <CardContent>
                <form onSubmit={handleSubmit} className="space-y-6">
                  {/* Selección de tipo de inscripción */}
                  <div className="space-y-2">
                    <Label htmlFor="tipo">Tipo de Inscripción *</Label>
                    <Select value={tipoSeleccionado} onValueChange={handleTipoChange}>
                      <SelectTrigger>
                        <SelectValue placeholder="Seleccione un tipo" />
                      </SelectTrigger>
                      <SelectContent>
                        {tiposInscripcion.map((tipo) => (
                          <SelectItem key={tipo.tipo} value={tipo.tipo}>
                            {tipo.nombre}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  {tipoSeleccionado && (
                    <>
                      {/* Datos básicos */}
                      <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-2">
                          <Label htmlFor="estudianteId">ID Estudiante *</Label>
                          <Input
                            id="estudianteId"
                            type="number"
                            value={formData.estudianteId}
                            onChange={(e) => setFormData({ ...formData, estudianteId: parseInt(e.target.value) })}
                            required
                            disabled
                            className="bg-muted"
                          />
                        </div>
                        <div className="space-y-2">
                          <Label htmlFor="cursoId">Curso *</Label>
                          <Select
                            value={formData.cursoId}
                            onValueChange={(value) => setFormData({ ...formData, cursoId: value })}
                          >
                            <SelectTrigger>
                              <SelectValue placeholder="Seleccione un curso" />
                            </SelectTrigger>
                            <SelectContent>
                              {cursosDisponibles.map((curso) => (
                                <SelectItem key={curso.id} value={curso.id.toString()}>
                                  {curso.nombre}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                        </div>
                      </div>

                      {/* Campos específicos para inscripción PAGA */}
                      {tipoSeleccionado === 'PAGA' && (
                        <div className="space-y-4 border-t pt-4">
                          <h3 className="font-semibold text-sm">Información de Pago</h3>
                          <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-2">
                              <Label htmlFor="metodoPago">Método de Pago *</Label>
                              <Select
                                value={formData.metodoPago}
                                onValueChange={(value) => setFormData({ ...formData, metodoPago: value })}
                              >
                                <SelectTrigger>
                                  <SelectValue placeholder="Seleccione método" />
                                </SelectTrigger>
                                <SelectContent>
                                  <SelectItem value="TARJETA">Tarjeta de Crédito</SelectItem>
                                  <SelectItem value="PSE">PSE</SelectItem>
                                  <SelectItem value="EFECTIVO">Efectivo</SelectItem>
                                </SelectContent>
                              </Select>
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="monto">Monto *</Label>
                              <Input
                                id="monto"
                                type="number"
                                step="0.01"
                                value={formData.monto}
                                onChange={(e) => setFormData({ ...formData, monto: e.target.value })}
                                required
                              />
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="numeroTarjeta">Número de Tarjeta *</Label>
                              <Input
                                id="numeroTarjeta"
                                type="text"
                                placeholder="4111111111111111"
                                value={formData.numeroTarjeta}
                                onChange={(e) => setFormData({ ...formData, numeroTarjeta: e.target.value })}
                                required
                              />
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="codigoDescuento">Código de Descuento</Label>
                              <Input
                                id="codigoDescuento"
                                type="text"
                                placeholder="PROMO10"
                                value={formData.codigoDescuento}
                                onChange={(e) => setFormData({ ...formData, codigoDescuento: e.target.value })}
                              />
                            </div>
                          </div>
                        </div>
                      )}

                      {/* Campos específicos para inscripción BECA */}
                      {tipoSeleccionado === 'BECA' && (
                        <div className="space-y-4 border-t pt-4">
                          <h3 className="font-semibold text-sm">Información de Beca</h3>
                          <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-2">
                              <Label htmlFor="tipoBeca">Tipo de Beca *</Label>
                              <Select
                                value={formData.tipoBeca}
                                onValueChange={(value) => setFormData({ ...formData, tipoBeca: value })}
                              >
                                <SelectTrigger>
                                  <SelectValue placeholder="Seleccione tipo" />
                                </SelectTrigger>
                                <SelectContent>
                                  <SelectItem value="ACADEMICA">Académica</SelectItem>
                                  <SelectItem value="DEPORTIVA">Deportiva</SelectItem>
                                  <SelectItem value="SOCIAL">Social</SelectItem>
                                </SelectContent>
                              </Select>
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="porcentajeBeca">Porcentaje de Beca *</Label>
                              <Input
                                id="porcentajeBeca"
                                type="number"
                                min="0"
                                max="100"
                                value={formData.porcentajeBeca}
                                onChange={(e) => setFormData({ ...formData, porcentajeBeca: e.target.value })}
                                required
                              />
                            </div>
                            <div className="space-y-2 col-span-2">
                              <Label htmlFor="codigoBeca">Código de Beca *</Label>
                              <Input
                                id="codigoBeca"
                                type="text"
                                placeholder="BECA-2024-001"
                                value={formData.codigoBeca}
                                onChange={(e) => setFormData({ ...formData, codigoBeca: e.target.value })}
                                required
                              />
                            </div>
                            <div className="space-y-2 col-span-2">
                              <Label htmlFor="documentoSoporte">Documento de Soporte</Label>
                              <Input
                                id="documentoSoporte"
                                type="text"
                                placeholder="URL o ruta del documento"
                                value={formData.documentoSoporte}
                                onChange={(e) => setFormData({ ...formData, documentoSoporte: e.target.value })}
                              />
                            </div>
                          </div>
                        </div>
                      )}

                      {/* Términos y condiciones */}
                      <div className="flex items-center space-x-2 border-t pt-4">
                        <input
                          type="checkbox"
                          id="aceptaTerminos"
                          checked={formData.aceptaTerminos}
                          onChange={(e) => setFormData({ ...formData, aceptaTerminos: e.target.checked })}
                          required
                          className="rounded"
                        />
                        <Label htmlFor="aceptaTerminos" className="cursor-pointer">
                          Acepto los términos y condiciones *
                        </Label>
                      </div>

                      <Button type="submit" className="w-full" disabled={procesando}>
                        {procesando ? (
                          <>
                            <Clock className="h-4 w-4 mr-2 animate-spin" />
                            Procesando...
                          </>
                        ) : (
                          'Procesar Inscripción'
                        )}
                      </Button>
                    </>
                  )}
                </form>
              </CardContent>
            </Card>

            {/* Vista previa de pasos */}
            {tipoSeleccionado && (
              <Card>
                <CardHeader>
                  <CardTitle className="text-lg">Pasos del Proceso</CardTitle>
                  <CardDescription>
                    Tipo: {tiposInscripcion.find(t => t.tipo === tipoSeleccionado)?.nombre}
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    {pasosTipo.map((paso, idx) => (
                      <div key={idx} className="flex items-start gap-3">
                        <Badge variant="outline" className="mt-0.5 shrink-0">
                          {idx + 1}
                        </Badge>
                        <span className="text-sm">{paso}</span>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            )}
          </div>
        </TabsContent>

        {/* Tab: Resultado */}
        <TabsContent value="resultado" className="space-y-4">
          {resultado && (
            <>
              {/* Resumen del resultado */}
              <Card>
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle className="flex items-center gap-2">
                        {resultado.exitoso ? (
                          <CheckCircle2 className="h-6 w-6 text-green-600" />
                        ) : (
                          <XCircle className="h-6 w-6 text-red-600" />
                        )}
                        Resultado de la Inscripción
                      </CardTitle>
                      <CardDescription>{resultado.mensaje}</CardDescription>
                    </div>
                    <Badge variant={resultado.exitoso ? 'default' : 'destructive'}>
                      {resultado.estado}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  {/* Información general */}
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div>
                      <Label className="text-xs text-muted-foreground">Tipo</Label>
                      <p className="font-medium">{resultado.tipoInscripcion}</p>
                    </div>
                    <div>
                      <Label className="text-xs text-muted-foreground">Estudiante ID</Label>
                      <p className="font-medium">{resultado.estudianteId}</p>
                    </div>
                    <div>
                      <Label className="text-xs text-muted-foreground">Curso ID</Label>
                      <p className="font-medium">{resultado.cursoId}</p>
                    </div>
                    <div>
                      <Label className="text-xs text-muted-foreground">Duración</Label>
                      <p className="font-medium">{resultado.duracionTotalMs} ms</p>
                    </div>
                  </div>

                  {/* Información adicional si está disponible */}
                  {resultado.numeroInscripcion && (
                    <Alert>
                      <FileText className="h-4 w-4" />
                      <AlertTitle>Número de Inscripción</AlertTitle>
                      <AlertDescription className="font-mono">
                        {resultado.numeroInscripcion}
                      </AlertDescription>
                    </Alert>
                  )}

                  {/* Detalles económicos */}
                  {(resultado.montoTotal || resultado.descuentoAplicado) && (
                    <div className="grid grid-cols-2 gap-4 bg-muted p-4 rounded-lg">
                      {resultado.montoTotal && (
                        <div>
                          <Label className="text-xs text-muted-foreground">Monto Total</Label>
                          <p className="font-medium text-lg">${resultado.montoTotal}</p>
                        </div>
                      )}
                      {resultado.descuentoAplicado && (
                        <div>
                          <Label className="text-xs text-muted-foreground">Descuento</Label>
                          <p className="font-medium text-lg text-green-600">-${resultado.descuentoAplicado}</p>
                        </div>
                      )}
                    </div>
                  )}
                </CardContent>
              </Card>

              {/* Detalle de pasos ejecutados */}
              <Card>
                <CardHeader>
                  <CardTitle>Pasos Ejecutados</CardTitle>
                  <CardDescription>
                    Detalle del proceso Template Method paso a paso
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {resultado.pasos.map((paso, idx) => (
                      <div key={idx} className="border rounded-lg p-4">
                        <div className="flex items-start justify-between mb-2">
                          <div className="flex items-center gap-3">
                            {getIconoPaso(paso.exitoso)}
                            <div>
                              <h4 className="font-semibold">{paso.nombre}</h4>
                              <p className="text-sm text-muted-foreground">{paso.mensaje}</p>
                            </div>
                          </div>
                          <Badge variant="outline">
                            Paso {idx + 1}
                          </Badge>
                        </div>

                        {/* Detalles adicionales del paso */}
                        {paso.detalles && Object.keys(paso.detalles).length > 0 && (
                          <div className="mt-3 bg-muted p-3 rounded text-xs space-y-1">
                            {Object.entries(paso.detalles).map(([key, value]) => (
                              <div key={key} className="flex justify-between">
                                <span className="text-muted-foreground">{key}:</span>
                                <span className="font-mono">{value}</span>
                              </div>
                            ))}
                          </div>
                        )}

                        {paso.duracionMs && (
                          <div className="mt-2 text-xs text-muted-foreground">
                            Duración: {paso.duracionMs} ms
                          </div>
                        )}
                      </div>
                    ))}
                  </div>

                  {/* Progreso visual */}
                  <div className="mt-6">
                    <div className="flex items-center justify-between mb-2">
                      <Label className="text-sm">Progreso del Proceso</Label>
                      <span className="text-sm text-muted-foreground">
                        {resultado.pasos.filter(p => p.exitoso).length} / {resultado.pasos.length} pasos exitosos
                      </span>
                    </div>
                    <Progress
                      value={(resultado.pasos.filter(p => p.exitoso).length / resultado.pasos.length) * 100}
                    />
                  </div>
                </CardContent>
              </Card>
            </>
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
