package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.model.Contrato;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import es.uji.ei1050.ccc.daos.ContratoDAO;


@Controller
@RequestMapping("/contrato")
public class ContratoController {

    private ContratoDAO contratoDao;

    @Autowired
    public void setTrabajdoroDao(ContratoDAO contratoDao) {
        this.contratoDao = contratoDao;
    }


    /**
     * Vista para ver los datos del contrato de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/informacion", method = RequestMethod.GET)
    public String verInformacionTrabajador(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {
            String dni = (String) session.getAttribute("DNI");
            model.addAttribute("contrato", contratoDao.getContrato(dni));
            return "contrato/informacionparatrabajador";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    /**
     * Vista para ver los datos del contrato de un trabajador.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/informacion/{dni}", method = RequestMethod.GET)
    public String verContratoTrabajador(HttpSession session, Model model,  @PathVariable String dni) {

        if (session.getAttribute("usuario") == null) {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("contrato", contratoDao.getContrato(dni));
            return "contrato/informacionparajefe";

        }else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");

        }
    }

    //Métodos añadir un contrato

    /**
     * Vista para añadir un contrato.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/añadir") //TODO arreglar
    public String añadirContrato(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("contrato", new Contrato());

            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        //String dni = (String) session.getAttribute("DNI");

        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            Contrato contrato = new Contrato();
            //contrato.setPersone_dni(dni);
            model.addAttribute("contrato", contrato);
            return "contrato/añadir";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/" + session.getAttribute("url");
        }
    }

    /**
     * Método que añade el contrato.
     * @param contrato
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/añadir", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("contrato") Contrato contrato,
                                   BindingResult bindingResult) {


        contrato.setDiasVacaciones(30);
        if (bindingResult.hasErrors())
            return "contrato/añadir";
        contratoDao.addContrato(contrato);
        return "trabajador/lista";
    }

    /**
     * Método que borra un contrato
     * @param dni
     * @return
     */
    @RequestMapping(value = "/borrar/{dni}", method = RequestMethod.DELETE)
    public String processDelete(@PathVariable String dni) {
        contratoDao.deleteContrato(dni);
        return "redirect:../list";
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
            model.addAttribute("contrato", contratoDao.getContrato(dni));
            return "contrato/editar";
        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:" + session.getAttribute("url");
        }
    }

    /**
     * Método que actualiza los datos del trabajador.
     * @param session

     * @param contrato
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/editar/{dni}", method = RequestMethod.POST)
    public String processUpdateSubmit(@RequestBody String data, HttpSession session,
                                      @ModelAttribute("contrato") Contrato contrato, @PathVariable String dni,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "contrato/editar";


        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();


        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            contrato.setPersone_dni(dni);
            contratoDao.updateContrato(contrato);
            return "informacionparatrabajador";
        } else {
            return "redirect:" + session.getAttribute("url");
        }
    }
}
