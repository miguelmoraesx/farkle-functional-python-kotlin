import random
from functools import reduce


def rolar_dados(n):
    """Sorteia n dados."""
    return [random.randint(1, 6) for _ in range(n)]


def contar(lst, alvo):
    """Conta ocorrências de alvo."""
    if not lst:
        return 0

    primeiro = 1 if lst[0] == alvo else 0
    return primeiro + contar(lst[1:], alvo)


def valores_unicos(lst):
    """Mantém só a primeira ocorrência de cada valor."""
    def aux(resto, vistos):
        if not resto:
            return []

        primeiro = resto[0]
        if primeiro in vistos:
            return aux(resto[1:], vistos)

        return [primeiro] + aux(resto[1:], vistos + [primeiro])

    return aux(lst, [])


def valores_unicos_(lst):
    """Nome pedido no enunciado."""
    return valores_unicos(lst)


def eh_sequencia(dados):
    """Confere a sequência 1, 2, 3, 4, 5, 6."""
    return sorted(dados) == [1, 2, 3, 4, 5, 6]


def pontos_por_numero(dados, num):
    """Pontua as ocorrências de um número."""
    qtd = contar(dados, num)

    if qtd < 3:
        if num == 1:
            return (qtd * 100, qtd)
        if num == 5:
            return (qtd * 50, qtd)

        return (0, 0)

    trincas = qtd // 3
    resto = qtd % 3

    pontos_trinca = 1000 if num == 1 else num * 100
    pontos = trincas * pontos_trinca
    dados_usados = trincas * 3

    if num == 1:
        return (pontos + resto * 100, dados_usados + resto)

    if num == 5:
        return (pontos + resto * 50, dados_usados + resto)

    return (pontos, dados_usados)


def pontos_por_numero_(dados, num):
    """Nome pedido no enunciado."""
    return pontos_por_numero(dados, num)


def pontuacao_jogada(dados):
    """Calcula pontos e quantos dados entraram na pontuação."""
    if eh_sequencia(dados):
        return (1500, 6)

    resultados = map(
        lambda num: pontos_por_numero(dados, num),
        [1, 2, 3, 4, 5, 6]
    )

    return reduce(
        lambda total, atual: (total[0] + atual[0], total[1] + atual[1]),
        resultados,
        (0, 0)
    )


def pontuacao_jogada_(dados):
    """Nome pedido no enunciado."""
    return pontuacao_jogada(dados)


def escolha_aleatoria(n_restante, pontos):
    """Decide no sorteio se continua."""
    return random.choice([True, False])


def escolha_heuristica(n_restante, pontos):
    """Para com 300+ pontos ou quando restam poucos dados."""
    if pontos >= 300:
        return False

    if n_restante <= 2:
        return False

    return True


def turno(pontos_rodada, n_dados, escolha, verbose=True):
    """Roda um turno até o jogador parar ou sair Farkle."""
    dados = rolar_dados(n_dados)
    pontos, dados_pontuadores = pontuacao_jogada(dados)

    if pontos == 0:
        if verbose:
            print(
                f"Continua com {dados} (0 pts) | "
                f"FARKLE! Perdeu {pontos_rodada} pontos acumulados."
            )

        return 0

    novo_total = pontos_rodada + pontos
    n_restante = n_dados - dados_pontuadores

    if n_restante == 0:
        n_restante = 6

    if verbose:
        print(
            f"Lança {dados} ({pontos} pts) | "
            f"Acumulado: {novo_total} | "
            f"Restantes: {n_restante} dados"
        )

    if escolha(n_restante, novo_total):
        return turno(novo_total, n_restante, escolha, verbose)

    if verbose:
        print(f"Jogador PARA com {novo_total} pontos")

    return novo_total


def partida(
    estrategia0,
    estrategia1,
    pontos=None,
    jogador=0,
    pontuacao_max=10000,
    verbose=False
):
    """Alterna os jogadores até alguém bater a meta."""
    if pontos is None:
        pontos = [0, 0]

    if pontos[0] >= pontuacao_max or pontos[1] >= pontuacao_max:
        if verbose:
            print(
                f"\nFIM DE JOGO! "
                f"Jogador 0: {pontos[0]} | Jogador 1: {pontos[1]}"
            )

        if pontos[0] >= pontuacao_max and pontos[1] >= pontuacao_max:
            if pontos[0] > pontos[1]:
                return 0
            if pontos[1] > pontos[0]:
                return 1
            return None

        return 0 if pontos[0] >= pontuacao_max else 1

    if verbose:
        print(f"\n- Jogador {jogador} | Placar: {pontos[0]}-{pontos[1]}")

    escolha = estrategia0 if jogador == 0 else estrategia1
    pontos_do_turno = turno(0, 6, escolha, verbose)

    novos_pontos = [
        pontos[0] + (pontos_do_turno if jogador == 0 else 0),
        pontos[1] + (pontos_do_turno if jogador == 1 else 0)
    ]

    return partida(
        estrategia0,
        estrategia1,
        novos_pontos,
        1 - jogador,
        pontuacao_max,
        verbose
    )


def simular(n=1000):
    """Compara a heurística com a escolha aleatória."""
    vitorias = reduce(
        lambda total, _: total + (
            1 if partida(escolha_heuristica, escolha_aleatoria) == 0 else 0
        ),
        range(n),
        0
    )

    percentual = vitorias / n * 100
    return f"Heurística venceu {percentual:.2f}% das partidas contra aleatório."


def testar_pontuacao():
    """Casos básicos da tabela de pontuação."""
    casos = [
        ([1, 5, 2, 3, 4, 6], (1500, 6)),
        ([2, 2, 3, 4, 4, 6], (0, 0)),
        ([1, 1, 1, 2, 3, 4], (1000, 3)),
        ([2, 2, 2, 4, 5, 6], (250, 4)),
        ([1, 5, 2, 2, 3, 4], (150, 2)),
        ([2, 2, 2, 2, 2, 2], (400, 6)),
        ([2, 2, 2, 3, 3, 3], (500, 6)),
        ([4, 4, 4, 6, 6, 6], (1000, 6)),
        ([5, 6, 5, 6, 6, 6], (700, 5)),
        ([1, 1, 1, 1, 1, 1], (2000, 6))
    ]

    resultados = map(
        lambda caso: (
            caso[0],
            caso[1],
            pontuacao_jogada(caso[0])
        ),
        casos
    )

    print(f"{'Entrada':<24} | {'Esperado':<12} | {'Obtido':<12} | Status")
    print("-" * 62)

    list(map(
        lambda r: print(
            f"{str(r[0]):<24} | "
            f"{str(r[1]):<12} | "
            f"{str(r[2]):<12} | "
            f"{'OK' if r[1] == r[2] else 'FALHOU'}"
        ),
        resultados
    ))


if __name__ == "__main__":
    print("Testes da pontuação:")
    testar_pontuacao()

    print("\nSimulação:")
    print(simular(1000))
