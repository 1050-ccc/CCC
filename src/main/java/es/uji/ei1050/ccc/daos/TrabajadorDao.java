package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.Trabajador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository("tabajadorDao")
public class TrabajadorDao {

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
            trabajador.setSemestreInicioEstancia(rs.getInt("semestre_inicio_estancia"));
            trabajador.setIdItinerario(rs.getInt("itinerario"));
            trabajador.setItinerario(rs.getString(6));
            return trabajador;
        }
    }

    /**
     * Method that lists all <code>Alumno</code>s.
     * @return An <code>ArrayList</code> of <code>Alumno</code>s or <code>null</code> if an error occurs while accessing database.
     */
    public List<Trabajador> getTrabajadores() {
        try {
            return this.jdbcTemplate.query(
                    "select a.dni, a.nombre, a.username, a.semestre_inicio_estancia, a.itinerario, i.nombre " +
                            "from alumno a join itinerario i on (a.itinerario = i.id);", new TrabajadorMapper());
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
    public Alumno getAlumno(String dni) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select a.dni, a.nombre, a.username, a.semestre_inicio_estancia, i.nombre " +
                            "from alumno a join itinerario i on (a.itinerario = i.id) where upper(dni)=?",
                    new Object[]{dni.toUpperCase()}, new AlumnoMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Alumno getInfoAlumno(String dni) {
        return this.jdbcTemplate.queryForObject(
                "select dni, nombre, semestre_inicio_estancia, itinerario, username from alumno where upper(dni)=?",
                new Object[]{dni.toUpperCase()}, new AlumnoMapper());
    }

    /**
     * Method that list an <code>Alumno</code> by its <b>username</b>.
     * @param username <code>String</code> that indicates the <code>Alumno</code>'s <b>username</b>.
     * @return An <code>Alumno</code> or <code>null</code> if an error occurs while accessing database.
     */
    public Alumno getAlumnoByUsername(String username) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select a.dni, a.nombre, a.username, a.semestre_inicio_estancia, a.itinerario, i.nombre " +
                            "from alumno a join itinerario i on (a.itinerario = i.id) where upper(username)=?",
                    new Object[]{username.toUpperCase()}, new AlumnoMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that adds an <code>Alumno</code> to the database.
     * @param alumno <code>Alumno</code> object to be added.
     * @return A <code>boolean</code> value indicating if the operation was successful or not.
     */
    public boolean addAlumno(Alumno alumno) {
        try {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            String pass = passwordEncryptor.encryptPassword(alumno.getPasswd());
            if (this.jdbcTemplate.update("insert into user_login(username, passwd, tipo) values(?, ?, ?)",
                    alumno.getDni(), pass, Tipo.ALUMNO.getDescription()) == 1)
                if(this.jdbcTemplate.update(
                        "insert into alumno(dni, nombre, username, semestre_inicio_estancia, itinerario) values(?, ?, ?, ?, ?)",
                        alumno.getDni(), alumno.getNombre(), alumno.getUsername(), alumno.getSemestreInicioEstancia(), alumno.getItinerario()) == 1)
                    return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Method the updates an <code>Alumno</code> object in database.
     * @param alumno <code>Alumno</code> object that will be updated.
     * @return A <code>boolean</code> value indicating either the operation was successful or not.
     */
    public boolean updateAlumno(Alumno alumno) {
        try {
            if (this.jdbcTemplate.update(
                    "update alumno set semestre_inicio_estancia=? where upper(dni) = ?",
                    alumno.getSemestreInicioEstancia(),     alumno.getDni().toUpperCase()) > 0){
                System.out.println("\n\nFunciona\n\n");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }



    /**
     * Method that removes <code>Alumno</code>s in a database.
     * @param dni <code>String</code> that indicates what <code>Alumno</code>s to remove from database.
     * @return A <code>boolean</code> value indicating either the operation was successful or not.
     */
    public boolean deleteAlumno(String dni) {
        try {
            Alumno a = getAlumno(dni);
            if(this.jdbcTemplate.update("delete from alumno where upper(dni) = ?", dni.toUpperCase()) > 0)
                if(this.jdbcTemplate.update("delete from user_login where upper(username) = ?", a.getUsername().toUpperCase()) > 0)
                    return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
}
