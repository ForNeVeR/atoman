fs = require 'fs'
path = require 'path'

module.exports =
  class AtomanView
    @@spriteNames = [
      'Pacman-Chomping-Down'
      'Pacman-Chomping-Left'
      'Pacman-Chomping-Right'
      'Pacman-Chomping-Up'
      'Pacman-Death'
      'Pill'
      'Red-Ghost-Down'
      'Red-Ghost-Left'
      'Red-Ghost-Right'
      'Red-Ghost-Up'
    ]

    @canvas = null
    @map = null

    constructor: (serializedState) ->
      @canvas = document.createElement 'canvas'
      @canvas.classList.add 'atoman'

      @load().then(
        => @startGame()
        => showError())

    serialize: ->

    destroy: ->
      @canvas.remove()

    load: ->
      console.log 'Atoman', 'Loading game...'
      Promise.all @loadMap(), @loadSprites()

    loadMap: ->
      filePath = path.join(
        atom.packages.resolvePackagePath 'atoman'
        'pacmacs/maps/map01.txt')
      new Promise (resolve, reject) =>
        fs.readFile filePath, (error, data) =>
          if error
            reject error
          else
            console.log 'Atoman', 'Map loaded.', data
            @map = data
            resolve()
          return
        return

    loadSprites: ->
      # TODO: Load sprites.
