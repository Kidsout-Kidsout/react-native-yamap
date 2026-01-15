import { Ref, useCallback, useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { BUTTON_COLOR } from './constants';

export const sleep = (ms: number) =>
  new Promise<void>((resolve) => {
    setTimeout(resolve, ms);
  });

export const useBoolean = (label: string, initialValue: boolean) => {
  const [value, setValue] = useState(initialValue);

  const handleToggle = useCallback(() => setValue((v) => !v), []);

  const element = (
    <View style={styles.row}>
      <Text onPress={handleToggle} style={{ color: BUTTON_COLOR }}>
        {label} ({value ? 'on' : 'off'})
      </Text>
    </View>
  );

  return [value, element] as const;
};

export const useListToggle = <const T extends any>(
  label: string,
  initialList: T[]
) => {
  const [value, setValue] = useState<T>(initialList[0]);

  const handleToggle = useCallback(() => {
    setValue((v) => {
      const currentIndex = initialList.indexOf(v);
      const nextIndex = (currentIndex + 1) % initialList.length;
      return initialList[nextIndex];
    });
  }, [initialList]);

  const element = (
    <View style={styles.row}>
      <Text onPress={handleToggle} style={{ color: BUTTON_COLOR }}>
        {label}: {String(value)}
      </Text>
    </View>
  );

  return [value, element] as const;
};

export const useMergedRefs = <T,>(...refs: (Ref<T> | undefined)[]) => {
  return useCallback((instance: T) => {
    refs.forEach((ref) => {
      if (typeof ref === 'function') {
        ref(instance);
      } else if (ref != null) {
        // @ts-ignore
        ref.current = instance;
      }
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
};

const styles = StyleSheet.create({
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
});
