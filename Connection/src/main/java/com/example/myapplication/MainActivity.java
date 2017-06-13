package com.example.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText et_url;
    Button btn_send;
    WebView webView;
    private String url = null;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        }
    private void init() {
        et_url = (EditText) findViewById(R.id.et_url);
        btn_send = (Button) findViewById(R.id.btn_send);
        webView = (WebView) findViewById(R.id.webView);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_url.getText().toString();  //去获取text中输入的网址
                url = "http://" + str;
                webView.loadUrl(url);            //设置到webView中去
            }
        });
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使网页可以再WebView中打开
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制网页在WebView中去打开，如果为false调用系统浏览器或者第三方浏览器打开
                view.loadUrl(url);
                return true;
            }//WebViewClient帮助WebView去处理一些页面控制和请求通知
        });

        //启用支持javaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress 1-100之间的整数
                if (newProgress == 100) {
                    //网页加载完毕,关闭ProgressDialog
                    closeDialo();
                } else {
                    //网页正在加载，打开ProgressDialog
                    openDialog(newProgress);
                    et_url.setText(webView.getUrl()); //实时显示当前网址
                    et_url.requestFocus();           //把输入焦点放在调用这个方法的控件上
                    et_url.setSelectAllOnFocus(true); //点击之后就被全选
                }
            }

            private void closeDialo() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

            private void openDialog(int newProgress) {
                if (dialog == null) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("加载中...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();
                } else {
                    dialog.setProgress(newProgress);
                }
            }
        });

    }
  //改写物理按键——返回的逻辑
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();   //返回上一页面
                return true;
            }else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode,event);
    }

}


