# Jogo da Forca (Java SE)

Uma implementação moderna, robusta e resiliente do clássico Jogo da Forca desenvolvida em Java 17. O projeto utiliza arquitetura desacoplada baseada em serviços para consumir palavras e definições diretamente de uma API externa, contando com um mecanismo inteligente de contingência local estruturado em JSON.

---

## 🚀 Funcionalidades Chave

* **Consumo de API Externa:** Integração assíncrona com a API do *Dicionário Aberto* para obter termos e significados dinamicamente.
* **Mecanismo de Fallback Autônomo:** Se a API externa estiver instável ou apresentar latência, o sistema chaveia automaticamente para um banco de dados local com mais de 1.200 palavras.
* **Filtros e Validações Estritas:** O motor do jogo descarta automaticamente termos compostos, espaços e palavras com hífens, garantindo a integridade das partidas.
* **Níveis de Dificuldade Dinâmicos:** Controle rigoroso do tamanho das palavras e do limite de erros permitidos com base em quatro categorias:
  * **Fácil:** 3 a 5 letras (até 6 erros).
  * **Médio:** 6 a 8 letras (até 5 erros).
  * **Difícil:** 9 a 11 letras (até 4 erros).
  * **Extremo:** 12 ou mais letras (até 3 erros).
* **Normalização Inteligente de Caracteres:** Suporte nativo a acentuação da língua portuguesa. Se a palavra contiver "Á", o palpite "A" revelará a letra mantendo o caractere acentuado original visualmente.
* **Arquitetura Testável:** Lógica de negócio desacoplada da interface gráfica e dos provedores de dados, coberta por testes unitários automatizados.

---

## 🛠️ Tecnologias e Ferramentas

* **Java 17 (LTS):** Utilização de recursos modernos da linguagem e HttpClient nativo.
* **Apache Maven:** Gerenciamento de ciclo de vida de build e dependências de terceiros.
* **Jackson Databind:** Manipulação e parsing eficiente de payloads JSON da API e arquivos locais.
* **JUnit 5:** Framework para criação e execução de testes unitários.
* **Python 3:** Script de automação e engenharia de dados integrado para mineração incremental da base de fallback.

---

## 📂 Arquitetura do Projeto

O projeto utiliza o padrão **Service/Controller** com Injeção de Dependência, isolando regras de negócio, contratos de serviço e interface gráfica:

```yaml
src/
├── main/
│   ├── java/com/rapassos/forca/
│   │   ├── main/Main.java             # Ponto de entrada da aplicação
│   │   ├── controller/GameController.java  # Orquestração de estados e lógica de palpites
│   │   ├── model/                     # Enums e objetos de valor (Difficulty, GameState)
│   │   ├── service/                   # Interfaces e implementações (API, Fallback Local)
│   │   └── view/                      # Interface gráfica Swing (MenuView, GameView)
│   └── resources/
│       └── palavras.json              # Banco de dados local para fallback
└── test/
    └── java/com/rapassos/forca/
        └── controller/GameControllerTest.java # Testes de lógica e normalização
```

---

## 🏃 Como Executar a Aplicação

### Pré-requisitos
* Java JDK 17 ou superior configurado.
* Apache Maven instalado.

### Compilação e Execução
Na raiz do projeto, execute os comandos abaixo no terminal:

```bash
# Limpar builds antigos e compilar o projeto
mvn clean compile

# Executar a suíte de testes unitários (JUnit 5)
mvn test

# Iniciar a aplicação
mvn exec:java -Dexec.mainClass="com.rapassos.forca.main.Main"
```

---

## 🐍 Utilitários: Script Gerador de Massa de Dados

O repositório inclui um script em Python focado em engenharia de dados para atualizar de forma incremental o arquivo `palavras.json` consumindo o endpoint randômico da API oficial.

### Como rodar o gerador:
```bash
# Criar e ativar o ambiente virtual isolado
python3 -m venv .venv
source .venv/bin/activate

# Instalar as dependências e executar
pip install requests
python scripts/generate_fallback.py
```