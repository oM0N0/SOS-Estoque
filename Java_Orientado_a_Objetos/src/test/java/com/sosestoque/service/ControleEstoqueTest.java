package com.sosestoque.service;

import com.sosestoque.model.Movimentacao;
import com.sosestoque.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControleEstoqueTest {

    private ControleEstoque controle;

    @BeforeEach
    void preparar() {
        controle = new ControleEstoque();
        controle.cadastrarProduto(
                new Produto(101, "Arroz", "Alimentos", 25.90, 20, 5)
        );
    }

    @Test
    void deveCadastrarProduto() {
        controle.cadastrarProduto(
                new Produto(102, "Feijão", "Alimentos", 8.50, 10, 3)
        );

        assertEquals(2, controle.totalProdutos());
        assertEquals("Feijão", controle.buscarPorCodigo(102).getNome());
    }

    @Test
    void deveImpedirCodigoDuplicado() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> controle.cadastrarProduto(
                        new Produto(101, "Outro", "Teste", 10, 1, 0)
                )
        );

        assertTrue(erro.getMessage().contains("Já existe"));
    }

    @Test
    void deveValidarDadosDoProduto() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.cadastrarProduto(
                        new Produto(102, "", "Teste", 10, 1, 0)
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.cadastrarProduto(
                        new Produto(102, "Teste", "", 10, 1, 0)
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.cadastrarProduto(
                        new Produto(102, "Teste", "Categoria", 0, 1, 0)
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.cadastrarProduto(
                        new Produto(102, "Teste", "Categoria", 10, -1, 0)
                )
        );
    }

    @Test
    void deveRegistrarEntrada() {
        controle.registrarMovimentacao(101, 5, "entrada");

        assertEquals(25, controle.buscarPorCodigo(101).getQuantidade());
        assertEquals(1, controle.listarHistorico().size());
    }

    @Test
    void deveRegistrarSaidaComOuSemAcento() {
        controle.registrarMovimentacao(101, 2, "saída");
        controle.registrarMovimentacao(101, 3, "saida");

        assertEquals(15, controle.buscarPorCodigo(101).getQuantidade());
        assertEquals("saida", controle.listarHistorico().get(0).getTipo());
    }

    @Test
    void deveImpedirSaidaSemSaldo() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> controle.registrarMovimentacao(101, 21, "saida")
        );

        assertTrue(erro.getMessage().contains("suficientes"));
        assertEquals(20, controle.buscarPorCodigo(101).getQuantidade());
    }

    @Test
    void deveImpedirMovimentacaoInvalida() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.registrarMovimentacao(101, 0, "entrada")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.registrarMovimentacao(101, 1, "transferencia")
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> controle.registrarMovimentacao(999, 1, "entrada")
        );
    }

    @Test
    void deveBuscarProduto() {
        assertEquals(1, controle.buscar("arroz").size());
        assertEquals(1, controle.buscar("alimentos").size());
        assertEquals(1, controle.buscar("101").size());
        assertTrue(controle.buscar("inexistente").isEmpty());
    }

    @Test
    void deveIdentificarEstoqueCritico() {
        controle.registrarMovimentacao(101, 15, "saida");

        List<Produto> criticos = controle.verificarEstoqueCritico();
        assertEquals(1, criticos.size());
        assertEquals(101, criticos.get(0).getCodigo());
    }

    @Test
    void deveCalcularValorTotal() {
        controle.cadastrarProduto(
                new Produto(102, "Feijão", "Alimentos", 8.50, 10, 3)
        );

        assertEquals(603.0, controle.calcularValorEstoque(), 0.001);
    }

    @Test
    void deveManterHistorico() {
        controle.registrarMovimentacao(101, 2, "saida");

        List<Movimentacao> historico = controle.listarHistorico();
        assertEquals(1, historico.size());
        assertEquals(18, historico.get(0).getSaldoResultante());
    }
}
