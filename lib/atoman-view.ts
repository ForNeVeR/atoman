import fs = require('fs');
import path = require('path');

class AtomanView {
  static spriteNames = [
    'Pacman-Chomping-Down',
    'Pacman-Chomping-Left',
    'Pacman-Chomping-Right',
    'Pacman-Chomping-Up',
    'Pacman-Death',
    'Pill',
    'Red-Ghost-Down',
    'Red-Ghost-Left',
    'Red-Ghost-Right',
    'Red-Ghost-Up'
  ];

  canvas: HTMLCanvasElement;
  map: any;

  constructor(serializedState) {
    this.canvas = document.createElement('canvas')
    this.canvas.classList.add('atoman')

    this.load().then(
      () => { /* TODO: Start game. */ },
      () => { /* TODO: Show error. */ });
  }

  serialize() {
  }

  destroy() {
    this.canvas.remove();
  }

  load() {
    console.log('Atoman', 'Loading game...');
    return Promise.all([this.loadMap(), this.loadSprites()]);
  }

  loadMap() {
    var filePath = path.join(
      atom.packages.resolvePackagePath('atoman'),
      'pacmacs/maps/map01.txt');
    return new Promise((resolve, reject) => {
      fs.readFile(filePath, (error, data) => {
        if (error) {
          reject(error);
        } else {
          console.log('Atoman', 'Map loaded.', data);
          this.map = data;
          resolve()
        }
      });
    });
  }

  loadSprites(): Promise<{}> {
    return new Promise(() => {});
  }
}

export = AtomanView;
