import { type Point, type YamapGeocodeResult } from './interfaces';
import NativeModule from './specs/NativeYamapGeocode';
import { invariant } from './utils/invariant';

const getModule = () =>
  NativeModule ?? invariant('YamapGeocode native module is not linked.');

type GeocodeFetcher = (
  query: string | { uri: string } | Point
) => Promise<YamapGeocodeResult | undefined>;

const geocode: GeocodeFetcher = (query) => {
  const YamapGeocode = getModule();

  if (typeof query === 'string') {
    return YamapGeocode.geocode(query);
  }

  if ('uri' in query) {
    return YamapGeocode.geocodeUri(query.uri);
  }

  return YamapGeocode.geocodePoint([query.lat, query.lon]);
};

/**
 * This version of Geocode doesn't use http-geocoding and doesn't require separate api key
 */
export const Geocode = {
  geocode,
};
