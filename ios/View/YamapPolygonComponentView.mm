#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "YamapPolygonView.h"
#import <react/renderer/core/EventEmitter.h>

@interface YamapPolygonComponentView : RCTViewComponentView
@end

@implementation YamapPolygonComponentView {
  YamapPolygonView *_polygonView;
}

+ (NSString *)componentViewName
{
  return @"YamapPolygon";
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    _polygonView = [YamapPolygonView new];
    _polygonView.backgroundColor = [UIColor clearColor];
    self.contentView = _polygonView;
  }
  return self;
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
  [super updateEventEmitter:eventEmitter];
  __weak auto emitter = _eventEmitter;
  __weak YamapPolygonView *weakPolygon = _polygonView;
  weakPolygon.onPress = ^(NSDictionary *event) {
    if (emitter) {
      emitter->dispatchEvent("onPress");
    }
  };
}

@end
#endif
