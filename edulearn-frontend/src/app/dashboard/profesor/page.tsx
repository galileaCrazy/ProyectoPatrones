'use client';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { API_URL } from '@/lib/api';

interface Usuario {
  id: number;
  nombre: string;
  email: string;
  tipoUsuario: string;
}

interface Curso {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  tipoCurso: string;
  duracion: number;
  estado: string;
}

export default function DashboardProfesor() {
  const router = useRouter();
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [activeTab, setActiveTab] = useState('mis-cursos');
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [mensaje, setMensaje] = useState('');
  const [loading, setLoading] = useState(false);

  // Estados para crear curso
  const [showCrearModal, setShowCrearModal] = useState(false);
  const [tipoCursoNuevo, setTipoCursoNuevo] = useState<'virtual' | 'presencial' | 'hibrido'>('virtual');
  const [nombreCurso, setNombreCurso] = useState('');
  const [descripcionCurso, setDescripcionCurso] = useState('');

  // Estados para decorar curso
  const [showDecorarModal, setShowDecorarModal] = useState(false);
  const [cursoADecorar, setCursoADecorar] = useState<number | null>(null);

  useEffect(() => {
    const stored = localStorage.getItem('usuario');
    if (!stored) {
      router.push('/login');
      return;
    }
    const user = JSON.parse(stored);
    if (user.tipoUsuario !== 'profesor') {
      router.push('/login');
      return;
    }
    setUsuario(user);
    cargarCursos();
  }, [router]);

  const cargarCursos = async () => {
    try {
      const res = await fetch(`${API_URL}/cursos`);
      const data = await res.json();
      setCursos(Array.isArray(data) ? data : []);
    } catch {
      setCursos([]);
    }
  };

  // Crear curso usando Abstract Factory
  const crearCursoPorTipo = async () => {
    if (!nombreCurso.trim()) {
      setMensaje('❌ Ingresa un nombre para el curso');
      return;
    }

    setLoading(true);
    try {
      const res = await fetch(`${API_URL}/patrones/abstract-factory/crear-curso?tipo=${tipoCursoNuevo}`, {
        method: 'POST'
      });
      const data = await res.json();

      if (data.curso) {
        setMensaje(`✅ Curso ${tipoCursoNuevo} creado exitosamente usando patrón Abstract Factory`);
        cargarCursos();
        setShowCrearModal(false);
        setNombreCurso('');
        setDescripcionCurso('');
      } else {
        setMensaje('❌ Error al crear curso');
      }
    } catch {
      setMensaje('❌ Error de conexión');
    } finally {
      setLoading(false);
    }
  };

  // Clonar curso usando Prototype
  const clonarCurso = async (cursoId: number) => {
    if (confirm('¿Deseas clonar este curso?')) {
      setLoading(true);
      setMensaje('');
      try {
        const res = await fetch(`${API_URL}/patrones/prototype/clonar-curso/${cursoId}`, {
          method: 'POST'
        });
        const data = await res.json();

        if (data.clon) {
          setMensaje('✅ Curso clonado exitosamente usando patrón Prototype');
          cargarCursos();
        } else if (data.error) {
          setMensaje('❌ ' + data.error);
        } else {
          setMensaje('❌ Error al clonar curso');
        }
      } catch (error) {
        setMensaje('❌ Error de conexión con el servidor');
        console.error('Error al clonar:', error);
      } finally {
        setLoading(false);
      }
    }
  };

  // Abrir modal para decorar curso
  const abrirDecorarModal = (cursoId: number) => {
    setCursoADecorar(cursoId);
    setShowDecorarModal(true);
  };

  // Decorar curso
  const decorarCurso = async (tipo: string) => {
    if (!cursoADecorar) return;

    setLoading(true);
    setMensaje('');
    setShowDecorarModal(false);

    try {
      const res = await fetch(`${API_URL}/patrones/decorator/decorar-curso/${cursoADecorar}?decorador=${tipo}`, {
        method: 'POST'
      });
      const data = await res.json();

      if (data.caracteristicas) {
        setMensaje(`✅ Funcionalidad "${tipo}" agregada usando patrón Decorator`);
        cargarCursos();
      } else if (data.error) {
        setMensaje('❌ ' + data.error);
      } else {
        setMensaje('❌ Error al decorar curso');
      }
    } catch (error) {
      setMensaje('❌ Error de conexión con el servidor');
      console.error('Error al decorar:', error);
    } finally {
      setLoading(false);
      setCursoADecorar(null);
    }
  };

  const logout = () => {
    localStorage.clear();
    router.push('/login');
  };

  if (!usuario) return <div className="p-8">Cargando...</div>;

  const getTipoCursoBadge = (tipo: string) => {
    const tipos: { [key: string]: { bg: string; text: string; icon: string } } = {
      virtual: { bg: 'bg-blue-100', text: 'text-blue-800', icon: '💻' },
      presencial: { bg: 'bg-green-100', text: 'text-green-800', icon: '🏫' },
      hibrido: { bg: 'bg-yellow-100', text: 'text-yellow-800', icon: '🔄' }
    };
    const t = tipos[tipo?.toLowerCase()] || tipos.presencial;
    return (
      <span className={`${t.bg} ${t.text} px-3 py-1 rounded-full text-sm font-medium`}>
        {t.icon} {tipo || 'Presencial'}
      </span>
    );
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <nav className="bg-gradient-to-r from-green-600 to-green-700 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold">Dashboard Profesor</h1>
            <p className="text-green-100 text-sm mt-1">Gestión de cursos y patrones de diseño</p>
          </div>
          <div className="flex items-center gap-4">
            <div className="text-right">
              <p className="font-semibold">{usuario.nombre}</p>
              <p className="text-sm text-green-100">{usuario.email}</p>
            </div>
            <button
              onClick={logout}
              className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg font-medium transition-colors"
            >
              Salir
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 py-6">
        {/* Mensaje */}
        {mensaje && (
          <div className={`mb-6 p-4 rounded-lg ${
            mensaje.includes('✅') ? 'bg-green-50 border border-green-200 text-green-800' :
            'bg-red-50 border border-red-200 text-red-800'
          }`}>
            <div className="flex justify-between items-center">
              <span>{mensaje}</span>
              <button onClick={() => setMensaje('')} className="text-lg font-bold ml-4">×</button>
            </div>
          </div>
        )}

        {/* Tabs */}
        <div className="mb-6 flex gap-2 overflow-x-auto pb-2">
          {[
            { id: 'mis-cursos', label: ' Mis Cursos', icon: '📚' },
            { id: 'crear', label: ' Crear Curso', icon: '➕' },
            { id: 'patrones', label: ' Patrones', icon: '🎯' }
          ].map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-6 py-3 rounded-lg font-semibold whitespace-nowrap transition-all ${
                activeTab === tab.id
                  ? 'bg-green-600 text-white shadow-md'
                  : 'bg-white text-gray-700 hover:bg-gray-100'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Contenido */}
        {activeTab === 'mis-cursos' && (
          <div className="bg-white rounded-lg shadow-md p-6">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-2xl font-bold text-gray-800">Mis Cursos</h2>
              <button
                onClick={() => setActiveTab('crear')}
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg font-medium transition-colors"
              >
                + Nuevo Curso
              </button>
            </div>

            {cursos.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <div className="text-6xl mb-4"></div>
                <p className="text-lg font-medium">No tienes cursos aún</p>
                <p className="text-sm mt-2">Crea un curso usando los patrones de diseño</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {cursos.map(curso => (
                  <div key={curso.id} className="border border-gray-200 rounded-lg p-5 hover:shadow-lg transition-shadow bg-white">
                    <div className="flex justify-between items-start mb-3">
                      {getTipoCursoBadge(curso.tipoCurso)}
                      <span className="text-xs text-gray-500">#{curso.codigo}</span>
                    </div>

                    <h3 className="text-lg font-bold text-gray-800 mb-2">{curso.nombre}</h3>
                    <p className="text-sm text-gray-600 mb-4 line-clamp-2">{curso.descripcion}</p>

                    <div className="flex items-center gap-3 text-sm text-gray-600 mb-4">
                      <span>⏱ {curso.duracion || 40}h</span>
                      <span>•</span>
                      <span className={`font-medium ${
                        curso.estado === 'activo' ? 'text-green-600' : 'text-gray-500'
                      }`}>
                        {curso.estado || 'activo'}
                      </span>
                    </div>

                    <div className="flex gap-2">
                      <button
                        onClick={() => clonarCurso(curso.id)}
                        disabled={loading}
                        className="flex-1 bg-blue-50 hover:bg-blue-100 text-blue-700 py-2 rounded-lg text-sm font-medium transition-colors disabled:opacity-50"
                      >
                         Clonar
                      </button>
                      <button
                        onClick={() => abrirDecorarModal(curso.id)}
                        disabled={loading}
                        className="flex-1 bg-purple-50 hover:bg-purple-100 text-purple-700 py-2 rounded-lg text-sm font-medium transition-colors disabled:opacity-50"
                      >
                        Mejorar
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'crear' && (
          <div className="bg-white rounded-lg shadow-md p-8">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Crear Nuevo Curso</h2>
            <p className="text-gray-600 mb-6">Utiliza el patrón <strong>Abstract Factory</strong> para crear cursos con configuraciones predefinidas</p>

            <div className="max-w-2xl">
              {/* Selección de tipo */}
              <div className="mb-6">
                <label className="block text-sm font-bold text-gray-700 mb-3">Tipo de Curso</label>
                <div className="grid grid-cols-3 gap-4">
                  {[
                    { value: 'virtual' as const, label: 'Virtual', desc: 'Hasta 100 estudiantes', icon: '💻', color: 'blue' },
                    { value: 'presencial' as const, label: 'Presencial', desc: 'Hasta 30 estudiantes', icon: '🏫', color: 'green' },
                    { value: 'hibrido' as const, label: 'Híbrido', desc: 'Modalidad combinada', icon: '🔄', color: 'yellow' }
                  ].map(tipo => (
                    <button
                      key={tipo.value}
                      onClick={() => setTipoCursoNuevo(tipo.value)}
                      className={`p-4 border-2 rounded-lg text-left transition-all ${
                        tipoCursoNuevo === tipo.value
                          ? `border-${tipo.color}-500 bg-${tipo.color}-50`
                          : 'border-gray-200 hover:border-gray-300'
                      }`}
                    >
                      <div className="text-3xl mb-2">{tipo.icon}</div>
                      <div className="font-bold text-gray-800">{tipo.label}</div>
                      <div className="text-xs text-gray-600 mt-1">{tipo.desc}</div>
                    </button>
                  ))}
                </div>
              </div>

              {/* Formulario básico */}
              <div className="space-y-4 mb-6">
                <div>
                  <label className="block text-sm font-bold text-gray-700 mb-2">Nombre del Curso</label>
                  <input
                    type="text"
                    value={nombreCurso}
                    onChange={e => setNombreCurso(e.target.value)}
                    placeholder="Ej: Programación Web Avanzada"
                    className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-gray-800"
                  />
                </div>

                <div>
                  <label className="block text-sm font-bold text-gray-700 mb-2">Descripción (Opcional)</label>
                  <textarea
                    value={descripcionCurso}
                    onChange={e => setDescripcionCurso(e.target.value)}
                    placeholder="Describe brevemente el contenido del curso..."
                    rows={3}
                    className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent text-gray-800"
                  />
                </div>
              </div>

              <button
                onClick={crearCursoPorTipo}
                disabled={loading || !nombreCurso.trim()}
                className="w-full bg-green-600 hover:bg-green-700 text-white py-4 rounded-lg font-bold text-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Creando...' : '✓ Crear Curso con Abstract Factory'}
              </button>

              <p className="text-xs text-gray-500 mt-3 text-center">
                Este patrón crea automáticamente: curso + material + evaluación según el tipo seleccionado
              </p>
            </div>
          </div>
        )}

        {activeTab === 'patrones' && (
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Patrones de Diseño Disponibles</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                <div className="text-4xl mb-3"></div>
                <h3 className="text-xl font-bold text-gray-800 mb-2">Abstract Factory</h3>
                <p className="text-gray-600 mb-4">Crea familias de objetos relacionados (curso, material, evaluación) según el tipo.</p>
                <div className="bg-blue-50 p-3 rounded text-sm text-blue-800">
                  <strong>Uso:</strong> Crear cursos virtuales, presenciales o híbridos
                </div>
              </div>

              <div className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                <div className="text-4xl mb-3"></div>
                <h3 className="text-xl font-bold text-gray-800 mb-2">Prototype</h3>
                <p className="text-gray-600 mb-4">Clona cursos existentes para crear versiones similares rápidamente.</p>
                <div className="bg-green-50 p-3 rounded text-sm text-green-800">
                  <strong>Uso:</strong> Botón "Clonar" en cada curso
                </div>
              </div>

              <div className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                <div className="text-4xl mb-3"></div>
                <h3 className="text-xl font-bold text-gray-800 mb-2">Decorator</h3>
                <p className="text-gray-600 mb-4">Agrega funcionalidades adicionales a cursos existentes (gamificación, certificación).</p>
                <div className="bg-purple-50 p-3 rounded text-sm text-purple-800">
                  <strong>Uso:</strong> Botón "Mejorar" en cada curso
                </div>
              </div>

              <div className="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow">
                <div className="text-4xl mb-3"></div>
                <h3 className="text-xl font-bold text-gray-800 mb-2">Adapter</h3>
                <p className="text-gray-600 mb-4">Integra plataformas de videoconferencia (Zoom, Teams, Meet) con el sistema.</p>
                <div className="bg-yellow-50 p-3 rounded text-sm text-yellow-800">
                  <strong>Uso:</strong> Configuración de clases virtuales
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Modal para Decorar Curso */}
      {showDecorarModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
            <h3 className="text-2xl font-bold text-gray-800 mb-4">Mejorar Curso</h3>
            <p className="text-gray-600 mb-6">Selecciona qué funcionalidad deseas agregar usando el patrón <strong>Decorator</strong>:</p>

            <div className="space-y-3">
              <button
                onClick={() => decorarCurso('certificado')}
                disabled={loading}
                className="w-full bg-gradient-to-r from-yellow-50 to-yellow-100 border-2 border-yellow-300 hover:border-yellow-400 text-left p-4 rounded-lg transition-all disabled:opacity-50"
              >
                <div className="flex items-center gap-3">
                  <span className="text-3xl"></span>
                  <div>
                    <div className="font-bold text-gray-800">Certificado de Finalización</div>
                    <div className="text-sm text-gray-600">Los estudiantes recibirán un certificado al completar</div>
                  </div>
                </div>
              </button>

              <button
                onClick={() => decorarCurso('tutoria')}
                disabled={loading}
                className="w-full bg-gradient-to-r from-blue-50 to-blue-100 border-2 border-blue-300 hover:border-blue-400 text-left p-4 rounded-lg transition-all disabled:opacity-50"
              >
                <div className="flex items-center gap-3">
                  <span className="text-3xl"></span>
                  <div>
                    <div className="font-bold text-gray-800">Sesiones de Tutoría</div>
                    <div className="text-sm text-gray-600">Agrega sesiones de tutoría personalizadas 1-a-1</div>
                  </div>
                </div>
              </button>

              <button
                onClick={() => decorarCurso('proyecto')}
                disabled={loading}
                className="w-full bg-gradient-to-r from-purple-50 to-purple-100 border-2 border-purple-300 hover:border-purple-400 text-left p-4 rounded-lg transition-all disabled:opacity-50"
              >
                <div className="flex items-center gap-3">
                  <span className="text-3xl"></span>
                  <div>
                    <div className="font-bold text-gray-800">Proyecto Práctico Final</div>
                    <div className="text-sm text-gray-600">Los estudiantes desarrollarán un proyecto real</div>
                  </div>
                </div>
              </button>
            </div>

            <button
              onClick={() => {
                setShowDecorarModal(false);
                setCursoADecorar(null);
              }}
              className="w-full mt-6 bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 rounded-lg font-medium transition-colors"
            >
              Cancelar
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
