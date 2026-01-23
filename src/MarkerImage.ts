import { type ImageResolvedAssetSource } from 'react-native';

export class MarkerImage {
  readonly src: ImageResolvedAssetSource;
  constructor(src: ImageResolvedAssetSource | undefined) {
    if (!src) throw new Error('MarkerImage source cannot be undefined');
    this.src = src;
  }
}
