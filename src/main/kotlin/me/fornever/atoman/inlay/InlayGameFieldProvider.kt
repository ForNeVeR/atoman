@file:Suppress("UnstableApiUsage")

package me.fornever.atoman.inlay

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.BasePresentation
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.rd.util.launchOnUi
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.adviseWithPrev
import me.fornever.atoman.AtomanBundle
import me.fornever.atoman.AtomanLifetimeProvider
import me.fornever.atoman.game.GameController
import java.awt.Graphics2D
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

    private val game = GameController().apply {
        lt.launchOnUi { start() }
    }

    override val width: Int
        get() = game.renderSize.value.width
    override val height: Int
        get() = game.renderSize.value.height

    init {
        game.renderSize.adviseWithPrev(lt) { prev, cur ->
            if (prev.hasValue)
                fireSizeChanged(prev.asNullable!!, cur)
        }
        game.render.advise(lt) {
            fireContentChanged()
        }
    }

    override fun paint(g: Graphics2D, attributes: TextAttributes) {
        game.render(g)
    }

    override fun toString() = "Game Field"
}
