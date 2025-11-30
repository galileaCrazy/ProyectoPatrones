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
    // Verificar si el usuario ya está autenticado
    const usuario = localStorage.getItem('usuario')

    if (usuario) {
      const userData = JSON.parse(usuario)
      setIsAuthenticated(true)

      // Mapear el tipo de usuario al formato del Dashboard
      const roleMap: Record<string, 'student' | 'professor' | 'admin'> = {
        'estudiante': 'student',
        'profesor': 'professor',
        'administrador': 'admin'
      }
      setUserRole(roleMap[userData.tipoUsuario] || 'student')
    }
    setLoading(false)
  }, [])

  const handleLogout = () => {
    localStorage.removeItem('usuario')
    localStorage.removeItem('permisos')
    localStorage.removeItem('menu')
    setIsAuthenticated(false)
    router.push('/')
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
    <LoginPage />
  )
}
