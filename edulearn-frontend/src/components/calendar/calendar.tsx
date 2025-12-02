'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

export default function CalendarView() {
  const [currentDate, setCurrentDate] = useState(new Date(2025, 10, 18))

  const getDaysInMonth = (date: Date) => {
    return new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate()
  }

  const getFirstDayOfMonth = (date: Date) => {
    return new Date(date.getFullYear(), date.getMonth(), 1).getDay()
  }

  const days = []
  const firstDay = getFirstDayOfMonth(currentDate)
  const daysInMonth = getDaysInMonth(currentDate)

  for (let i = 0; i < firstDay; i++) {
    days.push(null)
  }
  for (let i = 1; i <= daysInMonth; i++) {
    days.push(i)
  }

  const monthNames = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre']

  const events = [
    { date: 20, title: 'Clase en vivo: POO', type: 'clase' },
    { date: 25, title: 'Entrega: Examen Parcial', type: 'evaluacion' },
    { date: 28, title: 'Inicio: Módulo 3', type: 'evento' },
  ]

  return (
    <div className="p-8 max-w-7xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-8">Calendario Académico</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <Card className="border-border/50">
            <CardHeader>
              <div className="flex justify-between items-center">
                <CardTitle>
                  {monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}
                </CardTitle>
                <div className="flex gap-2">
                  <button
                    onClick={() => setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1))}
                    className="px-3 py-1 rounded bg-muted hover:bg-muted/80 text-foreground"
                  >
                    ← Anterior
                  </button>
                  <button
                    onClick={() => setCurrentDate(new Date())}
                    className="px-3 py-1 rounded bg-muted hover:bg-muted/80 text-foreground"
                  >
                    Hoy
                  </button>
                  <button
                    onClick={() => setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1))}
                    className="px-3 py-1 rounded bg-muted hover:bg-muted/80 text-foreground"
                  >
                    Siguiente →
                  </button>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-7 gap-2 mb-4">
                {['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'].map((day) => (
                  <div key={day} className="text-center font-semibold text-muted-foreground text-sm py-2">
                    {day}
                  </div>
                ))}
              </div>
              <div className="grid grid-cols-7 gap-2">
                {days.map((day, idx) => {
                  const hasEvent = events.some(e => e.date === day)
                  const isToday = day === 18
                  return (
                    <div
                      key={idx}
                      className={`aspect-square flex items-center justify-center rounded-lg border text-sm font-medium transition-colors ${
                        day === null
                          ? 'bg-transparent border-transparent'
                          : hasEvent
                          ? 'bg-primary/10 border-primary text-primary cursor-pointer hover:bg-primary/20'
                          : isToday
                          ? 'bg-primary text-primary-foreground border-primary cursor-pointer'
                          : 'bg-muted border-border text-foreground hover:bg-muted/80'
                      }`}
                    >
                      {day}
                    </div>
                  )
                })}
              </div>
            </CardContent>
          </Card>
        </div>

        <div>
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle className="text-lg">Próximos Eventos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {events.map((event, idx) => (
                  <div key={idx} className="p-3 rounded-lg bg-muted/50 border border-border">
                    <div className="flex gap-2 mb-1">
                      <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded">
                        {event.type === 'clase' ? 'Clase' : event.type === 'evaluacion' ? 'Evaluación' : 'Evento'}
                      </span>
                    </div>
                    <p className="font-medium text-sm text-foreground">{event.title}</p>
                    <p className="text-xs text-muted-foreground mt-1">{event.date} de noviembre</p>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}
