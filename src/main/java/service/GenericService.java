package service;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica para servicios de negocio.
 * Define operaciones CRUD con manejo de transacciones.
 * 
 * @param <T> Tipo de entidad
 */
public interface GenericService<T> {
    
    /**
     * Inserta una nueva entidad con validaciones y transacciones.
     * 
     * @param entity Entidad a insertar
     * @return ID de la entidad insertada
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IllegalArgumentException si la entidad no es válida
     */
    Long insertar(T entity) throws SQLException, IllegalArgumentException;
    
    /**
     * Actualiza una entidad existente con validaciones y transacciones.
     * 
     * @param entity Entidad a actualizar
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IllegalArgumentException si la entidad no es válida
     */
    void actualizar(T entity) throws SQLException, IllegalArgumentException;
    
    /**
     * Elimina lógicamente una entidad.
     * 
     * @param id ID de la entidad a eliminar
     * @throws SQLException si ocurre un error en la base de datos
     */
    void eliminar(Long id) throws SQLException;
    
    /**
     * Obtiene una entidad por su ID.
     * 
     * @param id ID de la entidad
     * @return Entidad encontrada o null
     * @throws SQLException si ocurre un error en la base de datos
     */
    T getById(Long id) throws SQLException;
    
    /**
     * Obtiene todas las entidades no eliminadas.
     * 
     * @return Lista de entidades
     * @throws SQLException si ocurre un error en la base de datos
     */
    List<T> getAll() throws SQLException;
}

