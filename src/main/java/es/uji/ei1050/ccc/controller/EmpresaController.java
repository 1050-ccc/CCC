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
@RequestMapping("/empresa")
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
    @RequestMapping(value = "/informacion", method = RequestMethod.GET)
    public String verInformacionTrabajador(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();

        String username = user.getEmail();
        String cif = (String) session.getAttribute("CIF");

        if (tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("empresa", empresaDao.getEmpresa(cif));
            return "empresa/informacionparajefe";
        } else {

            if (tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {
                model.addAttribute("empresa", empresaDao.getEmpresa(cif));
                return "empresa/informacionparatrabajador";
            }else{
                model.addAttribute("error", "No tienes permiso para acceder a este sitio");
                return "redirect:" + session.getAttribute("url");
            }


        }
    }

    //Métodos editar empresa

    /**
     * Vista para actualizar los datos de una empresa.
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/update/{cif}", method = RequestMethod.GET)
    public String updateTrabajador(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if (tipo.getDescripcion().equals(Perfiles.JF.getDescripcion()) || tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {
            String cif = (String) session.getAttribute("CIF");
            model.addAttribute("empresa", empresaDao.getEmpresa(cif));
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


        Usuario user = (Usuario) session.getAttribute("usuario");
        String cif = (String) session.getAttribute("usuario");
        empresa = empresaDao.getEmpresa(cif);
        Perfiles tipo = user.getTipo();


        if (tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            empresaDao.updateEmpresa(empresa);
            return "informacionparajefe";
        } else {
            return "redirect:" + session.getAttribute("url");
        }
    }

}
