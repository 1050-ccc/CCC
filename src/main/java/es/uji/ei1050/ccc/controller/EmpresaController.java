package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.EmpresaDAO;
import es.uji.ei1050.ccc.model.Empresa;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/templates/empresa")
public class EmpresaController {

    private EmpresaDAO empresaDao;

    @Autowired
    public void setEmpresaDao(EmpresaDAO empresaDao) {
        this.empresaDao = empresaDao;
    }


    /**
     * Vista para ver los datos de una empresa.
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String verInformacionTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        if (tipo.equals(Perfiles.JF.getDescripcion()) || tipo.equals(Perfiles.TR.getDescripcion())) {
            String username = user.getEmail();
            String cif = user.getCIF();
            model.addAttribute("templates/empresa", empresaDao.getEmpresa(cif));
            return "empresa/informacion";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    //Métodos editar trabajador

    /**
     * Vista para actualizar los datos de una empresa.
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/update/{cif}", method = RequestMethod.GET)
    public String updateTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("user");
        Perfiles tipo = user.getTipo();
        if (tipo.equals(Perfiles.JF.getDescripcion()) || tipo.equals(Perfiles.TR.getDescripcion())) {
            String cif = user.getCIF();
            model.addAttribute("templates/empresa", empresaDao.getEmpresa(cif));
            return "trabajador/update";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    /**
     * Método que actualiza los datos de la empresa.
     *
     * @param session
     * @param empresa
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@RequestBody String data, HttpSession session,
                                      @ModelAttribute("templates/empresa") Empresa empresa,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "empresa/update";


        Usuario user = (Usuario) session.getAttribute("user");
        empresa = empresaDao.getEmpresa(user.getCIF());
        Perfiles tipo = user.getTipo();


        if (tipo.equals(Perfiles.JF.getDescripcion())) {
            empresaDao.updateEmpresa(empresa);
            return "empresa/informacion";
        } else {
            return "redirect:" + session.getAttribute("url");
        }
    }

}
