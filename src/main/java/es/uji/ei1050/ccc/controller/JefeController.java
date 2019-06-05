package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.JefeDAO;
import es.uji.ei1050.ccc.daos.PersoneDAO;
import es.uji.ei1050.ccc.daos.TrabajadorDAO;
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
    private TrabajadorDAO trabajadorDao;

    @Autowired
    public void setPersoneDao(PersoneDAO personeDAO) {
        this.personeDAO = personeDAO;
    }

    @Autowired
    public void setJefeDao(JefeDAO jefeDao) {
        this.jefeDao = jefeDao;
    }

    @Autowired
    public void setTrabajadorDAODao(TrabajadorDAO trabajadorDao) {
        this.trabajadorDao = trabajadorDao;
    }

    @RequestMapping("") //load the template
    public String load_template(HttpSession session, Model model) {
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


    @RequestMapping(value = "/informacion", method = RequestMethod.GET)
    public String verInformacionJefe(HttpSession session, Model model) {

        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
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
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
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
