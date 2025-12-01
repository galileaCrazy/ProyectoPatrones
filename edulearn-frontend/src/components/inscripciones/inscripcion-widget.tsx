'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { Award, CreditCard, BookOpen, ArrowRight, TrendingUp } from 'lucide-react'

interface TipoInscripcion {
  tipo: string
  nombre: string
  descripcion: string
}

interface WidgetProps {
  onSelectTipo?: (tipo: string) => void
  compact?: boolean
}

/**
 * Widget compacto para mostrar las opciones de inscripción
 * Útil para dashboards o vistas resumen
 */
export default function InscripcionWidget({ onSelectTipo, compact = false }: WidgetProps) {
  const [tipos, setTipos] = useState<TipoInscripcion[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchTipos()
  }, [])

  const fetchTipos = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/inscripciones/proceso/tipos')
      const data = await response.json()
      setTipos(data)
    } catch (error) {
      console.error('Error al cargar tipos:', error)
    } finally {
      setLoading(false)
    }
  }

  const getIconoTipo = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case 'GRATUITA':
        return <BookOpen className="h-5 w-5" />
      case 'PAGA':
        return <CreditCard className="h-5 w-5" />
      case 'BECA':
        return <Award className="h-5 w-5" />
      default:
        return <BookOpen className="h-5 w-5" />
    }
  }

  const getColorTipo = (tipo: string) => {
    switch (tipo.toUpperCase()) {
      case 'GRATUITA':
        return 'bg-blue-500/10 text-blue-600 hover:bg-blue-500/20'
      case 'PAGA':
        return 'bg-green-500/10 text-green-600 hover:bg-green-500/20'
      case 'BECA':
        return 'bg-purple-500/10 text-purple-600 hover:bg-purple-500/20'
      default:
        return 'bg-gray-500/10 text-gray-600 hover:bg-gray-500/20'
    }
  }

  if (loading) {
    return (
      <Card>
        <CardContent className="p-6">
          <div className="flex items-center justify-center">
            <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-primary"></div>
          </div>
        </CardContent>
      </Card>
    )
  }

  if (compact) {
    return (
      <Card>
        <CardHeader className="pb-3">
          <CardTitle className="text-lg flex items-center gap-2">
            <TrendingUp className="h-5 w-5" />
            Tipos de Inscripción
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-2">
          {tipos.map((tipo) => (
            <Button
              key={tipo.tipo}
              variant="outline"
              className={`w-full justify-start gap-2 ${getColorTipo(tipo.tipo)}`}
              onClick={() => onSelectTipo?.(tipo.tipo)}
            >
              {getIconoTipo(tipo.tipo)}
              <span className="font-medium">{tipo.nombre}</span>
            </Button>
          ))}
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <div>
          <h3 className="text-lg font-semibold">Opciones de Inscripción</h3>
          <p className="text-sm text-muted-foreground">
            Selecciona el tipo de inscripción que deseas realizar
          </p>
        </div>
        <Badge variant="outline">Template Method</Badge>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {tipos.map((tipo) => (
          <Card
            key={tipo.tipo}
            className="cursor-pointer hover:shadow-lg transition-all group"
            onClick={() => onSelectTipo?.(tipo.tipo)}
          >
            <CardHeader>
              <div className="flex items-center justify-between mb-2">
                <div className={`p-2 rounded-lg ${getColorTipo(tipo.tipo)}`}>
                  {getIconoTipo(tipo.tipo)}
                </div>
                <ArrowRight className="h-4 w-4 text-muted-foreground group-hover:text-primary transition-colors" />
              </div>
              <CardTitle className="text-lg">{tipo.nombre}</CardTitle>
              <CardDescription className="line-clamp-2">
                {tipo.descripcion}
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Button
                variant="ghost"
                size="sm"
                className="w-full"
                onClick={(e) => {
                  e.stopPropagation()
                  onSelectTipo?.(tipo.tipo)
                }}
              >
                Seleccionar
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}
