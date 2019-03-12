package es.uji.ei1050.ccc.model;

/**
 * Clase email para guardar la información de login en la base
 * de datos
 */
public class Usuario {

    String email; // varchar 20 EMAIL
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
     * @param email
     * @param password
     * @param tipo
     */
    public Usuario(String email, String password, Perfiles tipo) {
        super();
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
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
     * @param tipo the tipo to set
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
        return "Usuario [email=" + email + ", password=" + password + ", tipo=" + tipo + "]";
    }

}
