#import <Cordova/CDV.h>

@interface TikTokSDKPlugin : CDVPlugin

- (void)login:(CDVInvokedUrlCommand*)command;
- (void)isAppInstalled:(CDVInvokedUrlCommand*)command;

@end