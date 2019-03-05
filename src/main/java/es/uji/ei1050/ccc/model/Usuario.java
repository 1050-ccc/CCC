package es.uji.ei1050.ccc.model;

/**
 * Clase usuario para guardar la información de login en la base
 *         de datos
 */
public class Usuario {

    String usuario; // varchar 20
    String password; // varchar 256
    Perfiles tipo;

    // --------------------------//

    /**
     *
     */
    public Usuario() {
        super();
    }

    /**
     * @param usuario
     * @param password
     * @param tipo
     */
    public Usuario(String usuario, String password, Perfiles tipo) {
        super();
        this.usuario = usuario;
        this.password = password;
        this.tipo = tipo;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario
     *            the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the tipo
     */
    public Perfiles getTipo() {
        return tipo;
    }

    /**
     * @param tipo
     *            the tipo to set
     */
    public void setTipo(Perfiles tipo) {
        this.tipo = tipo;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Usuario [usuario=" + usuario + ", password=" + password + ", tipo=" + tipo + "]";
    }

}
