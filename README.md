# 🎲 Farkle Funcional - Python e Kotlin

Projeto desenvolvido para a disciplina **Linguagens de Programação (2026-1)** da Universidade Federal do Amazonas (UFAM).

---

## 👨‍💻 Integrantes

- Miguel Oliveira Moraes de Souza  
- Adrian Batista Pereira  
- Pedro Henrique Belota Gadelha  

---

## 📌 Descrição

Este projeto consiste na implementação do jogo **Farkle** utilizando o paradigma de **programação funcional em Python**.

A proposta foi desenvolver toda a lógica do jogo evitando estruturas imperativas como `for` e `while`, priorizando:

- uso de recursão  
- funções puras sempre que possível  
- funções de alta ordem (`map`, `reduce`)  
- list comprehensions  

Além disso, o projeto também inclui:

- tradução da solução para **Kotlin**  
- validação por meio de testes  
- comparação entre Python e Kotlin no contexto funcional  

---

## 🎯 Objetivos

- Aplicar conceitos de programação funcional na prática  
- Desenvolver a lógica de um jogo completo sem estruturas imperativas  
- Criar uma estratégia heurística para tomada de decisão  
- Comparar desempenho com uma estratégia aleatória  
- Traduzir a solução para Kotlin mantendo o estilo funcional  

---

## 📂 Estrutura do Projeto

```txt
farkle-functional-python-kotlin/
├── README.md
├── src/
│   └── farkle.py
├── kotlin/
│   ├── Farkle.kt
│   └── Testes.kt
└── notebook/
    └── miguel_pedrobelota_adrian.ipynb
```

## ▶️ Como executar

### Python

```bash
python3 src/farkle.py
```

Esse comando executa os testes de pontuação e uma simulação de 1000 partidas.

### Kotlin

Carregue o SDKMAN na sessão do terminal:

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Confira se a versão nova está sendo usada:

```bash
which kotlinc
kotlinc -version
```

O caminho deve apontar para algo como `.sdkman/candidates/kotlin/current/bin/kotlinc`.

Depois compile e execute:

```bash
kotlinc kotlin/Farkle.kt kotlin/Testes.kt -include-runtime -d farkle-testes.jar
java -jar farkle-testes.jar
```

O arquivo `kotlin/Testes.kt` valida a função `pontuacaoJogada` com mais de 10 casos.

### Notebook

A entrega principal está em:

```txt
notebook/miguel_pedrobelota_adrian.ipynb
```

O notebook reúne a Parte I, a implementação Python, a tradução Kotlin, os testes e a comparação entre Python e Kotlin.
