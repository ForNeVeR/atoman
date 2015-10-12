declare module AtomCore {
  interface CompositeDisposable {
    add(command: Function, name: string, dispatch: any): void;
    dispose(): void;
  }

  interface IAtom {
    CompositeDisposable: new () => CompositeDisposable;
  }
}
