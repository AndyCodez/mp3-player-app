package com.bambacode.mp3player;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView playList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playList = findViewById(R.id.lv_playList);

        SongDataService songDataService = new SongDataService(this);
        songDataService.fetchPlaylist(new SongDataService.VolleyResponseListener() {
            @Override
            public void onResponse(List response) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, response);
                playList.setAdapter(arrayAdapter);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}