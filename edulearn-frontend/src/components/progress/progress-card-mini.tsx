'use client'

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';
import { Button } from '@/components/ui/button';
import { CheckCircle2, BookOpen, TrendingUp } from 'lucide-react';

interface ProgressCardMiniProps {
  estudianteId: number;
  cursoId: number;
  nombreCurso: string;
}

export default function ProgressCardMini({ estudianteId, cursoId, nombreCurso }: ProgressCardMiniProps) {
  const router = useRouter();
  const [progreso, setProgreso] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [primerMaterialPendiente, setPrimerMaterialPendiente] = useState<any>(null);

  useEffect(() => {
    cargarProgreso();
    cargarPrimerMaterialPendiente();
  }, [estudianteId, cursoId]);

  const cargarProgreso = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/progreso/${estudianteId}/${cursoId}`);
      if (response.ok) {
        const data = await response.json();
        setProgreso(data);
      }
    } catch (error) {
      console.error('Error al cargar progreso:', error);
    } finally {
      setLoading(false);
    }
  };

  const cargarPrimerMaterialPendiente = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/materiales/siguiente-pendiente?estudianteId=${estudianteId}&cursoId=${cursoId}`
      );
      if (response.ok) {
        const material = await response.json();
        setPrimerMaterialPendiente(material);
      }
    } catch (error) {
      console.error('Error al cargar material pendiente:', error);
    }
  };

  const handleContinuarAprendiendo = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (primerMaterialPendiente) {
      router.push(`/curso/${cursoId}/material/${primerMaterialPendiente.id}`);
    }
  };

  const handleVerDetalle = () => {
    router.push(`/progreso/${cursoId}`);
  };

  if (loading) {
    return (
      <Card>
        <CardContent className="p-6">
          <div className="animate-pulse space-y-3">
            <div className="h-4 bg-gray-200 rounded w-3/4"></div>
            <div className="h-2 bg-gray-200 rounded"></div>
          </div>
        </CardContent>
      </Card>
    );
  }

  const porcentaje = progreso?.porcentajeCompletado || 0;
  const tareasCompletadas = progreso?.leccionesCompletadas || 0;

  return (
    <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={handleVerDetalle}>
      <CardHeader>
        <div className="flex items-start justify-between">
          <div className="flex-1">
            <CardTitle className="text-lg">{nombreCurso}</CardTitle>
            <CardDescription>Tu progreso en este curso</CardDescription>
          </div>
          <BookOpen className="w-5 h-5 text-blue-600" />
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div>
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm font-medium">Progreso</span>
            <span className="text-sm font-bold text-blue-600">{porcentaje}%</span>
          </div>
          <Progress value={porcentaje} className="h-2" />
        </div>

        <div className="grid grid-cols-2 gap-4 text-sm">
          <div className="flex items-center gap-2">
            <CheckCircle2 className="w-4 h-4 text-green-600" />
            <span>{tareasCompletadas} tareas</span>
          </div>
          <div className="flex items-center gap-2">
            <TrendingUp className="w-4 h-4 text-orange-600" />
            <span>Módulo {progreso?.moduloActualId || 1}</span>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-2">
          <Button
            size="sm"
            variant="outline"
            onClick={(e) => {
              e.stopPropagation();
              handleVerDetalle();
            }}
          >
            Ver Detalle
          </Button>
          {porcentaje < 100 && primerMaterialPendiente && (
            <Button
              size="sm"
              onClick={handleContinuarAprendiendo}
              className="bg-blue-600 hover:bg-blue-700"
            >
              Continuar
            </Button>
          )}
          {porcentaje >= 100 && (
            <Button
              size="sm"
              onClick={(e) => {
                e.stopPropagation();
                handleVerDetalle();
              }}
              className="bg-green-600 hover:bg-green-700"
            >
              ¡Completado!
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
