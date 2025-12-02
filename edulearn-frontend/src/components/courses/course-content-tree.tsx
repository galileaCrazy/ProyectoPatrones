"use client"

import { useState } from "react"
import { ContentTreeNode } from "./content-tree-node"
import { ModuleEditor } from "./module-editor"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Book, CheckCircle2, Circle, Lock } from "lucide-react"

interface ContentItem {
  id: string
  name: string
  type: "VIDEO" | "PDF" | "LECTURE" | "TASK" | "QUIZ" | "SUPPLEMENT" | "FORM" | "PRACTICE"
  duration: number
  file?: string
  isCompleted?: boolean
  size?: string // Para materiales grandes (ej: "850 MB")
}

interface TreeNode {
  id: string
  name: string
  type: "module" | "submodule" | "lesson" | "activity"
  status?: "completed" | "in-progress" | "locked"
  children?: TreeNode[]
  completedItems?: number
  totalItems?: number
  estimatedTime?: number
  description?: string
  content?: ContentItem[]
}

interface CourseContentTreeProps {
  courseId: string
  role: "ESTUDIANTE" | "DOCENTE" | "ADMIN"
}

export function CourseContentTree({ courseId, role }: CourseContentTreeProps) {
  // ═══════════════════════════════════════════════════════════════
  // ESTADO LOCAL: Control de módulo en edición
  // ═══════════════════════════════════════════════════════════════
  const [editingModuleId, setEditingModuleId] = useState<string | null>(null)
  const [selectedModule, setSelectedModule] = useState<TreeNode | null>(null)

  // Mock data del contenido del curso
  const courseContent: TreeNode[] = [
    {
      id: "m1",
      name: "Patrón Proxy - Lazy Loading",
      type: "module",
      status: "in-progress",
      completedItems: 2,
      totalItems: 5,
      estimatedTime: 4,
      description:
        "Implementación del patrón Proxy con carga diferida y control de acceso. Aprende a optimizar recursos cargando contenido solo cuando es necesario.",
      content: [
        {
          id: "c1",
          name: "Introducción al Patrón Proxy",
          type: "VIDEO",
          duration: 15,
          size: "450 MB",
          isCompleted: true,
        },
        {
          id: "c2",
          name: "Documentación del Patrón Proxy",
          type: "PDF",
          duration: 10,
          size: "850 MB",
          file: "/materiales/proxy-pattern.pdf",
          isCompleted: false,
        },
        {
          id: "c3",
          name: "Lazy Loading en UI",
          type: "LECTURE",
          duration: 8,
          isCompleted: true,
        },
        {
          id: "c4",
          name: "Quiz: Patrón Proxy",
          type: "QUIZ",
          duration: 5,
          isCompleted: false,
        },
      ],
      children: [
        {
          id: "s1",
          name: "Conceptos Teóricos",
          type: "submodule",
          status: "completed",
          completedItems: 2,
          totalItems: 2,
          children: [
            {
              id: "l1",
              name: "¿Qué es el Patrón Proxy?",
              type: "lesson",
              status: "completed",
            },
            {
              id: "l2",
              name: "Tipos de Proxy",
              type: "lesson",
              status: "completed",
            },
          ],
        },
        {
          id: "s2",
          name: "Implementación Práctica",
          type: "submodule",
          status: "in-progress",
          completedItems: 1,
          totalItems: 2,
          children: [
            {
              id: "l3",
              name: "Código del Backend",
              type: "lesson",
              status: "completed",
            },
            {
              id: "l4",
              name: "Integración con Frontend",
              type: "lesson",
              status: "in-progress",
            },
          ],
        },
      ],
    },
    {
      id: "m2",
      name: "Patrón Strategy",
      type: "module",
      status: "completed",
      completedItems: 3,
      totalItems: 3,
      estimatedTime: 3,
      description: "Implementación del patrón Strategy para diferentes algoritmos de evaluación.",
      content: [
        {
          id: "c5",
          name: "Introducción al Strategy",
          type: "VIDEO",
          duration: 12,
          isCompleted: true,
        },
        {
          id: "c6",
          name: "Implementación en Java",
          type: "LECTURE",
          duration: 15,
          isCompleted: true,
        },
      ],
      children: [],
    },
    {
      id: "m3",
      name: "Patrón Chain of Responsibility",
      type: "module",
      status: "locked",
      completedItems: 0,
      totalItems: 4,
      estimatedTime: 5,
      description: "Cadena de validaciones para procesos de inscripción.",
      content: [],
      children: [
        {
          id: "s3",
          name: "Conceptos",
          type: "submodule",
          status: "locked",
          children: [
            {
              id: "l5",
              name: "Introducción",
              type: "lesson",
              status: "locked",
            },
          ],
        },
      ],
    },
  ]

  // ═══════════════════════════════════════════════════════════════
  // FUNCIÓN: Buscar módulo por ID
  // ═══════════════════════════════════════════════════════════════
  const getModuleById = (id: string): TreeNode | undefined => {
    return courseContent.find((m) => m.id === id)
  }

  // ═══════════════════════════════════════════════════════════════
  // FUNCIÓN: Manejar solicitud de edición de módulo
  // Solo se activa si el rol es DOCENTE o ADMIN
  // ═══════════════════════════════════════════════════════════════
  const handleEditModule = (moduleId: string) => {
    console.log("📝 [CourseContentTree] Solicitud de edición del módulo:", moduleId)
    console.log("👤 [CourseContentTree] Rol del usuario:", role)

    if (role === "DOCENTE" || role === "ADMIN") {
      setEditingModuleId(moduleId)
      setSelectedModule(null) // Cerrar vista de detalle si estaba abierta
      console.log("✅ [CourseContentTree] Editor de módulo activado")
    } else {
      console.log("❌ [CourseContentTree] Acceso denegado. Solo DOCENTE/ADMIN pueden editar")
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // FUNCIÓN: Guardar cambios del módulo
  // Placeholder - En producción, llamaría a la API
  // ═══════════════════════════════════════════════════════════════
  const handleSaveModule = (data: any) => {
    console.log("💾 [CourseContentTree] Guardando cambios del módulo:", data)

    // Aquí iría la lógica para actualizar el backend
    // Por ahora, solo cerramos el editor
    setEditingModuleId(null)

    console.log("✅ [CourseContentTree] Cambios guardados (simulado)")
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZADO CONDICIONAL: Editor de Módulo
  // Solo se muestra si editingModuleId tiene un valor
  // ═══════════════════════════════════════════════════════════════
  if (editingModuleId && (role === "DOCENTE" || role === "ADMIN")) {
    const moduleToEdit = getModuleById(editingModuleId)

    if (moduleToEdit) {
      console.log("🎨 [CourseContentTree] Renderizando ModuleEditor para:", moduleToEdit.name)

      return (
        <ModuleEditor
          moduleId={moduleToEdit.id}
          moduleName={moduleToEdit.name}
          estimatedTime={moduleToEdit.estimatedTime || 3}
          description={moduleToEdit.description || ""}
          initialContent={moduleToEdit.content || []}
          onSave={handleSaveModule}
          onClose={() => {
            console.log("❌ [CourseContentTree] Editor cerrado sin guardar")
            setEditingModuleId(null)
          }}
          role={role}
          cursoId={courseId}
        />
      )
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZADO PRINCIPAL: Árbol de Contenido
  // ═══════════════════════════════════════════════════════════════
  return (
    <Card className="border-border/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Book className="w-5 h-5 text-primary" />
          Contenido del Curso
          {(role === "DOCENTE" || role === "ADMIN") && (
            <span className="ml-auto text-xs text-muted-foreground">
              Modo edición activado - Pasa el cursor sobre los módulos
            </span>
          )}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-1">
          {courseContent.map((node) => (
            <ContentTreeNode
              key={node.id}
              contentItem={node}
              level={0}
              role={role}
              onEditModule={handleEditModule}
            />
          ))}
        </div>

        {/* Legend */}
        <div className="mt-6 pt-6 border-t border-border/50">
          <h4 className="text-sm font-semibold text-foreground mb-3">Leyenda:</h4>
          <div className="flex flex-wrap gap-6">
            <div className="flex items-center gap-2 text-sm">
              <CheckCircle2 className="w-4 h-4 text-green-500" />
              <span className="text-muted-foreground">Completado</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <Circle className="w-4 h-4 text-blue-500" />
              <span className="text-muted-foreground">En Progreso</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <Lock className="w-4 h-4 text-gray-400" />
              <span className="text-muted-foreground">Bloqueado</span>
            </div>
          </div>

          {/* Información de Lazy Loading */}
          <div className="mt-4 p-3 bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg">
            <p className="text-sm text-blue-700 dark:text-blue-300">
              <span className="font-semibold">💡 Lazy Loading Activado:</span> Los materiales pesados (videos, PDFs)
              solo se cargarán cuando hagas clic en "Cargar Contenido".
            </p>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
