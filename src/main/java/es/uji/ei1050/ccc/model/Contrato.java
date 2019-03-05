package es.uji.ei1050.ccc.model;

public class Contrato {

    int sueldoBase; // int
    int diasVacaciones; // int
    ETipoContrato tipoContrato; // varchar 10

    // --------------------------//

    public Contrato() {
        super();
    }

    public Contrato(int sueldoBase, int diasVacaciones, ETipoContrato tipoContrato) {
        this.sueldoBase = sueldoBase;
        this.diasVacaciones = diasVacaciones;
        this.tipoContrato = tipoContrato;
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

    public ETipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(ETipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
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
