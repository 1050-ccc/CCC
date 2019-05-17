package es.uji.ei1050.ccc.controller;


import es.uji.ei1050.ccc.daos.NotificacionDAO;
import es.uji.ei1050.ccc.model.Notificacion;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notificacion")
public class NotificacionController {

    private NotificacionDAO notificacionDao;


    @Autowired
    public void setUsuariosDAO(NotificacionDAO notificacionDao) {
        this.notificacionDao = notificacionDao;
    }

    /**
     * Lista trabajadores.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/lista")
    public String listTrabajadoresEmpresa(HttpSession session, Model model) {

        if (session.getAttribute("usuario") == null) {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();

        String dni= (String) session.getAttribute("DNI");

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("notificaciones", notificacionDao.getNotificaciones(dni));
            return "notificacion/lista";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/jefe";
        }
    }



}
