package visao

import modelo.CamposEvento
import modelo.Tabuleiro
import java.awt.*
import java.io.File
import javax.imageio.ImageIO
import javax.swing.*


fun main() {
    TelaPrincipal()
}

public class TelaPrincipal : JFrame() {

    private val tabuleiro = Tabuleiro(qtdeLinhas = 10, qtdeColunas = 10, qtdeLixo = 15, qtdVidas = 3, qtdAlgas = 5)
    private val painelTabuleiro = PainelTabuleiro(tabuleiro)
    private var infoLabel = JLabel(formatarTextoInfo())
    init {
        tabuleiro.onEvento(this::mostrarResultado)
        add(painelTabuleiro, BorderLayout.CENTER)
        val painelInfo = JPanel(FlowLayout(FlowLayout.CENTER))
        painelInfo.add(infoLabel)
        add(painelInfo, BorderLayout.SOUTH)
        infoLabel.font = Font(infoLabel.font.name, Font.PLAIN, 20)

        val iconeImagem = ImageIcon(ImageIO.read(File("src/images/lata_lixo.png")))
        val imagemObjeto: Image = iconeImagem.image
        iconImage = imagemObjeto

        setSize(1000, 600)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Caça ao lixo"

        isVisible = true




        val cursorImage: Image = Toolkit.getDefaultToolkit().getImage("src/images/tridente.png")
        val cursor: Cursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImage,
            Point(0, 0),
            "MeuCursor"
        )
        setCursor(cursor)
    }

    private fun formatarTextoInfo(): String {
        return "<html><font color='red'>Vidas:</font> <font color='red'>" + tabuleiro.vidas + "</font> | " +
                "<font color='green'>Lixos Encontrados:</font> <font color='green'>" + tabuleiro.lixoAchado + "</font> | " +
                "<font color='blue'>Lixos Restantes:</font> <font color='blue'>" + tabuleiro.lixoSobrando + "</font> | " +
                "<font color='#ff69b4'>Algas:</font> <font color='#ff69b4'>" + tabuleiro.algas + "</font></html>"

    }



    private fun mostrarResultado(evento: CamposEvento) {
        SwingUtilities.invokeLater {
            when (evento) {
                CamposEvento.VITORIA -> {
                    val labelMensagem = JLabel("Parabéns! Você venceu! 🎉" +
                            "Você conseguiu salvar os mares encontrando todos os lixos.")
                    labelMensagem.foreground = Color.GREEN
                    val font = labelMensagem.font
                    val newFont = font.deriveFont(font.style, 16f)
                    labelMensagem.font = newFont

                    val timer = Timer(500) {
                        labelMensagem.isVisible = !labelMensagem.isVisible
                    }
                    timer.start()
                    JOptionPane.showMessageDialog(
                        this,
                        labelMensagem,
                        "Resultado",
                        JOptionPane.WARNING_MESSAGE,
                        ImageIcon(ImageIO.read(File("src/images/joinha_ganhou.png")))
                    )
                    timer.stop()
                }
                CamposEvento.DERROTA -> {
                    val labelMensagemDerrota = JLabel("Suas vidas acabaram. Você não conseguiu salvar os mares. Tente de novo!")
                    labelMensagemDerrota.foreground = Color.RED
                    val font = labelMensagemDerrota.font
                    val newFont = font.deriveFont(font.style, 16f)
                    labelMensagemDerrota.font = newFont
                    val timer = Timer(500) {
                        labelMensagemDerrota.isVisible = !labelMensagemDerrota.isVisible
                    }
                    timer.start()
                    JOptionPane.showMessageDialog(
                        this,
                        labelMensagemDerrota,
                        "Resultado",
                        JOptionPane.WARNING_MESSAGE,
                        ImageIcon(ImageIO.read(File("src/images/emoji_perdeu.png")))
                    )
                    timer.stop()
                }
                CamposEvento.ENCONTROU_LIXO -> {
                    infoLabel.text = formatarTextoInfo()
                }
                CamposEvento.PERDEU_VIDA -> {
                    infoLabel.text = formatarTextoInfo()
                }
                CamposEvento.ENCONTROU_ALGA -> {
                    infoLabel.text = formatarTextoInfo()
                }

            }

            if (evento == CamposEvento.VITORIA || evento == CamposEvento.DERROTA) {
                tabuleiro.reiniciar()
                infoLabel.text = formatarTextoInfo()
                infoLabel.repaint()
                painelTabuleiro.reiniciar(tabuleiro)
            }

        }
    }
}