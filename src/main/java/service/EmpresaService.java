package service;

import config.DatabaseConnection;
import dao.DomicilioFiscalDao;
import dao.EmpresaDao;
import entities.DomicilioFiscal;
import entities.Empresa;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la entidad Empresa.
 * Implementa reglas de negocio, validaciones y transacciones.
 * Gestiona la relación 1→1 con DomicilioFiscal.
 */
public class EmpresaService implements GenericService<Empresa> {
    
    private final EmpresaDao empresaDao;
    private final DomicilioFiscalDao domicilioFiscalDao;
    
    public EmpresaService() {
        this.empresaDao = new EmpresaDao();
        this.domicilioFiscalDao = new DomicilioFiscalDao();
    }
    
    @Override
    public Long insertar(Empresa entity) throws SQLException, IllegalArgumentException {
        validar(entity);
        
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            
            // Verificar unicidad del CUIT
            Empresa empresaExistente = empresaDao.buscarPorCuit(entity.getCuit(), connection);
            if (empresaExistente != null) {
                throw new IllegalArgumentException("El CUIT '" + entity.getCuit() + "' ya existe");
            }
            
            // Crear empresa
            Long empresaId = empresaDao.crear(entity, connection);
            
            // Si tiene domicilio fiscal asociado, crearlo y establecer la relación 1→1
            if (entity.getDomicilioFiscal() != null) {
                // Verificar que no exista ya un domicilio fiscal para esta empresa (regla 1→1)
                DomicilioFiscal domicilioExistente = domicilioFiscalDao.leerPorEmpresaId(empresaId, connection);
                if (domicilioExistente != null) {
                    throw new SQLException("La empresa ya tiene un domicilio fiscal asociado (violación 1→1)");
                }
                
                // Crear domicilio fiscal
                Long domicilioId = domicilioFiscalDao.crear(entity.getDomicilioFiscal(), connection);
                
                // Establecer relación 1→1
                domicilioFiscalDao.establecerRelacionEmpresa(domicilioId, empresaId, connection);
                entity.getDomicilioFiscal().setId(domicilioId);
            }
            
            connection.commit();
            return empresaId;
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
    public void actualizar(Empresa entity) throws SQLException, IllegalArgumentException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("El ID de la empresa es obligatorio para actualizar");
        }
        
        validar(entity);
        
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            
            // Verificar que existe
            Empresa existente = empresaDao.leer(entity.getId(), connection);
            if (existente == null) {
                throw new SQLException("No se encontró la empresa con ID: " + entity.getId());
            }
            
            // Verificar unicidad del CUIT si cambió
            if (!existente.getCuit().equals(entity.getCuit())) {
                Empresa otraEmpresa = empresaDao.buscarPorCuit(entity.getCuit(), connection);
                if (otraEmpresa != null && !otraEmpresa.getId().equals(entity.getId())) {
                    throw new IllegalArgumentException("El CUIT '" + entity.getCuit() + "' ya existe");
                }
            }
            
            // Actualizar empresa
            empresaDao.actualizar(entity, connection);
            
            // Si se actualizó el domicilio fiscal asociado
            if (entity.getDomicilioFiscal() != null && entity.getDomicilioFiscal().getId() != null) {
                domicilioFiscalDao.actualizar(entity.getDomicilioFiscal(), connection);
            }
            
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
            Empresa existente = empresaDao.leer(id, connection);
            if (existente == null) {
                throw new SQLException("No se encontró la empresa con ID: " + id);
            }
            
            // Eliminar empresa (baja lógica)
            empresaDao.eliminar(id, connection);
            
            // Si tiene domicilio fiscal asociado, eliminarlo también (baja lógica)
            if (existente.getDomicilioFiscal() != null) {
                domicilioFiscalDao.eliminar(existente.getDomicilioFiscal().getId(), connection);
            }
            
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
    public Empresa getById(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio");
        }
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            return empresaDao.leer(id, connection);
        }
    }
    
    @Override
    public List<Empresa> getAll() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return empresaDao.leerTodos(connection);
        }
    }
    
    /**
     * Busca una empresa por CUIT.
     * 
     * @param cuit CUIT a buscar
     * @return Empresa encontrada o null
     * @throws SQLException si ocurre un error
     */
    public Empresa buscarPorCuit(String cuit) throws SQLException {
        if (cuit == null || cuit.trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT no puede estar vacío");
        }
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            return empresaDao.buscarPorCuit(cuit.trim(), connection);
        }
    }
    
    /**
     * Busca una empresa por razón social.
     * 
     * @param razonSocial Razón social a buscar
     * @return Empresa encontrada o null
     * @throws SQLException si ocurre un error
     */
    public Empresa buscarPorRazonSocial(String razonSocial) throws SQLException {
        if (razonSocial == null || razonSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social no puede estar vacía");
        }
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            return empresaDao.buscarPorRazonSocial(razonSocial.trim(), connection);
        }
    }
    
    /**
     * Valida los campos obligatorios y formatos de Empresa.
     * 
     * @param entity Entidad a validar
     * @throws IllegalArgumentException si la entidad no es válida
     */
    private void validar(Empresa entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("La empresa no puede ser nula");
        }
        
        if (entity.getRazonSocial() == null || entity.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social es obligatoria");
        }
        
        if (entity.getRazonSocial().length() > 120) {
            throw new IllegalArgumentException("La razón social no puede exceder 120 caracteres");
        }
        
        if (entity.getCuit() == null || entity.getCuit().trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT es obligatorio");
        }
        
        if (entity.getCuit().length() > 13) {
            throw new IllegalArgumentException("El CUIT no puede exceder 13 caracteres");
        }
        
        if (entity.getActividadPrincipal() != null && entity.getActividadPrincipal().length() > 80) {
            throw new IllegalArgumentException("La actividad principal no puede exceder 80 caracteres");
        }
        
        if (entity.getEmail() != null && entity.getEmail().length() > 120) {
            throw new IllegalArgumentException("El email no puede exceder 120 caracteres");
        }
        
        // Validación básica de formato de email
        if (entity.getEmail() != null && !entity.getEmail().trim().isEmpty()) {
            if (!entity.getEmail().contains("@")) {
                throw new IllegalArgumentException("El formato del email no es válido");
            }
        }
    }
}

