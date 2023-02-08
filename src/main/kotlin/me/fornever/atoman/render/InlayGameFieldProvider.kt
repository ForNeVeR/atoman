@file:Suppress("UnstableApiUsage")

package me.fornever.atoman.render

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.BasePresentation
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.rd.util.launchIOBackground
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.adviseWithPrev
import com.jetbrains.rd.util.reactive.map
import edu.illinois.library.imageio.xpm.XPMImageReaderSpi
import kotlinx.coroutines.delay
import me.fornever.atoman.AtomanBundle
import me.fornever.atoman.AtomanLifetimeProvider
import me.fornever.atoman.game.GameState
import me.fornever.atoman.game.Loaded
import me.fornever.atoman.game.Loading
import java.awt.Dimension
import java.awt.Graphics2D
import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry
import javax.swing.JPanel

class InlayGameFieldProvider : InlayHintsProvider<NoSettings> {

    override val key: SettingsKey<NoSettings> = settingsKey

    override val name: String
        get() = AtomanBundle.message("inlay.name")

    override val previewText: String
        get() = AtomanBundle.message("inlay.preview-text")

    override fun createConfigurable(settings: NoSettings) = object : ImmediateConfigurable {
        override fun createComponent(listener: ChangeListener) = JPanel()
    }

    override fun createSettings() = NoSettings()

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: NoSettings,
        sink: InlayHintsSink
    ): InlayGameFieldHintCollector {
        val lt = AtomanLifetimeProvider.getInstance(file.project).lifetime
        return InlayGameFieldHintCollector(lt)
    }

    companion object {
        private val settingsKey = SettingsKey<NoSettings>("atoman.InlayGameFieldProvider.settingsKey")
    }
}

class InlayGameFieldHintCollector(private val lt: Lifetime) : InlayHintsCollector {

    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        sink.addBlockElement(
            0,
            relatesToPrecedingText = false,
            showAbove = false,
            priority = 100,
            presentation = GameFieldPresentation(lt)
        )
        return false
    }
}

class GameFieldPresentation(lt: Lifetime) : BasePresentation() {
    private val state = Property<GameState>(Loading)
    private val size = state.map {
        when (it) {
            Loading -> Dimension(20, 20)
            is Loaded -> Dimension(it.size.first, it.size.second)
        }
    }

    override val width: Int
        get() = size.value.width
    override val height: Int
        get() = size.value.height

    init {
        size.adviseWithPrev(lt) { prev, cur ->
            if (prev.hasValue)
                lt.launchOnUi {
                    fireSizeChanged(prev.asNullable!!, cur)
                }
        }
        state.advise(lt) {
            lt.launchOnUi {
                fireContentChanged()
            }
        }

        lt.launchIOBackground {
            delay(3000)
            state.set(Loaded(80 to 80))
        }
    }

    override fun paint(g: Graphics2D, attributes: TextAttributes) {

        // TODO: Move to some static place
        IIORegistry.getDefaultInstance().registerServiceProvider(XPMImageReaderSpi())
        when (state.value) {
            Loading -> g.drawString("Loading!", 0, 0)
            is Loaded -> {
                // TODO: Load sprites in background
                val sprite = this.javaClass.classLoader.getResourceAsStream("sprites/Pacman-Chomping-Down.xpm").use {
                    ImageIO.read(it)
                }
                g.drawImage(sprite, 0, 0, null)
            }
        }
    }

    override fun toString() = "Game Field"
}
