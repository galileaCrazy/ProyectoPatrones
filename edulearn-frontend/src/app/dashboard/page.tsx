'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import Dashboard from '@/components/dashboard/dashboard'
import LoginPage from '@/components/auth/login-page'

export default function DashboardPage() {
  const router = useRouter()
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [userRole, setUserRole] = useState<'student' | 'professor' | 'admin'>('student')

  useEffect(() => {
    const usuario = localStorage.getItem('usuario')
    if (usuario) {
      const user = JSON.parse(usuario)
      setIsAuthenticated(true)
      
      // Mapear tipos de usuario
      const roleMap: { [key: string]: 'student' | 'professor' | 'admin' } = {
        'estudiante': 'student',
        'profesor': 'professor',
        'administrador': 'admin'
      }
      setUserRole(roleMap[user.tipoUsuario as string] || 'student')
    } else {
      router.push('/')
    }
  }, [router])

  const handleLogout = () => {
    localStorage.removeItem('usuario')
    localStorage.removeItem('permisos')
    localStorage.removeItem('menu')
    setIsAuthenticated(false)
    router.push('/')
  }

  if (!isAuthenticated) {
    return <div>Cargando...</div>
  }

  return <Dashboard role={userRole} onLogout={handleLogout} />
}