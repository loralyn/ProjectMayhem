/**
 * Project Mayhem: Jacob Hohisel, Loralyn Solomon, Brian Plocki, Brandon Soto
 */
package edu.uw.ProjectMayhem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A screen that allows the user to reset his/her password.
 */
public class ResetActivity extends ActionBarActivity {

    /**
     * Where the user types in email address.
     */
    private EditText mEmail;

    /**
     * Initiates the reset password process.
     */
    private Button mResetButton;

    /**
     * onCreate method creates the Reset Activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset);

        mEmail = (EditText) findViewById(R.id.email);
        mResetButton = (Button) findViewById(R.id.reset_password);

        Intent loginIntent = getIntent();

        mResetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset(v);
            }
        });

    }

    /**
     * Attempts to reset the user's password.
     *
     * @param view the view context of widget calling this method.
     */
    private void reset(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);

        // Kick off a background task to
        // perform the user login attempt.
        ResetPasswordTask task = new ResetPasswordTask();
        task.execute();
        String response = "";

        try {
            response = task.get();
        } catch (Exception e) {
            System.err.println("Something bad happened");
        }

        System.out.println("response: " + response);

        if (response != null) {
            try {

                JSONObject o = new JSONObject(response);

                System.out.println("Response: " + o.get("result"));

                if(o.get("result").equals("success")) {
                    startActivity(loginIntent);
                    Toast.makeText(this, o.get("message").toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, o.get("error").toString(), Toast.LENGTH_LONG).show();
                    System.out.println("Error: " + o.get("error"));
                }

            } catch (JSONException e) {
                System.out.println("JSON Exception " + e);
            }

        }

    }

    /**
     * Running the loading of the JSON in a separate thread.
     * Code adapted from http://www.vogella.com/tutorials/AndroidBackgroundProcessing/article.html
     */

    private class ResetPasswordTask extends AsyncTask<Void, Void, String> {


        private final String webURL = "http://450.atwebpages.com/reset.php";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = "";
            HttpURLConnection connection;
            URL url = null;
            String parameters = ("?email=" + mEmail.getText().toString());

            try
            {
                url = new URL(webURL + parameters);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("GET");

                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);

                result = reader.readLine();

                isr.close();
                reader.close();

            }
            catch(IOException e)
            {
                System.err.println("Something bad happened");
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }


}