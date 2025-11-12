package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos MySQL.
 * Lee la configuración desde un archivo de propiedades.
 */
public class DatabaseConnection {
    
    private static final String PROPERTIES_FILE = "/db.properties";
    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USER_KEY = "db.user";
    private static final String DB_PASSWORD_KEY = "db.password";
    
    /**
     * Obtiene una conexión a la base de datos.
     * 
     * @return Connection a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        Properties props = loadProperties();
        
        String url = props.getProperty(DB_URL_KEY);
        String user = props.getProperty(DB_USER_KEY);
        String password = props.getProperty(DB_PASSWORD_KEY);
        
        if (url == null || user == null || password == null) {
            throw new SQLException("Faltan propiedades de configuración en db.properties");
        }
        
        return DriverManager.getConnection(url, user, password);
    }
    
    /**
     * Carga las propiedades desde el archivo db.properties.
     * 
     * @return Properties con la configuración
     * @throws SQLException si no se puede cargar el archivo
     */
    private static Properties loadProperties() throws SQLException {
        Properties props = new Properties();
        
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new SQLException("No se encontró el archivo " + PROPERTIES_FILE);
            }
            props.load(input);
        } catch (Exception e) {
            throw new SQLException("Error al cargar propiedades: " + e.getMessage(), e);
        }
        
        return props;
    }
}

