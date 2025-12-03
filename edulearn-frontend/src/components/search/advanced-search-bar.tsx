'use client'

import { useState } from 'react'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { SearchEngine } from '@/patterns/interpreter/search-interpreter'

interface AdvancedSearchBarProps<T> {
  data: T[]
  onSearchResults: (results: T[]) => void
  placeholder?: string
  suggestions?: string[]
  showHelp?: boolean
}

export default function AdvancedSearchBar<T>({
  data,
  onSearchResults,
  placeholder = 'Buscar... (ej: nombre:Juan AND estado:activo)',
  suggestions = [],
  showHelp = true
}: AdvancedSearchBarProps<T>) {
  const [query, setQuery] = useState('')
  const [showSuggestions, setShowSuggestions] = useState(false)
  const [showHelpPanel, setShowHelpPanel] = useState(false)
  const searchEngine = new SearchEngine()

  const handleSearch = (searchQuery: string) => {
    setQuery(searchQuery)

    if (!searchQuery.trim()) {
      onSearchResults(data)
      return
    }

    try {
      const results = searchEngine.search(data, searchQuery)
      onSearchResults(results)
    } catch (error) {
      console.error('Error en búsqueda:', error)
      onSearchResults([])
    }
  }

  const insertSuggestion = (field: string) => {
    const newQuery = query ? `${query} AND ${field}:` : `${field}:`
    setQuery(newQuery)
    setShowSuggestions(false)

    const inputElement = document.querySelector('input[type="text"]') as HTMLInputElement
    if (inputElement) {
      inputElement.focus()
    }
  }

  const clearSearch = () => {
    setQuery('')
    onSearchResults(data)
  }

  return (
    <div className="space-y-3">
      <Card className="border-border/50">
        <CardContent className="p-4">
          <div className="flex items-center gap-3">
            <div className="flex-1 relative">
              <input
                type="text"
                value={query}
                onChange={(e) => handleSearch(e.target.value)}
                onFocus={() => setShowSuggestions(true)}
                onBlur={() => setTimeout(() => setShowSuggestions(false), 200)}
                placeholder={placeholder}
                className="w-full px-4 py-2.5 rounded-lg border border-input bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-primary transition-all"
              />

              {showSuggestions && suggestions.length > 0 && (
                <div className="absolute top-full left-0 right-0 mt-2 bg-background border border-border rounded-lg shadow-lg z-50 max-h-60 overflow-y-auto">
                  <div className="p-2">
                    <p className="text-xs text-muted-foreground px-2 py-1 font-medium">
                      Campos disponibles:
                    </p>
                    {suggestions.map((field) => (
                      <button
                        key={field}
                        onClick={() => insertSuggestion(field)}
                        className="w-full text-left px-3 py-2 text-sm hover:bg-muted rounded transition-colors"
                      >
                        <span className="font-mono text-primary">{field}</span>
                      </button>
                    ))}
                  </div>
                </div>
              )}
            </div>

            {query && (
              <Button
                onClick={clearSearch}
                variant="outline"
                className="text-sm"
              >
                Limpiar
              </Button>
            )}

            {showHelp && (
              <Button
                onClick={() => setShowHelpPanel(!showHelpPanel)}
                variant="outline"
                className="text-sm"
              >
                {showHelpPanel ? 'Ocultar ayuda' : 'Ayuda'}
              </Button>
            )}
          </div>
        </CardContent>
      </Card>

      {showHelpPanel && (
        <Card className="border-border/50 bg-muted/30">
          <CardContent className="p-4">
            <h3 className="font-semibold text-foreground mb-3 text-sm">
              Sintaxis de búsqueda avanzada
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
              <div>
                <p className="text-muted-foreground mb-2 font-medium">Operadores de campo:</p>
                <ul className="space-y-1 text-muted-foreground">
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo:valor</code> - Contiene texto</li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo=valor</code> - Igual a</li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo!=valor</code> - Diferente de</li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo&gt;valor</code> - Mayor que</li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo&gt;=valor</code> - Mayor o igual</li>
                </ul>
              </div>
              <div>
                <p className="text-muted-foreground mb-2 font-medium">Operadores lógicos:</p>
                <ul className="space-y-1 text-muted-foreground">
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">expr1 AND expr2</code> - Ambas condiciones</li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">expr1 OR expr2</code> - Al menos una</li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">NOT expr</code> - Niega la condición</li>
                </ul>
                <p className="text-muted-foreground mt-3 font-medium">Ejemplos:</p>
                <ul className="space-y-1 text-muted-foreground">
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">nombre:Juan</code></li>
                  <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">puntaje&gt;=50 AND estado:activo</code></li>
                </ul>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
