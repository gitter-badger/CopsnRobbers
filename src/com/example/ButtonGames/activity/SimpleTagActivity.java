package com.example.ButtonGames.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.example.ButtonGames.R;
import com.example.ButtonGames.model.Board;
import com.example.ButtonGames.model.Obstacle;
import com.example.ButtonGames.view.SimpleTagSurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SimpleTagActivity extends Activity{

    private Board board;
    private List<List<Obstacle>> maps;
    private Timer timer;
    private TimerTask timerTask;
    private SimpleTagSurfaceView stSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initMaps();
        board = new Board(maps.get(0));

        stSurfaceView = new SimpleTagSurfaceView(this);
        stSurfaceView.setBoard(board);

        setContentView(R.layout.game);

        addListenerOnButtons();

       startTimerTask();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
    }


    public void startTimerTask(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                board.updateBoard();
            }
        }, 1000, 1000);
    }

    public void initMaps(){
        List<Obstacle> simpleMap = new ArrayList<Obstacle>();
        simpleMap.add(new Obstacle(500.00, 800.00, 500.00, 800.00));
        maps = new ArrayList<List<Obstacle>>();
        maps.add(simpleMap);
    }


    public void addListenerOnButtons(){
        Button buttonR = (Button) findViewById(R.id.buttonR);
        Button buttonL = (Button) findViewById(R.id.buttonL);

        buttonR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRunning();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startSpinning();
                }
                return false;
            }
        });

        buttonL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startRunning();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startSpinning();
                }
                return false;
            }
        });
    }

    public void startRunning(){
        board.getPlayerR().setMoving(true);
        board.getPlayerR().setSpinning(false);
    }

    public void startSpinning(){
        board.getPlayerR().setMoving(false);
        board.getPlayerR().setSpinning(true);
    }


}
