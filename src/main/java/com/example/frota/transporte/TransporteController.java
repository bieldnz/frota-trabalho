package com.example.frota.transporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frota.caixa.CaixaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/transporte")
public class TransporteController {

    @Autowired
    private TransporteService transporteService;

    @Autowired
    private CaixaService caixaService;

    // Suporta tanto /transporte quanto /transporte/listagem
    @GetMapping({"", "/listagem"})
    public String listarTransporte(Model model) {
        model.addAttribute("listaTransportes", transporteService.procurarTodos());
        return "transporte/listagem";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(@RequestParam(required = false) Long id, Model model) {
        CadastroTransporte dto;
        if (id != null) {
            Transporte existente = transporteService.procurarPorId(id)
                    .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado"));
            dto = new CadastroTransporte(
                    existente.getId(),
                    existente.getProduto() != null ? existente.getProduto() : "",
                    existente.getComprimento(),
                    existente.getLargura(),
                    existente.getAltura(),
                    existente.getCaixa() != null ? existente.getCaixa().getId() : null,
                    existente.getPeso(),
                    existente.getQuantidade(),
                    existente.getOrigem(),
                    existente.getDestino(),
                    existente.getValorFrete()
            );
        } else {
            dto = new CadastroTransporte(null, null, 0, 0, 0, null, 0, 0, null, null, 0);
        }
        model.addAttribute("transporte", dto);
        model.addAttribute("caixas", caixaService.procurarTodos());
        return "transporte/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("transporte") @Valid CadastroTransporte dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("caixas", caixaService.procurarTodos());
            return "transporte/formulario";
        }

        try {
            Transporte salvo = transporteService.salvarOuAtualizar(dto);
            String mensagem = dto.id() != null ? "Transporte atualizado com sucesso!" : "Transporte cadastrado com sucesso!";
            redirectAttributes.addFlashAttribute("message", mensagem);
            return "redirect:/transporte";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/transporte/formulario" + (dto.id() != null ? "?id=" + dto.id() : "");
        }
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            transporteService.apagarPorId(id);
            redirectAttributes.addFlashAttribute("message", "Transporte " + id + " foi apagado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transporte";
    }
}