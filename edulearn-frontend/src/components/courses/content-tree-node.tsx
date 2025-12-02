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
} from "lucide-react"
import { Button } from "@/components/ui/button"

interface ContentItem {
  id: string
  name: string
  type: "VIDEO" | "PDF" | "LECTURE" | "TASK" | "QUIZ" | "SUPPLEMENT" | "FORM" | "PRACTICE"
  duration: number
  file?: string
  isCompleted?: boolean
  size?: string // Para materiales grandes
}

interface TreeNode {
  id: string
  name: string
  type: "module" | "submodule" | "lesson" | "activity"
  status?: "completed" | "in-progress" | "locked"
  children?: TreeNode[]
  completedItems?: number
  totalItems?: number
  content?: ContentItem[]
}

interface ContentTreeNodeProps {
  contentItem: TreeNode
  level?: number
  role: "ESTUDIANTE" | "DOCENTE" | "ADMIN"
  onEditModule?: (nodeId: string) => void
}

export function ContentTreeNode({ contentItem, level = 0, role, onEditModule }: ContentTreeNodeProps) {
  const [isExpanded, setIsExpanded] = useState(level < 2)

  // ═══════════════════════════════════════════════════════════════
  // ESTADO PARA LAZY LOADING EN UI
  // ═══════════════════════════════════════════════════════════════
  const [isContentLoaded, setIsContentLoaded] = useState(false)
  const [isLoading, setIsLoading] = useState(false)

  const hasChildren = contentItem.children && contentItem.children.length > 0
  const hasContent = contentItem.content && contentItem.content.length > 0

  // ═══════════════════════════════════════════════════════════════
  // DETERMINAR SI EL NODO TIENE MATERIALES GRANDES (LAZY LOAD)
  // ═══════════════════════════════════════════════════════════════
  const hasLargeContent =
    hasContent &&
    contentItem.content?.some((item) => item.type === "VIDEO" || item.type === "PDF" || item.size)

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
  // FUNCIÓN: Cargar contenido pesado (LAZY LOADING EN UI)
  // Simula el delay de carga del backend
  // ═══════════════════════════════════════════════════════════════
  const handleLoadContent = async () => {
    console.log("🔷 [ContentTreeNode] Iniciando carga de contenido pesado...")
    console.log("📦 [ContentTreeNode] Módulo:", contentItem.name)
    console.log("👤 [ContentTreeNode] Rol:", role)

    setIsLoading(true)

    // Simular delay de carga (como si llamáramos al backend con el patrón Proxy)
    await new Promise((resolve) => setTimeout(resolve, 1500))

    setIsContentLoaded(true)
    setIsLoading(false)

    console.log("✅ [ContentTreeNode] Contenido cargado exitosamente")
  }

  // ═══════════════════════════════════════════════════════════════
  // RENDERIZAR ITEM DE CONTENIDO INDIVIDUAL
  // ═══════════════════════════════════════════════════════════════
  const renderContentItem = (item: ContentItem) => {
    const isLarge = item.type === "VIDEO" || item.type === "PDF" || item.size

    const getContentIcon = () => {
      switch (item.type) {
        case "VIDEO":
          return <Play className="w-4 h-4 text-blue-500" />
        case "PDF":
          return <FileIcon className="w-4 h-4 text-red-500" />
        case "LECTURE":
          return <FileText className="w-4 h-4 text-purple-500" />
        case "QUIZ":
          return <CheckCircle2 className="w-4 h-4 text-orange-500" />
        default:
          return <Circle className="w-4 h-4 text-gray-500" />
      }
    }

    return (
      <div
        key={item.id}
        className="ml-8 py-2 px-3 rounded-lg hover:bg-muted/30 border border-border/30 mb-2"
      >
        <div className="flex items-center gap-2">
          {getContentIcon()}
          <span className={`text-sm flex-1 ${item.isCompleted ? "text-green-600 line-through" : ""}`}>
            {item.name}
          </span>
          <span className="text-xs text-muted-foreground">{item.duration} min</span>
          {isLarge && item.size && (
            <span className="text-xs bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-300 px-2 py-0.5 rounded">
              {item.size}
            </span>
          )}
        </div>

        {/* Simular el visor de contenido para items grandes (VIDEO/PDF) */}
        {isLarge && (
          <div className="mt-2 p-4 bg-gray-100 dark:bg-gray-800 border border-gray-300 dark:border-gray-700 rounded">
            {item.type === "VIDEO" && (
              <div className="text-center py-6">
                <Play className="w-12 h-12 mx-auto mb-2 text-blue-500" />
                <p className="text-sm text-muted-foreground">Video Player Simulado</p>
                <p className="text-xs text-muted-foreground mt-1">"{item.name}"</p>
              </div>
            )}
            {item.type === "PDF" && (
              <div className="text-center py-6">
                <FileIcon className="w-12 h-12 mx-auto mb-2 text-red-500" />
                <p className="text-sm text-muted-foreground">PDF Viewer Simulado</p>
                <p className="text-xs text-muted-foreground mt-1">"{item.name}"</p>
              </div>
            )}
          </div>
        )}
      </div>
    )
  }

  return (
    <div className="select-none">
      {/* ═══════════════════════════════════════════════════════════════ */}
      {/* NODO PRINCIPAL (Módulo/Submódulo/Lección)                        */}
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
        {/* BOTÓN DE EDICIÓN (Solo visible para DOCENTE/ADMIN en hover)      */}
        {/* ═══════════════════════════════════════════════════════════════ */}
        {canEdit && (
          <Button
            size="sm"
            variant="ghost"
            onClick={(e) => {
              e.stopPropagation()
              onEditModule?.(contentItem.id)
            }}
            className="opacity-0 group-hover:opacity-100 transition-opacity"
            title="Editar módulo"
          >
            <Edit2 className="w-4 h-4" />
          </Button>
        )}
      </div>

      {/* ═══════════════════════════════════════════════════════════════ */}
      {/* SECCIÓN DE CONTENIDO (Con Lazy Loading en UI)                    */}
      {/* ═══════════════════════════════════════════════════════════════ */}
      {isExpanded && hasContent && (
        <div className="ml-6 mt-2 mb-3 border-l-2 border-border/50 pl-4">
          {!isContentLoaded ? (
            /* ESTADO INICIAL: Mostrar botón "Cargar Contenido" */
            <div className="bg-muted/30 border border-border/50 rounded-lg p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-foreground">
                    Contenido del módulo ({contentItem.content?.length} items)
                  </p>
                  <p className="text-xs text-muted-foreground mt-1">
                    {hasLargeContent
                      ? "⚠️ Incluye materiales pesados (videos/PDFs)"
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
                <span className="font-medium">Contenido cargado (Lazy Loading completado)</span>
              </div>
              {contentItem.content?.map((item) => renderContentItem(item))}
            </div>
          )}
        </div>
      )}

      {/* ═══════════════════════════════════════════════════════════════ */}
      {/* CHILDREN RECURSIVOS (Submódulos, Lecciones)                      */}
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
            />
          ))}
        </div>
      )}
    </div>
  )
}
