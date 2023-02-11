package me.fornever.atoman.sprites

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.openapi.rd.util.withIOBackgroundContext
import com.intellij.util.ui.UIUtil
import edu.illinois.library.imageio.xpm.XPMImageReaderSpi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import me.fornever.atoman.map.CellType
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry

data class Sprite(val name: String, val info: SpriteInfo, val image: BufferedImage) {

    companion object {

        init {
            IIORegistry.getDefaultInstance().registerServiceProvider(XPMImageReaderSpi())
        }

        suspend fun loadAll(): Map<CellType, Sprite?> = mapOf(
            CellType.Empty to null,
            CellType.Wall to Sprite(
                name = "Wall",
                image = generateWallImage(),
                info = SpriteInfo(
                    frames = mapOf(
                        "Wall 0" to SpriteFrameInfo(
                            Frame(0, 0, 40, 40),
                            duration = 0
                        )
                    )
                )
            ),
            CellType.Player to load("Pacman-Chomping-Right"), // TODO: Directions
            CellType.Ghost to load("Red-Ghost-Left"),
            CellType.Pill to load("Pill")
        )

        private fun generateWallImage(): BufferedImage =
            UIUtil.createImage(null, 40, 40, BufferedImage.TYPE_INT_ARGB).apply {
                for (y in 0..39) {
                    for (x in 0..39) {
                        setRGB(x, y, 0x0000FF00)
                    }
                }
            }

        private val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        private suspend fun load(name: String): Sprite =
            withIOBackgroundContext {
                coroutineScope {
                    val image = async {
                        Sprite::class.java.classLoader.getResourceAsStream("sprites/$name.xpm")
                            .use(ImageIO::read)
                    }
                    val info = async {
                        Sprite::class.java.classLoader.getResourceAsStream("sprites/$name.json")
                            .use { mapper.readValue(it, SpriteInfo::class.java) }
                    }

                    Sprite(
                        name,
                        info.await(),
                        image.await()
                    )
                }
            }

        fun getFrames(sprite: Sprite): List<SpriteFrameInfo> {
            val info = sprite.info
            return info.frames.keys.map { info.frames[it]!! }
        }
    }
}
