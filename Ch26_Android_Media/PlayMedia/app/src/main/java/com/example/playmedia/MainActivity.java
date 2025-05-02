package com.example.playmedia;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button playButton;
    private SeekBar seekBar;
    private boolean isPrepared = false;
    private boolean isUserSeeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.play_button);
        seekBar = findViewById(R.id.seekBarView);
        playButton.setEnabled(false);
        seekBar.setEnabled(false);

        setupMediaPlayer();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
            
            mediaPlayer.setOnCompletionListener(mp -> {

                seekBar.setMax(mp.getDuration());

                runOnUiThread(() -> {
                    int duration = mp.getDuration();
                    int minutes = (duration / 1000) / 60;
                    int seconds = (duration / 1000) % 60;
                    Toast.makeText(MainActivity.this, 
                            String.format("Duration: %d:%02d", minutes, seconds),
                            Toast.LENGTH_LONG).show();
                    playButton.setText(R.string.play_text);
                });
            });

            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                runOnUiThread(() -> {
                    playButton.setEnabled(true);
                    seekBar.setEnabled(true);
                    seekBar.setMax(mp.getDuration());
                    playButton.setText(R.string.play_text);
                });
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error playing audio", Toast.LENGTH_SHORT).show();
                    playButton.setEnabled(false);
                    seekBar.setEnabled(false);
                });
                return false;
            });

            playButton.setOnClickListener(view -> {
                if (!isPrepared) return;
                
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    runOnUiThread(() -> playButton.setText(R.string.play_text));
                } else {
                    mediaPlayer.start();
                    runOnUiThread(() -> playButton.setText(R.string.pause_text));
                }
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && isPrepared) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    isUserSeeking = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    isUserSeeking = false;
                }
            });

            // Update seekbar position
            new Thread(() -> {
                while (mediaPlayer != null) {
                    try {
                        Thread.sleep(1000);
                        if (!isUserSeeking && isPrepared && mediaPlayer.isPlaying()) {
                            runOnUiThread(() -> seekBar.setProgress(mediaPlayer.getCurrentPosition()));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Toast.makeText(this, "Error setting up audio source", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}