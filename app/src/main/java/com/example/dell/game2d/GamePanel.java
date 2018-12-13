package com.example.dell.game2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by DELL on 21-09-2018.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{

private SceneManager manager;
    private MainThread thread;
    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this );

        Constants.CURRENT_CONTEXT=context;

        thread = new MainThread(getHolder(),this);
        manager= new SceneManager();
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
        manager.recieveTouch(event);
        return true;
        // return super.onTouchEvent(event);
    }
    public void update() {
manager.update();

    }
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        manager.draw(canvas);
    }


}