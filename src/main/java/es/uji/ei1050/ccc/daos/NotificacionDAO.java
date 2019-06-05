package es.uji.ei1050.ccc.daos;


import es.uji.ei1050.ccc.model.Notificacion;
import es.uji.ei1050.ccc.model.Trabajador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

@Repository("notificacionDao")
public class NotificacionDAO {


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
    private static final class NotificacionMapper implements RowMapper<Notificacion> {

        /**
         * Method that maps database objects to <code>Alumno</code> objects.
         * @param rs Database result.
         * @param rowNum Number of rows in Database result <code>rs</code>.
         * @return An <code>Alumno</code> object.
         * @throws SQLException If something goes wrong.
         */
        public Notificacion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notificacion notificacion = new Notificacion();
            notificacion.setFechaHora(rs.getDate("fechaHora"));
            notificacion.setAsunto(rs.getString("Asunto"));
            notificacion.setPersone_dni(rs.getString("Persone_dni"));
            return notificacion;
        }
    }

    //TERMINAR
    public void addNotificacionDimision(Trabajador trabajador,  String dniJefe) {


        Calendar fecha = Calendar.getInstance();
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int mes = fecha.get(Calendar.MONTH);
        int año = fecha.get(Calendar.YEAR);

        String dni = trabajador.getDni();
        String nombre = trabajador.getNombre();
        String apellido = trabajador.getApellidos();
        String puestoTrabajo = trabajador.getPuestoTrabajo();

        String asunto = "El empleado "+nombre+" "+apellido+" con "+dni+" presenta su dimisión y abandonará su puesto" +
                " de trabajo de "+trabajador.getPuestoTrabajo()+" con efecto inmediato";

        //String asunto ="El trabajador a dimitido";

        String sql = "INSERT INTO Notificacion(fechaHora, Asunto, Persone_dni) " + "VALUES(?,?,?);";
        this.jdbcTemplate.update(sql, fecha, asunto, dniJefe);

    }

    public List<Notificacion> getNotificaciones(String cif) {
        String sql = "SELECT n.* FROM notificacion AS n JOIN persone AS p ON n.persone_dni=p.dni WHERE p.empresa_cif=? ORDER BY n.fechaHora DESC";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{cif}, new NotificacionMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNumeroNotificaciones(String cif){
        String sql = "SELECT COUNT(n.*) FROM notificacion AS n JOIN persone AS p ON n.persone_dni=p.dni WHERE p.empresa_cif=? GROUP BY n.fechaHora ORDER BY n.fechaHora DESC";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{cif}, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
