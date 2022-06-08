package com.example.keyboardtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    NumberKeyboard mNumberKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MyKeyboard myKeyboard = new MyKeyboard(this, R.xml.keyboard_main);
//        myKeyboard.registerKeyboardView(findViewById(R.id.keyboardView));
//        Button btn = findViewById(R.id.button);
//        myKeyboard.registerControlButton(btn);
//        CustomKeyboard keyboard = new CustomKeyboard(this, R.xml.number_keyboard);
//        keyboard.registerKeyboardView(findViewById(R.id.keyboardView));
//        keyboard.registerEditText(findViewById(R.id.editText), true);
    }
}