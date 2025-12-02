'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { Separator } from '@/components/ui/separator'
import {
  Info,
  Code,
  FileCode,
  GitBranch,
  Layers,
  CheckCircle2,
  AlertCircle,
  BookOpen,
  Award,
  CreditCard
} from 'lucide-react'

interface TipoInscripcion {
  tipo: string
  nombre: string
  descripcion: string
  icono: string
}

interface DemoInfo {
  patron: string
  descripcion: string
  proposito: string
  ventajas: string[]
  estructuraProceso: string[]
  implementaciones: {
    clase: string
    tipo: string
    descripcion: string
    pasosEspecificos: string[]
  }[]
}

export default function TemplateMethodManager() {
  const [tiposInscripcion, setTiposInscripcion] = useState<TipoInscripcion[]>([])
  const [demoInfo, setDemoInfo] = useState<DemoInfo | null>(null)
  const [tipoSeleccionado, setTipoSeleccionado] = useState<string | null>(null)
  const [pasosTipo, setPasosTipo] = useState<string[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchData()
  }, [])

  useEffect(() => {
    if (tipoSeleccionado) {
      fetchPasosPorTipo(tipoSeleccionado)
    }
  }, [tipoSeleccionado])

  const fetchData = async () => {
    setLoading(true)
    try {
      const [tiposRes, demoRes] = await Promise.all([
        fetch('http://localhost:8080/api/inscripciones/proceso/tipos'),
        fetch('http://localhost:8080/api/inscripciones/proceso/demo')
      ])

      const tiposData = await tiposRes.json()
      const demoData = await demoRes.json()

      setTiposInscripcion(tiposData)
      setDemoInfo(demoData)
    } catch (error) {
      console.error('Error al cargar datos:', error)
    } finally {
      setLoading(false)
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

  const getIconoTipo = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case 'GRATUITA':
        return <BookOpen className="h-5 w-5" />
      case 'PAGA':
        return <CreditCard className="h-5 w-5" />
      case 'BECA':
        return <Award className="h-5 w-5" />
      default:
        return <FileCode className="h-5 w-5" />
    }
  }

  if (loading) {
    return (
      <div className="container mx-auto py-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-center">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
            </div>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="container mx-auto py-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Gestión de Template Method</h1>
          <p className="text-muted-foreground">Panel de control del patrón de inscripciones</p>
        </div>
        <Badge variant="outline" className="text-sm">
          <Code className="h-4 w-4 mr-1" />
          Behavioral Pattern
        </Badge>
      </div>

      {/* Información del Patrón */}
      {demoInfo && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Info className="h-5 w-5" />
              {demoInfo.patron}
            </CardTitle>
            <CardDescription>{demoInfo.descripcion}</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <Alert>
              <AlertCircle className="h-4 w-4" />
              <AlertTitle>Propósito</AlertTitle>
              <AlertDescription>{demoInfo.proposito}</AlertDescription>
            </Alert>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {/* Ventajas */}
              <Card>
                <CardHeader>
                  <CardTitle className="text-sm flex items-center gap-2">
                    <CheckCircle2 className="h-4 w-4 text-green-600" />
                    Ventajas del Patrón
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <ul className="space-y-2">
                    {demoInfo.ventajas?.map((ventaja, idx) => (
                      <li key={idx} className="text-sm flex items-start gap-2">
                        <span className="text-green-600 mt-0.5">•</span>
                        <span>{ventaja}</span>
                      </li>
                    ))}
                  </ul>
                </CardContent>
              </Card>

              {/* Estructura */}
              <Card>
                <CardHeader>
                  <CardTitle className="text-sm flex items-center gap-2">
                    <Layers className="h-4 w-4" />
                    Estructura del Proceso
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-2">
                    {demoInfo.estructuraProceso?.map((paso, idx) => (
                      <div key={idx} className="flex items-start gap-2 text-sm">
                        <Badge variant="outline" className="shrink-0 text-xs">{idx + 1}</Badge>
                        <span>{paso}</span>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>
          </CardContent>
        </Card>
      )}

      <Separator />

      {/* Tipos de Inscripción */}
      <div>
        <h2 className="text-2xl font-bold mb-4">Tipos de Inscripción Implementados</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {tiposInscripcion.map((tipo) => (
            <Card
              key={tipo.tipo}
              className={`cursor-pointer transition-all ${
                tipoSeleccionado === tipo.tipo
                  ? 'border-primary shadow-md'
                  : 'hover:border-primary/50'
              }`}
              onClick={() => setTipoSeleccionado(tipo.tipo)}
            >
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  {getIconoTipo(tipo.tipo)}
                  {tipo.nombre}
                </CardTitle>
                <CardDescription>{tipo.descripcion}</CardDescription>
              </CardHeader>
              <CardContent>
                <Badge variant="secondary">{tipo.tipo}</Badge>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>

      {/* Detalle del tipo seleccionado */}
      {tipoSeleccionado && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <GitBranch className="h-5 w-5" />
              Pasos de la Implementación: {tipoSeleccionado}
            </CardTitle>
            <CardDescription>
              Estos son los pasos específicos que ejecuta este tipo de inscripción
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {pasosTipo.map((paso, idx) => (
                <div
                  key={idx}
                  className="flex items-start gap-3 p-3 bg-muted rounded-lg hover:bg-muted/80 transition-colors"
                >
                  <Badge variant="outline" className="mt-0.5 shrink-0">
                    {idx + 1}
                  </Badge>
                  <div className="flex-1">
                    <p className="text-sm font-medium">{paso}</p>
                  </div>
                </div>
              ))}
            </div>

            {/* Explicación de pasos comunes vs específicos */}
            <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">
              <Alert>
                <Layers className="h-4 w-4" />
                <AlertTitle>Pasos Comunes</AlertTitle>
                <AlertDescription className="text-xs mt-2">
                  Validación de requisitos, verificación de disponibilidad, registro y notificaciones
                  son comunes a todos los tipos.
                </AlertDescription>
              </Alert>

              <Alert>
                <FileCode className="h-4 w-4" />
                <AlertTitle>Pasos Específicos</AlertTitle>
                <AlertDescription className="text-xs mt-2">
                  La validación de documentación, procesamiento económico y generación de documentos
                  varían según el tipo de inscripción.
                </AlertDescription>
              </Alert>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Implementaciones Técnicas */}
      {demoInfo?.implementaciones && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileCode className="h-5 w-5" />
              Implementaciones en Código
            </CardTitle>
            <CardDescription>
              Clases que implementan el patrón Template Method
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {demoInfo.implementaciones.map((impl, idx) => (
                <div key={idx} className="border rounded-lg p-4 space-y-3">
                  <div className="flex items-start justify-between">
                    <div>
                      <h4 className="font-mono text-sm font-semibold">{impl.clase}</h4>
                      <p className="text-sm text-muted-foreground mt-1">{impl.descripcion}</p>
                    </div>
                    <Badge variant="outline">{impl.tipo}</Badge>
                  </div>

                  {impl.pasosEspecificos && impl.pasosEspecificos.length > 0 && (
                    <div className="bg-muted p-3 rounded space-y-2">
                      <p className="text-xs font-semibold text-muted-foreground">
                        Métodos sobrescritos:
                      </p>
                      {impl.pasosEspecificos.map((paso, pIdx) => (
                        <div key={pIdx} className="text-xs font-mono flex items-center gap-2">
                          <span className="text-primary">→</span>
                          <span>{paso}</span>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Información adicional */}
      <Alert>
        <Info className="h-4 w-4" />
        <AlertTitle>Acerca de Template Method</AlertTitle>
        <AlertDescription>
          Este patrón permite definir el esqueleto de un algoritmo en un método (template),
          delegando algunos pasos a las subclases. Las subclases pueden redefinir ciertos pasos
          del algoritmo sin cambiar su estructura general. Esto promueve la reutilización de código
          y facilita el mantenimiento.
        </AlertDescription>
      </Alert>
    </div>
  )
}
