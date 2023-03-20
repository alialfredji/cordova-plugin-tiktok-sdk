# TikTok SDK Integration Documentation

## Table of Contents

1. [Installation](#installation)
2. [Usage](#usage)
   - [Login](#login)
   - [Check if TikTok App is Installed](#check-if-tiktok-app-is-installed)
3. [Handling Errors](#handling-errors)

## Installation

**Important:** Ensure that you have your `clientId` from your [TikTok Dev Console](https://developers.tiktok.com/apps/).

To install the Cordova plugin for TikTok SDK, execute the following command:

```bash
cordova plugin add cordova-plugin-tiktok-sdk --variable TIKTOK_CLIENT_ID="xxxx"
```

## Usage

### Login

**Note:** The TikTok app must be installed for login to work, otherwise an error will be triggered.

To login using TikTok SDK, use the following code snippet:

```javascript
cordova.plugins.TikTokSDKPlugin.login(
    "user.info.basic",
    function (result) {
        // "result" is an object containing "code" and "permissions"
        console.log("TikTok SDK login successful: ", result);
        alert("Login success: " + JSON.stringify(result));
    },
    function (error) {
        // "error" is an object containing "errorCode" and "errorMsg"
        console.error("Error during TikTok SDK login: ", error);
        alert("Login error: " + JSON.stringify(error));
    }
);
```

### Check if TikTok App is Installed

To check if the TikTok app is installed on the user's device, use the following code snippet:

```javascript
cordova.plugins.TikTokSDKPlugin.isAppInstalled(
    function (result) {
        if (result === 1) {
            console.log('TikTok app is installed');
        }
    },
    function () {}
);
```

## Handling Errors

Refer to the TikTok documentation for handling errors on [iOS](https://developers.tiktok.com/doc/getting-started-ios-handling-errors/) and [Android](https://developers.tiktok.com/doc/getting-started-android-handling-errors/).

The table below lists the possible `errorCode` and `errorMsg` values:

| `errorCode` | `errorMsg`               |
|-------------|--------------------------|
| -1          | unknown error            |
| -2          | user cancelled           |
| -3          | send failed              |
| -4          | auth denied             |
| -5          | unsupported              |
| -1000       | TikTok app not installed |
| any other code | something went wrong     |

In case of an error, ensure to handle it appropriately within your application. For example:

```javascript
cordova.plugins.TikTokSDKPlugin.login(
    "user.info.basic",
    function (result) {
        // Handle successful login
    },
    function (error) {
        switch (error.errorCode) {
            case -1:
                // Handle unknown error
                break;
            case -2:
                // Handle user cancelled
                break;
            case -3:
                // Handle send failed
                break;
            case -4:
                // Handle auth denied
                break;
            case -5:
                // Handle unsupported
                break;
            case -1000:
                // Handle TikTok app not installed
                break;
            default:
                // Handle other errors
                break;
        }
    }
);
```

Ensure to follow the best practices while handling errors to provide a seamless user experience.