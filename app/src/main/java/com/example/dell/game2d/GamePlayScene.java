package com.example.dell.game2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GamePlayScene implements Scene{
    private RectPlayer player;

    private Rect r= new Rect();


    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean movingPlayer = false;
    private boolean gameOver =false;
    private long gameOverTime;
public GamePlayScene(){  player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(10,220,225));
    playerPoint= new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
    player.update(playerPoint);

    obstacleManager= new ObstacleManager(200,350,75,Color.BLACK);
}

    public void reset(){
        playerPoint= new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager= new ObstacleManager(200,350,75,Color.BLACK);
        movingPlayer = false;
    }


    @Override
    public void update() {
        if (!gameOver){
            player.update(playerPoint);
            obstacleManager.update();
            if (obstacleManager.playerCollide(player)){
                gameOver=true;
                gameOverTime=System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.RED);
            drawCenterText(canvas,paint,"GAME OVER");

        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE=0;
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch(event.getAction()){case MotionEvent.ACTION_DOWN:
            if (!gameOver&&player.getRectangle().contains((int)event.getX(),(int) event.getY()))
                movingPlayer=true;
            if (gameOver&& System.currentTimeMillis()-gameOverTime>=20000){
                reset();
                gameOver=false;
            }
            break;
            case MotionEvent.ACTION_MOVE:
                if (!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(),(int )event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer=false;
                break;
        }
    }
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        //  paint.setColor(Color.rgb(255, 255, 255));
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
        // drawTextBounds(canvas, r, (int) x, (int) y);
    }
}
