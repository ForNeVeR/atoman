import Map = require('./map');
import ResourceManager = require('./resource-manager');
import Sprite = require('./sprite');

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
  map: Map;
  sprites: { [name: string]: HTMLImageElement };

  constructor(serializedState) {
    this.canvas = document.createElement('canvas');
    this.canvas.classList.add('atoman');

    this.load().then(
      () => {
        console.log('Atoman loaded');
        this.render();
      },
      (error) => { console.error(error); });
  }

  render() {

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

  loadMap(): Promise<Map> {
    return ResourceManager.textFile('pacmacs/maps/map01.txt').then(Map.parse);
  }

  loadSprites(): Promise<{ [name: string]: HTMLImageElement }> {
    return new Promise((resolve, reject) => {
      Promise.all(AtomanView.spriteNames.map(name => this.loadSprite(name)))
        .then(resolve, reject);
    });
  }

  loadSprite(name: string): Promise<Sprite> {
    return Sprite.load(name);
  }
}

export = AtomanView;
