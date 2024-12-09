import { NativeModules } from "react-native";
import { AddressComponent, Point } from "./interfaces";

const { YamapGeocode } = NativeModules;

export type YamapGeocodeResult = {
  name: string;
  descriptionText: string;
  formattedAddress: string;
  coords: Point;
  upperCorner: Point;
  lowerCorner: Point;
  components: { name: string; kinds: AddressComponent[] }[];
};

type GeocodeFetcher = (
  query: string | { uri: string } | Point
) => Promise<YamapGeocodeResult | undefined>;
const geocode: GeocodeFetcher = (query) =>
  typeof query === "string"
    ? YamapGeocode.geocode(query)
    : "uri" in query
    ? YamapGeocode.geocodeUri(query.uri)
    : YamapGeocode.geocodePoint([query.lat, query.lon]);

/**
 * This version of Geocode doesn't use http-geocoding and doesn't require separate api key
 */
const Geocode = {
  geocode,
};

export default Geocode;
