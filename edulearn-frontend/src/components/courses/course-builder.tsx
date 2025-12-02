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

interface Material {
  nombre: string
  descripcion: string
}

interface Evaluacion {
  nombre: string
  descripcion: string
}

interface Modulo {
  titulo: string
  descripcion: string
  materiales: Material[]
  evaluaciones: Evaluacion[]
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
    type: 'virtual',
    period: '2025-1',
    evaluationStrategy: 'ponderada',
    duracion: 40,
  })

  // Estado para módulos, materiales y evaluaciones
  const [modulos, setModulos] = useState<Modulo[]>([])
  const [moduloActual, setModuloActual] = useState<Modulo>({
    titulo: '',
    descripcion: '',
    materiales: [],
    evaluaciones: []
  })

  // Estado para el Paso 3: selección de módulo existente
  const [moduloSeleccionadoIndex, setModuloSeleccionadoIndex] = useState<number>(-1)
  const [materialActual, setMaterialActual] = useState<Material>({ nombre: '', descripcion: '' })
  const [evaluacionActual, setEvaluacionActual] = useState<Evaluacion>({ nombre: '', descripcion: '' })
  const [isCreating, setIsCreating] = useState(false)

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

    // Validar paso 2: debe haber al menos un módulo
    if (step === 2) {
      if (modulos.length === 0) {
        alert('Debes agregar al menos un módulo para continuar')
        return
      }
    }

    // Validar paso 3: si hay módulos, resetear la selección
    if (step === 2 && modulos.length > 0) {
      // Al pasar del Paso 2 al Paso 3, resetear el módulo seleccionado
      setModuloSeleccionadoIndex(-1)
    }

    if (step < 4) setStep(step + 1)
  }

  const handlePrev = () => {
    if (step > 1) {
      setStep(step - 1)
      setErrors({}) // Limpiar errores al retroceder
    }
  }

  // Funciones para manejar módulos
  const agregarModulo = () => {
    if (!moduloActual.titulo.trim()) {
      alert('El título del módulo es obligatorio')
      return
    }
    setModulos([...modulos, moduloActual])
    setModuloActual({
      titulo: '',
      descripcion: '',
      materiales: [],
      evaluaciones: []
    })
  }

  const eliminarModulo = (index: number) => {
    setModulos(modulos.filter((_, i) => i !== index))
  }

  // Funciones para manejar materiales (Paso 3 - trabaja con módulos existentes)
  const agregarMaterial = () => {
    if (moduloSeleccionadoIndex === -1) {
      alert('Debes seleccionar un módulo del dropdown antes de agregar un material')
      return
    }

    if (!materialActual.nombre.trim()) {
      alert('El nombre del material es obligatorio')
      return
    }

    // Actualizar el módulo seleccionado en el array de módulos
    const nuevosModulos = [...modulos]
    nuevosModulos[moduloSeleccionadoIndex] = {
      ...nuevosModulos[moduloSeleccionadoIndex],
      materiales: [...nuevosModulos[moduloSeleccionadoIndex].materiales, materialActual]
    }
    setModulos(nuevosModulos)
    setMaterialActual({ nombre: '', descripcion: '' })
  }

  const eliminarMaterial = (index: number) => {
    if (moduloSeleccionadoIndex === -1) return

    const nuevosModulos = [...modulos]
    nuevosModulos[moduloSeleccionadoIndex] = {
      ...nuevosModulos[moduloSeleccionadoIndex],
      materiales: nuevosModulos[moduloSeleccionadoIndex].materiales.filter((_, i) => i !== index)
    }
    setModulos(nuevosModulos)
  }

  // Funciones para manejar evaluaciones (Paso 3 - trabaja con módulos existentes)
  const agregarEvaluacion = () => {
    if (moduloSeleccionadoIndex === -1) {
      alert('Debes seleccionar un módulo del dropdown antes de agregar una evaluación')
      return
    }

    if (!evaluacionActual.nombre.trim()) {
      alert('El nombre de la evaluación es obligatorio')
      return
    }

    // Actualizar el módulo seleccionado en el array de módulos
    const nuevosModulos = [...modulos]
    nuevosModulos[moduloSeleccionadoIndex] = {
      ...nuevosModulos[moduloSeleccionadoIndex],
      evaluaciones: [...nuevosModulos[moduloSeleccionadoIndex].evaluaciones, evaluacionActual]
    }
    setModulos(nuevosModulos)
    setEvaluacionActual({ nombre: '', descripcion: '' })
  }

  const eliminarEvaluacion = (index: number) => {
    if (moduloSeleccionadoIndex === -1) return

    const nuevosModulos = [...modulos]
    nuevosModulos[moduloSeleccionadoIndex] = {
      ...nuevosModulos[moduloSeleccionadoIndex],
      evaluaciones: nuevosModulos[moduloSeleccionadoIndex].evaluaciones.filter((_, i) => i !== index)
    }
    setModulos(nuevosModulos)
  }

  const handleCreate = async () => {
    setIsCreating(true)

    try {
      // Preparar el payload según la estructura esperada por el backend
      const payload = {
        nombre: courseData.name,
        descripcion: courseData.description,
        tipoCurso: courseData.type.toLowerCase(),
        profesorId: parseInt(courseData.professorId),
        periodoAcademico: courseData.period,
        duracion: courseData.duracion,
        estrategiaEvaluacion: courseData.evaluationStrategy.toLowerCase(),
        cupoMaximo: parseInt(courseData.cupoMaximo) || 30,
        modulos: modulos.map(modulo => ({
          titulo: modulo.titulo,
          descripcion: modulo.descripcion,
          materiales: modulo.materiales.map(mat => ({
            nombre: mat.nombre,
            descripcion: mat.descripcion
          })),
          evaluaciones: modulo.evaluaciones.map(evaluacion => ({
            nombre: evaluacion.nombre,
            descripcion: evaluacion.descripcion
          }))
        }))
      }

      console.log('📤 Enviando curso al backend:', payload)

      // Llamar al endpoint del backend
      const response = await fetch('http://localhost:8080/api/cursos/crear-completo', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
      })

      console.log('📥 Respuesta del servidor:', response.status, response.statusText)

      if (!response.ok) {
        const errorText = await response.text()
        console.error('❌ Error HTTP:', response.status, errorText)
        alert(`Error HTTP ${response.status}: ${errorText}`)
        setIsCreating(false)
        return
      }

      const result = await response.json()
      console.log('📊 Resultado:', result)

      if (result.exito) {
        alert(`Curso creado exitosamente!\n\n` +
              `ID: ${result.cursoId}\n` +
              `Código: ${result.codigo}\n` +
              `Tipo: ${result.tipoCurso}\n` +
              `Módulos: ${result.totalModulos}\n` +
              `Materiales: ${result.totalMateriales}\n` +
              `Evaluaciones: ${result.totalEvaluaciones}\n` +
              `Factory usada: ${result.factoryUsada}`)
        onClose()
      } else {
        console.error('❌ Error del servidor:', result.mensaje)
        alert('Error al crear el curso: ' + result.mensaje)
      }
    } catch (error) {
      console.error('❌ Error completo:', error)
      alert('Error al comunicarse con el servidor: ' + error)
    } finally {
      setIsCreating(false)
    }
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
            {step === 1 && 'Información Básica y Asignación'}
            {step === 2 && 'Agregar Módulos'}
            {step === 3 && 'Configurar Evaluaciones'}
            {step === 4 && 'Revisión y Confirmación'}
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
                    Cupo máximo: {limiteCupos[courseData.type]} estudiantes (Chain of Responsibility)
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
                    Validado con Chain of Responsibility - Hoy: {new Date().toLocaleDateString('es-ES')}
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
                        {profesores.length} profesores disponibles desde la BD
                      </p>
                    )}
                  </>
                )}
              </div>

              {/* Resumen de validaciones */}
              {Object.keys(errors).length > 0 && (
                <div className="p-4 bg-red-50 dark:bg-red-900/20 rounded-lg border border-red-200 dark:border-red-800">
                  <p className="text-sm text-red-800 dark:text-red-200 font-medium">
                    Por favor completa todos los campos requeridos antes de continuar
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
              <div className="p-4 rounded-lg bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 mb-4">
                <p className="text-sm text-blue-800 dark:text-blue-200 font-medium mb-2">
                  Agregar Módulos al Curso
                </p>
                <p className="text-xs text-blue-600 dark:text-blue-300">
                  Los módulos organizan el contenido del curso. Puedes agregar materiales y evaluaciones a cada módulo en el siguiente paso.
                </p>
              </div>

              {/* Formulario para agregar nuevo módulo */}
              <div className="p-4 rounded-lg border border-border bg-background">
                <h3 className="font-medium text-foreground mb-3">Nuevo Módulo</h3>
                <div className="space-y-3">
                  <div>
                    <label className="text-sm font-medium text-foreground block mb-1">
                      Título del Módulo *
                    </label>
                    <input
                      type="text"
                      placeholder="Ej: Módulo 1: Introducción a la Programación"
                      value={moduloActual.titulo}
                      onChange={(e) => setModuloActual({ ...moduloActual, titulo: e.target.value })}
                      className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                    />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-foreground block mb-1">
                      Descripción
                    </label>
                    <textarea
                      placeholder="Describe el contenido de este módulo..."
                      value={moduloActual.descripcion}
                      onChange={(e) => setModuloActual({ ...moduloActual, descripcion: e.target.value })}
                      className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary h-20 resize-none"
                    />
                  </div>
                  <Button
                    onClick={agregarModulo}
                    className="w-full bg-primary hover:bg-primary/90 text-primary-foreground"
                  >
                    + Agregar Módulo
                  </Button>
                </div>
              </div>

              {/* Lista de módulos agregados */}
              {modulos.length > 0 && (
                <div className="space-y-2">
                  <h3 className="font-medium text-foreground">Módulos Agregados ({modulos.length})</h3>
                  {modulos.map((modulo, index) => (
                    <div key={index} className="p-4 rounded-lg bg-muted/50 border border-border">
                      <div className="flex justify-between items-start">
                        <div className="flex-1">
                          <p className="font-medium text-foreground">Módulo {index + 1}: {modulo.titulo}</p>
                          {modulo.descripcion && (
                            <p className="text-sm text-muted-foreground mt-1">{modulo.descripcion}</p>
                          )}
                          <p className="text-xs text-muted-foreground mt-2">
                            {modulo.materiales.length} material(es), {modulo.evaluaciones.length} evaluación(es)
                          </p>
                        </div>
                        <button
                          onClick={() => eliminarModulo(index)}
                          className="text-destructive hover:text-destructive/90 ml-2"
                        >
                          ✕
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              )}

              {modulos.length === 0 && (
                <div className="p-6 text-center border-2 border-dashed border-border rounded-lg">
                  <p className="text-muted-foreground">No hay módulos agregados aún.</p>
                  <p className="text-sm text-muted-foreground mt-1">Agrega al menos un módulo para continuar.</p>
                </div>
              )}
            </div>
          )}

          {step === 3 && (
            <div className="space-y-4">
              <div className="p-4 rounded-lg bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 mb-4">
                <p className="text-sm text-blue-800 dark:text-blue-200 font-medium mb-2">
                  Agregar Contenido a los Módulos
                </p>
                <p className="text-xs text-blue-600 dark:text-blue-300">
                  Selecciona un módulo del dropdown y agrega materiales o evaluaciones. Los contenidos se crearán usando el patrón <strong>Abstract Factory</strong> según el tipo de curso ({courseData.type}).
                </p>
              </div>

              {/* Dropdown para seleccionar módulo (OBLIGATORIO) */}
              <div className="p-4 rounded-lg border-2 border-primary/30 bg-primary/5">
                <label className="text-sm font-medium text-foreground block mb-2">
                  Selecciona el Módulo *
                  <span className="ml-2 text-xs text-muted-foreground">
                    (Obligatorio para agregar contenido)
                  </span>
                </label>
                <select
                  value={moduloSeleccionadoIndex}
                  onChange={(e) => setModuloSeleccionadoIndex(parseInt(e.target.value))}
                  className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value={-1}>-- Selecciona un módulo --</option>
                  {modulos.map((modulo, index) => (
                    <option key={index} value={index}>
                      Módulo {index + 1}: {modulo.titulo}
                    </option>
                  ))}
                </select>
                {moduloSeleccionadoIndex !== -1 && (
                  <p className="text-xs text-green-600 dark:text-green-400 mt-2">
                    Módulo seleccionado: <strong>{modulos[moduloSeleccionadoIndex].titulo}</strong>
                  </p>
                )}
              </div>

              {/* Formulario para agregar material */}
              <div className={`p-4 rounded-lg border ${moduloSeleccionadoIndex === -1 ? 'border-muted bg-muted/20 opacity-50' : 'border-border bg-background'}`}>
                <h3 className="font-medium text-foreground mb-3">
                  Agregar Material
                  {moduloSeleccionadoIndex !== -1 && (
                    <span className="text-sm text-muted-foreground ml-2">
                      (al módulo: {modulos[moduloSeleccionadoIndex].titulo})
                    </span>
                  )}
                </h3>
                <div className="space-y-3">
                  <div>
                    <label className="text-sm font-medium text-foreground block mb-1">
                      Nombre del Material *
                    </label>
                    <input
                      type="text"
                      placeholder="Ej: Video de Introducción"
                      value={materialActual.nombre}
                      onChange={(e) => setMaterialActual({ ...materialActual, nombre: e.target.value })}
                      disabled={moduloSeleccionadoIndex === -1}
                      className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50"
                    />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-foreground block mb-1">
                      Descripción
                    </label>
                    <input
                      type="text"
                      placeholder="Descripción del material..."
                      value={materialActual.descripcion}
                      onChange={(e) => setMaterialActual({ ...materialActual, descripcion: e.target.value })}
                      disabled={moduloSeleccionadoIndex === -1}
                      className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50"
                    />
                  </div>
                  <Button
                    onClick={agregarMaterial}
                    disabled={moduloSeleccionadoIndex === -1}
                    className="w-full bg-green-600 hover:bg-green-700 text-white disabled:opacity-50"
                  >
                    + Agregar Material
                  </Button>
                </div>
              </div>

              {/* Materiales del módulo seleccionado */}
              {moduloSeleccionadoIndex !== -1 && modulos[moduloSeleccionadoIndex].materiales.length > 0 && (
                <div className="p-3 rounded-lg bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800">
                  <p className="text-sm font-medium text-green-800 dark:text-green-200 mb-2">
                    Materiales del módulo ({modulos[moduloSeleccionadoIndex].materiales.length})
                  </p>
                  <div className="space-y-1">
                    {modulos[moduloSeleccionadoIndex].materiales.map((material, index) => (
                      <div key={index} className="flex justify-between items-center text-sm">
                        <span className="text-green-700 dark:text-green-300">• {material.nombre}</span>
                        <button
                          onClick={() => eliminarMaterial(index)}
                          className="text-red-600 hover:text-red-700 dark:text-red-400"
                        >
                          ✕
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Formulario para agregar evaluación */}
              <div className={`p-4 rounded-lg border ${moduloSeleccionadoIndex === -1 ? 'border-muted bg-muted/20 opacity-50' : 'border-border bg-background'}`}>
                <h3 className="font-medium text-foreground mb-3">
                  Agregar Evaluación
                  {moduloSeleccionadoIndex !== -1 && (
                    <span className="text-sm text-muted-foreground ml-2">
                      (al módulo: {modulos[moduloSeleccionadoIndex].titulo})
                    </span>
                  )}
                </h3>
                <div className="space-y-3">
                  <div>
                    <label className="text-sm font-medium text-foreground block mb-1">
                      Nombre de la Evaluación *
                    </label>
                    <input
                      type="text"
                      placeholder="Ej: Quiz Módulo 1"
                      value={evaluacionActual.nombre}
                      onChange={(e) => setEvaluacionActual({ ...evaluacionActual, nombre: e.target.value })}
                      disabled={moduloSeleccionadoIndex === -1}
                      className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50"
                    />
                  </div>
                  <div>
                    <label className="text-sm font-medium text-foreground block mb-1">
                      Descripción
                    </label>
                    <input
                      type="text"
                      placeholder="Descripción de la evaluación..."
                      value={evaluacionActual.descripcion}
                      onChange={(e) => setEvaluacionActual({ ...evaluacionActual, descripcion: e.target.value })}
                      disabled={moduloSeleccionadoIndex === -1}
                      className="w-full px-3 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary disabled:opacity-50"
                    />
                  </div>
                  <Button
                    onClick={agregarEvaluacion}
                    disabled={moduloSeleccionadoIndex === -1}
                    className="w-full bg-purple-600 hover:bg-purple-700 text-white disabled:opacity-50"
                  >
                    + Agregar Evaluación
                  </Button>
                </div>
              </div>

              {/* Evaluaciones del módulo seleccionado */}
              {moduloSeleccionadoIndex !== -1 && modulos[moduloSeleccionadoIndex].evaluaciones.length > 0 && (
                <div className="p-3 rounded-lg bg-purple-50 dark:bg-purple-900/20 border border-purple-200 dark:border-purple-800">
                  <p className="text-sm font-medium text-purple-800 dark:text-purple-200 mb-2">
                    Evaluaciones del módulo ({modulos[moduloSeleccionadoIndex].evaluaciones.length})
                  </p>
                  <div className="space-y-1">
                    {modulos[moduloSeleccionadoIndex].evaluaciones.map((evaluacion, index) => (
                      <div key={index} className="flex justify-between items-center text-sm">
                        <span className="text-purple-700 dark:text-purple-300">• {evaluacion.nombre}</span>
                        <button
                          onClick={() => eliminarEvaluacion(index)}
                          className="text-red-600 hover:text-red-700 dark:text-red-400"
                        >
                          ✕
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              <div className="p-4 rounded-lg bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800">
                <p className="text-xs text-amber-800 dark:text-amber-200">
                  💡 <strong>Tip:</strong> Este paso es opcional. Si no agregas materiales o evaluaciones ahora,
                  el curso se creará con los módulos vacíos y podrás agregar contenido después.
                  Los materiales y evaluaciones se asociarán al <strong>módulo seleccionado</strong> ({moduloSeleccionadoIndex !== -1 ? modulos[moduloSeleccionadoIndex].titulo : 'ninguno'}).
                </p>
              </div>
            </div>
          )}

          {step === 4 && (
            <div className="space-y-4">
              <div className="p-4 rounded-lg bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 mb-4">
                <p className="text-sm text-blue-800 dark:text-blue-200 font-medium mb-2">
                  Revisión Final
                </p>
                <p className="text-xs text-blue-600 dark:text-blue-300">
                  Revisa la información antes de crear el curso. Se usarán los patrones <strong>Abstract Factory ({courseData.type})</strong> y <strong>Builder</strong> para crear el curso completo.
                </p>
              </div>

              {/* Información básica */}
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground mb-2">Información del Curso</p>
                <div className="space-y-2">
                  <div>
                    <span className="text-xs text-muted-foreground">Nombre: </span>
                    <span className="font-semibold text-foreground">{courseData.name}</span>
                  </div>
                  <div>
                    <span className="text-xs text-muted-foreground">Tipo: </span>
                    <span className="font-semibold text-foreground">{courseData.type}</span>
                  </div>
                  <div>
                    <span className="text-xs text-muted-foreground">Período: </span>
                    <span className="font-semibold text-foreground">{courseData.period}</span>
                  </div>
                  <div>
                    <span className="text-xs text-muted-foreground">Profesor: </span>
                    <span className="font-semibold text-foreground">{courseData.professorName || 'Sin asignar'}</span>
                  </div>
                  <div>
                    <span className="text-xs text-muted-foreground">Estrategia: </span>
                    <span className="font-semibold text-foreground">{courseData.evaluationStrategy}</span>
                  </div>
                </div>
              </div>

              {/* Descripción */}
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground mb-1">Descripción</p>
                <p className="text-sm text-foreground">{courseData.description}</p>
              </div>

              {/* Resumen de módulos */}
              <div className="p-4 rounded-lg bg-muted/50 border border-border">
                <p className="text-sm text-muted-foreground mb-3">
                  Estructura del Curso ({modulos.length} módulo(s))
                </p>
                <div className="space-y-3">
                  {modulos.map((modulo, index) => (
                    <div key={index} className="p-3 rounded-lg bg-background border border-border">
                      <p className="font-medium text-foreground text-sm">
                        Módulo {index + 1}: {modulo.titulo}
                      </p>
                      {modulo.descripcion && (
                        <p className="text-xs text-muted-foreground mt-1">{modulo.descripcion}</p>
                      )}
                      <div className="mt-2 flex gap-4 text-xs">
                        <span className="text-green-600 dark:text-green-400">
                          {modulo.materiales.length} material(es)
                        </span>
                        <span className="text-purple-600 dark:text-purple-400">
                          {modulo.evaluaciones.length} evaluación(es)
                        </span>
                      </div>

                      {/* Detalles de materiales */}
                      {modulo.materiales.length > 0 && (
                        <div className="mt-2 pl-3 border-l-2 border-green-200 dark:border-green-800">
                          <p className="text-xs font-medium text-green-700 dark:text-green-300 mb-1">Materiales:</p>
                          {modulo.materiales.map((mat, idx) => (
                            <p key={idx} className="text-xs text-muted-foreground">• {mat.nombre}</p>
                          ))}
                        </div>
                      )}

                      {/* Detalles de evaluaciones */}
                      {modulo.evaluaciones.length > 0 && (
                        <div className="mt-2 pl-3 border-l-2 border-purple-200 dark:border-purple-800">
                          <p className="text-xs font-medium text-purple-700 dark:text-purple-300 mb-1">Evaluaciones:</p>
                          {modulo.evaluaciones.map((evaluacion, idx) => (
                            <p key={idx} className="text-xs text-muted-foreground">• {evaluacion.nombre}</p>
                          ))}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </div>

              {/* Resumen estadístico */}
              <div className="p-4 rounded-lg bg-gradient-to-r from-primary/10 to-primary/5 border border-primary/20">
                <p className="text-sm font-medium text-foreground mb-3">📊 Resumen del Curso</p>
                <div className="grid grid-cols-3 gap-4">
                  <div className="text-center">
                    <p className="text-2xl font-bold text-primary">{modulos.length}</p>
                    <p className="text-xs text-muted-foreground">Módulos</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-green-600">
                      {modulos.reduce((sum, m) => sum + m.materiales.length, 0)}
                    </p>
                    <p className="text-xs text-muted-foreground">Materiales</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-purple-600">
                      {modulos.reduce((sum, m) => sum + m.evaluaciones.length, 0)}
                    </p>
                    <p className="text-xs text-muted-foreground">Evaluaciones</p>
                  </div>
                </div>
              </div>

              {/* Información sobre patrones */}
              <div className="p-4 rounded-lg bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800">
                <p className="text-xs text-amber-800 dark:text-amber-200">
                  🏗️ <strong>Patrones de Diseño:</strong> Este curso se creará usando:
                </p>
                <ul className="text-xs text-amber-700 dark:text-amber-300 mt-2 ml-4 space-y-1">
                  <li>• <strong>Abstract Factory:</strong> Para crear componentes específicos del tipo {courseData.type}</li>
                  <li>• <strong>Builder:</strong> Para construir el curso paso a paso con todos sus componentes</li>
                  <li>• <strong>Strategy:</strong> Estrategia de evaluación: {courseData.evaluationStrategy}</li>
                </ul>
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
          disabled={isCreating}
        >
          {step === 1 ? 'Cancelar' : 'Anterior'}
        </Button>
        <Button
          onClick={step === 4 ? handleCreate : handleNext}
          className="bg-primary hover:bg-primary/90 text-primary-foreground"
          disabled={isCreating}
        >
          {step === 4 ? (isCreating ? 'Creando...' : 'Crear Curso') : 'Siguiente'}
        </Button>
      </div>
    </div>
  )
}
