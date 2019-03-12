package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.TrabajadorDAO;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Trabajador;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/templates/trabajador")
public class TrabajadorController {

    private TrabajadorDAO trabajadorDao;

    @Autowired
    public void setTrabajdoroDao(TrabajadorDAO trabajadorDao) {
        this.trabajadorDao = trabajadorDao;
    }


    /**
     * Vista para ver los datos de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String verInformacionTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        if(tipo.equals(Perfiles.JF.getDescripcion()) || tipo.equals(Perfiles.TR.getDescripcion())) {
            String username = user.getEmail();
            model.addAttribute("templates/trabajador", trabajadorDao.getTrabajadorByUsername(username));
            return "trabajador/informacion :: list";
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
    @RequestMapping(value = "/add")
    public String añadirTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();

        if(tipo.equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("templates/trabajador", new Trabajador());
            return "trabajador/add";
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("templates/trabajador") Trabajador trabajador,
                                   BindingResult bindingResult) {

        //AlumnoValidator alumnoValidator = new AlumnoValidator();
        //alumnoValidator.validate(alumno, bindingResult);

        if (bindingResult.hasErrors())
            return "alumno/add";
        trabajadorDao.addTrabajador(trabajador);
        return "redirect:list.html :: list";
    }

    /**
     * Método que borra un alumno
     * @param dni
     * @return
     */
    @RequestMapping(value = "/borrar/{dni}", method = RequestMethod.DELETE)
    public String processDelete(@PathVariable String dni) {
        // TODO - Comprobar quien puede borrar un alumno
        trabajadorDao.deleteTrabajador(dni);
        return "redirect:../list";
    }

    //Métodos editar trabajador

    /**
     * Vista para actualizar los datos de una alumno.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/editar/{dni}", method = RequestMethod.GET)
    public String updateTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        if(tipo.equals(Perfiles.JF.getDescripcion()) || tipo.equals(Perfiles.TR.getDescripcion())) {
            String dni = user.getEmail();
            System.out.println(dni);
            model.addAttribute("templates/trabajador", trabajadorDao.getTrabajadorByUsername(dni));
            return "trabajador/update";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    /**
     * Método que actualiza los datos del trabajador.
     * @param session

     * @param trabajador
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String processUpdateSubmit(@RequestBody String data, HttpSession session,
                                      @ModelAttribute("templates/trabajador") Trabajador trabajador,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "trabajador/update";


        Usuario user = (Usuario) session.getAttribute("user");
        trabajador = trabajadorDao.getTrabajadorByUsername(user.getEmail());
        Perfiles tipo = user.getTipo();
        String dni = trabajador.getDni();


        if(tipo.equals(Perfiles.JF.getDescripcion()) || tipo.equals(Perfiles.TR.getDescripcion())) {
            trabajadorDao.updateTrabajador(trabajador);
            return "trabajador/informacion";
        } else {
            return "redirect:" + session.getAttribute("url");
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
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        String cif = session.getAttribute("cif");
        if(tipo.equals(Perfiles.JF.getDescripcion())) {
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
    @RequestMapping("/listDisponibilidad")
    public String listDisponibilidadTrabajadoresEmpresa(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        String cif = user.getCif();
        if(tipo.equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("alumnos", trabajadorDao.getDisponibilidadTrabajadores(cif));
            return "trabajador/listDisponibilidad";

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
    @RequestMapping("/listEmails")
    public String listDisponibilidadTrabajadoresEmpresa(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        String cif = user.getCif();

        if(tipo.equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("alumnos", trabajadorDao.getEmailTrabajadores(cif));
            return "trabajador/listaemails";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }

    /**
     * Vista para ver los datos de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String verDisponibilidadTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
        {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        if(tipo.equals(Perfiles.JF.getDescripcion()) || tipo.equals(Perfiles.TR.getDescripcion())) {
            String username = user.getEmail();
            model.addAttribute("templates/trabajador", trabajadorDao.getDisponibilidadTrabajador(username));
            return "trabajador/disponibilidadTrabajador";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

}
