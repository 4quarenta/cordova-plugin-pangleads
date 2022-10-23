package cordova.plugin.pangleads;

import static org.apache.cordova.LOG.d;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.bytedance.sdk.openadsdk.api.PAGConstant;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAd;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerAdLoadListener;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerRequest;
import com.bytedance.sdk.openadsdk.api.banner.PAGBannerSize;
import com.bytedance.sdk.openadsdk.api.init.PAGConfig;
import com.bytedance.sdk.openadsdk.api.init.PAGSdk;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadListener;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest;
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenAd;
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenAdInteractionListener;
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenAdLoadListener;
import com.bytedance.sdk.openadsdk.api.open.PAGAppOpenRequest;

import com.example.app.R; // -> to fix. how to import?

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
@SuppressWarnings("unused")
public class pangleads extends CordovaPlugin {

    private static final String TAG = "PangleSDK";

    // init conf
    private static String APP_ID= "8079096";
    private static String INTERSTITIAL_ID = "980278895";
    private static String BANNER_ID = "980279652";
    private static String APP_OPEN_ID = "890003433";

    private PAGBannerAd bannerAd;
    private ViewGroup pangleParentView;
    private ViewGroup parentView;

    static boolean _isBannerShowing=false;


    private Activity getCurrentActivity() { return cordova.getActivity(); }

    private static PAGConfig buildNewConfig(Context context) {
        return new PAGConfig.Builder()
                .appId(APP_ID)
                .appIcon(R.mipmap.ic_launcher)
                .debugLog(true)
                .supportMultiProcess(false)//If your app is a multi-process app, set this value to true
                .build();
    }

    @Override
    public void initialize(final CordovaInterface cordova, final CordovaWebView webView)
    {
        super.initialize( cordova, webView );
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ( "initializeSDK".equalsIgnoreCase( action ) )
        {
            String pluginVersion = args.getString( 0 );
            String appid = args.getString( 1 );

            Log.i(TAG, "Initializing Pangleads Cordova v" + pluginVersion + "..." );

             // Check if Activity is available
            Activity currentActivity = getCurrentActivity();

            if ( currentActivity == null ) throw new IllegalStateException( "No Activity found" );

            Context context = cordova.getContext();

            if(PAGSdk.isInitSuccess()) callbackContext.success("PangleSDK já está iniciado!");

            PAGConfig pAGInitConfig = buildNewConfig(context);

            PAGConfig.setGDPRConsent(1);
            PAGConfig.setChildDirected(1);
            PAGConfig.setDoNotSell(1);

            PAGSdk.init(context, pAGInitConfig, new PAGSdk.PAGInitCallback() {
                @Override
                public void success() {
                    Log.i(TAG, "PAGInitCallback new api init success!");

                    // send
                    callbackContext.success("PangleSDK init: " + String.valueOf(PAGSdk.isInitSuccess()));

                    return;
                }

                @Override
                public void fail(int code, String msg) {
                    Log.e(TAG, "PAGInitCallback new api init fail: " + code);

                    callbackContext.error("pangle init fail: " + code + " " + msg);
                    return;
                }

            });
            //initializeSDK( pluginVersion, appid, callbackContext );

            return true;

        } else if ( "showAppOpen".equalsIgnoreCase( action ) )
        {
            String slotId = args.getString( 0 );
            Number load_timeout = args.getInt( 1 );

            PAGAppOpenRequest request = new PAGAppOpenRequest();
            request.setTimeout(load_timeout != null ? (int) load_timeout : 3000);

            PAGAppOpenAd.loadAd(slotId, request, new PAGAppOpenAdLoadListener() {
                @Override
                public void onError(int code, String message) {
                    callbackContext.error("ad error: " + code + ", " + message);
                    return;
                }

                @Override
                public void onAdLoaded(PAGAppOpenAd appOpenAd) {
                    if (appOpenAd != null) {
                        appOpenAd.show(getCurrentActivity());

                        /*appOpenAd.setAdInteractionListener(new PAGAppOpenAdInteractionListener(){
                            @Override
                            public void onAdShowed() {
                                fireWindowEvent( "onAppOpenAdShowed", (JSONObject) appOpenAd.getMediaExtraInfo());
                            }
                            @Override
                            public void onAdClicked() {

                            }
                            @Override
                            public void onAdDismissed() {

                            }
                        });*/
                    }
                }
            });

            return true;

        } else if ( "showInterstitial".equalsIgnoreCase( action ) )
        {
            String slotId = args.getString( 0 );

            PAGInterstitialRequest request = new PAGInterstitialRequest();

            PAGInterstitialAd.loadAd(slotId,
                request,
                new PAGInterstitialAdLoadListener() {
                    @Override
                    public void onError(int code, String message) {
                        callbackContext.error("ad error: " + code + ", " + message);
                        return;
                    }

                    @Override
                    public void onAdLoaded(PAGInterstitialAd ads) {
                        if (ads != null) {
                            ads.show(getCurrentActivity());
                        }
                        callbackContext.success("ad loaded");
                        return;
                    }
            });

            return true;
        }
        else if("showBanner".equalsIgnoreCase( action ) ){

            String slotId = args.getString( 0 );

            if(bannerAd != null && _isBannerShowing!=false) {
                callbackContext.error("Banner is showing");
                return false;
            }

           // String w = args.getString( 0 );
           // String h = args.getString( 0 );

            //PAGBannerSize bannerSize = new PAGBannerSize(w!null?w:320,h!null?h:50);
            PAGBannerSize bannerSize = PAGBannerSize.BANNER_W_320_H_50;
            PAGBannerRequest bannerRequest = new PAGBannerRequest(bannerSize);

            PAGBannerAd.loadAd(slotId, bannerRequest, new PAGBannerAdLoadListener() {
            @Override
            public void onError(int code, String message) { 
                    callbackContext.error("ad error: " + code + ", " + message);
                    return;              
            }

            @Override
            public void onAdLoaded(PAGBannerAd bads) {
                bannerAd = bads;
                // -- study this
                try {
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                /*banner align left bug (bug da porra)*/
                                View viewPangle = webView.getView(); // WEBVIEW // DEVICE SIZE
                                ViewGroup panglewvParentView = (ViewGroup) viewPangle.getParent();

                                if (pangleParentView == null) {
                                    pangleParentView = new LinearLayout(webView.getContext());
                                }

                                if (panglewvParentView != null && panglewvParentView != pangleParentView) {
                                    ViewGroup panglerootView = (ViewGroup) (viewPangle.getParent()); // DEVICE VIEW

                                    panglewvParentView.removeView(viewPangle);

                                    pangleParentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,0.0F));
                                    pangleParentView.setBackgroundColor(Color.BLACK);

                                    ((LinearLayout) pangleParentView).setOrientation(LinearLayout.VERTICAL);// THIS REMOVE CENTERED GRAVITY
                                    ((LinearLayout) pangleParentView).setGravity(Gravity.CENTER_HORIZONTAL); // this not working if setOrientation
                                    ((LinearLayout) pangleParentView).setBaselineAligned(true);

                                    viewPangle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F)); // this is webview area

                                    pangleParentView.addView(viewPangle);
                                    panglerootView.addView(pangleParentView); // add banner to screen

                                }

                                // show banner
                                pangleParentView.addView(bannerAd.getBannerView(), 1); //, 0 to top
                                pangleParentView.bringToFront();
                                pangleParentView.requestLayout();
                                pangleParentView.requestFocus();

                                _isBannerShowing = true;

                                PluginResult result = new PluginResult(PluginResult.Status.OK, "ad show"); //Pangle Banner AdLoaded
                                result.setKeepCallback(true);
                                callbackContext.sendPluginResult(result);
                                //*/


                            } catch (IllegalArgumentException err) {
                                callbackContext.error(err.toString());
                            }
                        }
                    });
                }
                catch (RuntimeException e){
                    callbackContext.error("Runtime exception found");
                }
                callbackContext.success("ad loaded");
                return;
            }
        });
            return true;
        } else if("destroyBanner".equalsIgnoreCase( action ) ){
            if(_isBannerShowing != true) {
                callbackContext.error("Banner is not showing");
                return false;
            }
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bannerAd != null) {
                        try {

                            bannerAd.destroy();
                            bannerAd.setVisibility(View.GONE);

                            ((ViewGroup) bannerAd.getParent()).removeView(bannerAd);
                            bannerAd = null;


                            _isBannerShowing = false;

                            PluginResult result = new PluginResult(PluginResult.Status.OK, "AdDistroyed");   //Facebook Banner AdDistroyed
                            result.setKeepCallback(true);
                            callbackContext.sendPluginResult(result);

                        } catch (NullPointerException err) {
                            callbackContext.error(err.toString());
                        }
                    }

                    //callbackContext.success("Facebook banner Ads hide");

                    //PluginResult result = new PluginResult(PluginResult.Status.OK, "");
                    //callbackContext.sendPluginResult(result);
                }
            });
            return true;
        }
        return false;
    }
    // React Native Bridge
    private void fireWindowEvent(final String name, final JSONObject params)
    {
        getCurrentActivity().runOnUiThread( () -> webView.loadUrl( "javascript:cordova.fireWindowEvent('" + name + "', " + params.toString() + ");" ) );
    }
    

}
