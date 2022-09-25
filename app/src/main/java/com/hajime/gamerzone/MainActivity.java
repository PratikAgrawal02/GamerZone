package com.hajime.gamerzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    MediaPlayer click, congo;
    SeekBar volumecontrol;
    AudioManager audioManager;

    public AdView mAdview;
    int adcounter=0;

    String playername[]={"Player 1", "Player 2"},winnername;
    Boolean gameactive = true,flag=false;
    int ipl1score=0, ipl2score=0,count=0, maxscore=3;

    int activeplayer, status[]={2,2,2,2,2,2,2,2,2},win[][]={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          final SharedPreferences countPref=getSharedPreferences("Counter", Context.MODE_PRIVATE);
          adcounter=countPref.getInt("count",0);
          if(adcounter<=3){
              LoadAdsMethod();
          }

        SharedPreferences.Editor editorAdsControl = countPref.edit();
        if (countPref.getLong("Expireddate", -1)>System.currentTimeMillis()){
            //Log.i()
            }else
            {
                editorAdsControl.clear();
                editorAdsControl.apply();
            }

        player = MediaPlayer.create(this, R.raw.mrbean);
        player.setLooping(true); // Set looping
        volumecontrol= (SeekBar) findViewById(R.id.seekBar);
        click = MediaPlayer.create(this, R.raw.click);
        click.setVolume(100,100);
        congo = MediaPlayer.create(this, R.raw.congo);
        congo.setVolume(100,100);


        Random rand= new Random();
        activeplayer= rand.nextInt(2);


        AlertDialog.Builder mbuilder= new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.playername, null);
        mbuilder.setView(mView);
        AlertDialog dialog = mbuilder.create();
        dialog.show();


        EditText player1= (EditText) mView.findViewById(R.id.player1);
        EditText player2= (EditText) mView.findViewById(R.id.player2);
        Button start = (Button) mView.findViewById(R.id.button);
        ImageView pl2 = (ImageView) mView.findViewById(R.id.pl2);
        ImageView pl1 = (ImageView) mView.findViewById(R.id.pl1);


        TextView pl1name= (TextView) findViewById(R.id.textViewpl1);
        TextView turn= (TextView) findViewById(R.id.Turn);
        TextView pl2name= (TextView) findViewById(R.id.textViewpl2);


        pl1.setImageResource(R.drawable.pl2);
        pl2.setImageResource(R.drawable.play1);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!player1.getText().toString().isEmpty() && !player2.getText().toString().isEmpty()){
                    playername[0]=player1.getText().toString();
                    pl1name.setText(player1.getText().toString());
                    pl2name.setText(player2.getText().toString());
                    playername[1]=player2.getText().toString();
                    dialog.dismiss();
                    Log.i("active", " "+activeplayer);
                    Toast.makeText(MainActivity.this, "Its Your Turn first "+ playername[activeplayer] , Toast.LENGTH_SHORT).show();
                    turn.setText("Your Turn: "+playername[activeplayer]);

                }
                else {
                    Toast.makeText(MainActivity.this, "Please Enter a player name for both players", Toast.LENGTH_SHORT).show();
                }
            }
        });

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxvol= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currvol= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumecontrol.setMax(maxvol);
        volumecontrol.setProgress(currvol);

        volumecontrol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void LoadAdsMethod(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus){
            }
        });
        mAdview=findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(adcounter<=4){
            mAdview.loadAd(adRequest);
        }

        mAdview.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                adcounter++;
                SharedPreferences countPref=getSharedPreferences("Counter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorAdsControl = countPref.edit();
                editorAdsControl.putInt("count", adcounter);
                editorAdsControl.putLong("Expireddate", System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(1440));
                editorAdsControl.apply();
                Log.i("ad", ""+adcounter);
                if(adcounter>=3){
                    mAdview.setVisibility(View.INVISIBLE);
                }

            }
        });
    }



     public  void  Volume(View view){
        Button volumeslide= (Button) view;
        if(volumecontrol.getVisibility()== View.VISIBLE){
            volumecontrol.setVisibility(View.INVISIBLE);
        }
        else {
            volumecontrol.setVisibility(View.VISIBLE);

        }
     }
    public void onClick(View view)
    {
        ImageView imageView= (ImageView) view;
        Button reset = (Button) findViewById(R.id.reset);
        TextView turn= (TextView) findViewById(R.id.Turn);

        int tag = Integer.parseInt(imageView.getTag().toString())-1;
        if(status[tag]==2 && gameactive)
        {
            click.start();
            count++;

            Log.i("active", " "+activeplayer);
            if (activeplayer == 1) {
                activeplayer = 0;
                imageView.setImageResource(R.drawable.circle);
            } else {
                activeplayer = 1;
                imageView.setImageResource(R.drawable.pngegg);
            }
            turn.setText("Your Turn: "+playername[activeplayer]);
            imageView.animate().alpha(1).setDuration(600);


            status[tag] = activeplayer;

            for(int wining[]:win){
                if(status[wining[0]]==status[wining[1]] && status[wining[2]]==status[wining[1]] && status[wining[0]] !=2){
                    gameactive=false;

                    count=0;
                    TextView pl1score = (TextView) findViewById(R.id.pl1score);
                    TextView pl2score = (TextView) findViewById(R.id.pl2score);
                    if (activeplayer == 1) {
                        activeplayer = 0;
                        String a = ""+String.valueOf(++ipl1score);
                        pl1score.setText(a);

                    } else {
                        activeplayer = 1;
                        String b =""+String.valueOf(++ipl2score);
                        pl2score.setText(b);
                    }
                    Toast.makeText(this, "You got a Score: "+ playername[activeplayer], Toast.LENGTH_SHORT).show();
                    if(ipl1score>=maxscore || ipl2score>=maxscore){
                        winnername= playername[activeplayer];
                        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
                        ipl2score=0;
                        ipl1score=0;
                        flag=true;
                        AlertDialog.Builder mbuilder2= new AlertDialog.Builder(MainActivity.this);
                        View mView2 = getLayoutInflater().inflate(R.layout.congrats, null);
                        TextView winner = (TextView) mView2.findViewById(R.id.winnertext) ;
                        winner.setText(""+playername[activeplayer]);
                        mbuilder2.setView(mView2);
                        player.pause();
                        congo.start();
                        reset.setText("Play again");
                        AlertDialog dialog2 = mbuilder2.create();
                        dialog2.show();
                    }
                     reset.animate().alpha(1).setDuration(1200);



                }
            }
            if(count>=9 && gameactive){
                count=0;
                gameactive=false;
                reset.animate().alpha(1).setDuration(1200);
                Toast.makeText(this, "Oh! Looks like its a Draw", Toast.LENGTH_SHORT).show();

            }

        }

        if(volumecontrol.getVisibility()== View.VISIBLE){
            volumecontrol.setVisibility(View.INVISIBLE);
        }

    }
    public void reset(View view)
    {


        Button reset = (Button) findViewById(R.id.reset);

        ImageView imageView1 =(ImageView) findViewById(R.id.imageView);
        ImageView imageView2 =(ImageView) findViewById(R.id.imageView2);
        ImageView imageView3 =(ImageView) findViewById(R.id.imageView3);
        ImageView imageView4 =(ImageView) findViewById(R.id.imageView4);
        ImageView imageView5 =(ImageView) findViewById(R.id.imageView5);
        ImageView imageView6 =(ImageView) findViewById(R.id.imageView6);
        ImageView imageView7 =(ImageView) findViewById(R.id.imageView7);
        ImageView imageView8 =(ImageView) findViewById(R.id.imageView8);
        ImageView imageView9 =(ImageView) findViewById(R.id.imageView9);

        imageView1.animate().alpha(0).setDuration(500);
        imageView2.animate().alpha(0).setDuration(500);
        imageView3.animate().alpha(0).setDuration(500);
        imageView4.animate().alpha(0).setDuration(500);
        imageView5.animate().alpha(0).setDuration(500);
        imageView6.animate().alpha(0).setDuration(500);
        imageView7.animate().alpha(0).setDuration(500);
        imageView8.animate().alpha(0).setDuration(500);
        imageView9.animate().alpha(0).setDuration(500);

        TextView pl1score = (TextView) findViewById(R.id.pl1score);
        TextView pl2score = (TextView) findViewById(R.id.pl2score);
        TextView turn= (TextView) findViewById(R.id.Turn);

        if(activeplayer==1){
            activeplayer=0;
        }
        else {
            activeplayer=1;
        }
        reset.animate().alpha(0).setDuration(600);
        turn.setText("Your Turn: "+playername[activeplayer]);

        if(flag){

             String zero= ""+String.valueOf(0);
             pl1score.setText(zero);
             pl2score.setText(zero);
             flag=false;
             player.start();
             reset.setText("Next Round");
        }
        Toast.makeText(MainActivity.this, "Lets start with you: " + playername[activeplayer], Toast.LENGTH_SHORT).show();
        for (int i = 0; i < 9; i++) status[i] = 2;
        gameactive = true;
    }

    @Override
    public void onBackPressed() {
        player.pause();
        //stopService(svc);
        super.onBackPressed();
    }


    @Override
    protected void onUserLeaveHint() {
        //stopService(svc);
        player.pause();
        super.onUserLeaveHint();
    }

    @Override
    protected void onStart() {
        //startService(svc);
        player.start();
        super.onStart();
    }

}
