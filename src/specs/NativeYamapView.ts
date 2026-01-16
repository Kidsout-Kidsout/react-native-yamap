import * as React from 'react';
import {
  type HostComponent,
  type ViewProps,
  codegenNativeCommands,
  codegenNativeComponent,
  type CodegenTypes,
} from 'react-native';

export interface NativeProps extends ViewProps {}

export default codegenNativeComponent<NativeProps>('YamapView');
