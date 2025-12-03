'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import {
  CommandManager,
  EvaluationService,
  CreateEvaluationCommand,
  PublishEvaluationCommand,
  CancelEvaluationCommand,
  ChangeDueDateCommand,
  ScheduleEvaluationCommand,
  Evaluation
} from '@/patterns/command/evaluation-commands'

interface EvaluationManagerProps {
  role: 'student' | 'professor' | 'admin'
}

interface Course {
  id: number
  nombre: string
  codigo: string
}

interface EvaluationsByCourse {
  [courseId: number]: {
    course: Course
    evaluations: Evaluation[]
  }
}

export default function EvaluationManager({ role }: EvaluationManagerProps) {
  const [evaluationsByCourse, setEvaluationsByCourse] = useState<EvaluationsByCourse>({})
  const [courses, setCourses] = useState<Course[]>([])
  const [loading, setLoading] = useState(true)
  const [commandManager] = useState(() => new CommandManager())
  const [evaluationService] = useState(() => new EvaluationService())
  const [history, setHistory] = useState<string[]>([])
  const [showHistory, setShowHistory] = useState(false)
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [selectedCourseFilter, setSelectedCourseFilter] = useState<number | 'all'>('all')

  const [formData, setFormData] = useState<Partial<Evaluation>>({
    nombre: '',
    cursoId: 0,
    tipoEvaluacion: 'Examen',
    fechaInicio: new Date().toISOString().split('T')[0],
    fechaLimite: '',
    puntajeMaximo: 100,
    estado: 'Borrador',
    duracionMinutos: 60,
    intentosPermitidos: 1
  })

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    await Promise.all([loadCourses(), loadEvaluations()])
  }

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

  const loadEvaluations = async () => {
    try {
      setLoading(true)
      const response = await fetch('http://localhost:8080/api/evaluaciones')
      if (response.ok) {
        const data = await response.json()
        const mappedData = data.map((evaluation: any) => ({
          id: evaluation.id,
          nombre: evaluation.nombre || evaluation.titulo,
          cursoId: evaluation.moduloId || 0,
          tipoEvaluacion: evaluation.tipoEvaluacion,
          fechaInicio: evaluation.fechaApertura,
          fechaLimite: evaluation.fechaCierre,
          puntajeMaximo: evaluation.puntajeMaximo,
          estado: evaluation.estado.charAt(0).toUpperCase() + evaluation.estado.slice(1),
          duracionMinutos: evaluation.tiempoLimiteMinutos,
          intentosPermitidos: evaluation.intentosPermitidos
        }))

        // Agrupar evaluaciones por curso
        const grouped: EvaluationsByCourse = {}
        mappedData.forEach((evaluation: Evaluation) => {
          const courseId = evaluation.cursoId || 0
          if (!grouped[courseId]) {
            grouped[courseId] = {
              course: { id: courseId, nombre: 'Sin asignar', codigo: 'N/A' },
              evaluations: []
            }
          }
          grouped[courseId].evaluations.push(evaluation)
        })

        setEvaluationsByCourse(grouped)
      }
    } catch (error) {
      console.error('Error al cargar evaluaciones:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    // Actualizar información de cursos en evaluaciones agrupadas
    const updated = { ...evaluationsByCourse }
    Object.keys(updated).forEach(courseIdStr => {
      const courseId = parseInt(courseIdStr)
      const course = courses.find(c => c.id === courseId)
      if (course) {
        updated[courseId].course = course
      }
    })
    setEvaluationsByCourse(updated)
  }, [courses])

  const updateHistory = () => {
    setHistory(commandManager.getHistory())
  }

  const handleCreateEvaluation = async () => {
    const command = new CreateEvaluationCommand(
      evaluationService,
      formData as Evaluation,
      () => {
        loadEvaluations()
      }
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Evaluación creada exitosamente')
      setShowCreateForm(false)
      setFormData({
        nombre: '',
        cursoId: 0,
        tipoEvaluacion: 'Examen',
        fechaInicio: new Date().toISOString().split('T')[0],
        fechaLimite: '',
        puntajeMaximo: 100,
        estado: 'Borrador',
        duracionMinutos: 60,
        intentosPermitidos: 1
      })
    } catch (error) {
      alert('Error al crear evaluación')
    }
  }

  const handlePublish = async (id: number) => {
    const command = new PublishEvaluationCommand(
      evaluationService,
      id,
      () => loadEvaluations()
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Evaluación publicada')
    } catch (error) {
      alert('Error al publicar evaluación')
    }
  }

  const handleSchedule = async (id: number) => {
    const command = new ScheduleEvaluationCommand(
      evaluationService,
      id,
      () => loadEvaluations()
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Evaluación programada')
    } catch (error) {
      alert('Error al programar evaluación')
    }
  }

  const handleCancel = async (id: number) => {
    const command = new CancelEvaluationCommand(
      evaluationService,
      id,
      () => loadEvaluations()
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Evaluación cancelada')
    } catch (error) {
      alert('Error al cancelar evaluación')
    }
  }

  const handleChangeDueDate = async (id: number) => {
    const newDate = prompt('Ingrese la nueva fecha límite (YYYY-MM-DD):')
    if (!newDate) return

    const command = new ChangeDueDateCommand(
      evaluationService,
      id,
      newDate,
      () => loadEvaluations()
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Fecha límite actualizada')
    } catch (error) {
      alert('Error al cambiar fecha')
    }
  }

  const handleUndo = async () => {
    const success = await commandManager.undo()
    if (success) {
      await loadEvaluations()
      updateHistory()
      alert('Acción deshecha')
    } else {
      alert('No hay acciones para deshacer')
    }
  }

  const handleRedo = async () => {
    const success = await commandManager.redo()
    if (success) {
      await loadEvaluations()
      updateHistory()
      alert('Acción rehecha')
    } else {
      alert('No hay acciones para rehacer')
    }
  }

  const getEstadoColor = (estado: string) => {
    switch (estado) {
      case 'Borrador': return 'bg-gray-100 text-gray-700'
      case 'Programada': return 'bg-blue-100 text-blue-700'
      case 'Publicada': return 'bg-green-100 text-green-700'
      case 'Activa': return 'bg-emerald-100 text-emerald-700'
      case 'Cancelada': return 'bg-red-100 text-red-700'
      case 'Cerrada': return 'bg-purple-100 text-purple-700'
      default: return 'bg-gray-100 text-gray-700'
    }
  }

  const getTipoColor = (tipo: string) => {
    switch (tipo) {
      case 'Examen': return 'bg-red-50 text-red-700 border-red-200'
      case 'Quiz': return 'bg-blue-50 text-blue-700 border-blue-200'
      case 'Tarea': return 'bg-green-50 text-green-700 border-green-200'
      case 'Proyecto': return 'bg-purple-50 text-purple-700 border-purple-200'
      default: return 'bg-gray-50 text-gray-700 border-gray-200'
    }
  }

  const getFilteredCourses = () => {
    if (selectedCourseFilter === 'all') {
      return Object.values(evaluationsByCourse)
    }
    const filtered = evaluationsByCourse[selectedCourseFilter]
    return filtered ? [filtered] : []
  }

  if (loading) {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Cargando evaluaciones...</p>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="mb-8">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold text-foreground mb-2">
              Gestión de Evaluaciones
            </h1>
            <p className="text-muted-foreground">
              Por Cursos
            </p>
          </div>
          {role !== 'student' && (
            <Button
              onClick={() => setShowCreateForm(true)}
              className="bg-primary hover:bg-primary/90 text-primary-foreground"
            >
              + Nueva Evaluación
            </Button>
          )}
        </div>

        {/* Filtros por curso */}
        <Card className="border-border/50">
          <CardContent className="p-4">
            <div className="flex items-center gap-4">
              <span className="text-sm font-medium text-muted-foreground whitespace-nowrap">
                Filtrar por:
              </span>
              <div className="flex items-center gap-3 flex-1">
                <button
                  onClick={() => setSelectedCourseFilter('all')}
                  className={`px-6 py-2.5 rounded-lg text-sm font-medium transition-all ${
                    selectedCourseFilter === 'all'
                      ? 'bg-primary text-primary-foreground shadow-sm'
                      : 'bg-muted/50 text-muted-foreground hover:bg-muted hover:text-foreground'
                  }`}
                >
                  Todos los cursos
                </button>
                <span className="text-muted-foreground">o</span>
                <select
                  value={selectedCourseFilter === 'all' ? '' : selectedCourseFilter}
                  onChange={(e) => setSelectedCourseFilter(e.target.value ? parseInt(e.target.value) : 'all')}
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

      {/* Controles de Undo/Redo */}
      {role !== 'student' && (
        <div className="mb-6 flex gap-4 items-center">
          <Button
            onClick={handleUndo}
            disabled={!commandManager.canUndo()}
            className="bg-orange-600 hover:bg-orange-700 text-white disabled:bg-gray-300"
          >
            Deshacer
          </Button>
          <Button
            onClick={handleRedo}
            disabled={!commandManager.canRedo()}
            className="bg-green-600 hover:bg-green-700 text-white disabled:bg-gray-300"
          >
            Rehacer
          </Button>
          <Button
            onClick={() => setShowHistory(!showHistory)}
            className="bg-purple-600 hover:bg-purple-700 text-white"
          >
            {showHistory ? 'Ocultar' : 'Ver'} Historial
          </Button>
        </div>
      )}

      {/* Historial de Comandos */}
      {showHistory && (
        <Card className="mb-6 border-purple-200">
          <CardHeader>
            <CardTitle className="text-lg">Historial de Comandos</CardTitle>
            <CardDescription>Últimas {history.length} acciones ejecutadas</CardDescription>
          </CardHeader>
          <CardContent>
            {history.length === 0 ? (
              <p className="text-muted-foreground text-sm">No hay comandos en el historial</p>
            ) : (
              <ul className="space-y-2">
                {history.map((cmd, index) => (
                  <li key={index} className="flex items-center gap-2 text-sm">
                    <span className="text-purple-600 font-semibold">{index + 1}.</span>
                    <span className="text-foreground">{cmd}</span>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>
      )}

      {/* Formulario de Creación */}
      {showCreateForm && (
        <Card className="mb-6 border-green-200">
          <CardHeader>
            <CardTitle>Crear Nueva Evaluación</CardTitle>
            <CardDescription>Complete los datos para crear una evaluación</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-2">Nombre de la Evaluación</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  className="w-full px-3 py-2 border rounded-lg"
                  placeholder="Ej: Examen Final"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Curso</label>
                <select
                  value={formData.cursoId}
                  onChange={(e) => setFormData({ ...formData, cursoId: parseInt(e.target.value) })}
                  className="w-full px-3 py-2 border rounded-lg"
                >
                  <option value={0}>Seleccione un curso</option>
                  {courses.map(course => (
                    <option key={course.id} value={course.id}>
                      {course.codigo} - {course.nombre}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Tipo</label>
                <select
                  value={formData.tipoEvaluacion}
                  onChange={(e) => setFormData({ ...formData, tipoEvaluacion: e.target.value })}
                  className="w-full px-3 py-2 border rounded-lg"
                >
                  <option value="Examen">Examen</option>
                  <option value="Quiz">Quiz</option>
                  <option value="Tarea">Tarea</option>
                  <option value="Proyecto">Proyecto</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Puntaje Máximo</label>
                <input
                  type="number"
                  value={formData.puntajeMaximo}
                  onChange={(e) => setFormData({ ...formData, puntajeMaximo: parseInt(e.target.value) })}
                  className="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Fecha Inicio</label>
                <input
                  type="date"
                  value={formData.fechaInicio}
                  onChange={(e) => setFormData({ ...formData, fechaInicio: e.target.value })}
                  className="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Fecha Límite</label>
                <input
                  type="date"
                  value={formData.fechaLimite}
                  onChange={(e) => setFormData({ ...formData, fechaLimite: e.target.value })}
                  className="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Duración (minutos)</label>
                <input
                  type="number"
                  value={formData.duracionMinutos}
                  onChange={(e) => setFormData({ ...formData, duracionMinutos: parseInt(e.target.value) })}
                  className="w-full px-3 py-2 border rounded-lg"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Intentos Permitidos</label>
                <input
                  type="number"
                  value={formData.intentosPermitidos}
                  onChange={(e) => setFormData({ ...formData, intentosPermitidos: parseInt(e.target.value) })}
                  className="w-full px-3 py-2 border rounded-lg"
                />
              </div>
            </div>
            <div className="flex gap-3 mt-6">
              <Button onClick={handleCreateEvaluation} className="bg-green-600 hover:bg-green-700 text-white">
                Crear Evaluación
              </Button>
              <Button onClick={() => setShowCreateForm(false)} className="bg-gray-500 hover:bg-gray-600 text-white">
                Cancelar
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Lista de Evaluaciones Agrupadas por Curso */}
      <div className="space-y-6">
        {getFilteredCourses().length === 0 ? (
          <Card>
            <CardContent className="p-8 text-center">
              <p className="text-muted-foreground">No hay evaluaciones disponibles</p>
            </CardContent>
          </Card>
        ) : (
          getFilteredCourses().map(({ course, evaluations }) => (
            <Card key={course.id} className="border-border/50 overflow-hidden">
              <CardHeader className="bg-muted/30">
                <div className="flex justify-between items-center">
                  <div>
                    <CardTitle className="text-xl">{course.nombre}</CardTitle>
                    <CardDescription className="text-sm mt-1">
                      Código: {course.codigo} • {evaluations.length} evaluacion(es)
                    </CardDescription>
                  </div>
                  <Badge variant="outline" className="text-sm px-3 py-1">
                    {evaluations.length} Evaluaciones
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="p-6">
                <div className="space-y-4">
                  {evaluations.map((evaluation: Evaluation) => (
                    <Card key={evaluation.id} className="border-l-4 border-l-primary/50">
                      <CardContent className="p-4">
                        <div className="flex justify-between items-start mb-3">
                          <div className="flex-1">
                            <div className="flex items-center gap-3 mb-2">
                              <h3 className="text-lg font-semibold text-foreground">
                                {evaluation.nombre}
                              </h3>
                              <Badge className={getTipoColor(evaluation.tipoEvaluacion)}>
                                {evaluation.tipoEvaluacion}
                              </Badge>
                              <span className={`text-xs px-3 py-1 rounded-full font-semibold ${getEstadoColor(evaluation.estado)}`}>
                                {evaluation.estado}
                              </span>
                            </div>
                            <div className="grid grid-cols-4 gap-4 text-sm mt-3">
                              <div>
                                <p className="text-muted-foreground text-xs">Fecha Inicio</p>
                                <p className="font-medium">
                                  {evaluation.fechaInicio
                                    ? new Date(evaluation.fechaInicio).toLocaleDateString('es-ES')
                                    : 'No definida'}
                                </p>
                              </div>
                              <div>
                                <p className="text-muted-foreground text-xs">Fecha Límite</p>
                                <p className="font-medium">
                                  {evaluation.fechaLimite
                                    ? new Date(evaluation.fechaLimite).toLocaleDateString('es-ES')
                                    : 'No definida'}
                                </p>
                              </div>
                              <div>
                                <p className="text-muted-foreground text-xs">Duración</p>
                                <p className="font-medium">
                                  {evaluation.duracionMinutos ? `${evaluation.duracionMinutos} min` : 'Sin límite'}
                                </p>
                              </div>
                              <div>
                                <p className="text-muted-foreground text-xs">Puntaje Máximo</p>
                                <p className="font-medium">{evaluation.puntajeMaximo} pts</p>
                              </div>
                            </div>
                          </div>
                        </div>

                        {role !== 'student' && (
                          <div className="flex gap-2 pt-3 border-t">
                            {evaluation.estado === 'Borrador' && (
                              <>
                                <Button
                                  onClick={() => handleSchedule(evaluation.id!)}
                                  className="bg-blue-600 hover:bg-blue-700 text-white text-sm"
                                >
                                  Programar
                                </Button>
                                <Button
                                  onClick={() => handlePublish(evaluation.id!)}
                                  className="bg-green-600 hover:bg-green-700 text-white text-sm"
                                >
                                  Publicar
                                </Button>
                              </>
                            )}
                            {(evaluation.estado === 'Programada' || evaluation.estado === 'Publicada') && (
                              <>
                                <Button
                                  onClick={() => handleChangeDueDate(evaluation.id!)}
                                  className="bg-purple-600 hover:bg-purple-700 text-white text-sm"
                                >
                                  Cambiar Fecha
                                </Button>
                                <Button
                                  onClick={() => handleCancel(evaluation.id!)}
                                  className="bg-red-600 hover:bg-red-700 text-white text-sm"
                                >
                                  Cancelar
                                </Button>
                              </>
                            )}
                          </div>
                        )}
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  )
}
