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
  static load(name: string): Promise<Sprite> {
    return Promise.all([
      ResourceManager.textFile(`pacmacs/sprites/${name}.xpm`),
      ResourceManager.textFile(`pacmacs/sprites/${name}.json`)])
      .then(([image, info]) => {
        var xpmData = image.replace(/\r\n/g, '\n');
        return {
          image: libxpm.xpm_to_img(xpmData),
          info: <SpriteInfo> JSON.parse(info)
        };
      });
  }

  image: HTMLImageElement;
  info: SpriteInfo;
}

export = Sprite;
