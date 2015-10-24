import fs = require('fs');
import path = require('path');
import libxpm = require('libxpm');

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

  static loadResource(fileName: string): Promise<Buffer> {
    var filePath = path.join(
      atom.packages.resolvePackagePath('atoman'),
      fileName);

    return new Promise((resolve, reject) => {
      fs.readFile(filePath, (error, data) => {
        if (error) {
          reject(error);
        } else {
          resolve(data);
        }
      });
    });
  }

  canvas: HTMLCanvasElement;
  map: Buffer;
  sprites: { [name: string]: HTMLImageElement };

  constructor(serializedState) {
    this.canvas = document.createElement('canvas');
    this.canvas.classList.add('atoman');

    this.load().then(
      () => { console.log('Atoman loaded') },
      (error) => { console.error(error); });
  }

  serialize() {
  }

  destroy() {
    this.canvas.remove();
  }

  load(): Promise<{}> {
    return Promise.all<{}>([
      this.loadMap().then(map => this.map = map),
      this.loadSprites().then(sprites => this.sprites = sprites)]);
  }

  loadMap(): Promise<Buffer> {
    return AtomanView.loadResource('pacmacs/maps/map01.txt');
  }

  loadSprites(): Promise<{ [name: string]: HTMLImageElement }> {
    return new Promise((resolve, reject) => {
      Promise.all(AtomanView.spriteNames.map(name => this.loadSprite(name)))
        .then(resolve, reject);
    });
  }

  loadSprite(name: string) {
    return AtomanView.loadResource(`pacmacs/sprites/${name}.xpm`).then((data) => {
      return libxpm.xpm_to_img(data.toString().replace(/\r\n/g, '\n'));
    });
  }
}

export = AtomanView;
