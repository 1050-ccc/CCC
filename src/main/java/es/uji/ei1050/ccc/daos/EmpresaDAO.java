package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("empresaDao")
public class EmpresaDAO {

    /**
     * <code>SpringFramework</code> object used to access databases.
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * Method that initializes the <code>JdbcTemplate</code> with the database source.
     * @param dataSource Database source used to initialize the <code>JdbcTemplate</code>.
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Inner class that maps database objects to <code>Alumno</code> objects.
     */
    private static final class EmpresaMapper implements RowMapper<Empresa> {

        /**
         * Method that maps database objects to <code>Alumno</code> objects.
         * @param rs Database result.
         * @param rowNum Number of rows in Database result <code>rs</code>.
         * @return An <code>Alumno</code> object.
         * @throws SQLException If something goes wrong.
         */
        public Empresa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Empresa empresa = new Empresa();
            empresa.setCIF(rs.getString("cif"));
            empresa.setNombre(rs.getString("nombre"));
            empresa.setNombreComercial(rs.getString("nombreComercial"));
            empresa.setDomicilioFiscal(rs.getString("domicilioFiscal"));
            empresa.setCuentaBancaria(rs.getString("cuentaBancaria"));
            empresa.setPersonaJuridica(rs.getString("personaJuridica"));
            empresa.setCuentaBancaria(rs.getString("personaFiscal"));
            empresa.setEmail(rs.getString("email"));
            empresa.setTelefono(rs.getInt("telefono"));
            return empresa;
        }
    }

    /**
     * @return lista de todas las empresas
     */
    public List<Empresa> getEmpresas() {
        String sql = "SELECT * " + "FROM empresa ORDER BY cif;";
        return this.jdbcTemplate.query(sql, new EmpresaMapper());
    }


    public Empresa getEmpresa(String cif) {
        return this.jdbcTemplate.queryForObject(
                "select cif, nombre, nombreComercial, domicilioFiscal, cuentaBancaria, personaJuridica, personaFiscal, email, telefono  from empresa where upper(cif)=?",
                new Object[]{cif}, new EmpresaMapper());
    }

    /**
     * Método que devuelve los datos de una empresa a partir del nombre de usuario.
     * Se usa para obtener los datos de la empresa después de hacer el login.
     * @param username String que indica el nombre de usuario que se va ha buscar en la base de datos.
     * @return un objeto de tipo <code>Empresa</code>.
     */
    public Empresa getEmpresaByUsername(String username) {
        return this.jdbcTemplate.queryForObject(
                "select cif, nombre, nombreComercial, domicilioFiscal, cuentaBancaria, personaJuridica, personaFiscal, email, telefono  from empresa where upper(email)=?",
                new Object[]{username}, new EmpresaMapper());
    }

    public void addEmpresa(Empresa empresa) {
        if(this.jdbcTemplate.update(
                "insert into empresa(cif, nombre, nombreComercial, domicilio_fiscal, cuenta_bancaria, personaJuridica, personaFiscal, email, telefono) values(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                empresa.getCIF(), empresa.getNombre(), empresa.getNombreComercial(), empresa.getDomicilioFiscal(), empresa.getCuentaBancaria(),
                empresa.getPersonaJuridica(), empresa.getPersonaFiscal(), empresa.getEmail(), empresa.getTelefono()) > 0 );
    }

    public void updateEmpresa(Empresa empresa) {
        if(this.jdbcTemplate.update(
                "update empresa set nombre=?, nombreComercial=?, domicilioFiscal=?, cuentaBancaria=?, personaJuridica=?, personaFiscal=?, telefono=? where upper(cif) = ?",
                empresa.getNombre(), empresa.getNombreComercial(), empresa.getDomicilioFiscal(), empresa.getCuentaBancaria(),
                empresa.getPersonaJuridica(), empresa.getPersonaFiscal(), empresa.getTelefono(), empresa.getCIF() ) > 0);
    }

    public void deleteEmpresa(String cif) {
        if(this.jdbcTemplate.update("delete from empresa where upper(cif) = ?", cif) > 0)
            this.jdbcTemplate.update("delete from usuarios where upper(username) = ?", cif);
    }




}
