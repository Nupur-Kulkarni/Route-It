package com.example.deepika.travelguide.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.example.deepika.travelguide.R;

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

import static android.content.ContentValues.TAG;


@SuppressLint("ValidFragment")
public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText useremail, pwd;
    ImageButton bt_login;
    String BASE_URL = "";
    private static final String LOGIN_URL = "login.php";
    sessionManagement session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View log =inflater.inflate(R.layout.fragment_login, container, false);
        session = new sessionManagement(getActivity().getApplicationContext());
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"font/irmatextroundstdbold.otf");

        useremail = (EditText) log.findViewById(R.id.editText);
        pwd = (EditText) log.findViewById(R.id.editText2);
        useremail.setTypeface(font);
        pwd.setTypeface(font);
        log.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        BASE_URL = getString(R.string.baseUrl);
        bt_login = (ImageButton)log.findViewById(R.id.login);
        bt_login.setOnClickListener(this);
        return  log;
    }


    @Override
    public void onClick(View v) {
        final String email = useremail.getText().toString().trim().toLowerCase();
        final String password = pwd.getText().toString().trim().toLowerCase();
        if (email.equalsIgnoreCase("")) {
            useremail.setError("Invalid Email");
        }
        if (password.equalsIgnoreCase("")) {
            pwd.setError("Invalid Password");
        }
        if (!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
            Log.d(TAG, "login successful");
            String[] data = new String[2];
            data[0] = email;
            data[1] = password;
            session.createLoginSession(password, email);
            LoginUser loginTask = new LoginUser();
            loginTask.execute(data);
        } else {
            Log.w(TAG, "login failed");
            Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }

    }
    private class LoginUser extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            SharedPreferences details = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor e = details.edit();

            e.putString("email", String.valueOf(useremail.getText().toString()));
            e.apply();

            //redirect to maps activity
          //  Log.d("RESPONSE: ", response);
          //  if(response!=("User does not exist!"))
            {
                Intent intent = new Intent(getActivity().getApplicationContext(), SelectCity.class);
                intent.putExtra("email", String.valueOf(useremail.getText()));
                startActivity(intent);
            }
          //  Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.",
          //          Toast.LENGTH_SHORT).show();

            //startActivity(i);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            String response = "";
            try {

                url = new URL(BASE_URL + LOGIN_URL);

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
                        .put("password", params[1])
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
    }
}





