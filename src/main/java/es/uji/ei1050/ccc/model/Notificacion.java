package es.uji.ei1050.ccc.model;

import java.util.Date;

public class Notificacion {

    int idNotificacion;
    Date fechaHora; // date
    String Asunto; // varchar 200
    String Persone_dni;


// --------------------------//


    public Notificacion() {
        super();
    }

    public Notificacion(int idNotificacion, Date fechaHora, String asunto, String persone_dni) {
        this.idNotificacion = idNotificacion;
        this.fechaHora = fechaHora;
        Asunto = asunto;
        Persone_dni = persone_dni;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
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
        Asunto = asunto;
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
                "idNotificacion=" + idNotificacion +
                ", fechaHora=" + fechaHora +
                ", Asunto='" + Asunto + '\'' +
                ", Persone_dni='" + Persone_dni + '\'' +
                '}';
    }
}
