'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import LoginPage from '@/components/auth/login-page'
import Dashboard from '@/components/dashboard/dashboard'

export default function Home() {
  const router = useRouter()
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [userRole, setUserRole] = useState<'student' | 'professor' | 'admin'>('student')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Check if user is already authenticated
    const auth = localStorage.getItem('isAuthenticated')
    const role = localStorage.getItem('userRole')
    
    if (auth === 'true') {
      setIsAuthenticated(true)
      setUserRole((role as any) || 'student')
    }
    setLoading(false)
  }, [])

  const handleLogin = (role: 'student' | 'professor' | 'admin') => {
    localStorage.setItem('isAuthenticated', 'true')
    localStorage.setItem('userRole', role)
    setIsAuthenticated(true)
    setUserRole(role)
  }

  const handleLogout = () => {
    localStorage.removeItem('isAuthenticated')
    localStorage.removeItem('userRole')
    setIsAuthenticated(false)
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-background">
        <div className="text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
          <p className="mt-4 text-foreground">Cargando...</p>
        </div>
      </div>
    )
  }

  return isAuthenticated ? (
    <Dashboard role={userRole} onLogout={handleLogout} />
  ) : (
    <LoginPage onLogin={handleLogin} />
  )
}
