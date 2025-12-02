"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { CourseContentTree } from "./course-content-tree"
import { Book, Users, BarChart3, ClipboardList } from "lucide-react"

interface CourseDetailProps {
  courseId: string | null
  onBack: () => void
}

export default function CourseDetail({ courseId, onBack }: CourseDetailProps) {
  const [activeTab, setActiveTab] = useState("content")
  const [role, setRole] = useState<"ESTUDIANTE" | "DOCENTE" | "ADMIN">("ESTUDIANTE")

  // ═══════════════════════════════════════════════════════════════
  // RECUPERACIÓN DEL ROL DEL USUARIO
  // ═══════════════════════════════════════════════════════════════
  useEffect(() => {
    // Simulación de recuperación del rol desde el localStorage o contexto
    // En una implementación real, esto vendría del contexto de autenticación
    const userRole = localStorage.getItem("userRole") as "ESTUDIANTE" | "DOCENTE" | "ADMIN"

    if (userRole) {
      setRole(userRole)
      console.log("✅ [CourseDetail] Rol del usuario recuperado:", userRole)
    } else {
      // Valor por defecto para desarrollo
      const defaultRole = "DOCENTE" // Cambiar a "ESTUDIANTE" o "ADMIN" para probar
      setRole(defaultRole)
      localStorage.setItem("userRole", defaultRole)
      console.log("⚠️  [CourseDetail] Rol no encontrado. Usando rol por defecto:", defaultRole)
    }
  }, [])

  // Mock data del curso
  const course = {
    id: courseId,
    name: "Patrones de Diseño en Java",
    instructor: "Dr. Juan Pérez",
    description: "Aprende los patrones de diseño más importantes y cómo aplicarlos en Java",
    type: "Virtual",
    students: 45,
    progress: 65,
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <Button onClick={onBack} className="mb-6 bg-muted hover:bg-muted/80 text-foreground">
        ← Volver
      </Button>

      <div className="mb-8">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-3xl font-bold text-foreground">{course.name}</h1>
            <p className="text-muted-foreground mt-2">Instructor: {course.instructor}</p>

            {/* Indicador de rol del usuario */}
            <div className="mt-3 flex items-center gap-2">
              <span className="text-sm text-muted-foreground">Tu rol:</span>
              <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                role === "ADMIN"
                  ? "bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-300"
                  : role === "DOCENTE"
                    ? "bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300"
                    : "bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-300"
              }`}>
                {role}
              </span>
            </div>
          </div>
          <span className="bg-primary/10 text-primary px-4 py-2 rounded-lg">{course.type}</span>
        </div>

        {/* Progress Bar (Solo para estudiantes) */}
        {role === "ESTUDIANTE" && (
          <div className="w-full max-w-2xl">
            <div className="flex justify-between items-center mb-2">
              <span className="text-sm font-medium text-foreground">Progreso General</span>
              <span className="text-sm font-semibold text-primary">{course.progress}%</span>
            </div>
            <div className="w-full h-3 rounded-full bg-muted overflow-hidden">
              <div
                className="h-full bg-gradient-to-r from-primary to-accent transition-all duration-500"
                style={{ width: `${course.progress}%` }}
              ></div>
            </div>
          </div>
        )}
      </div>

      {/* Tabs */}
      <div className="flex gap-4 mb-6 border-b border-border">
        {[
          { id: "content", label: "Contenido", icon: Book },
          { id: "estudiantes", label: "Estudiantes", icon: Users, rolesPermitidos: ["DOCENTE", "ADMIN"] },
          { id: "evaluaciones", label: "Evaluaciones", icon: ClipboardList },
          { id: "estadisticas", label: "Estadísticas", icon: BarChart3, rolesPermitidos: ["DOCENTE", "ADMIN"] },
        ].map((tab) => {
          // Ocultar tabs según el rol
          if (tab.rolesPermitidos && !tab.rolesPermitidos.includes(role)) {
            return null
          }

          const Icon = tab.icon
          return (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-4 py-3 font-medium transition-colors border-b-2 flex items-center gap-2 ${
                activeTab === tab.id
                  ? "border-primary text-primary"
                  : "border-transparent text-muted-foreground hover:text-foreground"
              }`}
            >
              <Icon className="w-4 h-4" />
              {tab.label}
            </button>
          )
        })}
      </div>

      {/* Content */}
      <div>
        {activeTab === "content" && (
          <div>
            {/* ═══════════════════════════════════════════════════════════════ */}
            {/* PASO CRÍTICO: Pasar el rol al CourseContentTree                */}
            {/* ═══════════════════════════════════════════════════════════════ */}
            <CourseContentTree
              courseId={course.id || "1"}
              role={role}
            />
          </div>
        )}

        {activeTab === "estudiantes" && (
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Estudiantes Inscritos ({course.students})</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground">Lista de estudiantes del curso...</p>
            </CardContent>
          </Card>
        )}

        {activeTab === "evaluaciones" && (
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Evaluaciones</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground">
                {role === "ESTUDIANTE"
                  ? "Tus calificaciones y evaluaciones pendientes..."
                  : "Gestión de evaluaciones del curso..."}
              </p>
            </CardContent>
          </Card>
        )}

        {activeTab === "estadisticas" && (
          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Estadísticas del Curso</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground">Métricas y análisis del curso...</p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  )
}
