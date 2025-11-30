package com.edulearn.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt de contraseñas
 * Ejecutar este programa para obtener los hashes que se usarán en la base de datos
 */
public class GeneradorPasswordBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== Generador de Hashes BCrypt ===\n");

        // Generar hashes para las contraseñas de prueba
        String[] passwords = {
            "student123",
            "prof456",
            "admin789",
            "juan123",
            "maria123"
        };

        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.println("Contraseña: " + password);
            System.out.println("Hash BCrypt: " + hash);
            System.out.println();
        }
    }
}
