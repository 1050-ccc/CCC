package es.uji.ei1050.ccc.model;

public class Jefe extends Persone {

    // --------------------------//

    public Jefe() {
        super();
    }

    @Override
    public String toString() {
        return "Jefe{" +
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
