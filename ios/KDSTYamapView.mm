#import "KDSTYamapView.h"

#import "KDSTYamapCircleView.h"
#import "KDSTYamapMarkerView.h"
#import "KDSTYamapPolygonView.h"

#import <react/renderer/components/RNYamapSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNYamapSpec/EventEmitters.h>
#import <react/renderer/components/RNYamapSpec/Props.h>
#import <react/renderer/components/RNYamapSpec/RCTComponentViewHelpers.h>

#import <MapKit/MapKit.h>
#import <YandexMapsMobile/YMKMap.h>
#import <YandexMapsMobile/YMKPoint.h>
#import <YandexMapsMobile/YMKRequestPoint.h>
#import <YandexMapsMobile/YMKMapView.h>
#import <YandexMapsMobile/YMKUserLocation.h>
#import <YandexMapsMobile/YMKCameraPosition.h>
#import <YandexMapsMobile/YMKMapCameraListener.h>
#import <YandexMapsMobile/YMKClusterListener.h>
#import <YandexMapsMobile/YMKClusterTapListener.h>
#import <YandexMapsMobile/YMKMapLoadedListener.h>
#import <YandexMapsMobile/YMKTrafficListener.h>
#import <YandexMapsMobile/YMKMapObjectCollection.h>
#import <YandexMapsMobile/YMKMapLoadStatistics.h>

using namespace facebook::react;

@interface KDSTYamapView () <RCTYamapViewViewProtocol, YMKMapCameraListener, YMKMapLoadedListener, YMKMapInputListener>
@end

@implementation KDSTYamapView {
  YMKMapView * _mapView;
}

-(instancetype)init
{
  if(self = [super init]) {
    _mapView = [YMKMapView new];
    [_mapView.mapWindow.map addCameraListenerWithCameraListener:self];
    [_mapView.mapWindow.map setMapLoadedListenerWithMapLoadedListener:self];
    [_mapView.mapWindow.map addInputListenerWithInputListener:self];
    [self addSubview:_mapView];
  }
  return self;
}

- (void)dealloc {
  [_mapView.mapWindow.map removeCameraListenerWithCameraListener:self];
  [_mapView.mapWindow.map removeInputListenerWithInputListener:self];
  _mapView = nil;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto initial = oldProps == NULL;
  const auto &o = *std::static_pointer_cast<YamapViewProps const>(oldProps);
  const auto &n = *std::static_pointer_cast<YamapViewProps const>(props);
  
  if (initial || n.nightMode != o.nightMode) {
    [_mapView.mapWindow.map setNightModeEnabled:n.nightMode];
  }
  if (initial || n.mapType != o.mapType) {
    switch (n.mapType) {
      case facebook::react::YamapViewMapType::Raster:
        [_mapView.mapWindow.map setMapType:YMKMapTypeMap];
        break;
      case facebook::react::YamapViewMapType::Vector:
        [_mapView.mapWindow.map setMapType:YMKMapTypeVectorMap];
        break;
      case facebook::react::YamapViewMapType::Satellite:
        [_mapView.mapWindow.map setMapType:YMKMapTypeSatellite];
        break;
      case facebook::react::YamapViewMapType::Hybrid:
        [_mapView.mapWindow.map setMapType:YMKMapTypeHybrid];
        break;
      case facebook::react::YamapViewMapType::None:
      default:
        [_mapView.mapWindow.map setMapType:YMKMapTypeNone];
        break;
    }
  }
  if (initial || n.scrollGesturesEnabled != o.scrollGesturesEnabled ) {
    [_mapView.mapWindow.map setScrollGesturesEnabled:n.scrollGesturesEnabled];
  }
  if (initial || n.zoomGesturesEnabled != o.zoomGesturesEnabled ) {
    [_mapView.mapWindow.map setZoomGesturesEnabled:n.zoomGesturesEnabled];
  }
  if (initial || n.tiltGesturesEnabled != o.tiltGesturesEnabled ) {
    [_mapView.mapWindow.map setTiltGesturesEnabled:n.tiltGesturesEnabled];
  }
  if (initial || n.rotateGesturesEnabled != o.rotateGesturesEnabled ) {
    [_mapView.mapWindow.map setRotateGesturesEnabled:n.rotateGesturesEnabled];
  }
  if (initial || n.fastTapEnabled != o.fastTapEnabled ) {
    [_mapView.mapWindow.map setFastTapEnabled:n.fastTapEnabled];
  }
  if (initial || n.maxFps != o.maxFps ) {
    [_mapView.mapWindow setMaxFpsWithFps:n.maxFps];
  }
  
  [super updateProps:props oldProps:oldProps];
}

-(void)layoutSubviews
{
  [super layoutSubviews];
  _mapView.frame = self.bounds;
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  auto objects = _mapView.mapWindow.map.mapObjects;
  
  if ([childComponentView isKindOfClass:[KDSTYamapCircleView class]]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      auto *view = (KDSTYamapCircleView *)childComponentView;
      [view setCollection:objects];
    });
    return;
  }
  
  if ([childComponentView isKindOfClass:[KDSTYamapMarkerView class]]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      auto *view = (KDSTYamapMarkerView *)childComponentView;
      [view setCollection:objects];
    });
    return;
  }
  
  if ([childComponentView isKindOfClass:[KDSTYamapPolygonView class]]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      auto *view = (KDSTYamapPolygonView *)childComponentView;
      [view setCollection:objects];
    });
    return;
  }
  
  [super mountChildComponentView:childComponentView index:index];
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  if ([childComponentView isKindOfClass:[KDSTYamapCircleView class]]) {
    auto *view = (KDSTYamapCircleView *)childComponentView;
    [view unmount];
    return;
  }
  if ([childComponentView isKindOfClass:[KDSTYamapMarkerView class]]) {
    auto *view = (KDSTYamapCircleView *)childComponentView;
    [view unmount];
    return;
  }
  if ([childComponentView isKindOfClass:[KDSTYamapPolygonView class]]) {
    auto *view = (KDSTYamapPolygonView *)childComponentView;
    [view unmount];
    return;
  }
  
  [super unmountChildComponentView:childComponentView index:index];
}

- (void)onCameraPositionChangedWithMap:(YMKMap *)map cameraPosition:(YMKCameraPosition *)cameraPosition cameraUpdateReason:(YMKCameraUpdateReason)cameraUpdateReason finished:(BOOL)finished {
  auto em = [self eventEmitter];
  YamapViewEventEmitter::OnCameraPositionChangePoint point;
  YamapViewEventEmitter::OnCameraPositionChange value;
  value.zoom = cameraPosition.zoom;
  value.tilt = cameraPosition.tilt;
  value.azimuth = cameraPosition.azimuth;
  value.finished = finished;
  YamapViewEventEmitter::OnCameraPositionChangeReason reason;
  switch (cameraUpdateReason) {
    case YMKCameraUpdateReasonGestures:
      reason = YamapViewEventEmitter::OnCameraPositionChangeReason::Application;
    case YMKCameraUpdateReasonApplication:
    default:
      reason = YamapViewEventEmitter::OnCameraPositionChangeReason::Application;
  }
  value.reason = reason;
  value.point = point;
  em.onCameraPositionChange(value);
}

- (void)onMapLoadedWithStatistics:(YMKMapLoadStatistics *)statistics {
  auto em = [self eventEmitter];
  YamapViewEventEmitter::OnMapLoaded value;
  value.renderObjectCount = (int)statistics.renderObjectCount;
  value.curZoomModelsLoaded = statistics.curZoomModelsLoaded;
  value.curZoomPlacemarksLoaded = statistics.curZoomPlacemarksLoaded;
  value.curZoomLabelsLoaded = statistics.curZoomLabelsLoaded;
  value.curZoomGeometryLoaded = statistics.curZoomGeometryLoaded;
  value.tileMemoryUsage = (int)statistics.tileMemoryUsage;
  value.delayedGeometryLoaded = statistics.delayedGeometryLoaded;
  value.fullyAppeared = statistics.fullyAppeared;
  value.fullyLoaded = statistics.fullyLoaded;
  em.onMapLoaded(value);
}

- (void)onMapTapWithMap:(YMKMap *)map point:(YMKPoint *)point {
  auto em = [self eventEmitter];
  YamapViewEventEmitter::OnMapPress value;
  value.lat = point.latitude;
  value.lon = point.longitude;
  em.onMapPress(value);
}

- (void)onMapLongTapWithMap:(YMKMap *)map point:(YMKPoint *)point {
  auto em = [self eventEmitter];
  YamapViewEventEmitter::OnMapLongPress value;
  value.lat = point.latitude;
  value.lon = point.longitude;
  em.onMapLongPress(value);
}


// Event emitter convenience method
- (const YamapViewEventEmitter &)eventEmitter
{
  return static_cast<const YamapViewEventEmitter &>(*_eventEmitter);
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<YamapViewComponentDescriptor>();
}

@end
