# SOS-Estoque - Python Procedural

Aplicação de terminal para controle de estoque, implementada em Python conforme
o paradigma procedural. Os dados são armazenados em listas e dicionários e as
operações são executadas por funções.

## Pré-requisitos

- Python 3.10 ou superior.
- Nenhuma biblioteca externa é necessária.

## Como executar

1. Abra o terminal na pasta `sos_estoque_python`.
2. Execute:

```bash
python main.py
```

No Windows, se o comando acima não funcionar, tente:

```bash
py main.py
```

## Funcionalidades

- Cadastro de produtos.
- Registro de entradas e saídas.
- Consulta do inventário.
- Busca por código, nome ou categoria.
- Identificação de estoque crítico.
- Cálculo do valor total do estoque.
- Histórico de movimentações.

## Regras principais

- O código do produto deve ser único e maior que zero.
- Nome e categoria são obrigatórios.
- O preço deve ser maior que zero.
- Quantidade inicial e estoque mínimo não podem ser negativos.
- A quantidade movimentada deve ser maior que zero.
- Uma saída não pode ultrapassar o saldo disponível.
- Um produto é crítico quando seu saldo é menor ou igual ao estoque mínimo.

## Como executar os testes

Na pasta do projeto, execute:

```bash
python -m unittest -v
```

Ou, no Windows:

```bash
py -m unittest -v
```

## Observação

Os dados permanecem apenas em memória e são apagados quando o programa é
encerrado. Essa limitação faz parte do escopo acadêmico definido para o projeto.
