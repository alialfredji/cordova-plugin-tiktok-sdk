#import "TikTokSDKPlugin.h"
#import <TikTokOpenSDK/TikTokOpenSDKApplicationDelegate.h>
#import <TikTokOpenSDK/TikTokOpenSDKAuth.h>

@implementation TikTokSDKPlugin

- (void) pluginInitialize {
    [[TikTokOpenSDKApplicationDelegate sharedInstance] application:[UIApplication sharedApplication] didFinishLaunchingWithOptions:nil];
}

- (void)isAppInstalled:(CDVInvokedUrlCommand*)command {
    BOOL isInstalled = [[TikTokOpenSDKApplicationDelegate sharedInstance] isAppInstalled];
    int result = isInstalled ? 1 : 0;
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:result];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)login:(CDVInvokedUrlCommand*)command {
    if (![[TikTokOpenSDKApplicationDelegate sharedInstance] isAppInstalled]) {
        NSDictionary *errorResponse = @{
            @"errorCode": @-1000,
            @"errorMsg": @"tiktok app not installed"
        };
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:errorResponse];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }

    NSString *scope = [command.arguments objectAtIndex:0];
    NSArray *scopes = [scope componentsSeparatedByString:@","];
    NSOrderedSet *scopesSet = [NSOrderedSet orderedSetWithArray:scopes];
    TikTokOpenSDKAuthRequest *request = [[TikTokOpenSDKAuthRequest alloc] init];
    request.permissions = scopesSet;

    UIViewController *rootViewController = [UIApplication sharedApplication].delegate.window.rootViewController;
    __weak __typeof__(self) weakSelf = self;

    [request sendAuthRequestViewController:rootViewController
                                completion:^(TikTokOpenSDKAuthResponse *_Nonnull resp) {
        __strong __typeof__(weakSelf) strongSelf = weakSelf;
        if (resp.errCode == 0) {
            NSDictionary *resultData = @{
                @"code": resp.code,
                @"permissions": [self commaSeparatedStringFromOrderedSet:resp.grantedPermissions]
            };
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:resultData];
            [strongSelf.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        } else {
            NSDictionary *errorResponse = @{
                @"errorCode": @(resp.errCode),
                @"errorMsg": [self errorMessageForErrorCode:resp.errCode]
            };
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:errorResponse];
            [strongSelf.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }
    }];
}

- (NSString *)errorMessageForErrorCode:(NSInteger)errorCode {
    NSDictionary *errorMessages = @{
        @(-1): @"unknown error",
        @(-2): @"user cancelled",
        @(-3): @"send failed",
        @(-4): @"auth denied",
        @(-5): @"unsupported",
    };

    NSString *errorMessage = errorMessages[@(errorCode)];
    if (errorMessage) {
        return errorMessage;
    } else {
        return @"Something went wrong";
    }
}

- (NSString *)commaSeparatedStringFromOrderedSet:(NSOrderedSet *)orderedSet {
    NSArray *array = [orderedSet array];
    return [array componentsJoinedByString:@","];
}

@end