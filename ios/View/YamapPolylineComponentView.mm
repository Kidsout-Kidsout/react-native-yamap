#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "YamapPolylineView.h"
#import <react/renderer/core/EventEmitter.h>

@interface YamapPolylineComponentView : RCTViewComponentView
@end

@implementation YamapPolylineComponentView {
  YamapPolylineView *_polylineView;
}

+ (NSString *)componentViewName
{
  return @"YamapPolyline";
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    _polylineView = [YamapPolylineView new];
    _polylineView.backgroundColor = [UIColor clearColor];
    self.contentView = _polylineView;
  }
  return self;
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
  [super updateEventEmitter:eventEmitter];
  __weak auto emitter = _eventEmitter;
  __weak YamapPolylineView *weakPolyline = _polylineView;
  weakPolyline.onPress = ^(NSDictionary *event) {
    if (emitter) {
      emitter->dispatchEvent("onPress");
    }
  };
}

@end
#endif
