package es.uji.ei1050.ccc.controller;


import es.uji.ei1050.ccc.daos.JefeDAO;
import es.uji.ei1050.ccc.daos.NotificacionDAO;
import es.uji.ei1050.ccc.model.Notificacion;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Persone;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notificacion")
public class NotificacionController {

    private NotificacionDAO notificacionDao;
    private JefeDAO jefeDAO;


    @Autowired
    public void setUsuariosDAO(NotificacionDAO notificacionDao) {
        this.notificacionDao = notificacionDao;
    }

    @Autowired
    public void setJefeDAO(JefeDAO jefeDAO){ this.jefeDAO = jefeDAO;}

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

        String cif= (String) session.getAttribute("CIF");

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("notificaciones", notificacionDao.getNotificaciones(cif));
            return "notificacion/lista";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/jefe";
        }
    }

    @RequestMapping(value = "/borrar/{idNotificacion}")
    public String processDelete(HttpSession session, @PathVariable int idNotificacion) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        notificacionDao.deleteNotificacion(idNotificacion);

        return "notificacion/lista";
    }

    /**
     * Vista para notificar.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/notificar")
    public String añadirTrabajador(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String cif = (String) session.getAttribute("CIF");

        model.addAttribute("notificacion", new Notificacion());
        return "trabajador/notificar";

    }

    /**
     * Método que añade el trabajador.
     * @param notificacion
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/notificar", method = RequestMethod.POST)
    public String processAddSubmit(HttpSession session, @ModelAttribute("usuario") Usuario usuario, @ModelAttribute("notificacion") Notificacion notificacion,
                                   BindingResult bindingResult) {


        if (bindingResult.hasErrors())
            return "redirect:/";

        notificacionDao.sendNotificacion(notificacion, ((Persone) jefeDAO.getJefeByCif((String) session.getAttribute("CIF"))).getDni());

        return "redirect:/";
    }

}
