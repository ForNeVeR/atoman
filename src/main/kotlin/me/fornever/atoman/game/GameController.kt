package me.fornever.atoman.game

import com.intellij.util.ui.UIUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.*
import edu.illinois.library.imageio.xpm.XPMImageReaderSpi
import kotlinx.coroutines.delay
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry

class GameController {

    private val state = Property<GameState>(LoadingMap)

    val render: ISource<Unit> = state.change.map {  }
    val size: IPropertyView<Dimension> = state.map {
        when (it) {
            LoadingMap, LoadingSprites -> Dimension(60, 60)
            is Loaded -> Dimension(it.size.first, it.size.second)
        }
    }

    suspend fun start() {
        delay(1500) // TODO: Load map here
        state.set(LoadingSprites)
        delay(1500)

        // TODO: Move to some static place
        IIORegistry.getDefaultInstance().registerServiceProvider(XPMImageReaderSpi())

        val sprite = this.javaClass.classLoader.getResourceAsStream("sprites/Pacman-Chomping-Right.xpm").use {
            ImageIO.read(it)
        }

        var index = 0
        while (true) {
            state.set(Loaded(60 to 60, sprite, index))
            index = (index + 1) % 4
            delay(500)
        }
    }

    fun render(g: Graphics2D) {
        g.background = Color.BLACK
        g.fillRect(0, 0, size.value.width, size.value.height)

        val state = state.value
        when (state) {
            LoadingMap -> {
                g.font = UIUtil.getLabelFont()
                g.drawString("Loading map…", 0, 0)
            }
            LoadingSprites -> {
                g.font = UIUtil.getLabelFont()
                g.drawString("Loading sprites…", 0, 0)
            }
            is Loaded -> {
                // TODO: Load sprites in background
                g.drawImage(
                    state.sprite,
                    0, 0, 40, 40,
                    40*state.index, 0, 40*(state.index + 1), 40,
                    null)
            }
        }
    }
}
