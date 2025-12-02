'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import CourseBuilderView from './course-builder'
import { toast } from '@/components/ui/use-toast'

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api"

interface CoursesListViewProps {
  role: 'student' | 'professor' | 'admin'
  onSelectCourse: (id: string) => void
}

export default function CoursesListView({ role, onSelectCourse }: CoursesListViewProps) {
  const [searchQuery, setSearchQuery] = useState('')
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [courses, setCourses] = useState<any[]>([])
  const [loading, setLoading] = useState(false)
  const [cloningId, setCloningId] = useState<string | null>(null)

  const loadCourses = async () => {
    setLoading(true)
    try {
      const response = await fetch(`${API_URL}/cursos`)
      if (!response.ok) throw new Error('Error al cargar cursos')
      const data = await response.json()
      setCourses(data)
    } catch (error) {
      console.error('Error loading courses:', error)
      toast({
        title: "Error de conexión",
        description: "No se pudieron cargar los cursos",
        variant: "destructive"
      })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadCourses()
  }, [])

  const handleCourseCreated = () => {
    loadCourses()
    toast({
      title: "Curso creado",
      description: "El curso se creó exitosamente"
    })
  }

  // Patrón Prototype – clonar curso
  const handleClone = async (courseId: string) => {
    setCloningId(courseId)
    try {
      const response = await fetch(`${API_URL}/cursos/${courseId}/clonar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      })

      if (!response.ok) {
        const err = await response.json().catch(() => ({}))
        throw new Error(err.message || 'Error al clonar el curso')
      }

      toast({
        title: "Curso clonado",
        description: "Se creó una copia perfecta del curso"
      })
      loadCourses()
    } catch (error: any) {
      toast({
        title: "Error",
        description: error.message,
        variant: "destructive"
      })
    } finally {
      setCloningId(null)
    }
  }

  const filteredCourses = courses.filter(course =>
    course.nombre?.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-start mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">Cursos</h1>
          <p className="text-muted-foreground">
            {role === 'student' ? 'Tus cursos inscritos' : 'Gestión de cursos'}
          </p>
        </div>

        {role !== 'student' && (
          <Button
            onClick={() => setIsCreateDialogOpen(true)}
            className="bg-primary hover:bg-primary/90 text-primary-foreground"
          >
            + Crear Curso
          </Button>
        )}
      </div>

      {/* Búsqueda */}
      <div className="mb-6">
        <input
          type="text"
          placeholder="Buscar curso..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full max-w-md px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
        />
      </div>

      {/* Grid de cursos */}
      {loading ? (
        <div className="text-center py-12">Cargando cursos...</div>
      ) : filteredCourses.length === 0 ? (
        <div className="text-center py-12 text-muted-foreground">No se encontraron cursos</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCourses.map((course) => (
            <Card
              key={course.id}
              className="flex flex-col justify-between hover:shadow-lg transition-shadow cursor-pointer"
              onClick={() => onSelectCourse(course.id.toString())}
            >
              <div>
                <CardHeader>
                  <div className="flex justify-between items-start mb-2">
                    <CardTitle className="text-lg">{course.nombre || 'Sin nombre'}</CardTitle>
                    <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded capitalize">
                      {course.tipoCurso || 'N/A'}
                    </span>
                  </div>
                  <CardDescription>{course.descripcion || 'Sin descripción'}</CardDescription>
                </CardHeader>
                <CardContent>
                  <p className="text-sm text-muted-foreground">
                    Período: {course.periodoAcademico || '-'}
                  </p>
                </CardContent>
              </div>

              <CardContent className="mt-auto">
                <div className="flex gap-2">
                  <Button className="w-full" onClick={(e) => { e.stopPropagation(); onSelectCourse(course.id.toString()) }}>
                    Ver Detalles
                  </Button>

                  {role !== 'student' && (
                    <Button
                      variant="outline"
                      className="w-full"
                      disabled={cloningId === course.id.toString()}
                      onClick={(e) => {
                        e.stopPropagation()
                        handleClone(course.id.toString())
                      }}
                    >
                      {cloningId === course.id.toString() ? 'Clonando...' : 'Clonar'}
                    </Button>
                  )}
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Dialog con el Builder */}
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto p-0">
          <DialogHeader className="px-6 pt-6 border-b">
            <DialogTitle>Crear Nuevo Curso</DialogTitle>
          </DialogHeader>
          <div className="p-6">
            <CourseBuilderView
              onClose={() => setIsCreateDialogOpen(false)}
              onCourseCreated={handleCourseCreated}
              userRole="admin"
              userId="1"
              userName="Admin"
            />
          </div>
        </DialogContent>
      </Dialog>
    </div>
  )
}
