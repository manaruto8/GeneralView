package com.ma.generalview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.ma.generalview.R
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Created by Aeolus on 2018/5/30.
 */
class RadarChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var linePaint: Paint? = null
    private var viewWidth = 0
    private var viewHeight = 0
    private var path: Path? = null
    private var side = 0
    private var initAngle = 0.0
    private var lineColor = 0
    private var lineStrokeWidth = 0f
    private var dataLineStrokeWidth = 0f
    private var circleCount = 0
    private var max = 0
    private var avAngle = 0.0
    private var r = 0
    private var pointList: MutableList<PointXY> = ArrayList()
    private var dataList: MutableList<PointXY> = ArrayList()
    private val circleList: MutableList<List<PointXY>> = ArrayList()
    private var textList: List<String> = ArrayList()
    private val colorList: MutableList<String> = ArrayList()
    private val dateList: MutableList<List<Int>> = ArrayList()
    private val dataPointList: MutableList<List<PointXY>> = ArrayList()
    private fun initView(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarChartView)
        side = typedArray.getInteger(R.styleable.RadarChartView_sideCount, 5)
        initAngle = typedArray.getInteger(R.styleable.RadarChartView_initAngle, 180 / side).toDouble()
        lineColor = typedArray.getColor(R.styleable.RadarChartView_backgroundLineColor, Color.GRAY)
        lineStrokeWidth = typedArray.getFloat(R.styleable.RadarChartView_backgroundLineStrokeWidth, 1f)
        circleCount = typedArray.getInteger(R.styleable.RadarChartView_circleCount, side)
        max = typedArray.getInteger(R.styleable.RadarChartView_max, 100)
        dataLineStrokeWidth = typedArray.getFloat(R.styleable.RadarChartView_dataLineStrokeWidth, 3f)
        linePaint = Paint()
        linePaint!!.strokeWidth = lineStrokeWidth
        linePaint!!.color = lineColor
        linePaint!!.style = Paint.Style.STROKE


//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                for(int j=0;j<circlrList.size();j++){
//                    circlrList.get(j).clear();
//                }
//                circlrList.clear();
//                side++;
//                circleCount++;
//                postInvalidate();
//            }
//        },1000,2000);
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = width
        viewHeight = height
        r = min(viewWidth, viewHeight) / 2
        avAngle = 2 * Math.PI / side
        path = Path()
        for (j in 1..circleCount) {
            pointList = ArrayList()
            for (i in 0 until side) {
                val angle = initAngle * Math.PI / 180 + avAngle * i
                val pointX = (r * j / circleCount * sin(angle)).toFloat() + viewWidth / 2
                val pointY = (r * j / circleCount * cos(angle)).toFloat() + viewHeight / 2
                val pointXY = PointXY(pointX, pointY)
                pointList.add(pointXY)
            }
            circleList.add(pointList)
        }
        for (j in circleList.indices) {
            path!!.moveTo(circleList[j][0].x, circleList[j][0].y)
            for (i in circleList[j].indices) {
                path!!.lineTo(circleList[j][i].x, circleList[j][i].y)
                canvas.drawLine(viewWidth / 2.toFloat(), viewHeight / 2.toFloat(), circleList[j][i].x, circleList[j][i].y, linePaint!!)
            }
            path!!.lineTo(circleList[j][0].x, circleList[j][0].y)
            canvas.drawPath(path!!, linePaint!!)
        }
        for (j in dateList.indices) {
            dataList = ArrayList()
            for (i in dateList[j].indices) {
                val angle = initAngle * Math.PI / 180 + avAngle * i
                val pointX = (r * dateList[j][i] / max * sin(angle)).toFloat() + viewWidth / 2
                val pointY = (r * dateList[j][i] / max * cos(angle)).toFloat() + viewHeight / 2
                val pointXY = PointXY(pointX, pointY)
                dataList.add(pointXY)
            }
            dataPointList.add(dataList)
        }
        for (j in dataPointList.indices) {
            val dataPaint = Paint()
            val dataPath = Path()
            dataPaint.strokeWidth = lineStrokeWidth
            dataPaint.style = Paint.Style.FILL
            dataPaint.color = Color.parseColor(colorList[j])
            dataPaint.alpha = 150
            dataPath.moveTo(dataPointList[j][0].x, dataPointList[j][0].y)
            for (i in dataPointList[j].indices) {
                dataPath.lineTo(dataPointList[j][i].x, dataPointList[j][i].y)
            }
            dataPath.lineTo(dataPointList[j][0].x, dataPointList[j][0].y)
            canvas.drawPath(dataPath, dataPaint)
        }
    }

    fun setRadarChartText(textList: List<String>) {
        this.textList = textList
    }

    fun setRadarChartData(color: String, dateList: List<Int>) {
        colorList.add(color)
        this.dateList.add(dateList)
        postInvalidate()
    }

    private inner class PointXY(var x: Float, var y: Float)

    init {
        initView(context, attrs)
    }
}