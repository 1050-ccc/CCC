package es.uji.ei1050.ccc.controller;

import es.uji.ei1050.ccc.daos.HorarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/horario")
public class HorarioController {

    private HorarioDAO horarioDao;

    @Autowired
    public void setHorarioDao(HorarioDAO horarioDao) {
        this.horarioDao = horarioDao;
    }



}
