package es.uji.ei1050.ccc.model;

public class Contrato {

    int sueldoBase; // int
    int diasVacaciones; // int
    String tipoContrato; // varchar 10
    String Persone_dni; // varchar 9

    // --------------------------//

    public Contrato() {
        super();
    }

    public Contrato(int sueldoBase, int diasVacaciones, String tipoContrato, String Persone_dni) {
        this.sueldoBase = sueldoBase;
        this.diasVacaciones = diasVacaciones;
        this.tipoContrato = tipoContrato;
        this.Persone_dni = Persone_dni;
    }

    public int getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(int sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public int getDiasVacaciones() {
        return diasVacaciones;
    }

    public void setDiasVacaciones(int diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getPersone_dni() {
        return Persone_dni;
    }

    public void setPersone_dni(String persone_dni) {
        Persone_dni = persone_dni;
    }

    @Override
    public String toString() {
        return "Contrato{" +
                "sueldoBase=" + sueldoBase +
                ", diasVacaciones=" + diasVacaciones +
                ", tipoContrato=" + tipoContrato +
                '}';
    }
}
