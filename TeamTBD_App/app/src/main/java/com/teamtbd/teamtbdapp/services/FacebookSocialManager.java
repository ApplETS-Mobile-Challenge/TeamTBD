package com.teamtbd.teamtbdapp.services;

import android.app.Activity;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import java.util.Arrays;

public class FacebookSocialManager{
    public static final String APP_ID = "834720529983179";

    Activity activity;

    boolean loginSuccess = false;

    CallbackManager callbackManager;

    private ProfileTracker profileTracker;

    public FacebookSocialManager(Activity activity) {
        this.activity = activity;
        FacebookSdk.sdkInitialize(activity.getApplicationContext());

        if (AccessToken.getCurrentAccessToken() == null) {
            callbackManager = CallbackManager.Factory.create();
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                    Profile.setCurrentProfile(profile1);
                }
            };
            profileTracker.startTracking();

            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // TODO
                    loginSuccess = true;
                }

                @Override
                public void onCancel() {
                    loginSuccess = false;
                }

                @Override
                public void onError(FacebookException e) {
                    loginSuccess = false;
                }
            });
        }
    }

    public void login() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends"));

    }

    public void logOut() {
        LoginManager.getInstance().logOut();
        loginSuccess = false;
    }

    public boolean isLogged() {
        return loginSuccess;
    }

    public String getFirstName() {
        if (Profile.getCurrentProfile() != null)
            return Profile.getCurrentProfile().getFirstName();
        return "";
    }

    public String getLastName() {
        if (Profile.getCurrentProfile() != null)
            return Profile.getCurrentProfile().getLastName();
        return "";
    }

    public String getID() {
        if (Profile.getCurrentProfile() != null)
            return Profile.getCurrentProfile().getId();
        return "";
    }

    public void sendInvites() {
        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder().setApplinkUrl("http://www.google.ca/").setPreviewImageUrl("http://i.imgur.com/dW3s3QL.png").build();
            AppInviteDialog.show(activity, content);
        }
    }



    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy() {

    }
}
