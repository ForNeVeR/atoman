@file:Suppress("UnstableApiUsage")
package me.fornever.atoman.render

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.BasePresentation
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import me.fornever.atoman.AtomanBundle
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
    ) = InlayGameFieldHintCollector()

    companion object {
        private val settingsKey = SettingsKey<NoSettings>("atoman.InlayGameFieldProvider.settingsKey")
    }
}

class InlayGameFieldHintCollector : InlayHintsCollector {
    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
        sink.addBlockElement(0, false, false, 100, GameFieldPresentation())
        return false
    }
}

class GameFieldPresentation : BasePresentation() {
    override val height = 40
    override val width = 40

    override fun paint(g: Graphics2D, attributes: TextAttributes) {
        g.drawString("foo\nbar", 0.0f, 0.0f)
    }

    override fun toString() = "Game Field"
}
