package com.example.keyboardtest;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.keyboardtest.KeyboardView.Keyboard;
import com.example.keyboardtest.KeyboardView.KeyboardView;

public class MyKeyboard extends Keyboard {

    private Activity mActivity;

    public MyKeyboard(Activity activity, int xmlLayoutResId) {
        super(activity, xmlLayoutResId);
        mActivity = activity;
    }

    private KeyboardView mKeyboardView;

    public void registerKeyboardView(KeyboardView view) {
        mKeyboardView = view;
        mKeyboardView.setKeyboard(this);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mKeyActionListener);
    }

    private KeyboardView.OnKeyboardActionListener mKeyActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRelease(int primaryCode) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onText(CharSequence text) {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeLeft() {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeRight() {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeDown() {
            // TODO Auto-generated method stub

        }

        @Override
        public void swipeUp() {
            // TODO Auto-generated method stub

        }
    };

    public void show() {
        mKeyboardView.setVisibility(KeyboardView.VISIBLE);
        mKeyboardView.setEnabled(true);
    }

    public void hide() {
        mKeyboardView.setVisibility(KeyboardView.GONE);
        mKeyboardView.setEnabled(false);
    }

    private Button mButton;
    private boolean isVisibility = false;

    public void registerControlButton(Button button) {
        mButton = button;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisibility) {
                    hide();
                } else {
                    show();
                }
                isVisibility = !isVisibility;
            }
        });
    }

    private Button.OnClickListener mButtonClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mKeyboardView.getVisibility() == KeyboardView.VISIBLE) {
                hide();
            } else {
                show();
            }
        }
    };
}
