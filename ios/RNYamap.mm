#import "RNYamap.h"
#import <YandexMapsMobile/YMKMapKitFactory.h>
#import <YandexMapsMobile/YRTI18nManager.h>

@implementation Yamap

static NSString * _pinIcon;
static NSString * _arrowIcon;
static NSString * _markerIcon;
static NSString * _selectedMarkerIcon;

@synthesize map;

- (instancetype) init {
    self = [super init];
    if (self) {
        map = [[YamapView alloc] init];
    }

    return self;
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

+ (NSString *)moduleName { 
  return @"Yamap";
}


- (void)initWithKey:(NSString *) apiKey {
    [YMKMapKit setApiKey: apiKey];
}

- (dispatch_queue_t)methodQueue{
    return dispatch_get_main_queue();
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeYamapSpecJSI>(params);
}

- (void)init:(nonnull NSString *)apiKey resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  @try {
    [self initWithKey: apiKey];
    resolve(nil);
  } @catch (NSException *exception) {
    NSError *error = nil;
    if (exception.userInfo.count > 0) {
      error = [NSError errorWithDomain:NSCocoaErrorDomain code:0 userInfo:exception.userInfo];
    }
    reject(exception.name, exception.reason ?: @"Error initiating YMKMapKit", error);
  }
}

- (void)getLocale:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  NSString * locale = [YRTI18nManagerFactory getLocale];
  resolve(@[locale]);
}

- (void)resetLocale:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
  [YRTI18nManagerFactory setLocaleWithLocale:nil];
  resolve(@[]);
}

- (void)setLocale:(nonnull NSString *)locale resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject { 
  [YRTI18nManagerFactory setLocaleWithLocale:locale];
  resolve(@[]);
}

@end
