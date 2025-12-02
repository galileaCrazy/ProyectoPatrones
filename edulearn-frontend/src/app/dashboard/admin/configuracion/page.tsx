
'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { toast } from '@/components/ui/use-toast'

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api"

export default function ConfiguracionPage() {
  const [configs, setConfigs] = useState<Record<string, string>>({})
  const [loading, setLoading] = useState(true)
  const [newKey, setNewKey] = useState('')
  const [newValue, setNewValue] = useState('')
  const [isUpdating, setIsUpdating] = useState(false)

  // Cargar configuración inicial usando el patrón Singleton
  const fetchConfig = async () => {
    setLoading(true)
    try {
      const response = await fetch(`${API_URL}/patrones/singleton/configuracion`)
      const data = await response.json()
      if (response.ok) {
        setConfigs(data.configuraciones || {})
      } else {
        throw new Error(data.error || 'No se pudo cargar la configuración')
      }
    } catch (error: any) {
      toast({
        title: 'Error',
        description: error.message,
        variant: 'destructive',
      })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchConfig()
  }, [])

  // Actualizar una configuración
  const handleUpdate = async () => {
    if (!newKey || !newValue) {
      toast({
        title: 'Campos requeridos',
        description: 'La clave y el valor no pueden estar vacíos.',
        variant: 'destructive',
      })
      return
    }

    setIsUpdating(true)
    try {
      const params = new URLSearchParams({ clave: newKey, valor: newValue })
      const response = await fetch(`${API_URL}/patrones/singleton/configuracion?${params.toString()}`, {
        method: 'POST',
      })
      const data = await response.json()

      if (response.ok) {
        toast({
          title: 'Configuración Actualizada',
          description: `La clave '${newKey}' ha sido guardada.`,
        })
        setConfigs(data.configuraciones || {})
        setNewKey('')
        setNewValue('')
      } else {
        throw new Error(data.error || 'No se pudo actualizar la configuración')
      }
    } catch (error: any) {
      toast({
        title: 'Error',
        description: error.message,
        variant: 'destructive',
      })
    } finally {
      setIsUpdating(false)
    }
  }

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-2">Configuración del Sistema</h1>
      <p className="text-muted-foreground mb-8">
        Gestión de la configuración centralizada (Patrón Singleton). Cualquier cambio aquí afecta a todo el sistema.
      </p>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Formulario de actualización */}
        <Card>
          <CardHeader>
            <CardTitle>Actualizar Configuración</CardTitle>
            <CardDescription>Añada o modifique una clave de configuración.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <label className="text-sm font-medium">Clave</label>
              <Input
                placeholder="Ej: periodoAcademicoActual"
                value={newKey}
                onChange={(e) => setNewKey(e.target.value)}
                disabled={isUpdating}
              />
            </div>
            <div>
              <label className="text-sm font-medium">Valor</label>
              <Input
                placeholder="Ej: 2025-2"
                value={newValue}
                onChange={(e) => setNewValue(e.target.value)}
                disabled={isUpdating}
              />
            </div>
            <Button onClick={handleUpdate} disabled={isUpdating} className="w-full">
              {isUpdating ? 'Guardando...' : 'Guardar Configuración'}
            </Button>
          </CardContent>
        </Card>

        {/* Lista de configuraciones */}
        <Card>
          <CardHeader>
            <CardTitle>Configuraciones Actuales</CardTitle>
            <CardDescription>Esta es la instancia única de configuración.</CardDescription>
          </CardHeader>
          <CardContent>
            {loading ? (
              <p>Cargando...</p>
            ) : Object.keys(configs).length === 0 ? (
              <p className="text-muted-foreground">No hay configuraciones definidas.</p>
            ) : (
              <ul className="space-y-2">
                {Object.entries(configs).map(([key, value]) => (
                  <li key={key} className="flex justify-between p-2 rounded bg-muted/50">
                    <span className="font-semibold">{key}:</span>
                    <span>{value}</span>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
