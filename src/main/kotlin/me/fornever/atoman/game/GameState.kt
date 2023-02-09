package me.fornever.atoman.game

import java.awt.image.BufferedImage

sealed interface GameState

object LoadingMap : GameState
object LoadingSprites : GameState
data class Loaded(val size: Pair<Int, Int>, val sprite: BufferedImage, val index: Int) : GameState
