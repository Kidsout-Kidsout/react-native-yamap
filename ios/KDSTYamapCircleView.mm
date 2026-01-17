#import "KDSTYamapCircleView.h"

#import <react/renderer/components/RNYamapSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNYamapSpec/EventEmitters.h>
#import <react/renderer/components/RNYamapSpec/Props.h>
#import <react/renderer/components/RNYamapSpec/RCTComponentViewHelpers.h>

#import <MapKit/MapKit.h>
#import <YandexMapsMobile/YMKMap.h>
#import <YandexMapsMobile/YMKCircle.h>
#import <YandexMapsMobile/YMKRequestPoint.h>
#import <YandexMapsMobile/YMKMapObjectTapListener.h>
#import <YandexMapsMobile/YMKMapObjectCollection.h>

#import <YamapUtils.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

using namespace facebook::react;

@interface KDSTYamapCircleView () <RCTYamapCircleViewViewProtocol, YMKMapObjectTapListener>
@property (atomic, strong) YMKMapObjectCollection *col;
@property (atomic, strong) YMKCircleMapObject *obj;
@property (atomic) const YamapCircleViewProps *rprops;
@end

@implementation KDSTYamapCircleView {}

- (void)setCollection:(YMKMapObjectCollection *)collection{
  self->_col = collection;
  dispatch_async(dispatch_get_main_queue(), ^{
    self->_obj = [collection addCircleWithCircle:[YMKCircle circleWithCenter:[YMKPoint pointWithLatitude:0 longitude:0] radius:0]];
    [self->_obj addTapListenerWithTapListener:self];
    [self updateObject];
  });
}

- (void)updateObject {
  if (_rprops == NULL) return;
  if (_obj == NULL) return;
  
  auto obj = _obj;
  auto p = self->_rprops;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    [obj setGeometry: [YMKCircle circleWithCenter:[YMKPoint pointWithLatitude:p->center.lat longitude:p->center.lon] radius:p->radius]];
    [obj setFillColor:[YamapUtils uiColorFromColor:p->fillColor]];
    [obj setStrokeColor:[YamapUtils uiColorFromColor:p->strokeColor]];
    [obj setStrokeWidth:p->strokeWidth];
    [obj setZIndex:p->lIndex];
  });
}

- (void)unmount {
  [self->_col removeWithMapObject:_obj];
  _obj = nil;
  _col = nil;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &n = std::static_pointer_cast<YamapCircleViewProps const>(props).get();
  _rprops = n;
  
  [self updateObject];
  [super updateProps:props oldProps:oldProps];
}

- (BOOL)onMapObjectTapWithMapObject:(YMKMapObject *)mapObject point:(YMKPoint *)point {
  auto emitter = [self eventEmitter];
  emitter.onPress({});
  
  return true;
}

// Event emitter convenience method
- (const YamapCircleViewEventEmitter &)eventEmitter
{
  return static_cast<const YamapCircleViewEventEmitter &>(*_eventEmitter);
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<YamapCircleViewComponentDescriptor>();
}

@end
