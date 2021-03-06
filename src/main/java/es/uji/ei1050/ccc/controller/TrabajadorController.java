package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.*;
import es.uji.ei1050.ccc.model.*;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/trabajador")
public class TrabajadorController {

    private UsuarioDAO usuarioDAO;
    private PersoneDAO personeDAO;
    private TrabajadorDAO trabajadorDao;
    private NotificacionDAO notificacionDAO;
    private JefeDAO jefeDao;
    private ContratoDAO contratoDao;


    @Autowired
    public void setUsuariosDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Autowired
    public void setPersoneDao(PersoneDAO personeDAO) {
        this.personeDAO = personeDAO;
    }

    @Autowired
    public void setTrabajdoroDao(TrabajadorDAO trabajadorDao) {
        this.trabajadorDao = trabajadorDao;
    }

    @Autowired
    public void setNotificacionDAO(NotificacionDAO notificacionDAO) {
        this.notificacionDAO = notificacionDAO;
    }

    @Autowired
    public void setJefeDao(JefeDAO jefeDao) {
        this.jefeDao = jefeDao;
    }

    @Autowired
    public void setContratoDAO(ContratoDAO contratoDao) {
        this.contratoDao = contratoDao;
    }



    @RequestMapping("") //load the template
    public String load_template(HttpSession session, Model model) {

        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {
            return "redirect:/";
        }

        String url = (String) session.getAttribute("url");
        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();


        if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {

            String dni = (String) session.getAttribute("DNI");

            Trabajador trabajador = trabajadorDao.getTrabajadorByDNI(dni);

            if(trabajador.getTurno().equals("Mañana")){
                return "horario/mañana";
            }

            if(trabajador.getTurno().equals("Tarde")){
                return "horario/tarde";
            }

            if(trabajador.getTurno().equals("Noche")){
                return "horario/noche";
            }
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + url;
        }
        return "";
    }


    /**
     * Vista para ver los datos de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/informacion", method = RequestMethod.GET)
    public String verInformacionTrabajador(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if( tipo.getDescripcion().equals(Perfiles.TR.getDescripcion()) || tipo.getDescripcion().equals(Perfiles.JF.getDescripcion()) ) {
            String email = user.getEmail();
            model.addAttribute("trabajador", trabajadorDao.getTrabajadorByEmail(email));
            return "trabajador/informacion";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    //Métodos añadir trabajador

    /**
     * Vista para añadir una trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/añadir")
    public String añadirTrabajador(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String cif = (String) session.getAttribute("CIF");

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            Trabajador trabajador = new Trabajador();
            Contrato contrato = new Contrato();
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("trabajador", trabajador);
            return "trabajador/añadir";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/" + session.getAttribute("url");
        }
    }

    /**
     * Método que añade el trabajador.
     * @param trabajador
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/añadir", method = RequestMethod.POST)
    public String processAddSubmit(HttpSession session, @ModelAttribute("usuario") Usuario usuario, @ModelAttribute("trabajador") Trabajador trabajador,
                                 BindingResult bindingResult) {


        if (bindingResult.hasErrors())
            return "trabajador/añadir";


        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String pass = passwordEncryptor.encryptPassword(usuario.getPassword());
        usuario.setPassword(pass);

        usuario.setTipo(Perfiles.TR);
        trabajador.setEmail(usuario.getEmail());

        trabajador.setEmpresa_cif(String.valueOf(session.getAttribute("CIF")));
        personeDAO.addPersone(trabajador);
        trabajadorDao.addTrabajador(trabajador);
        usuarioDAO.addUsuario(usuario);

        String dni = trabajador.getDni();

        return "redirect:/contrato/anadir/"+dni;
    }

    /**
     * Método que borra un trabajador
     * @param dni
     * @return
     */
    @RequestMapping(value = "/borrar/{dni}")
    public String processDelete(HttpSession session, @PathVariable String dni) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }
        Trabajador trabajador = trabajadorDao.getTrabajadorByDNI(dni);
        String email = trabajador.getEmail();
        usuarioDAO.deleteUsuarios(email);
        trabajadorDao.deleteTrabajador(dni);
        contratoDao.deleteContrato(dni);


        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            personeDAO.deletePersone(dni);
            return "redirect:../lista";
        }else{
            Persone jefe =jefeDao.getJefeByCif(trabajador.getEmpresa_cif());
            notificacionDAO.addNotificacionDimision(trabajador,jefe.getDni());
            personeDAO.deletePersone(dni);
            session.invalidate();
            return "redirect:/";
        }
    }

    //Métodos editar trabajador

    /**
     * Vista para actualizar los datos de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/editar/{dni}", method = RequestMethod.GET)
    public String updateTrabajador(HttpSession session, Model model, @PathVariable String dni) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("trabajador", trabajadorDao.getTrabajadorByDNI(dni));
            return "trabajador/editarparajefe";
        } else {

            if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())){
                model.addAttribute("trabajador", trabajadorDao.getTrabajadorByDNI(dni));
                return "trabajador/editarparatrabajador";
            }
            else{
                model.addAttribute("error", "No tienes permiso para acceder a este sitio");
                return "redirect:" + session.getAttribute("url");
            }

        }
    }

    /**
     * Método que actualiza los datos del trabajador.
     * @param session

     * @param trabajador
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/editar/{dni}", method = RequestMethod.POST)
    public String processUpdateSubmit(@RequestBody String data, HttpSession session,
                                      @ModelAttribute("trabajador") Trabajador trabajador,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "editarparajefe";


        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();

        personeDAO.updatePersone(trabajador);
        trabajadorDao.updateTrabajador(trabajador);

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())){
            return "redirect:../lista";
        }else{
            if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())){
                return "trabajador/informacion";
            }else{
                return "redirect:" + session.getAttribute("url");
            }
        }

    }

    /**
     * Lista trabajadores.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/lista")
    public String listTrabajadoresEmpresa(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String cif = (String) session.getAttribute("CIF");


        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {

            model.addAttribute("trabajadores", trabajadorDao.getTrabajadoresEmpresa(cif));

            return "trabajador/lista";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }

    /**
     * Lista disponibilidad trabajadores.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/listadisponibilidad")
    public String listDisponibilidadTrabajadoresEmpresa(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String cif = (String) session.getAttribute("CIF");
        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("trabajadores", trabajadorDao.getDisponibilidadTrabajadores(cif));
            return "trabajador/listadisponibilidad";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }



    /**
     * Lista emails trabajadores.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/listaemails")
    public String listEmailsdTrabajadoresEmpresa(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String cif = (String) session.getAttribute("CIF");

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("trabajadores", trabajadorDao.getTrabajadoresEmpresa(cif));
            return "trabajador/listaemails";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }


}
