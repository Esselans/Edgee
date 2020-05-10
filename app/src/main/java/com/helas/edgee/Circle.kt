package com.helas.edgee

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.os.BatteryManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat


/**
 * TODO: document your custom view class.
 */
class Circle : View {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.Circle, defStyle, 0
        )

        a.recycle()

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        //canvas?.drawCircle(100f, 20f, 70f, paint)
var bat = this.getBatteryPercentage(getContext())


       bat = ((bat * 360) / 100)

        Log.i("EE-LEVEL",  "$bat%")

        val width = width.toFloat()
        val height = height.toFloat()
        val radius: Float

        radius = if (width > height) {
            height / 4
        } else {
            width / 4
        }

        val path = Path()
        path.addCircle(
            width / 2,
            height / 2, radius,
            Path.Direction.CW
        )

        val paint = Paint()
        paint.color = Color.parseColor("#eb4034")
        paint.strokeWidth = 9f
        paint.style = Paint.Style.STROKE

        val center_x: Float
        val center_y: Float
        val oval = RectF()
        paint.style = Paint.Style.STROKE

        center_x = (width / 2) - 23
        center_y = (height / 2) - 36

        oval[center_x - radius, center_y - radius, center_x + radius] = center_y + radius

          /*val colors = intArrayOf(
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step1),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0),
              ContextCompat.getColor(context, R.color.progress_color_step0)
          )

          var positions = floatArrayOf(0.0f, 0.1f, 0.2f, 0.3f, 0.35f, 0.45f, 0.50f, 0.75f, 0.9f, 0.9f, 1.0f)*/

        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.progress_color_step4),
            ContextCompat.getColor(context, R.color.progress_color_step5),
            ContextCompat.getColor(context, R.color.progress_color_step3),
            ContextCompat.getColor(context, R.color.progress_color_step6),
            ContextCompat.getColor(context, R.color.progress_color_step2)
        )

        var positions = floatArrayOf(0.1f, 0.30f, 0.55f, 0.70f, 1.00f)

          var sweepGradient : SweepGradient? = null

        sweepGradient = SweepGradient((width / 2) - 23,(height / 2) - 36, colors,  positions)

        paint.shader = sweepGradient


        canvas.drawArc(oval, 180f, bat.toFloat(), false  , paint)
    }

    fun getBatteryPercentage(context: Context): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, iFilter)
        val level = batteryStatus?.getIntExtra(
            BatteryManager.EXTRA_LEVEL,
            -1
        ) ?: -1
        val scale = batteryStatus?.getIntExtra(
            BatteryManager.EXTRA_SCALE,
            -1
        ) ?: -1
        val batteryPct = level / scale.toFloat()
        return (batteryPct * 100).toInt()
    }
}
