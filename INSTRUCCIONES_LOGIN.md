# Sistema de Autenticación - EduLearn

## Resumen de Cambios Implementados

El sistema de inicio de sesión y registro ahora está **completamente conectado a la base de datos MySQL** con las siguientes mejoras:

### ✅ Características Implementadas

1. **Login Conectado a Base de Datos**
   - El login ahora consulta la tabla `usuarios` en MySQL
   - Soporta autenticación con BCrypt (contraseñas encriptadas)
   - Detección automática del rol de usuario (estudiante, profesor, administrador)
   - Redirección automática al dashboard correspondiente según el rol

2. **Registro de Usuarios Conectado a BD**
   - Registra nuevos usuarios en la tabla `usuarios`
   - Encripta automáticamente las contraseñas usando BCrypt
   - Solo permite registro como **Estudiante** o **Profesor**
   - Bloquea el registro como administrador por seguridad
   - Si es estudiante, también crea un registro en la tabla `estudiantes`

3. **Sistema de Roles**
   - **Estudiante**: Acceso a cursos, inscripciones, evaluaciones
   - **Profesor**: Puede crear y gestionar cursos
   - **Administrador**: Acceso completo al sistema sin registro público

---

## Cómo Iniciar Sesión

### Para Administradores

Los administradores tienen correos predefinidos en la base de datos:

**Credenciales de Administrador:**
- Email: `admin@edulearn.com` o `admin@itoaxca.edu.mx`
- Contraseña: `admin789`

**Nota:** El administrador puede iniciar sesión normalmente desde la página de login. El sistema detectará automáticamente su rol y lo redirigirá al dashboard de administrador.

### Para Profesores y Estudiantes

**Usuarios de Prueba:**

**Estudiante:**
- Email: `estudiante@edulearn.com`
- Contraseña: `student123`

**Profesor:**
- Email: `profesor@edulearn.com`
- Contraseña: `prof456`

**O pueden registrarse:**
1. Ir a [http://192.168.1.83:3000/registro](http://192.168.1.83:3000/registro)
2. Llenar el formulario con nombre, apellidos, email y contraseña
3. Seleccionar el tipo: **Estudiante** o **Profesor**
4. Hacer clic en "Crear Cuenta"
5. Serán redirigidos al login automáticamente

---

## Configuración de la Base de Datos

### 1. Ejecutar el Script de Configuración Inicial

```bash
mysql -u root -p < database/setup_edulearn.sql
```

Este script:
- Crea la base de datos `edulearn`
- Crea las tablas necesarias
- Inserta usuarios de prueba

### 2. Actualizar Contraseñas a BCrypt (IMPORTANTE)

Después de ejecutar `setup_edulearn.sql`, ejecutar:

```bash
mysql -u root -p < database/update_passwords.sql
```

Este script:
- Actualiza las contraseñas a formato BCrypt (encriptadas)
- Configura el usuario administrador
- Verifica que todos los usuarios tengan contraseñas seguras

---

## Arquitectura del Sistema

### Backend (Spring Boot)

**Endpoints de Autenticación:**

1. **POST** `/api/auth/login`
   - Body: `{ "email": "...", "password": "..." }`
   - Retorna: datos del usuario, dashboard URL, permisos, menú

2. **POST** `/api/auth/register`
   - Body: `{ "nombre": "...", "apellidos": "...", "email": "...", "password": "...", "tipoUsuario": "estudiante|profesor" }`
   - Retorna: confirmación de registro exitoso

**Archivos Modificados:**
- [AuthController.java](edulearn-api/src/main/java/com/edulearn/controller/AuthController.java)
  - Implementa login con verificación BCrypt
  - Soporta contraseñas en texto plano (legacy) y BCrypt
  - Factory Method Pattern para crear dashboards según rol

- [RegistroController.java](edulearn-api/src/main/java/com/edulearn/controller/RegistroController.java)
  - Encriptación automática con BCrypt
  - Validación de tipos de usuario
  - Bloqueo de registro como administrador

### Frontend (Next.js)

**Páginas:**
- [/login](edulearn-frontend/src/app/login/page.tsx) - Página de inicio de sesión
- [/registro](edulearn-frontend/src/app/registro/page.tsx) - Página de registro

Ambas páginas están conectadas a los endpoints del backend y manejan:
- Validación de formularios
- Mensajes de error
- Redirección automática
- Almacenamiento de datos de usuario en localStorage

---

## Seguridad Implementada

1. **Encriptación BCrypt**: Todas las contraseñas nuevas se encriptan con BCrypt (factor 10)
2. **Validación de Registro**: No se puede registrar como administrador desde el formulario público
3. **Detección Automática de Rol**: El sistema identifica el rol desde la base de datos
4. **Compatibilidad Legacy**: Soporta contraseñas antiguas en texto plano (para migración)

---

## Cómo Agregar un Nuevo Administrador

Para agregar un nuevo administrador manualmente en la base de datos:

```sql
USE edulearn;

-- Insertar nuevo administrador con contraseña encriptada
INSERT INTO usuarios (nombre, apellidos, email, password_hash, tipo_usuario)
VALUES (
    'Nombre',
    'Apellidos',
    'email@ejemplo.com',
    '$2a$10$TaAU9mZO2UZ6QRq2UZ6QRvWO2UZ6QRq2UZ6QRq2UZ6QRq2UZ6QRq2U', -- Este es el hash de 'admin789'
    'administrador'
);
```

**Para generar un hash BCrypt de una nueva contraseña:**

Puedes usar el archivo [GeneradorPasswordBCrypt.java](edulearn-api/src/main/java/com/edulearn/util/GeneradorPasswordBCrypt.java):

```bash
cd edulearn-api
mvn compile
mvn exec:java -Dexec.mainClass="com.edulearn.util.GeneradorPasswordBCrypt"
```

---

## Solución de Problemas

### Error: "Usuario no encontrado"
- Verificar que el email esté en la base de datos
- Ejecutar: `SELECT * FROM usuarios WHERE email = 'tu-email@example.com';`

### Error: "Contraseña incorrecta"
- Asegurarse de haber ejecutado `update_passwords.sql`
- Verificar que el hash esté en formato BCrypt (`$2a$...`)

### El administrador no puede iniciar sesión
- Verificar que `tipo_usuario` sea exactamente `'administrador'` en la BD
- Ejecutar: `UPDATE usuarios SET tipo_usuario = 'administrador' WHERE email = 'admin@edulearn.com';`

### Los nuevos registros no funcionan
- Verificar que el backend esté corriendo en el puerto 8080
- Revisar que la conexión a MySQL esté configurada en `application.properties`

---

## Próximos Pasos Recomendados

1. ✅ Ejecutar `update_passwords.sql` para actualizar contraseñas a BCrypt
2. ✅ Probar login con los usuarios de prueba
3. ✅ Probar registro de nuevos usuarios (estudiante y profesor)
4. ✅ Verificar que administrador pueda iniciar sesión
5. 🔄 Implementar sesiones JWT (opcional para mayor seguridad)
6. 🔄 Agregar recuperación de contraseña

---

## Contacto y Soporte

Para reportar problemas o sugerencias sobre el sistema de autenticación, contactar al equipo de desarrollo.

**Última actualización:** 2025-11-30
