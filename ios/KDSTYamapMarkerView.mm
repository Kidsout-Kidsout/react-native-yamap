#import "KDSTYamapMarkerView.h"

#import <react/renderer/components/RNYamapSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNYamapSpec/EventEmitters.h>
#import <react/renderer/components/RNYamapSpec/Props.h>
#import <react/renderer/components/RNYamapSpec/RCTComponentViewHelpers.h>

#import <MapKit/MapKit.h>
#import <YandexMapsMobile/YMKMap.h>
#import <YandexMapsMobile/YMKPlacemark.h>
#import <YandexMapsMobile/YMKRequestPoint.h>
#import <YandexMapsMobile/YMKMapObjectTapListener.h>
#import <YandexMapsMobile/YMKMapObjectCollection.h>
#import <YandexMapsMobile/YMKBaseMapObjectCollection.h>
#import <YandexMapsMobile/YMKClusterizedPlacemarkCollection.h>
#import <YandexMapsMobile/YMKTextStyle.h>

#import <YamapUtils.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

using namespace facebook::react;

@interface KDSTYamapMarkerView () <RCTYamapMarkerViewViewProtocol, YMKMapObjectTapListener>
@property (atomic, strong) YMKBaseMapObjectCollection *col;
@property (atomic, strong) YMKPlacemarkMapObject *obj;
@property (atomic) const YamapMarkerViewProps *rprops;
@property (atomic, strong) UIView *inner;
@end

@implementation KDSTYamapMarkerView {}

- (void)setCollection:(YMKBaseMapObjectCollection *)collection{
  self->_col = collection;
  dispatch_async(dispatch_get_main_queue(), ^{
    if ([collection isKindOfClass:[YMKMapObjectCollection class]]) {
      auto col = (YMKMapObjectCollection *)collection;
      self->_obj = [col addPlacemark];
    }
    if ([collection isKindOfClass:[YMKClusterizedPlacemarkCollection class]]) {
      auto col = (YMKClusterizedPlacemarkCollection *)collection;
      self->_obj = [col addPlacemark];
    }
    [self->_obj addTapListenerWithTapListener:self];
    [self updateObject];
    [self updateChild];
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
    [obj setGeometry:[YMKPoint pointWithLatitude:p->center.lat longitude:p->center.lon]];
    auto style = [YMKTextStyle textStyleWithSize:p->styling.fontSize color:[YamapUtils uiColorFromColor:p->styling.fontColor] outlineWidth:0 outlineColor:UIColor.clearColor placement:YMKTextStylePlacementCenter offset:0 offsetFromIcon:false textOptional:false];
    [obj setTextWithText:[NSString stringWithUTF8String:p->text.c_str()] style:style];
    [obj setZIndex:p->lIndex];
    auto data = [[YamapMarkerUserData alloc] init];
    data.id = [NSString stringWithUTF8String:p->id.c_str()];
    [obj setUserData:(id)data];
  });
}

- (void)updateChild {
  if (_obj == NULL) return;
  
  auto obj = self->_obj;
  auto inner = self->_inner;
  

  dispatch_async(dispatch_get_main_queue(), ^{
    if(inner == NULL) {
      auto view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 10)];
      view.backgroundColor = [UIColor clearColor];
      [view setOpaque:false];
      [obj setViewWithView:[[YRTViewProvider alloc] initWithUIView:view cacheable:true]];
    } else {
      auto provider = [[YRTViewProvider alloc] initWithUIView:inner cacheable:true];
      [obj setViewWithView:provider];
    }
  });
}

- (void)unmount {
  [self->_col removeWithMapObject:_obj];
  _inner = nil;
  _obj = nil;
  _col = nil;
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  if (index > 0) return;
  [childComponentView setOpaque:false];
  _inner = childComponentView;
  [self updateChild];
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  if (index > 0) return;
  _inner = nil;
  [self updateChild];
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &n = std::static_pointer_cast<YamapMarkerViewProps const>(props).get();
  _rprops = n;

  [self updateObject];
  [super updateProps:props oldProps:oldProps];
}

- (BOOL)onMapObjectTapWithMapObject:(YMKMapObject *)mapObject point:(YMKPoint *)point {
  auto emitter = [self eventEmitter];
  YamapMarkerViewEventEmitter::OnPress value;
  YamapMarkerUserData *data = (YamapMarkerUserData *)self->_obj.userData;
  value.id = std::string([data.id UTF8String]);
  emitter.onPress(value);

  return true;
}

// Event emitter convenience method
- (const YamapMarkerViewEventEmitter &)eventEmitter
{
  return static_cast<const YamapMarkerViewEventEmitter &>(*_eventEmitter);
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<YamapMarkerViewComponentDescriptor>();
}

@end
