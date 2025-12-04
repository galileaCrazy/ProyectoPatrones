"use client"

import { useState } from "react"
import {
  ChevronRight,
  Book,
  FileText,
  CheckCircle2,
  Circle,
  Edit2,
  Lock,
  Play,
  FileIcon,
  Loader2,
  Award,
  BookOpen,
  CheckSquare,
  Eye,
  Sparkles,
} from "lucide-react"
import { Button } from "@/components/ui/button"

interface ContentItem {
  id: string
  name: string
  type: "VIDEO" | "PDF" | "LECTURE" | "TASK" | "QUIZ" | "SUPPLEMENT" | "FORM" | "PRACTICE"
  duration: number
  file?: string | null
  isCompleted?: boolean
  size?: string | number // Para materiales grandes (ej: "850 MB" o tamaño en bytes)
  materialId?: number // ID en la BD para lazy loading con Proxy
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

interface ContentTreeNodeProps {
  contentItem: TreeNode
  level?: number
  role: "ESTUDIANTE" | "DOCENTE" | "ADMIN"
  onEditModule?: (nodeId: string) => void
  onMaterialClick?: (material: ContentItem) => void
  onOpenDecorators?: (nodeId: string) => void
}

export function ContentTreeNode({
  contentItem,
  level = 0,
  role,
  onEditModule,
  onMaterialClick,
  onOpenDecorators,
}: ContentTreeNodeProps) {
  const [isExpanded, setIsExpanded] = useState(level < 2)

  // ═══════════════════════════════════════════════════════════════
  // ESTADO PARA LAZY LOADING EN UI (Metadata vs Contenido Real)
  // ═══════════════════════════════════════════════════════════════
  const [isContentLoaded, setIsContentLoaded] = useState(false)
  const [isLoading, setIsLoading] = useState(false)

  const hasChildren = contentItem.children && contentItem.children.length > 0
  const hasContent = contentItem.content && contentItem.content.length > 0

  // ═══════════════════════════════════════════════════════════════
  // DETERMINAR SI EL NODO TIENE MATERIALES PESADOS
  // Videos, PDFs y archivos grandes requieren Proxy con lazy loading
  // ═══════════════════════════════════════════════════════════════
  const hasLargeContent =
    hasContent && contentItem.content?.some((item) => item.type === "VIDEO" || item.type === "PDF" || item.size)

  const getIcon = () => {
    switch (contentItem.type) {
      case "module":
        return <Book className="w-5 h-5 text-primary" />
      case "submodule":
        return <FileText className="w-5 h-5 text-primary/70" />
      case "lesson":
        return <FileText className="w-5 h-5 text-accent" />
      case "activity":
        return <Circle className="w-4 h-4 text-muted-foreground" />
      default:
        return null
    }
  }

  const getStatusColor = () => {
    switch (contentItem.status) {
      case "completed":
        return "text-green-500"
      case "in-progress":
        return "text-blue-500"
      case "locked":
        return "text-gray-400"
      default:
        return "text-foreground"
    }
  }

  const getStatusIcon = () => {
    switch (contentItem.status) {
      case "completed":
        return <CheckCircle2 className={`w-4 h-4 ${getStatusColor()}`} />
      case "locked":
        return <Lock className={`w-4 h-4 ${getStatusColor()}`} />
      default:
        return null
    }
  }

  const progress =
    contentItem.completedItems && contentItem.totalItems
      ? Math.round((contentItem.completedItems / contentItem.totalItems) * 100)
      : null

  // ═══════════════════════════════════════════════════════════════
  // CONTROL DE ACCESO: Botón de edición solo para DOCENTE/ADMIN
  // ═══════════════════════════════════════════════════════════════
  const canEdit = (role === "DOCENTE" || role === "ADMIN") && contentItem.type === "module"

  // ═══════════════════════════════════════════════════════════════
  // FUNCIÓN: Cargar contenido pesado (LAZY LOADING)
  // En producción, aquí se llamaría al endpoint del Proxy
  // ═══════════════════════════════════════════════════════════════
  const handleLoadContent = async () => {
    console.log("🔷 [ContentTreeNode] Iniciando lazy loading de contenido...")
    console.log("📦 [ContentTreeNode] Módulo:", contentItem.name)
    console.log("👤 [ContentTreeNode] Rol:", role)

    setIsLoading(true)

    // Simular llamada al backend con Proxy
    // En producción: await obtenerMaterialesModuloConProxy({moduloId, usuarioId, rolUsuario})
    await new Promise((resolve) => setTimeout(resolve, 800))

    setIsContentLoaded(true)
    setIsLoading(false)

    console.log("✅ [ContentTreeNode] Contenido cargado exitosamente")
  }

  // ═══════════════════════════════════════════════════════════════
  // FUNCIÓN: Obtener ícono del tipo de contenido
  // ═══════════════════════════════════════════════════════════════
  const getContentIcon = (type: string) => {
    switch (type) {
      case "VIDEO":
        return <Play className="w-4 h-4 text-blue-500" />
      case "PDF":
        return <FileIcon className="w-4 h-4 text-red-500" />
      case "LECTURE":
        return <BookOpen className="w-4 h-4 text-purple-500" />
      case "TASK":
        return <CheckSquare className="w-4 h-4 text-orange-500" />
      case "QUIZ":
        return <Award className="w-4 h-4 text-green-500" />
      case "SUPPLEMENT":
        return <Eye className="w-4 h-4 text-cyan-500" />
      default:
        return <Circle className="w-4 h-4 text-gray-500" />
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZAR ITEM DE CONTENIDO INDIVIDUAL
  // Cada item puede ser clickeado para abrir el MaterialViewer
  // ═══════════════════════════════════════════════════════════════
  const renderContentItem = (item: ContentItem) => {
    const isLarge = item.type === "VIDEO" || item.type === "PDF" || item.size

    return (
      <button
        key={item.id}
        onClick={() => onMaterialClick?.(item)}
        className="w-full ml-8 py-2 px-3 rounded-lg hover:bg-muted/50 border border-border/30 mb-2 transition-colors text-left group"
      >
        <div className="flex items-center gap-3">
          {getContentIcon(item.type)}
          <span
            className={`text-sm flex-1 ${
              item.isCompleted ? "text-green-600 dark:text-green-400 line-through" : "text-foreground"
            } group-hover:text-primary transition-colors`}
          >
            {item.name}
          </span>
          <span className="text-xs text-muted-foreground">{item.duration} min</span>
          {isLarge && item.size && (
            <span className="text-xs bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-300 px-2 py-0.5 rounded font-medium">
              {item.size}
            </span>
          )}
          {item.isCompleted && <CheckCircle2 className="w-4 h-4 text-green-500" />}
        </div>
      </button>
    )
  }

  return (
    <div className="select-none">
      {/* ═══════════════════════════════════════════════════════════════ */}
      {/* NODO PRINCIPAL (Módulo/Submódulo/Lección/Actividad)              */}
      {/* ═══════════════════════════════════════════════════════════════ */}
      <div
        className="flex items-center gap-2 py-2 px-3 rounded-lg hover:bg-muted/50 cursor-pointer transition-colors group"
        style={{ paddingLeft: `${level * 1.5 + 0.75}rem` }}
        onClick={() => {
          if (hasChildren || hasContent) {
            setIsExpanded(!isExpanded)
          }
        }}
      >
        {/* Expand/Collapse Button */}
        {(hasChildren || hasContent) && (
          <button
            onClick={(e) => {
              e.stopPropagation()
              setIsExpanded(!isExpanded)
            }}
            className="flex-shrink-0 p-0 hover:bg-muted rounded transition-colors"
          >
            <ChevronRight
              className={`w-4 h-4 text-muted-foreground transition-transform ${isExpanded ? "rotate-90" : ""}`}
            />
          </button>
        )}
        {!hasChildren && !hasContent && <div className="w-4 flex-shrink-0" />}

        {/* Icon */}
        <div className="flex-shrink-0">{getIcon()}</div>

        {/* Content */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2">
            <span className={`font-medium truncate ${getStatusColor()}`}>{contentItem.name}</span>
            {getStatusIcon()}
          </div>
          {progress !== null && (
            <div className="mt-1 flex items-center gap-2">
              <div className="flex-1 h-1.5 rounded-full bg-muted overflow-hidden max-w-xs">
                <div
                  className="h-full bg-gradient-to-r from-primary to-accent transition-all"
                  style={{ width: `${progress}%` }}
                />
              </div>
              <span className="text-xs text-muted-foreground whitespace-nowrap">{progress}%</span>
            </div>
          )}
        </div>

        {/* ═══════════════════════════════════════════════════════════════ */}
        {/* BOTONES DE ACCIÓN (Solo visible para DOCENTE/ADMIN en hover)     */}
        {/* ═══════════════════════════════════════════════════════════════ */}
        {canEdit && (
          <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
            <Button
              size="sm"
              variant="ghost"
              onClick={(e) => {
                e.stopPropagation()
                onOpenDecorators?.(contentItem.id)
              }}
              className="h-8 w-8 p-0"
              title="Extender funcionalidad (Patrón Decorator)"
            >
              <Sparkles className="w-4 h-4 text-purple-500" />
            </Button>
            <Button
              size="sm"
              variant="ghost"
              onClick={(e) => {
                e.stopPropagation()
                onEditModule?.(contentItem.id)
              }}
              className="h-8 w-8 p-0"
              title="Editar módulo"
            >
              <Edit2 className="w-4 h-4" />
            </Button>
          </div>
        )}
      </div>

      {/* ═══════════════════════════════════════════════════════════════ */}
      {/* SECCIÓN DE CONTENIDO (Con Lazy Loading del Patrón Proxy)         */}
      {/* ═══════════════════════════════════════════════════════════════ */}
      {isExpanded && hasContent && (
        <div className="ml-6 mt-2 mb-3 border-l-2 border-border/50 pl-4">
          {!isContentLoaded ? (
            /* ESTADO INICIAL: Mostrar botón "Cargar Contenido" (Proxy) */
            <div className="bg-muted/30 border border-border/50 rounded-lg p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-foreground">
                    Contenido del módulo ({contentItem.content?.length} items)
                  </p>
                  <p className="text-xs text-muted-foreground mt-1">
                    {hasLargeContent
                      ? "💡 Incluye materiales pesados (videos/PDFs) - Lazy Loading"
                      : "Materiales de lectura y evaluaciones"}
                  </p>
                </div>
                <Button
                  size="sm"
                  onClick={(e) => {
                    e.stopPropagation()
                    handleLoadContent()
                  }}
                  disabled={isLoading}
                  className="gap-2"
                >
                  {isLoading ? (
                    <>
                      <Loader2 className="w-4 h-4 animate-spin" />
                      Cargando...
                    </>
                  ) : (
                    <>
                      <Play className="w-4 h-4" />
                      Cargar Contenido
                    </>
                  )}
                </Button>
              </div>
            </div>
          ) : (
            /* ESTADO CARGADO: Mostrar contenido real */
            <div>
              <div className="mb-3 flex items-center gap-2 text-sm text-green-600 dark:text-green-400">
                <CheckCircle2 className="w-4 h-4" />
                <span className="font-medium">Contenido cargado (Patrón Proxy - Lazy Loading)</span>
              </div>
              {contentItem.content?.map((item) => renderContentItem(item))}
            </div>
          )}
        </div>
      )}

      {/* ═══════════════════════════════════════════════════════════════ */}
      {/* CHILDREN RECURSIVOS (Patrón Composite)                           */}
      {/* Submódulos, Lecciones, Actividades se renderizan recursivamente  */}
      {/* ═══════════════════════════════════════════════════════════════ */}
      {hasChildren && isExpanded && (
        <div>
          {contentItem.children!.map((child) => (
            <ContentTreeNode
              key={child.id}
              contentItem={child}
              level={level + 1}
              role={role}
              onEditModule={onEditModule}
              onMaterialClick={onMaterialClick}
              onOpenDecorators={onOpenDecorators}
            />
          ))}
        </div>
      )}
    </div>
  )
}
