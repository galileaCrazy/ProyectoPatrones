'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

interface ReporteGenerado {
  id: number
  tipoReporte: string
  formato: string
  titulo: string
  contenido: string
  fechaGeneracion: string
  estado: string
}

export default function ReportsView() {
  const [reportType, setReportType] = useState('ESTUDIANTES')
  const [format, setFormat] = useState('PDF')
  const [generating, setGenerating] = useState(false)
  const [reportes, setReportes] = useState<ReporteGenerado[]>([])
  const [loading, setLoading] = useState(false)
  const [previewContent, setPreviewContent] = useState<string | null>(null)

  useEffect(() => {
    fetchReportes()
  }, [])

  const fetchReportes = async () => {
    setLoading(true)
    try {
      const response = await fetch('http://localhost:8080/api/reportes')
      if (response.ok) {
        const data = await response.json()
        setReportes(data)
      }
    } catch (error) {
      console.error('Error al cargar reportes:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleGenerateReport = async () => {
    setGenerating(true)
    try {
      const response = await fetch('http://localhost:8080/api/reportes/generar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          tipoReporte: reportType,
          formato: format
        })
      })

      if (response.ok) {
        const data = await response.json()
        alert(`✅ Reporte generado con patrón Bridge!\n\nID: ${data.reporteId}\nTipo: ${data.tipoReporte}\nFormato: ${data.formato}`)
        setPreviewContent(data.contenido)
        fetchReportes()
      } else {
        alert('❌ Error al generar reporte')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('❌ Error al comunicarse con el servidor')
    } finally {
      setGenerating(false)
    }
  }

  const getFormatIcon = (formato: string) => {
    switch (formato.toUpperCase()) {
      case 'PDF':
        return '📄'
      case 'EXCEL':
      case 'XLS':
        return '📊'
      case 'HTML':
        return '🌐'
      default:
        return '📁'
    }
  }

  const getReportTypeLabel = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case 'ESTUDIANTES':
        return 'Estudiantes'
      case 'CURSOS':
        return 'Cursos'
      case 'CALIFICACIONES':
        return 'Calificaciones'
      default:
        return tipo
    }
  }

  const handleDownloadReport = async (reporteId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reportes/${reporteId}/descargar`)

      if (response.ok) {
        const blob = await response.blob()
        const contentDisposition = response.headers.get('Content-Disposition')
        const filename = contentDisposition
          ? contentDisposition.split('filename=')[1]?.replace(/"/g, '')
          : 'reporte.txt'

        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = filename
        document.body.appendChild(a)
        a.click()
        window.URL.revokeObjectURL(url)
        document.body.removeChild(a)
      } else {
        alert('Error al descargar el reporte')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('Error al descargar el reporte')
    }
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground mb-2">
          Generador de Reportes
          <span className="ml-3 text-sm text-primary">Bridge Pattern</span>
        </h1>
        <p className="text-muted-foreground">
          Genera reportes en diferentes formatos usando el patrón Bridge
        </p>
      </div>

      <Card className="mb-6 border-primary/20 bg-primary/5">
        <CardContent className="pt-6">
          <div className="flex items-start gap-4">
            <div className="text-4xl">🌉</div>
            <div className="flex-1">
              <h3 className="font-semibold text-foreground mb-2">
                Patrón Bridge en Acción
              </h3>
              <p className="text-sm text-muted-foreground mb-3">
                El patrón <strong>Bridge</strong> separa la abstracción (tipo de reporte) de su implementación
                (formato de salida). Esto permite que ambos varíen independientemente.
              </p>
              <div className="grid grid-cols-2 gap-4 mt-4">
                <div className="bg-background border border-border rounded-lg p-3">
                  <p className="text-xs text-muted-foreground mb-1">Abstracción (Tipos)</p>
                  <div className="flex gap-2 flex-wrap">
                    <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded dark:bg-blue-900 dark:text-blue-200">
                      👥 Estudiantes
                    </span>
                    <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded dark:bg-green-900 dark:text-green-200">
                      📚 Cursos
                    </span>
                    <span className="text-xs bg-purple-100 text-purple-800 px-2 py-1 rounded dark:bg-purple-900 dark:text-purple-200">
                      📝 Calificaciones
                    </span>
                  </div>
                </div>
                <div className="bg-background border border-border rounded-lg p-3">
                  <p className="text-xs text-muted-foreground mb-1">Implementación (Formatos)</p>
                  <div className="flex gap-2 flex-wrap">
                    <span className="text-xs bg-red-100 text-red-800 px-2 py-1 rounded dark:bg-red-900 dark:text-red-200">
                      📄 PDF
                    </span>
                    <span className="text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded dark:bg-yellow-900 dark:text-yellow-200">
                      📊 Excel
                    </span>
                    <span className="text-xs bg-cyan-100 text-cyan-800 px-2 py-1 rounded dark:bg-cyan-900 dark:text-cyan-200">
                      🌐 HTML
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="border-border/50">
          <CardHeader>
            <CardTitle>Crear Nuevo Reporte</CardTitle>
            <CardDescription>Selecciona el tipo y formato</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-foreground mb-3">Tipo de Reporte</label>
              <div className="space-y-2">
                {[
                  { id: 'ESTUDIANTES', name: '👥 Estudiantes' },
                  { id: 'CURSOS', name: '📚 Cursos' },
                  { id: 'CALIFICACIONES', name: '📝 Calificaciones' },
                ].map((type) => (
                  <label key={type.id} className="flex items-center gap-3 p-3 rounded-lg border border-input cursor-pointer hover:bg-muted transition-colors">
                    <input
                      type="radio"
                      name="reportType"
                      value={type.id}
                      checked={reportType === type.id}
                      onChange={(e) => setReportType(e.target.value)}
                      className="w-4 h-4"
                    />
                    <span className="font-medium text-foreground">{type.name}</span>
                  </label>
                ))}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-3">Formato de Salida</label>
              <div className="flex gap-4">
                {[
                  { id: 'PDF', icon: '📄' },
                  { id: 'EXCEL', icon: '📊' },
                  { id: 'HTML', icon: '🌐' }
                ].map((f) => (
                  <label key={f.id} className="flex-1 flex flex-col items-center gap-2 p-4 rounded-lg border border-input cursor-pointer hover:bg-muted transition-colors">
                    <input
                      type="radio"
                      name="format"
                      value={f.id}
                      checked={format === f.id}
                      onChange={(e) => setFormat(e.target.value)}
                      className="w-4 h-4"
                    />
                    <span className="text-2xl">{f.icon}</span>
                    <span className="font-medium text-foreground text-sm">{f.id}</span>
                  </label>
                ))}
              </div>
            </div>

            <Button
              onClick={handleGenerateReport}
              disabled={generating}
              className="w-full bg-primary hover:bg-primary/90 text-primary-foreground"
            >
              {generating ? 'Generando...' : '🌉 Generar con Bridge'}
            </Button>
          </CardContent>
        </Card>

        {previewContent && (
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Vista Previa</CardTitle>
              <CardDescription>Contenido generado en {format}</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="bg-muted/30 p-4 rounded-lg border border-border max-h-96 overflow-auto">
                <pre className="text-xs text-foreground whitespace-pre-wrap font-mono">{previewContent}</pre>
              </div>
            </CardContent>
          </Card>
        )}
      </div>

      <Card className="border-border/50 mt-6">
        <CardHeader>
          <CardTitle>Reportes Generados</CardTitle>
          <CardDescription>Historial de reportes creados con Bridge Pattern</CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex items-center justify-center h-32">
              <div className="text-center">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-2"></div>
                <p className="text-sm text-muted-foreground">Cargando reportes...</p>
              </div>
            </div>
          ) : reportes.length === 0 ? (
            <p className="text-center text-muted-foreground py-8">
              No hay reportes generados todavía
            </p>
          ) : (
            <div className="space-y-3">
              {reportes.slice(0, 10).map((reporte) => (
                <div
                  key={reporte.id}
                  className="flex justify-between items-center p-4 rounded-lg bg-muted/50 border border-border hover:bg-muted transition-colors"
                >
                  <div className="flex items-start gap-3">
                    <span className="text-2xl">{getFormatIcon(reporte.formato)}</span>
                    <div>
                      <p className="font-medium text-foreground">{reporte.titulo}</p>
                      <p className="text-sm text-muted-foreground">
                        {getReportTypeLabel(reporte.tipoReporte)} • {reporte.formato}
                      </p>
                      <p className="text-xs text-muted-foreground mt-1">
                        {new Date(reporte.fechaGeneracion).toLocaleString('es-ES')}
                      </p>
                    </div>
                  </div>
                  <div className="flex gap-2">
                    <Button
                      onClick={() => setPreviewContent(reporte.contenido)}
                      className="bg-primary hover:bg-primary/90 text-primary-foreground text-sm"
                    >
                      👁️ Ver
                    </Button>
                    <Button
                      onClick={() => handleDownloadReport(reporte.id)}
                      className="bg-green-600 hover:bg-green-700 text-white text-sm"
                    >
                      📥 Descargar
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
