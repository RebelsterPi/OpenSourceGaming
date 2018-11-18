package com.example.dell.game2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by DELL on 21-09-2018.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
private RectPlayer player;
private Point playerPoint;
private ObstacleManager obstacleManager;

    private MainThread thread;
    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this );
        thread = new MainThread(getHolder(),this);

        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(10,220,225));
        playerPoint= new Point(150,150);

        obstacleManager= new ObstacleManager(200,350,75,Color.BLACK);

        setFocusable(true);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height ){

    }
    //helps restart
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry =true;
        while(true){
            try{thread.setRunning(false);
                thread.join();
            }catch(Exception e){e.printStackTrace();}
            retry = false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
       switch(event.getAction()){case MotionEvent.ACTION_DOWN:
           case MotionEvent.ACTION_MOVE:
               playerPoint.set((int)event.getX(),(int )event.getY());
       }
        return true;
        // return super.onTouchEvent(event);
    }
    public void update() {

        player.update(playerPoint);
        obstacleManager.update();
    }
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        obstacleManager.draw(canvas);
    }

}