package com.autoqq.autoqq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {


    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;

    private void open() {
        try {
            startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
            return;
        }
        catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
               open();
            }
        });

        if (SCREEN_WIDTH == 0){
            Display display = getWindowManager().getDefaultDisplay();
            SCREEN_WIDTH = display.getWidth();
            SCREEN_HEIGHT = display.getHeight();
        }

        startService(new Intent(this, AutoService.class));
    }

}
