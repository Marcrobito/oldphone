package dev.eighteentech.oldphone.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import dev.eighteentech.oldphone.R
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.roundToInt
import kotlin.math.sqrt


class DialerView(context: Context, attrs: AttributeSet?, defStyle: Int) : View(context, attrs, defStyle) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val dialerDrawable = context.resources.getDrawable(R.drawable.dialer, null)
    private var rotorAngle = 0f
    private val r1 = 50
    private val r2 = 160
    private var lastFi  = 0f

    private var listener : DialListener? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val availableWidth = right - left
        val availableHeight = bottom - top
        val x = availableWidth / 2
        val y = availableHeight / 2

        canvas.save()
        dialerDrawable.setBounds(
            0, 0, dialerDrawable.intrinsicWidth,
            dialerDrawable.intrinsicHeight
        )

        if (rotorAngle != 0f) {
            canvas.rotate(rotorAngle, x.toFloat(), y.toFloat())
        }

        dialerDrawable.draw(canvas)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x0 = (width / 2).toFloat()
        val y0 = (height / 2).toFloat()
        val x1 = event!!.x
        val y1 = event.y
        val x = x0 - x1
        val y = y0 - y1
        val r = sqrt((x * x + y * y).toDouble())
        val sinFi = y / r
        var fi = Math.toDegrees(asin(sinFi)).toFloat()

        when{
            x1 > x0 && y0 > y1 -> fi = 180 - fi
            x1 > x0 && y1 > y0 -> fi = 180 - fi
            x1 > x0 && y0 > y1 -> fi += 360
        }

        when(event.action){
            MotionEvent.ACTION_MOVE -> {
                if (r > r1 && r < r2) {
                    rotorAngle += abs(fi - lastFi) + 0.25f
                    rotorAngle %= 360;
                    lastFi = fi;
                    invalidate();
                    return true;
                }
            }
            MotionEvent.ACTION_DOWN -> {
                rotorAngle = 0f
                lastFi = fi
                return true
            }
            MotionEvent.ACTION_UP -> {
                val angle = rotorAngle % 360
                var number = (angle - 20).roundToInt() / 30

                if (number > 0) {
                    if (number == 10) {
                        number = 0
                    }
                    fireDialListenerEvent(number)
                }

                rotorAngle = 0f

                post {
                    val anim: Animation = RotateAnimation(
                        angle,
                        0f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    )
                    anim.interpolator = AnimationUtils.loadInterpolator(
                        context,
                        android.R.anim.decelerate_interpolator
                    )
                    anim.duration = (angle * 5L).toLong()
                    startAnimation(anim)
                }

                return true
            }
        }


        return super.onTouchEvent(event)
    }

    private fun fireDialListenerEvent(number: Int) {
        listener?.apply {
            onDial(number)
        }
    }

    fun setListener(listener: DialListener){
        this.listener = listener
    }

    fun removeListener(){
        this.listener = null
    }

}
interface DialListener {
    fun onDial(number: Int)
}