/// <reference path="../typings/tsd.d.ts"/>

import AtomanView = require('./atoman-view');

var CompositeDisposable = atom.CompositeDisposable;

class Atoman {
  modalPanel: AtomCore.Panel;
  view: AtomanView;
  subscriptions: AtomCore.CompositeDisposable;

  activate(state) {
    this.view = new AtomanView(state.atomanViewState);
    this.modalPanel = atom.workspace.addModalPanel({
      item: this.view.canvas,
      visible: false
    });

    this.subscriptions = new CompositeDisposable();
    this.subscriptions.add(atom.commands.add('atom-workspace', {
      'atoman:start': () => this.start()
    }));
  }

  deactivate() {
    this.modalPanel.destroy();
    this.subscriptions.dispose();
    this.view.destroy();
  }

  serialize() {
    return {
      atomanViewState: this.view.serialize()
    };
  }

  start() {
    this.modalPanel.show();
  }
}

export = new Atoman();
