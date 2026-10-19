import java.util.Scanner;
/** Menú de consola; concentra la entrada y muestra errores sin cerrar el programa. */
public class Main {
    private static Scanner teclado = new Scanner(System.in);
    public static void main(String[] args) {
        Empresa empresa = cargarDatos();
        int opcion = 0;
        while (opcion != 13) {
            System.out.println("\n=== TECNIHOGAR ===\n1. Cotizar\n2. Registrar orden\n3. Registrar cuadrilla"
                + "\n4. Crear jornada\n5. Asignar orden\n6. Retirar orden\n7. Iniciar jornada"
                + "\n8. Registrar resultado\n9. Consultar órdenes\n10. Consultar cuadrillas"
                + "\n11. Consultar jornadas\n12. Reporte\n13. Salir");
            opcion = entero("Opción: ");
            try {
                switch (opcion) {
                    case 1:
                    case 2:
                        OrdenServicio orden = pedirOrden();
                        System.out.printf("Cotización: Q%.2f | Atención estimada: %d días hábiles%n",
                            orden.calcularCosto(), orden.estimarDias());
                        if (opcion == 2) {
                            empresa.registrarOrden(orden);
                            System.out.println("Orden registrada, cobrada y pendiente.");
                        } else System.out.println("Cotización sin registro ni cobro.");
                        break;
                    case 3:
                        empresa.registrarCuadrilla(new Cuadrilla(texto("Código: "),
                            decimal("Capacidad de horas: "), texto("¿Certificada en refrigeración? (S/N): ").equalsIgnoreCase("S")));
                        System.out.println("Cuadrilla registrada."); break;
                    case 4:
                        empresa.crearJornada(texto("Código de jornada: "),
                            cuadrilla(empresa, texto("Código de cuadrilla: ")));
                        System.out.println("Jornada creada en preparación."); break;
                    case 5:
                        jornada(empresa, texto("Código de jornada: ")).asignar(
                            orden(empresa, texto("Código de orden: ")));
                        System.out.println("Orden asignada."); break;
                    case 6:
                        jornada(empresa, texto("Código de jornada: ")).retirar(
                            orden(empresa, texto("Código de orden: ")));
                        System.out.println("Orden retirada y pendiente."); break;
                    case 7:
                        jornada(empresa, texto("Código de jornada: ")).iniciar();
                        System.out.println("Jornada iniciada; cargos por visitas adicionales registrados."); break;
                    case 8:
                        Jornada actual = jornada(empresa, texto("Código de jornada: "));
                        OrdenServicio atendida = orden(empresa, texto("Código de orden: "));
                        String resultado = texto("Resultado (C completada / F visita fallida): ");
                        if (!resultado.equalsIgnoreCase("C") && !resultado.equalsIgnoreCase("F"))
                            throw new IllegalArgumentException("Resultado inválido: use C o F.");
                        actual.registrarResultado(atendida, resultado.equalsIgnoreCase("C"));
                        System.out.println("Estado de orden: " + atendida.getEstado()
                            + " | Estado de jornada: " + actual.getEstado()); break;
                    case 9:
                        if (empresa.getOrdenes().isEmpty()) System.out.println("No hay órdenes.");
                        for (OrdenServicio o : empresa.getOrdenes()) System.out.println(o); break;
                    case 10:
                        if (empresa.getCuadrillas().isEmpty()) System.out.println("No hay cuadrillas.");
                        for (Cuadrilla c : empresa.getCuadrillas()) System.out.println(c); break;
                    case 11:
                        if (empresa.getJornadas().isEmpty()) System.out.println("No hay jornadas.");
                        for (Jornada j : empresa.getJornadas()) System.out.println(j); break;
                    case 12: System.out.println(empresa.reporte()); break;
                    case 13: System.out.println("Hasta luego."); break;
                    default: System.out.println("Seleccione una opción entre 1 y 13.");
                }
            } catch (IllegalArgumentException | IllegalStateException error) {
                System.out.println("Operación rechazada: " + error.getMessage());
            }
        }
    }
    private static String texto(String mensaje) {
        System.out.print(mensaje); return teclado.nextLine().trim();
    }
    private static int entero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(texto(mensaje)); }
            catch (NumberFormatException error) { System.out.println("Ingrese un número entero válido."); }
        }
    }
    private static double decimal(String mensaje) {
        while (true) {
            try {
                double valor = Double.parseDouble(texto(mensaje));
                if (!Double.isFinite(valor)) throw new NumberFormatException();
                return valor;
            } catch (NumberFormatException error) { System.out.println("Ingrese un número decimal válido (use punto)."); }
        }
    }
    private static OrdenServicio pedirOrden() {
        System.out.println("1. Diagnóstico  2. Instalación  3. Eléctrica certificada  4. Mantenimiento de aire");
        int tipo = entero("Categoría: ");
        if (tipo < 1 || tipo > 4) throw new IllegalArgumentException("Categoría inválida.");
        String codigo = texto("Código: "); String cliente = texto("Cliente: ");
        String direccion = texto("Dirección: "); double horas = decimal("Horas estimadas: ");
        String zona = texto("Zona (METROPOLITANA/PERIFERICA): ");
        if (tipo == 1) return new Diagnostico(codigo, cliente, direccion, horas, zona);
        if (tipo == 2) return new Instalacion(codigo, cliente, direccion, horas, zona, entero("Puntos: "));
        if (tipo == 3) return new InstalacionElectrica(codigo, cliente, direccion, horas, zona,
            entero("Puntos: "), decimal("Valor declarado de materiales: Q"));
        return new MantenimientoAire(codigo, cliente, direccion, horas, zona, entero("Capacidad BTU: "));
    }
    private static OrdenServicio orden(Empresa empresa, String codigo) {
        OrdenServicio resultado = empresa.buscarOrden(codigo);
        if (resultado == null) throw new IllegalArgumentException("Orden no encontrada.");
        return resultado;
    }
    private static Cuadrilla cuadrilla(Empresa empresa, String codigo) {
        Cuadrilla resultado = empresa.buscarCuadrilla(codigo);
        if (resultado == null) throw new IllegalArgumentException("Cuadrilla no encontrada.");
        return resultado;
    }
    private static Jornada jornada(Empresa empresa, String codigo) {
        Jornada resultado = empresa.buscarJornada(codigo);
        if (resultado == null) throw new IllegalArgumentException("Jornada no encontrada.");
        return resultado;
    }
    private static Empresa cargarDatos() {
        Empresa e = new Empresa();
        e.cargarOrdenInicial(new Diagnostico("D01", "Ana", "Zona 1", 1, "METROPOLITANA"));
        e.cargarOrdenInicial(new Diagnostico("D02", "Luis", "Mixco", 1.5, "PERIFERICA"));
        e.cargarOrdenInicial(new Instalacion("I01", "Marta", "Zona 4", 2.5, "METROPOLITANA", 5));
        e.cargarOrdenInicial(new Instalacion("I02", "Sonia", "Villa Nueva", 1.2, "PERIFERICA", 1));
        e.cargarOrdenInicial(new InstalacionElectrica("E01", "Juan", "Zona 10", 2.5, "METROPOLITANA", 5, 2000));
        e.cargarOrdenInicial(new InstalacionElectrica("E02", "Laura", "Amatitlán", 1, "PERIFERICA", 2, 1000));
        e.cargarOrdenInicial(new MantenimientoAire("A01", "Pedro", "Zona 9", 2, "METROPOLITANA", 12000));
        e.cargarOrdenInicial(new MantenimientoAire("A02", "Elena", "Zona 14", 1.5, "METROPOLITANA", 24000));
        e.registrarCuadrilla(new Cuadrilla("C01", 8, true));
        e.registrarCuadrilla(new Cuadrilla("C02", 6, false));
        e.registrarCuadrilla(new Cuadrilla("C03", 4, false));
        return e;
    }
}