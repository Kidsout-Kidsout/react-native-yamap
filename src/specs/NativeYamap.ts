import { type TurboModule, TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  init(apiKey: string): Promise<void>;
  setLocale(locale: string): Promise<void>;
  getLocale(): Promise<string>;
  resetLocale(): Promise<void>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('Yamap');
