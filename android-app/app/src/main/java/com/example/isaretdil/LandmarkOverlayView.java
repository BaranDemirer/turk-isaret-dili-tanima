package com.example.isaretdil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import java.util.List;

public class LandmarkOverlayView extends View {

    private final Paint glowPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint linePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint jointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<NormalizedLandmark> rightHand, leftHand, pose;
    private boolean showLandmarks = true;

    // El noktaları arasındaki bağlantılar
    private static final int[][] HAND_CONN = {
            {0,1},{1,2},{2,3},{3,4},
            {0,5},{5,6},{6,7},{7,8},
            {0,9},{9,10},{10,11},{11,12},
            {0,13},{13,14},{14,15},{15,16},
            {0,17},{17,18},{18,19},{19,20},
            {5,9},{9,13},{13,17}
    };

    // Vücut ve Yüz (Gözler, Burun, Ağız) bağlantıları
    private static final int[][] POSE_CONN = {
            {11,12},{11,13},{13,15},{12,14},{14,16}, // Kollar ve omuzlar
            {0,1},{1,2},{2,3},{3,7}, // Sol göz bölgesi
            {0,4},{4,5},{5,6},{6,8}, // Sağ göz bölgesi
            {9,10}                   // Ağız
    };

    public LandmarkOverlayView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        glowPaint.setStyle(Paint.Style.STROKE);
        glowPaint.setStrokeWidth(10f);
        glowPaint.setAlpha(50);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2.5f);

        jointPaint.setStyle(Paint.Style.FILL);
    }

    public void update(List<NormalizedLandmark> rh,
                       List<NormalizedLandmark> lh,
                       List<NormalizedLandmark> ps) {
        this.rightHand = rh;
        this.leftHand  = lh;
        this.pose      = ps;
        postInvalidate();
    }

    public void setShowLandmarks(boolean v) {
        showLandmarks = v;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!showLandmarks) return;
        int w = getWidth(), h = getHeight();

        // Sağ Eli Çiz (Mor)
        if (rightHand != null && !rightHand.isEmpty())
            drawHand(canvas, rightHand, 0xFF7C5CFF, w, h);

        // Sol Eli Çiz (Mavi)
        if (leftHand != null && !leftHand.isEmpty())
            drawHand(canvas, leftHand, 0xFF00D4FF, w, h);

        // Vücut ve Yüzü Çiz (Eğer kamera yakalayabildiyse)
        if (pose != null && pose.size() > 16)
            drawPose(canvas, pose, w, h);
    }

    private void drawHand(Canvas canvas, List<NormalizedLandmark> lms, int color, int w, int h) {
        glowPaint.setColor(color);
        linePaint.setColor(color);

        for (int[] c : HAND_CONN) {
            if (c[0] < lms.size() && c[1] < lms.size()) {
                float x0 = lms.get(c[0]).x() * w, y0 = lms.get(c[0]).y() * h;
                float x1 = lms.get(c[1]).x() * w, y1 = lms.get(c[1]).y() * h;
                canvas.drawLine(x0, y0, x1, y1, glowPaint);
                canvas.drawLine(x0, y0, x1, y1, linePaint);
            }
        }

        for (int i = 0; i < lms.size(); i++) {
            float x = lms.get(i).x() * w, y = lms.get(i).y() * h;
            float r = (i == 0) ? 8f : 5f;

            jointPaint.setColor(0xFF05060A);
            canvas.drawCircle(x, y, r+2, jointPaint);

            jointPaint.setColor(color);
            canvas.drawCircle(x, y, r, jointPaint);
        }
    }

    private void drawPose(Canvas canvas, List<NormalizedLandmark> lms, int w, int h) {
        linePaint.setColor(0xAA7C5CFF);
        linePaint.setStrokeWidth(2f);

        for (int[] c : POSE_CONN) {
            if (c[0] < lms.size() && c[1] < lms.size()) {
                canvas.drawLine(
                        lms.get(c[0]).x() * w, lms.get(c[0]).y() * h,
                        lms.get(c[1]).x() * w, lms.get(c[1]).y() * h,
                        linePaint);
            }
        }

        // Yüzdeki noktaları (burun, göz, ağız uçları) küçük daireler olarak çiz
        jointPaint.setColor(0xFF7C5CFF);
        for (int i = 0; i <= 10; i++) {
            if (i < lms.size()) {
                canvas.drawCircle(lms.get(i).x() * w, lms.get(i).y() * h, 4f, jointPaint);
            }
        }
        linePaint.setStrokeWidth(2.5f);
    }
}