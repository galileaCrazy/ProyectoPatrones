"use client"
import { ContentTreeNode } from "./content-tree-node"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Book, CheckCircle2, Circle } from "lucide-react"
import { ModuleDetailView } from "./module-detail-view"
import { useState } from "react"

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
  content?: {
    id: string
    name: string
    type: "video" | "lecture" | "task" | "quiz" | "supplement"
    duration: number
    isCompleted?: boolean
  }[]
}

interface CourseContentTreeProps {
  courseId: string
  onNodeSelect?: (node: TreeNode) => void
}

export function CourseContentTree({ courseId, onNodeSelect }: CourseContentTreeProps) {
  const [selectedModule, setSelectedModule] = useState<TreeNode | null>(null)

  const courseContent: TreeNode[] = [
    {
      id: "m1",
      name: "Introducción a POO",
      type: "module",
      status: "completed",
      completedItems: 3,
      totalItems: 3,
      estimatedTime: 3,
      description:
        "En este módulo aprenderemos los conceptos fundamentales de la Programación Orientada a Objetos (POO), incluyendo qué es una clase, cómo funcionan los objetos y los pilares principales. Este módulo proporciona la base teórica necesaria para entender el resto del curso.",
      content: [
        { id: "c1", name: "Introducción al Programa", type: "video", duration: 5, isCompleted: true },
        { id: "c2", name: "¿Qué es la POO?", type: "lecture", duration: 3, isCompleted: true },
        {
          id: "c3",
          name: "¿Qué hace un Especialista en Asistencia de TI?",
          type: "video",
          duration: 2,
          isCompleted: true,
        },
        { id: "c4", name: "Introducción al Curso", type: "lecture", duration: 1, isCompleted: true },
        {
          id: "c5",
          name: "Empieza con tu certificado de Asistencia de TI de Google",
          type: "task",
          duration: 2,
          isCompleted: true,
        },
        { id: "c6", name: "Del ábaco al motor analítico", type: "video", duration: 5, isCompleted: true },
        {
          id: "c7",
          name: "La ruta de acceso a las computadoras modernas",
          type: "lecture",
          duration: 10,
          isCompleted: true,
        },
        { id: "c8", name: "Kevin: Su ruta de acceso", type: "video", duration: 1, isCompleted: true },
        { id: "c9", name: "Lenguaje informático", type: "quiz", duration: 2, isCompleted: true },
        { id: "c10", name: "Material complementario", type: "supplement", duration: 5, isCompleted: true },
      ],
      children: [
        {
          id: "s1",
          name: "Conceptos Fundamentales",
          type: "submodule",
          status: "completed",
          completedItems: 2,
          totalItems: 2,
          children: [
            {
              id: "l1",
              name: "Definición de POO",
              type: "lesson",
              status: "completed",
              children: [
                { id: "a1", name: "Lectura: Introducción a POO", type: "activity", status: "completed" },
                { id: "a2", name: "Video: Conceptos básicos", type: "activity", status: "completed" },
              ],
            },
            {
              id: "l2",
              name: "Pilares de POO",
              type: "lesson",
              status: "completed",
              children: [
                { id: "a3", name: "Lectura: Los 4 pilares", type: "activity", status: "completed" },
                { id: "a4", name: "Quiz: Pilares de POO", type: "activity", status: "completed" },
              ],
            },
          ],
        },
        {
          id: "s2",
          name: "Primeras Clases",
          type: "submodule",
          status: "completed",
          completedItems: 1,
          totalItems: 1,
          children: [
            {
              id: "l3",
              name: "Creando tu Primera Clase",
              type: "lesson",
              status: "completed",
              children: [{ id: "a5", name: "Ejercicio práctico", type: "activity", status: "completed" }],
            },
          ],
        },
      ],
    },
    {
      id: "m2",
      name: "Clases y Objetos",
      type: "module",
      status: "in-progress",
      completedItems: 3,
      totalItems: 4,
      children: [
        {
          id: "s3",
          name: "Atributos y Métodos",
          type: "submodule",
          status: "in-progress",
          completedItems: 2,
          totalItems: 3,
          children: [
            {
              id: "l4",
              name: "Definiendo Atributos",
              type: "lesson",
              status: "completed",
              children: [
                { id: "a6", name: "Lectura: Propiedades de clases", type: "activity", status: "completed" },
                { id: "a7", name: "Ejercicio: Crear atributos", type: "activity", status: "completed" },
              ],
            },
            {
              id: "l5",
              name: "Métodos de Clase",
              type: "lesson",
              status: "in-progress",
              children: [
                { id: "a8", name: "Lectura: Introducción a métodos", type: "activity", status: "completed" },
                { id: "a9", name: "Video: Métodos avanzados", type: "activity", status: "in-progress" },
              ],
            },
            {
              id: "l6",
              name: "Constructores y Destructores",
              type: "lesson",
              status: "locked",
              children: [{ id: "a10", name: "Lectura: Constructores", type: "activity", status: "locked" }],
            },
          ],
        },
      ],
    },
    {
      id: "m3",
      name: "Herencia y Polimorfismo",
      type: "module",
      status: "locked",
      completedItems: 0,
      totalItems: 3,
      children: [
        {
          id: "s4",
          name: "Concepto de Herencia",
          type: "submodule",
          status: "locked",
          children: [
            {
              id: "l7",
              name: "Introducción a Herencia",
              type: "lesson",
              status: "locked",
            },
          ],
        },
      ],
    },
  ]

  if (selectedModule && selectedModule.content) {
    return (
      <ModuleDetailView
        module={{
          id: selectedModule.id,
          title: selectedModule.name,
          moduleNumber: Number.parseInt(selectedModule.id.replace("m", "")),
          estimatedTime: selectedModule.estimatedTime || 3,
          description: selectedModule.description || "",
          content: selectedModule.content || [],
        }}
        onBack={() => setSelectedModule(null)}
      />
    )
  }

  return (
    <Card className="border-border/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Book className="w-5 h-5 text-primary" />
          Contenido del Curso - Vista Jerárquica
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-1">
          {courseContent.map((node) => (
            <ContentTreeNode key={node.id} node={node} level={0} onNodeClick={(node) => setSelectedModule(node)} />
          ))}
        </div>

        {/* Legend */}
        <div className="mt-6 pt-6 border-t border-border/50 flex flex-wrap gap-6">
          <div className="flex items-center gap-2 text-sm">
            <CheckCircle2 className="w-4 h-4 text-green-500" />
            <span className="text-muted-foreground">Completado</span>
          </div>
          <div className="flex items-center gap-2 text-sm">
            <Circle className="w-4 h-4 text-blue-500" />
            <span className="text-muted-foreground">En Progreso</span>
          </div>
          <div className="flex items-center gap-2 text-sm">
            <Circle className="w-4 h-4 text-gray-400" />
            <span className="text-muted-foreground">Bloqueado</span>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
