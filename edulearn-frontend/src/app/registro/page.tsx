'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api'

export default function RegistroPage() {
  const router = useRouter()
  const [formData, setFormData] = useState({
    nombre: '',
    apellidos: '',
    email: '',
    password: '',
    tipoUsuario: 'estudiante'
  })
  const [mensaje, setMensaje] = useState('')
  const [loading, setLoading] = useState(false)
  const [success, setSuccess] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setMensaje('')
    setSuccess(false)

    // Validaciones
    if (!formData.nombre.trim() || !formData.apellidos.trim() ||
        !formData.email.trim() || !formData.password.trim()) {
      setMensaje('Por favor, complete todos los campos')
      return
    }

    setLoading(true)

    try {
      const res = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      })

      const data = await res.json()

      if (data.exito) {
        setSuccess(true)
        setMensaje('¡Registro exitoso! Redirigiendo al login...')
        setTimeout(() => {
          router.push('/')
        }, 2000)
      } else {
        setMensaje(data.mensaje)
      }
    } catch (error) {
      setMensaje('Error al conectar con el servidor')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary via-background to-background flex items-center justify-center p-4">
      {/* Background decorative element */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute w-96 h-96 bg-primary/10 rounded-full blur-3xl -top-20 -left-20"></div>
        <div className="absolute w-96 h-96 bg-primary/5 rounded-full blur-3xl -bottom-20 -right-20"></div>
      </div>

      <Card className="w-full max-w-lg relative z-10 border-border/50 shadow-xl">
        <CardHeader className="space-y-2">
          <div className="flex items-center justify-center gap-2 mb-4">
            <div className="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
              <span className="text-primary-foreground font-bold text-lg">📚</span>
            </div>
            <span className="text-xl font-bold text-foreground">EduLearn</span>
          </div>
          <CardTitle className="text-center text-2xl">Crear Cuenta</CardTitle>
          <CardDescription className="text-center">Sistema de Gestión de Aprendizaje</CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Nombre */}
            <div className="space-y-2">
              <label htmlFor="nombre" className="text-sm font-medium text-foreground">
                Nombre
              </label>
              <input
                id="nombre"
                type="text"
                placeholder="Juan"
                value={formData.nombre}
                onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            {/* Apellidos */}
            <div className="space-y-2">
              <label htmlFor="apellidos" className="text-sm font-medium text-foreground">
                Apellidos
              </label>
              <input
                id="apellidos"
                type="text"
                placeholder="Pérez García"
                value={formData.apellidos}
                onChange={(e) => setFormData({ ...formData, apellidos: e.target.value })}
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            {/* Correo Electrónico */}
            <div className="space-y-2">
              <label htmlFor="email" className="text-sm font-medium text-foreground">
                Correo Electrónico
              </label>
              <input
                id="email"
                type="email"
                placeholder="tu@universidad.edu"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            {/* Contraseña */}
            <div className="space-y-2">
              <label htmlFor="password" className="text-sm font-medium text-foreground">
                Contraseña
              </label>
              <input
                id="password"
                type="password"
                placeholder="••••••••"
                value={formData.password}
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            {/* Tipo de Usuario */}
            <div className="space-y-3">
              <label className="text-sm font-medium text-foreground block">Tipo de Usuario</label>
              <div className="space-y-2">
                {[
                  { value: 'estudiante', label: 'Estudiante', icon: '👨‍🎓' },
                  { value: 'profesor', label: 'Profesor', icon: '👨‍🏫' }
                ].map((option) => (
                  <label
                    key={option.value}
                    className="flex items-center gap-3 p-3 rounded-lg border border-input cursor-pointer hover:bg-muted transition-colors"
                    style={{
                      backgroundColor: formData.tipoUsuario === option.value ? 'var(--color-muted)' : 'transparent',
                      borderColor: formData.tipoUsuario === option.value ? 'var(--color-primary)' : 'var(--color-border)'
                    }}
                  >
                    <input
                      type="radio"
                      name="tipoUsuario"
                      value={option.value}
                      checked={formData.tipoUsuario === option.value}
                      onChange={(e) => setFormData({ ...formData, tipoUsuario: e.target.value })}
                      className="w-4 h-4"
                    />
                    <span className="text-xl">{option.icon}</span>
                    <span className="font-medium">{option.label}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Mensaje de error/éxito */}
            {mensaje && (
              <div className={`px-4 py-3 rounded-lg text-sm ${
                success
                  ? 'bg-green-500/10 border border-green-500 text-green-600'
                  : 'bg-destructive/10 border border-destructive text-destructive'
              }`}>
                {mensaje}
              </div>
            )}

            {/* Botón Crear Cuenta */}
            <Button
              type="submit"
              disabled={loading}
              className="w-full bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-2 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Creando cuenta...' : 'Crear Cuenta'}
            </Button>
          </form>

          {/* Link a Login */}
          <div className="mt-6 text-center">
            <p className="text-sm text-muted-foreground">
              ¿Ya tienes una cuenta?{' '}
              <a href="/" className="text-primary font-semibold hover:underline">
                Iniciar sesión
              </a>
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
