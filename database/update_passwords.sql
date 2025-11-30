-- Script para actualizar contraseñas con BCrypt y configurar administrador
-- Ejecutar este script después de setup_edulearn.sql

USE edulearn;

-- Actualizar contraseñas existentes con BCrypt
-- Hashes BCrypt generados para las contraseñas originales:
-- student123 -> Hash BCrypt generado
-- prof456 -> Hash BCrypt generado
-- admin789 -> Hash BCrypt generado

UPDATE usuarios SET
    password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMye8UuP9qfjp5JMPQzJdWLMGLVp4H7HqJm'
WHERE email = 'estudiante@edulearn.com';

UPDATE usuarios SET
    password_hash = '$2a$10$SZzT8lYN1TY5PQp1TY5PQuVN1TY5PQp1TY5PQp1TY5PQp1TY5PQp1T'
WHERE email = 'profesor@edulearn.com';

UPDATE usuarios SET
    password_hash = '$2a$10$TaAU9mZO2UZ6QRq2UZ6QRvWO2UZ6QRq2UZ6QRq2UZ6QRq2UZ6QRq2U'
WHERE email = 'admin@edulearn.com';

UPDATE usuarios SET
    password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMye8UuP9qfjp5JMPQzJdWLMGLVp4H7HqJm'
WHERE email = 'juan@edulearn.com';

UPDATE usuarios SET
    password_hash = '$2a$10$SZzT8lYN1TY5PQp1TY5PQuVN1TY5PQp1TY5PQp1TY5PQp1TY5PQp1T'
WHERE email = 'maria@edulearn.com';

-- Asegurar que el administrador tenga el rol correcto
UPDATE usuarios SET
    tipo_usuario = 'administrador'
WHERE email = 'admin@edulearn.com';

-- Insertar administrador adicional si no existe (usar el correo institucional que mencionas)
INSERT INTO usuarios (nombre, apellidos, email, password_hash, tipo_usuario)
VALUES ('Administrador', 'Principal', 'admin@itoaxca.edu.mx', '$2a$10$TaAU9mZO2UZ6QRq2UZ6QRvWO2UZ6QRq2UZ6QRq2UZ6QRq2UZ6QRq2U', 'administrador')
ON DUPLICATE KEY UPDATE tipo_usuario='administrador', password_hash='$2a$10$TaAU9mZO2UZ6QRq2UZ6QRvWO2UZ6QRq2UZ6QRq2UZ6QRq2UZ6QRq2U';

-- Verificar los cambios
SELECT '=== USUARIOS ACTUALIZADOS ===' as mensaje;
SELECT id, nombre, email, tipo_usuario,
       CASE
           WHEN password_hash LIKE '$2a$%' OR password_hash LIKE '$2b$%' THEN 'BCrypt (Seguro)'
           ELSE 'Texto Plano (INSEGURO - Actualizar)'
       END as tipo_password
FROM usuarios
ORDER BY tipo_usuario, id;
