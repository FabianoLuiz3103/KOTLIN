package visao

import modelo.Tabuleiro
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JButton
import kotlin.random.Random

class BotaoCampo(private val campo: Tabuleiro.Campo) : JButton(), ActionListener {
    private val lixos = listOf(
        "src/images/lixos/madeira.png",
        "src/images/lixos/refrigerantes.png",
        "src/images/lixos/lixo.png",
        "src/images/lixos/latas.png",
        "src/images/lixos/roda.png"
    )
    private val indiceLixo: Int = Random.nextInt(lixos.size)
    private val caminhoDoLixo = lixos[indiceLixo]

    private val animais = listOf(
        "src/images/animais/estrelas-do-mar.png",
        "src/images/animais/espiga-amarela.png",
        "src/images/animais/tartaruga-marinha.png",
        "src/images/animais/tubarao.png"
    )
    private val indiceAnimal: Int = Random.nextInt(animais.size)
    private val caminhoDoAnimal = animais[indiceAnimal]
    private val imagemFundo = ImageIcon(ImageIO.read(File("src/images/fundo_agua.jpg")))
    private val imagemLixo = ImageIcon(ImageIO.read(File(caminhoDoLixo)))
    private val imagemAnimal = ImageIcon(ImageIO.read(File(caminhoDoAnimal)))
    private val algaNormal = ImageIcon(ImageIO.read(File("src/images/alga-normal.png")))



    init {
        icon = when {
            campo.temLixo -> imagemLixo
            campo.temAnimal -> imagemAnimal
            campo.temAlga -> algaNormal
            else -> imagemFundo
        }
        isContentAreaFilled = false
        isBorderPainted = true
        addActionListener(this)
        campo.tabuleiro.adicionarBotao(this)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                icon = when {
                    campo.temAlga -> algaNormal
                    else -> imagemFundo
                }
                repaint()
            }
        }, 3000)
    }

    override fun paintComponent(g: Graphics) {
        g.drawImage(imagemFundo.image, 0, 0, width, height, this)
        super.paintComponent(g)
    }

    override fun actionPerformed(e: ActionEvent) {
        campo.tabuleiro.clicar(campo.linha, campo.coluna)
        if (campo.temAlga) {
            campo.tabuleiro.atualizarTodosOsBotoes()
            isEnabled = false
            icon = algaNormal
        } else {
            icon = when {
                campo.temLixo -> imagemLixo
                campo.temAnimal -> imagemAnimal
                else -> imagemFundo
            }
        }
    }

    fun mostrarIconeTemporariamente() {
        if (!campo.tabuleiro.clickedCells.contains(campo.linha to campo.coluna)) {
            icon = when {
                campo.temLixo -> imagemLixo
                campo.temAnimal -> imagemAnimal
                campo.temAlga -> algaNormal
                else -> imagemFundo
            }
            repaint()

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    icon = when {
                        campo.temAlga -> algaNormal
                        else -> imagemFundo
                    }
                    repaint()
                }
            }, 3000)
        }
    }
}
