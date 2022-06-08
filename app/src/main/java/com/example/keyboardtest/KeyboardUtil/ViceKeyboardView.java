package com.example.keyboardtest.KeyboardUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.example.keyboardtest.KeyboardView.Keyboard;
import com.example.keyboardtest.KeyboardView.KeyboardView;
import com.example.keyboardtest.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViceKeyboardView extends KeyboardView {
    private Class mSuper;
    private Keyboard mKeyboardRemain;
    private Context mContext;
    private int kbdPaddingLeft;
    private int kbdPaddingRight;
    private int kbdPaddingTop;
    private int kbdPaddingBottom;
    private Map<Integer, Keyboard.Key> specialKeys;

    public ViceKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.keyboardViewStyle);
    }

    public ViceKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViceKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mSuper = MyKeyboardView.class.getSuperclass();
        mContext = context;
        specialKeys = new HashMap<>();
        initKeyboardView(context);
    }

    private void initKeyboardView(Context context) {
        setVisibility(TextView.GONE);
        mKeyboardRemain = new Keyboard(context, R.xml.keyboard_more);
        setKeyboard(mKeyboardRemain);
        setEnabled(false);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(mActionListener);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        kbdPaddingLeft = getPaddingLeft();
        kbdPaddingRight = getPaddingRight();
        kbdPaddingTop = getPaddingTop();
        kbdPaddingBottom = getPaddingBottom();

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == MyKeyboard.KeyCode_Space ||
                    key.codes[0] == MyKeyboard.KeyCode_Fn) {
                drawBackground(canvas, key, R.drawable.special_key_background_blue);
                drawText(canvas, key);
            }

            if (key.codes[0] == MyKeyboard.KeyCode_LeftShift ||
                    key.codes[0] == MyKeyboard.KeyCode_LeftCtrl ||
                    key.codes[0] == MyKeyboard.KeyCode_LeftAlt) {
                drawBackground(canvas, key, R.drawable.special_key_background_pink);
                drawText(canvas, key);
                specialKeys.put(key.codes[0], key);
            }
        }
    }

    public void drawBackground(Canvas canvas, Keyboard.Key key, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        drawable.setState(drawableState);
        final Rect bounds = drawable.getBounds();
        if (key.width != bounds.right ||
                key.height != bounds.bottom) {
            drawable.setBounds(0, 0, key.width, key.height);
        }
        canvas.translate(key.x + kbdPaddingLeft, key.y + kbdPaddingTop);
        drawable.draw(canvas);
        canvas.translate(-key.x - kbdPaddingLeft, -key.y - kbdPaddingTop);
    }

    public void drawText(Canvas canvas, Keyboard.Key key) {
        if (key.label != null) {
            drawText(canvas, key, key.label.toString());
        }
    }

    public void drawText(Canvas canvas, Keyboard.Key key, String text) {
        canvas.translate(key.x + kbdPaddingLeft, key.y + kbdPaddingTop);
        final Paint paint = mPaint;
        final Rect padding = mPadding;
        if (text.length() > 1 && key.codes.length < 2) {
            paint.setTextSize(mLabelTextSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            paint.setTextSize(mKeyTextSize);
            paint.setTypeface(Typeface.DEFAULT);
        }
        // Draw a drop shadow for the text
        paint.setShadowLayer(mShadowRadius, 0, 0, mShadowColor);
        // Draw the text
        canvas.drawText(text,
                (key.width - padding.left - padding.right) / 2
                        + padding.left,
                (key.height - padding.top - padding.bottom) / 2
                        + (paint.getTextSize() - paint.descent()) / 2 + padding.top,
                paint);
        // Turn off drop shadow
        paint.setShadowLayer(0, 0, 0, 0);
        canvas.translate(-key.x - kbdPaddingLeft, -key.y - kbdPaddingTop);
    }

    public void show(){
        setVisibility(TextView.VISIBLE);
        setEnabled(true);
    }

    public void hide(){
        setVisibility(TextView.GONE);
        setEnabled(false);
    }

    private KeyboardView.OnKeyboardActionListener mActionListener = new OnKeyboardActionListener() {
        private Boolean Right_Ctrl = false;
        private Boolean Right_Shift = false;
        private Boolean Right_Alt = false;

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            switch (primaryCode) {
                case MyKeyboard.KeyCode_LeftShift:
                    Right_Shift = !Right_Shift;
                    specialKeys.get(MyKeyboard.KeyCode_LeftShift).pressed = Right_Shift;
                    break;
                case MyKeyboard.KeyCode_LeftAlt:
                    Right_Alt = !Right_Alt;
                    specialKeys.get(MyKeyboard.KeyCode_LeftAlt).pressed = Right_Alt;
                    break;
                case MyKeyboard.KeyCode_LeftCtrl:
                    Right_Ctrl = !Right_Ctrl;
                    specialKeys.get(MyKeyboard.KeyCode_LeftCtrl).pressed = Right_Ctrl;
                    break;

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
}
