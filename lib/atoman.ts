/// <reference path="../typings/tsd.d.ts"/>

import AtomanView = require('./atoman-view');

var CompositeDisposable = atom.CompositeDisposable;

class Atoman {
  atomanView: AtomanView;
  subscriptions: AtomCore.CompositeDisposable;

  activate(state) {
    this.atomanView = new AtomanView(state.atomanViewState);

    this.subscriptions = new CompositeDisposable();
    this.subscriptions.add(atom.commands.add('atom-workspace', {
      'atoman:start': () => this.start()
    }));
  }

  deactivate() {
    this.subscriptions.dispose()
    this.atomanView.destroy()
  }

  serialize() {
    return {
      atomanViewState: this.atomanView.serialize()
    };
  }

  start() {
    console.log('atoman:start');
  }
}

export = Atoman;
