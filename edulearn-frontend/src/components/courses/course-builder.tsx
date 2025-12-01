'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api"

interface CourseBuilderViewProps {
  onClose: () => void
  onCourseCreated?: () => void
}

export default function CourseBuilderView({ onClose, onCourseCreated }: CourseBuilderViewProps) {
  const [step, setStep] = useState(1)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [courseData, setCourseData] = useState({
    name: '',
    description: '',
    type: 'presencial', // presencial, virtual, hibrido
    period: '2025-1',
    professorId: 1,
    startDate: '',
    endDate: '',
  })

  const handleNext = () => {
    if (step < 5) setStep(step + 1)
  }

  const handlePrev = () => {
    if (step > 1) setStep(step - 1)
  }

  // PATRÓN BUILDER: Construcción del curso usando el backend
  const handleCreate = async () => {
    setLoading(true)
    setError(null)

    try {
      // Determinar el tipo de curso y endpoint del Director
      let endpoint = ""
      let body: any = { nombre: courseData.name }

      // El Director selecciona la configuración según el tipo
      if (courseData.type === 'presencial') {
        endpoint = "/cursos/builder/regular"
        body.periodoAcademico = courseData.period
      } else if (courseData.type === 'virtual') {
        endpoint = "/cursos/builder/intensivo"
        body.profesorId = courseData.professorId
      } else {
        endpoint = "/cursos/builder/certificacion"
        body.profesorId = courseData.professorId
        body.periodoAcademico = courseData.period
      }

      // Llamar al patrón Builder en el backend
      const response = await fetch(`${API_URL}${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      })

      if (!response.ok) {
        const errorText = await response.text()
        console.error('Error response:', errorText)
        throw new Error(`Error al crear el curso: ${response.status} - ${errorText || response.statusText}`)
      }

      const createdCourse = await response.json()
      console.log('Curso creado:', createdCourse)

      onCourseCreated?.()
      onClose()
    } catch (err: any) {
      setError(err.message)
      console.error('Error:', err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-2">Crear Nuevo Curso</h1>
      <p className="text-muted-foreground mb-8">Paso {step} de 5</p>

      {/* Progress Bar */}
      <div className="w-full h-2 rounded-full bg-muted mb-8 overflow-hidden">
        <div
          className="h-full bg-primary transition-all"
          style={{ width: `${(step / 5) * 100}%` }}
        ></div>
      </div>

      {/* Steps */}
      <Card className="mb-8 border-border/50">
        <CardHeader>
          <CardTitle>
            {step === 1 && 'Información Básica'}
            {step === 2 && 'Profesor y Horario'}
            {step === 3 && 'Agregar Módulos'}
            {step === 4 && 'Configurar Evaluaciones'}
            {step === 5 && 'Revisión y Confirmación'}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          {step === 1 && (
            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">Nombre del Curso</label>
                <input
                  type="text"
                  placeholder="Ej: Programación Orientada a Objetos"
                  value={courseData.name}
                  onChange={(e) => setCourseData({ ...courseData, name: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">Descripción</label>
                <textarea
                  placeholder="Describe el contenido del curso..."
                  value={courseData.description}
                  onChange={(e) => setCourseData({ ...courseData, description: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary h-24 resize-none"
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-sm font-medium text-foreground block mb-2">Tipo de Curso</label>
                  <select
                    value={courseData.type}
                    onChange={(e) => setCourseData({ ...courseData, type: e.target.value })}
                    className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="presencial">Presencial</option>
                    <option value="virtual">Virtual</option>
                    <option value="hibrido">Híbrido</option>
                  </select>
                </div>
                <div>
                  <label className="text-sm font-medium text-foreground block mb-2">Período</label>
                  <select
                    value={courseData.period}
                    onChange={(e) => setCourseData({ ...courseData, period: e.target.value })}
                    className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option>2025-1</option>
                    <option>2025-2</option>
                  </select>
                </div>
              </div>
            </div>
          )}

          {step === 2 && (
            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">Profesor Asignado</label>
                <select className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary">
                  <option>Juan Pérez</option>
                  <option>María González</option>
                  <option>Carlos Ramírez</option>
                </select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-sm font-medium text-foreground block mb-2">Fecha de Inicio</label>
                  <input type="date" className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary" />
                </div>
                <div>
                  <label className="text-sm font-medium text-foreground block mb-2">Fecha de Fin</label>
                  <input type="date" className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary" />
                </div>
              </div>
            </div>
          )}

          {step === 3 && (
            <div className="space-y-4">
              <Button className="w-full bg-primary hover:bg-primary/90 text-primary-foreground mb-4">
                + Agregar Módulo
              </Button>
              {[1, 2].map((i) => (
                <div key={i} className="p-4 rounded-lg bg-muted/50 border border-border">
                  <p className="font-medium text-foreground">Módulo {i}: Introducción</p>
                  <p className="text-sm text-muted-foreground mt-1">3 lecciones</p>
                </div>
              ))}
            </div>
          )}

          {step === 4 && (
            <div className="space-y-4">
              <Button className="w-full bg-primary hover:bg-primary/90 text-primary-foreground mb-4">
                + Agregar Evaluación
              </Button>
              {[1, 2].map((i) => (
                <div key={i} className="p-4 rounded-lg bg-muted/50 border border-border flex justify-between items-center">
                  <div>
                    <p className="font-medium text-foreground">Quiz {i}</p>
                    <p className="text-sm text-muted-foreground">Peso: 20%</p>
                  </div>
                  <button className="text-destructive hover:text-destructive/90">×</button>
                </div>
              ))}
            </div>
          )}

          {step === 5 && (
            <div className="space-y-4">
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground">Nombre del Curso</p>
                <p className="font-semibold text-foreground text-lg">{courseData.name}</p>
              </div>
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground">Tipo</p>
                <p className="font-semibold text-foreground">{courseData.type}</p>
              </div>
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground">Descripción</p>
                <p className="font-semibold text-foreground">{courseData.description}</p>
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Error Message */}
      {error && (
        <div className="mb-4 p-4 rounded-lg bg-destructive/10 border border-destructive text-destructive">
          {error}
        </div>
      )}

      {/* Buttons */}
      <div className="flex justify-between gap-4">
        <Button
          onClick={step === 1 ? onClose : handlePrev}
          className="bg-muted hover:bg-muted/80 text-foreground"
          disabled={loading}
        >
          {step === 1 ? 'Cancelar' : 'Anterior'}
        </Button>
        <Button
          onClick={step === 5 ? handleCreate : handleNext}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
          disabled={loading}
        >
          {loading ? 'Creando...' : step === 5 ? 'Crear Curso' : 'Siguiente'}
        </Button>
      </div>
    </div>
  )
}
