'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'

interface NavigationProps {
  role: 'student' | 'professor' | 'admin'
  currentView: string
  onNavigate: (view: string) => void
  onLogout: () => void
}

const menuItems = {
  student: [
    { id: 'dashboard', label: 'Dashboard', icon: '' },
    { id: 'courses', label: 'Mis Cursos', icon: '' },
    { id: 'inscripciones', label: 'Inscripciones', icon: '' },
    { id: 'evaluations', label: 'Mis Evaluaciones', icon: '' },
    { id: 'notifications', label: 'Notificaciones', icon: '' },
    { id: 'calendar', label: 'Calendario', icon: '' },
    { id: 'forums', label: 'Foros', icon: '' },
  ],
  professor: [
    { id: 'dashboard', label: 'Dashboard', icon: '' },
    { id: 'courses', label: 'Mis Cursos', icon: '' },
    { id: 'create-course', label: 'Crear Curso', icon: '' },
    { id: 'students', label: 'Estudiantes', icon: '' },
    { id: 'evaluations', label: 'Evaluaciones', icon: '' },
    { id: 'notifications', label: 'Notificaciones', icon: '' },
    { id: 'reports', label: 'Reportes', icon: '' },
    { id: 'calendar', label: 'Calendario', icon: '' },
    { id: 'forums', label: 'Foros', icon: '' },
  ],
  admin: [
    { id: 'dashboard', label: 'Dashboard', icon: '' },
    { id: 'courses', label: 'Todos los Cursos', icon: '' },
    { id: 'students', label: 'Gestión Estudiantes', icon: '' },
    { id: 'evaluations', label: 'Evaluaciones', icon: '' },
    { id: 'notifications', label: 'Notificaciones', icon: '' },
    { id: 'settings', label: 'Configuración', icon: '' },
    { id: 'reports', label: 'Reportes', icon: '' },
    { id: 'calendar', label: 'Calendario', icon: '' },
  ],
}

export default function Navigation({ role, currentView, onNavigate, onLogout }: NavigationProps) {
  const [isCollapsed, setIsCollapsed] = useState(false)
  const items = menuItems[role]

  return (
    <nav className={`${isCollapsed ? 'w-20' : 'w-64'} bg-sidebar border-r border-sidebar-border transition-all duration-300 flex flex-col h-screen`}>
      {/* Header */}
      <div className="p-6 border-b border-sidebar-border flex items-center justify-between">
        {!isCollapsed && (
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-sidebar-primary rounded-lg flex items-center justify-center">
              <span className="text-sidebar-primary-foreground font-bold">📚</span>
            </div>
            <span className="font-bold text-sidebar-foreground">EduLearn</span>
          </div>
        )}
        <button
          onClick={() => setIsCollapsed(!isCollapsed)}
          className="text-sidebar-foreground hover:bg-sidebar-accent rounded p-1"
        >
          {isCollapsed ? '→' : '←'}
        </button>
      </div>

      {/* Menu Items */}
      <div className="flex-1 overflow-y-auto py-4">
        {items.map((item) => (
          <button
            key={item.id}
            onClick={() => onNavigate(item.id)}
            className={`w-full flex items-center gap-4 px-6 py-3 text-left transition-colors ${
              currentView === item.id
                ? 'bg-sidebar-primary/10 text-sidebar-primary border-r-4 border-sidebar-primary'
                : 'text-sidebar-foreground hover:bg-sidebar-accent'
            }`}
          >
            <span className="text-xl">{item.icon}</span>
            {!isCollapsed && <span className="font-medium">{item.label}</span>}
          </button>
        ))}
      </div>

      {/* Logout */}
      <div className="p-4 border-t border-sidebar-border">
        <Button
          onClick={onLogout}
          className="w-full bg-destructive/10 text-destructive hover:bg-destructive/20"
        >
          {isCollapsed ? '🚪' : 'Cerrar Sesión'}
        </Button>
      </div>
    </nav>
  )
}
