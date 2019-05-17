package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.ETurnos;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Trabajador;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository("tabajadorDao")
public class TrabajadorDAO {

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
    private static final class TrabajadorMapper implements RowMapper<Trabajador> {

        /**
         * Method that maps database objects to <code>Alumno</code> objects.
         * @param rs Database result.
         * @param rowNum Number of rows in Database result <code>rs</code>.
         * @return An <code>Alumno</code> object.
         * @throws SQLException If something goes wrong.
         */
        public Trabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trabajador trabajador = new Trabajador();
            trabajador.setNombre(rs.getString("nombre"));
            trabajador.setApellidos(rs.getString("apellidos"));
            trabajador.setDni(rs.getString("dni"));
            trabajador.setTelefono(rs.getInt("telefono"));
            trabajador.setDomicilio(rs.getString("domicilio"));
            trabajador.setEmail(rs.getString("email"));
            trabajador.setCuentaBancaria(rs.getString("cuentaBancaria"));
            trabajador.setEmpresa_cif(rs.getString("Empresa_cif"));
            trabajador.setPuestoTrabajo(rs.getString("puestoTrabajo"));
            trabajador.setTurno((rs.getString("turno")));
            return trabajador;
        }
    }

    /**
     * Method that lists all <code>Alumno</code>s.
     * @return An <code>ArrayList</code> of <code>Alumno</code>s or <code>null</code> if an error occurs while accessing database.
     */
    public List<Trabajador> getTrabajadoresEmpresa(String cif) {
        String sql = "select  p.nombre, p.apellidos, p.dni, p.telefono, p.domicilio, p.email, p.cuentaBancaria, p.Empresa_cif, t.puestoTrabajo, t.turno " +
                "from persone p join trabajador t on (p.dni = t.Persone_dni)  where upper(p.Empresa_cif)=?";

        try {
            return this.jdbcTemplate.query(sql, new Object[]{cif}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Trabajador getTrabajadorByEmail(String email) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, p.dni, p.telefono, p.domicilio, p.email, p.cuentaBancaria, p.Empresa_cif, t.puestoTrabajo, t.turno " +
                            "from persone p join trabajador t on (p.dni = t.Persone_dni) where upper(email)=?",
                    new Object[]{email.toUpperCase()}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that list an <code>Alumno</code> by its <b>DNI</b>.
     * @param dni <code>String</code> that indicates the <code>Alumno</code>'s <b>DNI</b>.
     * @return An <code>Alumno</code> or <code>null</code> if an error occurs while accessing database.
     */
    public Trabajador getTrabajadorByDNI(String dni) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, p.dni, p.telefono, p.domicilio, p.email, p.cuentaBancaria, p.Empresa_cif, t.puestoTrabajo, t.turno " +
                            "from persone p join trabajador t on (p.dni = t.Persone_dni) where upper(dni)=?",
                    new Object[]{dni.toUpperCase()}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Method that adds an <code>Trabajador</code> to the database.
     * @param trabajador <code>Trabajador</code> object to be added.
     */
    public void addTrabajador(Trabajador trabajador) {
        String sql = "INSERT INTO Trabajador(Persone_dni, puestoTrabajo, turno) " + "VALUES(?,?,?);";
        this.jdbcTemplate.update(sql, trabajador.getDni(), trabajador.getPuestoTrabajo(), trabajador.getTurno());
    }

    /**
     * Method the updates an <code>Alumno</code> object in database.
     * @param trabajador <code>Alumno</code> object that will be updated.
     * @return A <code>boolean</code> value indicating either the operation was successful or not.
     */

    public boolean updateTrabajador(Trabajador trabajador) {
        try {
            if(this.jdbcTemplate.update(
                    "update trabajador set puestoTrabajo=?, turno=? where upper(Persone_dni) = ?",
                    trabajador.getPuestoTrabajo(), trabajador.getTurno(), trabajador.getDni() ) > 0);
                return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method that removes <code>Persone</code>s in a database.
     * @param dni <code>String</code> that indicates what <code>Persone</code>s to remove from database.
     * @return A <code>boolean</code> value indicating either the operation was successful or not.
     */
    public boolean deleteTrabajador(String dni) {
        try {
            if(this.jdbcTemplate.update("delete from trabajador where upper(Persone_dni) = ?", dni) > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Method that list an <code>Alumno</code> by its <b>username</b>.
     * @param username <code>String</code> that indicates the <code>Alumno</code>'s <b>username</b>.
     * @return An <code>Alumno</code> or <code>null</code> if an error occurs while accessing database.
     */

    public Trabajador getTrabajadorByUsername(String username) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, p.dni, p.telefono, p.domicilio, p.email, p.cuentaBancaria, t.puestoTrabajo, t.turno " +
                            "from persone p join trabajador t on (p.dni = t.Persone_dni) where upper(email)=?",
                    new Object[]{username}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    public Trabajador getDisponibilidadTrabajador(String dni){
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, t.turno " +
                            "from persone p join trabajador t on (p.dni = t.Persone_dni) where upper(dni)=?",
                    new Object[]{dni.toUpperCase()}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public List<Trabajador> getDisponibilidadTrabajadores(String cif) {
        try {
            return this.jdbcTemplate.query(
                    "select  p.nombre, p.apellidos, t.turno " +
                            "from persone p join trabajador t on (p.dni = t.Persone_dni)  where upper(p.Empresa_cif)=?",new Object[]{cif}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Trabajador> getEmailTrabajadores(String cif) {
        try {
            return this.jdbcTemplate.query(
                    "select  nombre, apellidos, email " +
                            "from persone where upper(Empresa_cif)=?",new Object[]{cif}, new TrabajadorMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

}

