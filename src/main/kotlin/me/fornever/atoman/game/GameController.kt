package me.fornever.atoman.game

import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import com.jetbrains.rd.util.reactive.IPropertyView
import com.jetbrains.rd.util.reactive.ISource
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.map
import me.fornever.atoman.AtomanBundle
import me.fornever.atoman.map.GameMap
import me.fornever.atoman.sprites.Sprite
import java.awt.Dimension
import java.awt.Graphics2D

class GameController {

    companion object {
        const val cellRenderSize = 40
    }

    private val state = Property<GameState>(LoadingMap)

    val render: ISource<Unit> = state.change.map {  }
    val renderSize: IPropertyView<Dimension> = state.map {
        when (it) {
            LoadingMap, LoadingSprites -> Dimension(60, 60)
            is Loaded -> Dimension(it.map.width * cellRenderSize, it.map.height * cellRenderSize)
        }
    }

    suspend fun start() {
        val map = GameMap.loadByName("map01")

        state.set(LoadingSprites)
        val sprites = Sprite.loadAll()

        state.set(Loaded(map, sprites))
    }

    fun render(g: Graphics2D) {
        g.background = JBColor.BLACK
        g.fillRect(0, 0, renderSize.value.width, renderSize.value.height)

        when (val state = state.value) {
            LoadingMap -> {
                g.font = UIUtil.getLabelFont()
                g.drawString(AtomanBundle.message("game.loading.map"), 0, 0)
            }
            LoadingSprites -> {
                g.font = UIUtil.getLabelFont()
                g.drawString(AtomanBundle.message("game.loading.sprites"), 0, 0)
            }
            is Loaded -> {

                for ((y, row) in state.map.cells.withIndex()) {
                    for ((x, cell) in row.withIndex()) {
                        val sprite = state.sprites[cell.type] ?: continue
                        g.drawImage(
                            sprite.image,
                            x * cellRenderSize, y * cellRenderSize, (x + 1) * cellRenderSize, (y + 1) * cellRenderSize,
                            cellRenderSize * cell.frameIndex, 0, cellRenderSize * (cell.frameIndex + 1), cellRenderSize,
                            null
                        )
                    }
                }

            }
        }
    }
}
