package es.uji.ei1050.ccc.model;

import java.util.Date;

public class Notificacion {

    Date fechaHora; // date
    String asunto; // varchar 200

    // --------------------------//


    public Notificacion() {
        super();
    }

    public Notificacion(Date fechaHora, String asunto) {
        this.fechaHora = fechaHora;
        this.asunto = asunto;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "fechaHora=" + fechaHora +
                ", asunto='" + asunto + '\'' +
                '}';
    }
}
