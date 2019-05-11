package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.Persone;
import es.uji.ei1050.ccc.model.Trabajador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAOs de persone
 */
@Repository
public class PersoneDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class PersoneMapper implements RowMapper<Persone> {

        @Override
        public Persone mapRow(ResultSet rs, int rowNum) throws SQLException {
            Persone persone = new Persone();
            persone.setNombre(rs.getString("nombre"));
            persone.setApellidos(rs.getString("apellidos"));
            persone.setDni(rs.getString("dni"));
            persone.setDomicilio(rs.getString("domicilio"));
            persone.setTelefono(rs.getInt("telefono"));
            persone.setEmail(rs.getString("email"));
            persone.setCuentaBancaria(rs.getString("cuentaBancaria"));
            persone.setEmpresa_cif(rs.getString("Empresa_cif"));

            return persone;
        }
    }

    /**
     * @return lista con todos los usuarios
     */
    public List<Persone> getPersones() {
        String sql = "SELECT * " + "FROM persone;";
        return this.jdbcTemplate.query(sql, new PersoneMapper());
    }

    /**
     * @param dni
     *            dni de la persone
     * @return un persone a partir de su dni
     */
    public Persone getUsuario(String dni) {
        try {
            String sql = "SELECT * " + "FROM persone " + "WHERE dni=?;";
            Persone persone = this.jdbcTemplate.queryForObject(sql, new Object[] { dni }, new PersoneMapper());
            return persone;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * registra un usuario nuevo en la base de datos
     *
     * @param persone
     */
    public void addPersone(Persone persone) {
        String sql = "INSERT INTO persone(nombre, apellidos, dni, domicilio, telefono, email, cuentaBancaria, Empresa_cif) " + "VALUES(?,?,?, ?, ?, ? ,?, ?);";
        this.jdbcTemplate.update(sql, persone.getNombre(), persone.getApellidos(), persone.getDni(), persone.getDomicilio(), persone.getTelefono(), persone.getEmail(), persone.getCuentaBancaria(), persone.getEmpresa_cif());
    }

    public boolean updatePersone(Persone persone) {
        try {
            if(this.jdbcTemplate.update(
                    "update persone set telefono=?, domicilio=?, cuentaBancaria=? where upper(dni) = ?",
                    persone.getTelefono(), persone.getDomicilio(), persone.getCuentaBancaria(), persone.getDni() ) > 0);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
