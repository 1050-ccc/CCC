package es.uji.ei1050.ccc.model;

public enum ETurnos {
    Mañana("Mañana"),
    Tarde("Tarde"),
    Noche("Noche");

    private final String descripcion;

    private ETurnos(String descripcion){
        this.descripcion = descripcion;
    }

    /**
     * @param estado
     * @return Devuelve la enumeración a partir del nombre
     */
    public static ETurnos getEstado(String estado){
        for(ETurnos e : ETurnos.values())
            if(e.toString().equals(estado))
                return e;
        throw new IllegalArgumentException("Turno no encontrado");
    }

    public String getDescripcion() {
        return descripcion;
    }
}
