package main;

import entities.DomicilioFiscal;
import entities.Empresa;
import service.DomicilioFiscalService;
import service.EmpresaService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Menú de consola para operaciones CRUD de Empresa y DomicilioFiscal.
 * Maneja entradas del usuario, validaciones y errores.
 */
public class AppMenu {
    
    private final Scanner scanner;
    private final EmpresaService empresaService;
    private final DomicilioFiscalService domicilioFiscalService;
    
    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.empresaService = new EmpresaService();
        this.domicilioFiscalService = new DomicilioFiscalService();
    }
    
    /**
     * Inicia el menú principal.
     */
    public void iniciar() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarMenuPrincipal();
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            try {
                switch (opcion) {
                    case "1":
                        menuEmpresa();
                        break;
                    case "2":
                        menuDomicilioFiscal();
                        break;
                    case "0":
                        continuar = false;
                        System.out.println("\n¡Hasta luego!");
                        break;
                    default:
                        System.out.println("\n❌ Opción inválida. Por favor, seleccione una opción válida.");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: " + e.getMessage());
            }
            
            if (continuar) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Muestra el menú principal.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          MENÚ PRINCIPAL - TFI");
        System.out.println("     Empresa → DomicilioFiscal (1→1)");
        System.out.println("=".repeat(50));
        System.out.println("1. Gestionar Empresas");
        System.out.println("2. Gestionar Domicilios Fiscales");
        System.out.println("0. Salir");
        System.out.println("=".repeat(50));
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Menú de gestión de empresas.
     */
    private void menuEmpresa() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("          GESTIÓN DE EMPRESAS");
            System.out.println("-".repeat(50));
            System.out.println("1. Crear Empresa");
            System.out.println("2. Buscar Empresa por ID");
            System.out.println("3. Buscar Empresa por CUIT");
            System.out.println("4. Buscar Empresa por Razón Social");
            System.out.println("5. Listar Todas las Empresas");
            System.out.println("6. Actualizar Empresa");
            System.out.println("7. Eliminar Empresa (baja lógica)");
            System.out.println("0. Volver al menú principal");
            System.out.println("-".repeat(50));
            System.out.print("Seleccione una opción: ");
            
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            try {
                switch (opcion) {
                    case "1":
                        crearEmpresa();
                        break;
                    case "2":
                        buscarEmpresaPorId();
                        break;
                    case "3":
                        buscarEmpresaPorCuit();
                        break;
                    case "4":
                        buscarEmpresaPorRazonSocial();
                        break;
                    case "5":
                        listarEmpresas();
                        break;
                    case "6":
                        actualizarEmpresa();
                        break;
                    case "7":
                        eliminarEmpresa();
                        break;
                    case "0":
                        continuar = false;
                        break;
                    default:
                        System.out.println("\n❌ Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: " + e.getMessage());
            }
            
            if (continuar && !opcion.equals("0")) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Menú de gestión de domicilios fiscales.
     */
    private void menuDomicilioFiscal() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("      GESTIÓN DE DOMICILIOS FISCALES");
            System.out.println("-".repeat(50));
            System.out.println("1. Crear Domicilio Fiscal");
            System.out.println("2. Buscar Domicilio Fiscal por ID");
            System.out.println("3. Listar Todos los Domicilios Fiscales");
            System.out.println("4. Actualizar Domicilio Fiscal");
            System.out.println("5. Eliminar Domicilio Fiscal (baja lógica)");
            System.out.println("0. Volver al menú principal");
            System.out.println("-".repeat(50));
            System.out.print("Seleccione una opción: ");
            
            String opcion = scanner.nextLine().trim().toUpperCase();
            
            try {
                switch (opcion) {
                    case "1":
                        crearDomicilioFiscal();
                        break;
                    case "2":
                        buscarDomicilioFiscalPorId();
                        break;
                    case "3":
                        listarDomiciliosFiscales();
                        break;
                    case "4":
                        actualizarDomicilioFiscal();
                        break;
                    case "5":
                        eliminarDomicilioFiscal();
                        break;
                    case "0":
                        continuar = false;
                        break;
                    default:
                        System.out.println("\n❌ Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: " + e.getMessage());
            }
            
            if (continuar && !opcion.equals("0")) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    // ========== OPERACIONES DE EMPRESA ==========
    
    private void crearEmpresa() {
        System.out.println("\n--- Crear Empresa ---");
        
        try {
            System.out.print("Razón Social: ");
            String razonSocial = scanner.nextLine().trim();
            
            System.out.print("CUIT: ");
            String cuit = scanner.nextLine().trim();
            
            System.out.print("Actividad Principal (opcional): ");
            String actividadPrincipal = scanner.nextLine().trim();
            if (actividadPrincipal.isEmpty()) {
                actividadPrincipal = null;
            }
            
            System.out.print("Email (opcional): ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                email = null;
            }
            
            Empresa empresa = new Empresa();
            empresa.setRazonSocial(razonSocial);
            empresa.setCuit(cuit);
            empresa.setActividadPrincipal(actividadPrincipal);
            empresa.setEmail(email);
            
            // Preguntar si desea crear domicilio fiscal
            System.out.print("¿Desea crear un domicilio fiscal? (S/N): ");
            String crearDomicilio = scanner.nextLine().trim().toUpperCase();
            
            if (crearDomicilio.equals("S")) {
                DomicilioFiscal domicilioFiscal = solicitarDatosDomicilioFiscal();
                empresa.setDomicilioFiscal(domicilioFiscal);
            }
            
            Long id = empresaService.insertar(empresa);
            System.out.println("\n✅ Empresa creada exitosamente con ID: " + id);
            
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println("\n❌ Error al crear empresa: " + e.getMessage());
        }
    }
    
    private void buscarEmpresaPorId() {
        System.out.println("\n--- Buscar Empresa por ID ---");
        System.out.print("Ingrese el ID: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Empresa empresa = empresaService.getById(id);
            
            if (empresa != null) {
                System.out.println("\n✅ Empresa encontrada:");
                System.out.println(empresa);
                if (empresa.getDomicilioFiscal() != null) {
                    System.out.println("Domicilio fiscal asociado: " + empresa.getDomicilioFiscal());
                }
            } else {
                System.out.println("\n⚠️ No se encontró una empresa con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: ID inválido. Debe ser un número.");
        } catch (SQLException e) {
            System.out.println("\n❌ Error al buscar empresa: " + e.getMessage());
        }
    }
    
    private void buscarEmpresaPorCuit() {
        System.out.println("\n--- Buscar Empresa por CUIT ---");
        System.out.print("Ingrese el CUIT: ");
        
        try {
            String cuit = scanner.nextLine().trim();
            Empresa empresa = empresaService.buscarPorCuit(cuit);
            
            if (empresa != null) {
                System.out.println("\n✅ Empresa encontrada:");
                System.out.println(empresa);
                if (empresa.getDomicilioFiscal() != null) {
                    System.out.println("Domicilio fiscal asociado: " + empresa.getDomicilioFiscal());
                }
            } else {
                System.out.println("\n⚠️ No se encontró una empresa con CUIT: " + cuit);
            }
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println("\n❌ Error al buscar empresa: " + e.getMessage());
        }
    }
    
    private void buscarEmpresaPorRazonSocial() {
        System.out.println("\n--- Buscar Empresa por Razón Social ---");
        System.out.print("Ingrese la razón social: ");
        
        try {
            String razonSocial = scanner.nextLine().trim();
            Empresa empresa = empresaService.buscarPorRazonSocial(razonSocial);
            
            if (empresa != null) {
                System.out.println("\n✅ Empresa encontrada:");
                System.out.println(empresa);
                if (empresa.getDomicilioFiscal() != null) {
                    System.out.println("Domicilio fiscal asociado: " + empresa.getDomicilioFiscal());
                }
            } else {
                System.out.println("\n⚠️ No se encontró una empresa con razón social: " + razonSocial);
            }
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println("\n❌ Error al buscar empresa: " + e.getMessage());
        }
    }
    
    private void listarEmpresas() {
        System.out.println("\n--- Listar Todas las Empresas ---");
        
        try {
            List<Empresa> empresas = empresaService.getAll();
            
            if (empresas.isEmpty()) {
                System.out.println("\n⚠️ No hay empresas registradas.");
            } else {
                System.out.println("\n✅ Total de empresas: " + empresas.size());
                System.out.println("-".repeat(80));
                for (Empresa empresa : empresas) {
                    System.out.println(empresa);
                    if (empresa.getDomicilioFiscal() != null) {
                        System.out.println("  └─ Domicilio fiscal: " + empresa.getDomicilioFiscal());
                    }
                    System.out.println("-".repeat(80));
                }
            }
        } catch (SQLException e) {
            System.out.println("\n❌ Error al listar empresas: " + e.getMessage());
        }
    }
    
    private void actualizarEmpresa() {
        System.out.println("\n--- Actualizar Empresa ---");
        System.out.print("Ingrese el ID de la empresa a actualizar: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Empresa empresa = empresaService.getById(id);
            
            if (empresa == null) {
                System.out.println("\n⚠️ No se encontró una empresa con ID: " + id);
                return;
            }
            
            System.out.println("\nEmpresa actual:");
            System.out.println(empresa);
            System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");
            
            System.out.print("Razón Social [" + empresa.getRazonSocial() + "]: ");
            String razonSocial = scanner.nextLine().trim();
            if (!razonSocial.isEmpty()) {
                empresa.setRazonSocial(razonSocial);
            }
            
            System.out.print("CUIT [" + empresa.getCuit() + "]: ");
            String cuit = scanner.nextLine().trim();
            if (!cuit.isEmpty()) {
                empresa.setCuit(cuit);
            }
            
            System.out.print("Actividad Principal [" + (empresa.getActividadPrincipal() != null ? empresa.getActividadPrincipal() : "") + "]: ");
            String actividadPrincipal = scanner.nextLine().trim();
            if (!actividadPrincipal.isEmpty()) {
                empresa.setActividadPrincipal(actividadPrincipal);
            }
            
            System.out.print("Email [" + (empresa.getEmail() != null ? empresa.getEmail() : "") + "]: ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                empresa.setEmail(email);
            }
            
            empresaService.actualizar(empresa);
            System.out.println("\n✅ Empresa actualizada exitosamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: ID inválido. Debe ser un número.");
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println("\n❌ Error al actualizar empresa: " + e.getMessage());
        }
    }
    
    private void eliminarEmpresa() {
        System.out.println("\n--- Eliminar Empresa (baja lógica) ---");
        System.out.print("Ingrese el ID de la empresa a eliminar: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Empresa empresa = empresaService.getById(id);
            
            if (empresa == null) {
                System.out.println("\n⚠️ No se encontró una empresa con ID: " + id);
                return;
            }
            
            System.out.println("\n⚠️ ¿Está seguro de eliminar esta empresa?");
            System.out.println(empresa);
            System.out.print("Confirme (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacion.equals("S")) {
                empresaService.eliminar(id);
                System.out.println("\n✅ Empresa eliminada exitosamente (baja lógica).");
            } else {
                System.out.println("\n❌ Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: ID inválido. Debe ser un número.");
        } catch (SQLException e) {
            System.out.println("\n❌ Error al eliminar empresa: " + e.getMessage());
        }
    }
    
    // ========== OPERACIONES DE DOMICILIO FISCAL ==========
    
    private void crearDomicilioFiscal() {
        System.out.println("\n--- Crear Domicilio Fiscal ---");
        
        try {
            DomicilioFiscal domicilioFiscal = solicitarDatosDomicilioFiscal();
            
            Long id = domicilioFiscalService.insertar(domicilioFiscal);
            System.out.println("\n✅ Domicilio fiscal creado exitosamente con ID: " + id);
            System.out.println("⚠️ Nota: Este domicilio fiscal no está asociado a ninguna empresa.");
            System.out.println("   Para asociarlo, debe crear o actualizar una empresa y asignarle este domicilio.");
            
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println("\n❌ Error al crear domicilio fiscal: " + e.getMessage());
        }
    }
    
    private DomicilioFiscal solicitarDatosDomicilioFiscal() {
        System.out.print("Calle: ");
        String calle = scanner.nextLine().trim();
        
        System.out.print("Número: ");
        Integer numero = null;
        try {
            numero = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El número debe ser un entero válido");
        }
        
        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine().trim();
        
        System.out.print("Provincia: ");
        String provincia = scanner.nextLine().trim();
        
        System.out.print("Código Postal (opcional): ");
        String codigoPostal = scanner.nextLine().trim();
        if (codigoPostal.isEmpty()) {
            codigoPostal = null;
        }
        
        System.out.print("País: ");
        String pais = scanner.nextLine().trim();
        
        DomicilioFiscal domicilioFiscal = new DomicilioFiscal();
        domicilioFiscal.setCalle(calle);
        domicilioFiscal.setNumero(numero);
        domicilioFiscal.setCiudad(ciudad);
        domicilioFiscal.setProvincia(provincia);
        domicilioFiscal.setCodigoPostal(codigoPostal);
        domicilioFiscal.setPais(pais);
        
        return domicilioFiscal;
    }
    
    private void buscarDomicilioFiscalPorId() {
        System.out.println("\n--- Buscar Domicilio Fiscal por ID ---");
        System.out.print("Ingrese el ID: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            DomicilioFiscal domicilioFiscal = domicilioFiscalService.getById(id);
            
            if (domicilioFiscal != null) {
                System.out.println("\n✅ Domicilio fiscal encontrado:");
                System.out.println(domicilioFiscal);
            } else {
                System.out.println("\n⚠️ No se encontró un domicilio fiscal con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: ID inválido. Debe ser un número.");
        } catch (SQLException e) {
            System.out.println("\n❌ Error al buscar domicilio fiscal: " + e.getMessage());
        }
    }
    
    private void listarDomiciliosFiscales() {
        System.out.println("\n--- Listar Todos los Domicilios Fiscales ---");
        
        try {
            List<DomicilioFiscal> domicilios = domicilioFiscalService.getAll();
            
            if (domicilios.isEmpty()) {
                System.out.println("\n⚠️ No hay domicilios fiscales registrados.");
            } else {
                System.out.println("\n✅ Total de domicilios fiscales: " + domicilios.size());
                System.out.println("-".repeat(80));
                for (DomicilioFiscal domicilio : domicilios) {
                    System.out.println(domicilio);
                    System.out.println("-".repeat(80));
                }
            }
        } catch (SQLException e) {
            System.out.println("\n❌ Error al listar domicilios fiscales: " + e.getMessage());
        }
    }
    
    private void actualizarDomicilioFiscal() {
        System.out.println("\n--- Actualizar Domicilio Fiscal ---");
        System.out.print("Ingrese el ID del domicilio fiscal a actualizar: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            DomicilioFiscal domicilioFiscal = domicilioFiscalService.getById(id);
            
            if (domicilioFiscal == null) {
                System.out.println("\n⚠️ No se encontró un domicilio fiscal con ID: " + id);
                return;
            }
            
            System.out.println("\nDomicilio fiscal actual:");
            System.out.println(domicilioFiscal);
            System.out.println("\nIngrese los nuevos valores (presione Enter para mantener el valor actual):");
            
            System.out.print("Calle [" + domicilioFiscal.getCalle() + "]: ");
            String calle = scanner.nextLine().trim();
            if (!calle.isEmpty()) {
                domicilioFiscal.setCalle(calle);
            }
            
            System.out.print("Número [" + domicilioFiscal.getNumero() + "]: ");
            String numeroStr = scanner.nextLine().trim();
            if (!numeroStr.isEmpty()) {
                try {
                    domicilioFiscal.setNumero(Integer.parseInt(numeroStr));
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Número inválido, se mantendrá el actual");
                }
            }
            
            System.out.print("Ciudad [" + domicilioFiscal.getCiudad() + "]: ");
            String ciudad = scanner.nextLine().trim();
            if (!ciudad.isEmpty()) {
                domicilioFiscal.setCiudad(ciudad);
            }
            
            System.out.print("Provincia [" + domicilioFiscal.getProvincia() + "]: ");
            String provincia = scanner.nextLine().trim();
            if (!provincia.isEmpty()) {
                domicilioFiscal.setProvincia(provincia);
            }
            
            System.out.print("Código Postal [" + (domicilioFiscal.getCodigoPostal() != null ? domicilioFiscal.getCodigoPostal() : "") + "]: ");
            String codigoPostal = scanner.nextLine().trim();
            if (!codigoPostal.isEmpty()) {
                domicilioFiscal.setCodigoPostal(codigoPostal);
            }
            
            System.out.print("País [" + domicilioFiscal.getPais() + "]: ");
            String pais = scanner.nextLine().trim();
            if (!pais.isEmpty()) {
                domicilioFiscal.setPais(pais);
            }
            
            domicilioFiscalService.actualizar(domicilioFiscal);
            System.out.println("\n✅ Domicilio fiscal actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: ID inválido. Debe ser un número.");
        } catch (IllegalArgumentException | SQLException e) {
            System.out.println("\n❌ Error al actualizar domicilio fiscal: " + e.getMessage());
        }
    }
    
    private void eliminarDomicilioFiscal() {
        System.out.println("\n--- Eliminar Domicilio Fiscal (baja lógica) ---");
        System.out.print("Ingrese el ID del domicilio fiscal a eliminar: ");
        
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            DomicilioFiscal domicilioFiscal = domicilioFiscalService.getById(id);
            
            if (domicilioFiscal == null) {
                System.out.println("\n⚠️ No se encontró un domicilio fiscal con ID: " + id);
                return;
            }
            
            System.out.println("\n⚠️ ¿Está seguro de eliminar este domicilio fiscal?");
            System.out.println(domicilioFiscal);
            System.out.print("Confirme (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();
            
            if (confirmacion.equals("S")) {
                domicilioFiscalService.eliminar(id);
                System.out.println("\n✅ Domicilio fiscal eliminado exitosamente (baja lógica).");
            } else {
                System.out.println("\n❌ Operación cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: ID inválido. Debe ser un número.");
        } catch (SQLException e) {
            System.out.println("\n❌ Error al eliminar domicilio fiscal: " + e.getMessage());
        }
    }
}
