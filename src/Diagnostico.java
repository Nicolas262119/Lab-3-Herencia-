/** Diagnóstico: tarifa fija y hasta tres visitas. */
public class Diagnostico extends OrdenServicio {
    public Diagnostico(String codigo, String cliente, String direccion, double horas, String zona) {
        super(codigo, cliente, direccion, horas, zona);
        if (horas > 1.5) throw new IllegalArgumentException("Un diagnóstico no puede exceder 1.5 horas.");
    }
    @Override public String getCategoria() { return "DIAGNOSTICO"; }
    @Override public double calcularCosto() { return getZona().equals("METROPOLITANA") ? 150 : 225; }
    @Override public int estimarDias() { return getZona().equals("METROPOLITANA") ? 1 : 2; }
    @Override public int maximoVisitas() { return 3; }
}