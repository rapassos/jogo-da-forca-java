# Jogo da Forca (Java SE)

Este projeto é uma implementação moderna do clássico Jogo da Forca, desenvolvida em Java 17 com foco em arquitetura limpa, evolução incremental e apresentação profissional no GitHub. A intenção é manter a versão desktop atual e, em paralelo, evoluir para uma interface web moderna sem perder a base funcional já consolidada.

---

## 🎯 Objetivo do projeto

Construir uma experiência de jogo envolvente e bem estruturada, com:
- uma versão desktop clássica em Swing;
- uma versão web futura, criada em paralelo como feature moderna;
- documentação e histórico de desenvolvimento pensados para portfólio.

---

## 🚀 Funcionalidades atuais

* **Consumo de API Externa:** integração com a API do Dicionário Aberto para buscar palavras e definições.
* **Fallback Local:** quando a API não está disponível, o jogo utiliza um banco local com palavras estruturadas em JSON.
* **Níveis de Dificuldade:** fácil, médio, difícil e extremo.
* **Normalização de Caracteres:** suporte a palavras com acentuação.
* **Arquitetura Testável:** regras de negócio desacopladas da interface e de serviços externos.
* **Entrada por teclado:** a interface web pode receber letras diretamente pelo teclado do computador como alternativa de interação.

---

## 🧱 Estratégia de evolução

O projeto segue um modelo de desenvolvimento por sprints:
- Sprint 1: base de execução multi-interface e organização documental.
- Sprint 2: interface web inicial.
- Sprint 3: experiência de jogo aprimorada.
- Sprint 4: polish visual, identidade indie e publicação para portfólio.

Mais detalhes em [ROADMAP.md](ROADMAP.md).

---

## 🛠️ Tecnologias e ferramentas

* **Java 17 (LTS)**
* **Apache Maven**
* **Jackson Databind**
* **JUnit 5**
* **Python 3** para geração/atualização de dados de fallback

---

## 📂 Estrutura do projeto

```yaml
src/
├── main/
│   ├── java/com/rapassos/forca/
│   │   ├── app/                 # Bootstrap para múltiplas interfaces
│   │   ├── controller/          # Lógica do jogo
│   │   ├── model/               # Regras e estado do jogo
│   │   ├── service/             # Serviços de palavra e fallback
│   │   └── view/                # Interface Swing atual
│   └── resources/
│       └── palavras.json
└── test/
    └── java/com/rapassos/forca/
```

---

## ▶️ Como executar

### Pré-requisitos
* Java JDK 17 ou superior
* Apache Maven

### Compilar e testar
```bash
mvn clean test
```

### Executar a versão desktop
```bash
mvn exec:java -Dexec.mainClass="com.rapassos.forca.main.Main"
```

### Executar a versão web localmente
```bash
mvn exec:java -Dexec.mainClass="com.rapassos.forca.main.Main" -Dexec.args="--mode=web --port=8080"
```

### Build para deploy
```bash
mvn clean package
```

---

## 🚀 Passo a passo de deploy

### Opção 1 — Deploy simples da web no Render
1. Crie uma conta no Render.
2. Conecte este repositório ao Render.
3. Escolha a opção Web Service.
4. Defina o comando de start como:
   ```bash
   java -jar target/*-jar-with-dependencies.jar --mode=web --port=$PORT
   ```
5. Faça o deploy e copie a URL pública.

### Opção 2 — Deploy com Docker
```bash
docker build -t jogo-da-forca-java .
docker run -p 8080:8080 -e PORT=8080 jogo-da-forca-java
```

### Opção 3 — Deploy futuro no Vercel
- O frontend pode ser separado em um projeto estático depois.
- A API continua hospedada no backend Java e a URL base é trocada na configuração do frontend.

---

## 📝 Documentação complementar

* [ROADMAP.md](ROADMAP.md) — plano de desenvolvimento por sprints
* [CHANGELOG.md](CHANGELOG.md) — histórico de mudanças
* [CONTRIBUTING.md](CONTRIBUTING.md) — fluxo de contribuição e padrões de commit

---

## 🐍 Utilitários

O repositório também inclui um script em Python para atualização incremental do banco de fallback.

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install requests
python scripts/generate_fallback.py
```
