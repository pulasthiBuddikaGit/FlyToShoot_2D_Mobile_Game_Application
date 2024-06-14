package com.example.flytoshoot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.random.Random
//import java.util.Random

//2)
//surfaceView class use to change content of the screen very quickly
//accept the size of the screen
class GameView(activity: GameActivity, screenX: Int, screenY: Int) : SurfaceView(
    activity
),
    Runnable {
    //create a object of thread class
    private var thread: Thread? = null

    private var isPlaying = false
    private var isGameOver = false
    private var screenX: Int
    private var screenY: Int

    //below variables to make the game compatiable for all the screen sizes
    //companion object is similar to static method
    //you don't need object of class to access companion object properties.you can directly call them using classname
    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
    }

    private var score:Int = 0
    private var paint: Paint
    private var activity: GameActivity
    private var birds: Array<Bird?>
    //create a object from SharedPreferences
    private var prefs: SharedPreferences
    private var random: Random

    //create a List from Bullet class
    private var bullets: MutableList<Bullet>
    //private var sound: Int = 0

    //create a object from Flight class
    private var flight: Flight
//    private var bird: Bird

    //below Background objects help to move screen
    private var background1: Background
    private var background2: Background

    //init block get executed whenever an instance of this class is created(constructor)
    init {
        this.activity = activity
        //initialize SharedPreferences
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE)


        this.screenX = screenX
        this.screenY = screenY

        //initialize screenRatioX,Y(with my phone resolutions)
        screenRatioX = 1560f / screenX
        screenRatioY = 720f / screenY

        //initiate values for 2 background objects
        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        flight = Flight(this,screenY, resources)
//        bird = Bird(resources)

        bullets = ArrayList()

        //modify the x value of the background to screen X
        background2.x = screenX

        paint = Paint()
        paint.textSize = 120f
        paint.color = Color.WHITE

        paint.textSize = 128f
        paint.color = Color.WHITE

        birds = Array(4){ Bird(resources) } //take bird resources into array

        random = Random
    }

    override fun run() {
        //run until user is playing
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private fun update() {

        //everytime this update method called our background will move by 10px on the x axis towards the left
        //caluclations done by only x axes bcz we are moving the bg only to x axes
        background1.x -= (10 * screenRatioX).toInt()
        background2.x -= (10 * screenRatioX).toInt()
        //if bg is off the screen(current bg position(ex:-150px) ekata bg width(+100px) eka ekathu krla balanwa lesser than 0 d kiyala)
        if (background1.x + background1.background.width< 0) {
            //place the bg just after the screen ends
            background1.x = screenX
        }
        if (background2.x + background2.background.width< 0) {
            background2.x = screenX
        }

        //if flight is goingUp reduce eka flight y by 30px
        if (flight.isGoingUp){
            flight.y -= (30 * screenRatioY).toInt()
        } else{
            flight.y += (30 * screenRatioY).toInt()
        }
        //from following code,flight won't go off the screen from top
        if (flight.y < 0) {
            flight.y = 0
        }
        //from following code,flight won't go off the screen from bottom
        if (flight.y > screenY - flight.height) {
            flight.y = screenY - flight.height
        }

        val trash= mutableListOf<Bullet>()
        for (bullet in bullets) {
            //off the screen giya bullets tika trash list ekata danawa
            if (bullet.x > screenX) {
                trash.add(bullet)
            }
            bullet.x += (50 * screenRatioX).toInt()

            for (bird in birds) {
                //bullet and bird collision
                if (Rect.intersects(
                        //ensure its not null using !!
                        bird!!.getCollisionShape(),
                        bullet.getCollisionShape()
                    )
                )
                    {
                    score=score+1
                    bird.x = -500
                    bullet.x = screenX + 500
                    bird.wasShot = true
                }
            }
        }

        for (bullet in trash) {
            bullets.remove(bullet)
        }

        for (bird in birds) {
            bird!!.x -= bird!!.speed
            //if bird is off the screen he come up with different speed next time
            if (bird.x + bird.width < 0) {

                val bound = (10 * screenRatioX).toInt()
                bird.speed = random.nextInt(bound)
                //random eken speed eka 5 ta wada adu karanna denne na
                if (bird.speed < 5 * screenRatioX)
                    bird.speed = (5 * screenRatioX).toInt()
                //replace the bird
                bird.x = screenX
                bird.y = random.nextInt(screenY - bird.height)

                bird.wasShot = false
            }
            //if the bird hit the flight
            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true
                return
            }

        }

    }

    private fun draw() {
        //check if the surfaceview object has successfully initiated
        if (holder.surface.isValid) {
            //surfaceView object has canvas which we can draw(bg1 and bg2)
            //return the current canvas
            val canvas = holder.lockCanvas()
            //1st parameter-bitmap of bg1/ 2-source rectangle/ 3- (rectangle coordinates,screen width and height)/ 4-object of paint class
            canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
            canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

            //draw the flight after bg bcz if do it reverse flight will be inside the bg
            flight.getFlight()
                ?.let { canvas.drawBitmap(it, flight.x.toFloat(), flight.y.toFloat(), paint) }

            //canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)
            canvas.drawText(score.toString(), (screenX / 2).toFloat(), 164f, paint)

            if (isGameOver) {
                isPlaying = false
                flight.dead
                    ?.let { canvas.drawBitmap(it,flight.x.toFloat(),flight.y.toFloat(),paint) }
                holder.unlockCanvasAndPost(canvas)
                saveIfHighScore()
                waitBeforeExiting()
                return
            }
            //draw birds
            for (bird in birds) {
                //            for (bird in birds) canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint)
                bird!!.getBird()
                    ?.let { canvas.drawBitmap(it, bird.x.toFloat(), bird.y.toFloat(), paint) }
            }



            for (bullet in bullets) {
                //canvas.drawBitmap(bullet.bullet, bullet.x.toFloat(), bullet.y.toFloat(), paint)
                bullet.bullet?.let { canvas.drawBitmap(it, bullet.x.toFloat(), bullet.y.toFloat(), paint) }
            }

            //show the canvas on the screen
            holder.unlockCanvasAndPost(canvas)
        }
    }
    //update the highest score
    private fun saveIfHighScore() {
        //retreive the current highestcore in the sharedPreferences using getInt  and check
        if (prefs.getInt("highscore", 0) < score) {
            val editor = prefs.edit()
            //putInt store integer
            editor.putInt("highscore", score)
            editor.apply()
        }
    }

    //after game over return to mainactivity(Ex)
    private fun waitBeforeExiting() {
        try {
            Thread.sleep(3000)
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    //for every one second we update the position of the images
    //we draw it on screnn 60 times
    private fun sleep() {
        try {
            Thread.sleep(17)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    //game resume function
    fun resume() {
        isPlaying = true
        //initilaize the thread object
        thread = Thread(this)
        thread!!.start()
    }
    //game pause function
    fun pause() {
        try {
            isPlaying = false
            //terminate the thread
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                //screen eke leftside eka touch karama uda ynwa
                if (event.x < screenX / 2) {
                //isGoingUp property eka ganne Flight class eken
                flight.isGoingUp = true
            }

            MotionEvent.ACTION_UP -> {
                flight.isGoingUp = false
                if (event.x > screenX / 2) {
                    flight.toShoot++
                }
            }
        }
        return true
    }

    fun newBullet() {

        //create bullet object
        val bullet = Bullet(resources)
        //set bullets to be near the wings of flight
        bullet.x = flight.x + flight.width
        bullet.y = flight.y + (flight.height / 2)
        //add it into the List
        bullets.add(bullet)
    }

}

