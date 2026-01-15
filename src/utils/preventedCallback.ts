import { useCallback } from 'react';
import type { CodegenTypes } from 'react-native';

export const usePreventedCallback = <T>(
  callback: CodegenTypes.BubblingEventHandler<T> | undefined
) => {
  return useCallback<CodegenTypes.BubblingEventHandler<T>>(
    (e) => {
      e.preventDefault();
      e.stopPropagation();
      callback?.(e);
    },
    [callback]
  );
};
