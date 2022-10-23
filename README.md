# cordova-plugin-pangleads

## Installation

```sh
cordova plugin add cordova-plugin-pangleads
```

```sh
https://github.com/4quarenta/cordova-plugin-pangleads
```

## Pangle SDK version
I will try to keep the latest version, current version is 'com.pangle.global:ads-sdk:4.6.0.4'

## Usage

### 1. Create Ad Unit ID for your banner and interstitial.

Go to the [Pangle](https://www.pangleglobal.com/) and add your app (if you haven't done so already), once your app is added to your Pangle account, create a new ad unit for it.

### 2. Init SDK

```javascript

function success(result){
    console.log(result);
},
  
function error(result){
    console.log(result);
};
  
cordova.plugins.pangleads.initialize('YOU_APP_ID', success, error);
```
Now the plugin is ready to show ads

### 3. App Open Ads

```javascript
cordova.plugins.pangleads.ShowAppOpen("SLOT_ID");
```

OR 

```javascript

function success(result){
    console.log(result);
},
  
function error(result){
    console.log(result);
};
  
cordova.plugins.pangleads.ShowAppOpen("SLOT_ID",3000,(s)=>console.log(s),(e)=>console.log(e));
```

Note that 3000 is the timeout recommended by pangle, but with the plugin you are free to change it. Running the first way, 3000 is set by default.

### 2. Interstitial Ads

