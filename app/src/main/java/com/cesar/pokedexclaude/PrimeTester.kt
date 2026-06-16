import kotlin.random.Random

/**
 * Prisoner's Dilemma Challenge
 *
 * Simula el juego del Dilema del Prisionero entre dos jugadores
 * con diferentes estrategias.
 */

// Representa las posibles decisiones en cada ronda
enum class Decision {
    COOPERATE,
    DEFECT,
}

// Resultado del juego (equivalente a una tupla en Kotlin)
data class Payoff(
    val player1: Int,
    val player2: Int,
)

// Clase que maneja el estado del juego y las estrategias
class PrisonersDilemma(
    private val rounds: Int = 100,
) {
    // Constantes de payoff según las reglas
    companion object {
        const val BOTH_COOPERATE = 3
        const val BOTH_DEFECT = 1
        const val DEFECTOR_WINS = 5
        const val COOPERATOR_LOSES = 0
    }

    /**
     * Función principal del challenge
     * @param player1Strategy Estrategia del jugador 1
     * @param player2Strategy Estrategia del jugador 2
     * @return Payoff con los puntajes de cada jugador
     */
    fun prisonersDilemma(
        player1Strategy: String,
        player2Strategy: String,
    ): Payoff {
        // Historial de decisiones de cada jugador
        val history1 = mutableListOf<Decision>()
        val history2 = mutableListOf<Decision>()

        var payoff1 = 0
        var payoff2 = 0

        repeat(rounds) {
            // Cada jugador decide basándose en el historial del OPONENTE
            val decision1 = getDecision(player1Strategy, history2, history1)
            val decision2 = getDecision(player2Strategy, history1, history2)

            // Guardar decisiones en historial
            history1.add(decision1)
            history2.add(decision2)

            // Calcular payoffs de esta ronda
            val (p1, p2) = calculatePayoff(decision1, decision2)
            payoff1 += p1
            payoff2 += p2
        }

        return Payoff(payoff1, payoff2)
    }

    /**
     * Calcula el payoff según las reglas del juego
     */
    private fun calculatePayoff(
        decision1: Decision,
        decision2: Decision,
    ): Pair<Int, Int> =
        when {
            decision1 == Decision.COOPERATE && decision2 == Decision.COOPERATE -> {
                Pair(BOTH_COOPERATE, BOTH_COOPERATE)
            }

            decision1 == Decision.DEFECT && decision2 == Decision.DEFECT -> {
                Pair(BOTH_DEFECT, BOTH_DEFECT)
            }

            decision1 == Decision.DEFECT && decision2 == Decision.COOPERATE -> {
                Pair(DEFECTOR_WINS, COOPERATOR_LOSES)
            }

            else -> {
                // decision1 == COOPERATE && decision2 == DEFECT
                Pair(COOPERATOR_LOSES, DEFECTOR_WINS)
            }
        }

    /**
     * Determina la decisión según la estrategia
     * @param strategy Nombre de la estrategia
     * @param opponentHistory Historial del oponente
     * @param ownHistory Historial propio (para algunas estrategias)
     */
    private fun getDecision(
        strategy: String,
        opponentHistory: List<Decision>,
        ownHistory: List<Decision>,
    ): Decision =
        when (strategy.lowercase()) {
            // Siempre coopera
            "cooperate" -> {
                Decision.COOPERATE
            }

            // Siempre traiciona
            "defect" -> {
                Decision.DEFECT
            }

            // Coopera primero, luego imita la última jugada del oponente
            "tit_for_tat" -> {
                if (opponentHistory.isEmpty()) {
                    Decision.COOPERATE
                } else {
                    opponentHistory.last()
                }
            }

            // Friedman (Grim Trigger): Coopera hasta que el oponente traicione,
            // luego siempre traiciona
            "friedman" -> {
                if (opponentHistory.contains(Decision.DEFECT)) {
                    Decision.DEFECT
                } else {
                    Decision.COOPERATE
                }
            }

            // Joss: Mayormente coopera como Tit-for-Tat,
            // pero 10% de probabilidad de traicionar
            "joss" -> {
                val titForTatDecision =
                    if (opponentHistory.isEmpty()) {
                        Decision.COOPERATE
                    } else {
                        opponentHistory.last()
                    }

                // 10% de probabilidad de traicionar aleatoriamente
                if (Random.nextDouble() < 0.10) {
                    Decision.DEFECT
                } else {
                    titForTatDecision
                }
            }

            // Davis: Coopera inicialmente, traiciona si el oponente
            // traiciona DOS veces seguidas
            "davis" -> {
                val size = opponentHistory.size
                if (size >= 2 &&
                    opponentHistory[size - 1] == Decision.DEFECT &&
                    opponentHistory[size - 2] == Decision.DEFECT
                ) {
                    Decision.DEFECT
                } else {
                    Decision.COOPERATE
                }
            }

            // Downing: Coopera inicialmente, traiciona si el oponente
            // ha traicionado MÁS veces de las que ha cooperado
            "downing" -> {
                if (opponentHistory.isEmpty()) {
                    Decision.COOPERATE
                } else {
                    val defectCount = opponentHistory.count { it == Decision.DEFECT }
                    val cooperateCount = opponentHistory.count { it == Decision.COOPERATE }

                    if (defectCount > cooperateCount) {
                        Decision.DEFECT
                    } else {
                        Decision.COOPERATE
                    }
                }
            }

            else -> {
                throw IllegalArgumentException("Unknown strategy: $strategy")
            }
        }
}

// ============== MAIN: Demostración del juego ==============

fun main() {
    val game = PrisonersDilemma(rounds = 100)

    val strategies =
        listOf(
            "cooperate",
            "defect",
            "tit_for_tat",
            "friedman",
            "joss",
            "davis",
            "downing",
        )

    println("═══════════════════════════════════════════════════")
    println("       PRISONER'S DILEMMA - TOURNAMENT")
    println("═══════════════════════════════════════════════════\n")

    // Tabla de resultados
    val results = mutableMapOf<String, Int>()
    strategies.forEach { results[it] = 0 }

    // Cada estrategia vs todas las demás
    for (i in strategies.indices) {
        for (j in i + 1 until strategies.size) {
            val s1 = strategies[i]
            val s2 = strategies[j]

            val payoff = game.prisonersDilemma(s1, s2)

            println("$s1 vs $s2")
            println("  → Payoffs: (${payoff.player1}, ${payoff.player2})")

            val winner =
                when {
                    payoff.player1 > payoff.player2 -> s1
                    payoff.player2 > payoff.player1 -> s2
                    else -> "TIE"
                }
            println("  → Winner: $winner\n")

            results[s1] = results[s1]!! + payoff.player1
            results[s2] = results[s2]!! + payoff.player2
        }
    }

    // Ranking final
    println("═══════════════════════════════════════════════════")
    println("              FINAL RANKINGS")
    println("═══════════════════════════════════════════════════")

    results.entries
        .sortedByDescending { it.value }
        .forEachIndexed { index, (strategy, score) ->
            println("  ${index + 1}. %-12s : %d points".format(strategy, score))
        }
} 
