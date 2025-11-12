package dao;

import entities.DomicilioFiscal;
import entities.Empresa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto para la entidad Empresa.
 * Implementa operaciones CRUD usando PreparedStatement.
 */
public class EmpresaDao implements GenericDao<Empresa> {
    
    private final DomicilioFiscalDao domicilioFiscalDao;
    
    public EmpresaDao() {
        this.domicilioFiscalDao = new DomicilioFiscalDao();
    }
    
    private static final String INSERT = 
        "INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email) " +
        "VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT id, eliminado, razon_social, cuit, actividad_principal, email " +
        "FROM empresa WHERE id = ? AND eliminado = false";
    
    private static final String SELECT_ALL = 
        "SELECT id, eliminado, razon_social, cuit, actividad_principal, email " +
        "FROM empresa WHERE eliminado = false";
    
    private static final String UPDATE = 
        "UPDATE empresa SET razon_social = ?, cuit = ?, actividad_principal = ?, email = ? " +
        "WHERE id = ? AND eliminado = false";
    
    private static final String DELETE = 
        "UPDATE empresa SET eliminado = true WHERE id = ?";
    
    private static final String SELECT_BY_CUIT = 
        "SELECT id, eliminado, razon_social, cuit, actividad_principal, email " +
        "FROM empresa WHERE cuit = ? AND eliminado = false";
    
    private static final String SELECT_BY_RAZON_SOCIAL = 
        "SELECT id, eliminado, razon_social, cuit, actividad_principal, email " +
        "FROM empresa WHERE razon_social = ? AND eliminado = false";
    
    @Override
    public Long crear(Empresa entity, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, entity.getEliminado() != null ? entity.getEliminado() : false);
            ps.setString(2, entity.getRazonSocial());
            ps.setString(3, entity.getCuit());
            ps.setString(4, entity.getActividadPrincipal());
            ps.setString(5, entity.getEmail());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID generado para Empresa");
    }
    
    @Override
    public Empresa leer(Long id, Connection connection) throws SQLException {
        Empresa empresa = null;
        
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empresa = mapResultSetToEntity(rs);
                    
                    // Cargar el domicilio fiscal asociado (relación 1→1)
                    DomicilioFiscal domicilioFiscal = domicilioFiscalDao.leerPorEmpresaId(id, connection);
                    empresa.setDomicilioFiscal(domicilioFiscal);
                }
            }
        }
        
        return empresa;
    }
    
    @Override
    public List<Empresa> leerTodos(Connection connection) throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Empresa empresa = mapResultSetToEntity(rs);
                
                // Cargar el domicilio fiscal asociado para cada empresa
                DomicilioFiscal domicilioFiscal = domicilioFiscalDao.leerPorEmpresaId(empresa.getId(), connection);
                empresa.setDomicilioFiscal(domicilioFiscal);
                
                empresas.add(empresa);
            }
        }
        
        return empresas;
    }
    
    @Override
    public void actualizar(Empresa entity, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getRazonSocial());
            ps.setString(2, entity.getCuit());
            ps.setString(3, entity.getActividadPrincipal());
            ps.setString(4, entity.getEmail());
            ps.setLong(5, entity.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró la empresa con ID: " + entity.getId());
            }
        }
    }
    
    @Override
    public void eliminar(Long id, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    
    /**
     * Busca una empresa por CUIT.
     * 
     * @param cuit CUIT a buscar
     * @param connection Conexión a la base de datos
     * @return Empresa encontrada o null
     * @throws SQLException si ocurre un error
     */
    public Empresa buscarPorCuit(String cuit, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_CUIT)) {
            ps.setString(1, cuit);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empresa empresa = mapResultSetToEntity(rs);
                    DomicilioFiscal domicilioFiscal = domicilioFiscalDao.leerPorEmpresaId(empresa.getId(), connection);
                    empresa.setDomicilioFiscal(domicilioFiscal);
                    return empresa;
                }
            }
        }
        return null;
    }
    
    /**
     * Busca una empresa por razón social.
     * 
     * @param razonSocial Razón social a buscar
     * @param connection Conexión a la base de datos
     * @return Empresa encontrada o null
     * @throws SQLException si ocurre un error
     */
    public Empresa buscarPorRazonSocial(String razonSocial, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_RAZON_SOCIAL)) {
            ps.setString(1, razonSocial);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empresa empresa = mapResultSetToEntity(rs);
                    DomicilioFiscal domicilioFiscal = domicilioFiscalDao.leerPorEmpresaId(empresa.getId(), connection);
                    empresa.setDomicilioFiscal(domicilioFiscal);
                    return empresa;
                }
            }
        }
        return null;
    }
    
    /**
     * Mapea un ResultSet a una entidad Empresa.
     */
    private Empresa mapResultSetToEntity(ResultSet rs) throws SQLException {
        Empresa empresa = new Empresa();
        empresa.setId(rs.getLong("id"));
        empresa.setEliminado(rs.getBoolean("eliminado"));
        empresa.setRazonSocial(rs.getString("razon_social"));
        empresa.setCuit(rs.getString("cuit"));
        empresa.setActividadPrincipal(rs.getString("actividad_principal"));
        empresa.setEmail(rs.getString("email"));
        
        return empresa;
    }
}
