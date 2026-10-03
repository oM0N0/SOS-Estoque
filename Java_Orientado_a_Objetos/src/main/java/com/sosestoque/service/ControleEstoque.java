package com.sosestoque.service;

import com.sosestoque.model.Movimentacao;
import com.sosestoque.model.Produto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControleEstoque {

    private final List<Produto> produtos = new ArrayList<>();
    private final List<Movimentacao> historico = new ArrayList<>();

    public void cadastrarProduto(Produto produto) {
        validarProduto(produto);

        if (buscarPorCodigo(produto.getCodigo()) != null) {
            throw new IllegalArgumentException("Já existe um produto com esse código.");
        }

        produto.setNome(produto.getNome().trim());
        produto.setCategoria(produto.getCategoria().trim());
        produtos.add(produto);
    }

    public void registrarMovimentacao(int codigo, int quantidade, String tipo) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        String tipoNormalizado = normalizarTipo(tipo);

        if (!tipoNormalizado.equals("entrada") && !tipoNormalizado.equals("saida")) {
            throw new IllegalArgumentException("O tipo deve ser entrada ou saída.");
        }

        Produto produto = buscarPorCodigo(codigo);

        if (produto == null) {
            throw new IllegalArgumentException("Não há produto cadastrado com este código.");
        }

        if (tipoNormalizado.equals("entrada")) {
            produto.adicionarQuantidade(quantidade);
        } else {
            produto.removerQuantidade(quantidade);
        }

        historico.add(new Movimentacao(
                produto,
                tipoNormalizado,
                quantidade,
                produto.getQuantidade()
        ));
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }

    public int totalProdutos() {
        return produtos.size();
    }

    public Produto buscarPorCodigo(int codigo) {
        return produtos.stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    public List<Produto> buscar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarProdutos();
        }

        String texto = termo.toLowerCase();

        return produtos.stream()
                .filter(p ->
                        String.valueOf(p.getCodigo()).contains(texto)
                                || p.getNome().toLowerCase().contains(texto)
                                || p.getCategoria().toLowerCase().contains(texto))
                .collect(Collectors.toList());
    }

    public List<Produto> verificarEstoqueCritico() {
        return produtos.stream()
                .filter(Produto::isEstoqueCritico)
                .collect(Collectors.toList());
    }

    public double calcularValorEstoque() {
        return produtos.stream()
                .mapToDouble(Produto::getValorTotal)
                .sum();
    }

    public List<Movimentacao> listarHistorico() {
        return new ArrayList<>(historico);
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) {
            return "";
        }
        return tipo.trim().toLowerCase().replace("í", "i");
    }

    private void validarProduto(Produto produto) {
        if (produto.getCodigo() <= 0) {
            throw new IllegalArgumentException("O código deve ser maior que zero.");
        }

        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }

        if (produto.getCategoria() == null || produto.getCategoria().isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }

        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        if (produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade inicial não pode ser negativa.");
        }

        if (produto.getEstoqueMinimo() < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }
    }
}
