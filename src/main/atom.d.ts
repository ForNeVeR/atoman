declare module AtomCore {
  interface Panel {
    destroy(): void;
  }

  interface ICommandRegistry {
    add(selector: string, commands: { [name: string]: (event: any) => void }): Disposable;
  }

  interface IWorkspace {
    panelForItem(item: HTMLElement): Panel;
  }
}

declare module 'atom' {
  export class CompositeDisposable {
    add(disposable: AtomCore.Disposable): void;
    dispose(): void;
  }
}
