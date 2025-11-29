'use client';
import { useState, useEffect } from 'react';
import { API_URL } from '@/lib/api';
import Link from 'next/link';

interface RecursoInfo {
  tipo: string;
  icono: string;
  colorPrimario: string;
  colorSecundario: string;
  plantilla: string;
  esCompartido: boolean;
  vecesReutilizado: number;
}

interface CursoRenderizado {
  cursoId: number;
  nombre: string;
  tipo: string;
  html: string;
  recursoCompartido: RecursoInfo;
}

interface EstadisticasFlyweight {
  totalRecursosEnPool: number;
  tiposDisponibles: string[];
  usosPorTipo: { [key: string]: number };
  totalRenderizados: number;
  objetosCreados: number;
  memoriaAhorrada: string;
}

export default function DemoFlyweightPage() {
  const [cursos, setCursos] = useState<CursoRenderizado[]>([]);
  const [estadisticas, setEstadisticas] = useState<EstadisticasFlyweight | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [vistaActual, setVistaActual] = useState<'cursos' | 'estadisticas'>('cursos');

  useEffect(() => {
    cargarCursos();
    cargarEstadisticas();
  }, []);

  const cargarCursos = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API_URL}/patrones/flyweight/renderizar-cursos`);
      const data = await res.json();

      if (data.cursos) {
        setCursos(data.cursos);
      } else if (data.error) {
        setError(data.error);
      }
    } catch (err) {
      setError('Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  const cargarEstadisticas = async () => {
    try {
      const res = await fetch(`${API_URL}/patrones/flyweight/estadisticas`);
      const data = await res.json();

      if (data.estadisticas) {
        setEstadisticas(data.estadisticas);
      }
    } catch (err) {
      console.error('Error al cargar estadísticas:', err);
    }
  };

  const getColorClass = (tipo: string) => {
    switch (tipo.toLowerCase()) {
      case 'virtual':
        return 'bg-blue-50 border-blue-300';
      case 'presencial':
        return 'bg-green-50 border-green-300';
      case 'hibrido':
      case 'híbrido':
        return 'bg-purple-50 border-purple-300';
      default:
        return 'bg-gray-50 border-gray-300';
    }
  };

  const getIconoTipo = (tipo: string) => {
    switch (tipo.toLowerCase()) {
      case 'virtual':
        return '💻';
      case 'presencial':
        return '🏫';
      case 'hibrido':
      case 'híbrido':
        return '🔄';
      default:
        return '📚';
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-pink-100 p-6">
      {/* Header */}
      <div className="max-w-7xl mx-auto mb-6">
        <Link href="/" className="text-purple-600 hover:text-purple-800 mb-4 inline-block">
          ← Volver al Inicio
        </Link>
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">Patrón Flyweight</h1>
          <p className="text-lg text-gray-600 mb-4">
            <strong>Optimización de memoria</strong> compartiendo objetos inmutables entre múltiples contextos
          </p>
          <div className="bg-purple-50 border-l-4 border-purple-500 p-4 rounded">
            <p className="text-sm text-gray-700">
              <strong>Principio:</strong> El patrón Flyweight reduce el uso de memoria compartiendo el{' '}
              <span className="text-purple-700 font-bold">estado intrínseco</span> (inmutable: iconos, colores, plantillas)
              entre múltiples objetos, mientras que el{' '}
              <span className="text-pink-700 font-bold">estado extrínseco</span> (variable: datos del curso) se pasa como contexto.
            </p>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="max-w-7xl mx-auto mb-6">
        <div className="flex gap-4">
          <button
            onClick={() => setVistaActual('cursos')}
            className={`px-6 py-3 rounded-lg font-bold transition-all ${
              vistaActual === 'cursos'
                ? 'bg-purple-600 text-white shadow-lg'
                : 'bg-white text-gray-600 hover:bg-gray-50'
            }`}
          >
            Cursos Renderizados
          </button>
          <button
            onClick={() => setVistaActual('estadisticas')}
            className={`px-6 py-3 rounded-lg font-bold transition-all ${
              vistaActual === 'estadisticas'
                ? 'bg-purple-600 text-white shadow-lg'
                : 'bg-white text-gray-600 hover:bg-gray-50'
            }`}
          >
            Estadísticas del Pool
          </button>
        </div>
      </div>

      {/* Content */}
      {vistaActual === 'cursos' && (
        <div className="max-w-7xl mx-auto">
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4">
              Cursos Renderizados con Flyweight
            </h2>

            {loading && (
              <div className="text-center py-12">
                <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
                <p className="mt-4 text-gray-600">Cargando cursos...</p>
              </div>
            )}

            {error && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                {error}
              </div>
            )}

            {cursos.length > 0 && !loading && (
              <>
                <div className="bg-yellow-100 border-l-4 border-yellow-500 p-4 rounded mb-6">
                  <p className="text-sm font-bold text-yellow-800">
                    múltiples cursos del mismo tipo comparten el mismo objeto Flyweight
                  </p>
                  <p className="text-xs text-gray-700 mt-1">
                    Los iconos, colores y plantillas son inmutables y se reutilizan. Solo los datos específicos del curso cambian.
                  </p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {cursos.map((curso) => (
                    <div
                      key={curso.cursoId}
                      className={`border-2 rounded-lg p-4 ${getColorClass(curso.tipo)}`}
                    >
                      {/* Header del curso */}
                      <div className="flex items-center gap-3 mb-3">
                        <span className="text-3xl">{getIconoTipo(curso.tipo)}</span>
                        <div>
                          <h3 className="font-bold text-gray-800 text-sm">{curso.nombre}</h3>
                          <span className="text-xs text-gray-600 uppercase font-bold">
                            {curso.tipo}
                          </span>
                        </div>
                      </div>

                      {/* HTML Renderizado */}
                      <div
                        className="bg-white p-3 rounded border border-gray-300 text-xs mb-3 max-h-48 overflow-y-auto"
                        dangerouslySetInnerHTML={{ __html: curso.html }}
                      />

                      {/* Info del Recurso Compartido */}
                      <div className="bg-white p-3 rounded border border-gray-300">
                        <div className="flex items-center justify-between mb-2">
                          <span className="text-xs font-bold text-gray-700">
                             Recurso Compartido
                          </span>
                          <span className="text-xs bg-green-100 text-green-800 px-2 py-1 rounded-full">
                            Reutilizado {curso.recursoCompartido.vecesReutilizado}x
                          </span>
                        </div>
                        <div className="text-xs text-gray-600 space-y-1">
                          <div>Plantilla: {curso.recursoCompartido.plantilla}</div>
                          <div className="flex items-center gap-2">
                            <span>Colores:</span>
                            <div
                              className="w-4 h-4 rounded"
                              style={{ backgroundColor: curso.recursoCompartido.colorPrimario }}
                            ></div>
                            <div
                              className="w-4 h-4 rounded"
                              style={{ backgroundColor: curso.recursoCompartido.colorSecundario }}
                            ></div>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </>
            )}
          </div>
        </div>
      )}

      {vistaActual === 'estadisticas' && estadisticas && (
        <div className="max-w-7xl mx-auto">
          <div className="bg-white rounded-lg shadow-lg p-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">
               Estadísticas del Pool de Flyweights
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
              <div className="bg-gradient-to-br from-blue-50 to-blue-100 p-6 rounded-lg border-2 border-blue-300">
                <div className="text-3xl font-bold text-blue-700">
                  {estadisticas.objetosCreados}
                </div>
                <div className="text-sm text-gray-700 mt-2">Objetos Flyweight Creados</div>
                <div className="text-xs text-gray-600 mt-1">En el pool de memoria</div>
              </div>

              <div className="bg-gradient-to-br from-green-50 to-green-100 p-6 rounded-lg border-2 border-green-300">
                <div className="text-3xl font-bold text-green-700">
                  {estadisticas.totalRenderizados}
                </div>
                <div className="text-sm text-gray-700 mt-2">Total de Renderizados</div>
                <div className="text-xs text-gray-600 mt-1">Cursos procesados</div>
              </div>

              <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-6 rounded-lg border-2 border-purple-300">
                <div className="text-3xl font-bold text-purple-700">
                  {estadisticas.memoriaAhorrada}
                </div>
                <div className="text-sm text-gray-700 mt-2">Memoria Ahorrada</div>
                <div className="text-xs text-gray-600 mt-1">Objetos no creados</div>
              </div>
            </div>

            {/* Tipos disponibles */}
            <div className="bg-gray-50 p-6 rounded-lg border border-gray-300 mb-6">
              <h3 className="font-bold text-gray-800 mb-3">Tipos de Recursos en Pool</h3>
              <div className="flex flex-wrap gap-3">
                {estadisticas.tiposDisponibles.map((tipo, idx) => (
                  <div
                    key={idx}
                    className={`px-4 py-2 rounded-lg border-2 ${getColorClass(tipo)}`}
                  >
                    <span className="text-2xl mr-2">{getIconoTipo(tipo)}</span>
                    <span className="font-bold text-gray-800 uppercase text-sm">{tipo}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Usos por tipo */}
            <div className="bg-gradient-to-br from-yellow-50 to-orange-50 p-6 rounded-lg border-2 border-yellow-300">
              <h3 className="font-bold text-gray-800 mb-4">Reutilización por Tipo</h3>
              <div className="space-y-3">
                {Object.entries(estadisticas.usosPorTipo).map(([tipo, usos]) => (
                  <div key={tipo} className="flex items-center gap-4">
                    <span className="text-2xl">{getIconoTipo(tipo)}</span>
                    <div className="flex-1">
                      <div className="flex justify-between items-center mb-1">
                        <span className="font-bold text-gray-700 uppercase text-sm">{tipo}</span>
                        <span className="text-sm font-bold text-gray-600">{usos} usos</span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2">
                        <div
                          className="bg-gradient-to-r from-green-400 to-green-600 h-2 rounded-full transition-all"
                          style={{ width: `${(usos / estadisticas.totalRenderizados) * 100}%` }}
                        ></div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Explicación */}
      <div className="max-w-7xl mx-auto mt-6 bg-white rounded-lg shadow-lg p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">Cómo funciona el Patrón Flyweight</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-purple-50 p-4 rounded-lg border-2 border-purple-300">
            <h3 className="font-bold text-purple-700 mb-2">Estado Intrínseco (Compartido)</h3>
            <ul className="list-disc list-inside space-y-1 text-sm text-gray-700">
              <li>Iconos: 💻 🏫 🔄</li>
              <li>Colores: Azul, Verde, Morado</li>
              <li>Plantillas: VIRTUAL, PRESENCIAL, HÍBRIDO</li>
              <li><strong>Inmutable y compartido</strong> entre todos los cursos del mismo tipo</li>
              <li>Se almacena UNA sola vez en memoria</li>
            </ul>
          </div>
          <div className="bg-pink-50 p-4 rounded-lg border-2 border-pink-300">
            <h3 className="font-bold text-pink-700 mb-2">Estado Extrínseco (Contexto)</h3>
            <ul className="list-disc list-inside space-y-1 text-sm text-gray-700">
              <li>ID del curso</li>
              <li>Nombre del curso</li>
              <li>Descripción</li>
              <li>Duración y profesor</li>
              <li><strong>Único para cada curso</strong></li>
              <li>Se pasa como parámetro al renderizar</li>
            </ul>
          </div>
        </div>

        <div className="mt-6 bg-green-50 p-4 rounded-lg border-2 border-green-300">
          <h3 className="font-bold text-green-700 mb-2">✅ Beneficio del Patrón</h3>
          <p className="text-sm text-gray-700">
            <strong>Sin Flyweight:</strong> Si tienes 100 cursos virtuales, se crearían 100 objetos con iconos, colores y plantillas duplicadas = Alto uso de memoria
          </p>
          <p className="text-sm text-gray-700 mt-2">
            <strong>Con Flyweight:</strong> Solo se crea 1 objeto con los recursos visuales, compartido por los 100 cursos = Memoria optimizada
          </p>
        </div>
      </div>
    </div>
  );
}
