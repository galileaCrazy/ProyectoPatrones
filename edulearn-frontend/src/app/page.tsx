'use client';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Verificar si hay sesión activa
    const usuario = localStorage.getItem('usuario');
    if (usuario) {
      const user = JSON.parse(usuario);
      switch (user.tipoUsuario) {
        case 'estudiante':
          router.push('/dashboard/estudiante');
          break;
        case 'profesor':
          router.push('/dashboard/profesor');
          break;
        case 'administrador':
          router.push('/dashboard/admin');
          break;
        default:
          router.push('/login');
      }
    } else {
      router.push('/login');
    }
  }, [router]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="text-center">
        <h1 className="text-3xl font-bold text-gray-800 mb-4">EduLearn</h1>
        <p className="text-gray-600">Cargando...</p>
      </div>
    </div>
  );
}
