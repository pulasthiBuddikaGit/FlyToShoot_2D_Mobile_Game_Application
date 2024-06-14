package com.example.flytoshoot

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

import com.example.flytoshoot.GameView.Companion.screenRatioX
import com.example.flytoshoot.GameView.Companion.screenRatioY

class Bullet(res: Resources) {

    var x:Int = 0
    var y:Int = 0
    var width: Int
    var height: Int

    var bullet:Bitmap? = null

    init {
        bullet = BitmapFactory.decodeResource(res,R.drawable.bullet)

        //bullet is too big so
        width = bullet!!.width
        height = bullet!!.height

        width /=4
        height/=4

        //make bullet compatible
        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        bullet = Bitmap.createScaledBitmap(bullet!!, width, height, false)
    }

    //create a rectangle around the bird
    fun getCollisionShape(): Rect {
        //position of the rectangle
        return Rect(x, y, x + width, y + height)
    }
}