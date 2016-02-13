package com.teamtbd.teamtbdapp.activities;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.teamtbd.teamtbdapp.R;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setProfileId(Profile.getCurrentProfile().getId());

        TextView firstNameTextView = (TextView) findViewById(R.id.firstName);
        firstNameTextView.setText(Profile.getCurrentProfile().getFirstName());

        TextView lastNameTextView = (TextView) findViewById(R.id.lastName);
        lastNameTextView.setText(Profile.getCurrentProfile().getLastName());

    }
}
