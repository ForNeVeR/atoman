import libxpm = require('libxpm');

import ResourceManager = require('./resource-manager');

interface SpriteFrameInfo {
  frame: {
    x: number;
    y: number;
    w: number;
    h: number;
  };
  duration: number;
}

interface SpriteInfo {
  frames: { [name: string]: SpriteFrameInfo };
}

class Sprite {
  static wall = {
    name: 'Wall',
    image: Sprite.getWallImage(),
    info: {
      frames: {
        'Wall 0': {
          frame: {
            x: 0,
            y: 0,
            w: 40,
            h: 40
          },
          duration: '0'
        }
      }
    }
  }

  static getWallImage() {
    let canvas = document.createElement('canvas');
    canvas.width = canvas.height = 40;
    let context = canvas.getContext('2d');
    context.fillStyle = 'green';
    context.fillRect(0, 0, 40, 40);

    let img = document.createElement('img');
    img.src = canvas.toDataURL();
    return img;
  }

  static load(name: string): Promise<Sprite> {
    return Promise.all([
      ResourceManager.textFile(`pacmacs/sprites/${name}.xpm`),
      ResourceManager.textFile(`pacmacs/sprites/${name}.json`)])
      .then(([image, info]) => {
        var xpmData = image.replace(/\r\n/g, '\n');
        return {
          name,
          image: libxpm.xpm_to_img(xpmData),
          info: <SpriteInfo> JSON.parse(info)
        };
      });
  }

  name: string;
  image: HTMLImageElement;
  info: SpriteInfo;
}

export = Sprite;
