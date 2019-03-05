package es.uji.ei1050.ccc.model;

public class Trabajador extends Persone{

    String puestoTrabajo; // varchar 20
    ETurnos turno; //varchar 10

    // --------------------------//

    public Trabajador() {
        super();
    }

    public Trabajador(String puestoTrabajo, ETurnos turno) {
        this.puestoTrabajo = puestoTrabajo;
        this.turno = turno;
    }

    public String getPuestoTrabajo() {
        return puestoTrabajo;
    }

    public void setPuestoTrabajo(String puestoTrabajo) {
        this.puestoTrabajo = puestoTrabajo;
    }

    public ETurnos getTurno() {
        return turno;
    }

    public void setTurno(ETurnos turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "puestoTrabajo='" + puestoTrabajo + '\'' +
                ", turno=" + turno +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", cuentaBancaria='" + cuentaBancaria + '\'' +
                '}';
    }
}
