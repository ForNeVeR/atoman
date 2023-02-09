package me.fornever.atoman.map

import me.fornever.atoman.sprites.Sprite

internal enum class CellType {
    Empty,
    Player,
    Ghost,
    Wall,
    Pill
}

internal data class Cell(val type: CellType, val timeFromFrameStart: Int, val frameIndex: Int) {
    val spriteName = when (type) {
        CellType.Empty -> null
        CellType.Player -> "Pacman-Chomping-Right"
        CellType.Ghost -> "Red-Ghost-Right"
        CellType.Wall -> "Wall"
        CellType.Pill -> "Pill"
    }

    fun update() {
        if (this.type === CellType.Empty) {
            return
        }

        val frames = Sprite.getFrames(sprite)
        let frame = frames[this.frameIndex];
        if (frame.duration == 0) { // Static frame
            return;
        }

        let currentTime = this.timeFromFrameStart;
        while (delta > 0) {
            let step = Math.min(frame.duration - currentTime, delta);
            delta -= step;
            currentTime += step;

            if (currentTime >= frame.duration) {
                currentTime -= frame.duration;
                this.frameIndex = (this.frameIndex + 1) % frames.length;
                frame = frames[this.frameIndex];
            }
        }

        this.timeFromFrameStart = currentTime;
    }
}
