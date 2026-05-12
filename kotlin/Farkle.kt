import kotlin.random.Random

fun rolarDados(n: Int): List<Int> =
    (1..n).map { Random.nextInt(1, 7) }

tailrec fun contar(lst: List<Int>, alvo: Int, acumulado: Int = 0): Int =
    when {
        lst.isEmpty() -> acumulado
        lst.first() == alvo -> contar(lst.drop(1), alvo, acumulado + 1)
        else -> contar(lst.drop(1), alvo, acumulado)
    }

fun valoresUnicos(lst: List<Int>): List<Int> {
    fun aux(resto: List<Int>, vistos: List<Int>): List<Int> =
        when {
            resto.isEmpty() -> emptyList()
            resto.first() in vistos -> aux(resto.drop(1), vistos)
            else -> listOf(resto.first()) + aux(resto.drop(1), vistos + resto.first())
        }

    return aux(lst, emptyList())
}

fun ehSequencia(dados: List<Int>): Boolean =
    dados.sorted() == listOf(1, 2, 3, 4, 5, 6)

fun pontosPorNumero(dados: List<Int>, num: Int): Pair<Int, Int> {
    val qtd = contar(dados, num)

    if (qtd < 3) {
        return when (num) {
            1 -> Pair(qtd * 100, qtd)
            5 -> Pair(qtd * 50, qtd)
            else -> Pair(0, 0)
        }
    }

    val trincas = qtd / 3
    val resto = qtd % 3
    val pontosTrinca = if (num == 1) 1000 else num * 100
    val pontos = trincas * pontosTrinca
    val dadosUsados = trincas * 3

    return when (num) {
        1 -> Pair(pontos + resto * 100, dadosUsados + resto)
        5 -> Pair(pontos + resto * 50, dadosUsados + resto)
        else -> Pair(pontos, dadosUsados)
    }
}

fun pontuacaoJogada(dados: List<Int>): Pair<Int, Int> =
    if (ehSequencia(dados)) {
        Pair(1500, 6)
    } else {
        listOf(1, 2, 3, 4, 5, 6)
            .map { pontosPorNumero(dados, it) }
            .fold(Pair(0, 0)) { total, atual ->
                Pair(total.first + atual.first, total.second + atual.second)
            }
    }

fun escolhaAleatoria(nRestante: Int, pontos: Int): Boolean =
    listOf(true, false).random()

fun escolhaHeuristica(nRestante: Int, pontos: Int): Boolean =
    pontos < 300 && nRestante > 2

tailrec fun turno(
    pontosRodada: Int,
    nDados: Int,
    escolha: (Int, Int) -> Boolean,
    verbose: Boolean = true
): Int {
    val dados = rolarDados(nDados)
    val (pontos, dadosPontuadores) = pontuacaoJogada(dados)

    if (pontos == 0) {
        if (verbose) {
            println("Continua com $dados (0 pts) | FARKLE! Perdeu $pontosRodada pontos acumulados.")
        }
        return 0
    }

    val novoTotal = pontosRodada + pontos
    val nRestanteCalculado = nDados - dadosPontuadores
    val nRestante = if (nRestanteCalculado == 0) 6 else nRestanteCalculado

    if (verbose) {
        println("Lanca $dados ($pontos pts) | Acumulado: $novoTotal | Restantes: $nRestante dados")
    }

    return if (escolha(nRestante, novoTotal)) {
        turno(novoTotal, nRestante, escolha, verbose)
    } else {
        if (verbose) println("Jogador PARA com $novoTotal pontos")
        novoTotal
    }
}

tailrec fun partida(
    estrategia0: (Int, Int) -> Boolean,
    estrategia1: (Int, Int) -> Boolean,
    pontos: List<Int> = listOf(0, 0),
    jogador: Int = 0,
    pontuacaoMax: Int = 10000,
    verbose: Boolean = false
): Int? {
    val ponto0 = pontos[0]
    val ponto1 = pontos[1]

    if (ponto0 >= pontuacaoMax || ponto1 >= pontuacaoMax) {
        if (verbose) {
            println("\nFIM DE JOGO! Jogador 0: $ponto0 | Jogador 1: $ponto1")
        }

        return when {
            ponto0 >= pontuacaoMax && ponto1 >= pontuacaoMax && ponto0 > ponto1 -> 0
            ponto0 >= pontuacaoMax && ponto1 >= pontuacaoMax && ponto1 > ponto0 -> 1
            ponto0 >= pontuacaoMax && ponto1 >= pontuacaoMax -> null
            ponto0 >= pontuacaoMax -> 0
            else -> 1
        }
    }

    if (verbose) {
        println("\n- Jogador $jogador | Placar: $ponto0-$ponto1")
    }

    val escolha = if (jogador == 0) estrategia0 else estrategia1
    val pontosDoTurno = turno(0, 6, escolha, verbose)
    val novosPontos = listOf(
        ponto0 + if (jogador == 0) pontosDoTurno else 0,
        ponto1 + if (jogador == 1) pontosDoTurno else 0
    )

    return partida(estrategia0, estrategia1, novosPontos, 1 - jogador, pontuacaoMax, verbose)
}

fun passwordClassification(password: String): Pair<String, List<String>> {
    val criterios = listOf(
        Pair("tamanho", password.length >= 8),
        Pair("maiúscula", password.any { it in 'A'..'Z' }),
        Pair("minúscula", password.any { it in 'a'..'z' }),
        Pair("dígito", password.any { it in '0'..'9' }),
        Pair("caractere especial", password.any { it in "!@#$%^&*()-+" })
    )

    val pontos = criterios.count { it.second }
    val naoAtendidos = criterios
        .filter { !it.second }
        .map { it.first }

    val classificacao = when (pontos) {
        0, 1 -> "Fraca"
        2, 3 -> "Moderada"
        4 -> "Forte"
        else -> "Muito Forte"
    }

    return Pair(classificacao, naoAtendidos)
}

fun simular(n: Int = 1000): String {
    val vitorias = (1..n)
        .map { partida(::escolhaHeuristica, ::escolhaAleatoria) }
        .count { it == 0 }
    val percentual = vitorias.toDouble() / n * 100.0

    return "Heurística venceu %.2f%% das partidas contra aleatório.".format(percentual)
}

fun demoFarkle() {
    println(passwordClassification("abc"))
    println(passwordClassification("Abc123"))
    println(passwordClassification("Abc123!@"))
    println(simular(1000))
}
