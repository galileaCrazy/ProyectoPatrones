'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

export default function ReportsView() {
  const [reportType, setReportType] = useState('performance')
  const [format, setFormat] = useState('pdf')

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-8">Generador de Reportes</h1>

      <Card className="mb-8 border-border/50">
        <CardHeader>
          <CardTitle>Crear Nuevo Reporte</CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-foreground mb-3">Tipo de Reporte</label>
            <div className="space-y-2">
              {[
                { id: 'performance', name: 'Rendimiento Académico' },
                { id: 'participation', name: 'Participación de Estudiantes' },
                { id: 'statistics', name: 'Estadísticas Institucionales' },
                { id: 'progress', name: 'Progreso por Curso' },
              ].map((type) => (
                <label key={type.id} className="flex items-center gap-3 p-3 rounded-lg border border-input cursor-pointer hover:bg-muted transition-colors">
                  <input
                    type="radio"
                    name="reportType"
                    value={type.id}
                    checked={reportType === type.id}
                    onChange={(e) => setReportType(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span className="font-medium text-foreground">{type.name}</span>
                </label>
              ))}
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Desde</label>
              <input type="date" className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary" />
            </div>
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Hasta</label>
              <input type="date" className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary" />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-3">Formato de Salida</label>
            <div className="flex gap-4">
              {['pdf', 'excel', 'csv'].map((f) => (
                <label key={f} className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="format"
                    value={f}
                    checked={format === f}
                    onChange={(e) => setFormat(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span className="font-medium text-foreground uppercase">{f}</span>
                </label>
              ))}
            </div>
          </div>

          <Button className="w-full bg-primary hover:bg-primary/90 text-primary-foreground">
            Generar Reporte
          </Button>
        </CardContent>
      </Card>

      <Card className="border-border/50">
        <CardHeader>
          <CardTitle>Reportes Recientes</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="flex justify-between items-center p-4 rounded-lg bg-muted/50 border border-border hover:bg-muted transition-colors">
                <div>
                  <p className="font-medium text-foreground">Reporte de Rendimiento #{i}</p>
                  <p className="text-sm text-muted-foreground">Generado hace {i} días</p>
                </div>
                <Button className="bg-primary hover:bg-primary/90 text-primary-foreground text-sm">
                  Descargar
                </Button>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
