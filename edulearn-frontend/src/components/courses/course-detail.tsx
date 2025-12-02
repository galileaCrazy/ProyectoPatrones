'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { ModuleTree } from './module-tree'

interface CourseDetailViewProps {
  courseId: string | null
  role: 'student' | 'professor' | 'admin'
  onBack: () => void
}

interface Course {
  id: number
  codigo: string
  nombre: string
  descripcion: string
  tipoCurso: string
  estado: string
  profesorTitularId: number
  periodoAcademico: string
  duracion: number
  estrategiaEvaluacion: string
  cupoMaximo: number
}

export default function CourseDetailView({ courseId, role, onBack }: CourseDetailViewProps) {
  const [activeTab, setActiveTab] = useState('content')
  const [selectedNode, setSelectedNode] = useState<any>(null)
  const [course, setCourse] = useState<Course | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchCourse = async () => {
      if (!courseId) {
        setLoading(false)
        return
      }

      try {
        setLoading(true)
        setError(null)

        const response = await fetch(`http://localhost:8080/api/cursos/${courseId}`)

        if (!response.ok) {
          throw new Error(`Error al cargar el curso: ${response.status}`)
        }

        const data = await response.json()
        setCourse(data)
      } catch (err) {
        console.error('Error al cargar curso:', err)
        setError(err instanceof Error ? err.message : 'Error desconocido')
      } finally {
        setLoading(false)
      }
    }

    fetchCourse()
  }, [courseId])

  if (loading) {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Cargando curso...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error || !course) {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <Button onClick={onBack} className="mb-6 bg-muted hover:bg-muted/80 text-foreground">
          ← Volver
        </Button>
        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
          <p className="text-red-800">Error: {error || 'No se encontró el curso'}</p>
        </div>
      </div>
    )
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <Button onClick={onBack} className="mb-6 bg-muted hover:bg-muted/80 text-foreground">
        ← Volver
      </Button>

      <div className="mb-8">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold text-foreground">{course.nombre}</h1>
            <p className="text-muted-foreground mt-2">Código: {course.codigo}</p>
            <p className="text-sm text-muted-foreground mt-1">{course.descripcion}</p>
          </div>
          <span className="bg-primary/10 text-primary px-4 py-2 rounded-lg">
            {course.tipoCurso}
          </span>
        </div>

        {/* Course Info */}
        <div className="grid grid-cols-3 gap-4 max-w-2xl">
          <div className="p-4 rounded-lg bg-muted/50 border border-border">
            <p className="text-sm text-muted-foreground mb-1">Duración</p>
            <p className="text-lg font-semibold text-foreground">{course.duracion} horas</p>
          </div>
          <div className="p-4 rounded-lg bg-muted/50 border border-border">
            <p className="text-sm text-muted-foreground mb-1">Estado</p>
            <p className={`text-lg font-semibold ${
              course.estado === 'Activo' ? 'text-green-600' : 'text-gray-600'
            }`}>
              {course.estado}
            </p>
          </div>
          <div className="p-4 rounded-lg bg-muted/50 border border-border">
            <p className="text-sm text-muted-foreground mb-1">Período</p>
            <p className="text-lg font-semibold text-foreground">{course.periodoAcademico}</p>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex gap-4 mb-6 border-b border-border">
        {['content', 'estudiantes', 'evaluaciones', 'estadísticas'].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-3 font-medium transition-colors border-b-2 ${
              activeTab === tab
                ? 'border-primary text-primary'
                : 'border-transparent text-muted-foreground hover:text-foreground'
            }`}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      {/* Content */}
      {activeTab === 'content' && (
        <div className="space-y-6">
          <h2 className="text-2xl font-bold text-foreground mb-6">Contenido del Curso</h2>

          {/* Árbol jerárquico del curso usando Composite Pattern */}
          {courseId ? (
            <ModuleTree
              cursoId={parseInt(courseId)}
              onNodeSelect={setSelectedNode}
            />
          ) : (
            <Card className="border-border/50">
              <CardContent className="p-8 text-center">
                <p className="text-muted-foreground">
                  No se ha seleccionado un curso válido
                </p>
              </CardContent>
            </Card>
          )}

          {/* Información adicional del nodo seleccionado */}
          {selectedNode && (
            <Card className="border-border/50 bg-accent/5">
              <CardHeader>
                <CardTitle>{selectedNode.nombre}</CardTitle>
                <CardDescription>
                  Tipo: {selectedNode.tipo} | Duración: {Math.ceil((selectedNode.duracionTotal || 0) / 60)} min
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {selectedNode.descripcion && (
                    <p className="text-muted-foreground">{selectedNode.descripcion}</p>
                  )}

                  {selectedNode.estado && (
                    <div>
                      <p className="text-sm font-medium mb-1">Estado:</p>
                      <span className={`text-xs px-3 py-1 rounded-full font-semibold ${
                        selectedNode.estado === 'published' ? 'bg-green-100 text-green-700' :
                        selectedNode.estado === 'draft' ? 'bg-blue-100 text-blue-700' :
                        'bg-gray-100 text-gray-700'
                      }`}>
                        {selectedNode.estado === 'published' ? 'Publicado' :
                         selectedNode.estado === 'draft' ? 'Borrador' : selectedNode.estado}
                      </span>
                    </div>
                  )}

                  {selectedNode.hijos && selectedNode.hijos.length > 0 && (
                    <div>
                      <p className="text-sm font-medium mb-1">Contenido:</p>
                      <p className="text-sm text-muted-foreground">
                        {selectedNode.hijos.length} elemento(s) en este nodo
                      </p>
                    </div>
                  )}

                  {/* Información específica de materiales */}
                  {selectedNode.tipoMaterial && (
                    <div>
                      <p className="text-sm font-medium mb-1">Tipo de Material:</p>
                      <span className="text-sm text-muted-foreground">{selectedNode.tipoMaterial}</span>
                      {selectedNode.esObligatorio && (
                        <span className="ml-2 text-xs bg-orange-100 text-orange-700 px-2 py-1 rounded">
                          Obligatorio
                        </span>
                      )}
                    </div>
                  )}

                  {/* Información específica de evaluaciones */}
                  {selectedNode.tipoEvaluacion && (
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <p className="text-sm font-medium mb-1">Tipo:</p>
                        <p className="text-sm text-muted-foreground">{selectedNode.tipoEvaluacion}</p>
                      </div>
                      {selectedNode.puntajeMaximo && (
                        <div>
                          <p className="text-sm font-medium mb-1">Puntaje Máximo:</p>
                          <p className="text-sm text-muted-foreground">{selectedNode.puntajeMaximo}</p>
                        </div>
                      )}
                      {selectedNode.intentosPermitidos && (
                        <div>
                          <p className="text-sm font-medium mb-1">Intentos:</p>
                          <p className="text-sm text-muted-foreground">{selectedNode.intentosPermitidos}</p>
                        </div>
                      )}
                      {selectedNode.tiempoLimiteMinutos && (
                        <div>
                          <p className="text-sm font-medium mb-1">Tiempo Límite:</p>
                          <p className="text-sm text-muted-foreground">{selectedNode.tiempoLimiteMinutos} min</p>
                        </div>
                      )}
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      )}

      {activeTab === 'estudiantes' && (
        <Card>
          <CardHeader>
            <CardTitle>Estudiantes Inscritos (Cupo máximo: {course.cupoMaximo})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {Array.from({ length: 5 }).map((_, i) => (
                <div key={i} className="flex items-center justify-between p-3 rounded-lg bg-muted/50 border border-border">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                      <span className="text-primary font-semibold">ST</span>
                    </div>
                    <div>
                      <p className="font-medium text-foreground">Estudiante {i + 1}</p>
                      <p className="text-xs text-muted-foreground">est{i + 1}@universidad.edu</p>
                    </div>
                  </div>
                  <span className="text-sm font-semibold text-primary">85%</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {activeTab === 'evaluaciones' && (
        <Card>
          <CardHeader>
            <CardTitle>Evaluaciones del Curso</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {[
                { name: 'Quiz 1: Conceptos Básicos', type: 'Quiz', date: '15/11/2025', score: 92 },
                { name: 'Tarea: Implementación de Clases', type: 'Tarea', date: '20/11/2025', score: 88 },
                { name: 'Examen Parcial', type: 'Examen', date: '25/11/2025', score: null },
              ].map((eval_, i) => (
                <div key={i} className="p-3 rounded-lg bg-muted/50 border border-border">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-medium text-foreground">{eval_.name}</p>
                      <div className="flex gap-2 mt-1">
                        <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded">{eval_.type}</span>
                        <span className="text-xs text-muted-foreground">{eval_.date}</span>
                      </div>
                    </div>
                    {eval_.score ? (
                      <span className="text-lg font-bold text-primary">{eval_.score}%</span>
                    ) : (
                      <span className="text-sm text-muted-foreground">Pendiente</span>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {activeTab === 'estadísticas' && (
        <Card>
          <CardHeader>
            <CardTitle>Estadísticas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">32h</p>
                <p className="text-xs text-muted-foreground mt-1">Horas de estudio</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">15/18</p>
                <p className="text-xs text-muted-foreground mt-1">Tareas completadas</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">8</p>
                <p className="text-xs text-muted-foreground mt-1">Posts en foro</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">85%</p>
                <p className="text-xs text-muted-foreground mt-1">Promedio actual</p>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}