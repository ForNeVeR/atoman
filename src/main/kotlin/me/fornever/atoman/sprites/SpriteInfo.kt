package me.fornever.atoman.sprites

interface SpriteInfo {
    frames: { [name: string]: SpriteFrameInfo };
    meta: { size: { w: number; h: number; } }
}
