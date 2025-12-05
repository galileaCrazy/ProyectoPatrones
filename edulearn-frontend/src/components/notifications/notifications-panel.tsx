'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { API_URL } from '@/lib/api'

interface Notificacion {
  id: number
  tipo: string
  destinatario: string
  asunto: string
  mensaje: string
  estado: string
  fechaEnvio: string
  intentos: number
}

interface NotificationsPanelProps {
  userRole: 'student' | 'professor' | 'admin'
  userId?: number
}

export default function NotificationsPanel({ userRole, userId }: NotificationsPanelProps) {
  const [notifications, setNotifications] = useState<Notificacion[]>([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (userId) {
      fetchNotifications()
    }
  }, [userId])

  const fetchNotifications = async () => {
    if (!userId) {
      console.warn('No se puede cargar notificaciones sin userId')
      return
    }

    setLoading(true)
    try {
      const response = await fetch(`${API_URL}/notificaciones/usuario/${userId}/no-leidas`)
      if (response.ok) {
        const data = await response.json()
        setNotifications(data)
      }
    } catch (error) {
      console.error('Error al cargar notificaciones:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleMarkAsRead = async (id: number) => {
    try {
      const response = await fetch(`${API_URL}/notificaciones/${id}/leer`, {
        method: 'PUT'
      })

      if (response.ok) {
        fetchNotifications()
      }
    } catch (error) {
      console.error('Error al marcar como leída:', error)
    }
  }

  const getNotificationIcon = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case 'INTERNA':
        return '🔔'
      case 'EMAIL':
        return '📧'
      case 'SMS':
        return '📱'
      case 'PUSH':
        return '🔔'
      default:
        return '📢'
    }
  }

  const getStatusBadge = (estado: string) => {
    switch (estado.toUpperCase()) {
      case 'LEIDA':
        return 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200'
      case 'NO_LEIDA':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200'
      case 'ENVIADA':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
      case 'FALLIDA':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
      case 'PENDIENTE':
        return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200'
    }
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-start mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">
            Notificaciones
            <span className="ml-3 text-sm text-primary">Observer Pattern</span>
          </h1>
          <p className="text-muted-foreground">
            Sistema de notificaciones 
          </p>
        </div>
        <Button
          onClick={fetchNotifications}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
        >
          Actualizar
        </Button>
      </div>

      {/* Notifications List */}
      {loading ? (
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Cargando notificaciones...</p>
          </div>
        </div>
      ) : (
        <div className="space-y-4">
          {notifications.length === 0 ? (
            <Card>
              <CardContent className="pt-6 text-center">
                <p className="text-muted-foreground">No hay notificaciones disponibles</p>
              </CardContent>
            </Card>
          ) : (
            notifications.map((notif) => (
              <Card key={notif.id}>
                <CardContent className="pt-6">
                  <div className="flex items-start gap-4">
                    <div className="text-3xl">{getNotificationIcon(notif.tipo)}</div>
                    <div className="flex-1">
                      <div className="flex items-start justify-between mb-2">
                        <div>
                          <h3 className="font-semibold text-foreground">{notif.asunto}</h3>
                          <p className="text-sm text-muted-foreground">
                            Para: {notif.destinatario}
                          </p>
                        </div>
                        <div className="flex items-center gap-2">
                          <span className={`text-xs px-2 py-1 rounded ${getStatusBadge(notif.estado)}`}>
                            {notif.estado}
                          </span>
                          <span className="text-xs bg-muted text-muted-foreground px-2 py-1 rounded">
                            {notif.tipo}
                          </span>
                        </div>
                      </div>
                      <p className="text-sm text-muted-foreground mb-3">{notif.mensaje}</p>
                      <div className="flex items-center justify-between text-xs text-muted-foreground">
                        <span>
                          Fecha: {notif.fechaEnvio ? new Date(notif.fechaEnvio).toLocaleString('es-ES') : 'Pendiente'}
                        </span>
                        <div className="flex items-center gap-2">
                          {notif.estado.toUpperCase() === 'NO_LEIDA' && (
                            <Button
                              onClick={() => handleMarkAsRead(notif.id)}
                              className="h-8 text-xs bg-primary hover:bg-primary/90"
                            >
                              Marcar como leída
                            </Button>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </div>
      )}
    </div>
  )
}
