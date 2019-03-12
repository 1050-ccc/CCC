package es.uji.ei1050.ccc.model;

/**
 * Modelo para los tipos de perfiles/roles de usuarios
 *
 */
public enum Perfiles {
    JF("Jefe"),
    TR("Trabajador"),
    ADMIN("Administrador sistema");

    private final String descripcion;

    private Perfiles(String descripcion){
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la enumeración que corresponde al nombre de esta
     * @param estado
     * @return Devuelve un enum de Perfiles
     */
    public static Perfiles getEstado(String estado){
        for(Perfiles e : Perfiles.values()) {
            if(e.toString().equals(estado))
                return e;}
        throw new IllegalArgumentException("Estado no encontrado");
    }

    public String getDescripcion(){
        return descripcion;
    }

}
