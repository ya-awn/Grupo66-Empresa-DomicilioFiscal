package dao;

import entities.DomicilioFiscal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO concreto para la entidad DomicilioFiscal.
 * Implementa operaciones CRUD usando PreparedStatement.
 */
public class DomicilioFiscalDao implements GenericDao<DomicilioFiscal> {
    
    private static final String INSERT = 
        "INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT id, eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id " +
        "FROM domicilio_fiscal WHERE id = ? AND eliminado = false";
    
    private static final String SELECT_ALL = 
        "SELECT id, eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id " +
        "FROM domicilio_fiscal WHERE eliminado = false";
    
    private static final String UPDATE = 
        "UPDATE domicilio_fiscal SET calle = ?, numero = ?, ciudad = ?, provincia = ?, codigo_postal = ?, pais = ? " +
        "WHERE id = ? AND eliminado = false";
    
    private static final String DELETE = 
        "UPDATE domicilio_fiscal SET eliminado = true WHERE id = ?";
    
    private static final String SELECT_BY_EMPRESA_ID = 
        "SELECT id, eliminado, calle, numero, ciudad, provincia, codigo_postal, pais, empresa_id " +
        "FROM domicilio_fiscal WHERE empresa_id = ? AND eliminado = false";
    
    @Override
    public Long crear(DomicilioFiscal entity, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, entity.getEliminado() != null ? entity.getEliminado() : false);
            ps.setString(2, entity.getCalle());
            ps.setInt(3, entity.getNumero());
            ps.setString(4, entity.getCiudad());
            ps.setString(5, entity.getProvincia());
            ps.setString(6, entity.getCodigoPostal());
            ps.setString(7, entity.getPais());
            ps.setObject(8, null); // empresa_id se establece desde EmpresaDao
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    entity.setId(id);
                    return id;
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID generado para DomicilioFiscal");
    }
    
    @Override
    public DomicilioFiscal leer(Long id, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<DomicilioFiscal> leerTodos(Connection connection) throws SQLException {
        List<DomicilioFiscal> domicilios = new ArrayList<>();
        
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                domicilios.add(mapResultSetToEntity(rs));
            }
        }
        
        return domicilios;
    }
    
    @Override
    public void actualizar(DomicilioFiscal entity, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getCalle());
            ps.setInt(2, entity.getNumero());
            ps.setString(3, entity.getCiudad());
            ps.setString(4, entity.getProvincia());
            ps.setString(5, entity.getCodigoPostal());
            ps.setString(6, entity.getPais());
            ps.setLong(7, entity.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se encontró el domicilio fiscal con ID: " + entity.getId());
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
     * Lee un domicilio fiscal por el ID de la empresa (relación 1→1).
     * 
     * @param empresaId ID de la empresa
     * @param connection Conexión a la base de datos
     * @return DomicilioFiscal encontrado o null
     * @throws SQLException si ocurre un error
     */
    public DomicilioFiscal leerPorEmpresaId(Long empresaId, Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_EMPRESA_ID)) {
            ps.setLong(1, empresaId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Establece la relación con la empresa al crear el domicilio fiscal.
     * 
     * @param domicilioFiscalId ID del domicilio fiscal
     * @param empresaId ID de la empresa
     * @param connection Conexión a la base de datos
     * @throws SQLException si ocurre un error
     */
    public void establecerRelacionEmpresa(Long domicilioFiscalId, Long empresaId, Connection connection) throws SQLException {
        String sql = "UPDATE domicilio_fiscal SET empresa_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, empresaId);
            ps.setLong(2, domicilioFiscalId);
            ps.executeUpdate();
        }
    }
    
    /**
     * Mapea un ResultSet a una entidad DomicilioFiscal.
     */
    private DomicilioFiscal mapResultSetToEntity(ResultSet rs) throws SQLException {
        DomicilioFiscal domicilio = new DomicilioFiscal();
        domicilio.setId(rs.getLong("id"));
        domicilio.setEliminado(rs.getBoolean("eliminado"));
        domicilio.setCalle(rs.getString("calle"));
        domicilio.setNumero(rs.getInt("numero"));
        domicilio.setCiudad(rs.getString("ciudad"));
        domicilio.setProvincia(rs.getString("provincia"));
        domicilio.setCodigoPostal(rs.getString("codigo_postal"));
        domicilio.setPais(rs.getString("pais"));
        
        return domicilio;
    }
}
