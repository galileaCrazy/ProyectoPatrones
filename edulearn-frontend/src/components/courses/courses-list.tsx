'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogTitle } from '@/components/ui/dialog'
import CourseBuilderView from './course-builder'

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api"

const MOCK_COURSES = [
  {
    id: '1',
    name: 'Programación Orientada a Objetos',
    instructor: 'Juan Pérez',
    students: 45,
    progress: 75,
    type: 'Virtual',
    status: 'Activo',
  },
  {
    id: '2',
    name: 'Diseño de Patrones de Software',
    instructor: 'María González',
    students: 38,
    progress: 60,
    type: 'Híbrido',
    status: 'Activo',
  },
  {
    id: '3',
    name: 'Base de Datos Avanzadas',
    instructor: 'Carlos Ramírez',
    students: 52,
    progress: 45,
    type: 'Presencial',
    status: 'Activo',
  },
  {
    id: '4',
    name: 'Desarrollo Web Moderno',
    instructor: 'Laura Martínez',
    students: 60,
    progress: 30,
    type: 'Virtual',
    status: 'Activo',
  },
]

interface CoursesListViewProps {
  role: 'student' | 'professor' | 'admin'
  onSelectCourse: (id: string) => void
}

export default function CoursesListView({ role, onSelectCourse }: CoursesListViewProps) {
  const [selectedFilter, setSelectedFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [courses, setCourses] = useState<any[]>([])
  const [loading, setLoading] = useState(false)

  // Cargar cursos desde la API
  const loadCourses = async () => {
    setLoading(true)
    try {
      const response = await fetch(`${API_URL}/cursos`)
      const data = await response.json()
      setCourses(data)
    } catch (error) {
      console.error('Error loading courses:', error)
      // Fallback a datos mock si falla
      setCourses(MOCK_COURSES as any)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadCourses()
  }, [])

  const filteredCourses = courses.filter(course => {
    const matchesSearch = course.nombre?.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          course.name?.toLowerCase().includes(searchQuery.toLowerCase())
    if (selectedFilter === 'all') return matchesSearch
    const courseType = (course.tipoCurso || course.tipo_curso || course.type || '').toLowerCase()
    return matchesSearch && courseType === selectedFilter.toLowerCase()
  })

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

      {/* Filters */}
      <div className="mb-6 flex gap-4 items-center flex-wrap">
        <div className="flex-1 min-w-64">
          <input
            type="text"
            placeholder="Buscar cursos..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
          />
        </div>
        <div className="flex gap-2">
          {['all', 'Virtual', 'Híbrido', 'Presencial'].map((filter) => (
            <Button
              key={filter}
              onClick={() => setSelectedFilter(filter)}
              className={`${
                selectedFilter === filter
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-muted text-muted-foreground hover:bg-muted/80'
              }`}
            >
              {filter === 'all' ? 'Todos' : filter}
            </Button>
          ))}
        </div>
      </div>

      {/* Courses Grid */}
      {loading ? (
        <div className="text-center py-12 text-muted-foreground">Cargando cursos...</div>
      ) : filteredCourses.length === 0 ? (
        <div className="text-center py-12 text-muted-foreground">No se encontraron cursos</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCourses.map((course) => {
            const courseName = course.nombre || course.name || 'Sin nombre'
            const courseType = course.tipoCurso || course.tipo_curso || course.type || 'N/A'
            const courseStatus = course.estado || course.status || 'N/A'

            return (
              <Card
                key={course.id}
                className="cursor-pointer hover:shadow-lg transition-shadow border-border/50"
                onClick={() => onSelectCourse(course.id.toString())}
              >
                <CardHeader>
                  <div className="flex justify-between items-start mb-2">
                    <CardTitle className="text-lg">{courseName}</CardTitle>
                    <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded capitalize">
                      {courseType}
                    </span>
                  </div>
                  <CardDescription>
                    {course.instructor || course.descripcion || 'Sin descripción'}
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {course.progress !== undefined && (
                      <div>
                        <div className="flex justify-between text-sm mb-2">
                          <span className="text-muted-foreground">Progreso</span>
                          <span className="font-semibold text-foreground">{course.progress}%</span>
                        </div>
                        <div className="w-full h-2 rounded-full bg-muted overflow-hidden">
                          <div
                            className="h-full bg-primary"
                            style={{ width: `${course.progress}%` }}
                          ></div>
                        </div>
                      </div>
                    )}
                    <div className="grid grid-cols-2 gap-4 text-sm pt-2">
                      <div>
                        <p className="text-muted-foreground">Duración</p>
                        <p className="font-semibold text-foreground">{course.duracion || course.duration || 'N/A'}h</p>
                      </div>
                      <div>
                        <p className="text-muted-foreground">Estado</p>
                        <p className="font-semibold text-green-600 capitalize">{courseStatus}</p>
                      </div>
                    </div>
                  </div>
                  <Button
                    onClick={(e) => {
                      e.stopPropagation()
                      onSelectCourse(course.id.toString())
                    }}
                    className="w-full mt-4 bg-primary hover:bg-primary/90 text-primary-foreground"
                  >
                    Ver Detalles
                  </Button>
                </CardContent>
              </Card>
            )
          })}
        </div>
      )}

      {/* Dialog para crear curso con patrón Builder */}
      <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
        <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto p-0">
          <DialogTitle className="sr-only">Crear Nuevo Curso</DialogTitle>
          <CourseBuilderView
            onClose={() => setIsCreateDialogOpen(false)}
            onCourseCreated={() => {
              loadCourses() // Recargar lista de cursos
            }}
          />
        </DialogContent>
      </Dialog>
    </div>
  )
}
