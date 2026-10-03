import unittest

from main import (
    buscar_por_codigo,
    buscar_produtos,
    cadastrar_produto,
    calcular_valor_total_estoque,
    consultar_historico,
    registrar_movimentacao,
    verificar_estoque_critico,
)


class TesteSOSEstoque(unittest.TestCase):
    def setUp(self):
        self.estoque = []
        self.historico = []
        cadastrar_produto(
            self.estoque, 101, "Arroz", "Alimentos", 25.90, 20, 5
        )

    def test_cadastrar_produto(self):
        produto = cadastrar_produto(
            self.estoque, 102, "Feijão", "Alimentos", 8.50, 10, 3
        )
        self.assertEqual(produto["nome"], "Feijão")
        self.assertEqual(len(self.estoque), 2)

    def test_impedir_codigo_duplicado(self):
        with self.assertRaisesRegex(ValueError, "Já existe"):
            cadastrar_produto(
                self.estoque, 101, "Outro", "Teste", 10.0, 1, 0
            )

    def test_validar_dados_do_produto(self):
        with self.assertRaises(ValueError):
            cadastrar_produto(self.estoque, 102, "", "Teste", 10.0, 1, 0)
        with self.assertRaises(ValueError):
            cadastrar_produto(self.estoque, 102, "Teste", "", 10.0, 1, 0)
        with self.assertRaises(ValueError):
            cadastrar_produto(self.estoque, 102, "Teste", "Cat", 0, 1, 0)
        with self.assertRaises(ValueError):
            cadastrar_produto(self.estoque, 102, "Teste", "Cat", 10, -1, 0)

    def test_buscar_por_codigo(self):
        self.assertEqual(buscar_por_codigo(self.estoque, 101)["nome"], "Arroz")
        self.assertIsNone(buscar_por_codigo(self.estoque, 999))

    def test_registrar_entrada(self):
        movimento = registrar_movimentacao(
            self.estoque, self.historico, 101, 5, "entrada", "25/09/2026"
        )
        self.assertEqual(self.estoque[0]["quantidade"], 25)
        self.assertEqual(movimento["saldo_resultante"], 25)

    def test_registrar_saida(self):
        registrar_movimentacao(
            self.estoque, self.historico, 101, 4, "saida", "25/09/2026"
        )
        self.assertEqual(self.estoque[0]["quantidade"], 16)

    def test_impedir_saida_sem_saldo(self):
        with self.assertRaisesRegex(ValueError, "insuficiente"):
            registrar_movimentacao(
                self.estoque, self.historico, 101, 21, "saida"
            )

    def test_impedir_movimentacao_invalida(self):
        with self.assertRaises(ValueError):
            registrar_movimentacao(
                self.estoque, self.historico, 101, 0, "entrada"
            )
        with self.assertRaises(ValueError):
            registrar_movimentacao(
                self.estoque, self.historico, 101, 1, "transferencia"
            )
        with self.assertRaisesRegex(ValueError, "não encontrado"):
            registrar_movimentacao(
                self.estoque, self.historico, 999, 1, "entrada"
            )

    def test_buscar_produtos(self):
        self.assertEqual(len(buscar_produtos(self.estoque, "arroz")), 1)
        self.assertEqual(len(buscar_produtos(self.estoque, "alimentos")), 1)
        self.assertEqual(len(buscar_produtos(self.estoque, "101")), 1)
        self.assertEqual(buscar_produtos(self.estoque, "inexistente"), [])

    def test_estoque_critico(self):
        registrar_movimentacao(
            self.estoque, self.historico, 101, 15, "saida"
        )
        self.assertEqual(verificar_estoque_critico(self.estoque)[0]["codigo"], 101)

    def test_calcular_valor_total(self):
        cadastrar_produto(
            self.estoque, 102, "Feijão", "Alimentos", 8.50, 10, 3
        )
        self.assertAlmostEqual(calcular_valor_total_estoque(self.estoque), 603.0)

    def test_consultar_historico(self):
        registrar_movimentacao(
            self.estoque, self.historico, 101, 2, "saida", "25/09/2026"
        )
        resultado = consultar_historico(self.historico)
        self.assertEqual(len(resultado), 1)
        self.assertEqual(resultado[0]["tipo"], "saida")


if __name__ == "__main__":
    unittest.main()
