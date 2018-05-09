package com.example.deepika.travelguide;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.deepika.travelguide.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class signupFragment extends Fragment implements View.OnClickListener {
    EditText UserText, emailText, passText;
    ImageButton registration_tbn;
    private static final String TAG = "registrationActivity";
    String BASE_URL = "";
    private static final String REGISTER_URL = "registerUser.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View reg = inflater.inflate(R.layout.fragment_signup,container,false);
        BASE_URL = getString(R.string.baseUrl);
        UserText = (EditText) reg. findViewById(R.id.userText);
        emailText = (EditText) reg.findViewById(R.id.emailText);
        passText = (EditText) reg.findViewById(R.id.passText);
        registration_tbn = (ImageButton) reg.findViewById(R.id.s);
        registration_tbn.setOnClickListener(this);
        return  reg;
    }

    @Override
    public void onClick(View v) {
        final String full_name = UserText.getText().toString().trim().toLowerCase();
        final String password = passText.getText().toString().trim().toLowerCase();
        final String email = emailText.getText().toString().trim().toLowerCase();
        if (email.equalsIgnoreCase("")) {
            emailText.setError("Invalid Email");
        }
        if (password.equalsIgnoreCase("")) {
            passText.setError("Invalid Password");
        }
        if (full_name.equalsIgnoreCase("")) {
            UserText.setError("Invalid Name");
        }
        if (!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !full_name.equalsIgnoreCase("")) {
            Log.d(TAG, "registration successfully done");
            String[] input = new String[3];
            input[0] = email;
            input[1] = full_name;
            input[2] = password;
            RegisterUser registerTask = new RegisterUser();
            registerTask.execute(input);
        } else {
            Log.w(TAG, "registration failed");
            Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }

    }
    private class RegisterUser extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            URL url;
            String response = "";
            try {

                url = new URL(BASE_URL + REGISTER_URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                String requestJsonString = new JSONObject()
                        .put("email", params[0])
                        .put("full_name", params[1])
                        .put("password", params[2])
                        .toString();
                Log.d("REQUEST : ", requestJsonString);
                writer.write(requestJsonString);

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    line = br.readLine();
                    while (line != null) {
                        response += line;
                        line = br.readLine();
                    }

                    br.close();
                }
                Log.d("RESPONSE: ", response);
                conn.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Log.d("RESPONSE:", response);
            return response;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);

            SharedPreferences details = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor e = details.edit();

            e.putString("email", String.valueOf(emailText.getText()));
            e.apply();

            //redirect to maps activity
            Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            i.putExtra("email", String.valueOf(emailText.getText()));
            startActivity(i);

        }
    }
}




