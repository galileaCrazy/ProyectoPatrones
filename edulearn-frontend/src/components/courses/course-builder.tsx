'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

interface CourseBuilderViewProps {
  onClose: () => void
  userRole: 'professor' | 'admin'
  userId?: string
  userName?: string
}

// Mock de profesores (esto vendría del backend)
const MOCK_PROFESSORS = [
  { id: '1', name: 'Juan Pérez' },
  { id: '2', name: 'María González' },
  { id: '3', name: 'Carlos Ramírez' },
  { id: '4', name: 'Laura Martínez' },
]

export default function CourseBuilderView({ onClose, userRole, userId = '1', userName = 'Juan Pérez' }: CourseBuilderViewProps) {
  const [step, setStep] = useState(1)
  const [courseData, setCourseData] = useState({
    name: '',
    description: '',
    type: 'Virtual',
    period: '2025-1',
    professorId: userRole === 'professor' ? userId : '',
    professorName: userRole === 'professor' ? userName : '',
  })

  // Configuración según el rol usando Chain of Responsibility
  const [formConfig, setFormConfig] = useState({
    mostrarListaProfesores: userRole === 'admin',
    modoAutoAsignacion: userRole === 'professor',
    profesorAsignado: userRole === 'professor' ? userName : null,
  })

  useEffect(() => {
    // Simular llamada al backend para obtener configuración de Chain of Responsibility
    const configurarFormulario = async () => {
      // Aquí se haría la llamada al endpoint que usa el patrón Chain of Responsibility
      // Por ahora usamos lógica local
      if (userRole === 'professor') {
        setFormConfig({
          mostrarListaProfesores: false,
          modoAutoAsignacion: true,
          profesorAsignado: userName,
        })
      } else {
        setFormConfig({
          mostrarListaProfesores: true,
          modoAutoAsignacion: false,
          profesorAsignado: null,
        })
      }
    }

    configurarFormulario()
  }, [userRole, userName])

  const handleNext = () => {
    if (step < 5) setStep(step + 1)
  }

  const handlePrev = () => {
    if (step > 1) setStep(step - 1)
  }

  const handleCreate = () => {
    alert('Curso creado: ' + courseData.name)
    onClose()
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
                    <option>Virtual</option>
                    <option>Presencial</option>
                    <option>Híbrido</option>
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
              {/* Sección de Profesor - Adaptada dinámicamente según Chain of Responsibility */}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Profesor Asignado
                  {formConfig.modoAutoAsignacion && (
                    <span className="ml-2 text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">
                      Auto-asignado
                    </span>
                  )}
                </label>

                {/* PROFESOR: Muestra solo su nombre (no puede cambiarlo) */}
                {formConfig.modoAutoAsignacion && !formConfig.mostrarListaProfesores && (
                  <div className="w-full px-4 py-2 rounded-lg border border-input bg-muted/50 text-foreground flex items-center justify-between">
                    <span className="font-medium">{formConfig.profesorAsignado}</span>
                    <span className="text-xs text-muted-foreground">
                      (Tú serás el profesor de este curso)
                    </span>
                  </div>
                )}

                {/* ADMINISTRADOR: Muestra lista desplegable de profesores */}
                {formConfig.mostrarListaProfesores && !formConfig.modoAutoAsignacion && (
                  <select
                    value={courseData.professorId}
                    onChange={(e) => {
                      const selectedProf = MOCK_PROFESSORS.find(p => p.id === e.target.value)
                      setCourseData({
                        ...courseData,
                        professorId: e.target.value,
                        professorName: selectedProf?.name || ''
                      })
                    }}
                    className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="">Selecciona un profesor...</option>
                    {MOCK_PROFESSORS.map((professor) => (
                      <option key={professor.id} value={professor.id}>
                        {professor.name}
                      </option>
                    ))}
                  </select>
                )}
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

      {/* Buttons */}
      <div className="flex justify-between gap-4">
        <Button
          onClick={step === 1 ? onClose : handlePrev}
          className="bg-muted hover:bg-muted/80 text-foreground"
        >
          {step === 1 ? 'Cancelar' : 'Anterior'}
        </Button>
        <Button
          onClick={step === 5 ? handleCreate : handleNext}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
        >
          {step === 5 ? 'Crear Curso' : 'Siguiente'}
        </Button>
      </div>
    </div>
  )
}
