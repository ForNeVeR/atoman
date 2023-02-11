package me.fornever.atoman.sprites

data class SpriteMetaInfoSize(val w: Int, val h: Int)

data class SpriteMetaInfo(val size: SpriteMetaInfoSize)

data class SpriteInfo(
    val frames: Map<String, SpriteFrameInfo>,
    val meta: SpriteMetaInfo? = null
)
