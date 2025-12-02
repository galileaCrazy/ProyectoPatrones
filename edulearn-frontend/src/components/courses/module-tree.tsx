"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Book, AlertCircle } from "lucide-react"
import { ContentTreeNode } from "./content-tree-node"
import { ModuleDetailView } from "./module-detail-view"

interface TreeNode {
  id: number
  nombre: string
  tipo: string
  descripcion?: string
  orden: number
  duracionTotal: number
  esHoja: boolean
  hijos?: TreeNode[]

  // Campos de módulo
  duracionEstimada?: number
  estado?: string
  cantidadElementos?: number

  // Campos de material
  tipoMaterial?: string
  urlRecurso?: string
  archivoPath?: string
  duracionSegundos?: number
  esObligatorio?: boolean

  // Campos de evaluación
  tipoEvaluacion?: string
  puntajeMaximo?: number
  tiempoLimiteMinutos?: number
  intentosPermitidos?: number
}

interface ModuleTreeProps {
  cursoId: number
  onNodeSelect?: (node: TreeNode) => void
}

export function ModuleTree({ cursoId, onNodeSelect }: ModuleTreeProps) {
  const [treeData, setTreeData] = useState<TreeNode[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedModule, setSelectedModule] = useState<TreeNode | null>(null)

  useEffect(() => {
    const fetchTreeData = async () => {
      try {
        setLoading(true)
        setError(null)

        const response = await fetch(`http://localhost:8080/api/modulos/tree?cursoId=${cursoId}`)

        if (!response.ok) {
          const errorText = await response.text()
          console.error("Server error:", errorText)
          throw new Error(`Error del servidor: ${response.status}`)
        }

        const data = await response.json()

        if (data.success) {
          // Aceptar array vacío como válido
          setTreeData(data.modulos || [])
        } else {
          setError(data.error || "Error al cargar el árbol del curso")
        }
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error desconocido al cargar datos")
        console.error("Error fetching tree data:", err)
      } finally {
        setLoading(false)
      }
    }

    // Solo hacer fetch si cursoId es válido (mayor a 0)
    if (cursoId && cursoId > 0) {
      fetchTreeData()
    } else {
      setLoading(false)
      setError("ID de curso inválido")
    }
  }, [cursoId])

  const handleNodeClick = (node: TreeNode) => {
    setSelectedModule(node)
    onNodeSelect?.(node)
  }

  // Si hay un módulo seleccionado con contenido, mostrar la vista detallada
  if (selectedModule && !selectedModule.esHoja && selectedModule.hijos && selectedModule.hijos.length > 0) {
    return (
      <ModuleDetailView
        module={{
          id: selectedModule.id.toString(),
          title: selectedModule.nombre,
          moduleNumber: selectedModule.orden,
          estimatedTime: Math.ceil((selectedModule.duracionTotal || 0) / 60),
          description: selectedModule.descripcion || "",
          content: convertirHijosAContenido(selectedModule.hijos),
        }}
        onBack={() => setSelectedModule(null)}
      />
    )
  }

  if (loading) {
    return (
      <Card className="border-border/50">
        <CardContent className="p-8 text-center">
          <div className="flex items-center justify-center gap-2">
            <div className="w-5 h-5 border-2 border-primary border-t-transparent rounded-full animate-spin" />
            <span className="text-muted-foreground">Cargando árbol del curso...</span>
          </div>
        </CardContent>
      </Card>
    )
  }

  if (error) {
    return (
      <Card className="border-border/50">
        <CardContent className="p-8">
          <div className="flex items-center gap-3 text-destructive">
            <AlertCircle className="w-5 h-5" />
            <div>
              <p className="font-medium">Error al cargar el contenido</p>
              <p className="text-sm text-muted-foreground">{error}</p>
            </div>
          </div>
        </CardContent>
      </Card>
    )
  }

  if (treeData.length === 0) {
    return (
      <Card className="border-border/50">
        <CardContent className="p-8 text-center">
          <Book className="w-12 h-12 mx-auto mb-4 text-muted-foreground opacity-50" />
          <p className="text-muted-foreground">
            Este curso aún no tiene módulos configurados
          </p>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="border-border/50">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Book className="w-5 h-5 text-primary" />
          Estructura del Curso (Patrón Composite)
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-1">
          {treeData.map((node) => (
            <ContentTreeNode
              key={node.id}
              node={convertirNodoATreeNode(node)}
              level={0}
              onNodeClick={(n) => handleNodeClick(convertirTreeNodeANodo(n))}
            />
          ))}
        </div>

        {/* Estadísticas */}
        <div className="mt-6 pt-6 border-t border-border/50">
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
            <div>
              <p className="text-muted-foreground">Total Módulos</p>
              <p className="text-lg font-semibold text-foreground">{treeData.length}</p>
            </div>
            <div>
              <p className="text-muted-foreground">Duración Total</p>
              <p className="text-lg font-semibold text-foreground">
                {Math.ceil(treeData.reduce((acc, m) => acc + (m.duracionTotal || 0), 0) / 60)}h
              </p>
            </div>
            <div>
              <p className="text-muted-foreground">Elementos</p>
              <p className="text-lg font-semibold text-foreground">
                {treeData.reduce((acc, m) => acc + (m.cantidadElementos || 0), 0)}
              </p>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

// Función auxiliar para convertir el formato del backend al formato del frontend
function convertirNodoATreeNode(node: TreeNode): any {
  const status = node.estado === "published" ? "completed" :
                 node.estado === "draft" ? "in-progress" : "locked"

  return {
    id: node.id.toString(),
    name: node.nombre,
    type: mapearTipo(node.tipo),
    status,
    description: node.descripcion,
    children: node.hijos?.map(convertirNodoATreeNode),
    completedItems: 0,
    totalItems: node.cantidadElementos || node.hijos?.length || 0,
    estimatedTime: Math.ceil((node.duracionTotal || 0) / 60),
    content: node.hijos && node.hijos.length > 0 ? convertirHijosAContenido(node.hijos) : undefined,
  }
}

function convertirTreeNodeANodo(treeNode: any): TreeNode {
  return {
    id: parseInt(treeNode.id),
    nombre: treeNode.name,
    tipo: treeNode.type,
    descripcion: treeNode.description,
    orden: 0,
    duracionTotal: (treeNode.estimatedTime || 0) * 60,
    esHoja: !treeNode.children || treeNode.children.length === 0,
    hijos: treeNode.children?.map(convertirTreeNodeANodo),
    duracionEstimada: treeNode.estimatedTime,
    cantidadElementos: treeNode.totalItems,
  }
}

function mapearTipo(tipo: string): "module" | "submodule" | "lesson" | "activity" {
  const tipoLower = tipo.toLowerCase()

  if (tipoLower.includes("modulo") || tipoLower === "module") return "module"
  if (tipoLower.includes("submodulo") || tipoLower === "submodule") return "submodule"
  if (tipoLower.includes("leccion") || tipoLower === "lesson") return "lesson"

  return "activity"
}

function convertirHijosAContenido(hijos: TreeNode[]): any[] {
  return hijos.map((hijo, index) => ({
    id: hijo.id.toString(),
    name: hijo.nombre,
    type: mapearTipoContenido(hijo),
    duration: Math.ceil((hijo.duracionTotal || hijo.duracionSegundos || 0) / 60),
    isCompleted: hijo.estado === "published" || false,
  }))
}

function mapearTipoContenido(node: TreeNode): "video" | "lecture" | "task" | "quiz" | "supplement" {
  if (node.tipo === "MATERIAL") {
    const tipoMaterial = node.tipoMaterial?.toUpperCase()
    if (tipoMaterial === "VIDEO") return "video"
    if (tipoMaterial === "PDF" || tipoMaterial === "DOCUMENTO") return "lecture"
    if (tipoMaterial === "LINK") return "supplement"
    return "supplement"
  }

  if (node.tipo === "EVALUACION") {
    const tipoEval = node.tipoEvaluacion?.toUpperCase()
    if (tipoEval === "QUIZ") return "quiz"
    if (tipoEval === "TAREA" || tipoEval === "PROYECTO") return "task"
    return "quiz"
  }

  return "lecture"
}
