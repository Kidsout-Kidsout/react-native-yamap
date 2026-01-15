import { type AddressComponent, type Point } from './interfaces';
import NativeModule from './specs/YamapGeocode';
import { invariant } from './utils/invariant';

export type YamapGeocodeResult = {
  name: string;
  descriptionText: string;
  formattedAddress: string;
  coords: Point;
  upperCorner: Point;
  lowerCorner: Point;
  components: { name: string; kinds: AddressComponent[] }[];
};

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
