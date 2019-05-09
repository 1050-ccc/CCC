package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.Contrato;
import es.uji.ei1050.ccc.model.ETipoContrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository("contrato")
public class ContratoDAO {


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
    private static final class ContratoMapper implements RowMapper<Contrato> {

        /**
         * Method that maps database objects to <code>Alumno</code> objects.
         * @param rs Database result.
         * @param rowNum Number of rows in Database result <code>rs</code>.
         * @return An <code>Contrato</code> object.
         * @throws SQLException If something goes wrong.
         */
        public Contrato mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contrato contrato = new Contrato();
            contrato.setSueldoBase(rs.getInt("sueldoBase"));
            contrato.setDiasVacaciones(rs.getInt("diasVacaciones"));
            contrato.setTipoContrato(ETipoContrato.getEstado(rs.getString("tipoContrato")));
            contrato.setPersone_dni(rs.getString("Persone_dni"));
            return contrato;
        }
    }

    //Método para ver el sueldo base de un trabajador

    public Contrato getSueldoBase(String dni) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, c.sueldoBase" +
                            "from persone p join contrato c on (p.dni = c.Persone_dni) where upper(dni)=?",
                    new Object[]{dni}, new ContratoMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Método para ver los dias de vacaciones restantes de un trabajador

    public Contrato getDiasVacaciones(String dni) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, c.diasVacaciones" +
                            "from persone p join contrato c on (p.dni = c.Persone_dni) where upper(dni)=?",
                    new Object[]{dni}, new ContratoMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Método para ver el contrato de un trabajado

    public Contrato getContrato(String dni) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  p.nombre, p.apellidos, c.sueldoBase, c.diasVacaciones, c.tipoContrato" +
                            "from persone p join contrato c on (p.dni = c.Persone_dni) where upper(dni)=?",
                    new Object[]{dni}, new ContratoMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Método para añadir un contrato

    public void addContrato(Contrato contrato) {
        if(this.jdbcTemplate.update(
                "insert into contrato(sueldoBase, diasVacaciones, tipoContrato, Persone_dni) values(?, ?, ?, ?)",
                contrato.getSueldoBase(), contrato.getDiasVacaciones(), contrato.getTipoContrato(), contrato.getPersone_dni()) > 0 );
    }


    //Método para editar un contrato

    public void updateContrato(Contrato contrato) {
        if(this.jdbcTemplate.update(
                "update contrato set sueldoBase=?, diasVacaciones=?, tipoContrato=? where upper(Persone_dni) = ?",
                contrato.getSueldoBase(), contrato.getDiasVacaciones(), contrato.getTipoContrato(), contrato.getPersone_dni() ) > 0);
    }

    //Método para borrar un contrato

    public boolean deleteContrato(String dni) {
        if(this.jdbcTemplate.update("delete from contrato where upper(Persone_dni) = ?", dni) > 0)
           return true;
        return false;
    }


}
