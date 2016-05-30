package com.example.jinwoo.lab3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);

        InteractiveSearcher interactiveSearcher = new InteractiveSearcher(this);

        linearLayout.addView(interactiveSearcher);
        setContentView(linearLayout);
    }
}
