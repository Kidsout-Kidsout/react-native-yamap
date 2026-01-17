import { processColor } from 'react-native';

export function processColorProps<T>(props: T, ...keys: (keyof T)[]) {
  for (const name of keys) {
    if (typeof props[name] === 'number' || typeof props[name] === 'string') {
      props = { ...props, [name]: processColor(props[name] as any) };
    }
  }
  return props;
}

export function guid() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return `${s4()}${s4()}-${s4()}-${s4()}-${s4()}-${s4()}${s4()}${s4()}`;
}
