package com.example.rootfilecheckapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {

    private TextView resultText;
    private String androidVersion;
    private boolean isRooted;
    private boolean fileExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button checkRootBtn = new Button(this);
        checkRootBtn.setText("Root ellenőrzés");

        Button checkFileBtn = new Button(this);
        checkFileBtn.setText("Fájl ellenőrzés");

        Button saveReportBtn = new Button(this);
        saveReportBtn.setText("Jelentés mentése");

        Button exitBtn = new Button(this);
        exitBtn.setText("Kilépés");

        resultText = new TextView(this);
        androidVersion = Build.VERSION.RELEASE;
        resultText.setText("Android verzió: " + androidVersion);

        checkRootBtn.setOnClickListener(v -> {
            isRooted = checkRoot();
            resultText.append("\nRootolva: " + isRooted);
        });

        checkFileBtn.setOnClickListener(v -> {
            File file = new File(Environment.getExternalStorageDirectory(), "Documents/test.txt");
            fileExists = file.exists();
            resultText.append("\nFájl létezik: " + fileExists);
        });

        saveReportBtn.setOnClickListener(v -> {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), "report.txt");
                FileWriter writer = new FileWriter(file);
                writer.write("Android verzió: " + androidVersion + "\n");
                writer.write("Rootolva: " + isRooted + "\n");
                writer.write("Fájl létezik: " + fileExists + "\n");
                writer.close();
                resultText.append("\nJelentés elmentve: " + file.getAbsolutePath());
            } catch (IOException e) {
                resultText.append("\nHiba mentéskor: " + e.getMessage());
            }
        });

        exitBtn.setOnClickListener(v -> finish());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        layout.addView(checkRootBtn);
        layout.addView(checkFileBtn);
        layout.addView(saveReportBtn);
        layout.addView(resultText);
        layout.addView(exitBtn);

        setContentView(layout);
    }

    private boolean checkRoot() {
        String[] paths = {
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }
}