
var exec = require('cordova/exec');
var cordova = require('cordova');

const VERSION = '1.0.0';

function isFunction(functionObj) {
    return typeof functionObj === 'function';
}
// call function name
function callNative(name, params = [], successCallback = null, errorCallback = null) {
   cordova.exec(successCallback, errorCallback, 'pangleads', name, params);
}

var PangleSDK = {
    VERSION,
    // NOTE: We have to store states in JS as workaround for callback-based API
    // since Cordova does not allow for synchronous returns
    initialized: false,    
    initialize: function (app_id, successCallback, errorCallback) {
        callNative('initializeSDK', [VERSION, app_id], function (config) {
            PangleSDK.initialized = true;

            if (isFunction(successCallback)) {
                successCallback(config);
            }
        }, errorCallback);
    },

    isInitialized: function () {
        return this.initialized;
    },

    /*---------*/
    /* APP OPEN */
    /*---------*/
    ShowAppOpen: function (slotId, load_timeout, successCallback, errorCallback) {
        callNative('showAppOpen', [slotId, (load_timeout !== undefined ? load_timeout : 3000)], successCallback, errorCallback);
    },
    /*---------*/
    /* Intersticial */
    /*---------*/
    showInterstitial:function (slotId, successCallback, errorCallback) {
        callNative('showInterstitial', [slotId], successCallback, errorCallback);
    },
   
     /*---------*/
    /* BANNER */
    /*---------*/
    showBanner:function (slotId, successCallback, errorCallback) {
        callNative('showBanner', [slotId], successCallback, errorCallback);
    },
    destroyBanner:function (slotId, successCallback, errorCallback) {
        callNative('destroyBanner', [slotId], successCallback, errorCallback);
    }      

};

/* Attach listeners for ad readiness state management
window.addEventListener('onAppOpenAdShowed', function (adInfo) {
    console.log(adInfo);
});*/

if (typeof module !== undefined && module.exports) {
    module.exports = PangleSDK;
}
