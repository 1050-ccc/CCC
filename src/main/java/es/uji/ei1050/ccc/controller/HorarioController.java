package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.HorarioDAO;
import es.uji.ei1050.ccc.daos.TrabajadorDAO;
import es.uji.ei1050.ccc.model.Perfiles;
import es.uji.ei1050.ccc.model.Trabajador;
import es.uji.ei1050.ccc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Calendar;

@Controller
@RequestMapping("/horario")
public class HorarioController {

    private HorarioDAO horarioDao;
    private TrabajadorDAO trabajadorDao;


    @Autowired
    public void setHorarioDao(HorarioDAO horarioDao) {
        this.horarioDao = horarioDao;
    }

    @Autowired
    public void setTrabajadorDao(TrabajadorDAO trabajadorDao) {
        this.trabajadorDao = trabajadorDao;
    }

    /**
     * Lista horas trabajadas.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/listahoras")
    public String listaHorasTrabajadas(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String dni = (String) session.getAttribute("DNI");

        Calendar fecha = Calendar.getInstance();
        int mes = fecha.get(Calendar.MONTH);
        int año = fecha.get(Calendar.YEAR);


        if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {
            model.addAttribute("horarios", horarioDao.getHorasTrabajadas(dni, (mes + 1), año));
            return "horario/listahorasparatrabajador";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }

    /**
     * Lista horas trabajadas.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/listahoras/{dni}")
    public String listaHorasTrabajadasTrabajador(HttpSession session, Model model, @PathVariable String dni) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        //String dni = (String) session.getAttribute("DNI");

        Calendar fecha = Calendar.getInstance();
        int mes = fecha.get(Calendar.MONTH);
        int año = fecha.get(Calendar.YEAR);


        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("horarios", horarioDao.getHorasTrabajadas(dni, (mes + 1), año));
            return "horario/listahorasparajefe";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }

    @RequestMapping("/jornada")
    public String consultaJornadaTrababajo(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();


        if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {

            String dni = (String) session.getAttribute("DNI");

            Trabajador trabajador = trabajadorDao.getTrabajadorByDNI(dni);

            if(trabajador.getTurno().equals("Mañana")){
                return "/mañana";
            }

            if(trabajador.getTurno().equals("Tarde")){
                return "horario/tarde";
            }

            if(trabajador.getTurno().equals("Noche")){
                return "horario/noche";
            }

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
        System.out.println("Espero que no llegue aquí");
        return "";
    }

    @RequestMapping("/consulta")
    public String consultaHorasTrabajadas(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null)
        {
            model.addAttribute("usuario", new Usuario());
            return "login";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();


        if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {
            return "horario/consulta";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }

}
