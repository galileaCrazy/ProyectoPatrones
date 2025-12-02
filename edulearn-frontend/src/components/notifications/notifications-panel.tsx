'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

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
}

export default function NotificationsPanel({ userRole }: NotificationsPanelProps) {
  const [notifications, setNotifications] = useState<Notificacion[]>([])
  const [loading, setLoading] = useState(false)
  const [sending, setSending] = useState(false)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    tipo: 'EMAIL',
    destinatario: '',
    asunto: '',
    mensaje: ''
  })

  useEffect(() => {
    fetchNotifications()
  }, [])

  const fetchNotifications = async () => {
    setLoading(true)
    try {
      const response = await fetch('http://localhost:8080/api/notificaciones')
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

  const handleSendNotification = async () => {
    setSending(true)
    try {
      const response = await fetch('http://localhost:8080/api/notificaciones', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      })

      if (response.ok) {
        alert('✅ Notificación enviada exitosamente usando Factory Method!')
        setFormData({
          tipo: 'EMAIL',
          destinatario: '',
          asunto: '',
          mensaje: ''
        })
        setShowForm(false)
        fetchNotifications()
      } else {
        alert('❌ Error al enviar notificación')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('❌ Error al comunicarse con el servidor')
    } finally {
      setSending(false)
    }
  }

  const handleRetry = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/notificaciones/${id}/reintentar`, {
        method: 'POST'
      })

      if (response.ok) {
        alert('✅ Reintento de envío exitoso')
        fetchNotifications()
      } else {
        alert('❌ Error al reintentar')
      }
    } catch (error) {
      console.error('Error:', error)
    }
  }

  const getNotificationIcon = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case 'EMAIL':
        return '📧'
      case 'SMS':
        return '📱'
      case 'PUSH':
        return '🔔'
      default:
        return '📨'
    }
  }

  const getStatusBadge = (estado: string) => {
    switch (estado.toUpperCase()) {
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
            <span className="ml-3 text-sm text-primary">Factory Method Pattern</span>
          </h1>
          <p className="text-muted-foreground">
            Gestión de notificaciones usando el patrón Factory Method
          </p>
        </div>
        {(userRole === 'admin' || userRole === 'professor') && (
          <Button
            onClick={() => setShowForm(!showForm)}
            className="bg-primary hover:bg-primary/90 text-primary-foreground"
          >
            {showForm ? 'Cancelar' : '+ Nueva Notificación'}
          </Button>
        )}
      </div>

      {/* Factory Method Info Card */}
      <Card className="mb-6 border-primary/20 bg-primary/5">
        <CardContent className="pt-6">
          <div className="flex items-start gap-4">
            <div className="text-4xl">🏭</div>
            <div className="flex-1">
              <h3 className="font-semibold text-foreground mb-2">
                Patrón Factory Method en Acción
              </h3>
              <p className="text-sm text-muted-foreground mb-3">
                Este sistema usa el patrón <strong>Factory Method</strong> para crear diferentes tipos de notificaciones
                (Email, SMS, Push) sin que el código cliente conozca las clases concretas. La fábrica decide qué tipo
                de notificación instanciar según el parámetro proporcionado.
              </p>
              <div className="flex gap-2 flex-wrap">
                <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded dark:bg-blue-900 dark:text-blue-200">
                  📧 Email
                </span>
                <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded dark:bg-green-900 dark:text-green-200">
                  📱 SMS
                </span>
                <span className="text-xs bg-purple-100 text-purple-800 px-2 py-1 rounded dark:bg-purple-900 dark:text-purple-200">
                  🔔 Push
                </span>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Form */}
      {showForm && (
        <Card className="mb-6">
          <CardHeader>
            <CardTitle>Enviar Nueva Notificación</CardTitle>
            <CardDescription>
              El tipo de notificación se crea dinámicamente usando Factory Method
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Tipo de Notificación *
                </label>
                <select
                  value={formData.tipo}
                  onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="EMAIL">📧 Email</option>
                  <option value="SMS">📱 SMS</option>
                  <option value="PUSH">🔔 Push Notification</option>
                </select>
              </div>

              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Destinatario *
                </label>
                <input
                  type="text"
                  placeholder={formData.tipo === 'EMAIL' ? 'correo@ejemplo.com' : formData.tipo === 'SMS' ? '+1234567890' : 'user_id'}
                  value={formData.destinatario}
                  onChange={(e) => setFormData({ ...formData, destinatario: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Asunto *
                </label>
                <input
                  type="text"
                  placeholder="Asunto de la notificación"
                  value={formData.asunto}
                  onChange={(e) => setFormData({ ...formData, asunto: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>

              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Mensaje *
                </label>
                <textarea
                  placeholder="Contenido de la notificación"
                  value={formData.mensaje}
                  onChange={(e) => setFormData({ ...formData, mensaje: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary h-24 resize-none"
                />
              </div>

              <Button
                onClick={handleSendNotification}
                disabled={sending || !formData.destinatario || !formData.asunto || !formData.mensaje}
                className="w-full bg-primary hover:bg-primary/90 text-primary-foreground"
              >
                {sending ? 'Enviando...' : 'Enviar Notificación'}
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

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
                          Enviado: {new Date(notif.fechaEnvio).toLocaleString('es-ES')}
                        </span>
                        <div className="flex items-center gap-2">
                          {notif.estado.toUpperCase() === 'FALLIDA' && (userRole === 'admin' || userRole === 'professor') && (
                            <Button
                              onClick={() => handleRetry(notif.id)}
                              className="h-8 text-xs"
                            >
                              🔄 Reintentar
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
