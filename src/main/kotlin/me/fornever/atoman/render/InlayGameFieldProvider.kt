@file:Suppress("UnstableApiUsage")

package me.fornever.atoman.render

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.BasePresentation
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import edu.illinois.library.imageio.xpm.XPMImageReaderSpi
import me.fornever.atoman.AtomanBundle
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

    IIORegistry.getDefaultInstance().registerServiceProvider(XPMImageReaderSpi())

    ImageIO.scanForPlugins()

    val sprite = this.javaClass.classLoader.getResourceAsStream("sprites/Pacman-Chomping-Down.xpm").use {
      ImageIO.read(it)
    }
    g.drawImage(sprite, 0, 0, null)
  }

  override fun toString() = "Game Field"
}
