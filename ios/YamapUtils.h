#import <UIKit/UIKit.h>
#import <React-graphics/react/renderer/graphics/Color.h>

@interface YamapUtils: NSObject
+(NSString *)getKind:(NSNumber *)value;
+(UIColor *)uiColorFromColor:(facebook::react::SharedColor)color;
@end

@interface YamapMarkerUserData: NSObject
@property (atomic, strong) NSString *id;
@end
