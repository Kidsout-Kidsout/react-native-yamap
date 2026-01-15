#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import <YandexMapsMobile/YMKMapObjectCollection.h>
#import <YandexMapsMobile/YMKBaseMapObjectCollection.h>

NS_ASSUME_NONNULL_BEGIN

@interface KDSTYamapMarkerView : RCTViewComponentView
-(void) setCollection:(YMKBaseMapObjectCollection *)collection;
-(void) unmount;
@end

NS_ASSUME_NONNULL_END
