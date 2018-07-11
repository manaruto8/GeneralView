package com.ma.customview.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.View;

import com.ma.customview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Aeolus on 2018/5/30.
 */

public class CobwebView extends View {

    private Paint linePaint;
    private int width;
    private int height;
    private Path path;
    private int side;
    private double initAngle;
    private int lineColor;
    private float lineStrokeWidth;
    private float dataLineStrokeWidth;
    private int circleCount;
    private int max;
    private double avAngle;
    private int r;
    private List<PointXY> pointList;
    private List<PointXY> dataList;
    private List<List<PointXY>> circlrList=new ArrayList<>();
    private List<String> textList=new ArrayList<>();
    private List<String> colorList=new ArrayList<>();
    private List<List<Integer>> dateList=new ArrayList<>();
    private List<List<PointXY>> dataPointList=new ArrayList<>();




    public CobwebView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CobwebView);
        side=typedArray.getInteger(R.styleable.CobwebView_sideCount,5);
        initAngle=typedArray.getInteger(R.styleable.CobwebView_initAngle,180/side);
        lineColor=typedArray.getColor(R.styleable.CobwebView_backgroundLineColor,Color.GRAY);
        lineStrokeWidth=typedArray.getFloat(R.styleable.CobwebView_backgroundLineStrokeWidth,1);
        circleCount=typedArray.getInteger(R.styleable.CobwebView_circleCount,side);
        max=typedArray.getInteger(R.styleable.CobwebView_max,100);
        dataLineStrokeWidth=typedArray.getFloat(R.styleable.CobwebView_dataLineStrokeWidth,3);

        linePaint=new Paint();
        linePaint.setStrokeWidth(lineStrokeWidth);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(int j=0;j<circlrList.size();j++){
                    circlrList.get(j).clear();
                }
                circlrList.clear();
                side++;
                circleCount++;
                postInvalidate();
            }
        },1000,2000);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width=getWidth();
        height=getHeight();
        r=Math.min(width,height)/2;
        avAngle=2*Math.PI/side;
        path=new Path();
        for(int j=1;j<=circleCount;j++){
            pointList=new ArrayList<>();
            for (int i=0;i<side;i++){
                double angle = initAngle * Math.PI / 180 + avAngle * i;
                float pointX = (float) (r*j/circleCount * Math.sin(angle))+width/2;
                float pointY = (float) (r*j/circleCount * Math.cos(angle))+height/2;
                PointXY pointXY=new PointXY(pointX,pointY);
                pointList.add(pointXY);
            }
            circlrList.add(pointList);
        }

        for(int j=0;j<circlrList.size();j++){
            path.moveTo(circlrList.get(j).get(0).getX(),circlrList.get(j).get(0).getY());
            for (int i=0;i<circlrList.get(j).size();i++){
                path.lineTo(circlrList.get(j).get(i).getX(),circlrList.get(j).get(i).getY());
                canvas.drawLine(width/2,height/2,circlrList.get(j).get(i).getX(),circlrList.get(j).get(i).getY(),linePaint);
            }
            path.lineTo(circlrList.get(j).get(0).getX(),circlrList.get(j).get(0).getY());
            canvas.drawPath(path,linePaint);
        }

        for(int j=0;j<dateList.size();j++) {
            dataList=new ArrayList<>();
            for (int i = 0; i < dateList.get(j).size(); i++) {
                double angle = initAngle * Math.PI / 180 + avAngle * i;
                float pointX = (float) (r * dateList.get(j).get(i) / max * Math.sin(angle)) + width / 2;
                float pointY = (float) (r * dateList.get(j).get(i) / max * Math.cos(angle)) + height / 2;
                PointXY pointXY = new PointXY(pointX, pointY);
                dataList.add(pointXY);
            }
            dataPointList.add(dataList);
        }

        for (int j=0;j<dataPointList.size();j++){
            Paint dataPaint=new Paint();
            Path dataPath=new Path();
            dataPaint.setStrokeWidth(lineStrokeWidth);
            dataPaint.setStyle(Paint.Style.FILL);
            dataPaint.setColor(Color.parseColor(colorList.get(j)));
            dataPaint.setAlpha(150);
            dataPath.moveTo(dataPointList.get(j).get(0).getX(),dataPointList.get(j).get(0).getY());
            for(int i=0;i<dataPointList.get(j).size();i++){
                dataPath.lineTo(dataPointList.get(j).get(i).getX(),dataPointList.get(j).get(i).getY());
            }
            dataPath.lineTo(dataPointList.get(j).get(0).getX(),dataPointList.get(j).get(0).getY());
            canvas.drawPath(dataPath,dataPaint);
        }
    }

    public void setCobwebText(List<String> textList){
        this.textList=textList;
    }

    public void setCobwebData(String color ,List<Integer> dateList){
        this.colorList.add(color);
        this.dateList.add(dateList);
        postInvalidate();
    }


    private class PointXY{
        private float x;
        private float y;

        public PointXY(float x, float y) {
            this.x = x;
            this.y = y;
        }


        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }


    }
}
