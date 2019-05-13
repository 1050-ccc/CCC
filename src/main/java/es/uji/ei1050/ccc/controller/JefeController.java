package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.JefeDAO;
import es.uji.ei1050.ccc.daos.PersoneDAO;
import es.uji.ei1050.ccc.model.Jefe;
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
@RequestMapping("/jefe")
public class JefeController {

    private JefeDAO jefeDao;
    private PersoneDAO personeDAO;

    @Autowired
    public void setPersoneDao(PersoneDAO personeDAO) {
        this.personeDAO = personeDAO;
    }

    @Autowired
    public void setJefeDao(JefeDAO jefeDao) {
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

            return "jefe/index";
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

    //Métodos editar trabajador

    /**
     * Vista para actualizar los datos de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/editar/{dni}", method = RequestMethod.GET)
    public String updateTrabajador(HttpSession session, Model model, @PathVariable String dni) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("jefe", jefeDao.getJefeByDNI(dni));
            return "jefe/editar";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    /**
     * Método que actualiza los datos del trabajador.
     * @param session

     * @param jefe
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/editar/{dni}", method = RequestMethod.POST)
    public String processUpdateSubmit(@RequestBody String data, HttpSession session,
                                      @ModelAttribute("jefe") Jefe jefe,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "jefe/editar";


        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();

        personeDAO.updatePersone(jefe);
        //jefeDao.updateJefe(jefe);

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())){
            return "redirect:../informacion";
        }else{
                return "redirect:" + session.getAttribute("url");

        }

    }
}
