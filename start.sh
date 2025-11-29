#!/bin/bash

echo "🚀 Iniciando PlataformaCursos..."
echo ""

# Verificar que MySQL esté corriendo
if ! pgrep -x "mysqld" > /dev/null
then
    echo "⚠️  MySQL no está corriendo. Por favor, inicia MySQL primero:"
    echo "   sudo systemctl start mysql"
    exit 1
fi

echo "✅ MySQL está corriendo"
echo ""

# Iniciar el backend en segundo plano
echo "🔧 Iniciando Backend (Spring Boot)..."
cd edulearn-api
mvn spring-boot:run > ../backend.log 2>&1 &
BACKEND_PID=$!
echo "   Backend iniciado (PID: $BACKEND_PID)"
echo "   Logs: backend.log"
cd ..

# Esperar a que el backend inicie
echo ""
echo "⏳ Esperando a que el backend inicie (puede tomar 10-15 segundos)..."
sleep 15

# Iniciar el frontend en segundo plano
echo ""
echo "🎨 Iniciando Frontend (Next.js)..."
cd edulearn-frontend
npm run dev > ../frontend.log 2>&1 &
FRONTEND_PID=$!
echo "   Frontend iniciado (PID: $FRONTEND_PID)"
echo "   Logs: frontend.log"
cd ..

echo ""
echo "✅ ¡Aplicación iniciada!"
echo ""
echo "📍 URLs:"
echo "   Frontend: http://localhost:3000"
echo "   Backend:  http://localhost:8080"
echo ""
echo "🔐 Credenciales de prueba:"
echo "   Estudiante: juan@mail.com / password123"
echo "   Profesor:   galilea@mail.com / password123"
echo "   Admin:      admin@mail.com / admin123"
echo ""
echo "📝 Para ver los logs en tiempo real:"
echo "   Backend:  tail -f backend.log"
echo "   Frontend: tail -f frontend.log"
echo ""
echo "🛑 Para detener los servicios:"
echo "   kill $BACKEND_PID $FRONTEND_PID"
echo ""

# Guardar PIDs en archivo para fácil detención
echo "$BACKEND_PID" > .pids
echo "$FRONTEND_PID" >> .pids

echo "💡 Tip: Los PIDs fueron guardados en .pids"
echo "    Puedes detener todo con: cat .pids | xargs kill"
