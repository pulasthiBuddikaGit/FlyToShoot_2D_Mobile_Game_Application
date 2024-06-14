package com.example.flytoshoot

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.flytoshoot.GameView.Companion.screenRatioX
import com.example.flytoshoot.GameView.Companion.screenRatioY

class Bird(res:Resources) {

    var wasShot: Boolean = true
    var speed: Int=10
    var x:Int = 0
    var y:Int
    var width:Int = 0
    var height:Int = 0
    var bird1:Bitmap? = null
    var bird2:Bitmap? = null
    var bird3:Bitmap? = null
    var bird4:Bitmap? = null

    var birdCounter:Int = 1

    init {
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1)
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2)
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3)
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4)

        width = bird1!!.width
        height = bird1!!.height

        width /= 6
        height /= 6

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        bird1 = Bitmap.createScaledBitmap(bird1!!, width, height, false)
        bird2 = Bitmap.createScaledBitmap(bird2!!, width, height, false)
        bird3 = Bitmap.createScaledBitmap(bird3!!, width, height, false)
        bird4 = Bitmap.createScaledBitmap(bird4!!, width, height, false)

        //bird place off the screen start of the game
        y = -height
    }

    fun getBird(): Bitmap?{
        if (birdCounter==1){
            birdCounter++
            return bird1
        }
        if (birdCounter==2){
            birdCounter++
            return bird2
        }
        if (birdCounter==3){
            birdCounter++
            return bird1
        }
        if (birdCounter==4){
            birdCounter++
            return bird4
        }

        //to restart the bird animation itself
        birdCounter =1
        return bird4
    }

    //create a rectangle around the bird
    fun getCollisionShape(): Rect {
        //position of the rectangle
        return Rect(x, y, x + width, y + height)
    }
}