package com.sosestoque.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimentacao {

    private Produto produto;
    private String tipo;
    private int quantidadeMovimentada;
    private LocalDateTime data;
    private int saldoResultante;

    public Movimentacao(Produto produto, String tipo,
                        int quantidadeMovimentada, int saldoResultante) {
        this.produto = produto;
        this.tipo = tipo;
        this.quantidadeMovimentada = quantidadeMovimentada;
        this.data = LocalDateTime.now();
        this.saldoResultante = saldoResultante;
    }

    public Produto getProduto() {
        return produto;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidadeMovimentada() {
        return quantidadeMovimentada;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getDataFormatada() {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public int getSaldoResultante() {
        return saldoResultante;
    }
}
