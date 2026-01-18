#import "KDSTYamapView.h"

#import "KDSTYamapCircleView.h"
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

using namespace facebook::react;

@interface KDSTYamapView () <RCTYamapViewViewProtocol>
@end

@implementation KDSTYamapView {
  YMKMapView * _mapView;
}

-(instancetype)init
{
  if(self = [super init]) {
    _mapView = [YMKMapView new];
    [self addSubview:_mapView];
  }
  return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &oldViewProps = *std::static_pointer_cast<YamapViewProps const>(_props);
  const auto &newViewProps = *std::static_pointer_cast<YamapViewProps const>(props);
  
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
  if ([childComponentView isKindOfClass:[KDSTYamapPolygonView class]]) {
    auto *view = (KDSTYamapPolygonView *)childComponentView;
    [view unmount];
    return;
  }
  
  [super unmountChildComponentView:childComponentView index:index];
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
