#import <RNYamapSpec/RNYamapSpec.h>
#import "YamapView.h"

@interface Yamap : NSObject <NativeYamapSpec>

@property YamapView *map;

- (void)initWithKey:(NSString*)apiKey;

@end
