'use client'

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

interface LoginPageProps {
  onLogin: (role: 'student' | 'professor' | 'admin') => void
}

export default function LoginPage({ onLogin }: LoginPageProps) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [selectedRole, setSelectedRole] = useState<'student' | 'professor' | 'admin'>('student')

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (email && password) {
      onLogin(selectedRole)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary via-background to-background flex items-center justify-center p-4">
      {/* Background decorative element */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute w-96 h-96 bg-primary/10 rounded-full blur-3xl -top-20 -left-20"></div>
        <div className="absolute w-96 h-96 bg-primary/5 rounded-full blur-3xl -bottom-20 -right-20"></div>
      </div>

      <Card className="w-full max-w-md relative z-10 border-border/50 shadow-xl">
        <CardHeader className="space-y-2">
          <div className="flex items-center justify-center gap-2 mb-4">
            <div className="w-10 h-10 bg-primary rounded-lg flex items-center justify-center">
              <span className="text-primary-foreground font-bold text-lg">📚</span>
            </div>
            <span className="text-xl font-bold text-foreground">EduLearn</span>
          </div>
          <CardTitle className="text-center text-2xl">Iniciar Sesión</CardTitle>
          <CardDescription className="text-center">Sistema de Gestión de Aprendizaje</CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label htmlFor="email" className="text-sm font-medium text-foreground">
                Correo Electrónico
              </label>
              <input
                id="email"
                type="email"
                placeholder="tu@universidad.edu"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            <div className="space-y-2">
              <label htmlFor="password" className="text-sm font-medium text-foreground">
                Contraseña
              </label>
              <input
                id="password"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
              />
            </div>

            <div className="space-y-3">
              <label className="text-sm font-medium text-foreground block">Tipo de Usuario</label>
              <div className="space-y-2">
                {(['student', 'professor', 'admin'] as const).map((role) => (
                  <label key={role} className="flex items-center gap-3 p-3 rounded-lg border border-input cursor-pointer hover:bg-muted transition-colors"
                    style={{
                      backgroundColor: selectedRole === role ? 'var(--color-muted)' : 'transparent',
                      borderColor: selectedRole === role ? 'var(--color-primary)' : 'var(--color-border)'
                    }}
                  >
                    <input
                      type="radio"
                      name="role"
                      value={role}
                      checked={selectedRole === role}
                      onChange={(e) => setSelectedRole(e.target.value as any)}
                      className="w-4 h-4"
                    />
                    <span className="font-medium capitalize">
                      {role === 'student' && 'Estudiante'}
                      {role === 'professor' && 'Profesor'}
                      {role === 'admin' && 'Administrador'}
                    </span>
                  </label>
                ))}
              </div>
            </div>

            <Button
              type="submit"
              className="w-full bg-primary hover:bg-primary/90 text-primary-foreground font-semibold py-2 rounded-lg transition-colors"
            >
              Iniciar Sesión
            </Button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-sm text-muted-foreground">
              Credenciales de demostración: cualquier email/contraseña
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
