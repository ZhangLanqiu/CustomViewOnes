package com.example.usmall.customone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //找控件
        imageBtn = (ImageButton) findViewById(R.id.imagebtn);
    }

    
}
