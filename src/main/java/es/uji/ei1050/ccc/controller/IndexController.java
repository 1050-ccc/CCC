package es.uji.ei1050.ccc.controller;

import javax.servlet.http.HttpSession;

import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controlador del index
 */
@Controller
public class IndexController {
    /**
     * Redirige a la vista correspondiente
     * Si no hay un usuario logeado lo manda al login
     * Si hay un usuario logeado lo manda a la vista del menú que le corresponda
     * @param session
     * @param model
     * @return vista html
     */
    @RequestMapping("/")
    public String login(HttpSession session, Model model) {
        //COMPROBACION DE USUARIO LOGEADO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }

        //Redirigir si eres admin
        if (((Usuario) session.getAttribute("usuario")).getUsuario().equals("admin"))
            return "index.html";


        //Redirigir si eres Jefe a tu menú correspondiente
        if (((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF))
            return "jefe/index.html";
        //Redirigir si eres Trabajador a tu menú correspondiente
        if (((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR))
            return "redirect:/trabajador/index";

        //
        return "index.html";
    }

}
