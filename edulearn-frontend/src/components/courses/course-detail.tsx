'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { CourseContentTree } from './course-content-tree'

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
  const [course, setCourse] = useState<Course | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showEditModal, setShowEditModal] = useState(false)
  const [editFormData, setEditFormData] = useState<Partial<Course>>({})
  const [saving, setSaving] = useState(false)
  const [showHistoryModal, setShowHistoryModal] = useState(false)
  const [history, setHistory] = useState<any[]>([])
  const [loadingHistory, setLoadingHistory] = useState(false)

  // Convertir rol de student/professor/admin a ESTUDIANTE/DOCENTE/ADMIN
  const mapRole = (role: 'student' | 'professor' | 'admin'): 'ESTUDIANTE' | 'DOCENTE' | 'ADMIN' => {
    const roleMap = {
      student: 'ESTUDIANTE' as const,
      professor: 'DOCENTE' as const,
      admin: 'ADMIN' as const,
    }
    return roleMap[role]
  }

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

  const handleOpenEditModal = () => {
    if (course) {
      setEditFormData({
        nombre: course.nombre,
        descripcion: course.descripcion,
        tipoCurso: course.tipoCurso,
        duracion: course.duracion,
        periodoAcademico: course.periodoAcademico,
        estrategiaEvaluacion: course.estrategiaEvaluacion
      })
      setShowEditModal(true)
    }
  }

  const handleSaveEdit = async () => {
    if (!courseId) return

    try {
      setSaving(true)

      const response = await fetch(`http://localhost:8080/api/cursos/${courseId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(editFormData)
      })

      if (!response.ok) {
        throw new Error('Error al actualizar el curso')
      }

      alert('Curso actualizado exitosamente!')

      // Recargar el curso
      const courseResponse = await fetch(`http://localhost:8080/api/cursos/${courseId}`)
      if (courseResponse.ok) {
        const updatedCourse = await courseResponse.json()
        setCourse(updatedCourse)
      }

      setShowEditModal(false)
    } catch (error) {
      console.error('Error al actualizar curso:', error)
      alert('Error al actualizar el curso.')
    } finally {
      setSaving(false)
    }
  }

  const handleViewHistory = async () => {
    if (!courseId) return

    try {
      setLoadingHistory(true)
      setShowHistoryModal(true)

      const response = await fetch(`http://localhost:8080/api/cursos/${courseId}/historial`)

      if (!response.ok) {
        throw new Error('Error al cargar el historial')
      }

      const data = await response.json()
      setHistory(data.historial || [])
    } catch (error) {
      console.error('Error al cargar historial:', error)
      alert('Error al cargar el historial.')
    } finally {
      setLoadingHistory(false)
    }
  }

  const handleUndo = async () => {
    if (!courseId) return

    if (!confirm('¿Deshacer el último cambio realizado en este curso?')) {
      return
    }

    try {
      const response = await fetch(`http://localhost:8080/api/cursos/${courseId}/deshacer`, {
        method: 'POST'
      })

      if (!response.ok) {
        const error = await response.json()
        throw new Error(error.error || 'Error al deshacer cambio')
      }

      const result = await response.json()

      alert(`Cambio deshecho!\n\nOperación: ${result.operacionDeshecha}`)

      // Recargar el curso
      const courseResponse = await fetch(`http://localhost:8080/api/cursos/${courseId}`)
      if (courseResponse.ok) {
        const updatedCourse = await courseResponse.json()
        setCourse(updatedCourse)
      }

      setShowHistoryModal(false)
    } catch (error) {
      console.error('Error al deshacer cambio:', error)
      alert(error instanceof Error ? error.message : 'Error al deshacer cambio.')
    }
  }

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
          <div className="flex gap-2 items-start">
            <span className="bg-primary/10 text-primary px-4 py-2 rounded-lg">
              {course.tipoCurso}
            </span>
            {role !== 'student' && (
              <>
                <Button
                  onClick={handleOpenEditModal}
                  variant="outline"
                  className="border-blue-500 text-blue-600 hover:bg-blue-50"
                  title="Editar curso (Patrón Memento)"
                >
                  Editar
                </Button>
                <Button
                  onClick={handleViewHistory}
                  variant="outline"
                  className="border-purple-500 text-purple-600 hover:bg-purple-50"
                  title="Ver historial de cambios (Patrón Memento)"
                >
                  Historial
                </Button>
                <Button
                  onClick={handleUndo}
                  variant="outline"
                  className="border-orange-500 text-orange-600 hover:bg-orange-50"
                  title="Deshacer último cambio (Patrón Memento)"
                >
                  Deshacer
                </Button>
              </>
            )}
          </div>
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
          {/* Árbol de contenido con Composite Pattern y Proxy Pattern */}
          {courseId ? (
            <CourseContentTree
              courseId={courseId}
              role={mapRole(role)}
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

      {/* Modal de Edición */}
      {showEditModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-background border border-border rounded-lg p-6 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <h2 className="text-xl font-bold text-foreground mb-4">
              Editar Curso
            </h2>
            <p className="text-sm text-muted-foreground mb-4">
              Patrón Memento: Los cambios se guardarán en el historial y podrán deshacerse.
            </p>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-2">
                  Nombre del curso
                </label>
                <input
                  type="text"
                  value={editFormData.nombre || ''}
                  onChange={(e) => setEditFormData({...editFormData, nombre: e.target.value})}
                  className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-foreground mb-2">
                  Descripción
                </label>
                <textarea
                  value={editFormData.descripcion || ''}
                  onChange={(e) => setEditFormData({...editFormData, descripcion: e.target.value})}
                  className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                  rows={3}
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Tipo de curso
                  </label>
                  <select
                    value={editFormData.tipoCurso || ''}
                    onChange={(e) => setEditFormData({...editFormData, tipoCurso: e.target.value})}
                    className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                  >
                    <option value="Virtual">Virtual</option>
                    <option value="Híbrido">Híbrido</option>
                    <option value="Presencial">Presencial</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Duración (horas)
                  </label>
                  <input
                    type="number"
                    value={editFormData.duracion || ''}
                    onChange={(e) => setEditFormData({...editFormData, duracion: parseInt(e.target.value)})}
                    className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Período Académico
                  </label>
                  <input
                    type="text"
                    value={editFormData.periodoAcademico || ''}
                    onChange={(e) => setEditFormData({...editFormData, periodoAcademico: e.target.value})}
                    className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-foreground mb-2">
                    Estrategia de Evaluación
                  </label>
                  <input
                    type="text"
                    value={editFormData.estrategiaEvaluacion || ''}
                    onChange={(e) => setEditFormData({...editFormData, estrategiaEvaluacion: e.target.value})}
                    className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                  />
                </div>
              </div>
            </div>

            <div className="flex gap-3 mt-6">
              <Button
                onClick={() => setShowEditModal(false)}
                variant="outline"
                className="flex-1"
                disabled={saving}
              >
                Cancelar
              </Button>
              <Button
                onClick={handleSaveEdit}
                className="flex-1 bg-primary hover:bg-primary/90 text-primary-foreground"
                disabled={saving}
              >
                {saving ? 'Guardando...' : 'Guardar Cambios'}
              </Button>
            </div>
          </div>
        </div>
      )}

      {/* Modal de Historial */}
      {showHistoryModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-background border border-border rounded-lg p-6 max-w-3xl w-full mx-4 max-h-[90vh] overflow-y-auto">
            <h2 className="text-xl font-bold text-foreground mb-4">
              Historial de Cambios
            </h2>
            <p className="text-sm text-muted-foreground mb-4">
              Patrón Memento: Histórico de hasta 20 cambios reversibles
            </p>

            {loadingHistory ? (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-2"></div>
                <p className="text-muted-foreground">Cargando historial...</p>
              </div>
            ) : history.length === 0 ? (
              <div className="text-center py-8">
                <p className="text-muted-foreground">No hay cambios en el historial</p>
              </div>
            ) : (
              <div className="space-y-3">
                {history.map((item, index) => (
                  <div key={index} className="p-4 rounded-lg bg-muted/50 border border-border">
                    <div className="flex justify-between items-start mb-2">
                      <div>
                        <p className="font-semibold text-foreground">{item.operacion}</p>
                        <p className="text-sm text-muted-foreground">{item.descripcion}</p>
                      </div>
                      <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded">
                        {item.estado}
                      </span>
                    </div>
                    <p className="text-xs text-muted-foreground">
                      {new Date(item.fecha).toLocaleString('es-ES')}
                    </p>
                  </div>
                ))}
              </div>
            )}

            <div className="flex gap-3 mt-6">
              <Button
                onClick={() => setShowHistoryModal(false)}
                variant="outline"
                className="flex-1"
              >
                Cerrar
              </Button>
              {history.length > 0 && (
                <Button
                  onClick={handleUndo}
                  className="flex-1 bg-orange-500 hover:bg-orange-600 text-white"
                >
                  Deshacer Último Cambio
                </Button>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
