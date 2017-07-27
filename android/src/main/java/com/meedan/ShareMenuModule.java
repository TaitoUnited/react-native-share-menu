package com.meedan;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import com.meedan.ShareMenuPackage;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.content.ClipData;

import android.database.Cursor;
import android.provider.OpenableColumns;

public class ShareMenuModule extends ReactContextBaseJavaModule {

  private ReactContext context;

  public ShareMenuModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.context = reactContext;
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
    ClipData clipData = intent.getClipData();
    if (clipData == null || clipData.getItemCount() == 0) {
      successCallback.invoke("No clip data found", null);
      return;
    }
    Uri inputUri = clipData.getItemAt(0).getUri();
    String mimeType = clipData.getDescription().getMimeType(0);
    String filename = this.getFileName(inputUri);

    WritableMap map = Arguments.createMap();
    map.putString("uri", inputUri.toString());
    map.putString("type", mimeType);
    map.putString("filename", filename);

    successCallback.invoke(null, map);
  }

  @ReactMethod
  public void clearSharedText() {
    Activity mActivity = getCurrentActivity();
    Intent intent = mActivity.getIntent();
    intent.removeExtra(Intent.EXTRA_TEXT);
  }

  private String getFileName(Uri uri) {
    String result = null;
    if (uri.getScheme().equals("content")) {
      Cursor cursor = this.context.getContentResolver().query(uri, null, null, null, null);
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        cursor.close();
      }
    }
    if (result == null) {
      result = uri.getPath();
      int cut = result.lastIndexOf('/');
      if (cut != -1) {
        result = result.substring(cut + 1);
      }
    }
    return result;
  }
}
