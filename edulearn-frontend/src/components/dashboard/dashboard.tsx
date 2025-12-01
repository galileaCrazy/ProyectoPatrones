'use client'

import { useState } from 'react'
import Navigation from '@/components/layout/navigation'
import DashboardContent from '@/components/dashboard/dashboard-content'
import CoursesListView from '@/components/courses/courses-list'
import CourseDetailView from '@/components/courses/course-detail'
import CourseBuilderView from '@/components/courses/course-builder'
import StudentsManagementView from '@/components/students/students-management'
import EvaluationsView from '@/components/evaluations/evaluations-list'
import EvaluationGradeView from '@/components/evaluations/evaluation-grade'
import ReportsView from '@/components/reports/reports-generator'
import CalendarView from '@/components/calendar/calendar'
import ForumsView from '@/components/forums/forums'
import { StudentInscripcionView } from '@/components/inscripciones'

interface DashboardProps {
  role: 'student' | 'professor' | 'admin'
  onLogout: () => void
}

export default function Dashboard({ role, onLogout }: DashboardProps) {
  const [currentView, setCurrentView] = useState('dashboard')
  const [selectedCourseId, setSelectedCourseId] = useState<string | null>(null)

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
        return <CourseDetailView courseId={selectedCourseId} role={role} onBack={() => setCurrentView('courses')} />
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
        return <EvaluationsView role={role} onGrade={() => setCurrentView('grade-evaluation')} />
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
      default:
        return <DashboardContent role={role} />
    }
  }

  return (
    <div className="flex h-screen bg-background">
      <Navigation
        role={role}
        currentView={currentView}
        onNavigate={setCurrentView}
        onLogout={onLogout}
      />
      <main className="flex-1 overflow-auto">
        {renderContent()}
      </main>
    </div>
  )
}
