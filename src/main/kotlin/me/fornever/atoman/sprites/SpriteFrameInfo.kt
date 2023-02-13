package me.fornever.atoman.sprites

data class Frame(
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
)

data class SpriteFrameInfo(
    val frame: Frame,
    val duration: Int
)
