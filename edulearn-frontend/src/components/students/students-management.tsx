'use client'

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

export default function StudentsManagementView() {
  const [searchQuery, setSearchQuery] = useState('')

  const students = [
    { id: '001', name: 'Ana Torres', email: 'ana.torres@uni.edu', courses: 4, average: 85 },
    { id: '002', name: 'Juan Méndez', email: 'juan.mendez@uni.edu', courses: 5, average: 92 },
    { id: '003', name: 'María López', email: 'maria.lopez@uni.edu', courses: 3, average: 78 },
    { id: '004', name: 'Carlos Ruiz', email: 'carlos.ruiz@uni.edu', courses: 4, average: 88 },
    { id: '005', name: 'Laura Martínez', email: 'laura.martinez@uni.edu', courses: 5, average: 95 },
  ]

  const filteredStudents = students.filter(s =>
    s.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    s.email.toLowerCase().includes(searchQuery.toLowerCase())
  )

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

      {/* Search */}
      <div className="mb-6">
        <input
          type="text"
          placeholder="Buscar por nombre o email..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full px-4 py-2 rounded-lg border border-input bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
        />
      </div>

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
