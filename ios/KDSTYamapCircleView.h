#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import <YandexMapsMobile/YMKMapObjectCollection.h>

NS_ASSUME_NONNULL_BEGIN

@interface KDSTYamapCircleView : RCTViewComponentView
-(void) setCollection:(YMKMapObjectCollection *)collection;
-(void) unmount;
@end

NS_ASSUME_NONNULL_END
