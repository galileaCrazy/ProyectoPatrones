'use client'

import { useState, useEffect } from 'react'
import Navigation from '@/components/layout/navigation'
import DashboardContent from '@/components/dashboard/dashboard-content'
import CoursesListView from '@/components/courses/courses-list'
import CourseBuilderView from '@/components/courses/course-builder'
import CourseDetailMediator from '@/components/courses/course-detail-mediator'
import StudentsManagementView from '@/components/students/students-management'
import EvaluationsView from '@/components/evaluations/evaluations-list'
import EvaluationGradeView from '@/components/evaluations/evaluation-grade'
import { EvaluationManager } from '@/components/evaluations'
import ReportsView from '@/components/reports/reports-generator'
import CalendarView from '@/components/calendar/calendar'
import ForumsView from '@/components/forums/forums'
import { StudentInscripcionView } from '@/components/inscripciones'
import { NotificationsPanel } from '@/components/notifications'
import { SystemSettings } from '@/components/settings'
import BecasAdminView from '@/components/admin/becas-admin-view'
import IntegrationsCourseSelector from '@/components/integrations/integrations-courses-selector'
import MyProgressView from '@/components/progress/my-progress-view'
import { useDeviceRenderer } from '@/hooks/useDeviceRenderer'

interface DashboardProps {
  role: 'student' | 'professor' | 'admin'
  onLogout: () => void
}

export default function Dashboard({ role, onLogout }: DashboardProps) {
  const [currentView, setCurrentView] = useState('dashboard')
  const [selectedCourseId, setSelectedCourseId] = useState<string | null>(null)
  const [usuario, setUsuario] = useState<any>(null)

  // PATRÓN BRIDGE: Detectar dispositivo y obtener configuración adaptativa
  const { isMobile, isTablet, deviceType, getResponsiveClasses } = useDeviceRenderer()
  const responsiveClasses = getResponsiveClasses()

  // Cargar usuario del localStorage
  useEffect(() => {
    if (typeof window !== 'undefined') {
      const usuarioStr = localStorage.getItem('usuario')
      if (usuarioStr) {
        setUsuario(JSON.parse(usuarioStr))
      }
    }
  }, [])

  const renderContent = () => {
    switch (currentView) {
      case 'dashboard':
        return <DashboardContent role={role} />
      case 'courses':
        return <CoursesListView
          role={role}
          onSelectCourse={(id) => {
            setSelectedCourseId(id)
            setCurrentView('course-detail')
          }}
          onCreateCourse={() => setCurrentView('create-course')}
        />
      case 'course-detail':
        return <CourseDetailMediator courseId={selectedCourseId} role={role} onBack={() => setCurrentView('courses')} />
      case 'create-course':
        return <CourseBuilderView
          onClose={() => setCurrentView('courses')}
          userRole={role === 'professor' ? 'professor' : 'admin'}
          userId="1"
          userName="Usuario Actual"
        />
      case 'students':
        return <StudentsManagementView />
      case 'evaluations':
        return <EvaluationManager role={role} />
      case 'grade-evaluation':
        return <EvaluationGradeView />
      case 'reports':
        return <ReportsView />
      case 'calendar':
        return <CalendarView />
      case 'forums':
        return <ForumsView />
      case 'inscripciones':
        return <StudentInscripcionView />
      case 'mi-progreso':
        return usuario ? (
          <MyProgressView estudianteId={usuario.id} />
        ) : (
          <div className="p-8">
            <p>Cargando información del usuario...</p>
          </div>
        )
      case 'becas':
        return <BecasAdminView />
      case 'notifications':
        return <NotificationsPanel userRole={role} userId={usuario?.id} />
      case 'settings':
        return <SystemSettings userRole={role as 'admin'} />
      case 'integrations':
        return usuario ? (
          <IntegrationsCourseSelector profesorId={usuario.id} />
        ) : (
          <div className="p-8">
            <p>Cargando información del usuario...</p>
          </div>
        )
      default:
        return <DashboardContent role={role} />
    }
  }

  return (
    <div className={`flex ${isMobile ? 'flex-col' : 'flex-row'} h-screen bg-background`}>
      {/* Navegación adaptativa: Sidebar en desktop, Bottom bar en móvil */}
      {!isMobile && (
        <Navigation
          role={role}
          currentView={currentView}
          onNavigate={setCurrentView}
          onLogout={onLogout}
          userId={usuario?.id}
        />
      )}

      {/* Contenido principal con padding adaptativo */}
      <main className={`flex-1 overflow-auto ${isMobile ? 'pb-20' : ''}`}>
        {renderContent()}
      </main>

      {/* Navegación inferior en móviles */}
      {isMobile && (
        <nav className="fixed bottom-0 left-0 right-0 bg-white border-t shadow-lg z-50">
          <div className="flex justify-around items-center py-3 px-2">
            <button
              onClick={() => setCurrentView('dashboard')}
              className={`flex flex-col items-center justify-center px-2 py-1 min-w-[60px] rounded-lg transition-colors ${
                currentView === 'dashboard'
                  ? 'text-blue-600 bg-blue-50'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <svg className="w-6 h-6 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
              <span className="text-[10px] font-medium">Inicio</span>
            </button>

            <button
              onClick={() => setCurrentView('courses')}
              className={`flex flex-col items-center justify-center px-2 py-1 min-w-[60px] rounded-lg transition-colors ${
                currentView === 'courses'
                  ? 'text-blue-600 bg-blue-50'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <svg className="w-6 h-6 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
              </svg>
              <span className="text-[10px] font-medium">Cursos</span>
            </button>

            {role === 'student' && (
              <button
                onClick={() => setCurrentView('mi-progreso')}
                className={`flex flex-col items-center justify-center px-2 py-1 min-w-[60px] rounded-lg transition-colors ${
                  currentView === 'mi-progreso'
                    ? 'text-blue-600 bg-blue-50'
                    : 'text-gray-600 hover:bg-gray-50'
                }`}
              >
                <svg className="w-6 h-6 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
                </svg>
                <span className="text-[10px] font-medium">Progreso</span>
              </button>
            )}

            <button
              onClick={() => setCurrentView('notifications')}
              className={`flex flex-col items-center justify-center px-2 py-1 min-w-[60px] rounded-lg transition-colors relative ${
                currentView === 'notifications'
                  ? 'text-blue-600 bg-blue-50'
                  : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <svg className="w-6 h-6 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
              </svg>
              <span className="text-[10px] font-medium">Notif.</span>
            </button>

            <button
              onClick={onLogout}
              className="flex flex-col items-center justify-center px-2 py-1 min-w-[60px] rounded-lg transition-colors text-red-500 hover:bg-red-50"
            >
              <svg className="w-6 h-6 mb-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
              <span className="text-[10px] font-medium">Salir</span>
            </button>
          </div>
        </nav>
      )}
    </div>
  )
}
