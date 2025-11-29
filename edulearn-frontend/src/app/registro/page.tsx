'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { API_URL } from '@/lib/api';
import Link from 'next/link';

export default function RegistroPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    nombre: '',
    apellidos: '',
    email: '',
    password: '',
    tipoUsuario: 'estudiante'
  });
  const [mensaje, setMensaje] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMensaje('');

    // Validaciones
    if (!formData.nombre.trim() || !formData.apellidos.trim() ||
        !formData.email.trim() || !formData.password.trim()) {
      setMensaje('Por favor, complete todos los campos');
      return;
    }

    setLoading(true);

    try {
      const res = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      const data = await res.json();

      if (data.exito) {
        setMensaje('✅ Registro exitoso! Redirigiendo al login...');
        setTimeout(() => {
          router.push('/login');
        }, 2000);
      } else {
        setMensaje('❌ ' + data.mensaje);
      }
    } catch (error) {
      setMensaje('❌ Error al conectar con el servidor');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md">
        {/* Título */}
        <h1 className="text-3xl font-bold text-center text-gray-800 mb-2">
          Registrate
        </h1>
        <p className="text-center text-gray-600 mb-6">
          Sistema de Gestión de Aprendizaje
        </p>

        {/* Mensaje */}
        {mensaje && (
          <div className={`mb-4 p-3 rounded ${
            mensaje.includes('✅') ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}>
            {mensaje}
          </div>
        )}

        {/* Formulario */}
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Nombre */}
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-2">
              Nombre
            </label>
            <input
              type="text"
              placeholder="Carolina Vásquez"
              className="w-full p-3 border-2 border-gray-300 rounded-lg focus:border-blue-500 focus:outline-none text-gray-800"
              value={formData.nombre}
              onChange={e => setFormData({...formData, nombre: e.target.value})}
            />
          </div>

          {/* Apellidos */}
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-2">
              Apellidos
            </label>
            <input
              type="text"
              placeholder="Pérez García"
              className="w-full p-3 border-2 border-gray-300 rounded-lg focus:border-blue-500 focus:outline-none text-gray-800"
              value={formData.apellidos}
              onChange={e => setFormData({...formData, apellidos: e.target.value})}
            />
          </div>

          {/* Correo Electrónico */}
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-2">
              Correo Electrónico
            </label>
            <input
              type="email"
              placeholder="miNombre@itoaxca.edu.mx"
              className="w-full p-3 border-2 border-gray-300 rounded-lg focus:border-blue-500 focus:outline-none text-gray-800"
              value={formData.email}
              onChange={e => setFormData({...formData, email: e.target.value})}
            />
          </div>

          {/* Contraseña */}
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-2">
              Contraseña
            </label>
            <input
              type="password"
              placeholder="••••••••"
              className="w-full p-3 border-2 border-gray-300 rounded-lg focus:border-blue-500 focus:outline-none text-gray-800"
              value={formData.password}
              onChange={e => setFormData({...formData, password: e.target.value})}
            />
          </div>

          {/* Tipo de Usuario */}
          <div>
            <label className="block text-sm font-bold text-gray-700 mb-2">
              Tipo de Usuario
            </label>
            <div className="space-y-2">
              {[
                { value: 'estudiante', label: 'Estudiante' },
                { value: 'profesor', label: 'Profesor' }
              ].map(option => (
                <label
                  key={option.value}
                  className={`flex items-center p-3 border-2 rounded-lg cursor-pointer transition-all ${
                    formData.tipoUsuario === option.value
                      ? 'border-blue-500 bg-blue-50'
                      : 'border-gray-300 hover:border-gray-400'
                  }`}
                >
                  <input
                    type="radio"
                    name="tipoUsuario"
                    value={option.value}
                    checked={formData.tipoUsuario === option.value}
                    onChange={e => setFormData({...formData, tipoUsuario: e.target.value})}
                    className="mr-3"
                  />
                  <span className="text-gray-800 font-medium">{option.label}</span>
                </label>
              ))}
            </div>
          </div>

          {/* Botón Crear Cuenta */}
          <button
            type="submit"
            disabled={loading}
            className={`w-full py-3 rounded-lg font-bold text-white transition-colors ${
              loading
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-blue-600 hover:bg-blue-700'
            }`}
          >
            {loading ? 'Creando cuenta...' : 'Crear Cuenta'}
          </button>
        </form>

        {/* Link a Login */}
        <div className="mt-6 text-center text-gray-600">
          ¿Ya tienes una cuenta?{' '}
          <Link href="/login" className="text-blue-600 font-bold hover:underline">
            Iniciar sesión
          </Link>
        </div>
      </div>
    </div>
  );
}
