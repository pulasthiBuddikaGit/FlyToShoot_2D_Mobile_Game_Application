package com.example.flytoshoot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

//4)give a starting point for gameActivity
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //activity initalization
        setContentView(R.layout.activity_main)

        //start the game when 'play' text view clicked
        findViewById<View>(R.id.play).setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,  //MainActivity eken GameActivity ekata ywnwa
                    GameActivity::class.java         //request an action from GameActivity(Ex)
                )
            )
        }
        //find the id to store highest score
        val highScoreTxt = findViewById<TextView>(R.id.highScoreTxt)
        //initialize the SharedPreferences
        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        //Retrieve highest score from SharedPreferences
        highScoreTxt.text = "HighScore: " + prefs.getInt("highscore", 0)

    }
}