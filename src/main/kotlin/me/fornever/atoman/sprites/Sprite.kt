package me.fornever.atoman.sprites

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.rd.util.withIOBackgroundContext
import com.intellij.util.ui.UIUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

data class Sprite(val name: String, val info: SpriteInfo, val image: BufferedImage) {

    companion object {

        private val mapper = ObjectMapper()

        val wall = Sprite(
            name = "Wall",
            image = getWallImage(),
            info = SpriteInfo(
                frames = mapOf(
                    "Wall 0" to SpriteFrameInfo(
                        Frame(0, 0, 40, 40),
                        duration = 0
                    )
                )
            )
        )

        fun getWallImage(): BufferedImage =
            UIUtil.createImage(null, 40, 40, BufferedImage.TYPE_INT_ARGB).apply {
                for (y in 0..39) {
                    for (x in 0..39) {
                        setRGB(x, y, 0x0000FF00)
                    }
                }
            }

        suspend fun load(name: String): Sprite =
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

        fun getFrames(sprite: Sprite) {
            val info = sprite.info
            return info.frames.keys.map { info.frames[it] }
        }
    }
}
