import { createAbortError } from './abort';
import { PromiseWithResolvers } from './PromiseWithResolvers';

export class CallbacksManager {
  private static callbacks = new Map<string, PromiseWithResolvers<any>>();

  private listeners = new Map<
    string,
    (data: { nativeEvent: { cid: string } }) => void
  >();

  register<T>() {
    const id = guid();
    const p = PromiseWithResolvers<T>();
    CallbacksManager.callbacks.set(id, p);
    setTimeout(() => {
      if (CallbacksManager.callbacks.has(id)) {
        p.reject(createAbortError('Callback timeout'));
        CallbacksManager.callbacks.delete(id);
      }
    }, 10000);
    return { promise: p.promise, id };
  }

  private call(data: { cid: string }) {
    const p = CallbacksManager.callbacks.get(data.cid);
    if (p) {
      p.resolve(data);
      CallbacksManager.callbacks.delete(data.cid);
    }
  }

  createListener(eventName: string) {
    if (!this.listeners.has(eventName)) {
      this.listeners.set(eventName, (data) => {
        this.call(data.nativeEvent);
      });
    }
    return this.listeners.get(eventName)!;
  }
}

function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return `${s4()}${s4()}-${s4()}-${s4()}-${s4()}-${s4()}${s4()}${s4()}`;
}
