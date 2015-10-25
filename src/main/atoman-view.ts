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

  static spriteSize = 40;

  element: HTMLElement;
  canvas: HTMLCanvasElement;
  map: Map;
  sprites: { [name: string]: Sprite };
  time: number = null;

  constructor(serializedState) {
    this.element = document.createElement('div');
    this.element.classList.add('atoman');

    this.load().then(
      () => {
        console.log('Atoman loaded');
        this.initCanvas();

        window.requestAnimationFrame((time) => this.update(time));
      },
      (error) => { console.error(error); });
  }

  initCanvas() {
    let width = this.map.width * AtomanView.spriteSize;
    let height = this.map.height * AtomanView.spriteSize;
    this.canvas = document.createElement('canvas');
    this.canvas.width = width;
    this.canvas.height = height;

    while (this.element.firstChild != null) {
      this.element.removeChild(this.element.firstChild);
    }

    this.element.appendChild(this.canvas);
  }

  update(time: number) {
    if (this.time !== null) {
      let delta = time - this.time;
      this.map.forEachCell((cell, x, y) => {
        let sprite = this.getSprite(cell);
        cell.update(delta, sprite);
      });
    }

    this.time = time;
    this.render();

    requestAnimationFrame((time) => this.update(time));
  }

  render() {
    let context = this.canvas.getContext('2d');
    this.map.forEachCell((cell, x, y) => {
      let sprite = this.getSprite(cell);
      if (sprite != null) {
        this.renderSprite(context, sprite, cell, x, y);
      }
    });
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

  loadSprites(): Promise<{ [name: string]: Sprite }> {
    return Promise.all(
      AtomanView.spriteNames.map(name => this.loadSprite(name)))
      .then(sprites => {
        let map = {};
        sprites.forEach(sprite => {
          map[sprite.name] = sprite;
        });

        map['Wall'] = Sprite.wall;
        return map;
      });
  }

  loadSprite(name: string): Promise<Sprite> {
    return Sprite.load(name);
  }

  renderSprite(
    context: CanvasRenderingContext2D,
    sprite: Sprite,
    cell: Map.Cell,
    x: number,
    y: number) {
    let frame = this.getFrame(sprite, cell.getFrameNumber()).frame;
    context.drawImage(
      sprite.image,
      frame.x,
      frame.y,
      frame.w,
      frame.h,
      x * AtomanView.spriteSize,
      y * AtomanView.spriteSize,
      AtomanView.spriteSize,
      AtomanView.spriteSize);
  }

  getSprite(cell: Map.Cell): Sprite {
    let spriteName = cell.getSpriteName();
    return this.sprites[spriteName];
  }

  getFrame(sprite: Sprite, number: number) {
    let frames = Sprite.getFrames(sprite);
    return frames[number % frames.length];
  }
}

export = AtomanView;
