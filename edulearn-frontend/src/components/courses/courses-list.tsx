'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { SearchEngine } from '@/patterns/interpreter/search-interpreter'

interface Curso {
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

interface CoursesListViewProps {
  role: 'student' | 'professor' | 'admin'
  onSelectCourse: (id: string) => void
  onCreateCourse?: () => void
}

export default function CoursesListView({ role, onSelectCourse, onCreateCourse }: CoursesListViewProps) {
  const [selectedFilter, setSelectedFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')
  const [courses, setCourses] = useState<Curso[]>([])
  const [filteredCourses, setFilteredCourses] = useState<Curso[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showAdvancedHelp, setShowAdvancedHelp] = useState(false)
  const searchEngine = new SearchEngine()

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true)
        setError(null)

        // Obtener usuario del localStorage
        const usuarioStr = localStorage.getItem('usuario')
        if (!usuarioStr) {
          throw new Error('No hay usuario autenticado')
        }

        const usuario = JSON.parse(usuarioStr)
        const userId = usuario.id

        // Mapear rol del frontend al backend
        const rolMap: Record<string, string> = {
          'student': 'estudiante',
          'professor': 'profesor',
          'admin': 'administrador'
        }

        const rolBackend = rolMap[role] || 'estudiante'

        // Usar endpoint con Chain of Responsibility pattern
        const url = `http://localhost:8080/api/cursos/por-usuario/${userId}?rol=${rolBackend}`

        // Llamar al endpoint del backend
        const response = await fetch(url)

        if (!response.ok) {
          const errorText = await response.text()
          console.error('Error del servidor:', response.status, errorText)
          throw new Error(`Error al cargar los cursos: ${response.status} - ${errorText}`)
        }

        const data = await response.json()
        console.log('Cursos cargados:', data)
        setCourses(data)
        setFilteredCourses(data)
      } catch (err) {
        console.error('Error al cargar cursos:', err)
        setError(err instanceof Error ? err.message : 'Error desconocido')
      } finally {
        setLoading(false)
      }
    }

    fetchCourses()
  }, [role])

  const handleSearch = (query: string) => {
    setSearchQuery(query)

    if (!query.trim()) {
      // Si no hay búsqueda, aplicar solo filtro de tipo
      const filtered = selectedFilter === 'all'
        ? courses
        : courses.filter(c => c.tipoCurso.toLowerCase() === selectedFilter.toLowerCase())
      setFilteredCourses(filtered)
      return
    }

    try {
      // Usar el motor de búsqueda del patrón Interpreter
      let results = searchEngine.search(courses, query)

      // Aplicar filtro de tipo adicional
      if (selectedFilter !== 'all') {
        results = results.filter(c => c.tipoCurso.toLowerCase() === selectedFilter.toLowerCase())
      }

      setFilteredCourses(results)
    } catch (error) {
      console.error('Error en búsqueda:', error)
      setFilteredCourses([])
    }
  }

  const handleFilterChange = (filter: string) => {
    setSelectedFilter(filter)

    // Re-aplicar búsqueda con nuevo filtro
    if (!searchQuery.trim()) {
      const filtered = filter === 'all'
        ? courses
        : courses.filter(c => c.tipoCurso.toLowerCase() === filter.toLowerCase())
      setFilteredCourses(filtered)
    } else {
      try {
        let results = searchEngine.search(courses, searchQuery)
        if (filter !== 'all') {
          results = results.filter(c => c.tipoCurso.toLowerCase() === filter.toLowerCase())
        }
        setFilteredCourses(results)
      } catch (error) {
        console.error('Error en búsqueda:', error)
        setFilteredCourses([])
      }
    }
  }

  if (loading) {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Cargando cursos...</p>
          </div>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <div className="bg-red-50 border border-red-200 rounded-lg p-4">
          <p className="text-red-800">Error: {error}</p>
        </div>
      </div>
    )
  }

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
            onClick={onCreateCourse}
            className="bg-primary hover:bg-primary/90 text-primary-foreground"
          >
            + Crear Curso
          </Button>
        )}
      </div>

      {/* Búsqueda Avanzada */}
      <Card className="mb-6 border-border/50">
        <CardContent className="p-4">
          <div className="space-y-4">
            <div className="flex gap-3 items-start">
              <div className="flex-1">
                <input
                  type="text"
                  placeholder="Buscar cursos... (ej: nombre:Programación AND estado:Activo)"
                  value={searchQuery}
                  onChange={(e) => handleSearch(e.target.value)}
                  className="w-full px-4 py-2.5 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary text-sm"
                />
              </div>
              {searchQuery && (
                <Button
                  onClick={() => handleSearch('')}
                  variant="outline"
                  className="text-sm"
                >
                  Limpiar
                </Button>
              )}
              <Button
                onClick={() => setShowAdvancedHelp(!showAdvancedHelp)}
                variant="outline"
                className="text-sm"
              >
                {showAdvancedHelp ? 'Ocultar ayuda' : 'Ayuda'}
              </Button>
            </div>

            {showAdvancedHelp && (
              <div className="bg-muted/30 rounded-lg p-4">
                <h3 className="font-semibold text-foreground mb-3 text-sm">
                  Sintaxis de búsqueda avanzada
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
                  <div>
                    <p className="text-muted-foreground mb-2 font-medium">Campos disponibles:</p>
                    <ul className="space-y-1 text-muted-foreground">
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">nombre</code> - Nombre del curso</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">codigo</code> - Código del curso</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">estado</code> - Estado (Activo/Inactivo)</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">tipoCurso</code> - Tipo (Virtual/Híbrido/Presencial)</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">duracion</code> - Duración en horas</li>
                    </ul>
                  </div>
                  <div>
                    <p className="text-muted-foreground mb-2 font-medium">Operadores:</p>
                    <ul className="space-y-1 text-muted-foreground">
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo:valor</code> - Contiene texto</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo&gt;=valor</code> - Mayor o igual</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">expr AND expr</code> - Ambas condiciones</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">expr OR expr</code> - Al menos una</li>
                    </ul>
                    <p className="text-muted-foreground mt-3 font-medium">Ejemplos:</p>
                    <ul className="space-y-1 text-muted-foreground">
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">nombre:Programación</code></li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">estado:Activo AND tipoCurso:Virtual</code></li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">duracion&gt;=40</code></li>
                    </ul>
                  </div>
                </div>
              </div>
            )}

            <div className="flex gap-2 flex-wrap">
              <span className="text-sm text-muted-foreground self-center">Tipo de curso:</span>
              {['all', 'Virtual', 'Híbrido', 'Presencial'].map((filter) => (
                <Button
                  key={filter}
                  onClick={() => handleFilterChange(filter)}
                  className={`text-sm ${
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
        </CardContent>
      </Card>

      {/* Courses Grid */}
      {filteredCourses.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-muted-foreground text-lg">No hay cursos disponibles</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCourses.map((course) => (
            <Card
              key={course.id}
              className="cursor-pointer hover:shadow-lg transition-shadow border-border/50"
              onClick={() => onSelectCourse(course.id.toString())}
            >
              <CardHeader>
                <div className="flex justify-between items-start mb-2">
                  <CardTitle className="text-lg">{course.nombre}</CardTitle>
                  <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded">
                    {course.tipoCurso}
                  </span>
                </div>
                <CardDescription>{course.codigo}</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div>
                    <p className="text-sm text-muted-foreground mb-2">{course.descripcion}</p>
                  </div>
                  <div className="grid grid-cols-2 gap-4 text-sm pt-2 border-t">
                    <div>
                      <p className="text-muted-foreground">Duración</p>
                      <p className="font-semibold text-foreground">{course.duracion} horas</p>
                    </div>
                    <div>
                      <p className="text-muted-foreground">Estado</p>
                      <p className={`font-semibold ${
                        course.estado === 'Activo' ? 'text-green-600' : 'text-gray-600'
                      }`}>
                        {course.estado}
                      </p>
                    </div>
                  </div>
                  <div className="text-sm">
                    <p className="text-muted-foreground">Período</p>
                    <p className="font-semibold text-foreground">{course.periodoAcademico}</p>
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
          ))}
        </div>
      )}
    </div>
  )
}
