'use client'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { Progress } from '@/components/ui/progress'
import { Label } from '@/components/ui/label'
import { Separator } from '@/components/ui/separator'
import { CheckCircle2, XCircle, FileText, Clock, Info } from 'lucide-react'

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

interface ResultadoViewerProps {
  resultado: ResultadoInscripcion
  onClose?: () => void
  showActions?: boolean
}

/**
 * Componente reutilizable para visualizar el resultado de una inscripción
 * Puede ser usado de forma independiente o dentro de otros componentes
 */
export default function ResultadoViewer({ resultado, onClose, showActions = true }: ResultadoViewerProps) {
  const getIconoPaso = (exitoso: boolean) => {
    return exitoso ? (
      <CheckCircle2 className="h-5 w-5 text-green-600" />
    ) : (
      <XCircle className="h-5 w-5 text-red-600" />
    )
  }

  const pasosExitosos = resultado.pasos.filter(p => p.exitoso).length
  const progresoPercent = (pasosExitosos / resultado.pasos.length) * 100

  return (
    <div className="space-y-4">
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
              <p className="font-medium flex items-center gap-1">
                <Clock className="h-3 w-3" />
                {resultado.duracionTotalMs} ms
              </p>
            </div>
          </div>

          {/* Número de inscripción */}
          {resultado.numeroInscripcion && (
            <Alert>
              <FileText className="h-4 w-4" />
              <AlertTitle>Número de Inscripción</AlertTitle>
              <AlertDescription className="font-mono text-lg">
                {resultado.numeroInscripcion}
              </AlertDescription>
            </Alert>
          )}

          {/* Detalles económicos */}
          {(resultado.montoTotal || resultado.descuentoAplicado) && (
            <>
              <Separator />
              <div className="grid grid-cols-2 gap-4 bg-muted p-4 rounded-lg">
                {resultado.montoTotal && (
                  <div>
                    <Label className="text-xs text-muted-foreground">Monto Total</Label>
                    <p className="font-medium text-lg">${resultado.montoTotal.toFixed(2)}</p>
                  </div>
                )}
                {resultado.descuentoAplicado && (
                  <div>
                    <Label className="text-xs text-muted-foreground">Descuento Aplicado</Label>
                    <p className="font-medium text-lg text-green-600">
                      -${resultado.descuentoAplicado.toFixed(2)}
                    </p>
                  </div>
                )}
              </div>
            </>
          )}

          {/* Enlaces de documentos */}
          {(resultado.urlComprobante || resultado.urlRecibo) && (
            <>
              <Separator />
              <div className="flex gap-2 flex-wrap">
                {resultado.urlComprobante && (
                  <a
                    href={resultado.urlComprobante}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-sm text-primary hover:underline flex items-center gap-1"
                  >
                    <FileText className="h-3 w-3" />
                    Descargar Comprobante
                  </a>
                )}
                {resultado.urlRecibo && (
                  <a
                    href={resultado.urlRecibo}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-sm text-primary hover:underline flex items-center gap-1"
                  >
                    <FileText className="h-3 w-3" />
                    Descargar Recibo
                  </a>
                )}
              </div>
            </>
          )}
        </CardContent>
      </Card>

      {/* Detalle de pasos ejecutados */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Info className="h-5 w-5" />
            Pasos Ejecutados
          </CardTitle>
          <CardDescription>
            Detalle del proceso Template Method paso a paso
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Timeline de pasos */}
          <div className="space-y-4">
            {resultado.pasos.map((paso, idx) => (
              <div key={idx} className="border rounded-lg p-4 hover:bg-muted/50 transition-colors">
                <div className="flex items-start justify-between mb-2">
                  <div className="flex items-center gap-3 flex-1">
                    {getIconoPaso(paso.exitoso)}
                    <div className="flex-1">
                      <h4 className="font-semibold">{paso.nombre}</h4>
                      <p className="text-sm text-muted-foreground mt-1">{paso.mensaje}</p>
                    </div>
                  </div>
                  <div className="flex flex-col items-end gap-1">
                    <Badge variant="outline">Paso {idx + 1}</Badge>
                    {paso.duracionMs && (
                      <span className="text-xs text-muted-foreground flex items-center gap-1">
                        <Clock className="h-3 w-3" />
                        {paso.duracionMs}ms
                      </span>
                    )}
                  </div>
                </div>

                {/* Detalles adicionales del paso */}
                {paso.detalles && Object.keys(paso.detalles).length > 0 && (
                  <div className="mt-3 bg-muted p-3 rounded text-xs space-y-1">
                    {Object.entries(paso.detalles).map(([key, value]) => (
                      <div key={key} className="flex justify-between gap-4">
                        <span className="text-muted-foreground capitalize">
                          {key.replace(/([A-Z])/g, ' $1').toLowerCase()}:
                        </span>
                        <span className="font-mono text-right">{value}</span>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>

          {/* Barra de progreso */}
          <div className="pt-4 border-t">
            <div className="flex items-center justify-between mb-2">
              <Label className="text-sm">Progreso del Proceso</Label>
              <span className="text-sm text-muted-foreground">
                {pasosExitosos} / {resultado.pasos.length} pasos exitosos
              </span>
            </div>
            <Progress value={progresoPercent} className="h-2" />
            <p className="text-xs text-muted-foreground mt-2 text-center">
              {progresoPercent.toFixed(0)}% completado
            </p>
          </div>

          {/* Resumen temporal */}
          <div className="bg-muted p-4 rounded-lg">
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <Label className="text-xs text-muted-foreground">Inicio</Label>
                <p className="font-mono text-xs">
                  {new Date(resultado.fechaInicio).toLocaleString()}
                </p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">Fin</Label>
                <p className="font-mono text-xs">
                  {new Date(resultado.fechaFin).toLocaleString()}
                </p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Información del patrón */}
      <Alert>
        <Info className="h-4 w-4" />
        <AlertTitle>Patrón Template Method</AlertTitle>
        <AlertDescription className="text-xs">
          Este proceso siguió el patrón de diseño Template Method, donde el flujo general
          está definido en una plantilla base, pero algunos pasos específicos varían según
          el tipo de inscripción ({resultado.tipoInscripcion}).
        </AlertDescription>
      </Alert>
    </div>
  )
}
