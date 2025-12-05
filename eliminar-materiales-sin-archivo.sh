#!/bin/bash

# Script para eliminar materiales sin archivo asociado

echo "======================================"
echo "Eliminando materiales sin archivo"
echo "======================================"
echo ""

# 1. Ver materiales sin archivo
echo "1. Consultando materiales sin archivo..."
curl -X GET http://localhost:8080/api/materiales/sin-archivo

echo ""
echo ""

# 2. Eliminar materiales sin archivo
echo "2. Eliminando materiales sin archivo..."
curl -X DELETE http://localhost:8080/api/materiales/sin-archivo

echo ""
echo ""
echo "======================================"
echo "Operación completada"
echo "======================================"
