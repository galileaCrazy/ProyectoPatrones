package com.edulearn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Configuración para asegurar que todas las respuestas HTTP usen UTF-8
 * Esto resuelve problemas de codificación con caracteres españoles (ñ, á, etc.)
 *
 * Nota: El filtro CharacterEncodingFilter ya está configurado por Spring Boot
 * automáticamente con las propiedades en application.properties
 */
@Configuration
public class EncodingConfig implements WebMvcConfigurer {

    /**
     * Configura los convertidores de mensajes HTTP para usar UTF-8
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);
    }
}
