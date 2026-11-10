package com.example.isaretdil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.holisticlandmarker.HolisticLandmarker;
import com.google.mediapipe.tasks.vision.holisticlandmarker.HolisticLandmarkerResult;
import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IsaretDili";
    private static final int MAX_FRAMES = 60;
    private static final int NUM_FEATURES = 211;
    private static final int NUM_CLASSES = 226;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final float CONFIDENCE_THRESHOLD = 0.5f;

    private static final int IDX_NOSE_X  = 0;
    private static final int IDX_NOSE_Y  = 1;
    private static final int IDX_RH_X    = 99;
    private static final int IDX_RH_Y    = 100;
    private static final int IDX_LH_X    = 162;
    private static final int IDX_LH_Y    = 163;
    private static final int IDX_MOUTH_X = 27;
    private static final int IDX_MOUTH_Y = 28;
    private static final int IDX_REYE_X  = 6;
    private static final int IDX_REYE_Y  = 7;
    private static final int IDX_LEYE_X  = 15;
    private static final int IDX_LEYE_Y  = 16;
    private static final int IDX_RS_X    = 33;
    private static final int IDX_RS_Y    = 34;
    private static final int IDX_LS_X    = 36;
    private static final int IDX_LS_Y    = 37;

    private PreviewView viewFinder;
    private TextView sonucText, confidenceText, bufferText, fpsText;
    private ProgressBar bufferBar, confidenceBar;
    private LandmarkOverlayView overlayView;
    private boolean showLandmarks = true;
    private boolean darkMode = false; // Apple teması varsayılan olarak aydınlıktır
    private boolean usingFrontCamera = true;

    private Interpreter tfliteInterpreter;
    private HolisticLandmarker holisticLandmarker;
    private ExecutorService cameraExecutor;

    private final Deque<float[]> frameBuffer = new ArrayDeque<>();
    private float prevRhX = 0, prevRhY = 0;
    private float prevLhX = 0, prevLhY = 0;
    private int frameCount = 0;

    private int fpsCounter = 0;
    private long lastFpsTime = System.currentTimeMillis();
    private static final String[] LABEL_TR = {
            "abla","acele","acikmak","afiyet_olsun","agabey","agac","agir","aglamak","aile","akilli",
            "akilsiz","akraba","alisveris","anahtar","anne","arkadas","ataturk","ayakkabi","ayna","ayni",
            "baba","bahce","bakmak","bal","bardak","bayrak","bayram","bebek","bekar","beklemek",
            "ben","benzin","beraber","bilgi_vermek","biz","calismak","carsamba","catal","cay","caydanlik",
            "cekic","cirkin","cocuk","corba","cuma","cumartesi","cuzdan","dakika","dede","degistirmek",
            "devirmek","devlet","doktor","dolu","dugun","dun","dusman","duvar","eczane","eldiven",
            "emek","emekli","erkek","et","ev","evet","evli","ezberlemek","fil","fotograf",
            "futbol","gecmis","gecmis_olsun","getirmek","gol","gomlek","gormek","gostermek","gulmek","hafif",
            "hakli","hali","hasta","hastane","hata","havlu","hayir","hayirli_olsun","hayvan","hediye",
            "helal","hep","hic","hoscakal","icmek","igne","ilac","ilgilenmemek","isik","itmek",
            "iyi","kacmak","kahvalti","kalem","kalorifer","kapi","kardes","kavsak","kaza","kemer",
            "keske","kim","kimlik","kira","kitap","kiyma","kiz","koku","kolonya","komur",
            "kopek","kopru","kotu","kucak","leke","maas","makas","masa","masallah","melek",
            "memnun_olmak","mendil","merdiven","misafir","mudur","musluk","nasil","neden","nerede","nine",
            "ocak","oda","odun","ogretmen","okul","olimpiyat","olmaz","olur","onlar","orman",
            "oruc","ozur_dilemek","pamuk","pantolon","para","pastirma","patates","pazar","pazartesi","pencere",
            "persembe","piknik","polis","psikoloji","rica_etmek","saat","sabun","salca","sali","sampiyon",
            "sapka","savas","seker","selam","semsiye","sen","senet","serbest","ses","sevmek",
            "seytan","sinir","siz","soylemek","soz","sut","tamam","tarak","tarih","tatil",
            "tatli","tavan","tehlike","telefon","terazi","terzi","tesekkur","tornavida","turkiye","turuncu",
            "tuvalet","un","uzak","uzgun","var","vergi","yakin","yalniz","yanlis","yapmak",
            "yarabandi","yardim","yarin","yasak","yastik","yatak","yavas","yemek","yemek_pisirmek","yildiz",
            "yok","yol","yorgun","yumurta","zaman","zor"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFinder     = findViewById(R.id.viewFinder);
        sonucText      = findViewById(R.id.sonucText);
        confidenceText = findViewById(R.id.confidenceText);
        bufferText     = findViewById(R.id.bufferText);
        fpsText        = findViewById(R.id.fpsText);
        bufferBar      = findViewById(R.id.bufferBar);
        confidenceBar  = findViewById(R.id.confidenceBar);
        overlayView    = findViewById(R.id.landmarkOverlay);

        cameraExecutor = Executors.newSingleThreadExecutor();

        ImageButton btnGeri = findViewById(R.id.btnGeri);
        if (btnGeri != null) {
            btnGeri.setOnClickListener(v -> {
                finish();
            });
        }

        ImageButton btnLandmark = findViewById(R.id.btnLandmark);
        if (btnLandmark != null) {
            btnLandmark.setOnClickListener(v -> {
                showLandmarks = !showLandmarks;
                overlayView.setShowLandmarks(showLandmarks);
                btnLandmark.setAlpha(showLandmarks ? 1f : 0.35f);
            });
        }

        ImageButton btnTheme = findViewById(R.id.btnTheme);
        if (btnTheme != null) {
            btnTheme.setOnClickListener(v -> {
                darkMode = !darkMode;
                View bottomCard = findViewById(R.id.bottomCard);
                if (bottomCard != null) {
                    bottomCard.setBackgroundColor(darkMode ? 0xFF1C1C1E : 0xFFFFFFFF);
                }
                sonucText.setTextColor(darkMode ? 0xFFFFFFFF : 0xFF1D1D1F);
            });
        }

        ImageButton btnFlip = findViewById(R.id.btnFlip);
        if (btnFlip != null) {
            btnFlip.setOnClickListener(v -> {
                usingFrontCamera = !usingFrontCamera;
                startCamera();
            });
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initAll();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (tfliteInterpreter  != null) tfliteInterpreter.close();
        if (holisticLandmarker != null) holisticLandmarker.close();
    }

    private void initAll() {
        loadModel();
        initMediaPipe();
        startCamera();
    }

    private void loadModel() {
        try {
            MappedByteBuffer buffer = loadModelFile("isaret_dili_beyni_v2.tflite");
            Interpreter.Options opts = new Interpreter.Options();
            opts.setNumThreads(2);
            tfliteInterpreter = new Interpreter(buffer, opts);
            Log.d(TAG, "✅ Model yuklendi");
        } catch (IOException e) {
            Log.e(TAG, "❌ Model yuklenemedi: " + e.getMessage());
        }
    }

    private MappedByteBuffer loadModelFile(String fileName) throws IOException {
        android.content.res.AssetFileDescriptor afd = getAssets().openFd(fileName);
        try (FileInputStream fis = new FileInputStream(afd.getFileDescriptor())) {
            FileChannel ch = fis.getChannel();
            return ch.map(FileChannel.MapMode.READ_ONLY,
                    afd.getStartOffset(), afd.getLength());
        }
    }

    private void initMediaPipe() {
        try {
            BaseOptions base = BaseOptions.builder()
                    .setModelAssetPath("holistic_landmarker.task")
                    .build();

            HolisticLandmarker.HolisticLandmarkerOptions opts =
                    HolisticLandmarker.HolisticLandmarkerOptions.builder()
                            .setBaseOptions(base)
                            .setRunningMode(RunningMode.LIVE_STREAM)
                            .setResultListener(this::onLandmarkResult)
                            .build();

            holisticLandmarker = HolisticLandmarker.createFromOptions(this, opts);
            Log.d(TAG, "✅ MediaPipe hazir");
        } catch (Exception e) {
            Log.e(TAG, "❌ MediaPipe hatasi: " + e.getMessage());
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);

        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();

                int rotation = viewFinder.getDisplay() != null
                        ? viewFinder.getDisplay().getRotation()
                        : android.view.Surface.ROTATION_0;

                Preview preview = new Preview.Builder()
                        .setTargetRotation(rotation)
                        .build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new android.util.Size(480, 640))
                        .setTargetRotation(rotation)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                analysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    android.graphics.Bitmap bmp = null;
                    try {
                        bmp = imageProxy.toBitmap();
                        if (bmp == null || holisticLandmarker == null) return;

                        android.graphics.Matrix matrix = new android.graphics.Matrix();
                        matrix.postRotate(imageProxy.getImageInfo().getRotationDegrees());
                        if (usingFrontCamera) {
                            matrix.postScale(-1f, 1f, bmp.getWidth() / 2f, bmp.getHeight() / 2f);
                        }

                        android.graphics.Bitmap processed = android.graphics.Bitmap.createBitmap(
                                bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false
                        );

                        MPImage mp = new BitmapImageBuilder(processed).build();
                        holisticLandmarker.detectAsync(mp, System.currentTimeMillis());

                    } catch (Exception e) {
                        Log.e(TAG, "Frame hatası: " + e.getMessage());
                    } finally {
                        if (bmp != null) bmp.recycle();
                        imageProxy.close();
                    }
                });

                CameraSelector selector = new CameraSelector.Builder()
                        .requireLensFacing(usingFrontCamera
                                ? CameraSelector.LENS_FACING_FRONT
                                : CameraSelector.LENS_FACING_BACK)
                        .build();

                provider.unbindAll();
                provider.bindToLifecycle(this, selector, preview, analysis);

            } catch (Exception e) {
                Log.e(TAG, "❌ Kamera hatasi: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void onLandmarkResult(HolisticLandmarkerResult result, MPImage image) {
        float[] raw = new float[225];

        List<NormalizedLandmark> pose = result.poseLandmarks();
        if (pose != null && !pose.isEmpty()) {
            for (int i = 0; i < Math.min(33, pose.size()); i++) {
                raw[i*3]   = pose.get(i).x();
                raw[i*3+1] = pose.get(i).y();
                raw[i*3+2] = pose.get(i).z();
            }
        }

        List<NormalizedLandmark> rh = result.rightHandLandmarks();
        if (rh != null && !rh.isEmpty()) {
            for (int i = 0; i < Math.min(21, rh.size()); i++) {
                raw[99+i*3]   = rh.get(i).x();
                raw[99+i*3+1] = rh.get(i).y();
                raw[99+i*3+2] = rh.get(i).z();
            }
        }

        List<NormalizedLandmark> lh = result.leftHandLandmarks();
        if (lh != null && !lh.isEmpty()) {
            for (int i = 0; i < Math.min(21, lh.size()); i++) {
                raw[162+i*3]   = lh.get(i).x();
                raw[162+i*3+1] = lh.get(i).y();
                raw[162+i*3+2] = lh.get(i).z();
            }
        }

        final List<NormalizedLandmark> rhFinal = rh;
        final List<NormalizedLandmark> lhFinal = lh;
        final List<NormalizedLandmark> poseFinal = pose;
        runOnUiThread(() -> overlayView.update(
                rhFinal != null && !rhFinal.isEmpty() ? rhFinal : null,
                lhFinal != null && !lhFinal.isEmpty() ? lhFinal : null,
                poseFinal
        ));

        float[] features = extractFeatures(raw);
        synchronized (frameBuffer) {
            if (frameBuffer.size() >= MAX_FRAMES) frameBuffer.pollFirst();
            frameBuffer.addLast(features);
        }

        int bufSize = frameBuffer.size();
        runOnUiThread(() -> {
            bufferBar.setProgress(bufSize);
            bufferText.setText(bufSize < MAX_FRAMES ? bufSize + "/" + MAX_FRAMES : "Canlı");
        });

        fpsCounter++;
        long now = System.currentTimeMillis();
        if (now - lastFpsTime >= 1000) {
            int fps = fpsCounter;
            fpsCounter = 0;
            lastFpsTime = now;
            runOnUiThread(() -> fpsText.setText("FPS " + fps));
        }

        frameCount++;
        if (frameCount % 20 == 0 && frameBuffer.size() == MAX_FRAMES) {
            runPrediction();
        }
    }

    private float[] extractFeatures(float[] raw) {
        float nx = raw[IDX_NOSE_X];
        float ny = raw[IDX_NOSE_Y];

        float[] base = new float[201];
        int idx = 0;

        for (int i = 0; i < 25; i++) {
            base[idx++] = (raw[i*3]   - nx) / 2.0f;
            base[idx++] = (raw[i*3+1] - ny) / 2.0f;
            base[idx++] =  raw[i*3+2]        / 2.0f;
        }
        for (int i = 0; i < 21; i++) {
            base[idx++] = (raw[99+i*3]   - nx) / 2.0f;
            base[idx++] = (raw[99+i*3+1] - ny) / 2.0f;
            base[idx++] =  raw[99+i*3+2]        / 2.0f;
        }
        for (int i = 0; i < 21; i++) {
            base[idx++] = (raw[162+i*3]   - nx) / 2.0f;
            base[idx++] = (raw[162+i*3+1] - ny) / 2.0f;
            base[idx++] =  raw[162+i*3+2]        / 2.0f;
        }

        float rhX = raw[IDX_RH_X], rhY = raw[IDX_RH_Y];
        float lhX = raw[IDX_LH_X], lhY = raw[IDX_LH_Y];

        float[] extra = new float[10];
        extra[0] = dist(rhX, rhY, raw[IDX_MOUTH_X], raw[IDX_MOUTH_Y]);
        extra[1] = dist(rhX, rhY, raw[IDX_REYE_X],  raw[IDX_REYE_Y]);
        extra[2] = dist(rhX, rhY, raw[IDX_NOSE_X],  raw[IDX_NOSE_Y]);
        extra[3] = dist(rhX, rhY, raw[IDX_RS_X],    raw[IDX_RS_Y]);
        extra[4] = dist(lhX, lhY, raw[IDX_MOUTH_X], raw[IDX_MOUTH_Y]);
        extra[5] = dist(lhX, lhY, raw[IDX_LEYE_X],  raw[IDX_LEYE_Y]);
        extra[6] = dist(lhX, lhY, raw[IDX_NOSE_X],  raw[IDX_NOSE_Y]);
        extra[7] = dist(lhX, lhY, raw[IDX_LS_X],    raw[IDX_LS_Y]);
        extra[8] = dist(rhX, rhY, prevRhX, prevRhY);
        extra[9] = dist(lhX, lhY, prevLhX, prevLhY);

        prevRhX = rhX; prevRhY = rhY;
        prevLhX = lhX; prevLhY = lhY;

        float[] features = new float[NUM_FEATURES];
        System.arraycopy(base,  0, features,   0, 201);
        System.arraycopy(extra, 0, features, 201,  10);
        return features;
    }

    private float dist(float x1, float y1, float x2, float y2) {
        float dx = x1-x2, dy = y1-y2;
        return (float) Math.sqrt(dx*dx + dy*dy);
    }

    private void runPrediction() {
        if (tfliteInterpreter == null) return;

        float[][][] input = new float[1][MAX_FRAMES][NUM_FEATURES];
        synchronized (frameBuffer) {
            int i = 0;
            for (float[] frame : frameBuffer) input[0][i++] = frame;
        }

        float[][] output = new float[1][NUM_CLASSES];
        tfliteInterpreter.run(input, output);

        int bestIdx = 0;
        float bestConf = 0;
        for (int i = 0; i < NUM_CLASSES; i++) {
            if (output[0][i] > bestConf) {
                bestConf = output[0][i];
                bestIdx  = i;
            }
        }

        final int fi = bestIdx;
        final float fc = bestConf;

        runOnUiThread(() -> {
            int pct = Math.round(fc * 100);
            confidenceBar.setProgress(pct);
            confidenceText.setText("%" + pct);

            if (fc >= CONFIDENCE_THRESHOLD) {
                String label = (fi >= 0 && fi < LABEL_TR.length) ? LABEL_TR[fi] : "Sinif " + fi;
                sonucText.setText(label);
                // 🎨 RENK GÜNCELLEMESİ (Beyaz Kart İçin Koyu Gri Yazı)
                sonucText.setTextColor(darkMode ? 0xFFFFFFFF : 0xFF1D1D1F);
                sonucText.animate().scaleX(1.06f).scaleY(1.06f)
                        .setDuration(80)
                        .withEndAction(() -> sonucText.animate()
                                .scaleX(1f).scaleY(1f).setDuration(80).start())
                        .start();
            } else {
                sonucText.setText("—");
                sonucText.setTextColor(0xFF86868B); // Açık Gri
            }
        });
    }
}