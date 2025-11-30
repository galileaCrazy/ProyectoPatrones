'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

interface CourseBuilderViewProps {
  onClose: () => void
}

export default function CourseBuilderView({ onClose }: CourseBuilderViewProps) {
  const [step, setStep] = useState(1)
  const [courseData, setCourseData] = useState({
    name: '',
    description: '',
    type: 'Virtual',
    period: '2025-1',
  })

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
