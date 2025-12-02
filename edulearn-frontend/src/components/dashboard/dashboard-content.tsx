'use client'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import Link from 'next/link'

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
        {/* ... (Stats Cards sin cambios) ... */}
      </div>

      {/* Admin Tools Section */}
      {role === 'admin' && (
        <div className="mb-8">
          <h2 className="text-2xl font-bold text-foreground mb-4">Herramientas de Administrador</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <Link href="/dashboard/admin/configuracion" passHref>
              <Card className="hover:shadow-lg hover:border-primary transition-all cursor-pointer h-full">
                <CardHeader>
                  <CardTitle>Configuración del Sistema</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardDescription>
                    Gestionar la configuración global de la aplicación (Patrón Singleton).
                  </CardDescription>
                </CardContent>
              </Card>
            </Link>
          </div>
        </div>
      )}


      {/* Content Sections */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* ... (Otras secciones sin cambios) ... */}
      </div>
    </div>
  )
}
