package com.bentechprotv.android.basedistante;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText _txtLogin, _txtPassword;
    Button _btnConnexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _txtLogin = (EditText)findViewById(R.id.txtLogin);
        _txtPassword = (EditText) findViewById(R.id.txtPassword);
        _btnConnexion = (Button) findViewById(R.id.btnConnexion);
        _btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = _txtLogin.getText().toString();
                String passwd = _txtPassword.getText().toString();
                bg background = new bg(MainActivity.this);
                background.execute(user,passwd);
            }
        });




    }

    private class bg extends AsyncTask <String,Void,String> {
        AlertDialog dialog;
        Context c;
        public bg(Context context){
            this.c = context;
        }

        @Override
        protected void onPreExecute() {

            dialog = new AlertDialog.Builder(c).create();
            dialog.setTitle("Etat de connexion");
        }



        @Override
        protected String doInBackground(String... strings) {
            String result  ="";
            String user = strings[0];
            String pass = strings[1];
            //pour savoir votre adresse ip: lancer la commande "ipconfig" avec le programme cmd
            String connstr = "http://192.168.100.116/bentechprotv/login.php";
            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
                String data = URLEncoder.encode("user","UTF-8") + "=" + URLEncoder.encode(user,"UTF-8") +
                        "&&" + URLEncoder.encode("pass", "UTF-8")+ "=" + URLEncoder.encode(pass,"UTF-8");
                writer.write(data);
                writer.flush();
                writer.close();
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));
                String ligne ="";
                while ((ligne = reader.readLine())!= null){
                    result = result + ligne;
                    // ou bien result += ligne;

                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.setMessage(s);
           try {
               dialog.show();
           } catch (Exception e){
               Log.e("errorpost",e.getMessage());
           }

          if (s.contains("succes")){
              Intent i = new Intent();
              i.setClass(c.getApplicationContext(),ProduitsActivity.class);
              startActivity(i);
          }
        }
    }
}