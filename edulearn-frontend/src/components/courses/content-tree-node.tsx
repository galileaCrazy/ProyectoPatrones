"use client"

import { useState } from "react"
import { ChevronRight, Book, FileText, CheckCircle2, Circle } from "lucide-react"

interface TreeNode {
  id: string
  name: string
  type: "module" | "submodule" | "lesson" | "activity"
  status?: "completed" | "in-progress" | "locked"
  children?: TreeNode[]
  completedItems?: number
  totalItems?: number
}

interface ContentTreeNodeProps {
  node: TreeNode
  level?: number
  onNodeClick?: (node: TreeNode) => void
}

export function ContentTreeNode({ node, level = 0, onNodeClick }: ContentTreeNodeProps) {
  const [isExpanded, setIsExpanded] = useState(level < 2)
  const hasChildren = node.children && node.children.length > 0

  const getIcon = () => {
    switch (node.type) {
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
    switch (node.status) {
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
    switch (node.status) {
      case "completed":
        return <CheckCircle2 className={`w-4 h-4 ${getStatusColor()}`} />
      default:
        return null
    }
  }

  const progress =
    node.completedItems && node.totalItems ? Math.round((node.completedItems / node.totalItems) * 100) : null

  return (
    <div className="select-none">
      <div
        className="flex items-center gap-2 py-2 px-3 rounded-lg hover:bg-muted/50 cursor-pointer transition-colors group"
        style={{ paddingLeft: `${level * 1.5 + 0.75}rem` }}
        onClick={() => onNodeClick?.(node)}
      >
        {/* Expand/Collapse Button */}
        {hasChildren && (
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
        {!hasChildren && <div className="w-4 flex-shrink-0" />}

        {/* Icon */}
        <div className="flex-shrink-0">{getIcon()}</div>

        {/* Content */}
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2">
            <span className={`font-medium truncate ${getStatusColor()}`}>{node.name}</span>
            {getStatusIcon()}
          </div>
          {progress !== null && (
            <div className="mt-1 flex items-center gap-2">
              <div className="flex-1 h-1.5 rounded-full bg-muted overflow-hidden">
                <div
                  className="h-full bg-gradient-to-r from-primary to-accent transition-all"
                  style={{ width: `${progress}%` }}
                />
              </div>
              <span className="text-xs text-muted-foreground whitespace-nowrap">{progress}%</span>
            </div>
          )}
        </div>
      </div>

      {/* Children */}
      {hasChildren && isExpanded && (
        <div>
          {node.children!.map((child) => (
            <ContentTreeNode key={child.id} node={child} level={level + 1} onNodeClick={onNodeClick} />
          ))}
        </div>
      )}
    </div>
  )
}
