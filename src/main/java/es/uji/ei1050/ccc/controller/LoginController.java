package es.uji.ei1050.ccc.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import es.uji.ei1050.ccc.daos.*;
import es.uji.ei1050.ccc.model.Jefe;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Persone;
import es.uji.ei1050.ccc.model.Usuario;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Usuario.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Usuario user = (Usuario) obj;
        if (user.getEmail().trim().equals(""))
            errors.rejectValue("usuario", "obligatorio", "No se ha introducido un email de usuario");
        if (user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "No se ha introducido una contraseña");

    }
}

/**
 * Controlador de los usuarios
 */
@Controller
@RequestMapping("/usuario")
public class LoginController {

    private UsuarioDAO usuarioDAO;
    private EmpresaDAO empresaDAO;
    private TrabajadorDAO trabajadorDAO;
    private JefeDAO jefeDAO;
    private PersoneDAO personeDAO;

    /**
     * @param usuarioDAO
     */
    @Autowired
    public void setUsuariosDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * @param empresaDAO
     */
    @Autowired
    public void setEmpresaDAO(EmpresaDAO empresaDAO) {
        this.empresaDAO = empresaDAO;
    }

    /**
     * @param trabajadorDAO
     */
    @Autowired
    public void setTrabajadorDAO(TrabajadorDAO trabajadorDAO) {
        this.trabajadorDAO = trabajadorDAO;
    }

    /**
     * @param jefeDAO
     */
    @Autowired
    public void setJefeDAO(JefeDAO jefeDAO) {
        this.jefeDAO = jefeDAO;
    }

    /**
     * @param personeDAO
     */
    @Autowired
    public void setPersoneDAO(PersoneDAO personeDAO) {
        this.personeDAO = personeDAO;
    }
    //

    /**
     * @param session
     * @param model
     * @return a la pagina raiz si el usuario ya esta logeado, o al formulario del
     *         login en caso contrario después de haber añadido un usuario al modelo
     */
    @RequestMapping("/login")
    public String listaAsignacions(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO
        if (session.getAttribute("usuario") != null) {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("persone", new Persone());
            return "redirect:/";
        }
        //
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("persone", new Persone());
        return "usuario/login";
    }

    /**
     * comprobaciones del login
     *
     * @param usuario
     * @param bindingResult
     * @param session
     * @return si el login es correcto devuelve a la página principal
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult,
                             HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usuario, bindingResult);

        /*if (bindingResult.hasErrors()) {
            return "usuario/login";
        }*/
        // Comprova que el login siga correcte
        // intentant carregar les dades de l'usuari
        usuario = usuarioDAO.loadUserByUsername(usuario.getEmail(), usuario.getPassword());
        if (usuario == null) {
            bindingResult.rejectValue("password", "badpw", "Contrasenya incorrecta");
            return "usuario/login";
        }
        // Autenticats correctament.
        String email = usuario.getEmail();
        String empresaCIF = "";
        String personeDNI = "0";

        if (usuario.getTipo().equals(Perfiles.JF)) {
            personeDNI = jefeDAO.getJefeByEmail(email).getDni();
            empresaCIF = jefeDAO.getJefeByEmail(email).getEmpresa_cif();
        }

        if (usuario.getTipo().equals(Perfiles.TR)) {
            personeDNI = trabajadorDAO.getTrabajadorByEmail(email).getDni();
            empresaCIF = trabajadorDAO.getTrabajadorByEmail(email).getEmpresa_cif();
        }


        // Guardem les dades de l'usuari autenticat a la sessió
        usuario.setPassword(null);
        session.setAttribute("usuario", usuario);
        session.setAttribute("CIF", empresaCIF);
        session.setAttribute("DNI", personeDNI);

        /*if(usuario.getTipo()==Perfiles.JF){
            return "ree"
        }*/

        // Torna a la pàgina principal
        return "redirect:/";
    }

    /**
     * Invalida la sesión
     *
     * @param session
     * @return a la página raíz
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~//

    /**
     * @param model
     * @return la vista para añadir un usuario
     */
    @RequestMapping(value = "/add")
    public String addUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Persone persone = (Persone) session.getAttribute("persone");
        if (usuario == null) {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("persone", new Persone());
            return "usuario/add";
        }
        if (((Usuario) session.getAttribute("usuario")).getEmail().equals("admin")) {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("persone", new Persone());
            return "usuario/add";
        }
        return "redirect:/";
    }

    /**
     * @param usuario
     * @param bindingResult
     * @return redirige al login si se ha añadido con éxito, y en caso contrario de
     *         nuevo a la pagina de de add
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("usuario") Usuario usuario, @ModelAttribute("persone") Persone persone, BindingResult bindingResult)
            throws NoSuchAlgorithmException {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usuario, bindingResult);
        if (bindingResult.hasErrors())
            return "usuario/add";
        //
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String pass = passwordEncryptor.encryptPassword(usuario.getPassword());
        usuario.setPassword(pass);
        //

        persone.setEmail(usuario.getEmail());
        personeDAO.addPersone(persone);
        usuarioDAO.addUsuario(usuario);

        if(usuario.getTipo().toString().equals("JF")) {
            jefeDAO.addJefe(persone.getDni());
        }

        return "redirect:/";
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~//

    // @RequestMapping(value = "/update/{usuario}/", method = RequestMethod.GET)
    // public String editEmpresa(Model model, @PathVariable String usuario) {
    // model.addAttribute("usuario", usuariosDAO.getEmail(usuario));
    // return "usuario/update";
    // }
    //
    // @RequestMapping(value = "/update/{usuario}/", method =
    // RequestMethod.POST)
    // public String processUpdateSubmit(@PathVariable String usuarioID,
    // @ModelAttribute("usuario") Usuario usuario,
    // BindingResult bindingResult) throws NoSuchAlgorithmException {
    // if (bindingResult.hasErrors())
    // return "usuario/update";
    // //
    // Usuario user = usuariosDAO.getEmail(usuario.getEmail());
    //
    // if (user == null) {
    // bindingResult.rejectValue("usuario", "notFound", "Usuario no
    // encontrado");
    // return "usuario/login";
    // }
    //
    // String = .get256(usuario.get(),user.getSalt());
    //
    // if(user.get()!=){
    // bindingResult.rejectValue("password", "pwdNoCorrecta", "Contraseña
    // incorrecta");
    // return "usuario/login";
    // }
    // //
    // byte[] salt = .generateSalt();
    // String = .get256(usuario.get(),salt);
    // usuario.set();
    // usuario.setSalt(salt);
    // //
    // usuariosDAO.updateUsuarios(usuario);
    // return "redirect:../../list";
    // }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~//

    // /**
    // * elimina un usuario
    // *
    // * @param usuario
    // * @return a la pagina raiz
    // */
    // @RequestMapping(value = "/delete/{usuario}/")
    // public String processDelete(@PathVariable String usuario) {
    // usuariosDAO.deleteUsuarios(usuario);
    // return "redirect:/";
    // }

}
