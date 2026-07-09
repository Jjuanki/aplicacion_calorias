package com.juanc.aplicacion_calorias;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {

    private List<Integer> data = new ArrayList<>();
    private int goal = 2000;
    private Paint barPaint;
    private Paint goalPaint;
    private Paint textPaint;

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.parseColor("#1E90FF"));
        barPaint.setStyle(Paint.Style.FILL);

        goalPaint = new Paint();
        goalPaint.setColor(Color.RED);
        goalPaint.setStrokeWidth(4);
        goalPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Integer> data, int goal) {
        this.data = data;
        this.goal = goal;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null || data.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int padding = 50;

        int maxVal = goal;
        for (int val : data) {
            if (val > maxVal) maxVal = val;
        }
        maxVal = (int) (maxVal * 1.2); // Give some head room

        float barWidth = (float) (width - 2 * padding) / data.size();
        float scale = (float) (height - 2 * padding) / maxVal;

        // Draw Goal Line
        float goalY = height - padding - (goal * scale);
        canvas.drawLine(padding, goalY, width - padding, goalY, goalPaint);
        canvas.drawText("Goal", padding, goalY - 10, textPaint);

        for (int i = 0; i < data.size(); i++) {
            float left = padding + i * barWidth + 10;
            float right = padding + (i + 1) * barWidth - 10;
            float top = height - padding - (data.get(i) * scale);
            float bottom = height - padding;

            canvas.drawRect(left, top, right, bottom, barPaint);
            canvas.drawText(String.valueOf(data.get(i)), (left + right) / 2, top - 10, textPaint);
        }
    }
}