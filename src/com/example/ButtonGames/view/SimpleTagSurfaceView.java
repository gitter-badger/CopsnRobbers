package com.example.ButtonGames.view;

import android.content.Context;
import android.graphics.*;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.ButtonGames.R;
import com.example.ButtonGames.activity.GameLoopThread;
import com.example.ButtonGames.model.Board;
import com.example.ButtonGames.model.Obstacle;
import com.example.ButtonGames.model.Sprite;

import java.util.List;

public class SimpleTagSurfaceView extends SurfaceView{

    private SurfaceHolder sh;
    private Board board;
    public GameLoopThread gameLoopThread;
    private final Paint textL = new Paint(Paint.ANTI_ALIAS_FLAG); // Color/style/size/alignment of text for Lscore
    private final Paint textR = new Paint(Paint.ANTI_ALIAS_FLAG); // Color/style/size/alignment of text for Rscore
    private final Paint textT = new Paint(Paint.ANTI_ALIAS_FLAG); // Color/style/size/alignment of text for timer
    private final Paint textM = new Paint(Paint.ANTI_ALIAS_FLAG); // Color/style/size/alignment of text for message
    private final Paint obstacle = new Paint(Paint.ANTI_ALIAS_FLAG); // Color/style for obstacle

    // Bitmap of background
    private Bitmap background;

    // Bitmap of green hunted sprite
    private Bitmap spriteGreen1 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_green_1);
    private Bitmap spriteGreen2 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_green_2);
    private Bitmap spriteGreen3 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_green_3);

    // Bitmap of green hunter sprite
    private Bitmap HspriteGreen1 = BitmapFactory.decodeResource(getResources(), R.drawable.hunter_sprite_green_1);
    private Bitmap HspriteGreen2 = BitmapFactory.decodeResource(getResources(), R.drawable.hunter_sprite_green_2);
    private Bitmap HspriteGreen3 = BitmapFactory.decodeResource(getResources(), R.drawable.hunter_sprite_green_3);

    // Bitmap of purple hunted sprite
    private Bitmap spritePurple1 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_purple_1);
    private Bitmap spritePurple2 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_purple_2);
    private Bitmap spritePurple3 = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_purple_3);

    // Bitmap of purple hunter sprite
    private Bitmap HspritePurple1 = BitmapFactory.decodeResource(getResources(), R.drawable.hunger_sprite_purple_1);
    private Bitmap HspritePurple2 = BitmapFactory.decodeResource(getResources(), R.drawable.hunger_sprite_purple_2);
    private Bitmap HspritePurple3 = BitmapFactory.decodeResource(getResources(), R.drawable.hunger_sprite_purple_3);

    private Bitmap deadSprite = BitmapFactory.decodeResource(getResources(), R.drawable.dead_sprite);


    public SimpleTagSurfaceView(Context context, final Board board, Bitmap background) {
        super(context);

        //Set up color/style of score text timer and obstacles
        textL.setColor(Color.GREEN);
        textL.setStyle(Paint.Style.FILL);
        textL.setTextSize(board.getHeight() / 6);
        textL.setTextAlign(Paint.Align.LEFT);
        textL.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "DS-DIGIB.TTF"));

        textR.setColor(Color.MAGENTA);
        textR.setStyle(Paint.Style.FILL);
        textR.setTextSize(board.getHeight() / 6);
        textR.setTextAlign(Paint.Align.RIGHT);
        textR.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "DS-DIGIB.TTF"));

        textT.setColor(Color.BLACK);
        textT.setStyle(Paint.Style.FILL);
        textT.setTextSize(board.getHeight() / 6);
        textT.setTextAlign(Paint.Align.CENTER);
        textT.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "DS-DIGIB.TTF"));

        textM.setColor(Color.RED);
        textM.setStyle(Paint.Style.FILL);
        textM.setTextSize(board.getHeight() / 6);
        textM.setTextAlign(Paint.Align.CENTER);

        obstacle.setColor(Color.GRAY);
        obstacle.setStyle(Paint.Style.FILL);

        this.background = background;

        // Set up stuff
        sh = getHolder();
        this.board = board;
        final SimpleTagSurfaceView view = this;

        sh.addCallback(new SurfaceHolder.Callback() {

            // Called when surface is created
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                gameLoopThread = new GameLoopThread(view, board);
                gameLoopThread.setRunning(true); // Start the thread
                try {
                    gameLoopThread.start();
                } catch (IllegalThreadStateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            // Called when surface is destroyed. Stop the thread. Or something.
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }



    @Override
    public void onDraw(Canvas canvas) {

        Rect rect = new Rect(0, 0, board.getWidth(), board.getHeight()); // Rectangle that is size of board
        // Draw the background
        canvas.drawBitmap(background, null, rect, null);

        // Make a matrix, get the correct left sprite for the frame
        Matrix leftMatrix = new Matrix();
        // Resize the sprite -- MUST CHANGE RADIUS AND SPEED
        Bitmap leftBitmap = Bitmap.createScaledBitmap(getCorrectSpriteL(),
                getCorrectSpriteL().getWidth() * (board.getHeight() / 12) / getCorrectSpriteL().getHeight(), board.getHeight() / 12, false);

        // Rotate and translate left sprite
        leftMatrix.setRotate((float) board.getPlayerL().getDirection(), (float) leftBitmap.getWidth() / 2, (float) leftBitmap.getHeight() / 2);
        leftMatrix.postTranslate((float) board.getPlayerL().getX() - ((float) leftBitmap.getWidth() / 2),
                (float) board.getPlayerL().getY() - ((float) leftBitmap.getHeight() / 2));


        // Make a matrix, get the correct right sprite for the frame
        Matrix rightMatrix = new Matrix();
        // Resize the sprite -- MUST CHANGE RADIUS AND SPEED
        Bitmap rightBitmap = Bitmap.createScaledBitmap(getCorrectSpriteR(),
                getCorrectSpriteR().getWidth() * (board.getHeight() / 12) / getCorrectSpriteR().getHeight(), board.getHeight() / 12, false);

        // Rotate and translate right sprite
        rightMatrix.setRotate((float) board.getPlayerR().getDirection(), (float) rightBitmap.getWidth() / 2, (float) rightBitmap.getHeight() / 2);
        rightMatrix.postTranslate((float) board.getPlayerR().getX() - ((float) rightBitmap.getWidth() / 2),
                (float) board.getPlayerR().getY() - ((float) rightBitmap.getHeight() / 2));


        // Draw score
        canvas.drawText(Integer.toString(board.getPlayerL().getScore()), 1*board.getWidth()/6, 35*board.getHeight() / 36, textL);
        canvas.drawText(Integer.toString(board.getPlayerR().getScore()), 5*board.getWidth()/6, 35*board.getHeight() / 36, textR);

        // Draw obstacles
        List<Obstacle> obstacles = board.getObstacles();
        for (Obstacle o : obstacles){
            canvas.drawRect((float) o.getXLower(), (float) o.getYLower(),
                    (float) o.getXUpper(), (float) o.getYUpper(),obstacle);
        }

        // Draw the countdown after resetting roles
        if (board.getCurrentFrame() <= -40){
            if ((-55 <= board.getCurrentFrame() && board.getCurrentFrame()<= -51) ||
                    (-47 <= board.getCurrentFrame() && board.getCurrentFrame() <= -43)) {

                // Draw the winning sprite on top
                if ((board.getWinMethod() && board.getHunterState()) ||
                        (!board.getWinMethod() && !board.getHunterState())){
                    // Draw right sprite
                    canvas.drawBitmap(rightBitmap, rightMatrix, null);
                    // Draw left sprite
                    canvas.drawBitmap(leftBitmap, leftMatrix, null);

                } else {
                    // Draw left sprite
                    canvas.drawBitmap(leftBitmap, leftMatrix, null);
                    // Draw right sprite
                    canvas.drawBitmap(rightBitmap, rightMatrix, null);
                }

                if (board.getWinMethod()) {
                    canvas.drawText("Hunter Wins!", board.getWidth() / 2, board.getHeight() / 2, textM);
                } else {
                    canvas.drawText("Prey Escaped!", board.getWidth() / 2, board.getHeight() / 2, textM);
                }

            }
        } else {
            // Draw left sprite
            canvas.drawBitmap(leftBitmap, leftMatrix, null);
            // Draw right sprite
            canvas.drawBitmap(rightBitmap, rightMatrix, null);

             if (board.getCurrentFrame() < 0) {
                 int loadingTime = board.getCurrentFrame() / 10;
                 if (loadingTime != 0)
                     canvas.drawText(Integer.toString(-1 * loadingTime), board.getWidth() / 2, board.getHeight() / 2, textT);
                 else
                     canvas.drawText("GO!", board.getWidth() / 2, board.getHeight() / 2, textT);
             } else {
                 // Calculate time on timer based on currentFrame
                 int timeOnTimer = (board.getSwitchRoleTime() / 10) - ((board.getCurrentFrame() % board.getSwitchRoleTime()) / 10); // <-- Frames per second
                 //Draws the timer
                 canvas.drawText("Time:" + Integer.toString(timeOnTimer), board.getWidth() / 2, 35 * board.getHeight() / 36, textT);
             }
        }
    }

    public Bitmap getCorrectSpriteL(){
        Sprite sprite = board.getPlayerL();


        if (board.getCurrentFrame() <= -40) {
            // Hunter and win by collision
            if (sprite.getState() && board.getWinMethod()){
                return HspriteGreen1;
            // Hunted and win by collision
            } else if (!sprite.getState() && board.getWinMethod()){
                return deadSprite;
                // Hunter and win by times up
            } else if (sprite.getState()){
                return deadSprite;
            } else {
                return spriteGreen1;
            }
        } else if (sprite.getState()) { // getState is true if hunter
            if (sprite.getSpinning()) {
                return HspriteGreen1; // If spinning keep the same left sprite every frame
            } else {
                int currentFrame = board.getCurrentFrame() % 4; // Alternate between left sprites each from
                if (currentFrame == 0) {
                    return HspriteGreen1;
                } else if (currentFrame == 1) {
                    return HspriteGreen2;
                } else if (currentFrame == 2) {
                    return HspriteGreen1;
                } else if (currentFrame == 3) {
                    return HspriteGreen3;
                }
            }

        } else { // is hunted
            if (sprite.getSpinning()){
                return spriteGreen1;
            } else {
                if (board.getCurrentFrame() <= -40) {
                    return deadSprite;
                } else {
                    int currentFrame = board.getCurrentFrame() % 4;
                    if (currentFrame == 0) {
                        return spriteGreen1;
                    } else if (currentFrame == 1) {
                        return spriteGreen2;
                    } else if (currentFrame == 2) {
                        return spriteGreen1;
                    } else { // currentFrame == 3
                        return spriteGreen3;
                    }
                }
            }
        }
        return spriteGreen1; // return generic if in doubt
    }

    public Bitmap getCorrectSpriteR(){
        Sprite sprite = board.getPlayerR();

        if (board.getCurrentFrame() <= -40) {
            // Hunter and win by collision
            if (sprite.getState() && board.getWinMethod()){
                return HspritePurple1;
                // Hunted and win by collision
            } else if (!sprite.getState() && board.getWinMethod()){
                return deadSprite;
                // Hunter and win by times up
            } else if (sprite.getState()){
                return deadSprite;
            } else {
                return spritePurple1;
            }
        } else if (sprite.getState()){
            if (sprite.getSpinning()){
                return HspritePurple1;
            } else {
                int currentFrame = board.getCurrentFrame() % 4;
                if (currentFrame == 0) {
                    return HspritePurple1;
                } else if (currentFrame == 1) {
                    return HspritePurple2;
                } else if (currentFrame == 2) {
                    return HspritePurple1;
                } else if (currentFrame == 3) {
                    return HspritePurple3;
                }
            }
        } else {
            if (sprite.getSpinning()){
                return spritePurple1;
            } else {
                if (board.getCurrentFrame() <= -40) {
                    return deadSprite;
                } else {
                    int currentFrame = board.getCurrentFrame() % 4;
                    if (currentFrame == 0) {
                        return spritePurple1;
                    } else if (currentFrame == 1) {
                        return spritePurple2;
                    } else if (currentFrame == 2) {
                        return spritePurple1;
                    } else { // currentFrame == 3
                        return spritePurple3;
                    }
                }

            }
        }
        return spritePurple1; // if in doubt return generic sprite
    }

}
