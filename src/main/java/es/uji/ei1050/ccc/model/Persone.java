package es.uji.ei1050.ccc.model;

public class Persone {

    String nombre; // varchar 15
    String apellidos; // varchar 30
    String dni; // varchar 9 PK

    String domicilio; // varchar 100
    int telefono; // int
    String email; // varchar 50

    String cuentaBancaria; // varchar 25

    String Empresa_cif; // varchar 50

    // --------------------------//


    public Persone() {
        super();
    }

    public Persone(String nombre, String apellidos, String dni, String domicilio, int telefono, String email, String cuentaBancaria, String Empresa_cif) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.email = email;
        this.cuentaBancaria = cuentaBancaria;
        this.Empresa_cif = Empresa_cif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getEmpresa_cif() {
        return this.Empresa_cif;
    }

    public void setEmpresa_cif(String Empresa_cif) {
        this.Empresa_cif = Empresa_cif;
    }

    @Override
    public String toString() {
        return "Persone{" +
                "nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", cuentaBancaria='" + cuentaBancaria + '\'' +
                '}';
    }
}
