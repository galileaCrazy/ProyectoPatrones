'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { BookOpen, ArrowRight, Users } from 'lucide-react'
import IntegrationsManager from './integrations-manager'

interface Curso {
  id: number
  nombre: string
  descripcion: string
  profesor_id: number
  estudiantes_inscritos?: number
}

interface IntegrationsCoursesSelectorProps {
  profesorId: number
}

export default function IntegrationsCourseSelector({ profesorId }: IntegrationsCoursesSelectorProps) {
  const [cursos, setCursos] = useState<Curso[]>([])
  const [loading, setLoading] = useState(true)
  const [cursoSeleccionado, setCursoSeleccionado] = useState<number | null>(null)

  useEffect(() => {
    cargarCursos()
  }, [profesorId])

  const cargarCursos = async () => {
    try {
      setLoading(true)
      const response = await fetch(`http://localhost:8080/api/cursos/por-usuario/${profesorId}?rol=profesor`)
      if (response.ok) {
        const data = await response.json()
        setCursos(data)
      }
    } catch (error) {
      console.error('Error al cargar cursos:', error)
    } finally {
      setLoading(false)
    }
  }

  // Si ya hay un curso seleccionado, mostrar el gestor de integraciones
  if (cursoSeleccionado) {
    return (
      <div>
        <Button
          onClick={() => setCursoSeleccionado(null)}
          variant="outline"
          className="mb-4"
        >
          ← Volver a Mis Cursos
        </Button>
        <IntegrationsManager cursoId={cursoSeleccionado} profesorId={profesorId} />
      </div>
    )
  }

  // Mostrar selector de cursos
  if (loading) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Cargando cursos...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="p-6 space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-foreground">Integraciones Externas</h2>
        <p className="text-muted-foreground mt-1">
          Selecciona un curso para gestionar sus integraciones de videoconferencia y repositorios
        </p>
      </div>

      {cursos.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center p-12">
            <BookOpen className="w-16 h-16 text-muted-foreground mb-4" />
            <h3 className="text-xl font-semibold text-foreground mb-2">
              No tienes cursos asignados
            </h3>
            <p className="text-muted-foreground text-center">
              Necesitas tener al menos un curso para gestionar integraciones externas
            </p>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {cursos.map((curso) => (
            <Card
              key={curso.id}
              className="hover:shadow-lg transition-all cursor-pointer border-2 hover:border-primary"
              onClick={() => setCursoSeleccionado(curso.id)}
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg flex items-center gap-2">
                      <BookOpen className="w-5 h-5 text-primary" />
                      {curso.nombre}
                    </CardTitle>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {curso.descripcion && (
                  <p className="text-sm text-muted-foreground line-clamp-2">
                    {curso.descripcion}
                  </p>
                )}

                {curso.estudiantes_inscritos !== undefined && (
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Users className="w-4 h-4" />
                    <span>{curso.estudiantes_inscritos} estudiantes</span>
                  </div>
                )}

                <Button
                  className="w-full bg-primary hover:bg-primary/90 text-primary-foreground"
                  onClick={(e) => {
                    e.stopPropagation()
                    setCursoSeleccionado(curso.id)
                  }}
                >
                  Gestionar Integraciones
                  <ArrowRight className="w-4 h-4 ml-2" />
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
