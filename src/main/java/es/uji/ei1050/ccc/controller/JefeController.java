package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.JefeDAO;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/jefe")
public class JefeController {

    private JefeDAO jefeDao;

    @Autowired
    public void setJefeoDao(JefeDAO jefeDao) {
        this.jefeDao = jefeDao;
    }

    @RequestMapping("") //load the template
    public String load_template(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            return "index";
        }
        String url = (String) session.getAttribute("url");
        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("jefe", jefeDao.getJefeByEmail(user.getEmail()));

            return "jefe/principal";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + url;
        }
    }


    @RequestMapping(value = "/informacion", method = RequestMethod.GET)
    public String verInformacionJefe(HttpSession session, Model model) {

        if (session.getAttribute("usuario") == null) {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            String username = user.getEmail();
            model.addAttribute("jefe", jefeDao.getJefeByEmail(username));
            return "jefe/informacion";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }
}
