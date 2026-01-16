import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface YamapSpec extends TurboModule {
  init(apiKey: string): Promise<void>;
  setLocale(locale: string): Promise<void>;
  getLocale(): Promise<string>;
  resetLocale(): Promise<void>;
}

export default TurboModuleRegistry.get<YamapSpec>('Yamap');
