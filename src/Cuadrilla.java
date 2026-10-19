/** Personal disponible para una jornada a la vez. */
public class Cuadrilla {
    private String codigo;
    private double capacidadHoras;
    private boolean certificadaRefrigeracion;
    private Jornada jornadaAbierta;
    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty() || !Double.isFinite(capacidadHoras) || capacidadHoras <= 0)
            throw new IllegalArgumentException("Código o capacidad de cuadrilla inválidos.");
        this.codigo = codigo.trim(); this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }
    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }
    public Jornada getJornadaAbierta() { return jornadaAbierta; }
    public void abrirJornada(Jornada jornada) {
        if (jornadaAbierta != null) throw new IllegalStateException("La cuadrilla ya tiene una jornada sin cerrar.");
        jornadaAbierta = jornada;
    }
    public void cerrarJornada(Jornada jornada) {
        if (jornadaAbierta == jornada) jornadaAbierta = null;
    }
    @Override public String toString() {
        return codigo + " | Capacidad: " + capacidadHoras + " h | Refrigeración: "
            + (certificadaRefrigeracion ? "Sí" : "No") + " | Jornada abierta: "
            + (jornadaAbierta == null ? "Ninguna" : jornadaAbierta.getCodigo());
    }
}