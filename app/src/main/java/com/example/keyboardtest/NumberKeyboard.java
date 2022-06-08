package com.example.keyboardtest;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.keyboardtest.KeyboardView.Keyboard;
import com.example.keyboardtest.KeyboardView.KeyboardView;

public class NumberKeyboard {

    private Context mContext;
    private KeyboardView mKeyboardView;
    private EditText mEditText;

    public NumberKeyboard(Context context, KeyboardView keyboardView, EditText editText) {
        this.mContext = context;
        this.mKeyboardView = keyboardView;
        this.mEditText = editText;
        initKeyboard();
        mKeyboardView.setVisibility(View.VISIBLE);
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    hideSystemKeyboard((EditText) v);
                    mKeyboardView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v instanceof EditText) {
                    if (!hasFocus) {
                        mKeyboardView.setVisibility(View.GONE);
                    } else {
                        hideSystemKeyboard((EditText) v);
                        mKeyboardView.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    private void initKeyboard() {
        Keyboard keyboard = new Keyboard(mContext,R.xml.keyboard_num);
        mKeyboardView.setKeyboard(keyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            if(primaryCode == -4 || primaryCode == -5){
                mKeyboardView.setPreviewEnabled(false);
            }else{
                mKeyboardView.setPreviewEnabled(true);
            }
        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();
            if(primaryCode == Keyboard.KEYCODE_DONE){
                mKeyboardView.setVisibility(View.GONE);
            }else if(primaryCode == Keyboard.KEYCODE_DELETE){
                if(editable != null && editable.length() > 0){
                    if(start == end){
                        editable.delete(start -1, start);
                    }else{
                        editable.delete(start,end);
                    }
                }
            }else{
                editable.replace(start,end,Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private void hideSystemKeyboard(EditText v) {
        this.mEditText = v;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm == null){
            return;
        }
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            v.setShowSoftInputOnFocus(false);
        }else{
            v.setInputType(0);
        }
    }
}
