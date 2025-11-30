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
  const [tipoCurso, setTipoCurso] = useState("BASICO")
  const [nombreCurso, setNombreCurso] = useState("")
  const [categoriaCurso, setCategoriaCurso] = useState("PROGRAMACION")

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
      const response = await fetch(`${API_URL}/cursos-builder`)
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
        case "BASICO":
          endpoint = "/cursos-builder/basico"
          break
        case "PREMIUM":
          endpoint = "/cursos-builder/premium"
          body.categoria = categoriaCurso
          break
        case "VIRTUAL":
          endpoint = "/cursos-builder/virtual"
          body.duracion = 40
          break
        case "INTENSIVO":
          endpoint = "/cursos-builder/intensivo"
          body.fechaInicio = new Date().toISOString().split('T')[0]
          break
        case "GRATUITO":
          endpoint = "/cursos-builder/gratuito"
          body.categoria = categoriaCurso
          break
        case "CORPORATIVO":
          endpoint = "/cursos-builder/corporativo"
          body.duracion = 60
          body.cupo = 25
          body.fechaInicio = new Date().toISOString().split('T')[0]
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

  const verDemoBuilder = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/cursos-builder/demo`)
      const data = await response.json()
      setResult(data)
    } catch (err: any) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const verEstadisticasCursos = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`${API_URL}/cursos-builder/estadisticas`)
      const data = await response.json()
      setResult(data)
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
          <Badge variant="default">4 Completados</Badge>
          <Badge variant="secondary">19 Pendientes</Badge>
        </div>
      </div>

      <Tabs defaultValue="singleton" className="space-y-4">
        <TabsList className="grid w-full grid-cols-4">
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
                <Button onClick={verDemoBuilder} variant="outline" disabled={loading}>
                  Ver Demo
                </Button>
                <Button onClick={verEstadisticasCursos} variant="outline" disabled={loading}>
                  Ver Estadísticas
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
                        <option value="BASICO">BÁSICO - 20hrs, Gratis, Presencial</option>
                        <option value="PREMIUM">PREMIUM - 80hrs, $299, Híbrido, Certificado</option>
                        <option value="VIRTUAL">VIRTUAL - 40hrs, $99, 100 cupos</option>
                        <option value="INTENSIVO">INTENSIVO - 40hrs, 2 semanas, Proyecto</option>
                        <option value="GRATUITO">GRATUITO - 10hrs, Virtual, Sin certificado</option>
                        <option value="CORPORATIVO">CORPORATIVO - Personalizado, $499</option>
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
                  {(tipoCurso === "PREMIUM" || tipoCurso === "GRATUITO") && (
                    <div>
                      <Label htmlFor="categoriaCurso">Categoría</Label>
                      <select
                        id="categoriaCurso"
                        className="w-full border rounded-md p-2"
                        value={categoriaCurso}
                        onChange={(e) => setCategoriaCurso(e.target.value)}
                      >
                        <option value="PROGRAMACION">Programación</option>
                        <option value="DISENO">Diseño</option>
                        <option value="MARKETING">Marketing</option>
                        <option value="NEGOCIOS">Negocios</option>
                      </select>
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
                            <Badge variant="outline">{curso.codigo}</Badge>
                          </div>
                          <div className="flex gap-2 text-xs">
                            <Badge variant={
                              curso.tipoConstruccion === "PREMIUM" ? "default" :
                              curso.tipoConstruccion === "GRATUITO" ? "secondary" :
                              "outline"
                            }>
                              {curso.tipoConstruccion}
                            </Badge>
                            <span>{curso.modalidad}</span>
                            <span>{curso.duracionHoras}hrs</span>
                            <span>${curso.precio}</span>
                          </div>
                        </div>
                        <div className="text-xs text-muted-foreground">
                          {curso.incluyeCertificado && "📜"}
                          {curso.incluyeVideoLectures && "🎥"}
                          {curso.incluyeProyectoFinal && "🎯"}
                        </div>
                      </div>
                    ))}
                  </div>
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
