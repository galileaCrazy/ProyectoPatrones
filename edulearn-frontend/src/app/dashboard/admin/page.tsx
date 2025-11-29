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
  nombre: string;
  descripcion: string;
  duracion: number;
  tipoCurso: string;
}

interface Estudiante {
  id: number;
  nombre: string;
  apellidos: string;
  email: string;
  codigo: string;
}

interface Inscripcion {
  id: number;
  estudianteId: number;
  cursoId: number;
  estudianteNombre: string;
  cursoNombre: string;
  fechaInscripcion: string;
}

export default function DashboardAdmin() {
  const router = useRouter();
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [activeTab, setActiveTab] = useState('dashboard');
  const [cursos, setCursos] = useState<Curso[]>([]);
  const [estudiantes, setEstudiantes] = useState<Estudiante[]>([]);
  const [inscripciones, setInscripciones] = useState<Inscripcion[]>([]);
  const [config, setConfig] = useState<Record<string, string>>({});
  const [resultado, setResultado] = useState<string>('');
  const [mensaje, setMensaje] = useState('');

  // Estados para el modal mejorado de inscripción
  const [busquedaEstudiante, setBusquedaEstudiante] = useState('');
  const [estudianteSeleccionado, setEstudianteSeleccionado] = useState<Estudiante | null>(null);
  const [cursoSeleccionado, setCursoSeleccionado] = useState<Curso | null>(null);

  // Modal states
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('');
  const [formData, setFormData] = useState<Record<string, string>>({});

  useEffect(() => {
    const stored = localStorage.getItem('usuario');
    if (!stored) {
      router.push('/login');
      return;
    }
    const user = JSON.parse(stored);
    if (user.tipoUsuario !== 'administrador') {
      router.push('/login');
      return;
    }
    setUsuario(user);
    cargarDatos();
    cargarConfiguracion();
  }, [router]);

  const cargarDatos = async () => {
    try {
      const [cursosRes, inscripcionesRes, estudiantesRes] = await Promise.all([
        fetch(`${API_URL}/cursos`),
        fetch(`${API_URL}/inscripciones`),
        fetch(`${API_URL}/estudiantes`)
      ]);
      const cursosData = await cursosRes.json();
      const inscripcionesData = await inscripcionesRes.json();
      const estudiantesData = await estudiantesRes.json();

      setCursos(Array.isArray(cursosData) ? cursosData : []);
      setInscripciones(Array.isArray(inscripcionesData) ? inscripcionesData : []);
      setEstudiantes(Array.isArray(estudiantesData) ? estudiantesData : []);
    } catch {
      setCursos([]);
      setInscripciones([]);
      setEstudiantes([]);
    }
  };

  const cargarConfiguracion = async () => {
    try {
      const res = await fetch(`${API_URL}/patrones/singleton/configuracion`);
      const data = await res.json();
      setConfig(data);
    } catch {
      setConfig({});
    }
  };

  // CRUD Operations
  const openModal = (type: string, data?: Record<string, string>) => {
    setModalType(type);
    setFormData(data || {});
    setShowModal(true);
    // Limpiar estados del modal de inscripción
    if (type === 'inscripcion') {
      setBusquedaEstudiante('');
      setEstudianteSeleccionado(null);
      setCursoSeleccionado(null);
    }
  };

  const closeModal = () => {
    setShowModal(false);
    setFormData({});
    setBusquedaEstudiante('');
    setEstudianteSeleccionado(null);
    setCursoSeleccionado(null);
  };

  const handleSubmit = async () => {
    try {
      if (modalType === 'curso') {
        if (formData.id) {
          // Modificar curso existente
          const res = await fetch(`${API_URL}/cursos/${formData.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              nombre: formData.nombre,
              descripcion: formData.descripcion,
              tipoCurso: formData.tipoCurso || 'virtual',
              duracion: parseInt(formData.duracion || '40')
            })
          });
          if (res.ok) {
            setMensaje('Curso modificado exitosamente');
            cargarDatos();
          }
        } else {
          // Crear nuevo curso usando Builder
          const params = new URLSearchParams({
            nombre: formData.nombre || '',
            descripcion: formData.descripcion || '',
            tipoCurso: formData.tipoCurso || 'virtual',
            duracion: formData.duracion || '40'
          });
          const res = await fetch(`${API_URL}/patrones/builder/construir-curso?${params}`, {
            method: 'POST'
          });
          if (res.ok) {
            setMensaje('Curso creado exitosamente usando patrón Builder');
            cargarDatos();
          }
        }
      } else if (modalType === 'inscripcion') {
        if (!estudianteSeleccionado || !cursoSeleccionado) {
          setMensaje('Selecciona estudiante y curso');
          return;
        }
        const res = await fetch(`${API_URL}/patrones/facade/inscribir?estudianteId=${estudianteSeleccionado.id}&cursoId=${cursoSeleccionado.id}`, {
          method: 'POST'
        });
        const data = await res.json();
        if (data.exito) {
          setMensaje('✅ Inscripción creada exitosamente usando patrón Facade');
          cargarDatos();
        } else {
          setMensaje('❌ ' + (data.mensaje || 'Error al inscribir'));
          return;
        }
      }
      closeModal();
    } catch (error) {
      setMensaje('Error en la operación');
    }
  };

  const eliminarCurso = async (id: number) => {
    if (confirm('¿Eliminar este curso?')) {
      try {
        await fetch(`${API_URL}/cursos/${id}`, { method: 'DELETE' });
        setMensaje('Curso eliminado');
        cargarDatos();
      } catch {
        setMensaje('Error al eliminar');
      }
    }
  };

  const eliminarInscripcion = async (id: number) => {
    if (confirm('¿Eliminar esta inscripción?')) {
      try {
        await fetch(`${API_URL}/inscripciones/${id}`, { method: 'DELETE' });
        setMensaje('Inscripción eliminada');
        cargarDatos();
      } catch {
        setMensaje('Error al eliminar');
      }
    }
  };

  const actualizarConfig = async (clave: string, valor: string) => {
    try {
      const res = await fetch(`${API_URL}/patrones/singleton/configuracion?clave=${clave}&valor=${valor}`, {
        method: 'POST'
      });
      const data = await res.json();
      setConfig(data);
      setMensaje(`SINGLETON: Configuración ${clave} actualizada`);
    } catch {
      setMensaje('Error al actualizar configuración');
    }
  };

  const ejecutarPatron = async (patron: string, params: string = '') => {
    try {
      const res = await fetch(`${API_URL}/patrones/${patron}${params}`, {
        method: params.includes('?') ? 'POST' : 'GET'
      });
      const data = await res.json();
      setResultado(JSON.stringify(data, null, 2));
      setMensaje(`Patrón ${patron} ejecutado`);
      cargarDatos();
    } catch {
      setMensaje('Error al ejecutar patrón');
    }
  };

  const logout = () => {
    localStorage.clear();
    router.push('/login');
  };

  if (!usuario) return <div className="p-8">Cargando...</div>;

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-purple-600 text-white p-4 flex justify-between">
        <h1 className="text-xl font-bold">Dashboard Administrador</h1>
        <div className="flex items-center gap-4">
          <span>{usuario.nombre}</span>
          <button onClick={logout} className="bg-red-500 px-3 py-1 rounded">Salir</button>
        </div>
      </nav>

      <div className="p-6">
        {mensaje && (
          <div className="mb-4 p-3 bg-green-100 text-green-800 rounded">
            {mensaje}
            <button onClick={() => setMensaje('')} className="ml-4 text-sm">×</button>
          </div>
        )}

        <div className="mb-6 flex flex-wrap gap-2">
          {['dashboard', 'cursos', 'inscripciones', 'patrones', 'singleton'].map(tab => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-4 py-2 rounded ${activeTab === tab ? 'bg-purple-600 text-white' : 'bg-white text-gray-800'}`}
            >
              {tab === 'singleton' ? 'Configuración (Singleton)' : tab.charAt(0).toUpperCase() + tab.slice(1)}
            </button>
          ))}
        </div>

        {activeTab === 'dashboard' && (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-bold text-gray-800">Total Cursos</h3>
              <p className="text-3xl font-bold text-blue-600">{cursos.length}</p>
            </div>
            <div className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-bold text-gray-800">Total Inscripciones</h3>
              <p className="text-3xl font-bold text-purple-600">{inscripciones.length}</p>
            </div>
          </div>
        )}

        {activeTab === 'cursos' && (
          <div className="bg-white p-6 rounded-lg shadow">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">Gestión de Cursos</h2>
              <button
                onClick={() => openModal('curso')}
                className="bg-green-500 text-white px-4 py-2 rounded"
              >
                + Agregar Curso
              </button>
            </div>
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left p-2 text-gray-800">ID</th>
                  <th className="text-left p-2 text-gray-800">Nombre</th>
                  <th className="text-left p-2 text-gray-800">Tipo</th>
                  <th className="text-left p-2 text-gray-800">Duración</th>
                  <th className="text-left p-2 text-gray-800">Acciones</th>
                </tr>
              </thead>
              <tbody>
                {cursos.map(curso => (
                  <tr key={curso.id} className="border-b">
                    <td className="p-2 text-gray-800">{curso.id}</td>
                    <td className="p-2 text-gray-800">{curso.nombre}</td>
                    <td className="p-2 text-gray-600">{curso.tipoCurso || 'N/A'}</td>
                    <td className="p-2 text-gray-800">{curso.duracion || 0}h</td>
                    <td className="p-2 space-x-2">
                      <button
                        onClick={() => openModal('curso', {
                          id: curso.id.toString(),
                          nombre: curso.nombre,
                          descripcion: curso.descripcion,
                          tipoCurso: curso.tipoCurso,
                          duracion: curso.duracion?.toString() || ''
                        })}
                        className="bg-blue-500 text-white px-2 py-1 rounded text-sm"
                      >
                        Modificar
                      </button>
                      <button
                        onClick={() => eliminarCurso(curso.id)}
                        className="bg-red-500 text-white px-2 py-1 rounded text-sm"
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {activeTab === 'inscripciones' && (
          <div className="bg-white p-6 rounded-lg shadow">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold text-gray-800">Gestión de Inscripciones</h2>
              <button
                onClick={() => openModal('inscripcion')}
                className="bg-green-500 text-white px-4 py-2 rounded"
              >
                + Agregar Inscripción
              </button>
            </div>
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="text-left p-2 text-gray-800">ID</th>
                  <th className="text-left p-2 text-gray-800">Estudiante</th>
                  <th className="text-left p-2 text-gray-800">Curso</th>
                  <th className="text-left p-2 text-gray-800">Fecha</th>
                  <th className="text-left p-2 text-gray-800">Acciones</th>
                </tr>
              </thead>
              <tbody>
                {inscripciones.map(ins => (
                  <tr key={ins.id} className="border-b">
                    <td className="p-2 text-gray-800">{ins.id}</td>
                    <td className="p-2 text-gray-800">{ins.estudianteNombre || `ID: ${ins.estudianteId}`}</td>
                    <td className="p-2 text-gray-800">{ins.cursoNombre || `ID: ${ins.cursoId}`}</td>
                    <td className="p-2 text-gray-600">{ins.fechaInscripcion || 'N/A'}</td>
                    <td className="p-2">
                      <button
                        onClick={() => eliminarInscripcion(ins.id)}
                        className="bg-red-500 text-white px-2 py-1 rounded text-sm"
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {activeTab === 'patrones' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-4 text-gray-800">Ejecutar Patrones</h2>
              <div className="grid grid-cols-2 gap-3">
                <button
                  onClick={() => ejecutarPatron('abstract-factory/crear-curso', '?tipo=virtual')}
                  className="bg-blue-500 text-white p-2 rounded text-sm"
                >
                  Abstract Factory (Virtual)
                </button>
                <button
                  onClick={() => ejecutarPatron('abstract-factory/crear-curso', '?tipo=presencial')}
                  className="bg-blue-600 text-white p-2 rounded text-sm"
                >
                  Abstract Factory (Presencial)
                </button>
                <button
                  onClick={() => ejecutarPatron('builder/construir-curso', '?nombre=CursoBuilder&descripcion=Creado con Builder&duracion=40&numModulos=3&conEvaluaciones=true')}
                  className="bg-green-500 text-white p-2 rounded text-sm"
                >
                  Builder
                </button>
                <button
                  onClick={() => ejecutarPatron('prototype/clonar-curso/' + (cursos[0]?.id || 1), '')}
                  className="bg-yellow-500 text-white p-2 rounded text-sm"
                >
                  Prototype (Clonar)
                </button>
                <button
                  onClick={() => ejecutarPatron('decorator/agregar-funcionalidad', `?cursoId=${cursos[0]?.id || 1}&gamificacion=true&certificacion=true`)}
                  className="bg-pink-500 text-white p-2 rounded text-sm"
                >
                  Decorator
                </button>
                <button
                  onClick={() => ejecutarPatron('adapter/videoconferencia', '?plataforma=zoom&sala=TestRoom')}
                  className="bg-purple-500 text-white p-2 rounded text-sm"
                >
                  Adapter (Zoom)
                </button>
                <button
                  onClick={() => ejecutarPatron('bridge/renderizar', '?contenido=Test&dispositivo=web')}
                  className="bg-indigo-500 text-white p-2 rounded text-sm"
                >
                  Bridge
                </button>
                <button
                  onClick={() => ejecutarPatron('composite/estructura-curso/' + (cursos[0]?.id || 1), '')}
                  className="bg-teal-500 text-white p-2 rounded text-sm"
                >
                  Composite
                </button>
              </div>
            </div>
            <div className="bg-white p-6 rounded-lg shadow">
              <h2 className="text-xl font-bold mb-4 text-gray-800">Resultado</h2>
              <pre className="bg-gray-100 p-4 rounded overflow-auto text-sm text-gray-800 max-h-64">
                {resultado || 'Ejecuta un patrón para ver el resultado'}
              </pre>
            </div>
          </div>
        )}

        {activeTab === 'singleton' && (
          <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-bold mb-4 text-gray-800">Configuración del Sistema (Patrón Singleton)</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {[
                { key: 'nombreSistema', label: 'Nombre del Sistema', default: 'EduLearn' },
                { key: 'maxEstudiantes', label: 'Max Estudiantes por Curso', default: '30' },
                { key: 'emailSoporte', label: 'Email de Soporte', default: 'soporte@edulearn.com' },
                { key: 'zonaHoraria', label: 'Zona Horaria', default: 'America/Mexico_City' }
              ].map(item => (
                <div key={item.key} className="p-4 border rounded">
                  <label className="block text-sm font-bold text-gray-800 mb-2">{item.label}</label>
                  <div className="flex gap-2">
                    <input
                      type="text"
                      defaultValue={config[item.key] || item.default}
                      className="flex-1 p-2 border rounded text-gray-800"
                      id={item.key}
                    />
                    <button
                      onClick={() => actualizarConfig(item.key, (document.getElementById(item.key) as HTMLInputElement).value)}
                      className="bg-purple-500 text-white px-3 rounded"
                    >
                      Guardar
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className={`bg-white p-6 rounded-lg shadow-2xl ${modalType === 'inscripcion' ? 'w-full max-w-3xl max-h-[90vh] overflow-y-auto' : 'w-96'}`}>
            <h3 className="text-2xl font-bold mb-4 text-gray-800">
              {modalType === 'curso' ? (formData.id ? '✏️ Modificar Curso' : '➕ Agregar Curso') : '📝 Nueva Inscripción'}
            </h3>

            {modalType === 'curso' && (
              <>
                <input
                  type="text"
                  placeholder="Nombre del curso"
                  className="w-full p-2 border rounded mb-3 text-gray-800"
                  value={formData.nombre || ''}
                  onChange={e => setFormData({...formData, nombre: e.target.value})}
                />
                <textarea
                  placeholder="Descripción"
                  className="w-full p-2 border rounded mb-3 text-gray-800"
                  value={formData.descripcion || ''}
                  onChange={e => setFormData({...formData, descripcion: e.target.value})}
                />
                <select
                  className="w-full p-2 border rounded mb-3 text-gray-800"
                  value={formData.tipoCurso || 'virtual'}
                  onChange={e => setFormData({...formData, tipoCurso: e.target.value})}
                >
                  <option value="virtual">Virtual</option>
                  <option value="presencial">Presencial</option>
                  <option value="hibrido">Híbrido</option>
                </select>
                <input
                  type="number"
                  placeholder="Duración (horas)"
                  className="w-full p-2 border rounded mb-3 text-gray-800"
                  value={formData.duracion || ''}
                  onChange={e => setFormData({...formData, duracion: e.target.value})}
                />
              </>
            )}

            {modalType === 'inscripcion' && (
              <div className="space-y-4">
                {/* Búsqueda de Estudiante */}
                <div>
                  <label className="block text-sm font-bold text-gray-700 mb-2">
                    Buscar Estudiante
                  </label>
                  <input
                    type="text"
                    placeholder="Buscar por nombre, email o código..."
                    className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-purple-500 text-gray-800"
                    value={busquedaEstudiante}
                    onChange={e => setBusquedaEstudiante(e.target.value)}
                  />

                  {/* Lista de resultados de estudiantes */}
                  {busquedaEstudiante && (
                    <div className="mt-2 max-h-48 overflow-y-auto border rounded-lg">
                      {estudiantes
                        .filter(est => {
                          const search = busquedaEstudiante.toLowerCase();
                          return (
                            (est.nombre || '').toLowerCase().includes(search) ||
                            (est.apellidos || '').toLowerCase().includes(search) ||
                            (est.email || '').toLowerCase().includes(search) ||
                            (est.codigo || '').toLowerCase().includes(search)
                          );
                        })
                        .map(est => (
                          <div
                            key={est.id}
                            onClick={() => {
                              setEstudianteSeleccionado(est);
                              setBusquedaEstudiante('');
                            }}
                            className="p-3 hover:bg-purple-50 cursor-pointer border-b last:border-b-0"
                          >
                            <div className="font-semibold text-gray-800">
                              {est.nombre} {est.apellidos}
                            </div>
                            <div className="text-sm text-gray-600">
                              {est.email} • Código: {est.codigo}
                            </div>
                          </div>
                        ))
                      }
                    </div>
                  )}

                  {/* Estudiante seleccionado */}
                  {estudianteSeleccionado && (
                    <div className="mt-3 p-4 bg-purple-50 border-2 border-purple-200 rounded-lg">
                      <div className="flex justify-between items-start">
                        <div>
                          <div className="font-bold text-gray-800">
                            👤 {estudianteSeleccionado.nombre} {estudianteSeleccionado.apellidos}
                          </div>
                          <div className="text-sm text-gray-600 mt-1">
                            📧 {estudianteSeleccionado.email}
                          </div>
                          <div className="text-sm text-gray-600">
                            🎓 Código: {estudianteSeleccionado.codigo}
                          </div>
                        </div>
                        <button
                          onClick={() => setEstudianteSeleccionado(null)}
                          className="text-red-500 font-bold"
                        >
                          ✕
                        </button>
                      </div>
                    </div>
                  )}
                </div>

                {/* Selección de Curso */}
                <div>
                  <label className="block text-sm font-bold text-gray-700 mb-2">
                    Seleccionar Curso
                  </label>
                  <div className="space-y-2 max-h-64 overflow-y-auto">
                    {cursos.map(curso => {
                      const inscritosEnCurso = inscripciones.filter(i => i.cursoId === curso.id).length;
                      const tipoCurso = (curso.tipoCurso || 'presencial').toLowerCase();
                      const cupoMaximo = tipoCurso === 'virtual' ? 100 : 30;
                      const cuposDisponibles = cupoMaximo - inscritosEnCurso;
                      const isSelected = cursoSeleccionado?.id === curso.id;

                      return (
                        <div
                          key={curso.id}
                          onClick={() => setCursoSeleccionado(curso)}
                          className={`p-4 border-2 rounded-lg cursor-pointer transition-all ${
                            isSelected
                              ? 'border-purple-500 bg-purple-50'
                              : 'border-gray-200 hover:border-purple-300 hover:bg-gray-50'
                          }`}
                        >
                          <div className="flex justify-between items-start">
                            <div className="flex-1">
                              <div className="font-bold text-gray-800">
                                {curso.nombre}
                              </div>
                              <div className="text-sm text-gray-600 mt-1">
                                {curso.descripcion}
                              </div>
                              <div className="flex gap-3 mt-2 text-xs">
                                <span className={`px-2 py-1 rounded ${
                                  tipoCurso === 'virtual' ? 'bg-blue-100 text-blue-700' :
                                  tipoCurso === 'presencial' ? 'bg-green-100 text-green-700' :
                                  'bg-yellow-100 text-yellow-700'
                                }`}>
                                  {tipoCurso}
                                </span>
                                <span className="bg-gray-100 text-gray-700 px-2 py-1 rounded">
                                  ⏱ {curso.duracion}h
                                </span>
                                <span className={`px-2 py-1 rounded ${
                                  cuposDisponibles > 10 ? 'bg-green-100 text-green-700' :
                                  cuposDisponibles > 0 ? 'bg-yellow-100 text-yellow-700' :
                                  'bg-red-100 text-red-700'
                                }`}>
                                  👥 {inscritosEnCurso}/{cupoMaximo}
                                </span>
                              </div>
                            </div>
                            {isSelected && (
                              <div className="text-purple-500 text-2xl ml-2">
                                ✓
                              </div>
                            )}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
            )}

            <div className="flex gap-3 justify-end mt-6">
              <button
                onClick={closeModal}
                className="px-6 py-2 bg-gray-300 hover:bg-gray-400 text-gray-800 font-semibold rounded-lg transition-colors"
              >
                Cancelar
              </button>
              <button
                onClick={handleSubmit}
                disabled={modalType === 'inscripcion' && (!estudianteSeleccionado || !cursoSeleccionado)}
                className={`px-6 py-2 font-semibold rounded-lg transition-colors ${
                  modalType === 'inscripcion' && (!estudianteSeleccionado || !cursoSeleccionado)
                    ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                    : 'bg-purple-600 hover:bg-purple-700 text-white'
                }`}
              >
                {modalType === 'inscripcion' ? '✓ Inscribir Estudiante' : 'Guardar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
