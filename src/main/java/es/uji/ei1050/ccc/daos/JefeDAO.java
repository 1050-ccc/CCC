package es.uji.ei1050.ccc.daos;
import es.uji.ei1050.ccc.model.Jefe;
import es.uji.ei1050.ccc.model.Perfiles;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository("jefeDao")
public class JefeDAO {

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
    private static final class JefeMapper implements RowMapper<Jefe> {

        /**
         * Method that maps database objects to <code>Alumno</code> objects.
         * @param rs Database result.
         * @param rowNum Number of rows in Database result <code>rs</code>.
         * @return An <code>Alumno</code> object.
         * @throws SQLException If something goes wrong.
         */
        public Jefe mapRow(ResultSet rs, int rowNum) throws SQLException {
            Jefe jefe = new Jefe();
            jefe.setNombre(rs.getString("nombre"));
            jefe.setApellidos(rs.getString("apellidos"));
            jefe.setDni(rs.getString("dni"));
            jefe.setTelefono(rs.getInt("telefono"));
            jefe.setDomicilio(rs.getString("domicilio"));
            jefe.setEmail(rs.getString("email"));
            jefe.setCuentaBancaria(rs.getString("cuentaBancaria"));
            return jefe;
        }
    }

    public Jefe getJefeByEmail(String email) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  nombre, apellidos, dni, telefono, domicilio, email, cuentaBancaria, puestoTrabajo, turno " +
                            "from persone where upper(email)=?",
                    new Object[]{email}, new JefeMapper());
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
    public Jefe getJefeByDNI(String dni) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  nombre, apellidos, dni, telefono, domicilio, email, cuentaBancaria, puestoTrabajo, turno " +
                            "from persone where upper(dni)=?",
                    new Object[]{dni}, new JefeMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Method that adds an <code>Alumno</code> to the database.
     * @param jefe <code>Alumno</code> object to be added.
     * @return A <code>boolean</code> value indicating if the operation was successful or not.
     */
    public boolean addJefe(Jefe jefe) {
        try {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            String pass = passwordEncryptor.encryptPassword(jefe.getPasswd());
            if (this.jdbcTemplate.update("insert into usuarios(username, passwd, tipo) values(?, ?, ?)",
                    jefe.getEmail(), pass, Perfiles.JF.getDescripcion()) == 1)
                if(this.jdbcTemplate.update(
                        "insert into persone(nombre, apellidos, dni, telefono, domicilio, email, cuentaBancaria) values(?, ?, ?, ?, ?, ?, ?)",
                        jefe.getNombre(), jefe.getApellidos(), jefe.getDni(), jefe.getTelefono(), jefe.getDomicilio(), jefe.getEmail(), jefe.getCuentaBancaria()) == 1)

                    if (this.jdbcTemplate.update("insert into jefe(Persone_dni) values(?)",
                            jefe.getDni()) == 1)
                        return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Method the updates an <code>Alumno</code> object in database.
     * @param jefe <code>Alumno</code> object that will be updated.
     * @return A <code>boolean</code> value indicating either the operation was successful or not.
     */

    //REVISAR DAO
    public boolean updateTrabajador(Jefe jefe) {
        try {
            if(this.jdbcTemplate.update(
                    "update persone set telefono=?, domicilio=?, cuentaBancaria=?, telefono=? where upper(cif) = ?",
                    jefe.getTelefono(), jefe.getDomicilio(), jefe.getCuentaBancaria(), jefe.getTelefono(), jefe.getDni() ) > 0);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method that removes <code>Alumno</code>s in a database.
     * @param dni <code>String</code> that indicates what <code>Alumno</code>s to remove from database.
     * @return A <code>boolean</code> value indicating either the operation was successful or not.
     */

    //REVISAR DAO
    public boolean deleteJefe(String dni) {
        try {
            Jefe a = getJefeByDNI(dni);
            if(this.jdbcTemplate.update("delete from jefe where upper(dni) = ?", dni) > 0)
                if(this.jdbcTemplate.update("delete from usuarios where upper(username) = ?", a.getEmail()) > 0)
                    return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


}
