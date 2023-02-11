package me.fornever.atoman.map

import me.fornever.atoman.sprites.Sprite

enum class CellType {
    Empty, Player, Ghost, Wall, Pill
}

data class Cell(val type: CellType, var timeFromFrameStart: Int = 0, var frameIndex: Int = 0) {

    fun update(delta: Int, sprite: Sprite) {
        if (this.type === CellType.Empty) {
            return
        }

        val frames = Sprite.getFrames(sprite)
        var frame = frames[this.frameIndex]
        if (frame.duration == 0) { // Static frame
            return;
        }

        var currentTime = this.timeFromFrameStart
        var currentDelta = delta
        while (currentDelta > 0) {
            val step = (frame.duration - currentTime).coerceAtMost(currentDelta)
            currentDelta -= step
            currentTime += step

            if (currentTime >= frame.duration) {
                currentTime -= frame.duration
                this.frameIndex = (this.frameIndex + 1) % frames.size
                frame = frames[this.frameIndex]
            }
        }

        this.timeFromFrameStart = currentTime
    }
}
