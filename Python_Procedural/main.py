"""SOS-Estoque - versão Python utilizando o paradigma procedural."""

from datetime import datetime


def buscar_por_codigo(estoque, codigo):
    """Retorna o produto com o código informado ou None quando não existe."""
    for produto in estoque:
        if produto["codigo"] == codigo:
            return produto
    return None


def cadastrar_produto(
    estoque, codigo, nome, categoria, preco, quantidade, estoque_minimo
):
    """Valida os dados e adiciona um novo produto ao estoque."""
    nome = nome.strip()
    categoria = categoria.strip()

    if codigo <= 0:
        raise ValueError("O código deve ser maior que zero.")
    if buscar_por_codigo(estoque, codigo) is not None:
        raise ValueError("Já existe um produto cadastrado com esse código.")
    if not nome:
        raise ValueError("O nome do produto não pode estar vazio.")
    if not categoria:
        raise ValueError("A categoria do produto não pode estar vazia.")
    if preco <= 0:
        raise ValueError("O preço deve ser maior que zero.")
    if quantidade < 0:
        raise ValueError("A quantidade inicial não pode ser negativa.")
    if estoque_minimo < 0:
        raise ValueError("O estoque mínimo não pode ser negativo.")

    produto = {
        "codigo": codigo,
        "nome": nome,
        "categoria": categoria,
        "preco": preco,
        "quantidade": quantidade,
        "estoque_minimo": estoque_minimo,
    }
    estoque.append(produto)
    return produto


def registrar_movimentacao(
    estoque, historico, codigo, quantidade, tipo, data=None
):
    """Registra uma entrada ou saída e atualiza o saldo do produto."""
    tipo = tipo.strip().lower()

    if quantidade <= 0:
        raise ValueError("A quantidade movimentada deve ser maior que zero.")
    if tipo not in ("entrada", "saida"):
        raise ValueError("O tipo da movimentação deve ser 'entrada' ou 'saida'.")

    produto = buscar_por_codigo(estoque, codigo)
    if produto is None:
        raise ValueError("Produto não encontrado.")

    if tipo == "saida" and quantidade > produto["quantidade"]:
        raise ValueError("Quantidade insuficiente no estoque.")

    if tipo == "entrada":
        produto["quantidade"] += quantidade
    else:
        produto["quantidade"] -= quantidade

    movimentacao = {
        "codigo_produto": produto["codigo"],
        "nome_produto": produto["nome"],
        "tipo": tipo,
        "quantidade_movimentada": quantidade,
        "data": data or datetime.now().strftime("%d/%m/%Y %H:%M:%S"),
        "saldo_resultante": produto["quantidade"],
    }
    historico.append(movimentacao)
    return movimentacao


def consultar_inventario(estoque):
    """Retorna os produtos em ordem crescente de código."""
    return sorted(estoque, key=lambda produto: produto["codigo"])


def buscar_produtos(estoque, termo):
    """Busca produtos por código, nome ou categoria."""
    termo_normalizado = str(termo).strip().lower()
    if not termo_normalizado:
        return []

    encontrados = []
    for produto in estoque:
        if (
            termo_normalizado in str(produto["codigo"]).lower()
            or termo_normalizado in produto["nome"].lower()
            or termo_normalizado in produto["categoria"].lower()
        ):
            encontrados.append(produto)
    return encontrados


def verificar_estoque_critico(estoque):
    """Retorna produtos com saldo menor ou igual ao estoque mínimo."""
    return [
        produto
        for produto in estoque
        if produto["quantidade"] <= produto["estoque_minimo"]
    ]


def calcular_valor_total_estoque(estoque):
    """Calcula o valor financeiro total armazenado no estoque."""
    return sum(
        produto["preco"] * produto["quantidade"] for produto in estoque
    )


def consultar_historico(historico):
    """Retorna as movimentações na ordem em que foram registradas."""
    return list(historico)


def ler_inteiro(mensagem):
    """Solicita um número inteiro e repete enquanto a entrada for inválida."""
    while True:
        try:
            return int(input(mensagem))
        except ValueError:
            print("Erro: informe um número inteiro válido.")


def ler_decimal(mensagem):
    """Solicita um número decimal, aceitando ponto ou vírgula."""
    while True:
        try:
            valor = input(mensagem).strip().replace(",", ".")
            return float(valor)
        except ValueError:
            print("Erro: informe um valor numérico válido.")


def formatar_moeda(valor):
    """Formata um número no padrão monetário brasileiro."""
    texto = f"{valor:,.2f}"
    texto = texto.replace(",", "TEMP").replace(".", ",").replace("TEMP", ".")
    return f"R$ {texto}"


def exibir_produtos(produtos):
    """Exibe uma lista de produtos de forma tabular no terminal."""
    if not produtos:
        print("Nenhum produto encontrado.")
        return

    print("\nCÓDIGO | NOME                 | CATEGORIA        | PREÇO        | QTD | MÍN.")
    print("-" * 80)
    for produto in produtos:
        print(
            f"{produto['codigo']:<6} | "
            f"{produto['nome'][:20]:<20} | "
            f"{produto['categoria'][:16]:<16} | "
            f"{formatar_moeda(produto['preco']):>12} | "
            f"{produto['quantidade']:<3} | "
            f"{produto['estoque_minimo']}"
        )


def exibir_historico(historico):
    """Exibe as movimentações registradas."""
    if not historico:
        print("Nenhuma movimentação registrada.")
        return

    print("\nDATA                | CÓD. | PRODUTO              | TIPO    | QTD | SALDO")
    print("-" * 80)
    for movimento in historico:
        print(
            f"{movimento['data']:<19} | "
            f"{movimento['codigo_produto']:<4} | "
            f"{movimento['nome_produto'][:20]:<20} | "
            f"{movimento['tipo']:<7} | "
            f"{movimento['quantidade_movimentada']:<3} | "
            f"{movimento['saldo_resultante']}"
        )


def opcao_cadastrar(estoque):
    print("\n--- CADASTRO DE PRODUTO ---")
    codigo = ler_inteiro("Código: ")
    nome = input("Nome: ")
    categoria = input("Categoria: ")
    preco = ler_decimal("Preço unitário: R$ ")
    quantidade = ler_inteiro("Quantidade inicial: ")
    estoque_minimo = ler_inteiro("Estoque mínimo: ")

    produto = cadastrar_produto(
        estoque, codigo, nome, categoria, preco, quantidade, estoque_minimo
    )
    print(f"Produto '{produto['nome']}' cadastrado com sucesso.")


def opcao_movimentar(estoque, historico, tipo):
    titulo = "ENTRADA" if tipo == "entrada" else "SAÍDA"
    print(f"\n--- REGISTRAR {titulo} ---")
    codigo = ler_inteiro("Código do produto: ")
    quantidade = ler_inteiro("Quantidade: ")
    movimento = registrar_movimentacao(
        estoque, historico, codigo, quantidade, tipo
    )
    print(
        f"{titulo.capitalize()} registrada. "
        f"Saldo atual: {movimento['saldo_resultante']}."
    )


def exibir_menu():
    print("\n" + "=" * 43)
    print("               SOS-ESTOQUE")
    print("=" * 43)
    print("1 - Cadastrar produto")
    print("2 - Registrar entrada")
    print("3 - Registrar saída")
    print("4 - Consultar inventário")
    print("5 - Buscar produto")
    print("6 - Verificar estoque crítico")
    print("7 - Calcular valor total do estoque")
    print("8 - Consultar histórico")
    print("0 - Sair")
    print("=" * 43)


def main():
    estoque = []
    historico = []

    while True:
        exibir_menu()
        opcao = input("Escolha uma opção: ").strip()

        try:
            if opcao == "1":
                opcao_cadastrar(estoque)
            elif opcao == "2":
                opcao_movimentar(estoque, historico, "entrada")
            elif opcao == "3":
                opcao_movimentar(estoque, historico, "saida")
            elif opcao == "4":
                exibir_produtos(consultar_inventario(estoque))
            elif opcao == "5":
                termo = input("Código, nome ou categoria: ")
                exibir_produtos(buscar_produtos(estoque, termo))
            elif opcao == "6":
                print("\n--- PRODUTOS COM ESTOQUE CRÍTICO ---")
                exibir_produtos(verificar_estoque_critico(estoque))
            elif opcao == "7":
                total = calcular_valor_total_estoque(estoque)
                print(f"Valor total do estoque: {formatar_moeda(total)}")
            elif opcao == "8":
                exibir_historico(consultar_historico(historico))
            elif opcao == "0":
                print("Sistema encerrado.")
                break
            else:
                print("Erro: opção inválida.")
        except ValueError as erro:
            print(f"Erro: {erro}")


if __name__ == "__main__":
    main()
