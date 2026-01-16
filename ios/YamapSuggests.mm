#import "YamapSuggests.h"
#import <YandexMapsMobile/YMKSearchManager.h>
#import <YandexMapsMobile/YMKSearchSuggestSession.h>
#import <YandexMapsMobile/YMKGeometry.h>
#import <YandexMapsMobile/YMKSuggestOptions.h>
#import <YandexMapsMobile/YMKPoint.h>
#import <YandexMapsMobile/YMKSearch.h>
#import <YandexMapsMobile/YMKSuggestResponse.h>

@implementation YamapSuggests {
  YMKSearchManager* searchManager;
  YMKSearchSuggestSession* suggestClient;
  YMKBoundingBox* defaultBoundingBox;
  YMKSuggestOptions* suggestOptions;
}

- (id)init {
  self = [super init];
  
  YMKPoint* southWestPoint = [YMKPoint pointWithLatitude:-90.0 longitude:-180.0];
  YMKPoint* northEastPoint = [YMKPoint pointWithLatitude:90.0 longitude:180.0];
  defaultBoundingBox = [YMKBoundingBox boundingBoxWithSouthWest:southWestPoint northEast:northEastPoint];
  suggestOptions = [YMKSuggestOptions suggestOptionsWithSuggestTypes: YMKSuggestTypeGeo userPosition:nil suggestWords:true strictBounds: false];
  searchManager = [[YMKSearchFactory instance] createSearchManagerWithSearchManagerType:YMKSearchManagerTypeOnline];
  
  return self;
}

+ (BOOL)requiresMainQueueSetup {
  return YES;
}

+ (NSString *)moduleName {
  return @"YamapSuggests";
}


// TODO: Этот метод можно вынести в отдельный файл утилей, но пока в этом нет необходимости.
void runOnMainQueueWithoutDeadlocking(void (^block)(void)) {
  if ([NSThread isMainThread]) {
    block();
  } else {
    dispatch_sync(dispatch_get_main_queue(), block);
  }
}

NSString* ERR_NO_REQUEST_ARG = @"YANDEX_SUGGEST_ERR_NO_REQUEST_ARG";
NSString* ERR_SUGGEST_FAILED = @"YANDEX_SUGGEST_ERR_SUGGEST_FAILED";

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeYamapSuggestsSpecJSI>(params);
}

- (YMKSearchSuggestSession*_Nonnull)getSuggestClient {
  if (suggestClient) {
    return suggestClient;
  }
  
  runOnMainQueueWithoutDeadlocking(^{
    self->suggestClient = [self->searchManager createSuggestSession];
  });
  
  return suggestClient;
}

-(void)suggestHandler: (nonnull NSString*) searchQuery options:(YMKSuggestOptions*) options resolver:(RCTPromiseResolveBlock) resolve rejecter:(RCTPromiseRejectBlock) reject {
  @try {
    YMKSearchSuggestSession* session = [self getSuggestClient];
    
    dispatch_async(dispatch_get_main_queue(), ^{
      [session suggestWithText:searchQuery
                        window:self->defaultBoundingBox
                suggestOptions:options
               responseHandler:^(YMKSuggestResponse * _Nullable response, NSError * _Nullable error) {
        if (error) {
          reject(ERR_SUGGEST_FAILED, [NSString stringWithFormat:@"search request: %@", searchQuery], error);
          return;
        }
        
        NSMutableArray *suggestsToPass = [NSMutableArray new];
        
        for (YMKSuggestItem* suggest in response.items) {
          NSMutableDictionary *suggestToPass = [NSMutableDictionary new];
          
          [suggestToPass setValue:[[suggest title] text] forKey:@"title"];
          [suggestToPass setValue:[[suggest subtitle] text] forKey:@"subtitle"];
          [suggestToPass setValue:[suggest uri] forKey:@"uri"];
          [suggestToPass setValue:[suggest searchText] forKey:@"searchText"];
          [suggestToPass setValue:[suggest displayText] forKey:@"displayText"];
          
          [suggestsToPass addObject:suggestToPass];
        }
        
        resolve(suggestsToPass);
      }];
    });
  }
  @catch ( NSException *error ) {
    reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: %@", searchQuery], nil);
  }
}

- (void)suggest:(nonnull NSString *)query resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  [self suggestHandler:query options:self->suggestOptions resolver:resolve rejecter:reject];
}

- (void)suggestWithOptions:(nonnull NSString *)searchQuery options:(id)options resolve:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  NSArray *suggestTypes = options[@"suggestTypes"];
  NSDictionary *userPosition = options[@"userPosition"];
  YMKSuggestType suggestType = YMKSuggestTypeGeo;
  
  YMKSuggestOptions *opt = [[YMKSuggestOptions alloc] init];
  
  if(options[@"suggestWords"] != nil){
    NSNumber *suggestWords = options[@"suggestWords"];
    if(![suggestWords isKindOfClass:[NSNumber class]]){
      reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: suggestWords must be a Boolean"], nil);
      return;
    }
    [opt setSuggestWords:suggestWords.boolValue];
  }
  
  if(suggestTypes != nil){
    if(![suggestTypes isKindOfClass: [NSArray class]]){
      reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: suggestTypes is not an Array"], nil);
      return;
    }
    
    suggestType = YMKSuggestTypeUnspecified;
    
    for(int i = 0; i < [suggestTypes count]; i++){
      NSNumber *value = suggestTypes[i];
      if(![value isKindOfClass: [NSNumber class]]){
        reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: one or more suggestTypes is not a Number"], nil);
        return;
      }
      suggestType = suggestType | [value unsignedLongValue];
    }
  }
  
  [opt setSuggestTypes:suggestType];
  
  if(userPosition != nil) {
    if(![userPosition isKindOfClass: [NSDictionary class]]){
      reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: userPosition is not an Object"], nil);
      return;
    }
    if(userPosition[@"lat"] == nil || userPosition[@"lon"] == nil){
      reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: lon and lat cannot be empty"], nil);
      return;
    }
    
    NSNumber *lat =  userPosition[@"lat"];
    NSNumber *lon =  userPosition[@"lon"];
    
    if(![lat isKindOfClass: [NSNumber class]] || ![lon isKindOfClass:[NSNumber class]]){
      reject(ERR_NO_REQUEST_ARG, [NSString stringWithFormat:@"search request: lat or lon is not a Number"], nil);
      return;
    }
    
    YMKPoint  *userPoint = [YMKPoint pointWithLatitude:[lat doubleValue] longitude:[lon doubleValue]];
    [opt setUserPosition:userPoint];
  }
  
  [self suggestHandler:searchQuery options:opt resolver:resolve rejecter:reject];
}

- (void)reset:(nonnull RCTPromiseResolveBlock)resolve reject:(nonnull RCTPromiseRejectBlock)reject {
  @try {
    if (suggestClient) {
      dispatch_async(dispatch_get_main_queue(),^{
        [self->suggestClient reset];
      });
    }
    
    resolve(@[]);
  }
  @catch(NSException *error) {
    reject(@"ERROR", @"Error during reset suggestions", nil);
  }
}

@end
