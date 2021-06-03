package de.titti.recipebridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private static final String URL_ROUTE = "/new/recipe";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if(loadConnection() != null){
            url = loadConnection();
        }


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        System.out.println(sharedText);
        JSONObject jsonBody = new JSONObject();
        String mRequestBody = "";
        if (sharedText != null) {
            // Update UI to reflect text being shared
            //System.out.println(sharedText);

            try {
                jsonBody.put("url", sharedText);
                mRequestBody = jsonBody.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //send url to device
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
//            String url ="https://www.google.com";
//            String url = "http://192.168.15.1:8080";

// Request a string response from the provided URL.
            String finalMRequestBody = mRequestBody;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url + URL_ROUTE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Toast.makeText(MainActivity.this, "Dein Rezept ist jetzt bereit", Toast.LENGTH_LONG).show();
                            //textView.setText("Response is: "+ response.substring(0,500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Überprüfe die Verbindung und ob das Gerät eingeschaltet ist", Toast.LENGTH_LONG).show();
//                    System.out.println(error.toString());
                    //textView.setText("That didn't work!");
                }
            })
            {
                @Override
                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
                    return "application/text; charset=utf-8";

                }

                @Override
                public byte[] getBody() {
                    try {
                        return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return "".getBytes();
                    }
//                    return finalMRequestBody == null ? null : finalMRequestBody.getBytes();
                }
//            {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("data", sharedText);
//
//
//                    return params;
//                }
//
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> params = new HashMap<String, String>();
////                    params.put("Content-Type", "application/x-www-form-urlencoded");
//                    params.put("Content-Type", "utf-8");
//
//                    return params;
//                }
            };

// Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    0, //DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Toast.makeText(MainActivity.this, "Die Kräuter werden gepflückt", Toast.LENGTH_LONG).show();
            queue.add(stringRequest);
            //tv.setText(sharedText);
        }
    }

    private String loadConnection(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String connection = sharedPref.getString(DEVICE_ADDRESS, "");
        return connection;
    }

}