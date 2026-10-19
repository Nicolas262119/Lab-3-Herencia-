import java.util.ArrayList;
/** Almacena colecciones, evita códigos duplicados y produce reportes. */
public class Empresa {
    private ArrayList<OrdenServicio> ordenes = new ArrayList<OrdenServicio>();
    private ArrayList<Cuadrilla> cuadrillas = new ArrayList<Cuadrilla>();
    private ArrayList<Jornada> jornadas = new ArrayList<Jornada>();
    public ArrayList<OrdenServicio> getOrdenes() { return new ArrayList<OrdenServicio>(ordenes); }
    public ArrayList<Cuadrilla> getCuadrillas() { return new ArrayList<Cuadrilla>(cuadrillas); }
    public ArrayList<Jornada> getJornadas() { return new ArrayList<Jornada>(jornadas); }
    public OrdenServicio buscarOrden(String codigo) {
        if (codigo == null) return null;
        for (OrdenServicio orden : ordenes) if (orden.getCodigo().equalsIgnoreCase(codigo.trim())) return orden;
        return null;
    }
    public Cuadrilla buscarCuadrilla(String codigo) {
        if (codigo == null) return null;
        for (Cuadrilla cuadrilla : cuadrillas) if (cuadrilla.getCodigo().equalsIgnoreCase(codigo.trim())) return cuadrilla;
        return null;
    }
    public Jornada buscarJornada(String codigo) {
        if (codigo == null) return null;
        for (Jornada jornada : jornadas) if (jornada.getCodigo().equalsIgnoreCase(codigo.trim())) return jornada;
        return null;
    }
    public void registrarOrden(OrdenServicio orden) {
        if (orden == null || buscarOrden(orden.getCodigo()) != null)
            throw new IllegalArgumentException("Código de orden repetido o inválido.");
        ordenes.add(orden); orden.registrar();
    }
    /** Las órdenes iniciales representan trabajos previos sin ingresos registrados. */
    public void cargarOrdenInicial(OrdenServicio orden) {
        if (orden == null || buscarOrden(orden.getCodigo()) != null)
            throw new IllegalArgumentException("Código de orden repetido o inválido.");
        ordenes.add(orden);
    }
    public void registrarCuadrilla(Cuadrilla cuadrilla) {
        if (cuadrilla == null || buscarCuadrilla(cuadrilla.getCodigo()) != null)
            throw new IllegalArgumentException("Código de cuadrilla repetido o inválido.");
        cuadrillas.add(cuadrilla);
    }
    public Jornada crearJornada(String codigo, Cuadrilla cuadrilla) {
        if (codigo == null || codigo.trim().isEmpty() || buscarJornada(codigo) != null || cuadrilla == null)
            throw new IllegalArgumentException("Código de jornada repetido o cuadrilla inválida.");
        Jornada jornada = new Jornada(codigo, cuadrilla);
        jornadas.add(jornada);
        return jornada;
    }
    public String reporte() {
        String[] categorias = {"DIAGNOSTICO", "INSTALACION", "ELECTRICA_CERTIFICADA", "MANTENIMIENTO_AIRE"};
        String[] estados = {"PENDIENTE", "ASIGNADA", "EN_ATENCION", "COMPLETADA", "CANCELADA"};
        String resultado = "\n=== REPORTE GENERAL ===\n";
        for (String categoria : categorias) {
            int total = 0; double ingresosCategoria = 0;
            for (OrdenServicio orden : ordenes) if (orden.getCategoria().equals(categoria)) {
                total++; ingresosCategoria += orden.getIngresoRegistro() + orden.getIngresoVisitas();
            }
            resultado += categoria + ": " + total + " órdenes | Ingresos: Q" + String.format("%.2f", ingresosCategoria) + "\n";
        }
        for (String estado : estados) {
            int total = 0;
            for (OrdenServicio orden : ordenes) if (orden.getEstado().equals(estado)) total++;
            resultado += estado + ": " + total + " órdenes\n";
        }
        double registros = 0, visitas = 0;
        for (OrdenServicio orden : ordenes) {
            registros += orden.getIngresoRegistro(); visitas += orden.getIngresoVisitas();
        }
        resultado += "Ingresos por registro: Q" + String.format("%.2f", registros)
            + " | Por visitas adicionales: Q" + String.format("%.2f", visitas)
            + " | Total: Q" + String.format("%.2f", registros + visitas) + "\n";
        resultado += "Jornadas sin cerrar:\n";
        int abiertas = 0;
        for (Jornada jornada : jornadas) if (!jornada.getEstado().equals("CERRADA")) {
            resultado += jornada + "\n"; abiertas++;
        }
        if (abiertas == 0) resultado += "Ninguna.\n";
        return resultado;
    }
}