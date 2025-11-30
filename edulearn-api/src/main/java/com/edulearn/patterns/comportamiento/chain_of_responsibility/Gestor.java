package com.edulearn.patterns.comportamiento.chain_of_responsibility;

/**
 * Clase abstracta Gestor (Handler) - Estructura clásica del patrón Chain of Responsibility
 *
 * Cada gestor concreto decide si procesa la solicitud o la pasa al siguiente en la cadena.
 * Esta es la implementación tradicional del patrón GoF.
 */
public abstract class Gestor {
    protected Gestor siguiente;

    /**
     * Establece el siguiente gestor en la cadena
     * @param siguiente El siguiente gestor que procesará la solicitud si este no puede
     * @return El gestor siguiente (permite encadenamiento fluido)
     */
    public Gestor establecerSiguiente(Gestor siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    /**
     * Método plantilla que define el flujo de procesamiento.
     * Llama a manejar() y si es necesario pasa al siguiente.
     *
     * @param solicitud La solicitud de validación a procesar
     */
    public void solicita(SolicitudValidacion solicitud) {
        if (!manejar(solicitud)) {
            // Si la validación falló, detener la cadena
            return;
        }

        // Si pasó la validación y hay un siguiente gestor, continuar
        if (siguiente != null) {
            siguiente.solicita(solicitud);
        } else {
            // Si llegamos al final de la cadena sin errores, aprobar
            solicitud.setAprobada(true);
        }
    }

    /**
     * Método abstracto que cada gestor concreto debe implementar.
     * Define la lógica específica de validación.
     *
     * @param solicitud La solicitud a validar
     * @return true si la validación pasó, false si falló
     */
    protected abstract boolean manejar(SolicitudValidacion solicitud);
}
