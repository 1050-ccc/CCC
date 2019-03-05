package es.uji.ei1050.ccc.model;

public class Empresa {

    String CIF; // PK varchar 10
    String nombre; // varchar 50
    String nombreComercial; // varchar 50
    String domicilioFiscal; // varchar 100
    String cuentaBancaria; // varchar 25

    String personaJuridica; // varchar 50
    String personaFiscal; // varchar 50

    String email; // varchar 50
    int telefono; // int

    // --------------------------//

    public Empresa() {
        super();
    }

    public Empresa(String CIF, String nombre, String nombreComercial, String domicilioFiscal, String cuentaBancaria, String personaJuridica, String personaFiscal, String email, int telefono) {
        this.CIF = CIF;
        this.nombre = nombre;
        this.nombreComercial = nombreComercial;
        this.domicilioFiscal = domicilioFiscal;
        this.cuentaBancaria = cuentaBancaria;
        this.personaJuridica = personaJuridica;
        this.personaFiscal = personaFiscal;
        this.email = email;
        this.telefono = telefono;
    }

    public String getCIF() {
        return CIF;
    }

    public void setCIF(String CIF) {
        this.CIF = CIF;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public void setDomicilioFiscal(String domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    public String getPersonaJuridica() {
        return personaJuridica;
    }

    public void setPersonaJuridica(String personaJuridica) {
        this.personaJuridica = personaJuridica;
    }

    public String getPersonaFiscal() {
        return personaFiscal;
    }

    public void setPersonaFiscal(String personaFiscal) {
        this.personaFiscal = personaFiscal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "CIF='" + CIF + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nombreComercial='" + nombreComercial + '\'' +
                ", domicilioFiscal='" + domicilioFiscal + '\'' +
                ", cuentaBancaria='" + cuentaBancaria + '\'' +
                ", personaJuridica='" + personaJuridica + '\'' +
                ", personaFiscal='" + personaFiscal + '\'' +
                ", email='" + email + '\'' +
                ", telefono=" + telefono +
                '}';
    }
}
