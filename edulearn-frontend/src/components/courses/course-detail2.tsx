"use client"

import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { CourseContentTree } from "./course-content-tree"
import { ModuleTree } from "./module-tree"

interface CourseDetailViewProps {
  courseId: string | null
  role: "student" | "professor" | "admin"
  onBack: () => void
}

export default function CourseDetailView({ courseId, role, onBack }: CourseDetailViewProps) {
  const [activeTab, setActiveTab] = useState("content")
  const [selectedNode, setSelectedNode] = useState<any>(null)

  const course = {
    id: courseId,
    name: "Programación Orientada a Objetos",
    instructor: "Juan Pérez",
    description: "Aprende los conceptos fundamentales de POO",
    type: "Virtual",
    students: 45,
    progress: 75,
  }

  const modules = [
    {
      id: "1",
      name: "Introducción a POO",
      lessons: 3,
      completed: 3,
      status: "completed",
    },
    {
      id: "2",
      name: "Clases y Objetos",
      lessons: 4,
      completed: 3,
      status: "in-progress",
    },
    {
      id: "3",
      name: "Herencia y Polimorfismo",
      lessons: 3,
      completed: 0,
      status: "locked",
    },
  ]

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
          </div>
          <span className="bg-primary/10 text-primary px-4 py-2 rounded-lg">{course.type}</span>
        </div>

        {/* Progress Bar */}
        <div className="w-full max-w-2xl">
          <div className="flex justify-between items-center mb-2">
            <span className="text-sm font-medium text-foreground">Progreso General</span>
            <span className="text-sm font-semibold text-primary">{course.progress}%</span>
          </div>
          <div className="w-full h-3 rounded-full bg-muted overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-primary to-accent"
              style={{ width: `${course.progress}%` }}
            ></div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex gap-4 mb-6 border-b border-border">
        {["content", "estudiantes", "evaluaciones", "estadísticas"].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4 py-3 font-medium transition-colors border-b-2 ${
              activeTab === tab
                ? "border-primary text-primary"
                : "border-transparent text-muted-foreground hover:text-foreground"
            }`}
          >
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>

      {/* Content */}
      {activeTab === "content" && (
        <div className="space-y-6">
          <CourseContentTree
            courseId={courseId || ""}
            role={role === "student" ? "ESTUDIANTE" : role === "professor" ? "DOCENTE" : "ADMIN"}
          />

          {/* Selected Node Details */}
          {selectedNode && (
            <Card className="border-border/50 bg-accent/5">
              <CardHeader>
                <CardTitle>{selectedNode.name}</CardTitle>
                <CardDescription>
                  Tipo: {selectedNode.type.charAt(0).toUpperCase() + selectedNode.type.slice(1)}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <p className="text-muted-foreground">
                    Selecciona cualquier módulo, lección o actividad para ver más detalles.
                  </p>
                  {selectedNode.status && (
                    <div>
                      <p className="text-sm font-medium mb-1">Estado:</p>
                      <span
                        className={`text-xs px-3 py-1 rounded-full font-semibold ${
                          selectedNode.status === "completed"
                            ? "bg-green-100 text-green-700"
                            : selectedNode.status === "in-progress"
                              ? "bg-blue-100 text-blue-700"
                              : "bg-gray-100 text-gray-700"
                        }`}
                      >
                        {selectedNode.status.charAt(0).toUpperCase() + selectedNode.status.slice(1)}
                      </span>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      )}

      {activeTab === "estudiantes" && (
        <Card>
          <CardHeader>
            <CardTitle>Estudiantes Inscritos ({course.students})</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {Array.from({ length: 5 }).map((_, i) => (
                <div
                  key={i}
                  className="flex items-center justify-between p-3 rounded-lg bg-muted/50 border border-border"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center">
                      <span className="text-primary font-semibold">ST</span>
                    </div>
                    <div>
                      <p className="font-medium text-foreground">Estudiante {i + 1}</p>
                      <p className="text-xs text-muted-foreground">est{i + 1}@universidad.edu</p>
                    </div>
                  </div>
                  <span className="text-sm font-semibold text-primary">85%</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {activeTab === "evaluaciones" && (
        <Card>
          <CardHeader>
            <CardTitle>Evaluaciones del Curso</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {[
                { name: "Quiz 1: Conceptos Básicos", type: "Quiz", date: "15/11/2025", score: 92 },
                { name: "Tarea: Implementación de Clases", type: "Tarea", date: "20/11/2025", score: 88 },
                { name: "Examen Parcial", type: "Examen", date: "25/11/2025", score: null },
              ].map((eval_, i) => (
                <div key={i} className="p-3 rounded-lg bg-muted/50 border border-border">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-medium text-foreground">{eval_.name}</p>
                      <div className="flex gap-2 mt-1">
                        <span className="text-xs bg-primary/10 text-primary px-2 py-1 rounded">{eval_.type}</span>
                        <span className="text-xs text-muted-foreground">{eval_.date}</span>
                      </div>
                    </div>
                    {eval_.score ? (
                      <span className="text-lg font-bold text-primary">{eval_.score}%</span>
                    ) : (
                      <span className="text-sm text-muted-foreground">Pendiente</span>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {activeTab === "estadísticas" && (
        <Card>
          <CardHeader>
            <CardTitle>Estadísticas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">32h</p>
                <p className="text-xs text-muted-foreground mt-1">Horas de estudio</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">15/18</p>
                <p className="text-xs text-muted-foreground mt-1">Tareas completadas</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">8</p>
                <p className="text-xs text-muted-foreground mt-1">Posts en foro</p>
              </div>
              <div className="text-center p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-2xl font-bold text-primary">85%</p>
                <p className="text-xs text-muted-foreground mt-1">Promedio actual</p>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
