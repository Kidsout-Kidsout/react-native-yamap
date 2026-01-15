import type { TurboModule } from "react-native/Libraries/TurboModule/RCTExport";
import { TurboModuleRegistry } from "react-native";
import { Double } from "react-native/Libraries/Types/CodegenTypes";

type Point = { lat: Double; lon: Double };

type AddressComponent =
  | "unknown"
  | "country"
  | "region"
  | "province"
  | "area"
  | "locality"
  | "district"
  | "street"
  | "house"
  | "entrance"
  | "route"
  | "station"
  | "metro"
  | "railway"
  | "vegetation"
  | "hydro"
  | "airport"
  | "other";

export type YamapGeocodeResult = {
  name: string;
  descriptionText: string;
  formattedAddress: string;
  coords: Point;
  upperCorner: Point;
  lowerCorner: Point;
  components: { name: string; kinds: AddressComponent[] }[];
};

export interface Spec extends TurboModule {
  geocode(query: string): Promise<YamapGeocodeResult | undefined>;
  geocodeUri(uri: string): Promise<YamapGeocodeResult | undefined>;
  geocodePoint(coords: number[]): Promise<YamapGeocodeResult | undefined>;
}

export default TurboModuleRegistry.get<Spec>("YamapGeocode");
