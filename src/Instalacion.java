/** Instalación normal; las horas facturadas se redondean hacia arriba. */
public class Instalacion extends OrdenServicio {
    private int puntos;
    public Instalacion(String codigo, String cliente, String direccion, double horas, String zona, int puntos) {
        super(codigo, cliente, direccion, horas, zona);
        if (puntos <= 0) throw new IllegalArgumentException("Los puntos deben ser positivos.");
        this.puntos = puntos;
    }
    public int getPuntos() { return puntos; }
    public int getHorasFacturables() { return (int) Math.ceil(Math.max(getHorasEstimadas(), puntos * 0.75)); }
    @Override public String getCategoria() { return "INSTALACION"; }
    @Override public double calcularCosto() { return tarifaBase() + 90 * getHorasFacturables(); }
    @Override public int estimarDias() { return getZona().equals("METROPOLITANA") ? 3 : 5; }
    @Override public int maximoVisitas() { return 3; }
    @Override public String toString() { return super.toString() + " | Puntos: " + puntos; }
}
