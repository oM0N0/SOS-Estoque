package com.sosestoque.model;

public class Produto {

    private int codigo;
    private String nome;
    private String categoria;
    private double preco;
    private int quantidade;
    private int estoqueMinimo;

    public Produto() {
    }

    public Produto(int codigo, String nome, String categoria, double preco,
                   int quantidade, int estoqueMinimo) {
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void adicionarQuantidade(int quantidadeEntrada) {
        if (quantidadeEntrada <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        quantidade += quantidadeEntrada;
    }

    public void removerQuantidade(int quantidadeSaida) {
        if (quantidadeSaida <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (quantidadeSaida > quantidade) {
            throw new IllegalArgumentException(
                    "Não há produtos suficientes disponíveis no estoque para realizar esta saída."
            );
        }
        quantidade -= quantidadeSaida;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public double getValorTotal() {
        return preco * quantidade;
    }

    public boolean isEstoqueCritico() {
        return quantidade <= estoqueMinimo;
    }
}
