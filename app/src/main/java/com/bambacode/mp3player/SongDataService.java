package com.bambacode.mp3player;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongDataService {
    Context context;
    public static final String URL = "https://orangevalleycaa.org/api/music";

    public SongDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onResponse(List response);
        void onErrorResponse(VolleyError error);
    }

    public void fetchPlaylist(VolleyResponseListener volleyResponseListener) {
        // Request a JsonArray response from the provided URL.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject songObject;
                List<JSONObject> songs = new ArrayList<>();

                for(int i=0; i <= response.length(); i++) {
                    try {
                        songObject = (JSONObject) response.get(i);
                        songs.add(songObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                volleyResponseListener.onResponse(songs);
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
