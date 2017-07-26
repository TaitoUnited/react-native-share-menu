package com.meedan;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.meedan.ShareMenuPackage;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.content.ClipData;

public class ShareMenuModule extends ReactContextBaseJavaModule {

  public ShareMenuModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ShareMenu";
  }

  @ReactMethod
  public void getSharedText(Callback successCallback) {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();

    // Support for clips
    String inputText = "";
    Uri inputUri = this.uriFromClipData(intent.getClipData());
    if (inputUri != null) {
      inputText = inputUri.toString(); 
    } else {
      inputText = intent.getStringExtra(Intent.EXTRA_TEXT);
    }
    successCallback.invoke(inputText);
  }

  @ReactMethod
  public void clearSharedText() {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();
    intent.removeExtra(Intent.EXTRA_TEXT);
  }

  private Uri uriFromClipData(ClipData clip) {
    if (clip != null && clip.getItemCount() > 0) {
        return clip.getItemAt(0).getUri();
    }
    return null;
  }
}
