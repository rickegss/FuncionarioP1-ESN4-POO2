# Sistema de Cadastro de Funcionários

Sistema de cadastro de funcionários feito em Java com interface JavaFX com Scene Builder, desenvolvido como avaliação P1 da disciplina de Programação Orientada a Objetos II (UNIFAN, Engenharia de Software). Os dados ficam salvos em um arquivo CSV e os relatórios são gerados com a API Stream.

Autor: Ricardo Gomes da Silva

## Funcionalidades

- Cadastrar funcionário com dados pessoais e endereço
- Listar todos os funcionários em uma tabela
- Buscar funcionário por matrícula
- Excluir funcionário (com confirmação)
- Relatórios:
  - Funcionários por cargo
  - Funcionários por faixa salarial
  - Média salarial por cargo
  - Funcionários agrupados por cidade/estado

## Tecnologias

- Java 17
- JavaFX 17.0.14 (controls e fxml)
- Maven 3.8+ (o projeto inclui o Maven Wrapper)

## Como executar

Clone o repositório e entre na pasta do projeto:

```
git clone https://github.com/rickegss/FuncionarioP1-ESN4-POO2.git
cd FuncionarioP1-ESN4-POO2
```

### Pelo Maven

```
mvn clean javafx:run
```

### Pelo IntelliJ

Abra a pasta como projeto Maven, configure o JDK 17 e execute a classe `ricardo.funcionariop1.Main`.

### Importante: diretório de execução

As telas `.fxml` são carregadas direto do sistema de arquivos, a partir do caminho relativo `src/main/java/ricardo/funcionariop1/view`, e o arquivo `funcionarios.csv` também é procurado no diretório atual. Por isso o programa precisa ser executado **a partir da raiz do projeto**. Se for rodado de outra pasta, as telas não carregam e o CSV será criado em outro lugar.

Ao iniciar, o aviso abaixo aparece no console e pode ser ignorado:

```
WARNING: Loading FXML document with JavaFX API of version 26 by JavaFX runtime of version 17.0.14
```

Ele acontece porque os FXML foram salvos pelo Scene Builder com uma versão mais nova do que a usada no projeto. Não afeta o funcionamento.

## Estrutura do projeto

```
src/main/java/ricardo/funcionariop1/
├── Main.java
├── Launcher.java
├── controller/
│   ├── MainController.java
│   ├── CadastroController.java
│   ├── ListagemController.java
│   └── RelatoriosController.java
├── model/
│   ├── Funcionario.java
│   └── Endereco.java
├── repository/
│   └── FuncionarioRepository.java
├── service/
│   └── FuncionarioService.java
├── utils/
│   ├── FuncionarioValidator.java
│   └── PathFXML.java
└── view/
    ├── main-view.fxml
    ├── cadastro-view.fxml
    ├── listagem-view.fxml
    └── relatorio-view.fxml
```

- **model**: classes `Funcionario` e `Endereco`. O funcionário tem um endereço por composição.
- **repository**: leitura e escrita do CSV.
- **service**: mantém a lista de funcionários em memória, aplica as regras de cadastro/exclusão e chama o repositório para salvar.
- **utils**: validação dos dados e caminho das telas.
- **controller**: tratamento de eventos das telas.
- **view**: arquivos FXML.

## Arquivo CSV

Os dados ficam em `funcionarios.csv`, na raiz do projeto, em UTF-8, com campos separados por ponto e vírgula. A primeira linha é o cabeçalho:

```
matricula;nome;CPF;dataNascimento;cargo;salario;dataContratacao;logradouro;numero;complemento;bairro;cidade;estado;CEP
```

- Datas no formato `AAAA-MM-DD`
- Salário com ponto como separador decimal (ex.: `4500.00`)
- Campos que contenham `;` ou `"` são escritos entre aspas
- O arquivo é lido uma vez ao iniciar e reescrito inteiro a cada cadastro ou exclusão
- Se o arquivo não existir, ele é criado no primeiro cadastro

O projeto já acompanha um `funcionarios.csv` com alguns registros de exemplo.

## Validações

| Campo | Regra |
|---|---|
| Matrícula | Exatamente 6 dígitos, sem repetir uma já cadastrada |
| Nome | Mínimo de 3 caracteres |
| CPF | 11 dígitos, não pode ter todos os dígitos iguais, com verificação dos dois dígitos verificadores (aceita com ou sem pontuação) |
| Data de nascimento | Idade mínima de 18 anos |
| Cargo | Obrigatório |
| Salário | Maior que zero (aceita `4500,00` ou `4.500,00` no formulário) |
| Data de contratação | Obrigatória e não pode ser futura |
| CEP | `00000-000` ou `00000000` |

Erros de validação são mostrados em um alerta e o cadastro não é feito.

## Relatórios

Todos usam Stream API sobre a lista retornada pelo service e estão na classe `RelatoriosController`.

- **Por cargo**: `filter` pelo cargo (sem diferenciar maiúsculas), `sorted` por nome
- **Por faixa salarial**: `filter` entre o mínimo e o máximo informados, `sorted` por salário
- **Média salarial por cargo**: `groupingBy` com `averagingDouble`
- **Por cidade/estado**: `groupingBy` na chave `cidade/estado`, com os nomes ordenados dentro de cada grupo

## Decisões de projeto

- Separação em camadas (controller, service, repository, model) para que os controllers não mexam direto no arquivo.
- O service guarda a lista em memória e só reescreve o CSV quando há alteração, assim a listagem e os relatórios não precisam reler o arquivo.
- `BigDecimal` para salário e `LocalDate` para datas, como pedido no enunciado.
- A interface foi montada no Scene Builder, com uma tela principal (menu lateral) que troca o conteúdo de um `StackPane` conforme a opção escolhida.
- O estilo visual é aplicado pelo atributo `style` dentro dos próprios FXML, sem arquivo CSS separado.
