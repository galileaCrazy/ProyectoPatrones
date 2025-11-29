'use client';
import { useState, useEffect } from 'react';
import { API_URL } from '@/lib/api';
import Link from 'next/link';

interface DashboardData {
  tipo: string;
  plataforma: string;
  navegacion: string;
  cursos: string;
  perfil: string;
  resolucion: string;
  capacidades: string;
  secciones: string[];
}

export default function DemoBridgePage() {
  const [plataforma, setPlataforma] = useState<'web' | 'movil' | 'smarttv'>('web');
  const [tipoUsuario, setTipoUsuario] = useState<'estudiante' | 'profesor' | 'administrador'>('estudiante');
  const [dashboardData, setDashboardData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    cargarDashboard();
  }, [plataforma, tipoUsuario]);

  const cargarDashboard = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API_URL}/patrones/bridge/dashboard?tipoUsuario=${tipoUsuario}&plataforma=${plataforma}`);
      const data = await res.json();

      if (data.dashboard) {
        setDashboardData(data.dashboard);
      } else if (data.error) {
        setError(data.error);
      }
    } catch (err) {
      setError('Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-50 to-blue-100 p-6">
      {/* Header */}
      <div className="max-w-7xl mx-auto mb-6">
        <Link href="/" className="text-blue-600 hover:text-blue-800 mb-4 inline-block">
          ← Volver al Inicio
        </Link>
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">🌉 Patrón Bridge</h1>
          <p className="text-lg text-gray-600 mb-4">
            <strong>Interfaz idéntica</strong> en Web, Móvil y Smart TV - <strong>Implementación diferente</strong> por plataforma
          </p>
          <div className="bg-blue-50 border-l-4 border-blue-500 p-4 rounded">
            <p className="text-sm text-gray-700">
              <strong>Principio:</strong> El patrón Bridge separa la <span className="text-blue-700 font-bold">abstracción</span> (Dashboard)
              de su <span className="text-purple-700 font-bold">implementación</span> (Plataforma). La misma funcionalidad funciona en todos
              los dispositivos, pero se renderiza de manera óptima para cada uno.
            </p>
          </div>
        </div>
      </div>

      {/* Controles */}
      <div className="max-w-7xl mx-auto mb-6 grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Selección de Plataforma */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4">🖥️ Seleccionar Plataforma</h2>
          <div className="space-y-3">
            {[
              { value: 'web' as const, label: 'Web Browser', icon: '🌐', desc: '1920x1080 - Mouse & Keyboard', color: 'blue' },
              { value: 'movil' as const, label: 'Mobile App', icon: '📱', desc: '390x844 - Touch Gestures', color: 'green' },
              { value: 'smarttv' as const, label: 'Smart TV', icon: '📺', desc: '3840x2160 - Remote Control', color: 'purple' }
            ].map(plat => (
              <button
                key={plat.value}
                onClick={() => setPlataforma(plat.value)}
                className={`w-full p-4 rounded-lg border-2 transition-all text-left ${
                  plataforma === plat.value
                    ? `border-${plat.color}-500 bg-${plat.color}-50 shadow-md`
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                <div className="flex items-center gap-3">
                  <span className="text-3xl">{plat.icon}</span>
                  <div>
                    <div className="font-bold text-gray-800">{plat.label}</div>
                    <div className="text-sm text-gray-600">{plat.desc}</div>
                  </div>
                </div>
              </button>
            ))}
          </div>
        </div>

        {/* Selección de Tipo de Usuario */}
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h2 className="text-xl font-bold text-gray-800 mb-4">👤 Tipo de Usuario</h2>
          <div className="space-y-3">
            {[
              { value: 'estudiante' as const, label: 'Estudiante', icon: '🎓', desc: 'Ver cursos y progreso' },
              { value: 'profesor' as const, label: 'Profesor', icon: '👨‍🏫', desc: 'Gestionar cursos' },
              { value: 'administrador' as const, label: 'Administrador', icon: '⚙️', desc: 'Administrar sistema' }
            ].map(tipo => (
              <button
                key={tipo.value}
                onClick={() => setTipoUsuario(tipo.value)}
                className={`w-full p-4 rounded-lg border-2 transition-all text-left ${
                  tipoUsuario === tipo.value
                    ? 'border-orange-500 bg-orange-50 shadow-md'
                    : 'border-gray-200 hover:border-gray-300'
                }`}
              >
                <div className="flex items-center gap-3">
                  <span className="text-3xl">{tipo.icon}</span>
                  <div>
                    <div className="font-bold text-gray-800">{tipo.label}</div>
                    <div className="text-sm text-gray-600">{tipo.desc}</div>
                  </div>
                </div>
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Resultado */}
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">
            📊 Dashboard Renderizado: {dashboardData?.plataforma}
          </h2>

          {loading && (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
              <p className="mt-4 text-gray-600">Cargando...</p>
            </div>
          )}

          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}

          {dashboardData && !loading && (
            <div className="space-y-6">
              {/* Información de Plataforma */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-blue-50 p-4 rounded-lg">
                  <div className="text-sm font-bold text-blue-800 mb-1">Plataforma</div>
                  <div className="text-lg text-gray-800">{dashboardData.plataforma}</div>
                </div>
                <div className="bg-green-50 p-4 rounded-lg">
                  <div className="text-sm font-bold text-green-800 mb-1">Resolución</div>
                  <div className="text-lg text-gray-800">{dashboardData.resolucion}</div>
                </div>
                <div className="bg-purple-50 p-4 rounded-lg">
                  <div className="text-sm font-bold text-purple-800 mb-1">Tipo Dashboard</div>
                  <div className="text-lg text-gray-800">{dashboardData.tipo}</div>
                </div>
              </div>

              {/* Secciones Disponibles */}
              <div className="bg-gray-50 p-4 rounded-lg">
                <h3 className="font-bold text-gray-800 mb-3">📋 Secciones Disponibles (Idénticas en todas las plataformas)</h3>
                <div className="flex flex-wrap gap-2">
                  {dashboardData.secciones.map((seccion, idx) => (
                    <span key={idx} className="bg-white px-3 py-1 rounded-full text-sm border border-gray-300">
                      {seccion}
                    </span>
                  ))}
                </div>
              </div>

              {/* Renderizado de Componentes */}
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
                <div className="bg-gray-50 p-4 rounded-lg">
                  <h3 className="font-bold text-gray-800 mb-3">🧭 Navegación</h3>
                  <pre className="text-xs overflow-x-auto bg-white p-3 rounded border border-gray-300 max-h-64">
                    {dashboardData.navegacion}
                  </pre>
                </div>

                <div className="bg-gray-50 p-4 rounded-lg">
                  <h3 className="font-bold text-gray-800 mb-3">📚 Cursos</h3>
                  <pre className="text-xs overflow-x-auto bg-white p-3 rounded border border-gray-300 max-h-64">
                    {dashboardData.cursos}
                  </pre>
                </div>

                <div className="bg-gray-50 p-4 rounded-lg">
                  <h3 className="font-bold text-gray-800 mb-3">👤 Perfil</h3>
                  <pre className="text-xs overflow-x-auto bg-white p-3 rounded border border-gray-300 max-h-64">
                    {dashboardData.perfil}
                  </pre>
                </div>
              </div>

              {/* Capacidades de la Plataforma */}
              <div className="bg-yellow-50 p-4 rounded-lg border border-yellow-300">
                <h3 className="font-bold text-yellow-800 mb-2">⚡ Capacidades de la Plataforma</h3>
                <p className="text-sm text-gray-700">{dashboardData.capacidades}</p>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Explicación */}
      <div className="max-w-7xl mx-auto mt-6 bg-white rounded-lg shadow-lg p-6">
        <h2 className="text-2xl font-bold text-gray-800 mb-4">💡 Cómo funciona el Patrón Bridge</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h3 className="font-bold text-blue-700 mb-2">Abstracción (Dashboard)</h3>
            <ul className="list-disc list-inside space-y-1 text-sm text-gray-700">
              <li>DashboardEstudiante</li>
              <li>DashboardProfesor</li>
              <li>DashboardAdmin</li>
              <li><strong>Misma funcionalidad</strong> en todas las plataformas</li>
            </ul>
          </div>
          <div>
            <h3 className="font-bold text-purple-700 mb-2">Implementación (Plataforma)</h3>
            <ul className="list-disc list-inside space-y-1 text-sm text-gray-700">
              <li>PlataformaWeb</li>
              <li>PlataformaMovil</li>
              <li>PlataformaSmartTV</li>
              <li><strong>Renderizado diferente</strong> según dispositivo</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
