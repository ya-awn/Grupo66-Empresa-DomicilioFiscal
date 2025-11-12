package dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz genérica para operaciones DAO (Data Access Object).
 * Define las operaciones CRUD básicas.
 * 
 * @param <T> Tipo de entidad
 */
public interface GenericDao<T> {
    
    /**
     * Crea una nueva entidad en la base de datos.
     * 
     * @param entity Entidad a crear
     * @param connection Conexión a la base de datos
     * @return ID de la entidad creada
     * @throws SQLException si ocurre un error en la base de datos
     */
    Long crear(T entity, Connection connection) throws SQLException;
    
    /**
     * Lee una entidad por su ID.
     * 
     * @param id ID de la entidad
     * @param connection Conexión a la base de datos
     * @return Entidad encontrada o null si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    T leer(Long id, Connection connection) throws SQLException;
    
    /**
     * Lee todas las entidades no eliminadas.
     * 
     * @param connection Conexión a la base de datos
     * @return Lista de entidades
     * @throws SQLException si ocurre un error en la base de datos
     */
    java.util.List<T> leerTodos(Connection connection) throws SQLException;
    
    /**
     * Actualiza una entidad existente.
     * 
     * @param entity Entidad a actualizar
     * @param connection Conexión a la base de datos
     * @throws SQLException si ocurre un error en la base de datos
     */
    void actualizar(T entity, Connection connection) throws SQLException;
    
    /**
     * Elimina lógicamente una entidad (baja lógica).
     * 
     * @param id ID de la entidad a eliminar
     * @param connection Conexión a la base de datos
     * @throws SQLException si ocurre un error en la base de datos
     */
    void eliminar(Long id, Connection connection) throws SQLException;
}

