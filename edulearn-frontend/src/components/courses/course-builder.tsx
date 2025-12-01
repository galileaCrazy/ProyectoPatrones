'use client'

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

interface CourseBuilderViewProps {
  onClose: () => void
  userRole: 'professor' | 'admin'
  userId?: string
  userName?: string
}

interface Professor {
  id: number
  nombre: string
  email: string
}

export default function CourseBuilderView({ onClose, userRole, userId = '1', userName = 'Usuario' }: CourseBuilderViewProps) {
  const [step, setStep] = useState(1)
  const [courseData, setCourseData] = useState({
    name: '',
    description: '',
    fechaInicio: '',
    fechaFin: '',
    cupoMaximo: '',
    professorId: userRole === 'professor' ? userId : '',
    professorName: userRole === 'professor' ? userName : '',
    type: 'Virtual',
    period: '2025-1',
    evaluationStrategy: 'PONDERADA',
  })

  // Configuración según el rol usando Chain of Responsibility
  const [formConfig, setFormConfig] = useState({
    mostrarListaProfesores: userRole === 'admin',
    modoAutoAsignacion: userRole === 'professor',
    profesorAsignado: userRole === 'professor' ? userName : null,
  })

  // Períodos académicos válidos obtenidos del backend
  const [periodosValidos, setPeriodosValidos] = useState<string[]>([])

  // Límites de cupo según tipo de curso (obtenidos del backend)
  const [limiteCupos, setLimiteCupos] = useState<Record<string, number>>({
    'Presencial': 35,
    'Virtual': 100,
    'Híbrido': 50
  })

  // Lista de profesores desde la BD
  const [profesores, setProfesores] = useState<Professor[]>([])

  // Nombre completo del usuario actual (para profesores)
  const [nombreCompletoUsuario, setNombreCompletoUsuario] = useState<string>(userName)

  // Errores de validación
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // Cargar períodos académicos válidos desde el backend usando Chain of Responsibility
    const cargarPeriodosValidos = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/cursos/periodos-validos')
        const data = await response.json()

        if (data.exito && data.periodosValidos) {
          setPeriodosValidos(data.periodosValidos)
          console.log('📅 Períodos válidos cargados:', data.periodosValidos)
        }
      } catch (error) {
        console.error('Error al cargar períodos:', error)
        setPeriodosValidos(['Enero-Junio 2026', 'Agosto-Diciembre 2026'])
      }
    }

    // Cargar lista de profesores desde la BD
    const cargarProfesores = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/cursos/profesores')
        const data = await response.json()

        if (data.exito && data.profesores) {
          setProfesores(data.profesores)
          console.log('👨‍🏫 Profesores cargados:', data.profesores)
        }
      } catch (error) {
        console.error('Error al cargar profesores:', error)
      }
    }

    // Cargar datos del usuario actual (para profesores)
    const cargarDatosUsuario = async () => {
      if (userRole === 'professor') {
        try {
          const response = await fetch(`http://localhost:8080/api/cursos/usuario/${userId}`)
          const data = await response.json()

          if (data.exito && data.nombreCompleto) {
            setNombreCompletoUsuario(data.nombreCompleto)
            setCourseData(prev => ({
              ...prev,
              professorId: userId,
              professorName: data.nombreCompleto
            }))
            console.log('👤 Datos del usuario cargados:', data.nombreCompleto)
          }
        } catch (error) {
          console.error('Error al cargar datos del usuario:', error)
        }
      }
    }

    // Configurar formulario según el rol usando Chain of Responsibility
    const configurarFormulario = async () => {
      console.log('🔍 Configurando formulario para rol:', userRole)

      if (userRole === 'professor') {
        const config = {
          mostrarListaProfesores: false,
          modoAutoAsignacion: true,
          profesorAsignado: nombreCompletoUsuario,
        }
        console.log('👨‍🏫 Configuración PROFESOR:', config)
        setFormConfig(config)
      } else {
        const config = {
          mostrarListaProfesores: true,
          modoAutoAsignacion: false,
          profesorAsignado: null,
        }
        console.log('👨‍💼 Configuración ADMIN:', config)
        setFormConfig(config)
      }
    }

    cargarPeriodosValidos()
    cargarProfesores()
    cargarDatosUsuario()
    configurarFormulario()
  }, [userRole, userId])

  // Validar paso 1 antes de avanzar
  const validateStep1 = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!courseData.name.trim()) {
      newErrors.name = 'El nombre del curso es requerido'
    }

    if (!courseData.description.trim()) {
      newErrors.description = 'La descripción es requerida'
    }

    if (!courseData.type) {
      newErrors.type = 'Debes seleccionar un tipo de curso'
    }

    if (!courseData.period) {
      newErrors.period = 'Debes seleccionar un período académico'
    }

    if (userRole === 'admin' && !courseData.professorId) {
      newErrors.professorId = 'Debes asignar un profesor al curso'
    }

    setErrors(newErrors)

    if (Object.keys(newErrors).length > 0) {
      console.log('❌ Validación fallida:', newErrors)
      return false
    }

    console.log('✅ Paso 1 validado correctamente')
    return true
  }

  const handleNext = () => {
    // Validar el paso 1 antes de avanzar
    if (step === 1) {
      if (!validateStep1()) {
        return // No avanzar si hay errores
      }
    }

    if (step < 4) setStep(step + 1) // Ahora son 4 pasos en lugar de 5
  }

  const handlePrev = () => {
    if (step > 1) {
      setStep(step - 1)
      setErrors({}) // Limpiar errores al retroceder
    }
  }

  const handleCreate = () => {
    alert('Curso creado: ' + courseData.name)
    onClose()
  }

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-2">Crear Nuevo Curso</h1>
      <p className="text-muted-foreground mb-8">Paso {step} de 4</p>

      {/* Progress Bar */}
      <div className="w-full h-2 rounded-full bg-muted mb-8 overflow-hidden">
        <div
          className="h-full bg-primary transition-all"
          style={{ width: `${(step / 4) * 100}%` }}
        ></div>
      </div>

      {/* Steps */}
      <Card className="mb-8 border-border/50">
        <CardHeader>
          <CardTitle>
            {step === 1 && '📋 Información Básica y Asignación'}
            {step === 2 && '📚 Agregar Módulos'}
            {step === 3 && '📝 Configurar Evaluaciones'}
            {step === 4 && '✅ Revisión y Confirmación'}
          </CardTitle>
          <CardDescription>
            {step === 1 && 'Completa todos los campos requeridos para continuar'}
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-6">
          {step === 1 && (
            <div className="space-y-4">
              {/* Nombre */}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Nombre del Curso *
                </label>
                <input
                  type="text"
                  placeholder="Ej: Programación Orientada a Objetos"
                  value={courseData.name}
                  onChange={(e) => setCourseData({ ...courseData, name: e.target.value })}
                  className={`w-full px-4 py-2 rounded-lg border ${errors.name ? 'border-red-500' : 'border-input'} bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary`}
                />
                {errors.name && <p className="text-xs text-red-500 mt-1">{errors.name}</p>}
              </div>

              {/* Descripción */}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Descripción *
                </label>
                <textarea
                  placeholder="Describe el contenido del curso..."
                  value={courseData.description}
                  onChange={(e) => setCourseData({ ...courseData, description: e.target.value })}
                  className={`w-full px-4 py-2 rounded-lg border ${errors.description ? 'border-red-500' : 'border-input'} bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary h-24 resize-none`}
                />
                {errors.description && <p className="text-xs text-red-500 mt-1">{errors.description}</p>}
              </div>

              {/* Tipo de Curso */}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Tipo de Curso *
                  <span className="ml-2 text-xs text-muted-foreground">
                    (Determina el cupo máximo)
                  </span>
                </label>
                <select
                  value={courseData.type}
                  onChange={(e) => {
                    setCourseData({ ...courseData, type: e.target.value })
                    setErrors({ ...errors, type: '' })
                  }}
                  className={`w-full px-4 py-2 rounded-lg border ${errors.type ? 'border-red-500' : 'border-input'} bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary`}
                >
                  <option value="">Selecciona un tipo...</option>
                  <option value="Virtual">Virtual (Cupo máximo: 100)</option>
                  <option value="Presencial">Presencial (Cupo máximo: 35)</option>
                  <option value="Híbrido">Híbrido (Cupo máximo: 50)</option>
                </select>
                {errors.type && <p className="text-xs text-red-500 mt-1">{errors.type}</p>}
                {courseData.type && (
                  <p className="text-xs text-green-600 mt-1">
                    ✅ Cupo máximo: {limiteCupos[courseData.type]} estudiantes (Chain of Responsibility)
                  </p>
                )}
              </div>

              {/* Período Académico */}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Período Académico *
                  <span className="ml-2 text-xs text-muted-foreground">
                    (Solo períodos futuros)
                  </span>
                </label>
                <select
                  value={courseData.period}
                  onChange={(e) => {
                    setCourseData({ ...courseData, period: e.target.value })
                    setErrors({ ...errors, period: '' })
                  }}
                  className={`w-full px-4 py-2 rounded-lg border ${errors.period ? 'border-red-500' : 'border-input'} bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary`}
                >
                  {periodosValidos.length === 0 ? (
                    <option value="">Cargando períodos...</option>
                  ) : (
                    <>
                      <option value="">Selecciona un período...</option>
                      {periodosValidos.map((periodo) => (
                        <option key={periodo} value={periodo}>
                          {periodo}
                        </option>
                      ))}
                    </>
                  )}
                </select>
                {errors.period && <p className="text-xs text-red-500 mt-1">{errors.period}</p>}
                {periodosValidos.length > 0 && courseData.period && (
                  <p className="text-xs text-green-600 mt-1">
                    ✅ Validado con Chain of Responsibility - Hoy: {new Date().toLocaleDateString('es-ES')}
                  </p>
                )}
              </div>

              {/* Profesor Asignado */}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">
                  Profesor Asignado *
                  {formConfig.modoAutoAsignacion && (
                    <span className="ml-2 text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded dark:bg-blue-900 dark:text-blue-200">
                      Auto-asignado
                    </span>
                  )}
                </label>

                {userRole === 'professor' ? (
                  <div className="w-full px-4 py-2 rounded-lg border border-input bg-muted/50 text-foreground flex items-center justify-between">
                    <span className="font-medium">{nombreCompletoUsuario}</span>
                    <span className="text-xs text-muted-foreground">
                      (Tú serás el profesor de este curso)
                    </span>
                  </div>
                ) : (
                  <>
                    <select
                      value={courseData.professorId}
                      onChange={(e) => {
                        const selectedProf = profesores.find(p => p.id.toString() === e.target.value)
                        setCourseData({
                          ...courseData,
                          professorId: e.target.value,
                          professorName: selectedProf?.nombre || ''
                        })
                        setErrors({ ...errors, professorId: '' })
                      }}
                      className={`w-full px-4 py-2 rounded-lg border ${errors.professorId ? 'border-red-500' : 'border-input'} bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary`}
                    >
                      {profesores.length === 0 ? (
                        <option value="">Cargando profesores...</option>
                      ) : (
                        <>
                          <option value="">Selecciona un profesor...</option>
                          {profesores.map((professor) => (
                            <option key={professor.id} value={professor.id}>
                              {professor.nombre}
                            </option>
                          ))}
                        </>
                      )}
                    </select>
                    {errors.professorId && <p className="text-xs text-red-500 mt-1">{errors.professorId}</p>}
                    {profesores.length > 0 && (
                      <p className="text-xs text-muted-foreground mt-1">
                        ✅ {profesores.length} profesores disponibles desde la BD
                      </p>
                    )}
                  </>
                )}
              </div>

              {/* Resumen de validaciones */}
              {Object.keys(errors).length > 0 && (
                <div className="p-4 bg-red-50 dark:bg-red-900/20 rounded-lg border border-red-200 dark:border-red-800">
                  <p className="text-sm text-red-800 dark:text-red-200 font-medium">
                    ⚠️ Por favor completa todos los campos requeridos antes de continuar
                  </p>
                </div>
              )}
              <div>
                <label className="text-sm font-medium text-foreground block mb-2">Estrategia de Evaluación</label>
                <select
                  value={courseData.evaluationStrategy}
                  onChange={(e) => setCourseData({ ...courseData, evaluationStrategy: e.target.value })}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="PONDERADA">Evaluación Ponderada (Tareas 30%, Exámenes 50%, Proyecto 20%)</option>
                  <option value="SIMPLE">Promedio Simple (Todas las evaluaciones tienen el mismo peso)</option>
                  <option value="COMPETENCIAS">Evaluación por Competencias (Basada en dominio de habilidades)</option>
                </select>
                <p className="text-xs text-muted-foreground mt-1">
                  Define cómo se calcularán las calificaciones finales de los estudiantes
                </p>
              </div>
            </div>
          )}

          {step === 2 && (
            <div className="space-y-4">
              <Button className="w-full bg-primary hover:bg-primary/90 text-primary-foreground mb-4">
                + Agregar Módulo
              </Button>
              {[1, 2].map((i) => (
                <div key={i} className="p-4 rounded-lg bg-muted/50 border border-border">
                  <p className="font-medium text-foreground">Módulo {i}: Introducción</p>
                  <p className="text-sm text-muted-foreground mt-1">3 lecciones</p>
                </div>
              ))}
            </div>
          )}

          {step === 3 && (
            <div className="space-y-4">
              <Button className="w-full bg-primary hover:bg-primary/90 text-primary-foreground mb-4">
                + Agregar Evaluación
              </Button>
              {[1, 2].map((i) => (
                <div key={i} className="p-4 rounded-lg bg-muted/50 border border-border flex justify-between items-center">
                  <div>
                    <p className="font-medium text-foreground">Quiz {i}</p>
                    <p className="text-sm text-muted-foreground">Peso: 20%</p>
                  </div>
                  <button className="text-destructive hover:text-destructive/90">×</button>
                </div>
              ))}
            </div>
          )}

          {step === 4 && (
            <div className="space-y-4">
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground">Nombre del Curso</p>
                <p className="font-semibold text-foreground text-lg">{courseData.name}</p>
              </div>
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground">Tipo</p>
                <p className="font-semibold text-foreground">{courseData.type}</p>
              </div>
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground">Descripción</p>
                <p className="font-semibold text-foreground">{courseData.description}</p>
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Buttons */}
      <div className="flex justify-between gap-4">
        <Button
          onClick={step === 1 ? onClose : handlePrev}
          className="bg-muted hover:bg-muted/80 text-foreground"
        >
          {step === 1 ? 'Cancelar' : 'Anterior'}
        </Button>
        <Button
          onClick={step === 4 ? handleCreate : handleNext}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
        >
          {step === 4 ? 'Crear Curso' : 'Siguiente'}
        </Button>
      </div>
    </div>
  )
}
