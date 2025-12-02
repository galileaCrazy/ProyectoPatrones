'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

interface Configuracion {
  id: number
  clave: string
  valor: string
  descripcion: string
  tipo: string
}

interface SystemSettingsProps {
  userRole: 'admin'
}

export default function SystemSettings({ userRole }: SystemSettingsProps) {
  const [configuraciones, setConfiguraciones] = useState<Record<string, string>>({})
  const [configuracionesCompletas, setConfiguracionesCompletas] = useState<Configuracion[]>([])
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [stats, setStats] = useState<any>(null)
  const [editando, setEditando] = useState<string | null>(null)
  const [valorTemporal, setValorTemporal] = useState('')

  useEffect(() => {
    fetchConfiguraciones()
    fetchStats()
  }, [])

  const fetchConfiguraciones = async () => {
    setLoading(true)
    try {
      // Obtener configuraciones desde el Singleton (caché)
      const response = await fetch('http://localhost:8080/api/configuraciones')
      if (response.ok) {
        const data = await response.json()
        setConfiguraciones(data)
      }

      // Obtener configuraciones completas desde BD
      const responseCompletas = await fetch('http://localhost:8080/api/configuraciones/completas')
      if (responseCompletas.ok) {
        const dataCompletas = await responseCompletas.json()
        setConfiguracionesCompletas(dataCompletas)
      }
    } catch (error) {
      console.error('Error al cargar configuraciones:', error)
    } finally {
      setLoading(false)
    }
  }

  const fetchStats = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/configuraciones/estadisticas')
      if (response.ok) {
        const data = await response.json()
        setStats(data)
      }
    } catch (error) {
      console.error('Error al cargar estadísticas:', error)
    }
  }

  const handleEdit = (clave: string, valor: string) => {
    setEditando(clave)
    setValorTemporal(valor)
  }

  const handleSave = async (clave: string) => {
    setSaving(true)
    try {
      const response = await fetch(`http://localhost:8080/api/configuraciones/${clave}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ valor: valorTemporal })
      })

      if (response.ok) {
        alert(`✅ Configuración actualizada en el Singleton!\nClave: ${clave}`)
        setEditando(null)
        fetchConfiguraciones()
        fetchStats()
      } else {
        alert('❌ Error al actualizar configuración')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('❌ Error al comunicarse con el servidor')
    } finally {
      setSaving(false)
    }
  }

  const handleReload = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/configuraciones/recargar', {
        method: 'POST'
      })

      if (response.ok) {
        alert('✅ Configuraciones recargadas desde la base de datos al Singleton')
        fetchConfiguraciones()
        fetchStats()
      } else {
        alert('❌ Error al recargar configuraciones')
      }
    } catch (error) {
      console.error('Error:', error)
    }
  }

  const handleCancel = () => {
    setEditando(null)
    setValorTemporal('')
  }

  if (userRole !== 'admin') {
    return (
      <div className="p-8 max-w-7xl mx-auto">
        <Card className="border-red-200 bg-red-50">
          <CardContent className="pt-6">
            <p className="text-red-800">⛔ Solo los administradores pueden acceder a esta sección</p>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-start mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">
            Configuración del Sistema
            <span className="ml-3 text-sm text-primary">Singleton Pattern</span>
          </h1>
          <p className="text-muted-foreground">
            Gestión centralizada usando el patrón Singleton
          </p>
        </div>
        <Button
          onClick={handleReload}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
        >
          🔄 Recargar desde BD
        </Button>
      </div>

      {/* Singleton Info Card */}
      <Card className="mb-6 border-primary/20 bg-primary/5">
        <CardContent className="pt-6">
          <div className="flex items-start gap-4">
            <div className="text-4xl">🎯</div>
            <div className="flex-1">
              <h3 className="font-semibold text-foreground mb-2">
                Patrón Singleton en Acción
              </h3>
              <p className="text-sm text-muted-foreground mb-3">
                El sistema usa el patrón <strong>Singleton</strong> para garantizar una única instancia del gestor
                de configuraciones en toda la aplicación. Todas las configuraciones se mantienen en caché en memoria
                para un acceso rápido, y se sincronizan automáticamente con la base de datos.
              </p>
              <div className="grid grid-cols-4 gap-4 mt-4">
                <div className="bg-background border border-border rounded-lg p-3">
                  <p className="text-xs text-muted-foreground">Instancias</p>
                  <p className="text-2xl font-bold text-primary">1</p>
                  <p className="text-xs text-muted-foreground mt-1">Única instancia</p>
                </div>
                <div className="bg-background border border-border rounded-lg p-3">
                  <p className="text-xs text-muted-foreground">En Caché</p>
                  <p className="text-2xl font-bold text-green-600">
                    {stats?.cantidadEnCache || 0}
                  </p>
                  <p className="text-xs text-muted-foreground mt-1">Configuraciones</p>
                </div>
                <div className="bg-background border border-border rounded-lg p-3">
                  <p className="text-xs text-muted-foreground">Accesos</p>
                  <p className="text-2xl font-bold text-blue-600">
                    {stats?.totalAccesos || 0}
                  </p>
                  <p className="text-xs text-muted-foreground mt-1">Al Singleton</p>
                </div>
                <div className="bg-background border border-border rounded-lg p-3">
                  <p className="text-xs text-muted-foreground">Thread-Safe</p>
                  <p className="text-2xl font-bold text-purple-600">✓</p>
                  <p className="text-xs text-muted-foreground mt-1">DCL Pattern</p>
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Configurations List */}
      {loading ? (
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Cargando configuraciones del Singleton...</p>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {configuracionesCompletas.length === 0 ? (
            <Card className="col-span-full">
              <CardContent className="pt-6 text-center">
                <p className="text-muted-foreground">No hay configuraciones disponibles</p>
              </CardContent>
            </Card>
          ) : (
            configuracionesCompletas.map((config) => (
              <Card key={config.id} className="hover:shadow-md transition-shadow">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg">{config.clave}</CardTitle>
                      <CardDescription className="mt-1">
                        {config.descripcion || 'Sin descripción'}
                      </CardDescription>
                    </div>
                    <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded">
                      {config.tipo}
                    </span>
                  </div>
                </CardHeader>
                <CardContent>
                  {editando === config.clave ? (
                    <div className="space-y-3">
                      <input
                        type="text"
                        value={valorTemporal}
                        onChange={(e) => setValorTemporal(e.target.value)}
                        className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        autoFocus
                      />
                      <div className="flex gap-2">
                        <Button
                          onClick={() => handleSave(config.clave)}
                          disabled={saving}
                          className="flex-1 bg-green-600 hover:bg-green-700 text-white"
                        >
                          {saving ? 'Guardando...' : '✓ Guardar'}
                        </Button>
                        <Button
                          onClick={handleCancel}
                          disabled={saving}
                          className="flex-1 bg-muted hover:bg-muted/80 text-foreground"
                        >
                          ✕ Cancelar
                        </Button>
                      </div>
                    </div>
                  ) : (
                    <div className="space-y-3">
                      <div className="p-3 rounded-lg bg-muted/50 border border-border">
                        <p className="text-sm font-mono text-foreground break-all">
                          {configuraciones[config.clave] || config.valor}
                        </p>
                      </div>
                      <Button
                        onClick={() => handleEdit(config.clave, configuraciones[config.clave] || config.valor)}
                        className="w-full bg-primary hover:bg-primary/90 text-primary-foreground"
                      >
                        ✏️ Editar
                      </Button>
                    </div>
                  )}

                  <div className="mt-3 pt-3 border-t border-border text-xs text-muted-foreground">
                    <p>Almacenado en: Singleton (caché) + Base de datos</p>
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
