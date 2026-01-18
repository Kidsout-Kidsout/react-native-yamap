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
#import <YandexMapsMobile/YMKVisibleRegion.h>

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

// Events

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

// Commands

- (void)handleCommand:(const NSString *)commandName args:(const NSArray *)args {
  RCTYamapViewHandleCommand(self, commandName, args);
}

- (void)commandSetCenter:(NSString *)cid lat:(double)lat lon:(double)lon zoom:(double)zoom azimuth:(double)azimuth tilt:(double)tilt offset:(double)offset animationType:(NSInteger)animationType animationDuration:(double)animationDuration {
  auto cpos = [_mapView.mapWindow.map cameraPosition];
  YMKPoint *point = [YMKPoint pointWithLatitude:lat longitude:lon];
  auto nzoom = zoom == 0 ? cpos.zoom : zoom;
  if (zoom != 0) {
    nzoom = zoom - offset;
  }
  auto nazimuth = azimuth == 0 ? cpos.azimuth : azimuth;
  auto ntilt = tilt == 0 ? cpos.tilt : tilt;
  YMKCameraPosition *pos = [YMKCameraPosition cameraPositionWithTarget:point zoom:nzoom azimuth:nazimuth tilt:ntilt];
  auto anType = animationType == 1 ? YMKAnimationTypeSmooth : YMKAnimationTypeLinear;
  YMKAnimation *anim = [YMKAnimation animationWithType:anType duration:animationDuration];
  
  [_mapView.mapWindow.map moveWithCameraPosition:pos animation:anim cameraCallback:^(BOOL completed) {
    auto em = [self eventEmitter];
    
    YamapViewEventEmitter::OnCommandSetCenterReceived value;
    value.cid = std::string([cid UTF8String]);
    value.completed = completed;
    em.onCommandSetCenterReceived(value);
  }];
}

- (void)commandSetBounds:(NSString *)cid bottomLeftPointLat:(double)bottomLeftPointLat bottomLeftPointLon:(double)bottomLeftPointLon topRightPointLat:(double)topRightPointLat topRightPointLon:(double)topRightPointLon offset:(double)offset animationType:(NSInteger)animationType animationDuration:(double)animationDuration {
  auto cpos = [_mapView.mapWindow.map cameraPosition];
  YMKPoint *sw = [YMKPoint pointWithLatitude:bottomLeftPointLat longitude:bottomLeftPointLon];
  YMKPoint *ne = [YMKPoint pointWithLatitude:topRightPointLat longitude:topRightPointLon];
  YMKBoundingBox *box = [YMKBoundingBox boundingBoxWithSouthWest:sw northEast:ne];
  YMKCameraPosition *pos = [_mapView.mapWindow.map cameraPositionWithGeometry:[YMKGeometry geometryWithBoundingBox:box]];
  if (offset != 0) {
    pos = [YMKCameraPosition cameraPositionWithTarget:pos.target zoom:pos.zoom - offset azimuth:pos.azimuth tilt:pos.tilt];
  }
  auto anType = animationType == 1 ? YMKAnimationTypeSmooth : YMKAnimationTypeLinear;
  YMKAnimation *anim = [YMKAnimation animationWithType:anType duration:animationDuration];
  
  [_mapView.mapWindow.map moveWithCameraPosition:pos animation:anim cameraCallback:^(BOOL completed) {
    auto em = [self eventEmitter];
    
    YamapViewEventEmitter::OnCommandSetBoundsReceived value;
    value.cid = std::string([cid UTF8String]);
    value.completed = completed;
    em.onCommandSetBoundsReceived(value);
  }];
}

- (void)commandSetZoom:(NSString *)cid zoom:(double)zoom offset:(double)offset animationType:(NSInteger)animationType animationDuration:(double)animationDuration {
  auto cpos = [_mapView.mapWindow.map cameraPosition];
  YMKCameraPosition *pos = [YMKCameraPosition cameraPositionWithTarget:cpos.target zoom:zoom - offset azimuth:cpos.azimuth tilt:cpos.tilt];
  auto anType = animationType == 1 ? YMKAnimationTypeSmooth : YMKAnimationTypeLinear;
  YMKAnimation *anim = [YMKAnimation animationWithType:anType duration:animationDuration];
  
  [_mapView.mapWindow.map moveWithCameraPosition:pos animation:anim cameraCallback:^(BOOL completed) {
    auto em = [self eventEmitter];
    
    YamapViewEventEmitter::OnCommandSetZoomReceived value;
    value.cid = std::string([cid UTF8String]);
    value.completed = completed;
    em.onCommandSetZoomReceived(value);
  }];
}

- (void)commandGetCameraPosition:(NSString *)cid {
  auto em = [self eventEmitter];
  auto pos = [_mapView.mapWindow.map cameraPosition];
  
  YamapViewEventEmitter::OnCommandGetCameraPositionReceived value;
  value.cid = std::string([cid UTF8String]);
  value.point.lat = pos.target.latitude;
  value.point.lon = pos.target.longitude;
  value.zoom = pos.zoom;
  value.azimuth = pos.azimuth;
  value.tilt = pos.tilt;
  em.onCommandGetCameraPositionReceived(value);
}

- (void)commandGetVisibleRegion:(NSString *)cid {
  auto em = [self eventEmitter];
  auto cpos = [_mapView.mapWindow.map visibleRegion];
  
  YamapViewEventEmitter::OnCommandGetVisibleRegionReceived value;
  value.cid = std::string([cid UTF8String]);
  value.bottomLeft.lat = cpos.bottomLeft.latitude;
  value.bottomLeft.lon = cpos.bottomLeft.longitude;
  value.bottomRight.lat = cpos.bottomRight.latitude;
  value.bottomRight.lon = cpos.bottomRight.longitude;
  value.topLeft.lat = cpos.topLeft.latitude;
  value.topLeft.lon = cpos.topLeft.longitude;
  value.topRight.lat = cpos.topRight.latitude;
  value.topRight.lon = cpos.topRight.longitude;
  em.onCommandGetVisibleRegionReceived(value);
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
