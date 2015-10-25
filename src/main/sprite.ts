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
  meta: { size: { w: number; h: number; } }
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
      .then(([imageData, infoData]) => {
        let xpmData = imageData.replace(/\r\n/g, '\n');
        let info = JSON.parse(infoData);
        let image = Sprite.loadSpriteImageFrom(info, xpmData);
        return {
          name,
          info,
          image
        };
      });
  }

  static loadSpriteImageFrom(info: SpriteInfo, xpmData: string) {
    let image = libxpm.xpm_to_img(xpmData);

    // Need to resample the image.
    let canvas = document.createElement('canvas');
    let context = canvas.getContext('2d');
    let size = info.meta.size;
    context.drawImage(image, 0, 0, size.w, size.h);

    image.src = canvas.toDataURL();
    return image;
  }

  static getFrames(sprite: Sprite) {
    let info = sprite.info;
    let frames = Object.keys(info.frames).map(name => {
      return info.frames[name];
    });

    return frames;
  }

  name: string;
  info: SpriteInfo;
  image: HTMLImageElement;
}

export = Sprite;
