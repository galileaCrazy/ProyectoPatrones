'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

export default function EvaluationGradeView() {
  const [currentStudent, setCurrentStudent] = useState(0)
  const [grades, setGrades] = useState({
    q1: 8,
    q2: 9,
  })

  const students = [
    { id: 1, name: 'Ana Torres', email: 'ana@uni.edu' },
    { id: 2, name: 'Juan Méndez', email: 'juan@uni.edu' },
    { id: 3, name: 'María López', email: 'maria@uni.edu' },
  ]

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-8">Calificar Evaluación</h1>

      <div className="grid grid-cols-3 gap-4 mb-8">
        {students.map((student, idx) => (
          <button
            key={student.id}
            onClick={() => setCurrentStudent(idx)}
            className={`p-4 rounded-lg border-2 transition-colors text-left ${
              currentStudent === idx
                ? 'border-primary bg-primary/10'
                : 'border-border hover:border-border/50'
            }`}
          >
            <p className="font-semibold text-foreground">{student.name}</p>
            <p className="text-sm text-muted-foreground">{student.email}</p>
            <p className="text-xs text-muted-foreground mt-2">
              {idx === 0 ? '17/20' : idx === 1 ? '18/20' : 'Sin calificar'}
            </p>
          </button>
        ))}
      </div>

      <Card className="mb-8 border-border/50">
        <CardHeader>
          <CardTitle>Respuestas de {students[currentStudent].name}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          {[1, 2].map((q) => (
            <div key={q} className="p-4 rounded-lg bg-muted/50 border border-border">
              <h3 className="font-semibold text-foreground mb-2">Pregunta {q}</h3>
              <p className="text-sm text-muted-foreground mb-4">
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
              </p>
              <label className="block text-sm font-medium text-foreground mb-2">Calificación: {grades[`q${q}` as keyof typeof grades]}/10</label>
              <input
                type="range"
                min="0"
                max="10"
                value={grades[`q${q}` as keyof typeof grades]}
                onChange={(e) => setGrades({ ...grades, [`q${q}`]: parseInt(e.target.value) })}
                className="w-full"
              />
              <textarea
                placeholder="Retroalimentación..."
                className="w-full mt-4 px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary h-20 resize-none"
              />
            </div>
          ))}
        </CardContent>
      </Card>

      <div className="flex justify-between gap-4">
        <Button className="bg-muted hover:bg-muted/80 text-foreground">Cancelar</Button>
        <Button className="bg-primary hover:bg-primary/90 text-primary-foreground">Guardar y Siguiente</Button>
      </div>
    </div>
  )
}
