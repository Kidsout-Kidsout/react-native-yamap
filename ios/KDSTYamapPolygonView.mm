#import "KDSTYamapPolygonView.h"

#import <react/renderer/components/RNYamapSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNYamapSpec/EventEmitters.h>
#import <react/renderer/components/RNYamapSpec/Props.h>
#import <react/renderer/components/RNYamapSpec/RCTComponentViewHelpers.h>

#import <MapKit/MapKit.h>
#import <YandexMapsMobile/YMKMap.h>
#import <YandexMapsMobile/YMKPolygon.h>
#import <YandexMapsMobile/YMKRequestPoint.h>
#import <YandexMapsMobile/YMKMapObjectTapListener.h>
#import <YandexMapsMobile/YMKMapObjectCollection.h>
#import <YandexMapsMobile/YMKGeometry.h>

#import <YamapUtils.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

using namespace facebook::react;

@interface KDSTYamapPolygonView () <RCTYamapPolygonViewViewProtocol, YMKMapObjectTapListener>
@property (atomic, strong) YMKMapObjectCollection *col;
@property (atomic, strong) YMKPolygonMapObject *obj;
@property (atomic) const YamapPolygonViewProps *rprops;
@end

@implementation KDSTYamapPolygonView {}

- (void)setCollection:(YMKMapObjectCollection *)collection{
  self->_col = collection;
  dispatch_async(dispatch_get_main_queue(), ^{
    auto polygon = [YMKPolygon polygonWithOuterRing:[YMKLinearRing linearRingWithPoints:[NSArray array]] innerRings:[NSArray array]];
    self->_obj = [collection
                  addPolygonWithPolygon:polygon];
    [self->_obj addTapListenerWithTapListener:self];
    [self updateObject];
  });
}

- (void)dealloc {
  if (_obj != NULL) {
    [self->_obj removeTapListenerWithTapListener:self];
  }
}

- (void)updateObject {
  if (_rprops == NULL) return;
  if (_obj == NULL) return;
  
  auto obj = _obj;
  auto p = self->_rprops;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    auto points = [NSMutableArray arrayWithCapacity:p->points.size()];
    for (int i = 0; i < p->points.size(); i++) {
      auto po = p->points[i];
      [points addObject:[YMKPoint pointWithLatitude:po.lat longitude:po.lon]];
    }
    auto ring = [YMKLinearRing linearRingWithPoints:points];
    auto innerRings = [NSMutableArray arrayWithCapacity:p->innerRings.size()];
    for (int i = 0; i < p->innerRings.size(); i++) {
      auto r = p->innerRings[i];
      auto points = [NSMutableArray arrayWithCapacity:r.size()];
      for (int j = 0; j < r.size(); j++) {
        auto po = r[i];
        [points addObject:[YMKPoint pointWithLatitude:po.lat longitude:po.lon]];
      }
      auto ir = [YMKLinearRing linearRingWithPoints:points];
      [innerRings addObject:ir];
    }
    
    [obj setGeometry: [YMKPolygon polygonWithOuterRing:ring innerRings:innerRings]];
    [obj setFillColor:[YamapUtils uiColorFromColor:p->styling.fillColor]];
    [obj setStrokeColor:[YamapUtils uiColorFromColor:p->styling.strokeColor]];
    [obj setStrokeWidth:p->styling.strokeWidth];
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
  const auto &n = std::static_pointer_cast<YamapPolygonViewProps const>(props).get();
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
- (const YamapPolygonViewEventEmitter &)eventEmitter
{
  return static_cast<const YamapPolygonViewEventEmitter &>(*_eventEmitter);
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<YamapPolygonViewComponentDescriptor>();
}

@end
