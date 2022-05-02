package com.bambacode.mp3player;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SongDataService {
    Context context;
    public static final String URL = "https://orangevalleycaa.org/api/music";

    public SongDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onResponse(JSONObject response);
        void onErrorResponse(VolleyError error);
    }

    public void getSong(VolleyResponseListener volleyResponseListener) {
        final JSONObject[] song = new JSONObject[1];

        // Request a JsonArray response from the provided URL.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    song[0] = response.getJSONObject(0);
                    volleyResponseListener.onResponse(song[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onErrorResponse(error);
            }
        });

        // Add the request to the RequestQueue.
        RequestsSingleton.getInstance(context).addToRequestQueue(request);
    }
}
