package com.bambacode.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView playList;
    Button playPauseBtn;
    Button stopBtn;
    TextView nowPlayingText;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playList = findViewById(R.id.lv_playList);
        playPauseBtn = findViewById(R.id.btn_PlayPause);
        nowPlayingText = findViewById(R.id.tv_NowPlaying);
        stopBtn = findViewById(R.id.btn_Stop);

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseBtn.setText("Play");
                } else if (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() > 1) {
                    // The player is paused
                    mediaPlayer.start();
                    playPauseBtn.setText("Pause");
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                playPauseBtn.setText("Play");
                stopBtn.setEnabled(false);
            }
        });

        SongDataService songDataService = new SongDataService(this);
        songDataService.fetchPlaylist(new SongDataService.VolleyResponseListener() {
            @Override
            public void onResponse(List response) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, response);
                playList.setAdapter(arrayAdapter);
                playList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        JSONObject song = (JSONObject) adapterView.getItemAtPosition(i);
                        String audioUrl = "";
                        String name = "";

                        try {
                            name = song.getString("name");
                            audioUrl = song.getString("music_url");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, audioUrl, Toast.LENGTH_LONG).show();

                        playSong(name, audioUrl);

                    }
                });
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void playSong(String name, String audioUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mediaPlayer.start();
        playPauseBtn.setText("Pause");
        nowPlayingText.setText("Now playing: " + name);
        stopBtn.setEnabled(true);
    }

    private void pauseSong(MediaPlayer mediaPlayer) {
        mediaPlayer.pause();
        Toast.makeText(MainActivity.this, "Pausing song", Toast.LENGTH_LONG).show();
    }
}