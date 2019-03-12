package es.uji.ei1050.ccc.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Usuario;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * DAOs de los usuarios
 */
@Repository
public class UsuarioDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class UsuariosMapper implements RowMapper<Usuario> {

        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuarios = new Usuario();
            usuarios.setUsuario(rs.getString("usuario"));
            usuarios.setPassword(rs.getString("password"));
            usuarios.setTipo(Perfiles.getEstado(rs.getString("tipo")));

            return usuarios;
        }
    }

    /**
     * @return lista con todos los usuarios
     */
    public List<Usuario> getUsuarios() {
        String sql = "SELECT usuario, tipo " + "FROM usuarios;";
        return this.jdbcTemplate.query(sql, new UsuariosMapper());
    }

    /**
     * @param usuario
     *            nombre de usuario el email para los profesores, tutores, alumnos y
     *            BTC el CIF para las empresas
     * @return un usuario a partir de su nombre de usuario
     */
    public Usuario getUsuario(String usuario) {
        try {
            String sql = "SELECT * " + "FROM usuarios " + "WHERE usuario=?;";
            Usuario user = this.jdbcTemplate.queryForObject(sql, new Object[] { usuario }, new UsuariosMapper());
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * registra un usuario nuevo en la base de datos
     *
     * @param usuario
     */
    public void addUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(usuario, password, tipo) " + "VALUES(?,?,?);";
        this.jdbcTemplate.update(sql, usuario.getUsuario(), usuario.getPassword(), usuario.getTipo().toString());
    }

    /**
     * modifica la contraseña de un usuario de la base de datos
     *
     * @param usuario
     */
    public void updateUsuarios(Usuario usuario) {
        String sql = "UPDATE usuarios SET password=?" + "WHERE usuario=?;";
        this.jdbcTemplate.update(sql, usuario.getPassword(), usuario.getUsuario());
    }

    /**
     * borra a un usuario de la base de datos
     *
     * @param usuario
     */
    public void deleteUsuarios(String usuario) {
        String sql = "DELETE FROM usuarios " + "WHERE usuario=?;";
        this.jdbcTemplate.update(sql, usuario);
    }

    /**
     * Compueba que un usuario este en la base de datros y que la contraseña
     * proporcionada en el login sea la correcta, en tal caso devuelve dicho usuario
     *
     * @param username
     * @param password
     * @return un usuario
     */
    public Usuario loadUserByUsername(String username, String password) {
        Usuario user = getUsuario(username.trim());
        if (user == null)
            return null; // Usuari no trobat
        // Contrasenya
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (passwordEncryptor.checkPassword(password, user.getPassword())) {
            password = null;
            return user;
        } else {
            return null; // bad login!
        }
    }
}
