var exec = require('cordova/exec');

var TikTokSDKPlugin = {
    login: function (scope, success, error) {
        exec(success, error, 'TikTokSDKPlugin', 'login', [scope]);
    },
    isAppInstalled: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TikTokSDKPlugin", "isAppInstalled", []);
    },
};

module.exports = TikTokSDKPlugin;