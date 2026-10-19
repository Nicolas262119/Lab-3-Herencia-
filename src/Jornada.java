import java.util.ArrayList;
/** Jornada: preparación, inicio, resultados y cierre automático. */
public class Jornada {
    private String codigo, estado;
    private Cuadrilla cuadrilla;
    private ArrayList<OrdenServicio> ordenes;
    public Jornada(String codigo, Cuadrilla cuadrilla) {
        if (codigo == null || codigo.trim().isEmpty() || cuadrilla == null)
            throw new IllegalArgumentException("Datos de jornada inválidos.");
        this.codigo = codigo.trim(); this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<OrdenServicio>(); this.estado = "PREPARACION";
        cuadrilla.abrirJornada(this);
    }
    public String getCodigo() { return codigo; }
    public String getEstado() { return estado; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public ArrayList<OrdenServicio> getOrdenes() { return new ArrayList<OrdenServicio>(ordenes); }
    public double getHorasAsignadas() {
        double suma = 0;
        for (OrdenServicio orden : ordenes) suma += orden.getHorasEstimadas();
        return suma;
    }
    public double getCapacidadDisponible() { return cuadrilla.getCapacidadHoras() - getHorasAsignadas(); }
    public void asignar(OrdenServicio orden) {
        if (!estado.equals("PREPARACION")) throw new IllegalStateException("La jornada ya inició o cerró.");
        if (orden == null || !orden.getEstado().equals("PENDIENTE") || orden.getJornada() != null)
            throw new IllegalStateException("La orden no está pendiente o ya pertenece a una jornada.");
        if (orden.getHorasEstimadas() > getCapacidadDisponible() + 0.000001)
            throw new IllegalStateException("La orden supera la capacidad de horas de la cuadrilla.");
        if (!orden.puedeAtender(cuadrilla))
            throw new IllegalStateException("La cuadrilla no tiene certificación para esta orden.");
        orden.asignar(this); ordenes.add(orden);
    }
    public void retirar(OrdenServicio orden) {
        if (!estado.equals("PREPARACION") || orden == null || orden.getJornada() != this)
            throw new IllegalStateException("No se puede retirar esa orden de esta jornada.");
        orden.retirar(); ordenes.remove(orden);
    }
    public void iniciar() {
        if (!estado.equals("PREPARACION") || ordenes.isEmpty())
            throw new IllegalStateException("La jornada no está en preparación o está vacía.");
        for (OrdenServicio orden : ordenes) orden.iniciarVisita();
        estado = "INICIADA";
    }
    public void registrarResultado(OrdenServicio orden, boolean completada) {
        if (!estado.equals("INICIADA") || orden == null || !ordenes.contains(orden)
                || !orden.getEstado().equals("EN_ATENCION"))
            throw new IllegalStateException("La jornada u orden no permite registrar ese resultado.");
        orden.registrarResultado(completada);
        boolean faltan = false;
        for (OrdenServicio actual : ordenes)
            if (actual.getEstado().equals("EN_ATENCION")) faltan = true;
        if (!faltan) {
            estado = "CERRADA";
            for (OrdenServicio actual : ordenes) actual.liberarJornada(this);
            cuadrilla.cerrarJornada(this);
        }
    }
    @Override public String toString() {
        return codigo + " | Cuadrilla: " + cuadrilla.getCodigo() + " | Estado: " + estado
            + " | Horas: " + String.format("%.2f", getHorasAsignadas())
            + " | Disponibles: " + String.format("%.2f", getCapacidadDisponible());
    }
}