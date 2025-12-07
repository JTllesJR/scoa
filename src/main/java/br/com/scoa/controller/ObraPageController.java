package br.com.scoa.controller;

import br.com.scoa.model.Obra;
import br.com.scoa.service.ObraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/obras")
public class ObraPageController {

    private final ObraService obraService;

    public ObraPageController(ObraService obraService) {
        this.obraService = obraService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("obras", obraService.listarTodas());
        return "obras/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("obra", new Obra());
        return "obras/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Obra obra, Model model) {
        try {
            obraService.salvar(obra);
        } catch (RuntimeException e) {
            model.addAttribute("obra", obra);
            model.addAttribute("erro", e.getMessage());
            return "obras/form";
        }
        return "redirect:/obras";
    }
}
