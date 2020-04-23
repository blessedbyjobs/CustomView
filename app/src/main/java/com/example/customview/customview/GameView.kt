package com.example.customview.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.customview.R
import com.example.customview.customview.figures.*
import kotlin.random.Random


class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var densityScale: Float = 0.0f

    private val figuresList = arrayListOf<Figure>()
    private var colorsList = arrayListOf<Int>()

    private lateinit var figuresCounter: TextView

    init {
        initViews()

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GameView)

        initPaint(attributes.getColor(R.styleable.GameView_defaultColor, Color.GREEN))
    }

    private fun initViews() {
        View.inflate(context, R.layout.layout_game, this)
        figuresCounter = findViewById(R.id.figures_counter_tv)

        densityScale = context.resources.displayMetrics.density

        setWillNotDraw(false)
    }

    private fun initPaint(color: Int) {
        paint.color = color
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val figureType = Random.nextInt(0, 3)
        val size = Random.nextInt(20, 50).toFloat()
        val color = if (colorsList.isEmpty()) paint.color else colorsList.random()
        val newPaint = Paint()
        newPaint.color = color

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (figureType) {
                    0 -> {
                        val circle =
                            Circle(event.x, event.y, newPaint, getPixelsFromDp(size))
                        figuresList.add(circle)
                    }
                    1 -> {
                        val square =
                            Square(event.x, event.y, newPaint, getPixelsFromDp(size))
                        figuresList.add(square)
                    }
                    2 -> {
                        val roundedSquare =
                            RoundedSquare(
                                RectF(
                                    event.x - getPixelsFromDp(size),
                                    event.y + getPixelsFromDp(size),
                                    event.x + getPixelsFromDp(size),
                                    event.y - getPixelsFromDp(size)
                                ), newPaint, 25F
                            )
                        figuresList.add(roundedSquare)
                    }
                }

                checkForEnding()
            }
        }

        invalidate()
        return true
    }

    /**
     * Метод, через который можно передать список цветов для отрисовки фигур
     */
    fun setColors(colors: ArrayList<Int>) {
        colorsList = colors
    }

    /**
     * Проверка, что игра может быть закончена
     */
    private fun checkForEnding() {
        if (figuresList.size == 10) {
            figuresList.clear()
            invalidate()
            Toast.makeText(context, "Game over!", Toast.LENGTH_SHORT).show()
        }

        figuresCounter.text = figuresList.size.toString()
    }

    /**
     * Метод, переводящий dp в пиксели для отрисовки
     */
    private fun getPixelsFromDp(dp: Float) = dp * densityScale

    override fun onDraw(canvas: Canvas) {
        figuresList.forEach {
            when (it) {
                is Circle -> {
                    canvas.drawCircle(it.x, it.y, it.radius, it.color)
                }
                is Square -> canvas.drawRect(it.x - it.size, it.y + it.size, it.x + it.size, it.y - it.size, it.color)
                is RoundedSquare -> canvas.drawRoundRect(it.square, it.cornerRadius, it.cornerRadius, it.color)
            }
        }
    }
}