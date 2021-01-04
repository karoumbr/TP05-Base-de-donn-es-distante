package com.bentechprotv.android.basedistante;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProduitsActivity extends AppCompatActivity {
    String ms ="";
    EditText  txtDesignation;
    TextView txtListeProduits;
    Button btnInsererProduit,btnListeProduits;
    ListView lsvListeProduits;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);
        txtListeProduits =  (TextView) findViewById(R.id.txtListeProduits);

        txtDesignation = (EditText) findViewById(R.id.txtDesignation);
        btnInsererProduit = (Button) findViewById(R.id.btnInsererProduit);
        btnListeProduits= (Button) findViewById(R.id.btnListeProduits);
        lsvListeProduits = (ListView) findViewById(R.id.lsvListeProduits);
        btnListeProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg_selectProduits bgs =new bg_selectProduits(getApplicationContext());
                bgs.execute();

            }
        });


        btnInsererProduit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String des = txtDesignation.getText().toString();
                // code à compléter
                bg_insertion_produit bg = new bg_insertion_produit(ProduitsActivity.this);
                bg.execute(des);
            }
        });
    }


    private class bg_insertion_produit extends AsyncTask<String, Void, String> {

        AlertDialog dialog;
        Context context;

        public bg_insertion_produit(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Etat de connexion");
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            String strdes = strings[0];

            String connstr = "http://192.168.100.116/bentechprotv/insertion_produit.php";
            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
                String data = URLEncoder.encode("des", "UTF-8") + "=" + URLEncoder.encode(strdes, "UTF-8");

                writer.write(data);
                Log.v("ProduitsActivity", data);
                writer.flush();
                writer.close();
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                String ligne = "";
                while ((ligne = reader.readLine()) != null) {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.setMessage(s);
            dialog.show();
            if (s.contains("succes insertion")) {
                Toast.makeText(context, "Produit inséré avec succès.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Problème d'insertion.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class bg_selectProduits extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog dialog;

        public bg_selectProduits(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(ProduitsActivity.this).create();
            dialog.setTitle("Affichage Liste Produits...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String connstr = "http://192.168.100.116/bentechprotv/getAllProduits.php";
            URL url = null;
            try {
                url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setDoInput(true);
                //http.setDoOutput(true);

                InputStream ips =  http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String ligne ="";
                while ((ligne = reader.readLine()) != null)
                {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;
            } catch (IOException e) {
                result = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("")){
                ArrayList lst = parse(s);

                txtListeProduits.setText(ms);
                //remplir le listView
                /***
                 * Map : projection
                 * String (le premier élément : clé)
                 * String (le deuxième élément : valeur)
                 */

                List<Map<String, String>> data = new ArrayList<Map<String,String>>();
                ArrayList<Produit> produits = new ArrayList<>();
                try {
                    JSONArray jProduitArray = new JSONArray(s);
                    for (int i = 0; i < jProduitArray.length(); i++){
                        Map<String,String> datum = new HashMap<String,String>(2);
                        datum.put("id", jProduitArray.optJSONObject(i).getString("id"));
                        datum.put("designation", jProduitArray.optJSONObject(i).getString("designation"));
                        data.add(datum);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(ProduitsActivity.this,data,android.R.layout.simple_list_item_2,new String[] {"id","designation"},new int[] {android.R.id.text2,android.R.id.text1});
                lsvListeProduits.setAdapter(adapter);

            }


        }

        private ArrayList<Produit> parse(final String json) {
            ms = "";
            final ArrayList<Produit> produits = new ArrayList<>();
            try {
                final JSONArray jProduitArray = new JSONArray(json);
                for (int i =0; i< jProduitArray.length(); i++){
                    produits.add(new Produit(jProduitArray.optJSONObject(i)));
                    ms = ms + "Référence: " + jProduitArray.optJSONObject(i).optString("id") + " | " +
                            "Désignation: " + jProduitArray.optJSONObject(i).optString("designation") + "\n";
                }
                return produits;


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}