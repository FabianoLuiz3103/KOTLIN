package visao

import modelo.Tabuleiro
import java.awt.Color
import java.awt.GridLayout
import javax.swing.JPanel

class PainelTabuleiro(tabuleiro: Tabuleiro) : JPanel() {
    val botoes: MutableList<BotaoCampo> = mutableListOf()

    init {
        layout = GridLayout(tabuleiro.qtdeLinhas, tabuleiro.qtdeColunas)
        criarBotoes(tabuleiro)
    }

    private fun criarBotoes(tabuleiro: Tabuleiro) {
        removeAll()
        botoes.clear()

        tabuleiro.forEachCampo { campo ->
            val botao = BotaoCampo(campo)
            botoes.add(botao)
            add(botao)
        }
        revalidate()
        repaint()
    }


    fun reiniciar(tabuleiro: Tabuleiro) {
        criarBotoes(tabuleiro)
        revalidate()
        repaint()
    }
}