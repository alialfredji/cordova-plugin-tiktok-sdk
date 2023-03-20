#import "AppDelegate+TikTokSDKPlugin.h"
#import <TikTokOpenSDK/TikTokOpenSDKApplicationDelegate.h>
#import <objc/runtime.h>

@implementation AppDelegate (TikTokSDKPlugin)

- (BOOL)application:(UIApplication *)application openURL:(nonnull NSURL *)url options:(nonnull NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    if ([[TikTokOpenSDKApplicationDelegate sharedInstance] application:application openURL:url sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey] annotation:options[UIApplicationOpenURLOptionsAnnotationKey]]
        ) {
        return YES;
    }
    return NO;
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    if ([[TikTokOpenSDKApplicationDelegate sharedInstance] application:application openURL:url sourceApplication:sourceApplication annotation:annotation]) {
        return YES;
    }
    return NO;
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    if ([[TikTokOpenSDKApplicationDelegate sharedInstance] application:application openURL:url sourceApplication:nil annotation:nil]) {
        return YES;
    }
    return NO;
}
@end
