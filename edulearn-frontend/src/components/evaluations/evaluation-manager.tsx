'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
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

export default function EvaluationManager({ role }: EvaluationManagerProps) {
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])
  const [loading, setLoading] = useState(true)
  const [commandManager] = useState(() => new CommandManager())
  const [evaluationService] = useState(() => new EvaluationService())
  const [history, setHistory] = useState<string[]>([])
  const [showHistory, setShowHistory] = useState(false)
  const [showCreateForm, setShowCreateForm] = useState(false)

  // Formulario para nueva evaluación
  const [formData, setFormData] = useState<Partial<Evaluation>>({
    nombre: '',
    cursoId: 1,
    tipoEvaluacion: 'Examen',
    fechaInicio: new Date().toISOString().split('T')[0],
    fechaLimite: '',
    puntajeMaximo: 100,
    estado: 'Borrador',
    duracionMinutos: 60,
    intentosPermitidos: 1
  })

  useEffect(() => {
    loadEvaluations()
  }, [])

  const loadEvaluations = async () => {
    try {
      setLoading(true)
      const response = await fetch('http://localhost:8080/api/evaluaciones')
      if (response.ok) {
        const data = await response.json()
        // Mapear datos del backend al formato del frontend
        const mappedData = data.map((evaluation: any) => ({
          id: evaluation.id,
          nombre: evaluation.nombre || evaluation.titulo,
          cursoId: evaluation.moduloId,
          tipoEvaluacion: evaluation.tipoEvaluacion,
          fechaInicio: evaluation.fechaApertura,
          fechaLimite: evaluation.fechaCierre,
          puntajeMaximo: evaluation.puntajeMaximo,
          estado: evaluation.estado.charAt(0).toUpperCase() + evaluation.estado.slice(1),
          duracionMinutos: evaluation.tiempoLimiteMinutos,
          intentosPermitidos: evaluation.intentosPermitidos
        }))
        setEvaluations(mappedData)
      }
    } catch (error) {
      console.error('Error al cargar evaluaciones:', error)
    } finally {
      setLoading(false)
    }
  }

  const updateHistory = () => {
    setHistory(commandManager.getHistory())
  }

  // ========== COMANDOS ==========

  const handleCreateEvaluation = async () => {
    if (!formData.nombre || !formData.fechaLimite) {
      alert('Por favor complete todos los campos requeridos')
      return
    }

    const command = new CreateEvaluationCommand(
      evaluationService,
      formData as Evaluation,
      (created) => {
        setEvaluations([...evaluations, created])
        setShowCreateForm(false)
        setFormData({
          nombre: '',
          cursoId: 1,
          tipoEvaluacion: 'Examen',
          fechaInicio: new Date().toISOString().split('T')[0],
          fechaLimite: '',
          puntajeMaximo: 100,
          estado: 'Borrador',
          duracionMinutos: 60,
          intentosPermitidos: 1
        })
      }
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Evaluación creada exitosamente')
    } catch (error) {
      alert('Error al crear evaluación')
    }
  }

  const handlePublish = async (id: number) => {
    const command = new PublishEvaluationCommand(
      evaluationService,
      id,
      () => {
        setEvaluations(evaluations.map(e =>
          e.id === id ? { ...e, estado: 'Publicada' } : e
        ))
      }
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
      () => {
        setEvaluations(evaluations.map(e =>
          e.id === id ? { ...e, estado: 'Programada' } : e
        ))
      }
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
      () => {
        setEvaluations(evaluations.map(e =>
          e.id === id ? { ...e, estado: 'Cancelada' } : e
        ))
      }
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
      () => {
        setEvaluations(evaluations.map(e =>
          e.id === id ? { ...e, fechaLimite: newDate } : e
        ))
      }
    )

    try {
      await commandManager.executeCommand(command)
      updateHistory()
      alert('Fecha límite actualizada')
    } catch (error) {
      alert('Error al cambiar fecha')
    }
  }

  // ========== DESHACER / REHACER ==========

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
      case 'Publicada': return 'bg-green-100 text-green-700'
      case 'Programada': return 'bg-blue-100 text-blue-700'
      case 'Borrador': return 'bg-gray-100 text-gray-700'
      case 'Cancelada': return 'bg-red-100 text-red-700'
      case 'Cerrada': return 'bg-purple-100 text-purple-700'
      default: return 'bg-gray-100 text-gray-700'
    }
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
      {/* Header con explicación del patrón */}
      <div className="mb-8">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold text-foreground mb-2">
              Gestión de Evaluaciones
            </h1>
            <p className="text-muted-foreground">
              Patrón Command - Operaciones con Deshacer/Rehacer
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
                <label className="block text-sm font-medium mb-2">Nombre</label>
                <input
                  type="text"
                  value={formData.nombre}
                  onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                  className="w-full px-3 py-2 border rounded-lg"
                  placeholder="Ej: Examen Parcial"
                />
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
                <label className="block text-sm font-medium mb-2">Puntaje Máximo</label>
                <input
                  type="number"
                  value={formData.puntajeMaximo}
                  onChange={(e) => setFormData({ ...formData, puntajeMaximo: parseInt(e.target.value) })}
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

      {/* Lista de Evaluaciones */}
      <div className="space-y-4">
        <h2 className="text-xl font-semibold mb-4">Evaluaciones ({evaluations.length})</h2>
        {evaluations.length === 0 ? (
          <Card>
            <CardContent className="p-8 text-center">
              <p className="text-muted-foreground">No hay evaluaciones disponibles</p>
            </CardContent>
          </Card>
        ) : (
          evaluations.map((evaluation) => (
            <Card key={evaluation.id} className="border-border/50">
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div>
                    <CardTitle className="text-lg">{evaluation.nombre}</CardTitle>
                    <CardDescription>
                      {evaluation.tipoEvaluacion} • Puntaje: {evaluation.puntajeMaximo} pts
                    </CardDescription>
                  </div>
                  <span className={`text-xs px-3 py-1 rounded-full font-semibold ${getEstadoColor(evaluation.estado)}`}>
                    {evaluation.estado}
                  </span>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div className="grid grid-cols-3 gap-4 text-sm">
                    <div>
                      <p className="text-muted-foreground">Fecha Inicio</p>
                      <p className="font-medium">
                        {evaluation.fechaInicio
                          ? new Date(evaluation.fechaInicio).toLocaleDateString('es-ES')
                          : 'No definida'}
                      </p>
                    </div>
                    <div>
                      <p className="text-muted-foreground">Fecha Límite</p>
                      <p className="font-medium">
                        {evaluation.fechaLimite
                          ? new Date(evaluation.fechaLimite).toLocaleDateString('es-ES')
                          : 'No definida'}
                      </p>
                    </div>
                    <div>
                      <p className="text-muted-foreground">Duración</p>
                      <p className="font-medium">
                        {evaluation.duracionMinutos ? `${evaluation.duracionMinutos} min` : 'Sin límite'}
                      </p>
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
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  )
}
