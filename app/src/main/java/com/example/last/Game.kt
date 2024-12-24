package com.example.last

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Motion
import kotlin.random.Random

lateinit var balls: Array<Ball>
lateinit var hole: MyRect
var h = 1000; var w = 1000
var colorsArr = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN)
val paint = Paint()

open class Game(ctx: Context): View(ctx){
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        for (ball in balls){
            paint.color = ball.color
            canvas.drawCircle(ball.x,ball.y,ball.r, paint)
        }
        paint.color = hole.color
        canvas.drawRect(hole.x,hole.y,hole.width, hole.height, paint)
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val r = Random(233)
        h = bottom - top
        w = right - left
        r.nextFloat()
        balls = Array(5){Ball(w*r.nextFloat()+75,(h/2)*r.nextFloat()+75, 75f, colorsArr[r.nextInt(0,
            colorsArr.size)])}
        hole = MyRect(0f, h*0.75f, w/1f, h/1f, balls[r.nextInt(0,balls.size)].color)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action){
            MotionEvent.ACTION_DOWN ->{
                Move(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE ->{
                Move(event.x, event.y)
            }
            MotionEvent.ACTION_UP ->{
                Check(event.x, event.y)
            }
        }

        return true

    }
    fun Move(x: Float, y: Float){
        for (ball in balls){
            if (x in (ball.x-ball.r..ball.r+ball.x) && y in (ball.y-ball.r..ball.r+ball.y)){
                if (ball.color != Color.TRANSPARENT) {
                    ball.x = x
                    ball.y = y
                    break
                }
            }
        }
        invalidate()
    }
    fun Check(x: Float, y: Float){
        for (ball in balls) {
            if (ball.x in hole.x..hole.width && ball.y in hole.y..hole.height && hole.color == ball.color) {
                ball.color = Color.TRANSPARENT
                var r = Random(0)
                var counter = 0
                for (ball in balls){
                    if (ball.color == Color.TRANSPARENT) {
                        counter++
                    }
                }
                if (counter < balls.size){
                    hole.color = balls[r.nextInt(0, balls.size)].color
                    while (hole.color == Color.TRANSPARENT) {
                        hole.color = balls[r.nextInt(0, balls.size)].color
                    }
                    break
                }
                else{
                    Toast.makeText(context, "Победа", Toast.LENGTH_SHORT).show()
                }
            }
        }
        invalidate()
    }
}
data class Ball(var x: Float, var y: Float, var r: Float, var color: Int)
data class MyRect(var x: Float, var y: Float, var width: Float, var height: Float, var color: Int)
