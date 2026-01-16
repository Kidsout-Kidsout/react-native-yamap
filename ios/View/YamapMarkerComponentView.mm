#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "YamapMarkerView.h"
#import "../Converter/RCTConvert+Yamap.mm"
#import <react/renderer/core/EventEmitter.h>

@interface YamapMarkerComponentView : RCTViewComponentView
@end

@implementation YamapMarkerComponentView {
  YamapMarkerView *_markerView;
}

+ (NSString *)componentViewName
{
  return @"YamapMarker";
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    _markerView = [YamapMarkerView new];
    _markerView.backgroundColor = [UIColor clearColor];
    self.contentView = _markerView;
  }
  return self;
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
  [super updateEventEmitter:eventEmitter];
  __weak auto emitter = _eventEmitter;
  __weak YamapMarkerView *weakMarker = _markerView;
  weakMarker.onPress = ^(NSDictionary *event) {
    if (emitter) {
      emitter->dispatchEvent("onPress");
    }
  };
}

- (void)handleCommand:(const NSString *)commandName args:(const NSArray *)args
{
  if (_markerView == nil) {
    return;
  }

  if ([commandName isEqualToString:@"animatedMoveTo"]) {
    if (args.count >= 2) {
      YMKPoint *point = [RCTConvert YMKPoint:args[0]];
      float duration = [args[1] floatValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_markerView animatedMoveTo:point withDuration:duration];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"animatedRotateTo"]) {
    if (args.count >= 2) {
      float angle = [args[0] floatValue];
      float duration = [args[1] floatValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_markerView animatedRotateTo:angle withDuration:duration];
      });
    }
    return;
  }
}

@end
#endif
