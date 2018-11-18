package com.example.dell.game2d;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by DELL on 23-09-2018.
 */

public class  ObstacleManager  {
    private ArrayList<Obstacle> obstacle;
    private int playerGap;
private int obstacleGap;
private int obstacleHeight;
private int color;
private long startTime;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight,int color){
        this.playerGap=playerGap;
        this.obstacleGap=obstacleGap;
        this.obstacleHeight= obstacleHeight;
        this.color = color;
        startTime= System.currentTimeMillis();
        obstacle=new ArrayList<>();

        populateObstacle();

    }

    private void populateObstacle(){

        int currY =-5*Constants.SCREEN_HEIGHT/4;
        while(currY<0){
            int xStart= (int)(Math.random()*(Constants.SCREEN_WIDTH-playerGap));
            obstacle.add(new Obstacle(obstacleHeight,color,xStart,currY, playerGap));
      currY+=obstacleHeight+ obstacleGap;

        }
    }
    public void update(){
int elapsedTime=(int)( System.currentTimeMillis()-startTime);
startTime=System.currentTimeMillis();
float speed = Constants.SCREEN_HEIGHT/10000.0f;
for (Obstacle ob : obstacle){
ob.incrementY(speed*elapsedTime);
}
if (obstacle.get(obstacle.size()-1).getRectangle().top>=Constants.SCREEN_HEIGHT){
    int xStart= (int)(Math.random()*(Constants.SCREEN_WIDTH-playerGap));
    obstacle.add(0,new Obstacle(obstacleHeight,color,xStart,obstacle.get(0).getRectangle().top-obstacleHeight-obstacleGap,playerGap));
    obstacle.remove(obstacle.size()-1);

}
   }
    public void draw (Canvas canvas){
        for (Obstacle ob : obstacle){
            ob.draw(canvas);
        }
    }
}
