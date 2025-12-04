/**
 * PATRÓN COMMAND
 *
 * Encapsula operaciones sobre evaluaciones como objetos,
 * permitiendo deshacer/rehacer acciones y mantener un historial.
 */

// ============= INTERFACES =============

export interface ICommand {
  execute(): Promise<void>
  undo(): Promise<void>
  getDescription(): string
}

export interface Evaluation {
  id?: number
  nombre: string
  cursoId: number
  tipoEvaluacion: string
  fechaInicio: string
  fechaLimite: string
  puntajeMaximo: number
  estado: 'Borrador' | 'Programada' | 'Publicada' | 'Cerrada' | 'Cancelada'
  duracionMinutos?: number
  intentosPermitidos?: number
}

// ============= RECEIVER =============

export class EvaluationService {
  private baseUrl = 'http://localhost:8080/api/evaluaciones'

  async createEvaluation(evaluation: Evaluation): Promise<Evaluation> {
    const response = await fetch(this.baseUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(evaluation)
    })
    if (!response.ok) throw new Error('Error al crear evaluación')
    return await response.json()
  }

  async updateEvaluation(id: number, evaluation: Partial<Evaluation>): Promise<Evaluation> {
    // Convertir fechas al formato esperado por el backend
    const payload: any = { ...evaluation }

    // Normalizar fechaLimite
    if (payload.fechaLimite) {
      // Extraer solo la fecha (YYYY-MM-DD) sin importar el formato de entrada
      const dateOnly = payload.fechaLimite.split('T')[0].replace(/\//g, '-')
      payload.fechaLimite = dateOnly + 'T23:59:59'
    }

    // Normalizar fechaInicio
    if (payload.fechaInicio) {
      // Extraer solo la fecha (YYYY-MM-DD) sin importar el formato de entrada
      const dateOnly = payload.fechaInicio.split('T')[0].replace(/\//g, '-')
      payload.fechaInicio = dateOnly + 'T00:00:00'
    }

    const response = await fetch(`${this.baseUrl}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })
    if (!response.ok) {
      const errorText = await response.text()
      console.error('Error al actualizar evaluación:', errorText)
      throw new Error('Error al actualizar evaluación')
    }
    return await response.json()
  }

  async deleteEvaluation(id: number): Promise<void> {
    const response = await fetch(`${this.baseUrl}/${id}`, {
      method: 'DELETE'
    })
    if (!response.ok) throw new Error('Error al eliminar evaluación')
  }

  async getEvaluation(id: number): Promise<Evaluation> {
    const response = await fetch(`${this.baseUrl}/${id}`)
    if (!response.ok) throw new Error('Error al obtener evaluación')
    return await response.json()
  }
}

// ============= CONCRETE COMMANDS =============

/**
 * Comando: Crear Evaluación
 */
export class CreateEvaluationCommand implements ICommand {
  private createdEvaluationId?: number

  constructor(
    private service: EvaluationService,
    private evaluation: Evaluation,
    private onSuccess?: (evaluation: Evaluation) => void
  ) {}

  async execute(): Promise<void> {
    console.log('Ejecutando: Crear evaluación')
    const created = await this.service.createEvaluation(this.evaluation)
    this.createdEvaluationId = created.id
    if (this.onSuccess) this.onSuccess(created)
  }

  async undo(): Promise<void> {
    if (this.createdEvaluationId) {
      console.log('↩️  Deshaciendo: Eliminar evaluación creada')
      await this.service.deleteEvaluation(this.createdEvaluationId)
    }
  }

  getDescription(): string {
    return `Crear evaluación: ${this.evaluation.nombre}`
  }
}

/**
 * Comando: Publicar Evaluación
 */
export class PublishEvaluationCommand implements ICommand {
  private previousState?: string

  constructor(
    private service: EvaluationService,
    private evaluationId: number,
    private onSuccess?: () => void
  ) {}

  async execute(): Promise<void> {
    console.log('📢 Ejecutando: Publicar evaluación')
    const evaluation = await this.service.getEvaluation(this.evaluationId)
    this.previousState = evaluation.estado

    await this.service.updateEvaluation(this.evaluationId, {
      estado: 'Publicada'
    })

    if (this.onSuccess) this.onSuccess()
  }

  async undo(): Promise<void> {
    if (this.previousState) {
      console.log('↩️  Deshaciendo: Revertir publicación')
      await this.service.updateEvaluation(this.evaluationId, {
        estado: this.previousState as any
      })
    }
  }

  getDescription(): string {
    return `Publicar evaluación ID: ${this.evaluationId}`
  }
}

/**
 * Comando: Cancelar Evaluación
 */
export class CancelEvaluationCommand implements ICommand {
  private previousState?: string

  constructor(
    private service: EvaluationService,
    private evaluationId: number,
    private onSuccess?: () => void
  ) {}

  async execute(): Promise<void> {
    console.log('Ejecutando: Cancelar evaluación')
    const evaluation = await this.service.getEvaluation(this.evaluationId)
    this.previousState = evaluation.estado

    await this.service.updateEvaluation(this.evaluationId, {
      estado: 'Cancelada'
    })

    if (this.onSuccess) this.onSuccess()
  }

  async undo(): Promise<void> {
    if (this.previousState) {
      console.log('↩️  Deshaciendo: Revertir cancelación')
      await this.service.updateEvaluation(this.evaluationId, {
        estado: this.previousState as any
      })
    }
  }

  getDescription(): string {
    return `Cancelar evaluación ID: ${this.evaluationId}`
  }
}

/**
 * Comando: Cambiar Fecha Límite
 */
export class ChangeDueDateCommand implements ICommand {
  private previousDate?: string

  constructor(
    private service: EvaluationService,
    private evaluationId: number,
    private newDueDate: string,
    private onSuccess?: () => void
  ) {}

  async execute(): Promise<void> {
    console.log('Ejecutando: Cambiar fecha límite')
    const evaluation = await this.service.getEvaluation(this.evaluationId)
    this.previousDate = evaluation.fechaLimite

    await this.service.updateEvaluation(this.evaluationId, {
      fechaLimite: this.newDueDate
    })

    if (this.onSuccess) this.onSuccess()
  }

  async undo(): Promise<void> {
    if (this.previousDate) {
      console.log('↩️  Deshaciendo: Restaurar fecha anterior')
      await this.service.updateEvaluation(this.evaluationId, {
        fechaLimite: this.previousDate
      })
    }
  }

  getDescription(): string {
    return `Cambiar fecha límite de evaluación ID: ${this.evaluationId}`
  }
}

/**
 * Comando: Programar Evaluación
 */
export class ScheduleEvaluationCommand implements ICommand {
  private previousState?: string

  constructor(
    private service: EvaluationService,
    private evaluationId: number,
    private onSuccess?: () => void
  ) {}

  async execute(): Promise<void> {
    console.log('Ejecutando: Programar evaluación')
    const evaluation = await this.service.getEvaluation(this.evaluationId)
    this.previousState = evaluation.estado

    await this.service.updateEvaluation(this.evaluationId, {
      estado: 'Programada'
    })

    if (this.onSuccess) this.onSuccess()
  }

  async undo(): Promise<void> {
    if (this.previousState) {
      console.log('↩️  Deshaciendo: Revertir programación')
      await this.service.updateEvaluation(this.evaluationId, {
        estado: this.previousState as any
      })
    }
  }

  getDescription(): string {
    return `Programar evaluación ID: ${this.evaluationId}`
  }
}

// ============= INVOKER =============

/**
 * Invoker: Gestor de Comandos con soporte para Undo/Redo
 */
export class CommandManager {
  private history: ICommand[] = []
  private currentIndex: number = -1
  private maxHistorySize: number = 20

  async executeCommand(command: ICommand): Promise<void> {
    try {
      await command.execute()

      // Eliminar comandos posteriores al índice actual
      this.history = this.history.slice(0, this.currentIndex + 1)

      // Agregar nuevo comando
      this.history.push(command)
      this.currentIndex++

      // Limitar tamaño del historial
      if (this.history.length > this.maxHistorySize) {
        this.history.shift()
        this.currentIndex--
      }

      console.log(`Comando ejecutado: ${command.getDescription()}`)
    } catch (error) {
      console.error('Error al ejecutar comando:', error)
      throw error
    }
  }

  async undo(): Promise<boolean> {
    if (!this.canUndo()) {
      console.log('⚠️  No hay comandos para deshacer')
      return false
    }

    try {
      const command = this.history[this.currentIndex]
      await command.undo()
      this.currentIndex--
      console.log(`↩️  Deshecho: ${command.getDescription()}`)
      return true
    } catch (error) {
      console.error('Error al deshacer:', error)
      return false
    }
  }

  async redo(): Promise<boolean> {
    if (!this.canRedo()) {
      console.log('⚠️  No hay comandos para rehacer')
      return false
    }

    try {
      this.currentIndex++
      const command = this.history[this.currentIndex]
      await command.execute()
      console.log(`↪️  Rehecho: ${command.getDescription()}`)
      return true
    } catch (error) {
      console.error('Error al rehacer:', error)
      this.currentIndex--
      return false
    }
  }

  canUndo(): boolean {
    return this.currentIndex >= 0
  }

  canRedo(): boolean {
    return this.currentIndex < this.history.length - 1
  }

  getHistory(): string[] {
    return this.history.slice(0, this.currentIndex + 1).map(cmd => cmd.getDescription())
  }

  clearHistory(): void {
    this.history = []
    this.currentIndex = -1
    console.log('Historial limpiado')
  }
}
