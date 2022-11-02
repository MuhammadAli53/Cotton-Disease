package com.farmer.wormdetection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

public class Logout_dialog extends Dialog implements
        View.OnClickListener {

    FirebaseAuth mAuth;
    public Activity activity;
    public TextView yes, no;

    public Logout_dialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logout_dialog);

        yes = (TextView) findViewById(R.id.tv_yes);
        no = (TextView) findViewById(R.id.tv_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yes:
                activity.finishAffinity();
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            case R.id.tv_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}