# Patrón Strategy - Estrategias de Evaluación

## Descripción

Implementación del patrón de diseño **Strategy** para manejar diferentes métodos de evaluación en el sistema de gestión de cursos EduLearn.

## Estructura del Patrón

```
strategy/
├── EstrategiaEvaluacion.java        (Interfaz Strategy)
├── EvaluacionPonderada.java         (Estrategia Concreta 1)
├── PromedioSimple.java               (Estrategia Concreta 2)
├── EvaluacionPorCompetencias.java   (Estrategia Concreta 3)
└── ContextoEvaluacion.java          (Contexto)
```

## Componentes

### 1. Interfaz Strategy: `EstrategiaEvaluacion`

Define el contrato que todas las estrategias de evaluación deben cumplir.

**Métodos principales:**
- `calcularCalificacionFinal(Map<String, Double> calificaciones): double`
- `getNombreEstrategia(): String`
- `getDescripcion(): String`
- `validarCalificaciones(Map<String, Double> calificaciones): boolean`

### 2. Estrategias Concretas

#### a) Evaluación Ponderada (`EvaluacionPonderada`)

Calcula la calificación aplicando pesos específicos a cada componente:

- **Tareas:** 30%
- **Exámenes:** 50%
- **Proyecto Final:** 20%

**Fórmula:**
```
Calificación Final = (Tareas × 0.30) + (Exámenes × 0.50) + (Proyecto × 0.20)
```

**Caso de uso:** Cursos donde se quiere dar más peso a los exámenes.

#### b) Promedio Simple (`PromedioSimple`)

Calcula el promedio aritmético simple de todas las evaluaciones sin aplicar pesos.

**Fórmula:**
```
Calificación Final = (Tareas + Exámenes + Proyecto) / 3
```

**Caso de uso:** Cursos donde todas las evaluaciones tienen la misma importancia.

#### c) Evaluación por Competencias (`EvaluacionPorCompetencias`)

Evalúa el dominio de competencias específicas con reglas especiales:

**Reglas:**
- Si el estudiante aprueba TODAS las competencias (≥ 70): Calificación final es el promedio
- Si falla alguna competencia (< 70): Penalización de -10 puntos
- Si todas las competencias son ≥ 90: Bonus de +5 puntos

**Caso de uso:** Cursos basados en desarrollo de habilidades específicas.

### 3. Contexto: `ContextoEvaluacion`

Mantiene una referencia a la estrategia actual y delega el cálculo de calificaciones.

**Características:**
- Permite cambiar la estrategia en tiempo de ejecución
- Valida las calificaciones antes de calcular
- Proporciona información sobre la estrategia actual

## Servicios y Controladores

### `EstrategiaEvaluacionService`

Servicio Spring que actúa como fábrica y gestor de estrategias.

**Métodos principales:**
- `obtenerEstrategia(String nombreEstrategia): EstrategiaEvaluacion`
- `obtenerEstrategiasDisponibles(): Map<String, String>`
- `calcularConEstrategia(String nombreEstrategia, Map<String, Double> calificaciones): double`
- `esEstrategiaValida(String nombreEstrategia): boolean`

### `EstrategiaEvaluacionController`

Controlador REST para gestionar las estrategias de evaluación.

**Endpoints:**

1. **GET** `/api/estrategias-evaluacion/disponibles`
   - Retorna la lista de estrategias disponibles

2. **GET** `/api/estrategias-evaluacion/validar/{nombreEstrategia}`
   - Valida si una estrategia es válida

3. **POST** `/api/estrategias-evaluacion/calcular`
   - Calcula una calificación usando una estrategia específica
   - Body: `{ "estrategia": "PONDERADA", "calificaciones": { "tareas": 85, "examenes": 90, "proyecto": 88 } }`

## Integración con Modelo Curso

El modelo `Curso` incluye el campo:
```java
@Column(name = "estrategia_evaluacion")
private String estrategiaEvaluacion; // PONDERADA, SIMPLE, COMPETENCIAS
```

## Uso en Frontend

El formulario de creación de cursos incluye un selector de estrategia de evaluación:

```typescript
<select value={courseData.evaluationStrategy}>
  <option value="PONDERADA">Evaluación Ponderada</option>
  <option value="SIMPLE">Promedio Simple</option>
  <option value="COMPETENCIAS">Evaluación por Competencias</option>
</select>
```

## Ejemplo de Uso

### Desde Java

```java
// Opción 1: Usando el servicio
@Autowired
private EstrategiaEvaluacionService estrategiaService;

Map<String, Double> calificaciones = Map.of(
    "tareas", 85.0,
    "examenes", 90.0,
    "proyecto", 88.0
);

double resultado = estrategiaService.calcularConEstrategia("PONDERADA", calificaciones);

// Opción 2: Usando el contexto directamente
EstrategiaEvaluacion estrategia = new EvaluacionPonderada();
ContextoEvaluacion contexto = new ContextoEvaluacion(estrategia);
double calificacionFinal = contexto.ejecutarEvaluacion(calificaciones);
```

### Desde API REST

```bash
# Obtener estrategias disponibles
curl http://localhost:8080/api/estrategias-evaluacion/disponibles

# Calcular calificación
curl -X POST http://localhost:8080/api/estrategias-evaluacion/calcular \
  -H "Content-Type: application/json" \
  -d '{
    "estrategia": "PONDERADA",
    "calificaciones": {
      "tareas": 85,
      "examenes": 90,
      "proyecto": 88
    }
  }'
```

## Ventajas del Patrón Strategy

1. **Flexibilidad:** Permite cambiar el algoritmo de cálculo en tiempo de ejecución
2. **Extensibilidad:** Fácil agregar nuevas estrategias sin modificar código existente
3. **Encapsulación:** Cada estrategia encapsula su propia lógica
4. **Principio Open/Closed:** Abierto a extensión, cerrado a modificación
5. **Mantenibilidad:** Código más organizado y fácil de mantener

## Extensión Futura

Para agregar una nueva estrategia:

1. Crear una clase que implemente `EstrategiaEvaluacion`
2. Agregar la anotación `@Component`
3. Implementar los métodos requeridos
4. Registrar en `EstrategiaEvaluacionService`
5. Agregar opción en el frontend

## Diagrama de Clases

```
┌─────────────────────────────┐
│  <<interface>>              │
│  EstrategiaEvaluacion       │
├─────────────────────────────┤
│ + calcularCalificacionFinal()│
│ + getNombreEstrategia()     │
│ + getDescripcion()          │
│ + validarCalificaciones()   │
└──────────┬──────────────────┘
           │
           ├──────────────────────────────┐
           │                              │
┌──────────▼──────────┐  ┌────────────────▼───────────┐
│ EvaluacionPonderada │  │     PromedioSimple         │
├─────────────────────┤  ├────────────────────────────┤
│ - PESO_TAREAS       │  │ + calcularCalificacionFinal()│
│ - PESO_EXAMENES     │  │ + validarCalificaciones()   │
│ - PESO_PROYECTO     │  └─────────────────────────────┘
└─────────────────────┘
           │
┌──────────▼─────────────────────┐
│ EvaluacionPorCompetencias      │
├────────────────────────────────┤
│ - UMBRAL_APROBACION            │
│ - UMBRAL_EXCELENCIA            │
│ - PENALIZACION                 │
│ - BONUS_EXCELENCIA             │
└────────────────────────────────┘

┌─────────────────────────┐
│  ContextoEvaluacion     │
├─────────────────────────┤
│ - estrategia            │
├─────────────────────────┤
│ + setEstrategia()       │
│ + ejecutarEvaluacion()  │
└─────────────────────────┘
```

## Autor

Implementado para el proyecto EduLearn - Sistema de Gestión de Aprendizaje

## Fecha

Noviembre 2025
