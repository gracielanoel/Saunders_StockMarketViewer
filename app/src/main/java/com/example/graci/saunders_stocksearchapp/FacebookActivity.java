package com.example.graci.saunders_stocksearchapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacebookActivity extends Activity {
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private LoginManager manager;
    ArrayList<String> fbDetails = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("In Fb Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        FacebookSdk.sdkInitialize(getApplicationContext());
        System.out.println("In Fb Activity 2");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        manager = LoginManager.getInstance();

        manager.logInWithPublishPermissions(this, permissionNeeds);
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                fbShare();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
                LoginManager.getInstance().logOut();
                fbShare();
                //finish();
            }

            @Override
            public void onError(FacebookException exception) {

                LoginManager.getInstance().logOut();
                //System.out.println("onError"+exception.toString());
                //finish();
                fbShare();
            }
        });
    }

    public void fbShare() {
        try {
            Intent intent = getIntent();
            fbDetails = intent.getStringArrayListExtra(ResultActivity.EXTRA_FACEBOOK);
            /*String summ = intent.getExtras().getString("SUMMARY");
            String desc = intent.getExtras().getString("DESC");
            String url = intent.getExtras().getString("URL");
            String icon = intent.getExtras().getString("ICON");*/

            String symbolFB = fbDetails.get(0);

            String shareText = "Hi! We are sharing.You share ";
            Log.i("shareText", shareText);
            shareDialog = new ShareDialog(this);
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {


                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(FacebookActivity.this, "You shared this post", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "SHARE SUCCESS");
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FacebookActivity.this, "Post Cancelled", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "SHARE CANCELLED");
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FacebookActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("DEBUG", "Share: " + exception.getMessage());
                        exception.printStackTrace();
                        finish();
                    }
                });

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Current Stock Price of " + fbDetails.get(0) + ", $" + fbDetails.get(1))
                        .setContentDescription("Stock Information of " + fbDetails.get(0))
                        .setContentUrl(Uri.parse(fbDetails.get(2)))
                        .setImageUrl(Uri.parse(fbDetails.get(3)))
                        .build();
                shareDialog.show(linkContent);


            } else {
                Log.i("ERROR", "cannot show share");
            }
        }
        catch (Exception e){
            Log.d("FBEXCEPTION", e.toString());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode,    Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}
