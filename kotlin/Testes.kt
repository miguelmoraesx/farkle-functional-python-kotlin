data class CasoPontuacao(
    val dados: List<Int>,
    val esperado: Pair<Int, Int>
)

fun explicarFalha(obtido: Pair<Int, Int>, esperado: Pair<Int, Int>): String =
    when {
        obtido.first != esperado.first && obtido.second != esperado.second ->
            "pontuacao e quantidade de dados pontuadores diferentes; revisar regras de trincas, sequencia e dados individuais"
        obtido.first != esperado.first ->
            "pontuacao diferente; revisar soma das combinacoes pontuadoras"
        else ->
            "quantidade de dados pontuadores diferente; revisar contagem dos dados usados na pontuacao"
    }

fun main() {
    val casos = listOf(
        CasoPontuacao(listOf(1, 5, 2, 3, 4, 6), Pair(1500, 6)),
        CasoPontuacao(listOf(2, 2, 3, 4, 4, 6), Pair(0, 0)),
        CasoPontuacao(listOf(1, 1, 1, 2, 3, 4), Pair(1000, 3)),
        CasoPontuacao(listOf(2, 2, 2, 4, 5, 6), Pair(250, 4)),
        CasoPontuacao(listOf(1, 5, 2, 2, 3, 4), Pair(150, 2)),
        CasoPontuacao(listOf(2, 2, 2, 2, 2, 2), Pair(400, 6)),
        CasoPontuacao(listOf(2, 2, 2, 3, 3, 3), Pair(500, 6)),
        CasoPontuacao(listOf(4, 4, 4, 6, 6, 6), Pair(1000, 6)),
        CasoPontuacao(listOf(5, 6, 5, 6, 6, 6), Pair(700, 5)),
        CasoPontuacao(listOf(1, 1, 1, 1, 1, 1), Pair(2000, 6)),
        CasoPontuacao(listOf(5, 5, 5, 5, 5, 5), Pair(1000, 6)),
        CasoPontuacao(listOf(3, 3, 3, 1, 5, 2), Pair(450, 5))
    )

    val resultados = casos.map { caso ->
        val obtido = pontuacaoJogada(caso.dados)
        Triple(caso, obtido, obtido == caso.esperado)
    }

    println("%-24s | %-12s | %-12s | Status".format("Entrada", "Esperado", "Obtido"))
    println("-".repeat(62))

    resultados.forEach { (caso, obtido, passou) ->
        println(
            "%-24s | %-12s | %-12s | %s".format(
                caso.dados.toString(),
                caso.esperado.toString(),
                obtido.toString(),
                if (passou) "OK" else "FALHOU"
            )
        )
        if (!passou) {
            println("Razao provavel: ${explicarFalha(obtido, caso.esperado)}")
            println("Correcao: ajustar pontuacaoJogada/pontosPorNumero para refletir exatamente a tabela de pontuacao.")
        }
    }

    val passaram = resultados.count { it.third }
    val falharam = resultados.size - passaram

    println("Casos que passaram: $passaram")
    println("Casos que falharam: $falharam")
}
