"use client"

import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { CheckCircle2, XCircle, Loader2, Send, Settings, Bell, BookOpen, Layers } from "lucide-react"

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api"

export default function PatronesPage() {
  const [loading, setLoading] = useState(false)
  const [result, setResult] = useState<any>(null)
  const [error, setError] = useState<string | null>(null)

  // Singleton State
  const [configuraciones, setConfiguraciones] = useState<any>(null)
  const [configKey, setConfigKey] = useState("")
  const [configValue, setConfigValue] = useState("")

  // Factory Method State
  const [notificaciones, setNotificaciones] = useState<any[]>([])
  const [tipoNotif, setTipoNotif] = useState("EMAIL")
  const [destinatario, setDestinatario] = useState("")
  const [asunto, setAsunto] = useState("")
  const [mensaje, setMensaje] = useState("")

  // Abstract Factory State
  const [contenidos, setContenidos] = useState<any[]>([])
  const [nivelContenido, setNivelContenido] = useState("BASICO")
  const [tipoContenido, setTipoContenido] = useState("VIDEO")
  const [cursoId, setCursoId] = useState("1")

  // Builder State
  const [cursos, setCursos] = useState<any[]>([])
  const [tipoCurso, setTipoCurso] = useState("REGULAR")
  const [nombreCurso, setNombreCurso] = useState("")
  const [periodoAcademico, setPeriodoAcademico] = useState("2025-1")
  const [profesorId, setProfesorId] = useState("1")

  // Prototype State
  const [cursoSeleccionado, setCursoSeleccionado] = useState<any>(null)
  const [nombreClon, setNombreClon] = useState("")
  const [periodoClon, setPeriodoClon] = useState("2025-2")
  const [tipoPlantilla, setTipoPlantilla] = useState("REGULAR")
  const [nombrePlantilla, setNombrePlantilla] = useState("")
  const [periodoPlantilla, setPeriodoPlantilla] = useState("2025-1")
  const [profesorPlantilla, setProfesorPlantilla] = useState("1")

  // Adapter State
  const [integraciones, setIntegraciones] = useState<any[]>([])
  const [tipoSistema, setTipoSistema] = useState("ZOOM")
  const [nombreIntegracion, setNombreIntegracion] = useState("")
  const [apiKey, setApiKey] = useState("")
  const [apiSecret, setApiSecret] = useState("")
  const [integracionSeleccionada, setIntegracionSeleccionada] = useState<any>(null)
  const [nombreSala, setNombreSala] = useState("")
  const [duracionReunion, setDuracionReunion] = useState("60")

  // Bridge State
  const [reportes, setReportes] = useState<any[]>([])
  const [tipoReporte, setTipoReporte] = useState("ESTUDIANTES")
  const [formatoReporte, setFormatoReporte] = useState("PDF")

  // Composite State
  const [modulos, setModulos] = useState<any[]>([])
  const [cursoIdModulos, setCursoIdModulos] = useState("1")
  const [tipoEstructura, setTipoEstructura] = useState("BASICO")
  const [arbolModulos, setArbolModulos] = useState<any>(null)

  // ========== SINGLETON ==========
  const cargarConfiguraciones = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/configuraciones`)
      const data = await response.json()
      setConfiguraciones(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const actualizarConfiguracion = async () => {
    if (!configKey || !configValue) {
      setError("Debes proporcionar clave y valor")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/configuraciones/${configKey}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ valor: configValue })
      })
      const data = await response.json()
      setResult(data)
      setConfigKey("")
      setConfigValue("")
      cargarConfiguraciones()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verDemoSingleton = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/configuraciones/demo`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verEstadisticasSingleton = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/configuraciones/estadisticas`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== FACTORY METHOD ==========
  const cargarNotificaciones = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/notificaciones`)
      const data = await response.json()
      setNotificaciones(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const enviarNotificacion = async () => {
    if (!destinatario || !mensaje) {
      setError("Debes proporcionar destinatario y mensaje")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/notificaciones`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          tipo: tipoNotif,
          destinatario,
          asunto,
          mensaje
        })
      })
      const data = await response.json()
      setResult(data)
      setDestinatario("")
      setAsunto("")
      setMensaje("")
      cargarNotificaciones()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verDemoFactory = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/notificaciones/demo`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verEstadisticasNotif = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/notificaciones/estadisticas`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== ABSTRACT FACTORY ==========
  const cargarContenidos = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/contenidos`)
      const data = await response.json()
      setContenidos(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearContenido = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/contenidos/crear`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nivel: nivelContenido,
          tipo: tipoContenido,
          cursoId: parseInt(cursoId)
        })
      })
      const data = await response.json()
      setResult(data)
      cargarContenidos()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearFamiliaCompleta = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/contenidos/crear-familia`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nivel: nivelContenido,
          cursoId: parseInt(cursoId)
        })
      })
      const data = await response.json()
      setResult(data)
      cargarContenidos()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verDemoAbstractFactory = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/contenidos/demo`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verEstadisticasContenidos = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/contenidos/estadisticas`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== BUILDER ==========
  const cargarCursos = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/cursos`)
      const data = await response.json()
      setCursos(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearCursoConDirector = async () => {
    if (!nombreCurso) {
      setError("El nombre del curso es obligatorio")
      return
    }

    setLoading(true)
    setError(null)
    try {
      let endpoint = ""
      let body: any = { nombre: nombreCurso }

      switch (tipoCurso) {
        case "REGULAR":
          endpoint = "/cursos/builder/regular"
          body.periodoAcademico = periodoAcademico
          break
        case "INTENSIVO":
          endpoint = "/cursos/builder/intensivo"
          body.profesorId = parseInt(profesorId)
          break
        case "CERTIFICACION":
          endpoint = "/cursos/builder/certificacion"
          body.profesorId = parseInt(profesorId)
          body.periodoAcademico = periodoAcademico
          break
      }

      const response = await fetch(`${API_URL}${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      })
      const data = await response.json()
      setResult(data)
      setNombreCurso("")
      cargarCursos()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== PROTOTYPE ==========
  const clonarCurso = async () => {
    if (!cursoSeleccionado) {
      setError("Debes seleccionar un curso para clonar")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const body = nombreClon || periodoClon !== "2025-2"
        ? { nombre: nombreClon, periodoAcademico: periodoClon }
        : {}

      const response = await fetch(`${API_URL}/cursos/${cursoSeleccionado.id}/clonar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      })
      const data = await response.json()
      setResult(data)
      setNombreClon("")
      setPeriodoClon("2025-2")
      cargarCursos()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearDesdePlantilla = async () => {
    if (!nombrePlantilla) {
      setError("El nombre del curso es obligatorio")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const body: any = {
        nombre: nombrePlantilla,
        periodoAcademico: periodoPlantilla
      }

      if (tipoPlantilla === "INTENSIVO" || tipoPlantilla === "CERTIFICACION") {
        body.profesorId = profesorPlantilla
      }

      const response = await fetch(`${API_URL}/cursos/plantilla/${tipoPlantilla}/clonar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      })
      const data = await response.json()
      setResult(data)
      setNombrePlantilla("")
      cargarCursos()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== ADAPTER ==========
  const cargarIntegraciones = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/integraciones`)
      const data = await response.json()
      setIntegraciones(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearIntegracion = async () => {
    if (!nombreIntegracion) {
      setError("El nombre de la integración es obligatorio")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const body = {
        tipoSistema,
        nombreConfiguracion: nombreIntegracion,
        apiKey,
        apiSecret,
        estado: "ACTIVO"
      }

      const response = await fetch(`${API_URL}/integraciones`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      })
      const data = await response.json()
      setResult(data)
      setNombreIntegracion("")
      setApiKey("")
      setApiSecret("")
      cargarIntegraciones()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearReunion = async () => {
    if (!integracionSeleccionada) {
      setError("Debes seleccionar una integración")
      return
    }
    if (!nombreSala) {
      setError("El nombre de la sala es obligatorio")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const body = {
        nombreSala,
        duracion: parseInt(duracionReunion)
      }

      const response = await fetch(`${API_URL}/integraciones/${integracionSeleccionada.id}/crear-reunion`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      })
      const data = await response.json()
      setResult(data)
      setNombreSala("")
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verificarIntegracion = async () => {
    if (!integracionSeleccionada) {
      setError("Debes seleccionar una integración")
      return
    }

    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/integraciones/${integracionSeleccionada.id}/verificar`, {
        method: "POST"
      })
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== BRIDGE ==========
  const cargarReportes = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/reportes`)
      const data = await response.json()
      setReportes(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const generarReporte = async () => {
    setLoading(true)
    setError(null)
    try {
      const endpoint = tipoReporte === "ESTUDIANTES" ? "/reportes/estudiantes" :
                       tipoReporte === "CURSOS" ? "/reportes/cursos" :
                       "/reportes/calificaciones"

      const response = await fetch(`${API_URL}${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ formato: formatoReporte })
      })
      const data = await response.json()
      setResult(data)
      cargarReportes()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  // ========== COMPOSITE ==========
  const cargarModulos = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/modulos/curso/${cursoIdModulos}/arbol`)
      const data = await response.json()
      setArbolModulos(data)
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const crearEstructura = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/modulos/crear-estructura`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          cursoId: parseInt(cursoIdModulos),
          tipo: tipoEstructura
        })
      })
      const data = await response.json()
      setResult(data)
      cargarModulos()
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container mx-auto p-6">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-2">🎨 Patrones de Diseño</h1>
        <p className="text-muted-foreground">
          Demostración interactiva de los 23 patrones de diseño implementados en EduLearn
        </p>
        <div className="flex gap-2 mt-4">
          <Badge variant="default">8 Completados</Badge>
          <Badge variant="secondary">15 Pendientes</Badge>
        </div>
      </div>

      <Tabs defaultValue="singleton" className="space-y-4">
        <TabsList className="grid w-full grid-cols-8">
          <TabsTrigger value="singleton">
            <Settings className="mr-2 h-4 w-4" />
            Singleton
          </TabsTrigger>
          <TabsTrigger value="factory">
            <Bell className="mr-2 h-4 w-4" />
            Factory Method
          </TabsTrigger>
          <TabsTrigger value="abstractfactory">
            <BookOpen className="mr-2 h-4 w-4" />
            Abstract Factory
          </TabsTrigger>
          <TabsTrigger value="builder">
            <Layers className="mr-2 h-4 w-4" />
            Builder
          </TabsTrigger>
          <TabsTrigger value="prototype">
            <Layers className="mr-2 h-4 w-4" />
            Prototype
          </TabsTrigger>
          <TabsTrigger value="adapter">
            <Send className="mr-2 h-4 w-4" />
            Adapter
          </TabsTrigger>
          <TabsTrigger value="bridge">
            <BookOpen className="mr-2 h-4 w-4" />
            Bridge
          </TabsTrigger>
          <TabsTrigger value="composite">
            <Layers className="mr-2 h-4 w-4" />
            Composite
          </TabsTrigger>
        </TabsList>

        {/* ========== SINGLETON TAB ========== */}
        <TabsContent value="singleton" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Singleton
              </CardTitle>
              <CardDescription>
                Garantiza una única instancia de ConfiguracionSistemaManager con acceso global
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Botones de acción */}
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarConfiguraciones} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Configuraciones
                </Button>
                <Button onClick={verDemoSingleton} variant="outline" disabled={loading}>
                  Ver Demo
                </Button>
                <Button onClick={verEstadisticasSingleton} variant="outline" disabled={loading}>
                  Ver Estadísticas
                </Button>
              </div>

              {/* Formulario actualizar */}
              <div className="border rounded-lg p-4 space-y-4">
                <h3 className="font-semibold">Actualizar Configuración</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="configKey">Clave</Label>
                    <Input
                      id="configKey"
                      value={configKey}
                      onChange={(e) => setConfigKey(e.target.value)}
                      placeholder="cupo_default"
                    />
                  </div>
                  <div>
                    <Label htmlFor="configValue">Valor</Label>
                    <Input
                      id="configValue"
                      value={configValue}
                      onChange={(e) => setConfigValue(e.target.value)}
                      placeholder="50"
                    />
                  </div>
                </div>
                <Button onClick={actualizarConfiguracion} disabled={loading}>
                  <Send className="mr-2 h-4 w-4" />
                  Actualizar
                </Button>
              </div>

              {/* Mostrar configuraciones */}
              {configuraciones && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Configuraciones Actuales</h3>
                  <div className="grid grid-cols-2 gap-2 text-sm">
                    {Object.entries(configuraciones).map(([key, value]) => (
                      <div key={key} className="flex justify-between">
                        <span className="font-mono text-muted-foreground">{key}:</span>
                        <span className="font-semibold">{value as string}</span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== FACTORY METHOD TAB ========== */}
        <TabsContent value="factory" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Factory Method
              </CardTitle>
              <CardDescription>
                Crea diferentes tipos de notificaciones (EMAIL, SMS, PUSH) sin conocer las clases concretas
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Botones de acción */}
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarNotificaciones} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Notificaciones
                </Button>
                <Button onClick={verDemoFactory} variant="outline" disabled={loading}>
                  Ver Demo
                </Button>
                <Button onClick={verEstadisticasNotif} variant="outline" disabled={loading}>
                  Ver Estadísticas
                </Button>
              </div>

              {/* Formulario enviar */}
              <div className="border rounded-lg p-4 space-y-4">
                <h3 className="font-semibold">Enviar Notificación</h3>
                <div className="space-y-4">
                  <div>
                    <Label htmlFor="tipo">Tipo</Label>
                    <select
                      id="tipo"
                      className="w-full border rounded-md p-2"
                      value={tipoNotif}
                      onChange={(e) => setTipoNotif(e.target.value)}
                    >
                      <option value="EMAIL">EMAIL</option>
                      <option value="SMS">SMS</option>
                      <option value="PUSH">PUSH</option>
                    </select>
                  </div>
                  <div>
                    <Label htmlFor="destinatario">Destinatario</Label>
                    <Input
                      id="destinatario"
                      value={destinatario}
                      onChange={(e) => setDestinatario(e.target.value)}
                      placeholder={
                        tipoNotif === "EMAIL" ? "usuario@example.com" :
                        tipoNotif === "SMS" ? "+573001234567" :
                        "device_token_largo_123456789"
                      }
                    />
                  </div>
                  <div>
                    <Label htmlFor="asunto">Asunto</Label>
                    <Input
                      id="asunto"
                      value={asunto}
                      onChange={(e) => setAsunto(e.target.value)}
                      placeholder="Asunto de la notificación"
                    />
                  </div>
                  <div>
                    <Label htmlFor="mensaje">Mensaje</Label>
                    <Input
                      id="mensaje"
                      value={mensaje}
                      onChange={(e) => setMensaje(e.target.value)}
                      placeholder="Mensaje de la notificación"
                    />
                  </div>
                </div>
                <Button onClick={enviarNotificacion} disabled={loading}>
                  <Send className="mr-2 h-4 w-4" />
                  Enviar Notificación
                </Button>
              </div>

              {/* Mostrar notificaciones */}
              {notificaciones.length > 0 && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Notificaciones Recientes</h3>
                  <div className="space-y-2">
                    {notificaciones.slice(-5).reverse().map((notif) => (
                      <div key={notif.id} className="flex items-center justify-between border-b pb-2">
                        <div>
                          <Badge variant={notif.estado === "ENVIADA" ? "default" : "destructive"}>
                            {notif.tipo}
                          </Badge>
                          <span className="ml-2 text-sm">{notif.destinatario}</span>
                        </div>
                        <span className="text-xs text-muted-foreground">
                          {notif.estado}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== ABSTRACT FACTORY TAB ========== */}
        <TabsContent value="abstractfactory" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Abstract Factory
              </CardTitle>
              <CardDescription>
                Crea familias de contenidos educativos (Video, Documento, Quiz) para diferentes niveles (Básico, Intermedio, Avanzado)
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Botones de acción */}
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarContenidos} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Contenidos
                </Button>
                <Button onClick={verDemoAbstractFactory} variant="outline" disabled={loading}>
                  Ver Demo
                </Button>
                <Button onClick={verEstadisticasContenidos} variant="outline" disabled={loading}>
                  Ver Estadísticas
                </Button>
              </div>

              {/* Formulario crear contenido individual */}
              <div className="border rounded-lg p-4 space-y-4">
                <h3 className="font-semibold">Crear Contenido Individual</h3>
                <div className="space-y-4">
                  <div className="grid grid-cols-3 gap-4">
                    <div>
                      <Label htmlFor="nivel">Nivel</Label>
                      <select
                        id="nivel"
                        className="w-full border rounded-md p-2"
                        value={nivelContenido}
                        onChange={(e) => setNivelContenido(e.target.value)}
                      >
                        <option value="BASICO">BÁSICO</option>
                        <option value="INTERMEDIO">INTERMEDIO</option>
                        <option value="AVANZADO">AVANZADO</option>
                      </select>
                    </div>
                    <div>
                      <Label htmlFor="tipoContenido">Tipo</Label>
                      <select
                        id="tipoContenido"
                        className="w-full border rounded-md p-2"
                        value={tipoContenido}
                        onChange={(e) => setTipoContenido(e.target.value)}
                      >
                        <option value="VIDEO">VIDEO</option>
                        <option value="DOCUMENTO">DOCUMENTO</option>
                        <option value="QUIZ">QUIZ</option>
                      </select>
                    </div>
                    <div>
                      <Label htmlFor="cursoId">Curso ID</Label>
                      <Input
                        id="cursoId"
                        type="number"
                        value={cursoId}
                        onChange={(e) => setCursoId(e.target.value)}
                        placeholder="1"
                      />
                    </div>
                  </div>
                </div>
                <Button onClick={crearContenido} disabled={loading}>
                  <Send className="mr-2 h-4 w-4" />
                  Crear Contenido
                </Button>
              </div>

              {/* Formulario crear familia completa */}
              <div className="border rounded-lg p-4 space-y-4 bg-blue-50 dark:bg-blue-950">
                <h3 className="font-semibold">Crear Familia Completa (Video + Documento + Quiz)</h3>
                <p className="text-sm text-muted-foreground">
                  Crea los 3 tipos de contenido para un nivel específico en una sola operación
                </p>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="nivelFamilia">Nivel</Label>
                    <select
                      id="nivelFamilia"
                      className="w-full border rounded-md p-2"
                      value={nivelContenido}
                      onChange={(e) => setNivelContenido(e.target.value)}
                    >
                      <option value="BASICO">BÁSICO</option>
                      <option value="INTERMEDIO">INTERMEDIO</option>
                      <option value="AVANZADO">AVANZADO</option>
                    </select>
                  </div>
                  <div>
                    <Label htmlFor="cursoIdFamilia">Curso ID</Label>
                    <Input
                      id="cursoIdFamilia"
                      type="number"
                      value={cursoId}
                      onChange={(e) => setCursoId(e.target.value)}
                      placeholder="1"
                    />
                  </div>
                </div>
                <Button onClick={crearFamiliaCompleta} disabled={loading} className="w-full">
                  <BookOpen className="mr-2 h-4 w-4" />
                  Crear Familia Completa
                </Button>
              </div>

              {/* Mostrar contenidos */}
              {contenidos.length > 0 && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Contenidos Creados ({contenidos.length})</h3>
                  <div className="space-y-2">
                    {contenidos.slice(-6).reverse().map((cont) => (
                      <div key={cont.id} className="flex items-center justify-between border-b pb-2">
                        <div className="flex gap-2">
                          <Badge variant={
                            cont.nivel === "BASICO" ? "secondary" :
                            cont.nivel === "INTERMEDIO" ? "default" :
                            "destructive"
                          }>
                            {cont.nivel}
                          </Badge>
                          <Badge variant="outline">{cont.tipo}</Badge>
                          <span className="text-sm">{cont.duracionEstimada} min</span>
                        </div>
                        <span className="text-xs text-muted-foreground">
                          Curso #{cont.cursoId}
                        </span>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== BUILDER TAB ========== */}
        <TabsContent value="builder" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Builder
              </CardTitle>
              <CardDescription>
                Construir cursos complejos paso a paso usando Builder + Director con diferentes configuraciones predefinidas
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Botones de acción */}
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarCursos} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Cursos
                </Button>
              </div>

              {/* Formulario crear curso con Director */}
              <div className="border rounded-lg p-4 space-y-4 bg-blue-50 dark:bg-blue-950">
                <h3 className="font-semibold">Crear Curso con Director (Recetas Predefinidas)</h3>
                <p className="text-sm text-muted-foreground">
                  El Director encapsula diferentes formas de construir cursos con configuraciones predefinidas
                </p>
                <div className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="tipoCurso">Tipo de Curso (Director)</Label>
                      <select
                        id="tipoCurso"
                        className="w-full border rounded-md p-2"
                        value={tipoCurso}
                        onChange={(e) => setTipoCurso(e.target.value)}
                      >
                        <option value="REGULAR">REGULAR - 40hrs, Estado ACTIVO</option>
                        <option value="INTENSIVO">INTENSIVO - 80hrs, Mayor carga horaria</option>
                        <option value="CERTIFICACION">CERTIFICACIÓN - 60hrs, Certificación profesional</option>
                      </select>
                    </div>
                    <div>
                      <Label htmlFor="nombreCurso">Nombre del Curso</Label>
                      <Input
                        id="nombreCurso"
                        value={nombreCurso}
                        onChange={(e) => setNombreCurso(e.target.value)}
                        placeholder="Ej: Programación Java Avanzada"
                      />
                    </div>
                  </div>
                  {tipoCurso === "REGULAR" && (
                    <div>
                      <Label htmlFor="periodoAcademico">Período Académico</Label>
                      <Input
                        id="periodoAcademico"
                        value={periodoAcademico}
                        onChange={(e) => setPeriodoAcademico(e.target.value)}
                        placeholder="Ej: 2025-1"
                      />
                    </div>
                  )}
                  {(tipoCurso === "INTENSIVO" || tipoCurso === "CERTIFICACION") && (
                    <div>
                      <Label htmlFor="profesorId">ID del Profesor Titular</Label>
                      <Input
                        id="profesorId"
                        type="number"
                        value={profesorId}
                        onChange={(e) => setProfesorId(e.target.value)}
                        placeholder="Ej: 1"
                      />
                    </div>
                  )}
                  {tipoCurso === "CERTIFICACION" && (
                    <div>
                      <Label htmlFor="periodoAcademico">Período Académico</Label>
                      <Input
                        id="periodoAcademico"
                        value={periodoAcademico}
                        onChange={(e) => setPeriodoAcademico(e.target.value)}
                        placeholder="Ej: 2025-1"
                      />
                    </div>
                  )}
                </div>
                <Button onClick={crearCursoConDirector} disabled={loading} className="w-full">
                  <Layers className="mr-2 h-4 w-4" />
                  Construir Curso con Director
                </Button>
              </div>

              {/* Mostrar cursos */}
              {cursos.length > 0 && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Cursos Creados ({cursos.length})</h3>
                  <div className="space-y-2">
                    {cursos.slice(-6).reverse().map((curso) => (
                      <div key={curso.id} className="flex items-center justify-between border-b pb-2">
                        <div className="flex flex-col gap-1">
                          <div className="flex gap-2 items-center">
                            <span className="font-semibold">{curso.nombre}</span>
                            {curso.codigo && <Badge variant="outline">{curso.codigo}</Badge>}
                          </div>
                          <div className="flex gap-2 text-xs">
                            <Badge variant={
                              curso.tipoCurso === "INTENSIVO" ? "default" :
                              curso.tipoCurso === "CERTIFICACION" ? "secondary" :
                              "outline"
                            }>
                              {curso.tipoCurso}
                            </Badge>
                            <span>{curso.duracion}hrs</span>
                            {curso.periodoAcademico && <span>{curso.periodoAcademico}</span>}
                            {curso.profesorTitularId && <span>Prof: {curso.profesorTitularId}</span>}
                          </div>
                          {curso.descripcion && (
                            <p className="text-xs text-muted-foreground">{curso.descripcion}</p>
                          )}
                        </div>
                        <div className="text-xs">
                          <Badge variant={curso.estado === "ACTIVO" ? "default" : "secondary"}>
                            {curso.estado}
                          </Badge>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== PROTOTYPE TAB ========== */}
        <TabsContent value="prototype" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Prototype
              </CardTitle>
              <CardDescription>
                Clonar cursos existentes o crear desde plantillas predefinidas para reutilizar configuraciones
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Botón de cargar cursos */}
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarCursos} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Cursos
                </Button>
              </div>

              {/* Clonar curso existente */}
              <div className="border rounded-lg p-4 space-y-4 bg-blue-50 dark:bg-blue-950">
                <h3 className="font-semibold">Clonar Curso Existente</h3>
                <p className="text-sm text-muted-foreground">
                  Selecciona un curso existente para clonarlo con personalizaciones
                </p>
                <div className="space-y-4">
                  <div>
                    <Label htmlFor="cursoSeleccionado">Seleccionar Curso</Label>
                    <select
                      id="cursoSeleccionado"
                      className="w-full border rounded-md p-2"
                      value={cursoSeleccionado?.id || ""}
                      onChange={(e) => {
                        const curso = cursos.find(c => c.id === parseInt(e.target.value))
                        setCursoSeleccionado(curso || null)
                      }}
                    >
                      <option value="">-- Selecciona un curso --</option>
                      {cursos.map(curso => (
                        <option key={curso.id} value={curso.id}>
                          {curso.nombre} ({curso.tipoCurso}) - {curso.periodoAcademico || 'Sin período'}
                        </option>
                      ))}
                    </select>
                  </div>
                  {cursoSeleccionado && (
                    <>
                      <div>
                        <Label htmlFor="nombreClon">Nuevo Nombre (opcional)</Label>
                        <Input
                          id="nombreClon"
                          value={nombreClon}
                          onChange={(e) => setNombreClon(e.target.value)}
                          placeholder={`${cursoSeleccionado.nombre} (Copia)`}
                        />
                      </div>
                      <div>
                        <Label htmlFor="periodoClon">Nuevo Período Académico</Label>
                        <Input
                          id="periodoClon"
                          value={periodoClon}
                          onChange={(e) => setPeriodoClon(e.target.value)}
                          placeholder="Ej: 2025-2"
                        />
                      </div>
                    </>
                  )}
                </div>
                <Button onClick={clonarCurso} disabled={loading || !cursoSeleccionado} className="w-full">
                  <Layers className="mr-2 h-4 w-4" />
                  Clonar Curso
                </Button>
              </div>

              {/* Crear desde plantilla */}
              <div className="border rounded-lg p-4 space-y-4 bg-green-50 dark:bg-green-950">
                <h3 className="font-semibold">Crear Desde Plantilla</h3>
                <p className="text-sm text-muted-foreground">
                  Utiliza plantillas predefinidas para crear cursos rápidamente
                </p>
                <div className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="tipoPlantilla">Tipo de Plantilla</Label>
                      <select
                        id="tipoPlantilla"
                        className="w-full border rounded-md p-2"
                        value={tipoPlantilla}
                        onChange={(e) => setTipoPlantilla(e.target.value)}
                      >
                        <option value="REGULAR">REGULAR - 40hrs</option>
                        <option value="INTENSIVO">INTENSIVO - 80hrs</option>
                        <option value="CERTIFICACION">CERTIFICACIÓN - 60hrs</option>
                      </select>
                    </div>
                    <div>
                      <Label htmlFor="nombrePlantilla">Nombre del Curso</Label>
                      <Input
                        id="nombrePlantilla"
                        value={nombrePlantilla}
                        onChange={(e) => setNombrePlantilla(e.target.value)}
                        placeholder="Ej: Desarrollo Web Full Stack"
                      />
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="periodoPlantilla">Período Académico</Label>
                      <Input
                        id="periodoPlantilla"
                        value={periodoPlantilla}
                        onChange={(e) => setPeriodoPlantilla(e.target.value)}
                        placeholder="Ej: 2025-1"
                      />
                    </div>
                    {(tipoPlantilla === "INTENSIVO" || tipoPlantilla === "CERTIFICACION") && (
                      <div>
                        <Label htmlFor="profesorPlantilla">ID del Profesor</Label>
                        <Input
                          id="profesorPlantilla"
                          type="number"
                          value={profesorPlantilla}
                          onChange={(e) => setProfesorPlantilla(e.target.value)}
                          placeholder="Ej: 1"
                        />
                      </div>
                    )}
                  </div>
                </div>
                <Button onClick={crearDesdePlantilla} disabled={loading} className="w-full">
                  <Layers className="mr-2 h-4 w-4" />
                  Crear Desde Plantilla
                </Button>
              </div>

              {/* Mostrar cursos */}
              {cursos.length > 0 && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Cursos Disponibles ({cursos.length})</h3>
                  <div className="space-y-2">
                    {cursos.slice(-6).reverse().map((curso) => (
                      <div key={curso.id} className="flex items-center justify-between border-b pb-2">
                        <div className="flex flex-col gap-1">
                          <div className="flex gap-2 items-center">
                            <span className="font-semibold">{curso.nombre}</span>
                            {curso.codigo && <Badge variant="outline">{curso.codigo}</Badge>}
                          </div>
                          <div className="flex gap-2 text-xs">
                            <Badge variant={
                              curso.tipoCurso === "INTENSIVO" ? "default" :
                              curso.tipoCurso === "CERTIFICACION" ? "secondary" :
                              "outline"
                            }>
                              {curso.tipoCurso}
                            </Badge>
                            <span>{curso.duracion}hrs</span>
                            {curso.periodoAcademico && <span>{curso.periodoAcademico}</span>}
                            {curso.profesorTitularId && <span>Prof: {curso.profesorTitularId}</span>}
                          </div>
                        </div>
                        <div className="text-xs">
                          <Badge variant={curso.estado === "ACTIVO" ? "default" : "secondary"}>
                            {curso.estado}
                          </Badge>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== ADAPTER TAB ========== */}
        <TabsContent value="adapter" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Adapter
              </CardTitle>
              <CardDescription>
                Integrar sistemas externos de videoconferencia (Zoom, Google Meet, MS Teams) con una interfaz común
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              {/* Botones de acción */}
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarIntegraciones} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Integraciones
                </Button>
              </div>

              {/* Crear nueva integración */}
              <div className="border rounded-lg p-4 space-y-4 bg-blue-50 dark:bg-blue-950">
                <h3 className="font-semibold">Configurar Nueva Integración</h3>
                <p className="text-sm text-muted-foreground">
                  Conecta sistemas externos de videoconferencia usando el patrón Adapter
                </p>
                <div className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="tipoSistema">Sistema de Videoconferencia</Label>
                      <select
                        id="tipoSistema"
                        className="w-full border rounded-md p-2"
                        value={tipoSistema}
                        onChange={(e) => setTipoSistema(e.target.value)}
                      >
                        <option value="ZOOM">Zoom</option>
                        <option value="GOOGLE_MEET">Google Meet</option>
                        <option value="MS_TEAMS">Microsoft Teams</option>
                      </select>
                    </div>
                    <div>
                      <Label htmlFor="nombreIntegracion">Nombre de Configuración</Label>
                      <Input
                        id="nombreIntegracion"
                        value={nombreIntegracion}
                        onChange={(e) => setNombreIntegracion(e.target.value)}
                        placeholder="Ej: Zoom Principal"
                      />
                    </div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="apiKey">API Key</Label>
                      <Input
                        id="apiKey"
                        value={apiKey}
                        onChange={(e) => setApiKey(e.target.value)}
                        placeholder="Ej: sk_demo_key_12345"
                      />
                    </div>
                    <div>
                      <Label htmlFor="apiSecret">API Secret (opcional para Google Meet)</Label>
                      <Input
                        id="apiSecret"
                        type="password"
                        value={apiSecret}
                        onChange={(e) => setApiSecret(e.target.value)}
                        placeholder="Ej: secret_xyz"
                      />
                    </div>
                  </div>
                </div>
                <Button onClick={crearIntegracion} disabled={loading} className="w-full">
                  <Send className="mr-2 h-4 w-4" />
                  Crear Integración
                </Button>
              </div>

              {/* Crear reunión usando integración */}
              <div className="border rounded-lg p-4 space-y-4 bg-green-50 dark:bg-green-950">
                <h3 className="font-semibold">Crear Reunión (Patrón Adapter)</h3>
                <p className="text-sm text-muted-foreground">
                  Usa cualquier integración configurada con una interfaz común
                </p>
                <div className="space-y-4">
                  <div>
                    <Label htmlFor="integracionSeleccionada">Seleccionar Integración</Label>
                    <select
                      id="integracionSeleccionada"
                      className="w-full border rounded-md p-2"
                      value={integracionSeleccionada?.id || ""}
                      onChange={(e) => {
                        const integracion = integraciones.find(i => i.id === parseInt(e.target.value))
                        setIntegracionSeleccionada(integracion || null)
                      }}
                    >
                      <option value="">-- Selecciona una integración --</option>
                      {integraciones.map(integracion => (
                        <option key={integracion.id} value={integracion.id}>
                          {integracion.nombreConfiguracion} ({integracion.tipoSistema})
                        </option>
                      ))}
                    </select>
                  </div>
                  {integracionSeleccionada && (
                    <>
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <Label htmlFor="nombreSala">Nombre de la Reunión</Label>
                          <Input
                            id="nombreSala"
                            value={nombreSala}
                            onChange={(e) => setNombreSala(e.target.value)}
                            placeholder="Ej: Clase de Matemáticas"
                          />
                        </div>
                        <div>
                          <Label htmlFor="duracionReunion">Duración (minutos)</Label>
                          <Input
                            id="duracionReunion"
                            type="number"
                            value={duracionReunion}
                            onChange={(e) => setDuracionReunion(e.target.value)}
                            placeholder="60"
                          />
                        </div>
                      </div>
                      <div className="flex gap-2">
                        <Button onClick={crearReunion} disabled={loading} className="flex-1">
                          <Send className="mr-2 h-4 w-4" />
                          Crear Reunión
                        </Button>
                        <Button onClick={verificarIntegracion} disabled={loading} variant="outline">
                          Verificar Conexión
                        </Button>
                      </div>
                    </>
                  )}
                </div>
              </div>

              {/* Mostrar integraciones */}
              {integraciones.length > 0 && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Integraciones Configuradas ({integraciones.length})</h3>
                  <div className="space-y-2">
                    {integraciones.map((integracion) => (
                      <div key={integracion.id} className="flex items-center justify-between border-b pb-2">
                        <div className="flex flex-col gap-1">
                          <div className="flex gap-2 items-center">
                            <span className="font-semibold">{integracion.nombreConfiguracion}</span>
                            <Badge variant="outline">{integracion.tipoSistema}</Badge>
                          </div>
                          <div className="flex gap-2 text-xs">
                            {integracion.salaReunion && (
                              <span className="text-muted-foreground truncate max-w-md">
                                Última sala: {integracion.salaReunion}
                              </span>
                            )}
                          </div>
                        </div>
                        <div className="text-xs">
                          <Badge variant={integracion.estado === "ACTIVO" ? "default" : "secondary"}>
                            {integracion.estado}
                          </Badge>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== BRIDGE TAB ========== */}
        <TabsContent value="bridge" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Bridge
              </CardTitle>
              <CardDescription>
                Separar abstracción (tipo reporte) de implementación (formato) para generar reportes en PDF, Excel o HTML
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarReportes} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Reportes
                </Button>
              </div>

              <div className="border rounded-lg p-4 space-y-4 bg-blue-50 dark:bg-blue-950">
                <h3 className="font-semibold">Generar Reporte (Patrón Bridge)</h3>
                <p className="text-sm text-muted-foreground">
                  La abstracción (tipo de reporte) está separada de la implementación (formato)
                </p>
                <div className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="tipoReporte">Tipo de Reporte (Abstracción)</Label>
                      <select
                        id="tipoReporte"
                        className="w-full border rounded-md p-2"
                        value={tipoReporte}
                        onChange={(e) => setTipoReporte(e.target.value)}
                      >
                        <option value="ESTUDIANTES">Estudiantes</option>
                        <option value="CURSOS">Cursos</option>
                        <option value="CALIFICACIONES">Calificaciones</option>
                      </select>
                    </div>
                    <div>
                      <Label htmlFor="formatoReporte">Formato (Implementación)</Label>
                      <select
                        id="formatoReporte"
                        className="w-full border rounded-md p-2"
                        value={formatoReporte}
                        onChange={(e) => setFormatoReporte(e.target.value)}
                      >
                        <option value="PDF">PDF</option>
                        <option value="EXCEL">Excel</option>
                        <option value="HTML">HTML</option>
                      </select>
                    </div>
                  </div>
                </div>
                <Button onClick={generarReporte} disabled={loading} className="w-full">
                  <BookOpen className="mr-2 h-4 w-4" />
                  Generar Reporte
                </Button>
              </div>

              {reportes.length > 0 && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Reportes Generados ({reportes.length})</h3>
                  <div className="space-y-2">
                    {reportes.slice(-6).reverse().map((reporte) => (
                      <div key={reporte.id} className="flex items-center justify-between border-b pb-2">
                        <div className="flex flex-col gap-1">
                          <div className="flex gap-2 items-center">
                            <span className="font-semibold">{reporte.titulo}</span>
                            <Badge variant="outline">{reporte.formato}</Badge>
                          </div>
                          <div className="flex gap-2 text-xs text-muted-foreground">
                            <span>Tipo: {reporte.tipoReporte}</span>
                            {reporte.fechaGeneracion && (
                              <span>{new Date(reporte.fechaGeneracion).toLocaleString()}</span>
                            )}
                          </div>
                        </div>
                        <Badge variant={reporte.estado === "GENERADO" ? "default" : "secondary"}>
                          {reporte.estado}
                        </Badge>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        {/* ========== COMPOSITE TAB ========== */}
        <TabsContent value="composite" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <CheckCircle2 className="h-5 w-5 text-green-500" />
                Patrón Composite
              </CardTitle>
              <CardDescription>
                Estructura jerárquica de módulos y submódulos (árbol) con operaciones recursivas
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex gap-2 flex-wrap">
                <Button onClick={cargarModulos} disabled={loading}>
                  {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                  Cargar Estructura
                </Button>
              </div>

              <div className="border rounded-lg p-4 space-y-4 bg-blue-50 dark:bg-blue-950">
                <h3 className="font-semibold">Crear Estructura de Módulos (Patrón Composite)</h3>
                <p className="text-sm text-muted-foreground">
                  Crea una jerarquía de módulos y submódulos con diferentes niveles de complejidad
                </p>
                <div className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <Label htmlFor="cursoIdModulos">Curso ID</Label>
                      <Input
                        id="cursoIdModulos"
                        type="number"
                        value={cursoIdModulos}
                        onChange={(e) => setCursoIdModulos(e.target.value)}
                        placeholder="1"
                      />
                    </div>
                    <div>
                      <Label htmlFor="tipoEstructura">Tipo de Estructura</Label>
                      <select
                        id="tipoEstructura"
                        className="w-full border rounded-md p-2"
                        value={tipoEstructura}
                        onChange={(e) => setTipoEstructura(e.target.value)}
                      >
                        <option value="BASICO">BÁSICO - 2 módulos, 4 temas (16h total)</option>
                        <option value="INTERMEDIO">INTERMEDIO - 3 módulos, 9 temas (40h total)</option>
                        <option value="AVANZADO">AVANZADO - 4 módulos jerárquicos (72h total)</option>
                      </select>
                    </div>
                  </div>
                </div>
                <Button onClick={crearEstructura} disabled={loading} className="w-full">
                  <Layers className="mr-2 h-4 w-4" />
                  Crear Estructura
                </Button>
              </div>

              {arbolModulos && (
                <div className="border rounded-lg p-4">
                  <h3 className="font-semibold mb-2">Estructura Jerárquica (Árbol Composite)</h3>
                  <div className="bg-slate-100 dark:bg-slate-900 p-4 rounded-lg">
                    <pre className="text-xs whitespace-pre-wrap font-mono">
                      {arbolModulos.arbolRenderizado || JSON.stringify(arbolModulos, null, 2)}
                    </pre>
                  </div>
                  {arbolModulos.duracionTotal && (
                    <div className="mt-4 p-3 bg-blue-100 dark:bg-blue-900 rounded-lg">
                      <p className="text-sm">
                        <span className="font-semibold">Duración Total Calculada (operación recursiva): </span>
                        {arbolModulos.duracionTotal} horas
                      </p>
                      <p className="text-xs text-muted-foreground mt-1">
                        El patrón Composite permite calcular la duración total recursivamente sumando todos los componentes del árbol
                      </p>
                    </div>
                  )}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Resultado */}
      {result && (
        <Card className="mt-4">
          <CardHeader>
            <CardTitle>Resultado</CardTitle>
          </CardHeader>
          <CardContent>
            <pre className="bg-slate-100 dark:bg-slate-900 p-4 rounded-lg overflow-auto text-xs">
              {JSON.stringify(result, null, 2)}
            </pre>
          </CardContent>
        </Card>
      )}

      {/* Error */}
      {error && (
        <Alert variant="destructive" className="mt-4">
          <XCircle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}
    </div>
  )
}
