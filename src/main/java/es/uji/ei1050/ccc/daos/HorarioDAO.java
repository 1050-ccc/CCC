package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("HorarioDao")
public class HorarioDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class HorarioMapper implements RowMapper<Horario> {

        @Override
        public Horario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Horario horario = new Horario();
            horario.setDia(rs.getDate("dia"));
            horario.setHoraInicio(rs.getTime("horaInicio"));
            horario.setHoraFin(rs.getTime("horaFin"));
            horario.setPersoneDNI(rs.getString("dni"));
            horario.setPersoneNombre(rs.getString("nombre"));
            horario.setHorasTrabajadas(8);
            return horario;
        }
    }


    public List<Horario> getHorasTrabajadas(String dni, int mes, int año) {
        String sql = "select  h.dia, h.horaInicio, h.horaFin " +
                "from persone p join contrato c on (p.dni = c.Persone_dni) join horario h on (c.idContrato = h.Contrato_idContrato)  where upper(dni)=? "+
                "AND EXTRACT(month FROM dia) = ?  order by dia DESC;";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{dni,mes}, new HorarioMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Horario> getHorarioTrabajadores(String cif){
        String sql = "select h.*, p.dni, p.nombre from horario AS h JOIN contrato AS c ON h.contrato_idcontrato = c.idcontrato JOIN persone AS p On c.persone_dni = p.dni where p.empresa_cif = ?";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{cif}, new HorarioMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Horario> getHorarioTrabajador(String dni){
        String sql = "select h.*, p.dni, p.nombre from horario AS h JOIN contrato AS c ON h.contrato_idcontrato = c.idcontrato JOIN persone AS p On c.persone_dni = p.dni where p.dni = ?";
        try {
            return this.jdbcTemplate.query(sql, new Object[]{dni}, new HorarioMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
