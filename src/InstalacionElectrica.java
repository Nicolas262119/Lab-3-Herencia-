/** Es una instalación, con certificado y garantía adicionales. */
public class InstalacionElectrica extends Instalacion {
    private double valorMateriales;
    public InstalacionElectrica(String codigo, String cliente, String direccion, double horas,
                               String zona, int puntos, double valorMateriales) {
        super(codigo, cliente, direccion, horas, zona, puntos);
        if (!Double.isFinite(valorMateriales) || valorMateriales <= 0)
            throw new IllegalArgumentException("El valor de los materiales debe ser positivo.");
        this.valorMateriales = valorMateriales;
    }
    public double getValorMateriales() { return valorMateriales; }
    @Override public String getCategoria() { return "ELECTRICA_CERTIFICADA"; }
    @Override public double calcularCosto() { return super.calcularCosto() + 120 + valorMateriales * 0.05; }
    @Override public int estimarDias() { return super.estimarDias() + 2; }
    @Override public int maximoVisitas() { return 2; }
    @Override public String toString() { return super.toString() + " | Materiales: Q" + String.format("%.2f", valorMateriales); }
}