package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.HorarioDAO;
import es.uji.ei1050.ccc.daos.TrabajadorDAO;
import es.uji.ei1050.ccc.model.Horario;
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
import java.util.List;

@Controller
@RequestMapping("/horario")
public class HorarioController {

    private HorarioDAO horarioDAO;
    private TrabajadorDAO trabajadorDao;


    @Autowired
    public void setHorarioDAO(HorarioDAO horarioDAO) {
        this.horarioDAO = horarioDAO;
    }

    @Autowired
    public void setTrabajadorDao(TrabajadorDAO trabajadorDao) {
        this.trabajadorDao = trabajadorDao;
    }


    @RequestMapping("") //load the template
    public String load_template(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {

            List<Horario> calendario = horarioDAO.getHorarioTrabajadores((String) session.getAttribute("CIF"));
            String[][] calAux = new String[calendario.size()][];
            for (int i = 0; i < calendario.size(); i++) {
                Horario horarioAux = calendario.get(i);
                String[] dia = horarioAux.getDia().toString().split("-");
                //año, mes, dia, horaInicio, horaFin, nombre
                String aux = new StringBuilder().append(dia[0]).append("#").append(dia[1]).append("#").append(dia[2]).append("#").append(horarioAux.getHoraInicio().toString().split(":")[0]).append("#").append(horarioAux.getHoraFin().toString().split(":")[0]).append("#").append(horarioAux.getPersoneNombre()).toString();
                calAux[i] = aux.split("#");
            }

            model.addAttribute("calendario", calAux);
            return "horario/horario";
        }

        if (((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {

            List<Horario> calendario = horarioDAO.getHorarioTrabajador((String) session.getAttribute("DNI"));
            String[][] calAux = new String[calendario.size()][];
            for (int i = 0; i < calendario.size(); i++) {
                Horario horarioAux = calendario.get(i);
                String[] dia = horarioAux.getDia().toString().split("-");
                //año, mes, dia, horaInicio, horaFin, nombre
                String aux = new StringBuilder().append(dia[0]).append("#").append(dia[1]).append("#").append(dia[2]).append("#").append(horarioAux.getHoraInicio().toString().split(":")[0]).append("#").append(horarioAux.getHoraFin().toString().split(":")[0]).append("#").append(horarioAux.getPersoneNombre()).toString();
                calAux[i] = aux.split("#");
            }

            model.addAttribute("calendario", calAux);
            return "horario/horario";
        }

        return "redirect:/";

    }

    /**
     * Lista horas trabajadas.
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/listahoras")
    public String listaHorasTrabajadas(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        String dni = (String) session.getAttribute("DNI");

        Calendar fecha = Calendar.getInstance();
        int mes = fecha.get(Calendar.MONTH);
        int año = fecha.get(Calendar.YEAR);


        if(tipo.getDescripcion().equals(Perfiles.TR.getDescripcion())) {
            model.addAttribute("horarios", horarioDAO.getHorasTrabajadas(dni, (mes + 1), año));
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
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.JF)) {
            return "redirect:/";
        }

        Usuario user = (Usuario) session.getAttribute("usuario");
        Perfiles tipo = user.getTipo();
        //String dni = (String) session.getAttribute("DNI");

        Calendar fecha = Calendar.getInstance();
        int mes = fecha.get(Calendar.MONTH);
        int año = fecha.get(Calendar.YEAR);


        if(tipo.getDescripcion().equals(Perfiles.JF.getDescripcion())) {
            model.addAttribute("horarios", horarioDAO.getHorasTrabajadas(dni, (mes + 1), año));
            return "horario/listahorasparajefe";

        } else {
            model.addAttribute("error", "No tienes permiso para acceder a este sitio");
            return "redirect:/trabajador";
        }
    }

    @RequestMapping("/jornada")
    public String consultaJornadaTrababajo(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {
            return "redirect:/";
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
        return "";
    }

    @RequestMapping("/consulta")
    public String consultaHorasTrabajadas(HttpSession session, Model model) {
        // COMPROBACION DE USUARIO LOGEADO Y DEL USUARIO CORRECTO
        if (session.getAttribute("usuario") == null) {
            return "redirect:/usuario/login";
        }
        if (!((Usuario) session.getAttribute("usuario")).getTipo().equals(Perfiles.TR)) {
            return "redirect:/";
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
