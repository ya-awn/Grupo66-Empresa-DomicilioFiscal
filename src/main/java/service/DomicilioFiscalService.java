package service;

import config.DatabaseConnection;
import dao.DomicilioFiscalDao;
import entities.DomicilioFiscal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la entidad DomicilioFiscal.
 * Implementa reglas de negocio y validaciones con transacciones.
 */
public class DomicilioFiscalService implements GenericService<DomicilioFiscal> {
    
    private final DomicilioFiscalDao domicilioFiscalDao;
    
    public DomicilioFiscalService() {
        this.domicilioFiscalDao = new DomicilioFiscalDao();
    }
    
    @Override
    public Long insertar(DomicilioFiscal entity) throws SQLException, IllegalArgumentException {
        validar(entity);
        
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            
            Long id = domicilioFiscalDao.crear(entity, connection);
            
            connection.commit();
            return id;
        } catch (SQLException | IllegalArgumentException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
    
    @Override
    public void actualizar(DomicilioFiscal entity) throws SQLException, IllegalArgumentException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("El ID del domicilio fiscal es obligatorio para actualizar");
        }
        
        validar(entity);
        
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            
            // Verificar que existe
            DomicilioFiscal existente = domicilioFiscalDao.leer(entity.getId(), connection);
            if (existente == null) {
                throw new SQLException("No se encontró el domicilio fiscal con ID: " + entity.getId());
            }
            
            domicilioFiscalDao.actualizar(entity, connection);
            
            connection.commit();
        } catch (SQLException | IllegalArgumentException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
    
    @Override
    public void eliminar(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio para eliminar");
        }
        
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            
            // Verificar que existe
            DomicilioFiscal existente = domicilioFiscalDao.leer(id, connection);
            if (existente == null) {
                throw new SQLException("No se encontró el domicilio fiscal con ID: " + id);
            }
            
            domicilioFiscalDao.eliminar(id, connection);
            
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
    
    @Override
    public DomicilioFiscal getById(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            return domicilioFiscalDao.leer(id, connection);
        }
    }
    
    @Override
    public List<DomicilioFiscal> getAll() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return domicilioFiscalDao.leerTodos(connection);
        }
    }
    
    /**
     * Valida los campos obligatorios de DomicilioFiscal.
     * 
     * @param entity Entidad a validar
     * @throws IllegalArgumentException si la entidad no es válida
     */
    private void validar(DomicilioFiscal entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("El domicilio fiscal no puede ser nulo");
        }
        
        if (entity.getCalle() == null || entity.getCalle().trim().isEmpty()) {
            throw new IllegalArgumentException("La calle es obligatoria");
        }
        
        if (entity.getCalle().length() > 100) {
            throw new IllegalArgumentException("La calle no puede exceder 100 caracteres");
        }
        
        if (entity.getNumero() == null) {
            throw new IllegalArgumentException("El número es obligatorio");
        }
        
        if (entity.getCiudad() == null || entity.getCiudad().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria");
        }
        
        if (entity.getCiudad().length() > 80) {
            throw new IllegalArgumentException("La ciudad no puede exceder 80 caracteres");
        }
        
        if (entity.getProvincia() == null || entity.getProvincia().trim().isEmpty()) {
            throw new IllegalArgumentException("La provincia es obligatoria");
        }
        
        if (entity.getProvincia().length() > 80) {
            throw new IllegalArgumentException("La provincia no puede exceder 80 caracteres");
        }
        
        if (entity.getCodigoPostal() != null && entity.getCodigoPostal().length() > 10) {
            throw new IllegalArgumentException("El código postal no puede exceder 10 caracteres");
        }
        
        if (entity.getPais() == null || entity.getPais().trim().isEmpty()) {
            throw new IllegalArgumentException("El país es obligatorio");
        }
        
        if (entity.getPais().length() > 80) {
            throw new IllegalArgumentException("El país no puede exceder 80 caracteres");
        }
    }
}

