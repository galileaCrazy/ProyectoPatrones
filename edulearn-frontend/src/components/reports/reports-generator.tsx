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

interface Course {
  id: number
  nombre: string
  codigo: string
}

export default function ReportsView() {
  const [reportType, setReportType] = useState('ESTUDIANTES')
  const [format, setFormat] = useState('PDF')
  const [generating, setGenerating] = useState(false)
  const [reportes, setReportes] = useState<ReporteGenerado[]>([])
  const [courses, setCourses] = useState<Course[]>([])
  const [loading, setLoading] = useState(false)
  const [selectedCourse, setSelectedCourse] = useState<number | 'all'>('all')

  useEffect(() => {
    fetchReportes()
    loadCourses()
  }, [])

  const loadCourses = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/cursos')
      if (response.ok) {
        const data = await response.json()
        const mappedCourses = data.map((curso: any) => ({
          id: curso.id,
          nombre: curso.nombre,
          codigo: curso.codigo
        }))
        setCourses(mappedCourses)
      }
    } catch (error) {
      console.error('Error al cargar cursos:', error)
    }
  }

  const fetchReportes = async () => {
    setLoading(true)
    try {
      // Agregar timestamp para evitar caché
      const response = await fetch(`http://localhost:8080/api/reportes?t=${Date.now()}`, {
        cache: 'no-store'
      })
      if (response.ok) {
        const data = await response.json()
        // Ordenar por fecha de generación descendente (más recientes primero)
        const sortedData = data.sort((a: ReporteGenerado, b: ReporteGenerado) => {
          return new Date(b.fechaGeneracion).getTime() - new Date(a.fechaGeneracion).getTime()
        })
        console.log('Reportes cargados:', sortedData.length)
        setReportes(sortedData)
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
          formato: format,
          cursoId: selectedCourse === 'all' ? null : selectedCourse
        })
      })

      if (response.ok) {
        const data = await response.json()
        // Recargar la lista de reportes primero
        await fetchReportes()
        alert(`Reporte generado exitosamente\n\nID: ${data.reporteId}\nTipo: ${data.tipoReporte}\nFormato: ${data.formato}`)
      } else {
        alert('Error al generar reporte')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('Error al comunicarse con el servidor')
    } finally {
      setGenerating(false)
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

  const handleViewReport = async (reporteId: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/reportes/${reporteId}/descargar`)

      if (response.ok) {
        const blob = await response.blob()
        const url = window.URL.createObjectURL(blob)

        // Abrir en nueva pestaña
        window.open(url, '_blank')

        // Limpiar después de un tiempo
        setTimeout(() => {
          window.URL.revokeObjectURL(url)
        }, 100)
      } else {
        alert('Error al abrir el reporte')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('Error al abrir el reporte')
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
          <span className="ml-3 text-sm text-primary">EduLearn</span>
        </h1>
        <p className="text-muted-foreground">
          Generación de reportes de cursos, estudiantes y calificaciones
        </p>
      </div>

      <div className="grid grid-cols-1 gap-6">
        <Card className="border-border/50">
          <CardHeader>
            <CardTitle>Crear Nuevo Reporte</CardTitle>
            <CardDescription>Selecciona el tipo, formato y alcance del reporte</CardDescription>
          </CardHeader>
          <CardContent className="space-y-6">
            {/* Filtro de Curso */}
            <div>
              <label className="block text-sm font-medium text-foreground mb-3">Alcance del Reporte</label>
              <Card className="border-border/50">
                <CardContent className="p-4">
                  <div className="flex items-center gap-4">
                    <span className="text-sm font-medium text-muted-foreground whitespace-nowrap">
                      Generar para:
                    </span>
                    <div className="flex items-center gap-3 flex-1">
                      <button
                        onClick={() => setSelectedCourse('all')}
                        className={`px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
                          selectedCourse === 'all'
                            ? 'bg-primary text-primary-foreground shadow-sm'
                            : 'bg-muted/50 text-muted-foreground hover:bg-muted hover:text-foreground'
                        }`}
                      >
                        Todos los cursos
                      </button>
                      <span className="text-muted-foreground">o</span>
                      <select
                        value={selectedCourse === 'all' ? '' : selectedCourse}
                        onChange={(e) => setSelectedCourse(e.target.value ? parseInt(e.target.value) : 'all')}
                        className="flex-1 max-w-md px-4 py-2.5 rounded-lg border border-input bg-background text-foreground text-sm font-medium focus:outline-none focus:ring-2 focus:ring-primary transition-all"
                      >
                        <option value="">Seleccionar curso específico...</option>
                        {courses.map(course => (
                          <option key={course.id} value={course.id}>
                            {course.codigo} • {course.nombre}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Tipo de Reporte */}
            <div>
              <label className="block text-sm font-medium text-foreground mb-3">Tipo de Reporte</label>
              <div className="grid grid-cols-3 gap-3">
                {[
                  { id: 'ESTUDIANTES', name: 'Estudiantes' },
                  { id: 'CURSOS', name: 'Cursos' },
                  { id: 'CALIFICACIONES', name: 'Calificaciones' },
                ].map((type) => (
                  <label
                    key={type.id}
                    className={`flex items-center justify-center gap-3 p-4 rounded-lg border-2 cursor-pointer transition-all ${
                      reportType === type.id
                        ? 'border-primary bg-primary/5'
                        : 'border-input hover:bg-muted'
                    }`}
                  >
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

            {/* Formato de Salida */}
            <div>
              <label className="block text-sm font-medium text-foreground mb-3">Formato de Salida</label>
              <div className="grid grid-cols-3 gap-3">
                {[
                  { id: 'PDF', label: 'PDF' },
                  { id: 'EXCEL', label: 'EXCEL' },
                  { id: 'HTML', label: 'HTML' }
                ].map((f) => (
                  <label
                    key={f.id}
                    className={`flex items-center justify-center gap-3 p-4 rounded-lg border-2 cursor-pointer transition-all ${
                      format === f.id
                        ? 'border-primary bg-primary/5'
                        : 'border-input hover:bg-muted'
                    }`}
                  >
                    <input
                      type="radio"
                      name="format"
                      value={f.id}
                      checked={format === f.id}
                      onChange={(e) => setFormat(e.target.value)}
                      className="w-4 h-4"
                    />
                    <span className="font-medium text-foreground text-sm">{f.label}</span>
                  </label>
                ))}
              </div>
            </div>

            <Button
              onClick={handleGenerateReport}
              disabled={generating}
              className="w-full bg-primary hover:bg-primary/90 text-primary-foreground py-6 text-base"
            >
              {generating ? 'Generando...' : 'Generar Reporte'}
            </Button>
          </CardContent>
        </Card>

        {/* Lista de Reportes Generados */}
        <Card className="border-border/50">
          <CardHeader>
            <CardTitle>Reportes Generados</CardTitle>
            <CardDescription>Historial de reportes creados recientemente</CardDescription>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="flex items-center justify-center py-12">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
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
                    <div className="flex items-start gap-3 flex-1">
                      <div className="flex-1">
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
                        onClick={() => handleViewReport(reporte.id)}
                        className="bg-primary hover:bg-primary/90 text-primary-foreground text-sm"
                      >
                        Abrir
                      </Button>
                      <Button
                        onClick={() => handleDownloadReport(reporte.id)}
                        className="bg-green-600 hover:bg-green-700 text-white text-sm"
                      >
                        Descargar
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
