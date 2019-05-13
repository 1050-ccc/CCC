package es.uji.ei1050.ccc.daos;

import es.uji.ei1050.ccc.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            return horario;
        }
    }

    //MODIFICAR Y TERMINAR CONSULTA, TIENE QUE SER UNA LISTA DE HORARIO PARA VER LAS HORAS QUE HA TRABAJADO CADA DIA
    public Horario getHorasTrabajadas(String dni, int mes) {
        try {
            return this.jdbcTemplate.queryForObject(
                    "select  h.dia, h.horaInicio, h.horaFin" +
                            "from persone p join contrato c on (p.dni = c.Persone_dni) join horario h on (c.idContrato = h.Contrato_idContrat)  where upper(dni)=? and (mes)=?",
                    new Object[]{dni}, new HorarioMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





}
