/// <reference path="../../typings/tsd.d.ts"/>
import Atoman = require('../main/atoman');

describe('Atoman', () => {
  let workspaceElement, activationPromise;
  beforeEach(() => {
    workspaceElement = atom.views.getView(atom.workspace);
    activationPromise = atom.packages.activatePackage('atoman');
  });

  describe('when the atoman:start event is triggered', () => {
    it('shows the modal panel', () => {
      expect(workspaceElement.querySelector('.atoman')).not.toExist();
      atom.commands.dispatch(workspaceElement, 'atoman:start');

      waitsForPromise(() => activationPromise);

      runs(() => {
        let atomanElement = workspaceElement.querySelector('.atoman');
        expect(atomanElement).toExist();

        let atomanPanel = atom.workspace.panelForItem(atomanElement);
        expect(atomanPanel.isVisible()).toBe(true);
      });
    });
  });
});
