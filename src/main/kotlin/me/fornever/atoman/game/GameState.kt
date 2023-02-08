package me.fornever.atoman.game

sealed interface GameState

object Loading : GameState
data class Loaded(val size: Pair<Int, Int>) : GameState
