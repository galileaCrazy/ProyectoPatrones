'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { SearchEngine } from '@/patterns/interpreter/search-interpreter'

export default function StudentsManagementView() {
  const [searchQuery, setSearchQuery] = useState('')
  const [showAdvancedHelp, setShowAdvancedHelp] = useState(false)
  const searchEngine = new SearchEngine()

  const students = [
    { id: '001', name: 'Ana Torres', email: 'ana.torres@uni.edu', courses: 4, average: 85 },
    { id: '002', name: 'Juan Méndez', email: 'juan.mendez@uni.edu', courses: 5, average: 92 },
    { id: '003', name: 'María López', email: 'maria.lopez@uni.edu', courses: 3, average: 78 },
    { id: '004', name: 'Carlos Ruiz', email: 'carlos.ruiz@uni.edu', courses: 4, average: 88 },
    { id: '005', name: 'Laura Martínez', email: 'laura.martinez@uni.edu', courses: 5, average: 95 },
  ]

  const [filteredStudents, setFilteredStudents] = useState(students)

  const handleSearch = (query: string) => {
    setSearchQuery(query)

    if (!query.trim()) {
      setFilteredStudents(students)
      return
    }

    try {
      const results = searchEngine.search(students, query)
      setFilteredStudents(results)
    } catch (error) {
      console.error('Error en búsqueda:', error)
      setFilteredStudents([])
    }
  }

  return (
    <div className="p-8 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-start mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">Gestión de Estudiantes</h1>
          <p className="text-muted-foreground">Total: {students.length} estudiantes</p>
        </div>
        <Button className="bg-primary hover:bg-primary/90 text-primary-foreground">
          + Agregar Estudiante
        </Button>
      </div>

      {/* Búsqueda Avanzada */}
      <Card className="mb-6 border-border/50">
        <CardContent className="p-4">
          <div className="space-y-4">
            <div className="flex gap-3 items-start">
              <div className="flex-1">
                <input
                  type="text"
                  placeholder="Buscar estudiantes... (ej: name:Ana AND average>=90)"
                  value={searchQuery}
                  onChange={(e) => handleSearch(e.target.value)}
                  className="w-full px-4 py-2.5 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary text-sm"
                />
              </div>
              {searchQuery && (
                <Button
                  onClick={() => handleSearch('')}
                  variant="outline"
                  className="text-sm"
                >
                  Limpiar
                </Button>
              )}
              <Button
                onClick={() => setShowAdvancedHelp(!showAdvancedHelp)}
                variant="outline"
                className="text-sm"
              >
                {showAdvancedHelp ? 'Ocultar ayuda' : 'Ayuda'}
              </Button>
            </div>

            {showAdvancedHelp && (
              <div className="bg-muted/30 rounded-lg p-4">
                <h3 className="font-semibold text-foreground mb-3 text-sm">
                  Sintaxis de búsqueda avanzada
                </h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
                  <div>
                    <p className="text-muted-foreground mb-2 font-medium">Campos disponibles:</p>
                    <ul className="space-y-1 text-muted-foreground">
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">id</code> - ID del estudiante</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">name</code> - Nombre del estudiante</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">email</code> - Correo electrónico</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">courses</code> - Número de cursos</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">average</code> - Promedio</li>
                    </ul>
                  </div>
                  <div>
                    <p className="text-muted-foreground mb-2 font-medium">Operadores:</p>
                    <ul className="space-y-1 text-muted-foreground">
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo:valor</code> - Contiene texto</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo&gt;=valor</code> - Mayor o igual</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">campo&lt;valor</code> - Menor que</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">expr AND expr</code> - Ambas condiciones</li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">expr OR expr</code> - Al menos una</li>
                    </ul>
                    <p className="text-muted-foreground mt-3 font-medium">Ejemplos:</p>
                    <ul className="space-y-1 text-muted-foreground">
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">name:Ana</code></li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">average&gt;=90</code></li>
                      <li><code className="bg-background px-1.5 py-0.5 rounded text-xs">courses&gt;=4 AND average&gt;85</code></li>
                    </ul>
                  </div>
                </div>
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Table */}
      <Card className="border-border/50">
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-muted/50 border-b border-border">
                <tr>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-foreground">ID</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-foreground">Nombre</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-foreground">Email</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-foreground">Cursos</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-foreground">Promedio</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-foreground">Acciones</th>
                </tr>
              </thead>
              <tbody>
                {filteredStudents.map((student, idx) => (
                  <tr key={student.id} className={`border-b border-border ${idx % 2 ? 'bg-muted/20' : ''}`}>
                    <td className="px-6 py-4 text-sm text-foreground">{student.id}</td>
                    <td className="px-6 py-4 text-sm font-medium text-foreground">{student.name}</td>
                    <td className="px-6 py-4 text-sm text-muted-foreground">{student.email}</td>
                    <td className="px-6 py-4 text-sm text-foreground">{student.courses}</td>
                    <td className="px-6 py-4 text-sm">
                      <span className="bg-primary/10 text-primary px-3 py-1 rounded-full font-semibold">
                        {student.average}%
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm">
                      <button className="text-primary hover:text-primary/80 font-medium mr-3">Ver</button>
                      <button className="text-accent hover:text-accent/80 font-medium">Editar</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
