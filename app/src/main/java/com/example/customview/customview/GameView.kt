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
import com.example.customview.customview.figures.FigureType.*
import kotlin.random.Random

/**
 * Вью игры
 */
class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var figuresCounter: TextView

    private val paint = Paint()
    private val figuresList = mutableListOf<Figure>()

    private var densityScale: Float = 0.0f
    private var colorsList = arrayListOf<Int>()

    init {
        initViews()

        with(context.obtainStyledAttributes(attrs, R.styleable.GameView)) {
            initPaint(this.getColor(R.styleable.GameView_defaultColor, Color.GREEN))
        }
    }

    override fun onDraw(canvas: Canvas) {
        figuresList.forEach { figure ->
            when (figure) {
                is Circle -> {
                    canvas.drawCircle(
                        figure.x,
                        figure.y,
                        figure.radius,
                        figure.color
                    )
                }
                is Square -> canvas.drawRect(
                    figure.x - figure.size,
                    figure.y + figure.size,
                    figure.x + figure.size,
                    figure.y - figure.size,
                    figure.color
                )
                is RoundedSquare -> canvas.drawRoundRect(
                    figure.square,
                    figure.cornerRadius,
                    figure.cornerRadius,
                    figure.color
                )
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val figureType = getRandomFigureType()
        val size = getRandomSize()
        val color = getPaintForColor(getRandomColor())

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val figure = when (figureType) {
                    CIRCLE -> {
                        Circle(
                            x = event.x,
                            y = event.y,
                            color = color,
                            radius = size.toPx()
                        )
                    }
                    SQUARE -> {
                        Square(
                            x = event.x,
                            y = event.y,
                            color = color,
                            size = size.toPx()
                        )
                    }
                    ROUNDED_SQUARE -> {
                        RoundedSquare(
                            square = RectF(
                                event.x - size.toPx(),
                                event.y + size.toPx(),
                                event.x + size.toPx(),
                                event.y - size.toPx()
                            ),
                            color = color,
                            cornerRadius = 25F
                        )
                    }
                    UNKNOWN -> {
                        null
                    }
                }

                figure?.let { figuresList.add(it) }

                tryToFinishGame()
            }
        }

        invalidate()
        return true
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

    /**
     * Метод, через который можно передать список цветов для отрисовки фигур
     */
    fun setColors(colors: ArrayList<Int>) {
        colorsList = colors
    }

    /**
     * Попытка закончить игру
     */
    private fun tryToFinishGame() {
        if (figuresList.size == 10) {
            figuresList.clear()
            invalidate()
            Toast.makeText(context, "Game over!", Toast.LENGTH_SHORT).show()
        }

        figuresCounter.text = figuresList.size.toString()
    }

    /**
     * Получить случайный тип фигуры
     */
    private fun getRandomFigureType(): FigureType {
        return FigureType.getByValue(Random.nextInt(0, 3))
    }

    /**
     * Получить случайный размер
     */
    private fun getRandomSize(): Float {
        return Random.nextInt(20, 50).toFloat()
    }

    /**
     * Получить случайный цвет
     */
    private fun getRandomColor(): Int {
        return if (colorsList.isEmpty()) {
            paint.color
        } else {
            colorsList.random()
        }
    }

    /**
     * Получить [Paint] с выбранным [Color]
     */
    private fun getPaintForColor(color: Int): Paint {
        return Paint().apply {
            setColor(color)
        }
    }

    /**
     * Extension, переводящий dp в пиксели для отрисовки
     */
    private fun Float.toPx() = this * densityScale
}