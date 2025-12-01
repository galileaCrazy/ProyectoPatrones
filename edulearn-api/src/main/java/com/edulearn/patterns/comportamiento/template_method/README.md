# Patrón Template Method - Proceso de Inscripción

## 📋 Descripción

El patrón **Template Method** define el esqueleto de un algoritmo en un método, delegando algunos pasos a las subclases. Permite que las subclases redefinan ciertos pasos de un algoritmo sin cambiar su estructura.

## 🎯 Caso de Uso: Proceso de Inscripción

En EduLearn, el proceso de inscripción sigue un flujo general común, pero cada tipo de inscripción (gratuita, paga, beca) tiene pasos específicos que varían.

### Flujo General (Template Method)

```
1. Validar requisitos previos     ─── Común a todos
2. Verificar disponibilidad       ─── Común a todos
3. Validar documentación          ─── Específico por tipo
4. Procesar aspecto económico     ─── Específico por tipo
5. Aplicar beneficios (hook)      ─── Opcional por tipo
6. Registrar inscripción          ─── Común a todos
7. Enviar notificaciones          ─── Común a todos
8. Generar documentos             ─── Específico por tipo
```

## 📁 Estructura de Archivos

```
template_method/
├── ProcesoInscripcionTemplate.java    # Clase abstracta con el template method
├── InscripcionGratuita.java           # Implementación para cursos gratuitos
├── InscripcionPaga.java               # Implementación para cursos de pago
├── InscripcionBeca.java               # Implementación para becados
├── InscripcionTemplateService.java    # Servicio orquestador
├── InscripcionTemplateController.java # REST Controller
├── dto/
│   ├── SolicitudInscripcion.java      # DTO de entrada
│   ├── ResultadoInscripcion.java      # DTO de resultado general
│   └── ResultadoPaso.java             # DTO de resultado por paso
└── README.md
```

## 🔌 Endpoints REST

### Base URL: `/api/inscripciones/proceso`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Procesar inscripción |
| GET | `/tipos` | Listar tipos de inscripción |
| GET | `/pasos/{tipo}` | Obtener pasos por tipo |
| GET | `/demo` | Demo del patrón |
| GET | `/cursos-disponibles` | Listar cursos |
| GET | `/verificar/{estudianteId}/{cursoId}` | Verificar elegibilidad |

## 📝 Ejemplos de Uso

### Inscripción Gratuita

```bash
curl -X POST http://localhost:8080/api/inscripciones/proceso \
  -H "Content-Type: application/json" \
  -d '{
    "estudianteId": 4,
    "cursoId": 4,
    "tipoInscripcion": "GRATUITA",
    "aceptaTerminos": true
  }'
```

### Inscripción Paga

```bash
curl -X POST http://localhost:8080/api/inscripciones/proceso \
  -H "Content-Type: application/json" \
  -d '{
    "estudianteId": 4,
    "cursoId": 4,
    "tipoInscripcion": "PAGA",
    "aceptaTerminos": true,
    "metodoPago": "TARJETA",
    "numeroTarjeta": "4111111111111111",
    "monto": 500.00,
    "codigoDescuento": "PROMO10"
  }'
```

### Inscripción con Beca

```bash
curl -X POST http://localhost:8080/api/inscripciones/proceso \
  -H "Content-Type: application/json" \
  -d '{
    "estudianteId": 4,
    "cursoId": 4,
    "tipoInscripcion": "BECA",
    "aceptaTerminos": true,
    "tipoBeca": "ACADEMICA",
    "codigoBeca": "BECA-2024-001",
    "porcentajeBeca": 100
  }'
```

### Ver Demo del Patrón

```bash
curl http://localhost:8080/api/inscripciones/proceso/demo
```

### Verificar Elegibilidad

```bash
curl http://localhost:8080/api/inscripciones/proceso/verificar/4/4
```

## 🔄 Respuesta de Ejemplo

```json
{
  "estudianteId": 4,
  "cursoId": 4,
  "tipoInscripcion": "GRATUITA",
  "exitoso": true,
  "estado": "COMPLETADA",
  "mensaje": "Inscripción procesada exitosamente",
  "pasos": [
    {
      "nombre": "Validación de requisitos previos",
      "exitoso": true,
      "mensaje": "Requisitos previos validados correctamente"
    },
    {
      "nombre": "Verificación de disponibilidad",
      "exitoso": true,
      "mensaje": "Cupo disponible en el curso"
    },
    ...
  ],
  "numeroInscripcion": "INS-1",
  "fechaInicio": "2024-01-15T10:30:00",
  "fechaFin": "2024-01-15T10:30:01",
  "duracionTotalMs": 150
}
```

## 🏗️ Diagrama UML

```
┌────────────────────────────────────────┐
│  ProcesoInscripcionTemplate (Abstract) │
├────────────────────────────────────────┤
│ + procesarInscripcion() : final        │ ◄── Template Method
│ # validarRequisitosPrevios()           │ ◄── Método común
│ # verificarDisponibilidad()            │ ◄── Método común
│ # validarDocumentacion() : abstract    │ ◄── Debe implementarse
│ # procesarAspectoEconomico() : abstract│ ◄── Debe implementarse
│ # aplicarBeneficios() : hook           │ ◄── Opcional
│ # registrarInscripcion()               │ ◄── Método común
│ # enviarNotificaciones()               │ ◄── Método común
│ # generarDocumentos() : abstract       │ ◄── Debe implementarse
└────────────────────────────────────────┘
                    △
                    │
       ┌────────────┼────────────┐
       │            │            │
       ▼            ▼            ▼
┌──────────┐  ┌──────────┐  ┌──────────┐
│Gratuita  │  │  Paga    │  │  Beca    │
├──────────┤  ├──────────┤  ├──────────┤
│validarDoc│  │validarDoc│  │validarDoc│
│procEcon  │  │procEcon  │  │procEcon  │
│genDocs   │  │aplicBenef│  │aplicBenef│
└──────────┘  │genDocs   │  │genDocs   │
              └──────────┘  └──────────┘
```

## ✅ Ventajas del Patrón

1. **Reutilización de código**: La lógica común está en la clase base
2. **Extensibilidad**: Fácil agregar nuevos tipos de inscripción
3. **Inversión de control**: "Hollywood Principle" - la clase base llama a las subclases
4. **Consistencia**: Todos los tipos siguen el mismo flujo general
5. **Hooks**: Puntos de extensión opcionales para personalización

## 🎨 Tipos de Beca Soportados

| Tipo | Cobertura | Requisitos |
|------|-----------|------------|
| ACADEMICA | 100% | Promedio ≥ 8.0 |
| DEPORTIVA | 75% | Actividad deportiva |
| SOCIECONOMICA | 80% | Estudio socioeconómico |
| CULTURAL | 50% | Actividad cultural |

## 🏷️ Códigos de Descuento (Inscripción Paga)

| Código | Descuento |
|--------|-----------|
| PROMO10 | 10% |
| PROMO20 | 20% |
| DESC50 | $50.00 |
