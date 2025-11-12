# TFI - Trabajo Final Integrador
## Empresa → DomicilioFiscal (1→1 unidireccional)

### Descripción del Dominio

Este proyecto implementa una aplicación Java que modela la relación **1→1 unidireccional** entre las entidades **Empresa** y **DomicilioFiscal**.

- **Empresa (A)**: Representa una empresa con información básica (razón social, CUIT, actividad principal, email).
- **DomicilioFiscal (B)**: Almacena el domicilio fiscal de la empresa (calle, número, ciudad, provincia, código postal, país).

La relación es **unidireccional**: solo la clase `Empresa` contiene una referencia a `DomicilioFiscal`, y está garantizada por una **clave foránea única** en la tabla `domicilio_fiscal` que referencia a `empresa(id)`.

### Requisitos

- **Java**: JDK 21 (recomendado) o superior
- **MySQL**: 8.0 o superior
- **Maven**: 3.6+ (para gestión de dependencias)

### Estructura del Proyecto

```
tpi-programacion2/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── config/          # Configuración de conexión a BD
│   │   │   ├── entities/         # Entidades de dominio (Empresa, DomicilioFiscal)
│   │   │   ├── dao/              # Interfaces genéricas y DAOs concretos
│   │   │   ├── service/          # Capa de servicios con transacciones
│   │   │   └── main/             # Clase Main y AppMenu
│   │   └── resources/
│   │       └── db.properties     # Configuración de base de datos
├── sql/
│   └── 01_create_database.sql    # Script de creación de BD y tablas
└── README.md
```

### Configuración de la Base de Datos

#### 1. Crear la base de datos y tablas

**⚠️ IMPORTANTE:** Este paso se ejecuta **UNA SOLA VEZ** para crear la estructura de la base de datos. La aplicación Java se conectará automáticamente después.

**Opción A: Desde MySQL Workbench (Recomendado)**

1. Abre MySQL Workbench.
2. Conéctate a tu servidor MySQL (localhost:3309 según tu configuración).
3. Ve a **File → Open SQL Script...**
4. Selecciona el archivo `sql/01_create_database.sql`.
5. Haz clic en el botón de ejecutar (⚡) o presiona `F5`.
6. Verifica que aparezcan mensajes de éxito: `Query OK, X rows affected`.

**Opción B: Desde MySQL Command Line Client**

1. Abre MySQL Command Line Client o ejecuta:
   ```bash
   mysql -u root -p
   ```
2. Ingresa tu contraseña cuando se solicite.
3. Una vez dentro de MySQL (verás el prompt `mysql>`), ejecuta:
   ```sql
   SOURCE C:/Users/Brian/Desktop/tpi-programacion2/sql/01_create_database.sql;
   ```
   O si estás en otra ubicación, usa la ruta completa a tu archivo:
   ```sql
   SOURCE C:\ruta\completa\a\tu\proyecto\sql\01_create_database.sql;
   ```

**Opción C: Desde PowerShell/CMD (si MySQL está en el PATH)**

Si MySQL está configurado en tu PATH, puedes usar:

**En CMD:**
```bash
mysql -u root -p < sql/01_create_database.sql
```

**En PowerShell:**
```powershell
Get-Content sql/01_create_database.sql | mysql -u root -p
```

O usando cmd:
```powershell
cmd /c "mysql -u root -p < sql/01_create_database.sql"
```

**Verificar que se creó correctamente:**

Después de ejecutar el script, puedes verificar en MySQL:

```sql
USE tpi_empresas;
SHOW TABLES;
DESCRIBE empresa;
DESCRIBE domicilio_fiscal;
```

Deberías ver:
- Base de datos: `tpi_empresas`
- Tabla: `empresa` (con campos: id, eliminado, razon_social, cuit, actividad_principal, email)
- Tabla: `domicilio_fiscal` (con campos: id, eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id)
- Relación 1→1: FK única `empresa_id` en `domicilio_fiscal` hacia `empresa(id)`

#### 2. Configurar credenciales de acceso

**⚠️ IMPORTANTE:** La aplicación Java se conecta automáticamente a MySQL usando las credenciales del archivo `db.properties`. No necesitas conectarte manualmente cada vez que ejecutas la aplicación.

Edite el archivo `src/main/resources/db.properties` con sus credenciales de MySQL:

```properties
db.url=jdbc:mysql://localhost:3309/tpi_empresas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.user=root
db.password=su_password_aqui
```

**Nota sobre la conexión:**
- El archivo `DatabaseConnection.java` lee automáticamente estas credenciales.
- La aplicación se conecta sola cuando la ejecutas.
- Solo necesitas ejecutar el script SQL una vez para crear la estructura.
- Asegúrate de que MySQL esté corriendo antes de ejecutar la aplicación Java.

### Compilación y Ejecución

#### Opción 1: Con Maven

1. **Compilar el proyecto**:
   ```bash
   mvn clean compile
   ```

2. **Copiar dependencias** (solo la primera vez o si cambias dependencias):
   ```bash
   mvn dependency:copy-dependencies -DoutputDirectory=target/lib
   ```

3. **Ejecutar la aplicación**:
   ```bash
   java -cp "target/classes;target/lib/*" main.Main
   ```
   
   **Nota:** `mvn exec:java` puede tener problemas con entrada interactiva en algunos terminales. Usar `java` directamente es más confiable.

#### Opción 2: Con IDE (IntelliJ IDEA, Eclipse, VS Code)

1. Importe el proyecto como proyecto Maven.
2. Configure el classpath para incluir las dependencias (Maven descargará automáticamente el driver de MySQL).
3. Ejecute la clase `main.Main`.

#### Opción 3: Compilación manual

1. **Descargar dependencias**:
   - MySQL Connector/J: [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)
   - Agregar el JAR al classpath

2. **Compilar**:
   ```bash
   javac -cp ".:mysql-connector-java-8.0.33.jar" -d target/classes src/main/java/**/*.java
   ```

3. **Ejecutar**:
   ```bash
   java -cp "target/classes:mysql-connector-java-8.0.33.jar" main.Main
   ```

### Flujo de Uso

Al ejecutar la aplicación, se mostrará un menú de consola con las siguientes opciones:
### Solo con maven
1. **Compilar el proyecto**:
   mvn exec:java
#### Menú Principal
- **1. Gestionar Empresas**: CRUD completo de empresas
- **2. Gestionar Domicilios Fiscales**: CRUD completo de domicilios fiscales
- **0. Salir**

#### Menú de Empresas
- **1. Crear Empresa**: Crea una nueva empresa (opcionalmente con domicilio fiscal)
- **2. Buscar Empresa por ID**: Busca una empresa por su ID
- **3. Buscar Empresa por CUIT**: Búsqueda por CUIT
- **4. Buscar Empresa por Razón Social**: Búsqueda por razón social
- **5. Listar Todas las Empresas**: Muestra todas las empresas no eliminadas
- **6. Actualizar Empresa**: Modifica una empresa existente
- **7. Eliminar Empresa**: Baja lógica de la empresa (y su domicilio fiscal asociado)

#### Menú de Domicilios Fiscales
- **1. Crear Domicilio Fiscal**: Crea un domicilio fiscal independiente
- **2. Buscar Domicilio Fiscal por ID**: Busca un domicilio fiscal por su ID
- **3. Listar Todos los Domicilios Fiscales**: Muestra todos los domicilios fiscales no eliminados
- **4. Actualizar Domicilio Fiscal**: Modifica un domicilio fiscal existente
- **5. Eliminar Domicilio Fiscal**: Baja lógica del domicilio fiscal

### Características Implementadas

✅ **Relación 1→1 unidireccional**: Solo `Empresa` referencia a `DomicilioFiscal`  
✅ **Patrón DAO**: Interfaces genéricas y DAOs concretos con `PreparedStatement`  
✅ **Capa Service**: Validaciones, reglas de negocio y transacciones (commit/rollback)  
✅ **Baja lógica**: Campo `eliminado` en ambas entidades  
✅ **Transacciones**: Operaciones compuestas con manejo de errores y rollback  
✅ **Validaciones**: Campos obligatorios, formatos, unicidad (CUIT de la empresa)  
✅ **Menú de consola**: CRUD completo con manejo robusto de errores  
✅ **Búsquedas**: Por ID, CUIT y razón social (empresa)  
✅ **Integridad referencial**: FK única garantiza relación 1→1 en BD  

### Validaciones Implementadas

- **Empresa**:
  - Razón social: obligatoria, máximo 120 caracteres
  - CUIT: obligatorio, máximo 13 caracteres, único
  - Actividad principal: opcional, máximo 80 caracteres
  - Email: opcional, máximo 120 caracteres, validación básica de formato

- **DomicilioFiscal**:
  - Calle: obligatoria, máximo 100 caracteres
  - Número: obligatorio, tipo Integer
  - Ciudad: obligatoria, máximo 80 caracteres
  - Provincia: obligatoria, máximo 80 caracteres
  - Código postal: opcional, máximo 10 caracteres
  - País: obligatorio, máximo 80 caracteres

- **Regla 1→1**: Una empresa solo puede tener un domicilio fiscal asociado (garantizado por FK única)

### Operaciones Transaccionales

Las operaciones que involucran múltiples tablas (crear empresa con domicilio fiscal, eliminar empresa y su domicilio fiscal) se ejecutan dentro de transacciones:

- `setAutoCommit(false)`: Inicia transacción
- `commit()`: Confirma cambios si todo es exitoso
- `rollback()`: Revierte cambios ante cualquier error
- `setAutoCommit(true)`: Restaura auto-commit y cierra conexión

### Ejemplo de Uso

```
=== MENU PRINCIPAL ===
1. Gestionar Empresas
2. Gestionar Domicilios Fiscales
0. Salir
Seleccione una opción: 1

=== GESTION DE EMPRESAS ===
1. Crear Empresa
2. Buscar Empresa por ID
...
Seleccione una opcion: 1

--- Crear Empresa ---
Razón Social: Tech Solutions S.A.
CUIT: 30-12345678-9
Actividad Principal (opcional): Desarrollo de Software
Email (opcional): contacto@techsolutions.com
¿Desea crear un domicilio fiscal? (S/N): S
Calle: Av. Corrientes
Número: 1234
Ciudad: Ciudad Autónoma de Buenos Aires
Provincia: Buenos Aires
Código Postal (opcional): C1043AAX
País: Argentina

✅ Empresa creada exitosamente con ID: 1
```

### Manejo de Errores

La aplicación maneja robustamente:
- Entradas inválidas (parseos numéricos, formatos de email)
- IDs inexistentes
- Errores de base de datos (violaciones de unicidad, FK, etc.)
- Validaciones de negocio (campos obligatorios, formatos, longitudes máximas)

Todos los errores se muestran con mensajes claros al usuario.

### Arquitectura

- **config/**: `DatabaseConnection` - Gestión de conexión a BD desde propiedades
- **entities/**: `Empresa`, `DomicilioFiscal` - Entidades de dominio
- **dao/**: `GenericDao<T>`, `EmpresaDao`, `DomicilioFiscalDao` - Acceso a datos con JDBC
- **service/**: `GenericService<T>`, `EmpresaService`, `DomicilioFiscalService` - Lógica de negocio y transacciones
- **main/**: `Main`, `AppMenu` - Punto de entrada y menú de consola

### Notas Importantes

- **Ejecución del script SQL:** Solo necesitas ejecutar `01_create_database.sql` **UNA VEZ** para crear la estructura de la base de datos. La aplicación Java se conecta automáticamente después.
- **Conexión automática:** La aplicación se conecta a MySQL automáticamente usando las credenciales en `db.properties`. No necesitas conectarte manualmente cada vez.
- **MySQL debe estar corriendo:** Asegúrate de que el servidor MySQL esté activo antes de ejecutar la aplicación Java.
- La relación 1→1 se garantiza mediante una **clave foránea única** (`empresa_id UNIQUE`) en la tabla `domicilio_fiscal`.
- La eliminación de una empresa elimina en cascada su domicilio fiscal asociado (`ON DELETE CASCADE`).
- Todas las eliminaciones son **bajas lógicas** (campo `eliminado = TRUE`).
- Las búsquedas y listados excluyen automáticamente los registros eliminados.
- El CUIT de la empresa es único en toda la base de datos.

### Video

[Enlace al video de presentación - agregar aquí]

### Integrantes

- **Integrante 1**: Brian Gamarra
- **Integrante 2**: [Nombre]
- **Integrante 3**: [Nombre]
- **Integrante 4**: [Nombre]

### Licencia

Este proyecto es parte de un Trabajo Final Integrador académico.
