package modelo

import kotlin.random.Random

class Tabuleiro(var qtdeLinhas: Int, var qtdeColunas: Int, val qtdeLixo: Int, private var qtdVidas: Int, private var qtdAlgas: Int) {
    private val tabuleiro: Array<IntArray> = Array(qtdeLinhas) { IntArray(qtdeColunas) { -1 } }
    var lixoSobrando = qtdeLixo
    val clickedCells = mutableSetOf<Pair<Int, Int>>()
    var lixoAchado = 0
    var vidas = qtdVidas
    var algas = qtdAlgas
    private val botoes = mutableListOf<visao.BotaoCampo>()


    init {
        sortearLixo()
        sortearAlgas()
        colocarAnimais()
    }

    private fun sortearLixo() {
        val gerador = Random
        var qtdeLixoAtual = 0

        while (qtdeLixoAtual < qtdeLixo) {
            val linhaSorteada = gerador.nextInt(qtdeLinhas)
            val colunaSorteada = gerador.nextInt(qtdeColunas)

            if (tabuleiro[linhaSorteada][colunaSorteada] == -1) {
                tabuleiro[linhaSorteada][colunaSorteada] = 0
                qtdeLixoAtual++
            }
        }
    }

    private fun sortearAlgas() {
        val gerador = Random
        var qtdeAlgasAtual = 0

        while (qtdeAlgasAtual < 5) {
            val linhaSorteada = gerador.nextInt(qtdeLinhas)
            val colunaSorteada = gerador.nextInt(qtdeColunas)

            if (tabuleiro[linhaSorteada][colunaSorteada] == -1) {
                tabuleiro[linhaSorteada][colunaSorteada] = 2
                qtdeAlgasAtual++
            }
        }
    }

    private fun colocarAnimais() {
        for (linha in 0 until qtdeLinhas) {
            for (coluna in 0 until qtdeColunas) {
                if (tabuleiro[linha][coluna] == -1) {
                    tabuleiro[linha][coluna] = 1
                }
            }
        }
    }

    fun adicionarBotao(botao: visao.BotaoCampo) {
        botoes.add(botao)
    }

    fun atualizarTodosOsBotoes() {
        for (botao in botoes) {
            botao.mostrarIconeTemporariamente()
        }
    }

    fun clicar(linha: Int, coluna: Int) {
        if (clickedCells.contains(linha to coluna)) {
            return
        }

        clickedCells.add(linha to coluna)

        when (tabuleiro[linha][coluna]) {
            0 -> {
                lixoAchado++
                lixoSobrando--
                onEvento(CamposEvento.ENCONTROU_LIXO)
            }
            1 -> {
                vidas--
                onEvento(CamposEvento.PERDEU_VIDA)
            }
            2 -> {
                algas--
                onEvento(CamposEvento.ENCONTROU_ALGA)
                atualizarTodosOsBotoes()
            }
        }
        if (lixoAchado == qtdeLixo) {
            onEvento(CamposEvento.VITORIA)
        } else if (vidas == 0) {
            onEvento(CamposEvento.DERROTA)
        }
    }

    fun reiniciar() {
        clickedCells.clear()
        vidas = qtdVidas
        lixoAchado = 0
        lixoSobrando = qtdeLixo
        algas = qtdAlgas
        for (linha in 0 until qtdeLinhas) {
            for (coluna in 0 until qtdeColunas) {
                tabuleiro[linha][coluna] = -1
            }
        }

        sortearLixo()
        sortearAlgas()
        colocarAnimais()

    }

    private var listener: ((CamposEvento) -> Unit)? = null

    fun onEvento(listener: (CamposEvento) -> Unit) {
        this.listener = listener
    }

    private fun onEvento(evento: CamposEvento) {
        listener?.invoke(evento)
    }

    fun forEachCampo(acao: (Campo) -> Unit) {
        for (linha in 0 until qtdeLinhas) {
            for (coluna in 0 until qtdeColunas) {
                acao(Campo(linha, coluna, tabuleiro[linha][coluna], this))
            }
        }
    }

    inner class Campo(val linha: Int, val coluna: Int, val tipo: Int, val tabuleiro: Tabuleiro) {
        val temLixo: Boolean get() = tipo == 0
        val temAnimal: Boolean get() = tipo == 1
        val temAlga: Boolean get() = tipo == 2
    }
}

enum class CamposEvento {
    VITORIA,
    DERROTA,
    ENCONTROU_LIXO,
    PERDEU_VIDA,
    ENCONTROU_ALGA
}
