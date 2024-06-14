package com.example.flytoshoot

import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

//3) display gameview on the screen
class GameActivity : AppCompatActivity() {

    //create a global object from GameView class
    private var gameView: GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //make the activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        //point contains the size of the screen
  //        Initialization of gameView object
                                        //pass the size of the screen of x and y(point) as arguments to gameView
        gameView = GameView(this, point.x, point.y)
        //setContentView shows gameView on the screen
        setContentView(gameView)
    }

    override fun onPause() {
        super.onPause()
        //gameView object eke pause method ekata call krnwa
        gameView!!.pause()
    }

    override fun onResume() {
        super.onResume()
        //gameView object eke resume method ekata call krnwa
        gameView!!.resume()
    }


}
