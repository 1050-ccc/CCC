package es.uji.ei1050.ccc.model;

public enum ETipoContrato {
    IN("Indefinido"),
    TP("Temporal");

    private final String descripcion;

    private ETipoContrato(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @param estado
     * @return Devuelve la enumeración a partir del nombre
     */
    public static ETipoContrato getEstado(String estado) {
        for (ETipoContrato e : ETipoContrato.values())
            if (e.toString().equals(estado))
                return e;
        throw new IllegalArgumentException("Turno no encontrado");
    }

    public String getDescripcion() {
        return descripcion;
    }
}