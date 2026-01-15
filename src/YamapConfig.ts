import NativeModule from './specs/NativeYamap';
import { invariant } from './utils/invariant';

export const YamapConfig = (() =>
  NativeModule ?? invariant('Yamap native module is not linked.'))();
