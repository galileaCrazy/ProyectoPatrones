"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import {
  ChevronDown,
  ChevronRight,
  Play,
  BookOpen,
  CheckSquare,
  Award,
  Clock,
  Eye,
  EyeOff,
  ArrowLeft,
} from "lucide-react"

interface ContentItem {
  id: string
  name: string
  type: "video" | "lecture" | "task" | "quiz" | "supplement"
  duration: number // en minutos
  isCompleted?: boolean
}

interface ModuleContent {
  id: string
  title: string
  moduleNumber: number
  estimatedTime: number // en horas
  description: string
  content: ContentItem[]
}

interface ModuleDetailViewProps {
  module: ModuleContent
  onBack?: () => void
}

const getTypeIcon = (type: string) => {
  const iconClass = "w-5 h-5"
  switch (type) {
    case "video":
      return <Play className={`${iconClass} text-blue-500`} />
    case "lecture":
      return <BookOpen className={`${iconClass} text-purple-500`} />
    case "task":
      return <CheckSquare className={`${iconClass} text-orange-500`} />
    case "quiz":
      return <Award className={`${iconClass} text-red-500`} />
    case "supplement":
      return <Eye className={`${iconClass} text-green-500`} />
    default:
      return <Eye className={`${iconClass} text-gray-500`} />
  }
}

const getTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    video: "Video",
    lecture: "Lectura",
    task: "Tarea",
    quiz: "Quiz",
    supplement: "Complemento",
  }
  return labels[type] || type
}

export function ModuleDetailView({ module, onBack }: ModuleDetailViewProps) {
  const [showDetails, setShowDetails] = useState(true)
  const [expandedItems, setExpandedItems] = useState<Set<string>>(new Set())

  // Contar contenidos por tipo
  const contentCounts = {
    videos: module.content.filter((c) => c.type === "video").length,
    lectures: module.content.filter((c) => c.type === "lecture").length,
    tasks: module.content.filter((c) => c.type === "task").length,
    quizzes: module.content.filter((c) => c.type === "quiz").length,
    supplements: module.content.filter((c) => c.type === "supplement").length,
  }

  // Calcular tiempo total
  const totalDuration = module.content.reduce((sum, item) => sum + item.duration, 0)

  // Agrupar contenido por tipo
  const groupedContent = {
    videos: module.content.filter((c) => c.type === "video"),
    lectures: module.content.filter((c) => c.type === "lecture"),
    tasks: module.content.filter((c) => c.type === "task"),
    quizzes: module.content.filter((c) => c.type === "quiz"),
    supplements: module.content.filter((c) => c.type === "supplement"),
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header con botón atrás */}
      <div className="flex items-center justify-between gap-4">
        {onBack && (
          <Button variant="ghost" size="sm" onClick={onBack} className="gap-2">
            <ArrowLeft className="w-4 h-4" />
            Volver al Curso
          </Button>
        )}
      </div>

      {/* Título Principal */}
      <div className="space-y-3">
        <div className="flex items-start justify-between gap-4">
          <div className="flex-1">
            <h1 className="text-3xl font-bold text-foreground">{module.title}</h1>
            <p className="text-sm text-muted-foreground mt-1">
              Módulo {module.moduleNumber} • {module.estimatedTime} horas para finalizar
            </p>
          </div>
          <Button
            variant="outline"
            size="sm"
            onClick={() => setShowDetails(!showDetails)}
            className="gap-2 whitespace-nowrap"
          >
            {showDetails ? (
              <>
                <EyeOff className="w-4 h-4" />
                Ocultar detalles
              </>
            ) : (
              <>
                <Eye className="w-4 h-4" />
                Ver detalles
              </>
            )}
          </Button>
        </div>

        {/* Descripción */}
        <p className="text-foreground leading-relaxed text-base">{module.description}</p>
      </div>

      {/* Qué incluye */}
      <Card className="border-border/50">
        <CardHeader className="pb-3">
          <h2 className="text-lg font-semibold text-foreground">Qué incluye</h2>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
            {contentCounts.videos > 0 && (
              <div className="flex items-center gap-2">
                <Play className="w-5 h-5 text-blue-500" />
                <div>
                  <p className="font-semibold text-foreground">{contentCounts.videos}</p>
                  <p className="text-xs text-muted-foreground">Video{contentCounts.videos !== 1 ? "s" : ""}</p>
                </div>
              </div>
            )}
            {contentCounts.lectures > 0 && (
              <div className="flex items-center gap-2">
                <BookOpen className="w-5 h-5 text-purple-500" />
                <div>
                  <p className="font-semibold text-foreground">{contentCounts.lectures}</p>
                  <p className="text-xs text-muted-foreground">Lectura{contentCounts.lectures !== 1 ? "s" : ""}</p>
                </div>
              </div>
            )}
            {contentCounts.tasks > 0 && (
              <div className="flex items-center gap-2">
                <CheckSquare className="w-5 h-5 text-orange-500" />
                <div>
                  <p className="font-semibold text-foreground">{contentCounts.tasks}</p>
                  <p className="text-xs text-muted-foreground">Tarea{contentCounts.tasks !== 1 ? "s" : ""}</p>
                </div>
              </div>
            )}
            {contentCounts.quizzes > 0 && (
              <div className="flex items-center gap-2">
                <Award className="w-5 h-5 text-red-500" />
                <div>
                  <p className="font-semibold text-foreground">{contentCounts.quizzes}</p>
                  <p className="text-xs text-muted-foreground">Quiz{contentCounts.quizzes !== 1 ? "s" : ""}</p>
                </div>
              </div>
            )}
            {contentCounts.supplements > 0 && (
              <div className="flex items-center gap-2">
                <Eye className="w-5 h-5 text-green-500" />
                <div>
                  <p className="font-semibold text-foreground">{contentCounts.supplements}</p>
                  <p className="text-xs text-muted-foreground">
                    Complemento{contentCounts.supplements !== 1 ? "s" : ""}
                  </p>
                </div>
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Contenido Expandible */}
      {showDetails && (
        <Card className="border-border/50">
          <CardHeader className="pb-3">
            <h2 className="text-lg font-semibold text-foreground">Contenido del módulo</h2>
          </CardHeader>
          <CardContent className="space-y-4">
            {/* Videos */}
            {groupedContent.videos.length > 0 && (
              <div className="space-y-3">
                <button
                  onClick={() => {
                    const key = "videos"
                    setExpandedItems((prev) => {
                      const next = new Set(prev)
                      next.has(key) ? next.delete(key) : next.add(key)
                      return next
                    })
                  }}
                  className="w-full flex items-center gap-3 font-semibold text-foreground hover:text-primary transition-colors"
                >
                  {expandedItems.has("videos") ? (
                    <ChevronDown className="w-5 h-5" />
                  ) : (
                    <ChevronRight className="w-5 h-5" />
                  )}
                  <Play className="w-5 h-5 text-blue-500" />
                  <span>
                    {contentCounts.videos} videos • Total:{" "}
                    {module.content.filter((c) => c.type === "video").reduce((sum, item) => sum + item.duration, 0)}{" "}
                    minutos
                  </span>
                </button>
                {expandedItems.has("videos") && (
                  <div className="space-y-2 pl-8">
                    {groupedContent.videos.map((item) => (
                      <ContentItemRow key={item.id} item={item} />
                    ))}
                  </div>
                )}
              </div>
            )}

            {/* Lecturas */}
            {groupedContent.lectures.length > 0 && (
              <div className="space-y-3">
                <button
                  onClick={() => {
                    const key = "lectures"
                    setExpandedItems((prev) => {
                      const next = new Set(prev)
                      next.has(key) ? next.delete(key) : next.add(key)
                      return next
                    })
                  }}
                  className="w-full flex items-center gap-3 font-semibold text-foreground hover:text-primary transition-colors"
                >
                  {expandedItems.has("lectures") ? (
                    <ChevronDown className="w-5 h-5" />
                  ) : (
                    <ChevronRight className="w-5 h-5" />
                  )}
                  <BookOpen className="w-5 h-5 text-purple-500" />
                  <span>{contentCounts.lectures} lecturas</span>
                </button>
                {expandedItems.has("lectures") && (
                  <div className="space-y-2 pl-8">
                    {groupedContent.lectures.map((item) => (
                      <ContentItemRow key={item.id} item={item} />
                    ))}
                  </div>
                )}
              </div>
            )}

            {/* Tareas */}
            {groupedContent.tasks.length > 0 && (
              <div className="space-y-3">
                <button
                  onClick={() => {
                    const key = "tasks"
                    setExpandedItems((prev) => {
                      const next = new Set(prev)
                      next.has(key) ? next.delete(key) : next.add(key)
                      return next
                    })
                  }}
                  className="w-full flex items-center gap-3 font-semibold text-foreground hover:text-primary transition-colors"
                >
                  {expandedItems.has("tasks") ? (
                    <ChevronDown className="w-5 h-5" />
                  ) : (
                    <ChevronRight className="w-5 h-5" />
                  )}
                  <CheckSquare className="w-5 h-5 text-orange-500" />
                  <span>{contentCounts.tasks} tareas</span>
                </button>
                {expandedItems.has("tasks") && (
                  <div className="space-y-2 pl-8">
                    {groupedContent.tasks.map((item) => (
                      <ContentItemRow key={item.id} item={item} />
                    ))}
                  </div>
                )}
              </div>
            )}

            {/* Quizzes */}
            {groupedContent.quizzes.length > 0 && (
              <div className="space-y-3">
                <button
                  onClick={() => {
                    const key = "quizzes"
                    setExpandedItems((prev) => {
                      const next = new Set(prev)
                      next.has(key) ? next.delete(key) : next.add(key)
                      return next
                    })
                  }}
                  className="w-full flex items-center gap-3 font-semibold text-foreground hover:text-primary transition-colors"
                >
                  {expandedItems.has("quizzes") ? (
                    <ChevronDown className="w-5 h-5" />
                  ) : (
                    <ChevronRight className="w-5 h-5" />
                  )}
                  <Award className="w-5 h-5 text-red-500" />
                  <span>{contentCounts.quizzes} quizzes</span>
                </button>
                {expandedItems.has("quizzes") && (
                  <div className="space-y-2 pl-8">
                    {groupedContent.quizzes.map((item) => (
                      <ContentItemRow key={item.id} item={item} />
                    ))}
                  </div>
                )}
              </div>
            )}

            {/* Complementos */}
            {groupedContent.supplements.length > 0 && (
              <div className="space-y-3">
                <button
                  onClick={() => {
                    const key = "supplements"
                    setExpandedItems((prev) => {
                      const next = new Set(prev)
                      next.has(key) ? next.delete(key) : next.add(key)
                      return next
                    })
                  }}
                  className="w-full flex items-center gap-3 font-semibold text-foreground hover:text-primary transition-colors"
                >
                  {expandedItems.has("supplements") ? (
                    <ChevronDown className="w-5 h-5" />
                  ) : (
                    <ChevronRight className="w-5 h-5" />
                  )}
                  <Eye className="w-5 h-5 text-green-500" />
                  <span>{contentCounts.supplements} complementos</span>
                </button>
                {expandedItems.has("supplements") && (
                  <div className="space-y-2 pl-8">
                    {groupedContent.supplements.map((item) => (
                      <ContentItemRow key={item.id} item={item} />
                    ))}
                  </div>
                )}
              </div>
            )}
          </CardContent>
        </Card>
      )}
    </div>
  )
}

// Componente para cada item de contenido
function ContentItemRow({ item }: { item: ContentItem }) {
  return (
    <button className="w-full flex items-center gap-3 p-3 rounded-lg hover:bg-muted/50 transition-colors text-left group">
      <div className="flex-shrink-0">{getTypeIcon(item.type)}</div>
      <div className="flex-1 min-w-0">
        <p className="font-medium text-foreground group-hover:text-primary transition-colors truncate">{item.name}</p>
      </div>
      <div className="flex-shrink-0 flex items-center gap-2 text-sm text-muted-foreground">
        <Clock className="w-4 h-4" />
        <span>{item.duration} min</span>
      </div>
      {item.isCompleted && (
        <div className="flex-shrink-0">
          <CheckSquare className="w-5 h-5 text-green-500" />
        </div>
      )}
    </button>
  )
}
