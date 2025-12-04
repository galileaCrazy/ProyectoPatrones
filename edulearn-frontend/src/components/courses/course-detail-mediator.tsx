'use client'

import { useState, useEffect, useRef } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { CourseContentTree } from './course-content-tree'
import {
  CourseMediator,
  ContentModule,
  EvaluationModule,
  ForumModule
} from '@/patterns/mediator/course-mediator'

interface CourseDetailMediatorProps {
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

interface Evaluation {
  id: number
  nombre: string
  tipo: string
  puntajeMaximo: number
  estado: string
  fechaInicio?: string
  fechaLimite?: string
}

interface Discussion {
  id: number
  topic: string
  author: string
  contentId?: number
  replies: number
  createdAt: string
}

export default function CourseDetailMediator({ courseId, role, onBack }: CourseDetailMediatorProps) {
  const [activeTab, setActiveTab] = useState('content')
  const [selectedNode, setSelectedNode] = useState<any>(null)
  const [course, setCourse] = useState<Course | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Estados de los módulos
  const [evaluations, setEvaluations] = useState<Evaluation[]>([])
  const [discussions, setDiscussions] = useState<Discussion[]>([])
  const [contentLocked, setContentLocked] = useState(false)
  const [evaluationInProgress, setEvaluationInProgress] = useState(false)
  const [forumExamMode, setForumExamMode] = useState(false)
  const [highlightedContent, setHighlightedContent] = useState<number | null>(null)
  const [filteredEvaluations, setFilteredEvaluations] = useState<Evaluation[]>([])
  const [filteredDiscussions, setFilteredDiscussions] = useState<Discussion[]>([])

  // Referencias al Mediator y módulos
  const mediatorRef = useRef<CourseMediator | null>(null)
  const contentModuleRef = useRef<ContentModule | null>(null)
  const evaluationModuleRef = useRef<EvaluationModule | null>(null)
  const forumModuleRef = useRef<ForumModule | null>(null)

  // Inicializar Mediator y módulos
  useEffect(() => {
    // Crear mediator
    const mediator = new CourseMediator()
    mediatorRef.current = mediator

    // Crear módulos con callbacks
    const contentModule = new ContentModule((data) => {
      console.log('[Content Module Update]:', data)

      switch (data.action) {
        case 'lock-content':
          setContentLocked(true)
          break
        case 'unlock-content':
          setContentLocked(false)
          break
        case 'highlight-content':
          setHighlightedContent(data.contentId)
          setTimeout(() => setHighlightedContent(null), 3000)
          break
      }
    })

    const evaluationModule = new EvaluationModule((data) => {
      console.log('[Evaluation Module Update]:', data)

      switch (data.action) {
        case 'evaluation:started':
          setEvaluationInProgress(true)
          break
        case 'evaluation:submitted':
          setEvaluationInProgress(false)
          break
        case 'filter-by-content':
          const filtered = evaluations.filter(e => e.id === data.contentId)
          setFilteredEvaluations(filtered.length > 0 ? filtered : evaluations)
          break
      }
    })

    const forumModule = new ForumModule((data) => {
      console.log('[Forum Module Update]:', data)

      switch (data.action) {
        case 'enable-exam-mode':
          setForumExamMode(true)
          break
        case 'disable-exam-mode':
          setForumExamMode(false)
          break
        case 'filter-discussions':
          const filtered = discussions.filter(d => d.contentId === data.contentId)
          setFilteredDiscussions(filtered.length > 0 ? filtered : discussions)
          break
        case 'discussion:created':
          setDiscussions(prev => [...prev, data.discussion])
          break
      }
    })

    // Registrar módulos en el mediator
    mediator.registerModule(contentModule)
    mediator.registerModule(evaluationModule)
    mediator.registerModule(forumModule)

    // Guardar referencias
    contentModuleRef.current = contentModule
    evaluationModuleRef.current = evaluationModule
    forumModuleRef.current = forumModule

    return () => {
      // Cleanup
      mediator.unregisterModule('content')
      mediator.unregisterModule('evaluation')
      mediator.unregisterModule('forum')
    }
  }, [])

  // Cargar datos del curso
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

        // Cargar evaluaciones del curso
        loadEvaluations(parseInt(courseId))

        // Cargar discusiones
        loadDiscussions()

      } catch (err) {
        console.error('Error al cargar curso:', err)
        setError(err instanceof Error ? err.message : 'Error desconocido')
      } finally {
        setLoading(false)
      }
    }

    fetchCourse()
  }, [courseId])

  const loadEvaluations = async (cursoId: number) => {
    try {
      const response = await fetch('http://localhost:8080/api/evaluaciones')
      if (response.ok) {
        const data = await response.json()
        const courseEvals = data
          .filter((e: any) => e.moduloId === cursoId)
          .map((e: any) => ({
            id: e.id,
            nombre: e.nombre || e.titulo,
            tipo: e.tipoEvaluacion,
            puntajeMaximo: e.puntajeMaximo,
            estado: e.estado,
            fechaInicio: e.fechaApertura,
            fechaLimite: e.fechaCierre
          }))

        setEvaluations(courseEvals)
        setFilteredEvaluations(courseEvals)

        // Notificar al módulo
        if (evaluationModuleRef.current) {
          evaluationModuleRef.current.loadEvaluations(courseEvals)
        }
      }
    } catch (error) {
      console.error('Error al cargar evaluaciones:', error)
    }
  }

  const loadDiscussions = () => {
    // Datos de ejemplo para el foro
    const sampleDiscussions: Discussion[] = [
      { id: 1, topic: 'Duda sobre Patrón Singleton', author: 'Juan Pérez', contentId: 1, replies: 5, createdAt: new Date().toISOString() },
      { id: 2, topic: 'Ayuda con Factory Method', author: 'María García', contentId: 2, replies: 3, createdAt: new Date().toISOString() },
      { id: 3, topic: 'Proyecto final - Observer', author: 'Carlos Ruiz', replies: 8, createdAt: new Date().toISOString() }
    ]

    setDiscussions(sampleDiscussions)
    setFilteredDiscussions(sampleDiscussions)

    if (forumModuleRef.current) {
      forumModuleRef.current.loadDiscussions(sampleDiscussions)
    }
  }

  // Handlers con Mediator
  const handleContentSelect = (node: any) => {
    setSelectedNode(node)

    if (contentModuleRef.current && node?.id) {
      contentModuleRef.current.selectContent(node.id)
    }
  }

  const handleStartEvaluation = (evaluationId: number) => {
    if (evaluationModuleRef.current) {
      evaluationModuleRef.current.startEvaluation(evaluationId)
    }
  }

  const handleSubmitEvaluation = (evaluationId: number) => {
    if (evaluationModuleRef.current) {
      evaluationModuleRef.current.submitEvaluation(evaluationId, {})
    }
  }

  const handleCompleteContent = (contentId: number) => {
    if (contentModuleRef.current) {
      contentModuleRef.current.completeContent(contentId)
    }
  }

  const handleCreateDiscussion = () => {
    const topic = prompt('Tema de la discusión:')
    if (topic && forumModuleRef.current) {
      forumModuleRef.current.createDiscussion(topic, selectedNode?.id)
    }
  }

  const handleRequestHelp = (discussionId: number) => {
    if (forumModuleRef.current) {
      forumModuleRef.current.requestHelp(discussionId)
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
            {contentLocked && (
              <Badge variant="destructive">Contenido Bloqueado</Badge>
            )}
            {evaluationInProgress && (
              <Badge className="bg-orange-500">Evaluación en Progreso</Badge>
            )}
            {forumExamMode && (
              <Badge className="bg-yellow-500">Foro - Modo Examen</Badge>
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
        {['content', 'evaluaciones', 'foro'].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-3 font-medium transition-colors border-b-2 ${
              activeTab === tab
                ? 'border-primary text-primary'
                : 'border-transparent text-muted-foreground hover:text-foreground'
            }`}
          >
            {tab === 'content' ? 'Contenido' : tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      {/* Content Tab */}
      {activeTab === 'content' && (
        <div className="space-y-6">
          <div className="flex justify-between items-center">
            <h2 className="text-2xl font-bold text-foreground">Contenido del Curso</h2>
            {contentLocked && (
              <Badge variant="destructive" className="text-sm">
                Bloqueado durante evaluación
              </Badge>
            )}
          </div>

          {courseId ? (
            <CourseContentTree
              courseId={courseId}
              role={role === 'student' ? 'ESTUDIANTE' : role === 'professor' ? 'DOCENTE' : 'ADMIN'}
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

          {selectedNode && (
            <Card className={`border-border/50 ${
              highlightedContent === selectedNode.id ? 'ring-2 ring-primary animate-pulse' : 'bg-accent/5'
            }`}>
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div>
                    <CardTitle>{selectedNode.nombre}</CardTitle>
                    <CardDescription>
                      Tipo: {selectedNode.tipo} | Duración: {Math.ceil((selectedNode.duracionTotal || 0) / 60)} min
                    </CardDescription>
                  </div>
                  {!contentLocked && (
                    <Button
                      onClick={() => handleCompleteContent(selectedNode.id)}
                      className="bg-green-600 hover:bg-green-700"
                    >
                      Marcar como Completado
                    </Button>
                  )}
                </div>
              </CardHeader>
              <CardContent>
                {selectedNode.descripcion && (
                  <p className="text-muted-foreground mb-4">{selectedNode.descripcion}</p>
                )}
              </CardContent>
            </Card>
          )}
        </div>
      )}

      {/* Evaluaciones Tab */}
      {activeTab === 'evaluaciones' && (
        <div className="space-y-6">
          <div className="flex justify-between items-center">
            <h2 className="text-2xl font-bold text-foreground">Evaluaciones</h2>
            <Badge variant="outline">
              {filteredEvaluations.length} evaluacion(es)
            </Badge>
          </div>

          {filteredEvaluations.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center">
                <p className="text-muted-foreground">No hay evaluaciones disponibles</p>
              </CardContent>
            </Card>
          ) : (
            <div className="grid gap-4">
              {filteredEvaluations.map((eval_) => (
                <Card key={eval_.id} className="border-border/50">
                  <CardContent className="p-6">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="text-lg font-semibold text-foreground mb-2">{eval_.nombre}</h3>
                        <div className="flex gap-3 mb-3">
                          <Badge className="bg-blue-100 text-blue-700">{eval_.tipo}</Badge>
                          <Badge variant="outline">{eval_.puntajeMaximo} pts</Badge>
                          <Badge className={
                            eval_.estado === 'publicada' ? 'bg-green-100 text-green-700' :
                            eval_.estado === 'borrador' ? 'bg-gray-100 text-gray-700' :
                            'bg-orange-100 text-orange-700'
                          }>
                            {eval_.estado}
                          </Badge>
                        </div>
                        {eval_.fechaLimite && (
                          <p className="text-sm text-muted-foreground">
                            Fecha límite: {new Date(eval_.fechaLimite).toLocaleDateString('es-ES')}
                          </p>
                        )}
                      </div>
                      <div className="flex gap-2">
                        {!evaluationInProgress && eval_.estado === 'publicada' && (
                          <Button
                            onClick={() => handleStartEvaluation(eval_.id)}
                            className="bg-primary hover:bg-primary/90"
                          >
                            Iniciar
                          </Button>
                        )}
                        {evaluationInProgress && evaluationModuleRef.current?.getActiveEvaluation() === eval_.id && (
                          <Button
                            onClick={() => handleSubmitEvaluation(eval_.id)}
                            className="bg-green-600 hover:bg-green-700"
                          >
                            Entregar
                          </Button>
                        )}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Foro Tab */}
      {activeTab === 'foro' && (
        <div className="space-y-6">
          <div className="flex justify-between items-center">
            <h2 className="text-2xl font-bold text-foreground">Foro de Discusión</h2>
            <div className="flex gap-2">
              {forumExamMode && (
                <Badge variant="destructive">Modo Examen Activo</Badge>
              )}
              <Button
                onClick={handleCreateDiscussion}
                disabled={forumExamMode}
                className="bg-primary hover:bg-primary/90"
              >
                Nueva Discusión
              </Button>
            </div>
          </div>

          {filteredDiscussions.length === 0 ? (
            <Card>
              <CardContent className="p-8 text-center">
                <p className="text-muted-foreground">No hay discusiones todavía</p>
              </CardContent>
            </Card>
          ) : (
            <div className="grid gap-4">
              {filteredDiscussions.map((discussion) => (
                <Card key={discussion.id} className="border-border/50">
                  <CardContent className="p-6">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="text-lg font-semibold text-foreground mb-2">{discussion.topic}</h3>
                        <div className="flex gap-3 mb-2">
                          <p className="text-sm text-muted-foreground">Por: {discussion.author}</p>
                          <p className="text-sm text-muted-foreground">•</p>
                          <p className="text-sm text-muted-foreground">{discussion.replies} respuestas</p>
                        </div>
                        <p className="text-xs text-muted-foreground">
                          {new Date(discussion.createdAt).toLocaleString('es-ES')}
                        </p>
                      </div>
                      <div className="flex gap-2">
                        <Button
                          variant="outline"
                          disabled={forumExamMode}
                        >
                          Ver
                        </Button>
                        <Button
                          onClick={() => handleRequestHelp(discussion.id)}
                          disabled={!evaluationInProgress}
                          className="bg-orange-600 hover:bg-orange-700"
                        >
                          Pedir Ayuda
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  )
}
