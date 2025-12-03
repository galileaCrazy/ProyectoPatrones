/**
 * Patrón Mediator - Coordinador de Módulos del Curso
 *
 * Permite que los módulos (foro, evaluaciones, contenido) se comuniquen
 * a través de un coordinador común sin tener dependencias directas entre ellos.
 *
 * Beneficios:
 * - Reduce el acoplamiento entre módulos
 * - Centraliza la lógica de coordinación
 * - Facilita el mantenimiento y extensión
 * - Coordina acciones complejas entre múltiples módulos
 */

// ============= INTERFAZ MEDIATOR =============

export interface ICourseMediator {
  notify(sender: ModuleColleague, event: string, data?: any): void
  registerModule(module: ModuleColleague): void
  unregisterModule(moduleId: string): void
}

// ============= INTERFAZ COLLEAGUE (MÓDULO) =============

export abstract class ModuleColleague {
  protected mediator: ICourseMediator | null = null
  protected moduleId: string
  protected moduleName: string

  constructor(moduleId: string, moduleName: string) {
    this.moduleId = moduleId
    this.moduleName = moduleName
  }

  setMediator(mediator: ICourseMediator): void {
    this.mediator = mediator
  }

  getModuleId(): string {
    return this.moduleId
  }

  getModuleName(): string {
    return this.moduleName
  }

  protected notifyMediator(event: string, data?: any): void {
    if (this.mediator) {
      this.mediator.notify(this, event, data)
    }
  }

  // Método abstracto que cada módulo debe implementar
  abstract receiveNotification(event: string, data?: any): void
}

// ============= MÓDULOS CONCRETOS (COLLEAGUES) =============

// Módulo de Contenido
export class ContentModule extends ModuleColleague {
  private contentItems: any[] = []
  private currentContentId: number | null = null
  private onUpdateCallback?: (data: any) => void

  constructor(onUpdateCallback?: (data: any) => void) {
    super('content', 'Contenido')
    this.onUpdateCallback = onUpdateCallback
  }

  // Cargar contenido
  loadContent(items: any[]): void {
    this.contentItems = items
    this.notifyMediator('content:loaded', { count: items.length })
  }

  // Seleccionar contenido
  selectContent(contentId: number): void {
    this.currentContentId = contentId
    const content = this.contentItems.find(item => item.id === contentId)

    this.notifyMediator('content:selected', {
      contentId,
      content,
      type: content?.tipo || 'unknown'
    })

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'content:selected',
        contentId,
        content
      })
    }
  }

  // Completar contenido
  completeContent(contentId: number): void {
    this.notifyMediator('content:completed', {
      contentId,
      timestamp: new Date().toISOString()
    })

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'content:completed',
        contentId
      })
    }
  }

  receiveNotification(event: string, data?: any): void {
    switch (event) {
      case 'evaluation:started':
        // Cuando se inicia una evaluación, bloquear contenido relacionado
        console.log(`[${this.moduleName}] Evaluación iniciada:`, data)
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'lock-content',
            reason: 'evaluation-in-progress'
          })
        }
        break

      case 'forum:discussion-created':
        // Cuando se crea discusión en foro, resaltar contenido relacionado
        console.log(`[${this.moduleName}] Nueva discusión en foro:`, data)
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'highlight-content',
            contentId: data?.contentId
          })
        }
        break

      case 'evaluation:submitted':
        // Desbloquear contenido después de evaluación
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'unlock-content'
          })
        }
        break
    }
  }

  getCurrentContent(): number | null {
    return this.currentContentId
  }
}

// Módulo de Evaluaciones
export class EvaluationModule extends ModuleColleague {
  private evaluations: any[] = []
  private activeEvaluationId: number | null = null
  private onUpdateCallback?: (data: any) => void

  constructor(onUpdateCallback?: (data: any) => void) {
    super('evaluation', 'Evaluaciones')
    this.onUpdateCallback = onUpdateCallback
  }

  loadEvaluations(evaluations: any[]): void {
    this.evaluations = evaluations
    this.notifyMediator('evaluation:loaded', { count: evaluations.length })
  }

  startEvaluation(evaluationId: number): void {
    this.activeEvaluationId = evaluationId
    const evaluation = this.evaluations.find(e => e.id === evaluationId)

    this.notifyMediator('evaluation:started', {
      evaluationId,
      evaluation,
      startTime: new Date().toISOString()
    })

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'evaluation:started',
        evaluationId,
        evaluation
      })
    }
  }

  submitEvaluation(evaluationId: number, answers: any): void {
    this.notifyMediator('evaluation:submitted', {
      evaluationId,
      answers,
      submitTime: new Date().toISOString()
    })

    this.activeEvaluationId = null

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'evaluation:submitted',
        evaluationId
      })
    }
  }

  receiveNotification(event: string, data?: any): void {
    switch (event) {
      case 'content:completed':
        // Cuando se completa contenido, verificar si desbloquea evaluaciones
        console.log(`[${this.moduleName}] Contenido completado:`, data)
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'check-prerequisites',
            contentId: data?.contentId
          })
        }
        break

      case 'forum:help-requested':
        // Pausar tiempo de evaluación si se solicita ayuda
        if (this.activeEvaluationId) {
          console.log(`[${this.moduleName}] Ayuda solicitada durante evaluación`)
          if (this.onUpdateCallback) {
            this.onUpdateCallback({
              action: 'pause-timer'
            })
          }
        }
        break

      case 'content:selected':
        // Mostrar evaluaciones relacionadas al contenido
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'filter-by-content',
            contentId: data?.contentId
          })
        }
        break
    }
  }

  getActiveEvaluation(): number | null {
    return this.activeEvaluationId
  }
}

// Módulo de Foro
export class ForumModule extends ModuleColleague {
  private discussions: any[] = []
  private activeDiscussionId: number | null = null
  private onUpdateCallback?: (data: any) => void

  constructor(onUpdateCallback?: (data: any) => void) {
    super('forum', 'Foro')
    this.onUpdateCallback = onUpdateCallback
  }

  loadDiscussions(discussions: any[]): void {
    this.discussions = discussions
    this.notifyMediator('forum:loaded', { count: discussions.length })
  }

  createDiscussion(topic: string, contentId?: number): void {
    const newDiscussion = {
      id: Date.now(),
      topic,
      contentId,
      createdAt: new Date().toISOString()
    }

    this.discussions.push(newDiscussion)

    this.notifyMediator('forum:discussion-created', {
      discussion: newDiscussion,
      contentId
    })

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'discussion:created',
        discussion: newDiscussion
      })
    }
  }

  requestHelp(discussionId: number): void {
    this.notifyMediator('forum:help-requested', {
      discussionId,
      timestamp: new Date().toISOString()
    })

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'help:requested',
        discussionId
      })
    }
  }

  postMessage(discussionId: number, message: string): void {
    this.notifyMediator('forum:message-posted', {
      discussionId,
      message,
      timestamp: new Date().toISOString()
    })

    if (this.onUpdateCallback) {
      this.onUpdateCallback({
        action: 'message:posted',
        discussionId,
        message
      })
    }
  }

  receiveNotification(event: string, data?: any): void {
    switch (event) {
      case 'content:selected':
        // Mostrar discusiones relacionadas al contenido
        console.log(`[${this.moduleName}] Contenido seleccionado:`, data)
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'filter-discussions',
            contentId: data?.contentId
          })
        }
        break

      case 'evaluation:started':
        // Notificar al foro que hay evaluación activa
        console.log(`[${this.moduleName}] Evaluación iniciada, modo ayuda limitado`)
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'enable-exam-mode'
          })
        }
        break

      case 'evaluation:submitted':
        // Restaurar funcionalidad completa del foro
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'disable-exam-mode'
          })
        }
        break

      case 'content:completed':
        // Sugerir discusiones relacionadas
        if (this.onUpdateCallback) {
          this.onUpdateCallback({
            action: 'suggest-discussions',
            contentId: data?.contentId
          })
        }
        break
    }
  }

  getActiveDiscussion(): number | null {
    return this.activeDiscussionId
  }
}

// ============= MEDIATOR CONCRETO =============

export class CourseMediator implements ICourseMediator {
  private modules: Map<string, ModuleColleague> = new Map()
  private eventLog: Array<{
    timestamp: string
    sender: string
    event: string
    data?: any
  }> = []

  registerModule(module: ModuleColleague): void {
    this.modules.set(module.getModuleId(), module)
    module.setMediator(this)
    console.log(`[Mediator] Módulo registrado: ${module.getModuleName()}`)
  }

  unregisterModule(moduleId: string): void {
    this.modules.delete(moduleId)
    console.log(`[Mediator] Módulo desregistrado: ${moduleId}`)
  }

  notify(sender: ModuleColleague, event: string, data?: any): void {
    // Registrar evento
    this.eventLog.push({
      timestamp: new Date().toISOString(),
      sender: sender.getModuleName(),
      event,
      data
    })

    console.log(`[Mediator] Evento recibido: ${event} desde ${sender.getModuleName()}`, data)

    // Notificar a otros módulos (excluyendo al remitente)
    this.modules.forEach((module, moduleId) => {
      if (module.getModuleId() !== sender.getModuleId()) {
        module.receiveNotification(event, data)
      }
    })
  }

  getEventLog(): Array<any> {
    return [...this.eventLog]
  }

  clearEventLog(): void {
    this.eventLog = []
  }

  getModule(moduleId: string): ModuleColleague | undefined {
    return this.modules.get(moduleId)
  }

  getAllModules(): ModuleColleague[] {
    return Array.from(this.modules.values())
  }
}

// ============= EXPORTAR TODO =============

export default {
  CourseMediator,
  ContentModule,
  EvaluationModule,
  ForumModule,
  ModuleColleague
}
