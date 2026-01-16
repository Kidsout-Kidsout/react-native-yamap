#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "RNYMView.h"
#import "../Converter/RCTConvert+Yamap.mm"

#import <react/renderer/core/EventEmitter.h>
#import <folly/dynamic.h>
#import <react/renderer/components/RNYamapSpec/ComponentDescriptors.h>

static folly::dynamic RCTConvertIdToFollyDynamic(id obj) {
  if (obj == nil) {
    return folly::dynamic(nullptr);
  }
  if ([obj isKindOfClass:[NSDictionary class]]) {
    folly::dynamic d = folly::dynamic::object();
    NSDictionary *dict = (NSDictionary *)obj;
    for (id key in dict) {
      id value = dict[key];
      std::string k = [(NSString *)key UTF8String];
      d[k] = RCTConvertIdToFollyDynamic(value);
    }
    return d;
  }
  if ([obj isKindOfClass:[NSArray class]]) {
    folly::dynamic arr = folly::dynamic::array();
    for (id v in (NSArray *)obj) {
      arr.push_back(RCTConvertIdToFollyDynamic(v));
    }
    return arr;
  }
  if ([obj isKindOfClass:[NSNumber class]]) {
    NSNumber *num = (NSNumber *)obj;
    const char *ctype = [num objCType];
    if (strcmp(ctype, @encode(BOOL)) == 0) {
      return folly::dynamic([num boolValue]);
    }
    // Treat as double for RN numeric consistency
    return folly::dynamic([num doubleValue]);
  }
  if ([obj isKindOfClass:[NSString class]]) {
    return folly::dynamic(std::string([(NSString *)obj UTF8String]));
  }
  if ([obj isKindOfClass:[NSNull class]]) {
    return folly::dynamic(nullptr);
  }
  // Fallback: description
  return folly::dynamic(std::string([[obj description] UTF8String]));
}

@interface YamapViewComponentView : RCTViewComponentView
@end

@implementation YamapViewComponentView {
  RNYMView *_mapView;
  BOOL _hasSizedUp;
  BOOL _initialApplied;
  NSDictionary *_pendingInitialRegion;
}

+ (NSString *)componentViewName
{
  return @"YamapView";
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    if ([NSThread isMainThread]) {
      _mapView = [RNYMView new];
      _mapView.backgroundColor = [UIColor clearColor];
      _mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
      self.contentView = _mapView;
    } else {
      dispatch_sync(dispatch_get_main_queue(), ^{
        self->_mapView = [RNYMView new];
        self->_mapView.backgroundColor = [UIColor clearColor];
        self->_mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        self.contentView = self->_mapView;
      });
    }
    _hasSizedUp = NO;
    _initialApplied = NO;
    _pendingInitialRegion = nil;
  }
  return self;
}

- (void)updateProps:(const facebook::react::Props::Shared &)props
            oldProps:(const facebook::react::Props::Shared &)oldProps
{
  [super updateProps:props oldProps:oldProps];
  if (_mapView == nil) {
    return;
  }

  using namespace facebook::react;
  const auto &newProps = *std::static_pointer_cast<const YamapViewProps>(props);

  // Apply boolean and numeric props
  if (newProps.nightMode.has_value()) {
    BOOL enabled = newProps.nightMode.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setNightMode:enabled]; });
  }
  if (newProps.withClusters.has_value()) {
    BOOL enabled = newProps.withClusters.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setClusters:enabled]; });
  }
  if (newProps.clusterColor.has_value()) {
    NSInteger color = (NSInteger)newProps.clusterColor.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setClusterColor:[RCTConvert UIColor:@(color)]]; });
  }
  if (newProps.showUserPosition.has_value()) {
    BOOL enabled = newProps.showUserPosition.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setListenUserLocation:enabled]; });
  }
  if (newProps.userLocationIcon.has_value()) {
    NSString *uri = [NSString stringWithUTF8String:newProps.userLocationIcon->c_str()];
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setUserLocationIcon:uri]; });
  }
  if (newProps.userLocationAccuracyFillColor.has_value()) {
    NSInteger color = (NSInteger)newProps.userLocationAccuracyFillColor.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setUserLocationAccuracyFillColor:[RCTConvert UIColor:@(color)]]; });
  }
  if (newProps.userLocationAccuracyStrokeColor.has_value()) {
    NSInteger color = (NSInteger)newProps.userLocationAccuracyStrokeColor.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setUserLocationAccuracyStrokeColor:[RCTConvert UIColor:@(color)]]; });
  }
  if (newProps.userLocationAccuracyStrokeWidth.has_value()) {
    CGFloat width = (CGFloat)newProps.userLocationAccuracyStrokeWidth.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setUserLocationAccuracyStrokeWidth:width]; });
  }
  if (newProps.mapStyle.has_value()) {
    NSString *style = [NSString stringWithUTF8String:newProps.mapStyle->c_str()];
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView.mapWindow.map setMapStyleWithStyle:style]; });
  }
  if (newProps.mapType.has_value()) {
    NSString *type = [NSString stringWithUTF8String:newProps.mapType->c_str()];
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setMapType:type]; });
  }
  if (newProps.zoomGesturesEnabled.has_value()) {
    BOOL v = newProps.zoomGesturesEnabled.value();
    dispatch_async(dispatch_get_main_queue(), ^{ self->_mapView.mapWindow.map.zoomGesturesEnabled = v; });
  }
  if (newProps.scrollGesturesEnabled.has_value()) {
    BOOL v = newProps.scrollGesturesEnabled.value();
    dispatch_async(dispatch_get_main_queue(), ^{ self->_mapView.mapWindow.map.scrollGesturesEnabled = v; });
  }
  if (newProps.tiltGesturesEnabled.has_value()) {
    BOOL v = newProps.tiltGesturesEnabled.value();
    dispatch_async(dispatch_get_main_queue(), ^{ self->_mapView.mapWindow.map.tiltGesturesEnabled = v; });
  }
  if (newProps.rotateGesturesEnabled.has_value()) {
    BOOL v = newProps.rotateGesturesEnabled.value();
    dispatch_async(dispatch_get_main_queue(), ^{ self->_mapView.mapWindow.map.rotateGesturesEnabled = v; });
  }
  if (newProps.fastTapEnabled.has_value()) {
    BOOL v = newProps.fastTapEnabled.value();
    dispatch_async(dispatch_get_main_queue(), ^{ self->_mapView.mapWindow.map.fastTapEnabled = v; });
  }
  if (newProps.maxFps.has_value()) {
    CGFloat fps = (CGFloat)newProps.maxFps.value();
    dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setMaxFps:fps]; });
  }
  if (newProps.initialRegion.has_value()) {
    auto r = newProps.initialRegion.value();
    NSMutableDictionary *initial = [NSMutableDictionary new];
    initial[@"lat"] = @(r.lat);
    initial[@"lon"] = @(r.lon);
    if (r.zoom.has_value()) initial[@"zoom"] = @(r.zoom.value());
    if (r.azimuth.has_value()) initial[@"azimuth"] = @(r.azimuth.value());
    if (r.tilt.has_value()) initial[@"tilt"] = @(r.tilt.value());
    if (!_hasSizedUp) {
      _pendingInitialRegion = initial;
    } else if (!_initialApplied) {
      dispatch_async(dispatch_get_main_queue(), ^{ [self->_mapView setInitialRegion:initial]; });
      _initialApplied = YES;
      _pendingInitialRegion = nil;
    }
  }
}

- (void)updateLayoutMetrics:(const facebook::react::LayoutMetrics &)layoutMetrics
           oldLayoutMetrics:(const facebook::react::LayoutMetrics &)oldLayoutMetrics
{
  [super updateLayoutMetrics:layoutMetrics oldLayoutMetrics:oldLayoutMetrics];
  if (_mapView == nil) {
    return;
  }
  // Map view is the contentView; use local bounds frame
  CGRect frame;
  frame.origin.x = 0;
  frame.origin.y = 0;
  frame.size.width = layoutMetrics.frame.size.width;
  frame.size.height = layoutMetrics.frame.size.height;
  dispatch_async(dispatch_get_main_queue(), ^{
    // Ensure YMKMapView receives proper resize notifications
    [self->_mapView reactSetFrame:frame];
    [self->_mapView setNeedsLayout];
    [self->_mapView layoutIfNeeded];
    if (!self->_hasSizedUp && frame.size.width > 0 && frame.size.height > 0) {
      self->_hasSizedUp = YES;
      [self applyPendingInitialRegionIfReady];
    }
  });
}

- (void)didMoveToWindow
{
  [super didMoveToWindow];
  if (_mapView == nil) {
    return;
  }
  if (self.window) {
    dispatch_async(dispatch_get_main_queue(), ^{
      self->_mapView.frame = self.bounds;
      [self->_mapView setNeedsLayout];
      [self->_mapView layoutIfNeeded];
      if (!self->_hasSizedUp && self.bounds.size.width > 0 && self.bounds.size.height > 0) {
        self->_hasSizedUp = YES;
        [self applyPendingInitialRegionIfReady];
      }
    });
  }
}

- (void)layoutSubviews
{
  [super layoutSubviews];
  if (_mapView) {
    CGRect b = self.bounds;
    dispatch_async(dispatch_get_main_queue(), ^{
      self->_mapView.frame = b;
      if (!self->_hasSizedUp && b.size.width > 0 && b.size.height > 0) {
        self->_hasSizedUp = YES;
        [self applyPendingInitialRegionIfReady];
      }
    });
  }
}

- (void)applyPendingInitialRegionIfReady
{
  if (_mapView == nil) return;
  if (_hasSizedUp && !_initialApplied && _pendingInitialRegion) {
    NSDictionary *initial = _pendingInitialRegion;
    dispatch_async(dispatch_get_main_queue(), ^{
      [self->_mapView setInitialRegion:initial];
    });
    _initialApplied = YES;
    _pendingInitialRegion = nil;
  }
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
  [super updateEventEmitter:eventEmitter];
  // Bridge legacy RNYMView event blocks to Fabric event emitter
  __weak auto emitter = _eventEmitter;
  __weak RNYMView *weakMap = _mapView;

  weakMap.onRouteFound = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onRouteFound", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onCameraPositionReceived = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onCameraPositionReceived", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onVisibleRegionReceived = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onVisibleRegionReceived", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onWorldToScreenPointsReceived = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onWorldToScreenPointsReceived", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onScreenToWorldPointsReceived = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onScreenToWorldPointsReceived", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onCameraPositionChange = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onCameraPositionChange", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onCameraPositionChangeEnd = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onCameraPositionChangeEnd", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onMapPress = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onMapPress", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onMapLongPress = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onMapLongPress", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
  weakMap.onMapLoaded = ^(NSDictionary *event) {
    if (emitter) {
      dispatch_async(dispatch_get_main_queue(), ^{
        emitter->dispatchEvent("onMapLoaded", RCTConvertIdToFollyDynamic(event));
      });
    }
  };
}

- (void)handleCommand:(const NSString *)commandName args:(const NSArray *)args
{
  if (_mapView == nil) {
    return;
  }

  if ([commandName isEqualToString:@"fitAllMarkers"]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      [self->_mapView fitAllMarkers];
    });
    return;
  }

  if ([commandName isEqualToString:@"setTrafficVisible"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSNumber class]]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setTrafficVisible:[(NSNumber *)args[0] boolValue]];
      });
    }
    return;
  }

  // Prop mapping commands
  if ([commandName isEqualToString:@"setNightMode"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setNightMode:enabled];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setWithClusters"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setClusters:enabled];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setClusterColor"]) {
    if (args.count >= 1) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setClusterColor:[RCTConvert UIColor:args[0]]];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setListenUserPosition"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setListenUserLocation:enabled];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setUserLocationIcon"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSString class]]) {
      NSString *uri = (NSString *)args[0];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setUserLocationIcon:uri];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setUserLocationAccuracyFillColor"]) {
    if (args.count >= 1) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setUserLocationAccuracyFillColor:[RCTConvert UIColor:args[0]]];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setUserLocationAccuracyStrokeColor"]) {
    if (args.count >= 1) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setUserLocationAccuracyStrokeColor:[RCTConvert UIColor:args[0]]];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setUserLocationAccuracyStrokeWidth"]) {
    if (args.count >= 1) {
      float width = [args[0] floatValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setUserLocationAccuracyStrokeWidth:width];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setMapStyle"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSString class]]) {
      NSString *style = (NSString *)args[0];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView.mapWindow.map setMapStyleWithStyle:style];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setMapType"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSString class]]) {
      NSString *type = (NSString *)args[0];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setMapType:type];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setZoomGesturesEnabled"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        self->_mapView.mapWindow.map.zoomGesturesEnabled = enabled;
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setScrollGesturesEnabled"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        self->_mapView.mapWindow.map.scrollGesturesEnabled = enabled;
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setTiltGesturesEnabled"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        self->_mapView.mapWindow.map.tiltGesturesEnabled = enabled;
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setRotateGesturesEnabled"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        self->_mapView.mapWindow.map.rotateGesturesEnabled = enabled;
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setFastTapEnabled"]) {
    if (args.count >= 1) {
      BOOL enabled = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        self->_mapView.mapWindow.map.fastTapEnabled = enabled;
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setInitialRegion"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSDictionary class]]) {
      NSDictionary *initial = (NSDictionary *)args[0];
      // Defer if not sized yet
      if (!_hasSizedUp) {
        _pendingInitialRegion = initial;
      } else {
        dispatch_async(dispatch_get_main_queue(), ^{
          [self->_mapView setInitialRegion:initial];
        });
      }
    }
    return;
  }

  if ([commandName isEqualToString:@"setMaxFps"]) {
    if (args.count >= 1) {
      float fps = [args[0] floatValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setMaxFps:fps];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setInteractive"]) {
    if (args.count >= 1) {
      BOOL interactive = [args[0] boolValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setInteractive:interactive];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"fitMarkers"]) {
    if (args.count >= 1) {
      NSArray<YMKPoint *> *points = [RCTConvert Points:args[0]];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView fitMarkers:points];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setCenter"]) {
    if (args.count >= 6) {
      NSDictionary *center = (NSDictionary *)args[0];
      YMKPoint *target = [RCTConvert YMKPoint:center];
      float zoom = [args[1] floatValue];
      float azimuth = [args[2] floatValue];
      float tilt = [args[3] floatValue];
      float duration = [args[4] floatValue];
      int animation = [args[5] intValue];
      YMKCameraPosition *position = [YMKCameraPosition cameraPositionWithTarget:target zoom:zoom azimuth:azimuth tilt:tilt];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setCenter:position withDuration:duration withAnimation:animation];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setBounds"]) {
    if (args.count >= 5) {
      NSDictionary *southWest = (NSDictionary *)args[0];
      NSDictionary *northEast = (NSDictionary *)args[1];
      float offset = [args[2] floatValue];
      float duration = [args[3] floatValue];
      int animation = [args[4] intValue];
      YMKPoint *sw = [RCTConvert YMKPoint:southWest];
      YMKPoint *ne = [RCTConvert YMKPoint:northEast];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setBoundsForSouthWest:sw northEast:ne withOffset:offset withDuration:duration withAnimation:animation];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"setZoom"]) {
    if (args.count >= 3) {
      float zoom = [args[0] floatValue];
      float duration = [args[1] floatValue];
      int animation = [args[2] intValue];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView setZoom:zoom withDuration:duration withAnimation:animation];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"getCameraPosition"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSString class]]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView emitCameraPositionToJS:(NSString *)args[0]];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"getVisibleRegion"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSString class]]) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView emitVisibleRegionToJS:(NSString *)args[0]];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"getScreenPoints"]) {
    if (args.count >= 2) {
      NSArray<YMKPoint *> *points = [RCTConvert Points:args[0]];
      NSString *cbId = (NSString *)args[1];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView emitWorldToScreenPoint:points withId:cbId];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"getWorldPoints"]) {
    if (args.count >= 2) {
      NSArray<YMKScreenPoint *> *points = [RCTConvert ScreenPoints:args[0]];
      NSString *cbId = (NSString *)args[1];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView emitScreenToWorldPoint:points withId:cbId];
      });
    }
    return;
  }

  if ([commandName isEqualToString:@"findRoutes"]) {
    if (args.count >= 1 && [args[0] isKindOfClass:[NSDictionary class]]) {
      NSDictionary *json = (NSDictionary *)args[0];
      NSArray<YMKPoint *> *points = [RCTConvert Points:json[@"points"]];
      NSMutableArray<YMKRequestPoint *> *requestPoints = [[NSMutableArray alloc] init];
      for (int i = 0; i < [points count]; ++i) {
        YMKRequestPoint *requestPoint = [YMKRequestPoint requestPointWithPoint:[points objectAtIndex:i] type:YMKRequestPointTypeWaypoint pointContext:nil drivingArrivalPointId:nil indoorLevelId:nil];
        [requestPoints addObject:requestPoint];
      }
      NSArray<NSString *> *vehicles = [RCTConvert Vehicles:json[@"vehicles"]];
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView findRoutes:requestPoints vehicles:vehicles withId:json[@"id"]];
      });
    }
    return;
  }
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index
{
  [super mountChildComponentView:childComponentView index:index];
  if (_mapView == nil) {
    return;
  }
  if ([childComponentView isKindOfClass:[RCTViewComponentView class]]) {
    RCTViewComponentView *child = (RCTViewComponentView *)childComponentView;
    UIView *childContent = child.contentView;
    if (childContent != nil) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView insertReactSubview:(UIView<RCTComponent> *)childContent atIndex:index];
      });
    }
  }
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index
{
  [super unmountChildComponentView:childComponentView index:index];
  if (_mapView == nil) {
    return;
  }
  if ([childComponentView isKindOfClass:[RCTViewComponentView class]]) {
    RCTViewComponentView *child = (RCTViewComponentView *)childComponentView;
    UIView *childContent = child.contentView;
    if (childContent != nil) {
      dispatch_async(dispatch_get_main_queue(), ^{
        [self->_mapView removeReactSubview:(UIView<RCTComponent> *)childContent];
      });
    }
  }
}

@end
#endif
