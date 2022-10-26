# cordova-plugin-pangleads

If you are earning more than USD$200 monthly from using this plugin, please consider [`buy me a coffee`](https://www.buymeacoffee.com/4quarenta) or [`paypal`](https://www.paypal.com/donate/?business=P2K2AXEMGJHDE&no_recurring=0&currency_code=BRL)

## Installation

```sh
cordova plugin add cordova-plugin-pangleads
```

```sh
cordova plugin add https://github.com/4quarenta/cordova-plugin-pangleads.git
```

`IMPORTANT!` there is currently a bug that I couldn't think of a way to solve (import the icons in a way that uses the default package), this happens because for the `app open` it is necessary to import the icon. If you have a solution for this let me know! 

To avoid it, right after adding the plugin:

<blockquote>
in platforms\android\app\src\main\java\cordova\plugin\pangleads\pangleads\pangleads.java
</blockquote>

On line 33, replace

```java
import com.example.app.R;
```
for your package

```java
import com.yourpackage.app.R;
```

You can also do this substitution above inside

<blockquote>
 \src-cordova\plugins\cordova.plugin.pangleads\src\android\pangleads.java
</blockquote>

but this way you should run `cordova prepare` after.

done, just be happy! :)

## Uninstallation
```sh
cordova plugin rm cordova.plugin.pangleads
```


## Pangle SDK version
I will try to keep the latest version, current version is 'com.pangle.global:ads-sdk:4.6.0.4'

## Usage

### 1. Create Ad Unit ID for your banner, app open and interstitial.

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
cordova.plugins.pangleads.showAppOpen("SLOT_ID");
```

OR 

```javascript

function success(result){
    console.log(result);
},
  
function error(result){
    console.log(result);
};
  
cordova.plugins.pangleads.showAppOpen("SLOT_ID", 3000, success, error);
```

Note that 3000 is the timeout recommended by pangle, but with the plugin you are free to change it. Running the first way, 3000 is set by default.

### 4. Interstitial Ads
```javascript

function success(result){
    console.log(result);
},
  
function error(result){
    console.log(result);
};

cordova.plugins.pangleads.showInterstitial("SLOT_ID", success, error);

```

### 5. Banner Ads

#### 5.1 to show

```javascript

function success(result){
    console.log(result);
},
  
function error(result){
    console.log(result);
};

cordova.plugins.pangleads.showBanner("SLOT_ID", success, error);

```

#### 5.1 to destroy

```javascript

function success(result){
    console.log(result);
},
  
function error(result){
    console.log(result);
};

cordova.plugins.pangleads.destroyBanner("SLOT_ID", success, error);

```

Note that to hide the banner it will be necessary to destroy it, to show it again it will be necessary to recreate it. soon i will add a function to just hide it

