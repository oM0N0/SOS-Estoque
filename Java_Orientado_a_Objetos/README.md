# SOS-Estoque - Java Orientado a Objetos

Implementação em Java orientado a objetos para a N2 do projeto SOS-Estoque.

## Tecnologias

- Java 17
- Spring Boot 3.5.15
- Maven
- Thymeleaf
- HTML/CSS

## Estrutura

- `model/Produto.java` - entidade Produto.
- `model/Movimentacao.java` - entidade do histórico.
- `service/ControleEstoque.java` - regras de negócio e gerenciamento das listas.
- `controller/EstoqueController.java` - comunicação entre interface web e serviço.
- `templates/index.html` - interface web.
- `static/css/style.css` - estilo da interface.

## Requisitos implementados

RF01 - Cadastro de produtos  
RF02 - Entradas e saídas  
RF03 - Consulta de inventário  
RF04 - Alerta de estoque crítico  
RF05 - Cálculo do valor do estoque  
RF06 - Busca por código, nome ou categoria  
RF07 - Histórico de movimentações  

RN01 - Código único  
RN02 - Não permite saldo negativo  
RN03 - Valores e quantidades válidos  
RN04 - Movimentação maior que zero e tipo válido  
RN05 - Movimentação somente de produto cadastrado  
RN06 - Produto crítico quando a quantidade é menor ou igual ao estoque mínimo  

Também são validados código positivo, nome obrigatório e categoria obrigatória.

## Como executar

No terminal, dentro da pasta do projeto:

```bash
mvn spring-boot:run
```

Depois abra:

`http://localhost:8080`

Os dados ficam em memória. Ao reiniciar a aplicação, os produtos e movimentações são zerados.

## Como executar os testes

No terminal, dentro da pasta do projeto:

```bash
mvn test
```

Os testes verificam cadastro, código duplicado, validações, entradas, saídas,
saldo insuficiente, busca, estoque crítico, valor total e histórico.
