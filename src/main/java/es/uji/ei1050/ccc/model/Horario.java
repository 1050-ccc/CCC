package es.uji.ei1050.ccc.model;

import java.sql.Time;
import java.util.Date;

public class Horario {
    Date dia; // date
    Time horaInicio;
    Time horaFin;
    int horasTrabajadas;
    String personeDNI;
    String personeNombre;

    public Horario() {
        super();
    }

    public Horario(Date dia, Time horaInicio, Time horaFin, int horasTrabajadas, String personeDNI, String personeNombre) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.horasTrabajadas = horasTrabajadas;
        this.personeDNI = personeDNI;
        this.personeNombre = personeNombre;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public String getPersoneDNI() {
        return personeDNI;
    }

    public void setPersoneDNI(String personeDNI) {
        this.personeDNI = personeDNI;
    }

    public String getPersoneNombre() {
        return personeNombre;
    }

    public void setPersoneNombre(String personeNombre) {
        this.personeNombre = personeNombre;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "dia=" + dia +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", horasTrabajadas=" + horasTrabajadas +
                ", personeDNI='" + personeDNI + '\'' +
                ", personeNombre='" + personeNombre + '\'' +
                '}';
    }
}
