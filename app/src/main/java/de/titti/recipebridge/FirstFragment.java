package de.titti.recipebridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    public final String DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public final String ROUTE_INGREDIENT = "/ingredient/page";
    public final String ROUTE_RECIPE = "/recipe/page";
    public final String ROUTE_SCREENSAVER = "/screensaver";
    private String url;
    EditText deviceAddress;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceAddress = view.findViewById(R.id.editTextDeviceAdress);
        if (loadConnection() != null) {
            deviceAddress.setText(loadConnection());
            url =deviceAddress.getText().toString();
        }
        view.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deviceAddress.getText() != null){
                    //sende get an die adresse um zu sehen, ob sie antwortet, und speichere die eingegebene adresse als neuen default
                    //check if url is live
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getActivity());


// Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, deviceAddress.getText().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    //save connection as default
                                    saveNewConnection(deviceAddress.getText().toString());
                                    Toast.makeText(getActivity(), "Verbindung wurde gespeichert",Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Das Gerät ist nicht erreichbar",Toast.LENGTH_LONG).show();
                        }
                    });

// Add the request to the RequestQueue.
                    queue.add(stringRequest);

                }
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.buttonRecipeBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonBody = new JSONObject();
                String mRequestBody = "";
                try {
                    jsonBody.put("direction", "--");
                    mRequestBody = jsonBody.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCommand(mRequestBody, ROUTE_RECIPE);
            }
        });
        view.findViewById(R.id.buttonRecipeForward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonBody = new JSONObject();
                String mRequestBody = "";
                try {
                    jsonBody.put("direction", "++");
                    mRequestBody = jsonBody.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCommand(mRequestBody, ROUTE_RECIPE);
            }
        });
        view.findViewById(R.id.buttonIngredientsUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonBody = new JSONObject();
                String mRequestBody = "";
                try {
                    jsonBody.put("direction", "--");
                    mRequestBody = jsonBody.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCommand(mRequestBody, ROUTE_INGREDIENT);
            }
        });
        view.findViewById(R.id.buttonIngredientsDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonBody = new JSONObject();
                String mRequestBody = "";
                try {
                    jsonBody.put("direction", "++");
                    mRequestBody = jsonBody.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCommand(mRequestBody, ROUTE_INGREDIENT);
            }
        });
        view.findViewById(R.id.buttonScreensaver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("", ROUTE_SCREENSAVER);
            }
        });

    }

    private void sendCommand(String requestBody, String route){

        if (requestBody != null) {
            // Update UI to reflect text being shared
            //System.out.println(sharedText);




            //send url to device
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());
//            String url ="https://www.google.com";
//            String url = "http://192.168.15.1:8080";

// Request a string response from the provided URL.
            String finalMRequestBody = requestBody;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url + route,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Toast.makeText(getActivity(), "Erledigt", Toast.LENGTH_LONG).show();
                            //textView.setText("Response is: "+ response.substring(0,500));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Überprüfe die Verbindung und ob das Gerät eingeschaltet ist", Toast.LENGTH_LONG).show();
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
            queue.add(stringRequest);
            //tv.setText(sharedText);
        }
    }

    private void saveNewConnection(String deviceAddress){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
        editor.putString(DEVICE_ADDRESS, deviceAddress);
        editor.apply();
    }

    private String loadConnection(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String connection = sharedPref.getString(DEVICE_ADDRESS, "");
        return connection;
    }

}