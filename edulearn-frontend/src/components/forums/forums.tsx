'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

export default function ForumsView() {
  const [selectedThread, setSelectedThread] = useState<string | null>(null)

  const threads = [
    {
      id: '1',
      title: '¿Dudas sobre el Examen Parcial?',
      author: 'Juan Pérez',
      replies: 15,
      lastActivity: 'Ayer',
      posts: [
        { author: 'Juan Pérez', content: '¿Alguien tiene dudas sobre los temas del examen?', likes: 5 },
        { author: 'Ana Torres', content: 'Yo tengo dudas sobre la herencia múltiple', likes: 3 },
      ]
    },
    {
      id: '2',
      title: 'Explicación de Herencia',
      author: 'María González',
      replies: 8,
      lastActivity: 'Hace 2h',
      posts: [
        { author: 'María González', content: 'Aquí les dejo una explicación detallada...', likes: 12 },
      ]
    },
    {
      id: '3',
      title: '¿Alguien tiene ejemplos de polimorfismo?',
      author: 'Carlos Ruiz',
      replies: 3,
      lastActivity: 'Hace 5h',
      posts: []
    },
  ]

  const selected = selectedThread ? threads.find(t => t.id === selectedThread) : null

  return (
    <div className="p-8 max-w-7xl mx-auto">
      <h1 className="text-3xl font-bold text-foreground mb-8">Foros de Discusión</h1>

      {!selectedThread ? (
        <div className="space-y-4">
          <Button className="bg-primary hover:bg-primary/90 text-primary-foreground mb-4">
            + Nuevo Tema
          </Button>
          {threads.map((thread) => (
            <Card
              key={thread.id}
              className="cursor-pointer hover:shadow-lg transition-shadow border-border/50"
              onClick={() => setSelectedThread(thread.id)}
            >
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div>
                    <CardTitle>{thread.title}</CardTitle>
                    <CardDescription>{thread.author}</CardDescription>
                  </div>
                  <span className="text-sm text-muted-foreground">{thread.replies} respuestas</span>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">Última actividad: {thread.lastActivity}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      ) : selected && (
        <div>
          <Button
            onClick={() => setSelectedThread(null)}
            className="mb-6 bg-muted hover:bg-muted/80 text-foreground"
          >
            ← Volver a Foros
          </Button>

          <Card className="mb-6 border-border/50">
            <CardHeader>
              <CardTitle className="text-2xl">{selected.title}</CardTitle>
              <CardDescription>{selected.author}</CardDescription>
            </CardHeader>
          </Card>

          <div className="space-y-4 mb-6">
            {selected.posts.map((post, idx) => (
              <Card key={idx} className="border-border/50">
                <CardHeader>
                  <CardTitle className="text-base">{post.author}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-foreground mb-4">{post.content}</p>
                  <button className="text-sm text-primary hover:text-primary/80">
                    👍 {post.likes}
                  </button>
                </CardContent>
              </Card>
            ))}
          </div>

          <Card className="border-border/50">
            <CardHeader>
              <CardTitle>Agregar Respuesta</CardTitle>
            </CardHeader>
            <CardContent>
              <textarea
                placeholder="Escribe tu respuesta..."
                className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary h-32 resize-none mb-4"
              />
              <Button className="bg-primary hover:bg-primary/90 text-primary-foreground">
                Enviar Respuesta
              </Button>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}
