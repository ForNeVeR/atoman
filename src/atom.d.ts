declare module AtomCore {
  interface CompositeDisposable {
    add(disposable: Disposable): void;
    dispose(): void;
  }

  interface IAtom {
    CompositeDisposable: new () => CompositeDisposable;
  }

  interface ICommandRegistry {
    add(selector: string, commands: { [name: string]: (event: any) => void }): Disposable;
  }
}
