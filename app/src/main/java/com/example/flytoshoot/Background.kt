package com.example.flytoshoot

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
//1)
                                        //take the size of the screen x axes and y exes
                                        //take the object of Resource(use to decode the bitmap from drawable)
class Background internal constructor(screenX: Int, screenY: Int, res: Resources) {
    var background: Bitmap
    var x = 0
    var y = 0

    //constructor
    init {
        background = BitmapFactory.decodeResource(res, R.drawable.background)
        //resize the bitmap
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}
