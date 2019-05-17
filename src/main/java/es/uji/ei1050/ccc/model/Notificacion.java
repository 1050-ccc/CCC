package es.uji.ei1050.ccc.model;

import java.util.Date;

public class Notificacion {

    Date fechaHora; // date
    String Asunto; // varchar 200
    String Persone_dni;


// --------------------------//


    public Notificacion() {
        super();
    }

    public Notificacion(Date fechaHora, String asunto) {
        this.fechaHora = fechaHora;
        this.Asunto = asunto;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getAsunto() {
        return Asunto;
    }

    public void setAsunto(String asunto) {
        this.Asunto = asunto;
    }

    public String getPersone_dni() {
        return Persone_dni;
    }

    public void setPersone_dni(String persone_dni) {
        Persone_dni = persone_dni;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "fechaHora=" + fechaHora +
                ", asunto='" + Asunto + '\'' +
                '}';
    }
}
