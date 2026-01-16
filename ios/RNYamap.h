#import <RNYamapSpec/RNYamapSpec.h>
#import "YamapView.h"

@interface yamap : NSObject <RCTBridgeModule>

@property YamapView *map;

- (void)initWithKey:(NSString*)apiKey;

@end
