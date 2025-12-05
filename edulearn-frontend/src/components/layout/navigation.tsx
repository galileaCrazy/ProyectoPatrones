'use client'

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { API_URL } from '@/lib/api'
import {
  LayoutDashboard,
  BookOpen,
  ClipboardList,
  FileCheck,
  Bell,
  Calendar,
  MessageSquare,
  PlusCircle,
  Users,
  BarChart3,
  Settings,
  GraduationCap,
  LogOut,
  Library,
  Link2,
  TrendingUp
} from 'lucide-react'

interface NavigationProps {
  role: 'student' | 'professor' | 'admin'
  currentView: string
  onNavigate: (view: string) => void
  onLogout: () => void
  userId?: number
}

const menuItems = {
  student: [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'courses', label: 'Mis Cursos', icon: BookOpen },
    { id: 'mi-progreso', label: 'Mi Progreso', icon: TrendingUp },
    { id: 'inscripciones', label: 'Inscripciones', icon: ClipboardList },
    { id: 'evaluations', label: 'Mis Evaluaciones', icon: FileCheck },
    { id: 'notifications', label: 'Notificaciones', icon: Bell },
    { id: 'calendar', label: 'Calendario', icon: Calendar },
    { id: 'forums', label: 'Foros', icon: MessageSquare },
  ],
  professor: [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'courses', label: 'Mis Cursos', icon: BookOpen },
    { id: 'create-course', label: 'Crear Curso', icon: PlusCircle },
    { id: 'students', label: 'Estudiantes', icon: Users },
    { id: 'evaluations', label: 'Evaluaciones', icon: FileCheck },
    { id: 'integrations', label: 'Integraciones', icon: Link2 },
    { id: 'notifications', label: 'Notificaciones', icon: Bell },
    { id: 'reports', label: 'Reportes', icon: BarChart3 },
    { id: 'calendar', label: 'Calendario', icon: Calendar },
    { id: 'forums', label: 'Foros', icon: MessageSquare },
  ],
  admin: [
    { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'courses', label: 'Todos los Cursos', icon: Library },
    { id: 'students', label: 'Gestión Estudiantes', icon: Users },
    { id: 'becas', label: 'Validar Becas', icon: GraduationCap },
    { id: 'evaluations', label: 'Evaluaciones', icon: FileCheck },
    { id: 'notifications', label: 'Notificaciones', icon: Bell },
    { id: 'settings', label: 'Configuración', icon: Settings },
    { id: 'reports', label: 'Reportes', icon: BarChart3 },
    { id: 'calendar', label: 'Calendario', icon: Calendar },
  ],
}

export default function Navigation({ role, currentView, onNavigate, onLogout, userId }: NavigationProps) {
  const [isCollapsed, setIsCollapsed] = useState(false)
  const [unreadCount, setUnreadCount] = useState(0)
  const items = menuItems[role]

  // Fetch unread notifications count
  useEffect(() => {
    if (userId) {
      fetchUnreadCount()
      // Actualizar cada 30 segundos
      const interval = setInterval(fetchUnreadCount, 30000)
      return () => clearInterval(interval)
    }
  }, [userId])

  const fetchUnreadCount = async () => {
    if (!userId) return

    try {
      const response = await fetch(`${API_URL}/notificaciones/usuario/${userId}/count`)
      if (response.ok) {
        const data = await response.json()
        setUnreadCount(data.unreadCount || 0)
      }
    } catch (error) {
      console.error('Error al obtener contador de notificaciones:', error)
    }
  }

  return (
    <nav className={`${isCollapsed ? 'w-20' : 'w-64'} bg-sidebar border-r border-sidebar-border transition-all duration-300 flex flex-col h-screen`}>
      {/* Header */}
      <div className="p-6 border-b border-sidebar-border flex items-center justify-between">
        {!isCollapsed && (
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-sidebar-primary rounded-lg flex items-center justify-center">
              <GraduationCap className="w-5 h-5 text-sidebar-primary-foreground" />
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
        {items.map((item) => {
          const IconComponent = item.icon
          const isNotificationItem = item.id === 'notifications'
          const showBadge = isNotificationItem && unreadCount > 0

          return (
            <button
              key={item.id}
              onClick={() => onNavigate(item.id)}
              className={`w-full flex items-center gap-4 px-6 py-3 text-left transition-colors relative ${
                currentView === item.id
                  ? 'bg-sidebar-primary/10 text-sidebar-primary border-r-4 border-sidebar-primary'
                  : 'text-sidebar-foreground hover:bg-sidebar-accent'
              }`}
            >
              <div className="relative flex-shrink-0">
                <IconComponent className="w-5 h-5" />
                {showBadge && (
                  <span className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 rounded-full flex items-center justify-center text-[10px] text-white font-bold animate-pulse">
                    {unreadCount > 9 ? '9+' : unreadCount}
                  </span>
                )}
              </div>
              {!isCollapsed && (
                <div className="flex items-center justify-between flex-1">
                  <span className="font-medium">{item.label}</span>
                  {showBadge && (
                    <span className="bg-red-500 text-white text-xs px-2 py-0.5 rounded-full font-bold">
                      {unreadCount}
                    </span>
                  )}
                </div>
              )}
            </button>
          )
        })}
      </div>

      {/* Logout */}
      <div className="p-4 border-t border-sidebar-border">
        <Button
          onClick={onLogout}
          className="w-full bg-destructive/10 text-destructive hover:bg-destructive/20 flex items-center justify-center gap-2"
        >
          <LogOut className="w-4 h-4" />
          {!isCollapsed && <span>Cerrar Sesión</span>}
        </Button>
      </div>
    </nav>
  )
}
