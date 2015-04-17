package edu.uw.ProjectMayhem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A login screen that offers login via email/password.
 */
public class ResetActivity extends Activity {

    private SharedPreferences prefs;
    private Bundle savedInstance;

    private TextView mQuestion;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private EditText mEmail;
    private EditText mAnswer;
    private Button mResetButton;

    private String savedAnswer;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        setContentView(R.layout.activity_reset);

        mQuestion = (TextView) findViewById(R.id.sec_question);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mEmail = (EditText) findViewById(R.id.email);
        mAnswer = (EditText) findViewById(R.id.security_answer);
        mResetButton = (Button) findViewById(R.id.reset_password);

        Intent loginIntent = getIntent();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String securityQuestion = prefs.getString("security", "No saved question!");

        savedAnswer = prefs.getString("answer", "No answer");
        currentEmail = prefs.getString("email", "No email");

        mQuestion.setText(securityQuestion);

        mResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset(v);
            }
        });

    }

    private void reset(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);

        if (mEmail.getText().toString().equals(currentEmail) && mAnswer.getText().toString().equals(savedAnswer) && mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("password", mNewPassword.getText().toString());
            editor.commit();
            finish();
            startActivity(loginIntent);
        } else {
            Log.d("invalid token", "Invalid email or security answer");
            onCreate(savedInstance);
        }

    }

}