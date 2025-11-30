'use client'

import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import Dashboard from '@/components/dashboard/dashboard'

export default function AdminDashboard() {
  const router = useRouter()
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const usuario = localStorage.getItem('usuario')
    
    if (!usuario) {
      router.push('/login')
      return
    }

    try {
      const user = JSON.parse(usuario)
      
      // Verificar que sea administrador
      if (user.tipoUsuario !== 'administrador') {
        // Redirigir al dashboard correcto
        router.push(`/dashboard/${user.tipoUsuario}`)
        return
      }
      
      setIsAuthenticated(true)
    } catch (error) {
      console.error('Error al verificar usuario:', error)
      router.push('/login')
    } finally {
      setLoading(false)
    }
  }, [router])

  const handleLogout = () => {
    localStorage.removeItem('usuario')
    localStorage.removeItem('isAuthenticated')
    setIsAuthenticated(false)
    router.push('/login')
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Cargando...</p>
        </div>
      </div>
    )
  }

  if (!isAuthenticated) {
    return null
  }

  return <Dashboard role="admin" onLogout={handleLogout} />
}
