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
    private OrientationData orientationData;
    private long frameTime;


public GamePlayScene(){  player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(10,220,225));
    playerPoint= new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
    player.update(playerPoint);

    obstacleManager= new ObstacleManager(200,350,75,Color.WHITE);
    orientationData= new OrientationData();
    orientationData
            .register();
    frameTime= System.currentTimeMillis();

}

    public void reset(){
        playerPoint= new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager= new ObstacleManager(200,350,75,Color.WHITE);
        movingPlayer = false;
    }


    @Override
    public void update() {
        if (!gameOver){

            if(frameTime<Constants.INIT_TIME)
                frameTime=Constants.INIT_TIME;
            int elapseTime= (int)(System.currentTimeMillis()-frameTime);
            frameTime=System.currentTimeMillis();

            if (orientationData.getOrientation()!=null && orientationData.getStartOrientation()!=null){
                float pitch = orientationData.getOrientation()[1]-orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2]-orientationData.getStartOrientation()[2];
                float xSpeed = 2*roll* Constants.SCREEN_WIDTH/500f;
                float ySpeed =pitch *Constants.SCREEN_HEIGHT/500f;
                playerPoint.x += Math.abs(xSpeed*elapseTime)>5 ? xSpeed*elapseTime : 0 ;
                playerPoint.y -= Math.abs(ySpeed*elapseTime)>5 ? ySpeed*elapseTime : 0 ;

            }

            if (playerPoint.x<0)
                playerPoint.x = 0;
            else if (playerPoint.x>Constants.SCREEN_WIDTH)
                playerPoint.x= Constants.SCREEN_WIDTH;
            if (playerPoint.y<0)
                playerPoint.y = 0;
            else if (playerPoint.y>Constants.SCREEN_HEIGHT)
                playerPoint.y= Constants.SCREEN_HEIGHT;


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
        canvas.drawColor(Color.BLACK);
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
                orientationData.newGame();
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
