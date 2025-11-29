// Usar variable de entorno o localhost por defecto
export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function getCursos() {
  const res = await fetch(`${API_URL}/cursos`);
  return res.json();
}

export async function getEstudiantes() {
  const res = await fetch(`${API_URL}/estudiantes`);
  return res.json();
}

export async function getInscripciones() {
  const res = await fetch(`${API_URL}/inscripciones`);
  return res.json();
}

export async function createCurso(curso: { nombre: string; descripcion: string; duracion: number }) {
  const res = await fetch(`${API_URL}/cursos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(curso)
  });
  return res.json();
}

export async function createEstudiante(estudiante: { nombre: string; apellidos: string; email: string }) {
  const res = await fetch(`${API_URL}/estudiantes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(estudiante)
  });
  return res.json();
}

export async function login(email: string, password: string) {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  return res.json();
}
