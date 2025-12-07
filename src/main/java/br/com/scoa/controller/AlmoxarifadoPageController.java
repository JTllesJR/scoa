package br.com.scoa.controller;

import br.com.scoa.model.ItemAlmoxarifado;
import br.com.scoa.service.ItemAlmoxarifadoService;
import br.com.scoa.service.MovimentacaoAlmoxarifadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/almoxarifado")
public class AlmoxarifadoPageController {

    private final ItemAlmoxarifadoService itemService;
    private final MovimentacaoAlmoxarifadoService movimentacaoService;

    public AlmoxarifadoPageController(ItemAlmoxarifadoService itemService,
                                      MovimentacaoAlmoxarifadoService movimentacaoService) {
        this.itemService = itemService;
        this.movimentacaoService = movimentacaoService;
    }

    // LISTA DE ITENS: /almoxarifado
    @GetMapping
    public String listarItens(Model model) {
        model.addAttribute("itens", itemService.listarTodos());
        return "almoxarifado/itens";
    }

    // FORM NOVO ITEM: /almoxarifado/itens/novo
    @GetMapping("/itens/novo")
    public String novoItem(Model model) {
        model.addAttribute("item", new ItemAlmoxarifado());
        return "almoxarifado/item-form";
    }

    // SALVAR ITEM: POST /almoxarifado/itens
    @PostMapping("/itens")
    public String salvarItem(@ModelAttribute ItemAlmoxarifado item, Model model) {
        try {
            itemService.salvar(item);
            return "redirect:/almoxarifado";
        } catch (RuntimeException e) {
            model.addAttribute("item", item);
            model.addAttribute("erro", e.getMessage());
            return "almoxarifado/item-form";
        }
    }

    // FORM DE MOVIMENTAÇÃO: /almoxarifado/movimentar/{itemId}
    @GetMapping("/movimentar/{itemId}")
    public String movimentarForm(@PathVariable Long itemId, Model model) {
        ItemAlmoxarifado item = itemService.buscarPorId(itemId);
        model.addAttribute("item", item);
        return "almoxarifado/movimentar";
    }

    // SALVAR MOVIMENTAÇÃO: POST /almoxarifado/movimentar
    @PostMapping("/movimentar")
    public String movimentar(@RequestParam("itemId") Long itemId,
                             @RequestParam("tipo") String tipo,
                             @RequestParam("quantidade") Integer quantidade,
                             @RequestParam(value = "observacao", required = false) String observacao,
                             Model model) {

        try {
            movimentacaoService.registrarMovimentacao(itemId, tipo, quantidade, observacao);
            return "redirect:/almoxarifado";
        } catch (RuntimeException e) {
            ItemAlmoxarifado item = itemService.buscarPorId(itemId);
            model.addAttribute("item", item);
            model.addAttribute("erro", e.getMessage());
            return "almoxarifado/movimentar";
        }
    }

    // LISTA DE MOVIMENTAÇÕES: /almoxarifado/movimentacoes
    @GetMapping("/movimentacoes")
    public String listarMovimentacoes(@RequestParam(value = "itemId", required = false) Long itemId,
                                      Model model) {

        if (itemId != null) {
            model.addAttribute("movimentacoes", movimentacaoService.listarPorItem(itemId));
            model.addAttribute("filtroItem", itemService.buscarPorId(itemId));
        } else {
            model.addAttribute("movimentacoes", movimentacaoService.listarTodas());
            model.addAttribute("filtroItem", null);
        }

        model.addAttribute("itens", itemService.listarTodos());

        return "almoxarifado/movimentacoes";
    }
}
