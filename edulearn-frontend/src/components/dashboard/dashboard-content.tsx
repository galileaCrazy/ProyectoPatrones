'use client'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

interface DashboardContentProps {
  role: 'student' | 'professor' | 'admin'
}

export default function DashboardContent({ role }: DashboardContentProps) {
  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-4xl font-bold text-foreground mb-2">
          {role === 'student' && 'Bienvenido Estudiante'}
          {role === 'professor' && 'Bienvenido Profesor'}
          {role === 'admin' && 'Panel de Administración'}
        </h1>
        <p className="text-muted-foreground">Sistema de Gestión de Aprendizaje EduLearn</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              {role === 'student' ? 'Cursos Activos' : 'Total Cursos'}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">
              {role === 'student' ? 4 : role === 'professor' ? 3 : 24}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {role === 'student' ? '+1 este semestre' : '+2 este período'}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              {role === 'student' ? 'Tareas Pendientes' : 'Estudiantes'}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">
              {role === 'student' ? 5 : role === 'professor' ? 145 : 1247}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {role === 'student' ? '3 vencen hoy' : 'activos'}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              {role === 'student' ? 'Promedio General' : 'Evaluaciones'}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">
              {role === 'student' ? '87%' : role === 'professor' ? 18 : 234}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {role === 'student' ? 'Excelente rendimiento' : 'este período'}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              {role === 'student' ? 'Horas de Estudio' : 'Reportes'}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-primary">
              {role === 'student' ? '32h' : role === 'professor' ? 8 : 15}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {role === 'student' ? 'esta semana' : 'generados'}
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Content Sections */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Courses */}
        <Card>
          <CardHeader>
            <CardTitle>Cursos Recientes</CardTitle>
            <CardDescription>Tus cursos activos</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {[
                { name: 'Programación Orientada a Objetos', progress: 75, instructor: 'Juan Pérez' },
                { name: 'Diseño de Patrones de Software', progress: 60, instructor: 'María González' },
                { name: 'Base de Datos Avanzadas', progress: 45, instructor: 'Carlos Ramírez' },
              ].map((course, idx) => (
                <div key={idx} className="p-3 rounded-lg bg-muted/50 border border-border">
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <h3 className="font-medium text-foreground">{course.name}</h3>
                      <p className="text-xs text-muted-foreground">{course.instructor}</p>
                    </div>
                    <span className="text-sm font-semibold text-primary">{course.progress}%</span>
                  </div>
                  <div className="w-full h-2 rounded-full bg-muted overflow-hidden">
                    <div
                      className="h-full bg-primary"
                      style={{ width: `${course.progress}%` }}
                    ></div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Activities */}
        <Card>
          <CardHeader>
            <CardTitle>Actividades Recientes</CardTitle>
            <CardDescription>Últimas actualizaciones</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {[
                { icon: '📝', text: 'Nueva evaluación en POO', time: 'hace 2 horas' },
                { icon: '✅', text: 'Tarea calificada en Patrones', time: 'ayer' },
                { icon: '💬', text: 'Nueva respuesta en foro', time: 'hace 3 horas' },
              ].map((activity, idx) => (
                <div key={idx} className="flex gap-3 items-start p-3 rounded-lg bg-muted/50 border border-border">
                  <span className="text-xl mt-1">{activity.icon}</span>
                  <div className="flex-1">
                    <p className="text-sm font-medium text-foreground">{activity.text}</p>
                    <p className="text-xs text-muted-foreground">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
