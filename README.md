Installation

NOTE! Get your `clientId` from your [TikTok Dev Console](https://developers.tiktok.com/apps/)

```bash

cordova plugin add cordova-plugin-tiktok-sdk --variable TIKTOK_CLIENT_ID="xxxx"

```

# Usage

#### Login

NOTE! Tiktok app is required to be installed for login to work, else an error will be fired

```js

cordova.plugins.TikTokSDKPlugin.login(
	"user.info.basic",
	function (result) {
		// "result" is an object contaning "code" and "permissions"
		console.log("TikTok SDK login successful: ", result);
		alert("Login success: " + JSON.stringify(result));
	},
	function (error) {
		// "result" is an object contaning "errorCode" and "errorMsg"
		console.error("Error during TikTok SDK login: ", error);
		alert("Login error: " + JSON.stringify(error));
	}
);

```

#### Check if tiktok app is installed

```js

cordova.plugins.TikTokSDKPlugin.isAppInstalled(
	function (result) {
		if (result === 1) {
			console.log('app is installed :D')
		}
	},
	function () {}
);

```

# Handling errors

- TikTok docs reference for iOS errors: [Link](https://developers.tiktok.com/doc/getting-started-ios-handling-errors/)
- TikTok docs reference for Android errors: [Link](https://developers.tiktok.com/doc/getting-started-android-handling-errors/)

| errorCode      | errorMsg                 |
| -------------- | ------------------------ |
| -1             | unknown error            |
| -2             | user cancelled           |
| -3             | send failed              |
| -4             | auth denied              |
| -5             | unsupported              |
| -1000          | tiktok app not installed |
| any other code | something went wrong     |