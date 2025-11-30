'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

interface EvaluationsViewProps {
  role: 'student' | 'professor' | 'admin'
  onGrade: () => void
}

export default function EvaluationsView({ role, onGrade }: EvaluationsViewProps) {
  const [activeTab, setActiveTab] = useState('pending')

  const evaluations = [
    { id: '1', name: 'Examen Parcial - POO', course: 'Programación OO', dueDate: '25/11/2025', submitted: 12, total: 45 },
    { id: '2', name: 'Quiz Patrones de Diseño', course: 'Patrones', dueDate: '20/11/2025', submitted: 5, total: 38 },
    { id: '3', name: 'Tarea - Base de Datos', course: 'BD Avanzadas', dueDate: '30/11/2025', submitted: 20, total: 52 },
  ]

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-start mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">Evaluaciones</h1>
          <p className="text-muted-foreground">
            {role === 'student' ? 'Tus evaluaciones' : 'Gestión de evaluaciones'}
          </p>
        </div>
        {role !== 'student' && (
          <Button className="bg-primary hover:bg-primary/90 text-primary-foreground">
            + Nueva Evaluación
          </Button>
        )}
      </div>

      {/* Tabs */}
      <div className="flex gap-4 mb-6 border-b border-border">
        {['pending', 'graded', 'all'].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-3 font-medium transition-colors border-b-2 ${
              activeTab === tab
                ? 'border-primary text-primary'
                : 'border-transparent text-muted-foreground hover:text-foreground'
            }`}
          >
            {tab === 'pending' ? 'Pendientes' : tab === 'graded' ? 'Calificadas' : 'Todas'}
          </button>
        ))}
      </div>

      {/* Evaluations */}
      <div className="space-y-4">
        {evaluations.map((eval_) => (
          <Card key={eval_.id} className="border-border/50">
            <CardHeader>
              <div className="flex justify-between items-start">
                <div>
                  <CardTitle>{eval_.name}</CardTitle>
                  <CardDescription>{eval_.course}</CardDescription>
                </div>
                <span className="text-sm bg-primary/10 text-primary px-3 py-1 rounded">
                  Vence: {eval_.dueDate}
                </span>
              </div>
            </CardHeader>
            <CardContent>
              <div className="flex justify-between items-center">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {eval_.submitted} de {eval_.total} completados
                  </p>
                  <div className="w-64 h-2 rounded-full bg-muted overflow-hidden">
                    <div
                      className="h-full bg-primary"
                      style={{ width: `${(eval_.submitted / eval_.total) * 100}%` }}
                    ></div>
                  </div>
                </div>
                {role !== 'student' && (
                  <Button onClick={onGrade} className="bg-primary hover:bg-primary/90 text-primary-foreground">
                    Calificar
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}
