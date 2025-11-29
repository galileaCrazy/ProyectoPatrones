# EduLearn - Plataforma de Cursos

Plataforma educativa desarrollada con Spring Boot (backend) y Next.js (frontend), implementando 10 patrones de diseño.

## Tecnologías

### Backend (edulearn-api)
- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- Maven

### Frontend (edulearn-frontend)
- Next.js 16.0.3
- React 19.2.0
- TypeScript 5
- Tailwind CSS 4

## Requisitos Previos

- Java 21 o superior
- Maven 3.6+
- Node.js 20+ y npm
- MySQL 8.0+

## Configuración Inicial

### 1. Base de Datos

Importa el script de base de datos:

```bash
mysql -u root -p < database/setup_edulearn.sql
```

### 2. Backend (Spring Boot)

Crea un archivo `.env` en `edulearn-api/` basado en `.env.example`:

```bash
cd edulearn-api
cp .env.example .env
```

Edita el archivo `.env` con tus credenciales de MySQL:

```properties
DB_URL=jdbc:mysql://localhost:3306/edulearn
DB_USERNAME=root
DB_PASSWORD=tu_contraseña_aqui
SERVER_PORT=8080
SERVER_ADDRESS=0.0.0.0
```

### 3. Frontend (Next.js)

Instala las dependencias:

```bash
cd edulearn-frontend
npm install
```

## Iniciar la Aplicación

### Windows (PowerShell)

```powershell
.\start.ps1
```

### Linux/Mac

```bash
chmod +x start.sh
./start.sh
```

Esto iniciará automáticamente:
- Backend en http://localhost:8080
- Frontend en http://localhost:3000

### Detener la Aplicación

**Windows:**
```powershell
.\stop.ps1
```

**Linux/Mac:**
```bash
cat .pids | xargs kill
```

## Inicio Manual

### Backend
```bash
cd edulearn-api
mvn spring-boot:run
```

### Frontend
```bash
cd edulearn-frontend
npm run dev
```

## Credenciales de Prueba

- **Estudiante:** juan@mail.com / password123
- **Profesor:** galilea@mail.com / password123
- **Admin:** admin@mail.com / admin123

## Patrones de Diseño Implementados

1. **Abstract Factory** - Creación de tipos de cursos
2. **Builder** - Construcción de objetos Curso
3. **Prototype** - Clonación de cursos
4. **Factory Method** - Creación de componentes
5. **Adapter** - Adaptación de interfaces externas
6. **Bridge** - Separación de abstracción e implementación
7. **Composite** - Estructura jerárquica de contenidos
8. **Decorator** - Extensión de funcionalidades
9. **Facade** - Simplificación de operaciones complejas
10. **Flyweight** - Optimización de recursos compartidos

## Estructura del Proyecto

```
PlataformaCursos/
├── edulearn-api/           # Backend Spring Boot
│   ├── src/
│   │   └── main/
│   │       ├── java/com/edulearn/
│   │       │   ├── controller/
│   │       │   ├── model/
│   │       │   ├── repository/
│   │       │   ├── patterns/
│   │       │   └── config/
│   │       └── resources/
│   │           └── application.properties
│   ├── pom.xml
│   ├── .env.example
│   └── .env (gitignored)
│
├── edulearn-frontend/      # Frontend Next.js
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── tsconfig.json
│
├── database/
│   └── setup_edulearn.sql
│
├── start.ps1              # Script de inicio Windows
├── start.sh               # Script de inicio Linux/Mac
├── stop.ps1               # Script de detención Windows
└── README.md
```

## API Endpoints

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar usuario

### Cursos
- `GET /api/cursos` - Listar cursos
- `GET /api/cursos/{id}` - Obtener curso
- `POST /api/cursos` - Crear curso (Admin/Profesor)
- `PUT /api/cursos/{id}` - Actualizar curso (Admin/Profesor)
- `DELETE /api/cursos/{id}` - Eliminar curso (Admin)

### Inscripciones
- `GET /api/inscripciones` - Listar inscripciones
- `POST /api/inscripciones` - Inscribirse a curso
- `DELETE /api/inscripciones/{id}` - Cancelar inscripción

### Patrones
- `GET /api/patrones/demo` - Demostración de patrones

## Notas de Desarrollo

- Las credenciales están en `.env` (no versionado)
- El archivo `.env.example` muestra la estructura necesaria
- Los logs se guardan en `backend.log` y `frontend.log` (Linux/Mac)
- Los PIDs de procesos se guardan en `.pids` para fácil detención

## Seguridad

⚠️ **IMPORTANTE:**
- Nunca subas el archivo `.env` al repositorio
- Cambia las credenciales por defecto en producción
- Implementa HTTPS en producción
- Configura CORS apropiadamente

## Licencia

Proyecto académico - Universidad/Institución
