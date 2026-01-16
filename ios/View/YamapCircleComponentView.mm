#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "YamapCircleView.h"
#import <react/renderer/core/EventEmitter.h>

@interface YamapCircleComponentView : RCTViewComponentView
@end

@implementation YamapCircleComponentView {
  YamapCircleView *_circleView;
}

+ (NSString *)componentViewName
{
  return @"YamapCircle";
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    _circleView = [YamapCircleView new];
    _circleView.backgroundColor = [UIColor clearColor];
    self.contentView = _circleView;
  }
  return self;
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
  [super updateEventEmitter:eventEmitter];
  __weak auto emitter = _eventEmitter;
  __weak YamapCircleView *weakCircle = _circleView;
  weakCircle.onPress = ^(NSDictionary *event) {
    if (emitter) {
      emitter->dispatchEvent("onPress");
    }
  };
}

@end
#endif
