'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Plus, Video, FolderOpen, Trash2, ExternalLink, Calendar, Clock, Cloud } from 'lucide-react'

interface Integracion {
  id: number
  cursoId: number
  profesorId: number
  tipo: string
  proveedor: string
  nombre: string
  descripcion: string
  urlRecurso: string
  estado: string
  vecesUsado: number
  fechaCreacion: string
  fechaUso: string | null
}

interface IntegrationsManagerProps {
  cursoId: number
  profesorId: number
}

export default function IntegrationsManager({ cursoId, profesorId }: IntegrationsManagerProps) {
  const [integraciones, setIntegraciones] = useState<Integracion[]>([])
  const [loading, setLoading] = useState(true)
  const [showCreateDialog, setShowCreateDialog] = useState(false)
  const [tipoSeleccionado, setTipoSeleccionado] = useState<string>('')
  const [proveedorSeleccionado, setProveedorSeleccionado] = useState<string>('')
  const [formData, setFormData] = useState({
    nombre: '',
    titulo: '',
    descripcion: '',
    fechaInicio: '',
    duracionMinutos: 60
  })
  const [creating, setCreating] = useState(false)
  const [estadisticas, setEstadisticas] = useState<any>(null)

  const proveedoresVideoconferencia = [
    { id: 'GOOGLE_MEET', nombre: 'Google Meet' },
    { id: 'ZOOM', nombre: 'Zoom' }
  ]

  const proveedoresRepositorio = [
    { id: 'GOOGLE_DRIVE', nombre: 'Google Drive' },
    { id: 'ONEDRIVE', nombre: 'OneDrive' }
  ]

  useEffect(() => {
    cargarIntegraciones()
    cargarEstadisticas()
  }, [cursoId])

  const cargarIntegraciones = async () => {
    try {
      setLoading(true)
      const response = await fetch(`http://localhost:8080/api/integraciones-externas/curso/${cursoId}/activas`)
      if (response.ok) {
        const data = await response.json()
        setIntegraciones(data)
      }
    } catch (error) {
      console.error('Error al cargar integraciones:', error)
    } finally {
      setLoading(false)
    }
  }

  const cargarEstadisticas = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/integraciones-externas/curso/${cursoId}/estadisticas`)
      if (response.ok) {
        const data = await response.json()
        setEstadisticas(data)
      }
    } catch (error) {
      console.error('Error al cargar estadísticas:', error)
    }
  }

  const handleCrearIntegracion = async () => {
    if (!proveedorSeleccionado || !formData.nombre) {
      alert('Por favor complete todos los campos requeridos')
      return
    }

    try {
      setCreating(true)

      const datos: any = {
        nombre: formData.nombre,
        descripcion: formData.descripcion
      }

      // Agregar datos específicos según el tipo
      if (tipoSeleccionado === 'VIDEOCONFERENCIA') {
        datos.titulo = formData.titulo || formData.nombre
        if (formData.fechaInicio) {
          datos.fechaInicio = formData.fechaInicio
        }
        datos.duracionMinutos = formData.duracionMinutos
        datos.requierePassword = proveedorSeleccionado === 'ZOOM'
      } else if (tipoSeleccionado === 'REPOSITORIO') {
        datos.esPublica = false
        datos.permisos = 'edit'
      }

      const response = await fetch('http://localhost:8080/api/integraciones-externas/crear', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          cursoId,
          profesorId,
          proveedor: proveedorSeleccionado,
          datos
        })
      })

      if (response.ok) {
        const result = await response.json()
        alert(`¡Integración creada exitosamente!\n\n${result.mensaje}\n\nURL: ${result.integracion.urlRecurso}`)
        setShowCreateDialog(false)
        resetForm()
        cargarIntegraciones()
        cargarEstadisticas()
      } else {
        const error = await response.json()
        alert(`Error: ${error.error || 'No se pudo crear la integración'}`)
      }
    } catch (error) {
      console.error('Error al crear integración:', error)
      alert('Error al crear la integración')
    } finally {
      setCreating(false)
    }
  }

  const handleEliminarIntegracion = async (id: number) => {
    if (!confirm('¿Está seguro de eliminar esta integración?')) {
      return
    }

    try {
      const response = await fetch(`http://localhost:8080/api/integraciones-externas/${id}`, {
        method: 'DELETE'
      })

      if (response.ok) {
        alert('Integración eliminada exitosamente')
        cargarIntegraciones()
        cargarEstadisticas()
      }
    } catch (error) {
      console.error('Error al eliminar:', error)
      alert('Error al eliminar la integración')
    }
  }

  const handleAbrirRecurso = async (integracion: Integracion) => {
    // Registrar uso
    try {
      await fetch(`http://localhost:8080/api/integraciones-externas/${integracion.id}/usar`, {
        method: 'POST'
      })
    } catch (error) {
      console.error('Error al registrar uso:', error)
    }

    // Abrir URL
    window.open(integracion.urlRecurso, '_blank')
  }

  const resetForm = () => {
    setFormData({
      nombre: '',
      titulo: '',
      descripcion: '',
      fechaInicio: '',
      duracionMinutos: 60
    })
    setTipoSeleccionado('')
    setProveedorSeleccionado('')
  }

  const getProveedorIcon = (proveedor: string) => {
    switch(proveedor) {
      case 'GOOGLE_MEET':
      case 'ZOOM':
        return <Video className="w-8 h-8" />
      case 'GOOGLE_DRIVE':
        return <FolderOpen className="w-8 h-8" />
      case 'ONEDRIVE':
        return <Cloud className="w-8 h-8" />
      default:
        return <ExternalLink className="w-8 h-8" />
    }
  }

  const getProveedorNombre = (proveedor: string) => {
    const nombres: Record<string, string> = {
      'GOOGLE_MEET': 'Google Meet',
      'ZOOM': 'Zoom',
      'GOOGLE_DRIVE': 'Google Drive',
      'ONEDRIVE': 'OneDrive'
    }
    return nombres[proveedor] || proveedor
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Cargando integraciones...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header con estadísticas */}
      <div className="flex justify-between items-start">
        <div>
          <h2 className="text-2xl font-bold text-foreground">Integraciones Externas</h2>
          <p className="text-muted-foreground mt-1">
            Gestiona videoconferencias y repositorios de archivos
          </p>
          {estadisticas && (
            <div className="flex gap-4 mt-3 text-sm">
              <span className="text-muted-foreground">
                Total: <strong>{estadisticas.total}</strong>
              </span>
              <span className="text-muted-foreground">
                Activas: <strong>{estadisticas.activas}</strong>
              </span>
              <span className="text-muted-foreground">
                Videoconferencias: <strong>{estadisticas.videoconferencias}</strong>
              </span>
              <span className="text-muted-foreground">
                Repositorios: <strong>{estadisticas.repositorios}</strong>
              </span>
            </div>
          )}
        </div>
        <Button
          onClick={() => setShowCreateDialog(true)}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
        >
          <Plus className="w-4 h-4 mr-2" />
          Nueva Integración
        </Button>
      </div>

      {/* Lista de integraciones */}
      {integraciones.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center p-12">
            <ExternalLink className="w-16 h-16 text-muted-foreground mb-4" />
            <h3 className="text-xl font-semibold text-foreground mb-2">
              No hay integraciones configuradas
            </h3>
            <p className="text-muted-foreground text-center mb-6">
              Agrega integraciones de videoconferencia (Meet, Zoom) o repositorios (Drive, OneDrive)
            </p>
            <Button onClick={() => setShowCreateDialog(true)}>
              <Plus className="w-4 h-4 mr-2" />
              Crear Primera Integración
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {integraciones.map((integracion) => (
            <Card key={integracion.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <div className="text-3xl">
                      {getProveedorIcon(integracion.proveedor)}
                    </div>
                    <div>
                      <CardTitle className="text-lg">{integracion.nombre}</CardTitle>
                      <CardDescription>
                        {getProveedorNombre(integracion.proveedor)}
                      </CardDescription>
                    </div>
                  </div>
                  <div className={`px-2 py-1 rounded text-xs font-medium ${
                    integracion.tipo === 'VIDEOCONFERENCIA'
                      ? 'bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300'
                      : 'bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300'
                  }`}>
                    {integracion.tipo === 'VIDEOCONFERENCIA' ? 'Videoconferencia' : 'Repositorio'}
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {integracion.descripcion && (
                  <p className="text-sm text-muted-foreground">
                    {integracion.descripcion}
                  </p>
                )}

                <div className="flex items-center gap-4 text-xs text-muted-foreground">
                  <div className="flex items-center gap-1">
                    <Clock className="w-3 h-3" />
                    {integracion.vecesUsado} usos
                  </div>
                  {integracion.fechaUso && (
                    <div className="flex items-center gap-1">
                      <Calendar className="w-3 h-3" />
                      Último uso: {new Date(integracion.fechaUso).toLocaleDateString()}
                    </div>
                  )}
                </div>

                <div className="flex gap-2">
                  <Button
                    onClick={() => handleAbrirRecurso(integracion)}
                    className="flex-1 bg-primary hover:bg-primary/90 text-primary-foreground"
                  >
                    <ExternalLink className="w-4 h-4 mr-2" />
                    Abrir
                  </Button>
                  <Button
                    onClick={() => handleEliminarIntegracion(integracion.id)}
                    variant="outline"
                    className="border-red-500 text-red-500 hover:bg-red-50 dark:hover:bg-red-950"
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Modal de creación */}
      {showCreateDialog && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-background border border-border rounded-lg p-6 max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <h2 className="text-2xl font-bold text-foreground mb-4">
              Nueva Integración Externa
            </h2>
            <p className="text-sm text-muted-foreground mb-6">
              <strong>Patrón Adapter:</strong> Integra sistemas externos con interfaces unificadas
            </p>

            <div className="space-y-6">
              {/* Paso 1: Seleccionar tipo */}
              {!tipoSeleccionado && (
                <div>
                  <h3 className="text-lg font-semibold mb-3">1. Selecciona el tipo de integración</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <button
                      onClick={() => setTipoSeleccionado('VIDEOCONFERENCIA')}
                      className="p-6 border-2 border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-all text-center"
                    >
                      <Video className="w-12 h-12 mx-auto mb-3 text-primary" />
                      <h4 className="font-semibold text-foreground">Videoconferencia</h4>
                      <p className="text-sm text-muted-foreground mt-2">
                        Google Meet, Zoom
                      </p>
                    </button>
                    <button
                      onClick={() => setTipoSeleccionado('REPOSITORIO')}
                      className="p-6 border-2 border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-all text-center"
                    >
                      <FolderOpen className="w-12 h-12 mx-auto mb-3 text-primary" />
                      <h4 className="font-semibold text-foreground">Repositorio</h4>
                      <p className="text-sm text-muted-foreground mt-2">
                        Google Drive, OneDrive
                      </p>
                    </button>
                  </div>
                </div>
              )}

              {/* Paso 2: Seleccionar proveedor */}
              {tipoSeleccionado && !proveedorSeleccionado && (
                <div>
                  <h3 className="text-lg font-semibold mb-3">2. Selecciona el proveedor</h3>
                  <div className="grid grid-cols-2 gap-4">
                    {(tipoSeleccionado === 'VIDEOCONFERENCIA' ? proveedoresVideoconferencia : proveedoresRepositorio).map((proveedor) => (
                      <button
                        key={proveedor.id}
                        onClick={() => setProveedorSeleccionado(proveedor.id)}
                        className="p-6 border-2 border-border rounded-lg hover:border-primary hover:bg-primary/5 transition-all text-center"
                      >
                        {tipoSeleccionado === 'VIDEOCONFERENCIA' ? (
                          <Video className="w-12 h-12 mx-auto mb-3 text-primary" />
                        ) : (
                          <FolderOpen className="w-12 h-12 mx-auto mb-3 text-primary" />
                        )}
                        <h4 className="font-semibold text-foreground">{proveedor.nombre}</h4>
                      </button>
                    ))}
                  </div>
                  <Button
                    onClick={() => setTipoSeleccionado('')}
                    variant="outline"
                    className="mt-4"
                  >
                    ← Atrás
                  </Button>
                </div>
              )}

              {/* Paso 3: Formulario de datos */}
              {proveedorSeleccionado && (
                <div>
                  <h3 className="text-lg font-semibold mb-3">3. Configura la integración</h3>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-foreground mb-2">
                        Nombre *
                      </label>
                      <input
                        type="text"
                        value={formData.nombre}
                        onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                        className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                        placeholder={tipoSeleccionado === 'VIDEOCONFERENCIA' ? 'Ej: Clase Semanal de Matemáticas' : 'Ej: Materiales del Curso'}
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-foreground mb-2">
                        Descripción
                      </label>
                      <textarea
                        value={formData.descripcion}
                        onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                        className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                        rows={3}
                        placeholder="Descripción opcional"
                      />
                    </div>

                    {tipoSeleccionado === 'VIDEOCONFERENCIA' && (
                      <>
                        <div>
                          <label className="block text-sm font-medium text-foreground mb-2">
                            Fecha y Hora de Inicio
                          </label>
                          <input
                            type="datetime-local"
                            value={formData.fechaInicio}
                            onChange={(e) => setFormData({ ...formData, fechaInicio: e.target.value })}
                            className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                          />
                        </div>

                        <div>
                          <label className="block text-sm font-medium text-foreground mb-2">
                            Duración (minutos)
                          </label>
                          <input
                            type="number"
                            value={formData.duracionMinutos}
                            onChange={(e) => setFormData({ ...formData, duracionMinutos: parseInt(e.target.value) })}
                            className="w-full px-3 py-2 border border-input rounded-lg bg-background text-foreground"
                            min="15"
                            step="15"
                          />
                        </div>
                      </>
                    )}
                  </div>

                  <div className="flex gap-3 mt-6">
                    <Button
                      onClick={() => {
                        setProveedorSeleccionado('')
                        resetForm()
                      }}
                      variant="outline"
                      className="flex-1"
                    >
                      ← Atrás
                    </Button>
                    <Button
                      onClick={handleCrearIntegracion}
                      disabled={creating || !formData.nombre}
                      className="flex-1 bg-primary hover:bg-primary/90 text-primary-foreground"
                    >
                      {creating ? 'Creando...' : 'Crear Integración'}
                    </Button>
                  </div>
                </div>
              )}
            </div>

            <Button
              onClick={() => {
                setShowCreateDialog(false)
                resetForm()
              }}
              variant="outline"
              className="w-full mt-4"
            >
              Cancelar
            </Button>
          </div>
        </div>
      )}
    </div>
  )
}
