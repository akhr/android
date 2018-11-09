/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.android.apis.R;

import java.util.Map;

import androidx.annotation.Nullable;

//.import androidx.webkit.WebResourceRequestCompat;


/**
 * Sample creating 1 webviews.
 */
public class WebView1 extends Activity implements View.OnTouchListener, Handler.Callback{

    private static final String TAG = "WebViewC";
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;

    private Handler handler;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.webview_1);
        handler = new Handler();

        final String mimeType = "text/html";
        WebView webView;
        webView = (WebView) findViewById(R.id.wv1);
        webView.setOnTouchListener(this);

        WebViewClient webViewClient = new MyWebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.setVerticalScrollBarEnabled(false);
        WebResourceRequest wr = new WebResourceRequest() {
            @Override
            public Uri getUrl() {
                return null;
            }

            @Override
            public boolean isForMainFrame() {
                return false;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }

            @Override
            public boolean hasGesture() {
                return false;
            }

            @Override
            public String getMethod() {
                return null;
            }

            @Override
            public Map<String, String> getRequestHeaders() {
                return null;
            }
        }
//      webView.loadData("", mimeType, null);
        webView.loadUrl("http://www.ammazon.com");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.wv1 && event.getAction() == MotionEvent.ACTION_DOWN){
            Log.e(TAG, this.getClass().getSimpleName()+" onTouch() - event.getAction() - ACTION_DOWN - "+event.getAction());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(CLICK_ON_WEBVIEW);
                }
            });
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL){
            Log.e(TAG, this.getClass().getSimpleName()+" handleMessage() - msg.what - CLICK_ON_URL - "+msg.what);
            handler.removeMessages(CLICK_ON_WEBVIEW);
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW){
            Log.e(TAG, this.getClass().getSimpleName()+" handleMessage() - msg.what - CLICK_ON_WEBVIEW - "+msg.what);
            Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, this.getClass().getSimpleName()+" shouldOverrideUrlLoading() - URL - "+url);
            handler.sendEmptyMessage(CLICK_ON_URL);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, this.getClass().getSimpleName()+" onPageStarted() - URL - "+url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, this.getClass().getSimpleName()+" onPageFinished() - URL - "+url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.d(TAG, this.getClass().getSimpleName()+" onLoadResource() - URL - "+url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            Log.d(TAG, this.getClass().getSimpleName()+" onPageCommitVisible() - URL - "+url);
            super.onPageCommitVisible(view, url);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.d(TAG, this.getClass().getSimpleName()+" shouldInterceptRequest() - URL - "+url);
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            Log.d(TAG, this.getClass().getSimpleName()+" onTooManyRedirects()");
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, this.getClass().getSimpleName()+" onReceivedError()");
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
             Log.d(TAG, this.getClass().getSimpleName()+" onFormResubmission()");
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Log.d(TAG, this.getClass().getSimpleName()+" doUpdateVisitedHistory() - URL - "+url);
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
             Log.d(TAG, this.getClass().getSimpleName()+" onReceivedSslError()");
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
             Log.d(TAG, this.getClass().getSimpleName()+" onReceivedClientCertRequest()");
            super.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
             Log.d(TAG, this.getClass().getSimpleName()+" onReceivedHttpAuthRequest()");
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
             Log.d(TAG, this.getClass().getSimpleName()+" shouldOverrideKeyEvent()");
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            Log.d(TAG, this.getClass().getSimpleName()+" onUnhandledKeyEvent()");
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            Log.d(TAG, this.getClass().getSimpleName()+" onScaleChanged()");
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
            Log.d(TAG, this.getClass().getSimpleName()+" onReceivedLoginRequest()");
            super.onReceivedLoginRequest(view, realm, account, args);
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            Log.d(TAG, this.getClass().getSimpleName()+" onRenderProcessGone()");
            return super.onRenderProcessGone(view, detail);
        }

        @Override
        public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
            Log.d(TAG, this.getClass().getSimpleName()+" onSafeBrowsingHit()");
            super.onSafeBrowsingHit(view, request, threatType, callback);
        }
    }
}
