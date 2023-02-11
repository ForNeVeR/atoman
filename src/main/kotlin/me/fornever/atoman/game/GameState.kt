package me.fornever.atoman.game

import me.fornever.atoman.map.CellType
import me.fornever.atoman.map.GameMap
import me.fornever.atoman.sprites.Sprite

sealed interface GameState

object LoadingMap : GameState
object LoadingSprites : GameState
data class Loaded(
    val map: GameMap,
    val sprites: Map<CellType, Sprite>
) : GameState
