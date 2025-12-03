/**
 * Patrón Interpreter - Sistema de Búsqueda Avanzada
 *
 * Permite crear consultas de búsqueda complejas usando una gramática específica:
 * - nombre:Juan → Buscar por nombre exacto
 * - codigo:POO → Buscar por código
 * - estado:activo → Buscar por estado
 * - tipo:examen → Buscar por tipo
 * - fecha>2024-01-01 → Buscar por fecha mayor
 * - puntaje>=50 → Buscar por puntaje mayor o igual
 * - AND, OR → Operadores lógicos
 */

// ============= CONTEXTO =============
export class SearchContext {
  private data: any[]

  constructor(data: any[]) {
    this.data = data
  }

  getData(): any[] {
    return this.data
  }

  setData(data: any[]): void {
    this.data = data
  }
}

// ============= EXPRESIÓN ABSTRACTA =============
export interface Expression {
  interpret(context: SearchContext): any[]
}

// ============= EXPRESIONES TERMINALES =============

// Expresión para búsqueda por campo específico
export class FieldExpression implements Expression {
  private field: string
  private operator: string
  private value: string

  constructor(field: string, operator: string, value: string) {
    this.field = field
    this.operator = operator
    this.value = value.toLowerCase()
  }

  interpret(context: SearchContext): any[] {
    const data = context.getData()

    return data.filter(item => {
      const fieldValue = this.getNestedValue(item, this.field)

      if (fieldValue === undefined || fieldValue === null) {
        return false
      }

      const itemValue = String(fieldValue).toLowerCase()

      switch (this.operator) {
        case ':':
        case '=':
          return itemValue.includes(this.value)
        case '!=':
          return !itemValue.includes(this.value)
        case '>':
          return this.compareValues(fieldValue, this.value) > 0
        case '>=':
          return this.compareValues(fieldValue, this.value) >= 0
        case '<':
          return this.compareValues(fieldValue, this.value) < 0
        case '<=':
          return this.compareValues(fieldValue, this.value) <= 0
        default:
          return itemValue.includes(this.value)
      }
    })
  }

  private getNestedValue(obj: any, path: string): any {
    return path.split('.').reduce((current, prop) => current?.[prop], obj)
  }

  private compareValues(a: any, b: any): number {
    // Intentar comparar como números
    const numA = Number(a)
    const numB = Number(b)

    if (!isNaN(numA) && !isNaN(numB)) {
      return numA - numB
    }

    // Intentar comparar como fechas
    const dateA = new Date(a)
    const dateB = new Date(b)

    if (!isNaN(dateA.getTime()) && !isNaN(dateB.getTime())) {
      return dateA.getTime() - dateB.getTime()
    }

    // Comparar como strings
    return String(a).localeCompare(String(b))
  }
}

// Expresión para búsqueda de texto libre (busca en todos los campos)
export class FreeTextExpression implements Expression {
  private searchText: string

  constructor(searchText: string) {
    this.searchText = searchText.toLowerCase()
  }

  interpret(context: SearchContext): any[] {
    const data = context.getData()

    return data.filter(item => {
      const allValues = this.getAllValues(item).join(' ').toLowerCase()
      return allValues.includes(this.searchText)
    })
  }

  private getAllValues(obj: any, values: any[] = []): any[] {
    for (const key in obj) {
      const value = obj[key]

      if (value === null || value === undefined) {
        continue
      }

      if (typeof value === 'object' && !Array.isArray(value)) {
        this.getAllValues(value, values)
      } else if (!Array.isArray(value)) {
        values.push(String(value))
      }
    }

    return values
  }
}

// ============= EXPRESIONES NO TERMINALES =============

// Expresión AND (ambas condiciones deben cumplirse)
export class AndExpression implements Expression {
  private left: Expression
  private right: Expression

  constructor(left: Expression, right: Expression) {
    this.left = left
    this.right = right
  }

  interpret(context: SearchContext): any[] {
    const leftResults = this.left.interpret(context)

    // Crear nuevo contexto con resultados de la izquierda
    const newContext = new SearchContext(leftResults)
    const rightResults = this.right.interpret(newContext)

    return rightResults
  }
}

// Expresión OR (al menos una condición debe cumplirse)
export class OrExpression implements Expression {
  private left: Expression
  private right: Expression

  constructor(left: Expression, right: Expression) {
    this.left = left
    this.right = right
  }

  interpret(context: SearchContext): any[] {
    const leftResults = this.left.interpret(context)
    const rightResults = this.right.interpret(context)

    // Combinar resultados únicos
    const combined = [...leftResults]
    const leftIds = new Set(leftResults.map(item => item.id))

    rightResults.forEach(item => {
      if (!leftIds.has(item.id)) {
        combined.push(item)
      }
    })

    return combined
  }
}

// Expresión NOT (niega la condición)
export class NotExpression implements Expression {
  private expression: Expression

  constructor(expression: Expression) {
    this.expression = expression
  }

  interpret(context: SearchContext): any[] {
    const allData = context.getData()
    const excludedResults = this.expression.interpret(context)
    const excludedIds = new Set(excludedResults.map(item => item.id))

    return allData.filter(item => !excludedIds.has(item.id))
  }
}

// ============= PARSER =============

export class SearchQueryParser {
  /**
   * Parsea una consulta de búsqueda y retorna una expresión
   *
   * Ejemplos de consultas:
   * - "nombre:Juan"
   * - "estado:activo AND tipo:examen"
   * - "puntaje>=50 OR estado:publicada"
   * - "NOT estado:cancelada"
   * - "fecha>2024-01-01 AND puntaje>=70"
   */
  parse(query: string): Expression {
    if (!query || query.trim() === '') {
      // Retornar expresión que devuelve todos los datos
      return new FreeTextExpression('')
    }

    // Eliminar espacios extra
    query = query.trim()

    // Parsear operadores OR (menor precedencia)
    if (query.includes(' OR ')) {
      const parts = query.split(' OR ')
      let expression = this.parse(parts[0])

      for (let i = 1; i < parts.length; i++) {
        expression = new OrExpression(expression, this.parse(parts[i]))
      }

      return expression
    }

    // Parsear operadores AND (mayor precedencia)
    if (query.includes(' AND ')) {
      const parts = query.split(' AND ')
      let expression = this.parse(parts[0])

      for (let i = 1; i < parts.length; i++) {
        expression = new AndExpression(expression, this.parse(parts[i]))
      }

      return expression
    }

    // Parsear operador NOT
    if (query.startsWith('NOT ')) {
      const innerQuery = query.substring(4)
      return new NotExpression(this.parse(innerQuery))
    }

    // Parsear expresión de campo
    const fieldMatch = query.match(/^(\w+(?:\.\w+)*)([:=!<>]+)(.+)$/)

    if (fieldMatch) {
      const [, field, operator, value] = fieldMatch
      return new FieldExpression(field, operator, value.trim())
    }

    // Si no coincide con ningún patrón, buscar como texto libre
    return new FreeTextExpression(query)
  }
}

// ============= MOTOR DE BÚSQUEDA =============

export class SearchEngine {
  private parser: SearchQueryParser

  constructor() {
    this.parser = new SearchQueryParser()
  }

  /**
   * Ejecuta una búsqueda sobre un conjunto de datos
   */
  search<T>(data: T[], query: string): T[] {
    if (!query || query.trim() === '') {
      return data
    }

    const expression = this.parser.parse(query)
    const context = new SearchContext(data)

    return expression.interpret(context) as T[]
  }

  /**
   * Valida si una consulta es sintácticamente correcta
   */
  validateQuery(query: string): { valid: boolean; error?: string } {
    try {
      this.parser.parse(query)
      return { valid: true }
    } catch (error) {
      return {
        valid: false,
        error: error instanceof Error ? error.message : 'Error de sintaxis'
      }
    }
  }

  /**
   * Obtiene sugerencias de campos disponibles para búsqueda
   */
  getSuggestions(sampleData: any): string[] {
    const fields: string[] = []

    const extractFields = (obj: any, prefix: string = '') => {
      for (const key in obj) {
        const value = obj[key]
        const fieldName = prefix ? `${prefix}.${key}` : key

        if (value !== null && typeof value === 'object' && !Array.isArray(value)) {
          extractFields(value, fieldName)
        } else if (!Array.isArray(value)) {
          fields.push(fieldName)
        }
      }
    }

    if (sampleData) {
      extractFields(sampleData)
    }

    return fields
  }
}

// ============= EXPORTAR TODO =============

export default {
  SearchEngine,
  SearchQueryParser,
  SearchContext,
  FieldExpression,
  FreeTextExpression,
  AndExpression,
  OrExpression,
  NotExpression
}
