import React from 'react';
import { Platform, type ImageSourcePropType } from 'react-native';
// @ts-ignore
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';
import { type Point } from '../interfaces';

export interface MarkerProps {
  children?: React.ReactElement;
  zIndex?: number;
  scale?: number;
  onPress?: () => void;
  point: Point;
  source?: ImageSourcePropType;
  anchor?: { x: number; y: number };
  visible?: boolean;
}

import YamapMarker, { Commands } from '../specs/YamapMarker';

interface State {
  recreateKey: boolean;
  children: any;
}

export class Marker extends React.Component<MarkerProps, State> {
  state = {
    recreateKey: false,
    children: this.props.children,
  };

  private refEl: any = null;

  static getDerivedStateFromProps(
    nextProps: MarkerProps,
    prevState: State
  ): Partial<State> {
    if (Platform.OS === 'ios') {
      return {
        children: nextProps.children,
        recreateKey:
          nextProps.children === prevState.children
            ? prevState.recreateKey
            : !prevState.recreateKey,
      };
    }

    return {
      children: nextProps.children,
      recreateKey: Boolean(nextProps.children),
    };
  }

  private resolveImageUri(img?: ImageSourcePropType) {
    return img ? resolveAssetSource(img).uri : '';
  }

  private getProps() {
    return {
      ...this.props,
      source: this.resolveImageUri(this.props.source),
    };
  }

  public animatedMoveTo(coords: Point, duration: number) {
    // @ts-ignore
    if (this.refEl) {
      Commands.animatedMoveTo(this.refEl, coords.lat, coords.lon, duration);
    }
  }

  public animatedRotateTo(angle: number, duration: number) {
    // @ts-ignore
    if (this.refEl) {
      Commands.animatedRotateTo(this.refEl, angle, duration);
    }
  }

  render() {
    return (
      <YamapMarker
        {...this.getProps()}
        key={String(this.state.recreateKey)}
        pointerEvents="none"
        // capture ref for commands
        // @ts-ignore
        ref={(ref) => (this.refEl = ref)}
      />
    );
  }
}
