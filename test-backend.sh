#!/bin/bash

echo "🧪 Probando Backend de Creación de Cursos"
echo "========================================="
echo ""

# Prueba 1: Verificar que el backend esté corriendo
echo "1️⃣ Verificando que el backend esté corriendo..."
curl -s http://localhost:8080/actuator/health > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "   ✅ Backend está corriendo"
else
    echo "   ❌ Backend NO está corriendo en puerto 8080"
    echo "   Por favor inicia el backend con: cd edulearn-api && mvn spring-boot:run"
    exit 1
fi

echo ""

# Prueba 2: Crear curso de prueba simple
echo "2️⃣ Intentando crear curso de prueba..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST http://localhost:8080/api/cursos/crear-completo \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Curso de Prueba Backend",
    "descripcion": "Curso de prueba para verificar funcionamiento",
    "tipoCurso": "virtual",
    "profesorId": 1,
    "periodoAcademico": "2025-1",
    "duracion": 40,
    "estrategiaEvaluacion": "ponderada",
    "cupoMaximo": 30,
    "modulos": [
      {
        "titulo": "Módulo de Prueba",
        "descripcion": "Módulo de prueba",
        "materiales": [
          {
            "nombre": "Material de Prueba",
            "descripcion": "Descripción de prueba"
          }
        ],
        "evaluaciones": [
          {
            "nombre": "Evaluación de Prueba",
            "descripcion": "Descripción de evaluación"
          }
        ]
      }
    ]
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

echo "   Status HTTP: $HTTP_CODE"
echo "   Respuesta:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

if [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "200" ]; then
    echo "   ✅ Curso creado exitosamente"
else
    echo "   ❌ Error al crear curso"
fi

echo ""
echo "========================================="
echo "Prueba completada"
