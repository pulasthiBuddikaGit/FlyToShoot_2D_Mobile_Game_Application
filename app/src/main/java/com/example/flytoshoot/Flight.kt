package com.example.flytoshoot


import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.flytoshoot.GameView.Companion.screenRatioX
import com.example.flytoshoot.GameView.Companion.screenRatioY

class Flight(gameView: GameView,screenY: Int, res: Resources) {
    var toShoot: Int = 0
    var isGoingUp: Boolean = false
    var x: Int = 0
    var y: Int = 0
    var width: Int = 0
    var height: Int = 0
    var flight1: Bitmap? = null
    var flight2: Bitmap? = null
    var shoot1: Bitmap? = null
    var shoot2: Bitmap? = null
    var shoot3: Bitmap? = null
    var shoot4: Bitmap? = null
    var shoot5: Bitmap? = null
    var dead: Bitmap? = null

    var wingCounter:Int=0
    var shootCounter:Int= 0
    private lateinit var gameView: GameView

    init {

        this.gameView = gameView;
        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1)
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2)

        width = flight1!!.width
        height = flight1!!.height

        width /= 4
        height /= 4

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        flight1 = Bitmap.createScaledBitmap(flight1!!, width, height, false)
        flight2 = Bitmap.createScaledBitmap(flight2!!, width, height, false)

        //refer images
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2)
        shoot3= BitmapFactory.decodeResource(res,R.drawable.shoot3)
        shoot4= BitmapFactory.decodeResource(res,R.drawable.shoot4)
        shoot5= BitmapFactory.decodeResource(res,R.drawable.shoot5)

        //resize the bitmap
        shoot1 = Bitmap.createScaledBitmap(shoot1!!, width, height, false)
        shoot2 = Bitmap.createScaledBitmap(shoot2!!, width, height, false)
        shoot3 = Bitmap.createScaledBitmap(shoot3!!, width, height, false)
        shoot4 = Bitmap.createScaledBitmap(shoot4!!, width, height, false)
        shoot5 = Bitmap.createScaledBitmap(shoot5!!, width, height, false)

        dead = BitmapFactory.decodeResource(res, R.drawable.dead)
        dead = Bitmap.createScaledBitmap(dead!!,width,height,false)

        y = screenY / 2
        x = (64 * screenRatioX).toInt()
    }

    fun getFlight(): Bitmap? {
        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter++
                return shoot1
            }
            if (shootCounter == 2) {
                shootCounter++
                return shoot2
            }
            if (shootCounter == 3) {
                shootCounter++
                return shoot3
            }
            if (shootCounter == 4) {
                shootCounter++
                return shoot4
            }
            shootCounter = 1
            toShoot--
            gameView.newBullet()
            return shoot5
        }

        if (wingCounter === 0) {
            wingCounter++
            return flight1
        }
        wingCounter--
        return flight2
    }
    //create a rectangle around the flight
    fun getCollisionShape(): Rect {
        //position of the rectangle
        return Rect(x, y, x + width, y + height)
    }

    //Since Kotlin automatically provides a getter for the dead property we don't need getDead()
//    fun getDead(): Bitmap? {
//        return dead
//    }

}


