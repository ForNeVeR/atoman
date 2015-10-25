import fs = require('fs');
import path = require('path');

export function textFile(fileName: string): Promise<string> {
  var filePath = path.join(
    atom.packages.resolvePackagePath('atoman'),
    fileName);

  return new Promise((resolve, reject) => {
    fs.readFile(filePath, 'utf-8', (error, content) => {
      if (error) {
        reject(error);
      } else {
        resolve(content);
      }
    });
  });
}
