/** Clase padre: datos y estado comunes de cualquier orden. */
public abstract class OrdenServicio {
    private String codigo, cliente, direccion, zona, estado;
    private double horasEstimadas, ingresoRegistro, ingresoVisitas;
    private int visitasRealizadas;
    private Jornada jornada;

    public OrdenServicio(String codigo, String cliente, String direccion, double horasEstimadas, String zona) {
        if (vacio(codigo) || vacio(cliente) || vacio(direccion) || !Double.isFinite(horasEstimadas)
                || horasEstimadas <= 0 || (!"METROPOLITANA".equalsIgnoreCase(zona)
                && !"PERIFERICA".equalsIgnoreCase(zona)))
            throw new IllegalArgumentException("Datos de la orden inválidos. Zona: METROPOLITANA o PERIFERICA.");
        this.codigo = codigo.trim(); this.cliente = cliente.trim(); this.direccion = direccion.trim();
        this.horasEstimadas = horasEstimadas; this.zona = zona.toUpperCase(); this.estado = "PENDIENTE";
    }
    protected static boolean vacio(String texto) { return texto == null || texto.trim().isEmpty(); }
    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public String getDireccion() { return direccion; }
    public String getZona() { return zona; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public String getEstado() { return estado; }
    public int getVisitasRealizadas() { return visitasRealizadas; }
    public Jornada getJornada() { return jornada; }
    public double getIngresoRegistro() { return ingresoRegistro; }
    public double getIngresoVisitas() { return ingresoVisitas; }
    protected double tarifaBase() { return zona.equals("METROPOLITANA") ? 100 : 175; }
    public abstract String getCategoria();
    public abstract double calcularCosto();
    public abstract int estimarDias();
    public abstract int maximoVisitas();
    /** Por defecto cualquier cuadrilla puede prestar el servicio. */
    public boolean puedeAtender(Cuadrilla cuadrilla) { return true; }
    public void registrar() { ingresoRegistro = calcularCosto(); }
    public void asignar(Jornada nueva) {
        if (!estado.equals("PENDIENTE") || jornada != null) throw new IllegalStateException("La orden no está disponible.");
        jornada = nueva; estado = "ASIGNADA";
    }
    public void retirar() {
        if (!estado.equals("ASIGNADA")) throw new IllegalStateException("La orden no está asignada.");
        jornada = null; estado = "PENDIENTE";
    }
    public void iniciarVisita() {
        if (!estado.equals("ASIGNADA")) throw new IllegalStateException("La orden no está asignada.");
        // El cargo se aplica al iniciar la segunda visita y las siguientes.
        if (visitasRealizadas >= 1) ingresoVisitas += 40;
        visitasRealizadas++; estado = "EN_ATENCION";
    }
    public void registrarResultado(boolean completada) {
        if (!estado.equals("EN_ATENCION")) throw new IllegalStateException("La orden no está en atención.");
        if (completada) estado = "COMPLETADA";
        else if (visitasRealizadas >= maximoVisitas()) estado = "CANCELADA";
        else estado = "PENDIENTE";
        // La referencia de jornada se libera al cerrar toda la jornada.
    }
    public void liberarJornada(Jornada actual) {
        if (jornada == actual) jornada = null;
    }
    @Override public String toString() {
        return codigo + " | " + getCategoria() + " | " + cliente + " | " + direccion + " | " + zona
            + " | " + horasEstimadas + " h | " + estado + " | Visitas: " + visitasRealizadas
            + "/" + maximoVisitas() + " | Costo: Q" + String.format("%.2f", calcularCosto());
    }
    @Override public boolean equals(Object otro) {
        if (!(otro instanceof OrdenServicio)) return false;
        return codigo.equalsIgnoreCase(((OrdenServicio) otro).codigo);
    }
    @Override public int hashCode() { return codigo.toLowerCase().hashCode(); }
}
