'use client'

import { useState } from 'react'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { FileText, Settings } from 'lucide-react'
import TemplateMethodInscripcion from './template-method-inscripcion'
import TemplateMethodManager from './template-method-manager'

/**
 * Página principal para el módulo de inscripciones
 * Combina ambos componentes del patrón Template Method
 */
export default function InscripcionesPage() {
  const [activeTab, setActiveTab] = useState('inscripcion')

  return (
    <div className="min-h-screen bg-background">
      <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
        <div className="border-b">
          <div className="container mx-auto">
            <TabsList className="h-14">
              <TabsTrigger value="inscripcion" className="gap-2">
                <FileText className="h-4 w-4" />
                Proceso de Inscripción
              </TabsTrigger>
              <TabsTrigger value="manager" className="gap-2">
                <Settings className="h-4 w-4" />
                Gestión del Patrón
              </TabsTrigger>
            </TabsList>
          </div>
        </div>

        <TabsContent value="inscripcion" className="m-0">
          <TemplateMethodInscripcion />
        </TabsContent>

        <TabsContent value="manager" className="m-0">
          <TemplateMethodManager />
        </TabsContent>
      </Tabs>
    </div>
  )
}
