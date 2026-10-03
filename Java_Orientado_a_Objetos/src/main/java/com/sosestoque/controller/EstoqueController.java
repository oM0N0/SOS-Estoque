package com.sosestoque.controller;

import com.sosestoque.model.Produto;
import com.sosestoque.service.ControleEstoque;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EstoqueController {

    private final ControleEstoque controleEstoque;

    public EstoqueController(ControleEstoque controleEstoque) {
        this.controleEstoque = controleEstoque;
    }

    @GetMapping("/")
    public String inicio(
            @RequestParam(required = false) String busca,
            Model model) {

        model.addAttribute("produtos", controleEstoque.buscar(busca));
        model.addAttribute("historico", controleEstoque.listarHistorico());
        model.addAttribute("criticos", controleEstoque.verificarEstoqueCritico());
        model.addAttribute("valorEstoque", controleEstoque.calcularValorEstoque());
        model.addAttribute("totalProdutos", controleEstoque.totalProdutos());
        model.addAttribute("busca", busca == null ? "" : busca);

        return "index";
    }

    @PostMapping("/produtos")
    public String cadastrarProduto(
            @RequestParam int codigo,
            @RequestParam String nome,
            @RequestParam String categoria,
            @RequestParam double preco,
            @RequestParam int quantidade,
            @RequestParam int estoqueMinimo,
            Model model) {

        try {
            Produto produto = new Produto(
                    codigo, nome, categoria, preco, quantidade, estoqueMinimo
            );

            controleEstoque.cadastrarProduto(produto);
            model.addAttribute("mensagem", "Produto cadastrado com sucesso.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
        }

        carregarDados(model, "");
        return "index";
    }

    @PostMapping("/movimentacoes")
    public String movimentar(
            @RequestParam int codigo,
            @RequestParam int quantidade,
            @RequestParam String tipo,
            Model model) {

        try {
            controleEstoque.registrarMovimentacao(codigo, quantidade, tipo);
            model.addAttribute("mensagem", "Movimentação registrada com sucesso.");
        } catch (IllegalArgumentException e) {
            // Repassa exatamente a mensagem enviada pelo Service (produto não encontrado ou saldo insuficiente)
            model.addAttribute("erro", e.getMessage());
        }

        carregarDados(model, "");
        return "index";
    }

    private void carregarDados(Model model, String busca) {
        model.addAttribute("produtos", controleEstoque.buscar(busca));
        model.addAttribute("historico", controleEstoque.listarHistorico());
        model.addAttribute("criticos", controleEstoque.verificarEstoqueCritico());
        model.addAttribute("valorEstoque", controleEstoque.calcularValorEstoque());
        model.addAttribute("totalProdutos", controleEstoque.totalProdutos());
        model.addAttribute("busca", busca);
    }
}
