declare function waitsForPromise<T>(action: () => Promise<T>): void;

declare module jasmine {
  interface Matchers {
    toExist(): boolean;
  }
}
