/** Mantenimiento: solo en zona metropolitana y con cuadrilla certificada. */
public class MantenimientoAire extends OrdenServicio {
    private int btu;
    public MantenimientoAire(String codigo, String cliente, String direccion, double horas, String zona, int btu) {
        super(codigo, cliente, direccion, horas, zona);
        if (!getZona().equals("METROPOLITANA") || btu < 9000 || btu > 60000)
            throw new IllegalArgumentException("El aire requiere zona metropolitana y BTU entre 9000 y 60000.");
        this.btu = btu;
    }
    public int getBtu() { return btu; }
    @Override public String getCategoria() { return "MANTENIMIENTO_AIRE"; }
    @Override public double calcularCosto() { return tarifaBase() + 110 * getHorasEstimadas() + 80; }
    @Override public int estimarDias() { return 2; }
    @Override public int maximoVisitas() { return 1; }
    @Override public boolean puedeAtender(Cuadrilla cuadrilla) { return cuadrilla.isCertificadaRefrigeracion(); }
    @Override public String toString() { return super.toString() + " | BTU: " + btu; }
}