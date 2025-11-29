'use client';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { API_URL } from '@/lib/api';

interface Usuario {
  id: number;
  nombre: string;
  email: string;
  tipoUsuario: string;
}

interface Curso {
  id: number;
  nombre: string;
  descripcion: string;
  duracion: number;
  tipoCurso?: string;
}

interface Modulo {
  tipo: string;
  nombre: string;
  lecciones?: Leccion[];
}

interface Leccion {
  tipo: string;
  nombre: string;
  duracion: number;
}

interface EstructuraCurso {
  patron: string;
  tipo: string;
  nombre: string;
  modulos: Modulo[];
}

interface DashboardBridge {
  tipo: string;
  plataforma: string;
  navegacion: string;
  cursos: string;
  perfil: string;
  resolucion: string;
  capacidades: string;
  secciones: string[];
}

export default function DashboardEstudiante() {
  const router = useRouter();
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [activeTab, setActiveTab] = useState('cursos');
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [misCursos, setMisCursos] = useState<Curso[]>([]);

  // Estados para Composite
  const [cursoSeleccionado, setCursoSeleccionado] = useState<number | null>(null);
  const [estructuraComposite, setEstructuraComposite] = useState<EstructuraCurso | null>(null);
  const [moduloExpandido, setModuloExpandido] = useState<number | null>(null);

  // Estados para Bridge
  const [plataformaBridge, setPlataformaBridge] = useState<'web' | 'movil' | 'smarttv'>('web');
  const [dashboardBridge, setDashboardBridge] = useState<DashboardBridge | null>(null);

  const [mensaje, setMensaje] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const stored = localStorage.getItem('usuario');
    if (!stored) {
      router.push('/login');
      return;
    }
    const user = JSON.parse(stored);
    if (user.tipoUsuario !== 'estudiante') {
      router.push('/login');
      return;
    }
    setUsuario(user);
    cargarCursos();
    cargarMisCursos(user.id);
  }, [router]);

  const cargarCursos = async () => {
    try {
      const res = await fetch(`${API_URL}/cursos`);
      const data = await res.json();
      setCursos(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error('Error cargando cursos:', error);
      setCursos([]);
    }
  };

  const cargarMisCursos = async (estudianteId: number) => {
    try {
      const res = await fetch(`${API_URL}/inscripciones`);
      const data = await res.json();
      
      // Asegurar que sea un array antes de usar .filter()
      const inscripciones = Array.isArray(data) ? data : [];
      
      const misInscripciones = inscripciones.filter((i: { estudianteId: number }) => i.estudianteId === estudianteId);
      const cursosInscritos = misInscripciones.map((i: { cursoId: number; cursoNombre: string }) => ({
        id: i.cursoId,
        nombre: i.cursoNombre
      }));
      setMisCursos(cursosInscritos);
    } catch (error) {
      console.error('Error cargando mis cursos:', error);
      setMisCursos([]);
    }
  };

  // FACADE: Inscripción completa
  const inscribirse = async (cursoId: number) => {
    if (!usuario) return;
    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/patrones/facade/inscribir?estudianteId=${usuario.id}&cursoId=${cursoId}`, {
        method: 'POST'
      });
      const data = await res.json();
      if (data.exito) {
        setMensaje('✅ Inscripción exitosa usando patrón Facade');
        cargarMisCursos(usuario.id);
        setTimeout(() => setMensaje(''), 3000);
      } else {
        setMensaje(`❌ ${data.mensaje}`);
      }
    } catch (error) {
      setMensaje('❌ Error al inscribirse');
    } finally {
      setLoading(false);
    }
  };

  // COMPOSITE: Ver estructura jerárquica del curso
  const verEstructuraComposite = async (cursoId: number) => {
    setLoading(true);
    setCursoSeleccionado(cursoId);
    setModuloExpandido(null);

    try {
      const res = await fetch(`${API_URL}/patrones/composite/estructura-curso/${cursoId}`);
      const data = await res.json();
      setEstructuraComposite(data);
      setActiveTab('estructura');
    } catch {
      setMensaje('❌ Error al cargar estructura');
    } finally {
      setLoading(false);
    }
  };

  // BRIDGE: Cambiar vista por dispositivo
  const cargarDashboardBridge = async (plataforma: 'web' | 'movil' | 'smarttv') => {
    setLoading(true);
    setPlataformaBridge(plataforma);

    try {
      const res = await fetch(`${API_URL}/patrones/bridge/dashboard?tipoUsuario=estudiante&plataforma=${plataforma}`);
      const data = await res.json();

      if (data.dashboard) {
        setDashboardBridge(data.dashboard);
      } else if (data.error) {
        setMensaje(`❌ ${data.error}`);
      }
    } catch (error) {
      setMensaje('❌ Error al cargar dashboard');
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.clear();
    router.push('/login');
  };

  if (!usuario) return <div className="p-8">Cargando...</div>;

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Header */}
      <nav className="bg-blue-600 text-white p-4 flex justify-between items-center shadow-lg">
        <h1 className="text-2xl font-bold">Dashboard Estudiante</h1>
        <div className="flex items-center gap-4">
          <span className="font-medium">{usuario.nombre}</span>
          <button
            onClick={logout}
            className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded font-medium transition-colors"
          >
            Salir
          </button>
        </div>
      </nav>

      <div className="p-6 max-w-7xl mx-auto">
        {/* Mensajes */}
        {mensaje && (
          <div className={`mb-4 p-4 rounded-lg flex justify-between items-center ${
            mensaje.includes('✅') ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}>
            <span>{mensaje}</span>
            <button onClick={() => setMensaje('')} className="text-xl font-bold hover:opacity-70">×</button>
          </div>
        )}

        {/* Tabs */}
        <div className="mb-6 flex gap-2 overflow-x-auto pb-2">
          {[
            { id: 'cursos', label: '📚 Mis Cursos', color: 'blue' },
            { id: 'inscribirse', label: '➕ Inscribirse (Facade)', color: 'green' },
            { id: 'estructura', label: '🌳 Estructura (Composite)', color: 'purple' },
            { id: 'vista', label: '📱 Vista (Bridge)', color: 'indigo' },
            { id: 'flyweight', label: '⚡ Optimización (Flyweight)', color: 'pink' }
          ].map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-6 py-3 rounded-lg font-semibold whitespace-nowrap transition-all ${
                activeTab === tab.id
                  ? 'bg-blue-600 text-white shadow-md'
                  : 'bg-white text-gray-700 hover:bg-gray-100'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Contenido */}
        {activeTab === 'cursos' && (
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-bold mb-4 text-gray-800">Mis Cursos Inscritos</h2>
            {misCursos.length === 0 ? (
              <div className="text-center py-12">
                <div className="text-6xl mb-4">📚</div>
                <p className="text-gray-600 text-lg">No estás inscrito en ningún curso</p>
                <button
                  onClick={() => setActiveTab('inscribirse')}
                  className="mt-4 bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Inscribirse a un curso
                </button>
              </div>
            ) : (
              <div className="grid gap-4">
                {misCursos.map(curso => (
                  <div key={curso.id} className="p-5 border-2 border-gray-200 rounded-lg hover:border-blue-400 hover:shadow-md transition-all">
                    <div className="flex justify-between items-center">
                      <div>
                        <h3 className="text-lg font-bold text-gray-800">{curso.nombre}</h3>
                        <p className="text-sm text-gray-600 mt-1">ID del curso: #{curso.id}</p>
                      </div>
                      <button
                        onClick={() => verEstructuraComposite(curso.id)}
                        disabled={loading}
                        className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-lg font-medium transition-colors disabled:opacity-50"
                      >
                        🌳 Ver Estructura
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'inscribirse' && (
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-bold mb-2 text-gray-800">Inscribirse a Curso</h2>
            <div className="bg-green-50 border-l-4 border-green-500 p-4 mb-6 rounded">
              <p className="text-sm text-gray-700">
                <strong>Patrón Facade:</strong> Simplifica el proceso de inscripción en un solo paso, ocultando la complejidad de múltiples operaciones (validación, creación de registro, asignación de recursos, etc.)
              </p>
            </div>

            {cursos.length === 0 ? (
              <div className="text-center py-12">
                <div className="text-6xl mb-4">📚</div>
                <p className="text-gray-600 text-lg">No hay cursos disponibles</p>
              </div>
            ) : (
              <div className="grid gap-4">
                {cursos.map(curso => (
                  <div key={curso.id} className="p-5 border-2 border-gray-200 rounded-lg hover:shadow-md transition-all">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="text-lg font-bold text-gray-800 mb-1">{curso.nombre}</h3>
                        <p className="text-sm text-gray-600 mb-2">{curso.descripcion}</p>
                        <div className="flex gap-4 text-sm text-gray-500">
                          <span>⏱ {curso.duracion}h</span>
                          {curso.tipoCurso && <span>📱 {curso.tipoCurso}</span>}
                        </div>
                      </div>
                      <button
                        onClick={() => inscribirse(curso.id)}
                        disabled={loading || misCursos.some(c => c.id === curso.id)}
                        className={`px-6 py-3 rounded-lg font-medium transition-colors ${
                          misCursos.some(c => c.id === curso.id)
                            ? 'bg-gray-300 text-gray-600 cursor-not-allowed'
                            : 'bg-green-500 hover:bg-green-600 text-white'
                        }`}
                      >
                        {misCursos.some(c => c.id === curso.id) ? '✓ Inscrito' : '➕ Inscribirse'}
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'estructura' && (
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-bold mb-2 text-gray-800">Estructura del Curso</h2>
            <div className="bg-purple-50 border-l-4 border-purple-500 p-4 mb-6 rounded">
              <p className="text-sm text-gray-700">
                <strong>Patrón Composite:</strong> Organiza el curso en una estructura jerárquica de árbol. Los módulos pueden contener lecciones, permitiendo tratar elementos individuales y grupos de manera uniforme.
              </p>
            </div>

            {!estructuraComposite || loading ? (
              <div className="text-center py-12">
                {loading ? (
                  <>
                    <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600 mb-4"></div>
                    <p className="text-gray-600">Cargando estructura...</p>
                  </>
                ) : (
                  <>
                    <div className="text-6xl mb-4">🌳</div>
                    <p className="text-gray-600">Selecciona un curso para ver su estructura jerárquica</p>
                  </>
                )}
              </div>
            ) : (
              <div className="space-y-4">
                {/* Curso raíz */}
                <div className="bg-gradient-to-r from-purple-100 to-purple-50 p-5 rounded-lg border-2 border-purple-300">
                  <div className="flex items-center gap-3">
                    <span className="text-3xl">🎓</span>
                    <div>
                      <div className="text-xs text-purple-600 font-semibold">CURSO (RAÍZ)</div>
                      <div className="text-lg font-bold text-gray-800">{estructuraComposite.nombre}</div>
                      <div className="text-sm text-gray-600 mt-1">
                        {estructuraComposite.modulos.length} módulos • Patrón: {estructuraComposite.patron}
                      </div>
                    </div>
                  </div>
                </div>

                {/* Módulos */}
                <div className="ml-8 space-y-3">
                  {estructuraComposite.modulos.map((modulo, idx) => (
                    <div key={idx} className="border-l-4 border-blue-400">
                      <button
                        onClick={() => setModuloExpandido(moduloExpandido === idx ? null : idx)}
                        className="w-full text-left bg-blue-50 hover:bg-blue-100 p-4 rounded-r-lg transition-colors"
                      >
                        <div className="flex items-center gap-3">
                          <span className="text-2xl">{moduloExpandido === idx ? '📂' : '📁'}</span>
                          <div className="flex-1">
                            <div className="text-xs text-blue-600 font-semibold">MÓDULO (COMPUESTO)</div>
                            <div className="font-bold text-gray-800">{modulo.nombre}</div>
                            <div className="text-sm text-gray-600 mt-1">
                              {modulo.lecciones?.length || 0} lecciones
                            </div>
                          </div>
                          <span className="text-gray-400">{moduloExpandido === idx ? '▼' : '▶'}</span>
                        </div>
                      </button>

                      {/* Lecciones */}
                      {moduloExpandido === idx && modulo.lecciones && (
                        <div className="ml-8 mt-2 space-y-2">
                          {modulo.lecciones.map((leccion, lIdx) => (
                            <div key={lIdx} className="bg-green-50 p-3 rounded-lg border-l-4 border-green-400">
                              <div className="flex items-center gap-3">
                                <span className="text-xl">📄</span>
                                <div>
                                  <div className="text-xs text-green-600 font-semibold">LECCIÓN (HOJA)</div>
                                  <div className="font-medium text-gray-800">{leccion.nombre}</div>
                                  <div className="text-sm text-gray-600">⏱ {leccion.duracion} min</div>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  ))}
                </div>

                {/* Explicación */}
                <div className="mt-6 bg-gray-50 p-4 rounded-lg">
                  <h3 className="font-bold text-gray-800 mb-2">🌳 Estructura Jerárquica:</h3>
                  <ul className="text-sm text-gray-700 space-y-1 list-disc list-inside">
                    <li><strong>Curso</strong>: Elemento raíz que contiene módulos</li>
                    <li><strong>Módulos</strong>: Elementos compuestos que contienen lecciones</li>
                    <li><strong>Lecciones</strong>: Elementos hoja (no contienen más elementos)</li>
                    <li>Todos se tratan de manera uniforme mediante el patrón Composite</li>
                  </ul>
                </div>
              </div>
            )}
          </div>
        )}

        {activeTab === 'vista' && (
          <div className="bg-white p-6 rounded-lg shadow-lg">
            <h2 className="text-2xl font-bold mb-2 text-gray-800">Cambiar Vista por Dispositivo</h2>
            <div className="bg-indigo-50 border-l-4 border-indigo-500 p-4 mb-6 rounded">
              <p className="text-sm text-gray-700">
                <strong>Patrón Bridge:</strong> Separa la abstracción (Dashboard de Estudiante) de su implementación (Plataforma Web/Móvil/TV). La misma funcionalidad se renderiza de manera diferente según el dispositivo.
              </p>
            </div>

            {/* Selector de plataforma */}
            <div className="mb-6">
              <h3 className="font-bold text-gray-800 mb-3">Selecciona una plataforma:</h3>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {[
                  { id: 'web' as const, label: 'Web Browser', icon: '🌐', desc: '1920x1080 - Desktop', color: 'blue' },
                  { id: 'movil' as const, label: 'Mobile App', icon: '📱', desc: '390x844 - Touch', color: 'green' },
                  { id: 'smarttv' as const, label: 'Smart TV', icon: '📺', desc: '3840x2160 - 4K', color: 'purple' }
                ].map(plat => (
                  <button
                    key={plat.id}
                    onClick={() => cargarDashboardBridge(plat.id)}
                    disabled={loading}
                    className={`p-5 rounded-lg border-2 transition-all text-left ${
                      plataformaBridge === plat.id
                        ? 'border-blue-500 bg-blue-50 shadow-lg'
                        : 'border-gray-200 hover:border-gray-300 hover:shadow-md'
                    }`}
                  >
                    <div className="text-center">
                      <div className="text-4xl mb-2">{plat.icon}</div>
                      <div className="font-bold text-gray-800">{plat.label}</div>
                      <div className="text-sm text-gray-600 mt-1">{plat.desc}</div>
                    </div>
                  </button>
                ))}
              </div>
            </div>

            {/* Dashboard renderizado */}
            {loading ? (
              <div className="text-center py-12">
                <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mb-4"></div>
                <p className="text-gray-600">Cargando dashboard...</p>
              </div>
            ) : dashboardBridge ? (
              <div className="space-y-6">
                {/* Info de plataforma */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="bg-blue-50 p-4 rounded-lg">
                    <div className="text-xs font-bold text-blue-700 mb-1">PLATAFORMA</div>
                    <div className="text-lg font-semibold text-gray-800">{dashboardBridge.plataforma}</div>
                  </div>
                  <div className="bg-green-50 p-4 rounded-lg">
                    <div className="text-xs font-bold text-green-700 mb-1">RESOLUCIÓN</div>
                    <div className="text-lg font-semibold text-gray-800">{dashboardBridge.resolucion}</div>
                  </div>
                  <div className="bg-purple-50 p-4 rounded-lg">
                    <div className="text-xs font-bold text-purple-700 mb-1">DASHBOARD</div>
                    <div className="text-lg font-semibold text-gray-800">{dashboardBridge.tipo}</div>
                  </div>
                </div>

                {/* Secciones */}
                <div className="bg-gray-50 p-4 rounded-lg">
                  <h3 className="font-bold text-gray-800 mb-3">📋 Secciones (Idénticas en todas las plataformas):</h3>
                  <div className="flex flex-wrap gap-2">
                    {dashboardBridge.secciones.map((seccion, idx) => (
                      <span key={idx} className="bg-white px-3 py-1 rounded-full text-sm border border-gray-300 font-medium">
                        {seccion}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Renderizados */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <h3 className="font-bold text-gray-800 mb-3 flex items-center gap-2">
                      <span>🧭</span> Navegación
                    </h3>
                    <pre className="text-xs overflow-x-auto bg-white p-3 rounded border border-gray-300 max-h-64 text-gray-700">
                      {dashboardBridge.navegacion}
                    </pre>
                  </div>

                  <div className="bg-gray-50 p-4 rounded-lg">
                    <h3 className="font-bold text-gray-800 mb-3 flex items-center gap-2">
                      <span>📚</span> Cursos
                    </h3>
                    <pre className="text-xs overflow-x-auto bg-white p-3 rounded border border-gray-300 max-h-64 text-gray-700">
                      {dashboardBridge.cursos}
                    </pre>
                  </div>

                  <div className="bg-gray-50 p-4 rounded-lg">
                    <h3 className="font-bold text-gray-800 mb-3 flex items-center gap-2">
                      <span>👤</span> Perfil
                    </h3>
                    <pre className="text-xs overflow-x-auto bg-white p-3 rounded border border-gray-300 max-h-64 text-gray-700">
                      {dashboardBridge.perfil}
                    </pre>
                  </div>
                </div>

                {/* Capacidades */}
                <div className="bg-yellow-50 p-4 rounded-lg border border-yellow-300">
                  <h3 className="font-bold text-yellow-800 mb-2">⚡ Capacidades de la Plataforma:</h3>
                  <p className="text-sm text-gray-700">{dashboardBridge.capacidades}</p>
                </div>
              </div>
            ) : (
              <div className="text-center py-12">
                <div className="text-6xl mb-4">📱</div>
                <p className="text-gray-600">Selecciona una plataforma para ver el dashboard renderizado</p>
              </div>
            )}
          </div>
        )}

        {/* Tab Flyweight */}
        {activeTab === 'flyweight' && (
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-4 flex items-center gap-2">
              <span>⚡</span> Patrón Flyweight - Optimización de Memoria
            </h2>

            <div className="bg-purple-50 border-l-4 border-purple-500 p-4 rounded mb-6">
              <p className="text-gray-700">
                <strong>¿Qué hace?</strong> El patrón Flyweight optimiza el uso de memoria compartiendo
                recursos visuales (iconos, colores, plantillas) entre múltiples cursos del mismo tipo.
              </p>
              <p className="text-gray-700 mt-2">
                En lugar de crear 16 objetos completos para 16 cursos, el sistema crea solo 3 objetos
                Flyweight (uno por tipo: Virtual, Presencial, Híbrido) y los reutiliza.
              </p>
            </div>

            <div className="text-center py-12">
              <div className="text-6xl mb-4">⚡</div>
              <h3 className="text-xl font-bold text-gray-800 mb-4">
                Patrón Flyweight
              </h3>
              <p className="text-gray-600 mb-6 max-w-2xl mx-auto">
                Visualiza cómo el patrón Flyweight optimiza la memoria compartiendo recursos entre cursos
                del mismo tipo. Verás estadísticas en tiempo real de cuánta memoria se ahorra.
              </p>

              <Link
                href="/demo-flyweight"
                className="inline-block bg-purple-600 hover:bg-purple-700 text-white font-bold py-3 px-8 rounded-lg transition-colors shadow-lg"
              >
                Ver patron →
              </Link>

              <div className="mt-8 grid grid-cols-1 md:grid-cols-3 gap-4 max-w-3xl mx-auto">
                <div className="bg-blue-50 p-4 rounded-lg border-2 border-blue-300">
                  <div className="text-3xl font-bold text-blue-700">3</div>
                  <div className="text-sm text-gray-700 mt-1">Objetos Flyweight</div>
                  <div className="text-xs text-gray-600">Virtual, Presencial, Híbrido</div>
                </div>

                <div className="bg-green-50 p-4 rounded-lg border-2 border-green-300">
                  <div className="text-3xl font-bold text-green-700">16</div>
                  <div className="text-sm text-gray-700 mt-1">Cursos Renderizados</div>
                  <div className="text-xs text-gray-600">Con recursos compartidos</div>
                </div>

                <div className="bg-purple-50 p-4 rounded-lg border-2 border-purple-300">
                  <div className="text-3xl font-bold text-purple-700">81%</div>
                  <div className="text-sm text-gray-700 mt-1">Memoria Ahorrada</div>
                  <div className="text-xs text-gray-600">13 objetos no creados</div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
