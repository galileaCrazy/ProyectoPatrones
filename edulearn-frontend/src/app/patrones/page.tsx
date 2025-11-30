"use client"

import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { CheckCircle2, XCircle, Loader2, Send, Settings, Bell } from "lucide-react"

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

  return (
    <div className="container mx-auto p-6">
      <div className="mb-8">
        <h1 className="text-4xl font-bold mb-2">🎨 Patrones de Diseño</h1>
        <p className="text-muted-foreground">
          Demostración interactiva de los 23 patrones de diseño implementados en EduLearn
        </p>
        <div className="flex gap-2 mt-4">
          <Badge variant="default">2 Completados</Badge>
          <Badge variant="secondary">21 Pendientes</Badge>
        </div>
      </div>

      <Tabs defaultValue="singleton" className="space-y-4">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="singleton">
            <Settings className="mr-2 h-4 w-4" />
            Singleton
          </TabsTrigger>
          <TabsTrigger value="factory">
            <Bell className="mr-2 h-4 w-4" />
            Factory Method
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
