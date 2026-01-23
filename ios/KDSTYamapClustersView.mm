#import "KDSTYamapClustersView.h"

#import "KDSTYamapMarkerView.h"

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
#import <YandexMapsMobile/YMKClusterizedPlacemarkCollection.h>
#import <YandexMapsMobile/YMKClusterListener.h>
#import <YandexMapsMobile/YMKClusterTapListener.h>
#import <YandexMapsMobile/YMKCluster.h>
#import <YandexMapsMobile/YMKPlacemark.h>
#import <YandexMapsMobile/YMKCameraPosition.h>

#import <YamapUtils.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

using namespace facebook::react;

@interface KDSTYamapClustersView () <RCTYamapClustersViewViewProtocol, YMKClusterListener, YMKClusterTapListener>
@property (atomic, strong) YMKMap *map;
@property (atomic, strong) YMKMapObjectCollection *col;
@property (atomic, strong) YMKClusterizedPlacemarkCollection *obj;
@property (atomic) const YamapClustersViewProps *rprops;
@end

@implementation KDSTYamapClustersView {
  NSTimer *timer;
  BOOL needsUpdate;
}

- (instancetype)init{
  self->timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(runScheduledUpdate) userInfo:nil repeats:true];
  return [super init];
}

- (void)setCollection:(YMKMapObjectCollection *)collection{
  self->_col = collection;
  dispatch_async(dispatch_get_main_queue(), ^{
    self->_obj = [collection addClusterizedPlacemarkCollectionWithClusterListener:self];
    [self updateObject];
  });
}

- (void)dealloc {
  [self->timer invalidate];
  self->timer = nil;
}

- (void)runScheduledUpdate {
  if (!self->needsUpdate) return;
  [self updateObject];
  self->needsUpdate = false;
}

- (void)updateObject {
  if (_rprops == NULL) return;
  if (_obj == NULL) return;

  auto obj = _obj;
  auto p = self->_rprops;

  dispatch_async(dispatch_get_main_queue(), ^{
    [obj clusterPlacemarksWithClusterRadius:p->radius minZoom:p->minZoom];
  });
}

- (void)unmount {
  [self->_col removeWithMapObject:_obj];
  _obj = nil;
  _col = nil;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &n = std::static_pointer_cast<YamapClustersViewProps const>(props).get();
  _rprops = n;

  [self updateObject];
  [super updateProps:props oldProps:oldProps];
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  if ([childComponentView isKindOfClass:[KDSTYamapMarkerView class]]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      auto *view = (KDSTYamapMarkerView *)childComponentView;
      [view setCollection:self->_obj];
      dispatch_async(dispatch_get_main_queue(), ^{
        self->needsUpdate = true;
      });
    });
    return;
  }
  
  [super mountChildComponentView:childComponentView index:index];
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  if ([childComponentView isKindOfClass:[KDSTYamapMarkerView class]]) {
    dispatch_async(dispatch_get_main_queue(), ^{
      auto *view = (KDSTYamapMarkerView *)childComponentView;
      [view unmount];
      self->needsUpdate = true;
    });
    return;
  }
  
  [super unmountChildComponentView:childComponentView index:index];
}

- (void)onClusterAddedWithCluster:(YMKCluster *)cluster {
  NSNumber *myNum = @([cluster size]);
  [[cluster appearance] setIconWithImage:[self clusterImage:myNum]];
  [cluster addClusterTapListenerWithClusterTapListener:self];
}

- (BOOL)onClusterTapWithCluster:(nonnull YMKCluster*)cluster {
  auto em = [self eventEmitter];
  YamapClustersViewEventEmitter::OnPress value;
  
  auto placemarks = [cluster placemarks];
  NSMutableArray<NSString *> *arr = [NSMutableArray arrayWithCapacity:[cluster size]];
  
  for(int i = 0; i < placemarks.count; i++) {
    auto placemark = placemarks[i];
    YamapMarkerUserData *data = (YamapMarkerUserData *)placemark.userData;
    [arr addObject:data.id];
  }
  
  std::vector<std::string> ids = {};
  for (NSString * str in arr) {
    std::string cppStr = std::string([str UTF8String]);
    ids.push_back(cppStr);
  }
  
  value.ids = ids;
  
  em.onPress(value);
  
  return YES;
}


- (UIImage*)clusterImage:(NSNumber*)clusterSize {
  float FONT_SIZE = self->_rprops->clusterStyle.fontSize;
  float MARGIN_SIZE = self->_rprops->clusterStyle.padding;
  float STROKE_SIZE = self->_rprops->clusterStyle.strokeWidth;
  float scale = UIScreen.mainScreen.scale;
  NSString *text = [clusterSize stringValue];
  UIFont *font = [UIFont systemFontOfSize:FONT_SIZE * scale];
  CGSize size = [text sizeWithFont:font];
  float textRadius = sqrt(size.height * size.height + size.width * size.width) / 2;
  float internalRadius = textRadius + MARGIN_SIZE * scale;
  float externalRadius = internalRadius + STROKE_SIZE * scale;
  
  auto fillColor = [YamapUtils uiColorFromColor:self->_rprops->clusterStyle.fillColor];
  auto strokeColor = [YamapUtils uiColorFromColor:self->_rprops->clusterStyle.strokeColor];
  
  //stroke
  auto fontColor = [YamapUtils uiColorFromColor:self->_rprops->clusterStyle.fontColor];
  
  // This function returns a newImage, based on image, that has been:
  // - scaled to fit in (CGRect) rect
  // - and cropped within a circle of radius: rectWidth/2
  
  //Create the bitmap graphics context
  UIGraphicsBeginImageContextWithOptions(CGSizeMake(externalRadius*2, externalRadius*2), NO, 1.0);
  CGContextRef context = UIGraphicsGetCurrentContext();
  CGContextSetFillColorWithColor(context, [strokeColor CGColor]);
  CGContextFillEllipseInRect(context, CGRectMake(0, 0, externalRadius*2, externalRadius*2));
  CGContextSetFillColorWithColor(context, [fillColor CGColor]);
  CGContextFillEllipseInRect(context, CGRectMake(externalRadius - internalRadius, externalRadius - internalRadius, internalRadius*2, internalRadius*2));
  [text drawInRect:CGRectMake(externalRadius - size.width/2, externalRadius - size.height/2, size.width, size.height) withAttributes:@{NSFontAttributeName: font, NSForegroundColorAttributeName: fontColor }];
  UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
  UIGraphicsEndImageContext();
  
  return newImage;
}

// Event emitter convenience method
- (const YamapClustersViewEventEmitter &)eventEmitter
{
  return static_cast<const YamapClustersViewEventEmitter &>(*_eventEmitter);
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<YamapClustersViewComponentDescriptor>();
}

@end
