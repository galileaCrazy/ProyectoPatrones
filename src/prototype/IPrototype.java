package prototype;

/**
 * PROTOTYPE (Prototipo) - Interface que define el metodo de clonacion.
 * Permite crear nuevos objetos copiando una instancia existente.
 *
 * @param <T> Tipo del objeto que se clonara
 */
public interface IPrototype<T> {

    /**
     * Crea una copia del objeto actual.
     * @return Una nueva instancia con los mismos valores
     */
    T clonar();

    /**
     * Crea una copia del objeto para un nuevo periodo academico.
     * @param nuevoPeriodo El nuevo periodo academico
     * @return Una nueva instancia adaptada al nuevo periodo
     */
    T clonarParaNuevoPeriodo(String nuevoPeriodo);
}
