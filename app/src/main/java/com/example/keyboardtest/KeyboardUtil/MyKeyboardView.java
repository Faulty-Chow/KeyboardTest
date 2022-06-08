package com.example.keyboardtest.KeyboardUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.example.keyboardtest.KeyboardView.Keyboard;
import com.example.keyboardtest.KeyboardView.KeyboardView;
import com.example.keyboardtest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyKeyboardView extends KeyboardView {
    private static final String TAG = "KeyboardView";
    //    private MyKeyboard mKeyboardDefault, mKeyboardNum, mKeyboardFunc;
    private List<MyKeyboard> mKeyboardList;
    private MyKeyboard currentKeyboard;
    private Context mContext;
    private int kbdPaddingLeft;
    private int kbdPaddingRight;
    private int kbdPaddingTop;
    private int kbdPaddingBottom;
    GestureDetector mGestureDetector;

    private final int FLING_MIN_DISTANCE;

    private Map<Integer, Keyboard.Key> specialKeys;

    public MyKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.keyboardViewStyle);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        specialKeys = new HashMap<>();
        mGestureDetector = new GestureDetector(context, mGestureListener);
        FLING_MIN_DISTANCE = MyKeyboardUtil.getScreenWidth((Activity) context) / 4;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        initKeyboardView(context);
    }

    private void initKeyboardView(Context context) {
        MyKeyboard mKeyboardDefault = new MyKeyboard(context, R.xml.keyboard_main, MyKeyboard.KeyboardType.KEYBOARD_DEFAULT);
        MyKeyboard mKeyboardNum = new MyKeyboard(context, R.xml.keyboard_num, MyKeyboard.KeyboardType.KEYBOARD_NUMBER);
        MyKeyboard mKeyboardFunc = new MyKeyboard(context, R.xml.keyboard_func, MyKeyboard.KeyboardType.KEYBOARD_FUNCTION);

        mKeyboardList = new ArrayList<>();
        mKeyboardList.add(mKeyboardDefault);
        mKeyboardList.add(mKeyboardNum);
        mKeyboardList.add(mKeyboardFunc);

        //默认显示字母键盘
        switchKeyboard(MyKeyboard.KeyboardType.KEYBOARD_DEFAULT);
        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(mKeyboardMain_ActionListener);
    }

    private void switchKeyboard(MyKeyboard.KeyboardType type) {
        for (MyKeyboard keyboard : mKeyboardList) {
            if (keyboard.getType() == type) {
                setKeyboard(keyboard);
                currentKeyboard = keyboard;
                break;
            }
        }
        if (type == MyKeyboard.KeyboardType.KEYBOARD_NUMBER) {
            // TODO: 关闭 NumLock
        }
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
            if (key.label == "A")
                Log.e(TAG, "A " + key.codes[0]);
            if (key.codes[0] == MyKeyboard.KeyCode_Vice ||
                    key.codes[0] == MyKeyboard.KeyCode_Fn ||
                    key.codes[0] == MyKeyboard.KeyCode_Hold) {
                drawBackground(canvas, key, R.drawable.special_key_background_yellow);
                drawText(canvas, key);
                specialKeys.put(key.codes[0], key);
            }
            if (key.codes[0] == MyKeyboard.KeyCode_Esc ||
                    key.codes[0] == MyKeyboard.KeyCode_Tab ||
                    key.codes[0] == MyKeyboard.KeyCode_Enter ||
                    key.codes[0] == MyKeyboard.KeyCode_Space ||
                    key.codes[0] == MyKeyboard.KeyCode_Left ||
                    key.codes[0] == MyKeyboard.KeyCode_Up ||
                    key.codes[0] == MyKeyboard.KeyCode_Right ||
                    key.codes[0] == MyKeyboard.KeyCode_Down ||
                    key.codes[0] == MyKeyboard.KeyCode_Backspace) {
                drawBackground(canvas, key, R.drawable.special_key_background_blue);
                drawText(canvas, key);
            }

            if (key.codes[0] == MyKeyboard.KeyCode_CapsLock ||
                    key.codes[0] == MyKeyboard.KeyCode_LeftShift ||
                    key.codes[0] == MyKeyboard.KeyCode_LeftCtrl ||
                    key.codes[0] == MyKeyboard.KeyCode_Win ||
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

    private KeyboardView.OnKeyboardActionListener mKeyboardMain_ActionListener = new OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
//            Log.i("KeyEvent", "onPress " + primaryCode);
//            Keyboard.Key key = currentKeyboard.getKeyByCode(primaryCode);
//            int index = currentKeyboard.getIndexByCode(primaryCode);
//            if (currentKeyboard.isModifierKey(key)) {
//                if (currentKeyboard.stickykeys.get(key)) {
//                    currentKeyboard.unstickKey(index);
//                } else {
//                    key.pressed = !key.pressed;
//                }
//            }
        }

        @Override
        public void onRelease(int primaryCode) {
//            Log.i("KeyEvent", "onRelease " + primaryCode);
//            Keyboard.Key key = currentKeyboard.getKeyByCode(primaryCode);
//            int index = currentKeyboard.getIndexByCode(primaryCode);
//            if (!currentKeyboard.isModifierKey(key)) {
//                currentKeyboard.stickykeys.forEach((stickykey, isSticky) -> {
//                    if (!isSticky) {
//                        stickykey.pressed = false;
//                    }
//                });
//            }
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
//            Log.i("KeyEvent", "onKey " + primaryCode);
//            Keyboard.Key key = currentKeyboard.getKeyByCode(primaryCode);
//            int index = currentKeyboard.getIndexByCode(primaryCode);
//            Keyboard.Key Hold = currentKeyboard.getKeyByCode(MyKeyboard.KeyCode_Hold);
//            Log.d("Hold", "isSticky = " + currentKeyboard.stickykeys.get(Hold).toString());
//            Log.d("Hold", "pressed = " + Hold.pressed);
//            if (!currentKeyboard.isModifierKey(key)) {
//                if (Hold.pressed == true && !key.pressed) {
//                    key.pressed = true;
//                } else if (key.pressed == true) {
//                    key.pressed = false;
//                }
//            }
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

    GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            int touchX = (int) e.getX() - getPaddingLeft();
            int touchY = (int) e.getY() - getPaddingTop();
            int primaryIndex = -1;
            int closestKey = -1;
            int closestKeyDist = mProximityThreshold + 1;
            java.util.Arrays.fill(mDistances, Integer.MAX_VALUE);
            int[] nearestKeyIndices = getKeyboard().getNearestKeys(touchX, touchY);
            final int keyCount = nearestKeyIndices.length;
            for (int i = 0; i < keyCount; i++) {
                final Keyboard.Key key = currentKeyboard.mKeys[nearestKeyIndices[i]];
                if (!currentKeyboard.isModifierKey(key))
                    continue;
                int dist = 0;
                boolean isInside = key.isInside(touchX, touchY);
                if (isInside) {
                    primaryIndex = nearestKeyIndices[i];
                }
                dist = key.squaredDistanceFrom(touchX, touchY);
                if (dist < mProximityThreshold || isInside) {
                    if (dist < closestKeyDist) {
                        closestKeyDist = dist;
                        closestKey = nearestKeyIndices[i];
                    }
                }
            }
            if (primaryIndex == -1) {
                primaryIndex = closestKey;
            }
            if (primaryIndex != -1) {
                Log.i(TAG, "onSingleTap: " + getKeyboard().getKeys().get(primaryIndex).label);
            }

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int touchX = (int) e.getX() - getPaddingLeft();
            int touchY = (int) e.getY() - getPaddingTop();
            int primaryIndex = -1;
            int closestKey = -1;
            int closestKeyDist = mProximityThreshold + 1;
            java.util.Arrays.fill(mDistances, Integer.MAX_VALUE);
            int[] nearestKeyIndices = getKeyboard().getNearestKeys(touchX, touchY);
            final int keyCount = nearestKeyIndices.length;
            for (int i = 0; i < keyCount; i++) {
                final Keyboard.Key key = currentKeyboard.mKeys[nearestKeyIndices[i]];
                if (!currentKeyboard.isModifierKey(key))
                    continue;
                int dist = 0;
                boolean isInside = key.isInside(touchX, touchY);
                if (isInside) {
                    primaryIndex = nearestKeyIndices[i];
                }
                dist = key.squaredDistanceFrom(touchX, touchY);
                if (dist < mProximityThreshold && isInside) {
                    if (dist < closestKeyDist) {
                        closestKeyDist = dist;
                        closestKey = nearestKeyIndices[i];
                    }
                }
            }
            if (primaryIndex == -1) {
                primaryIndex = closestKey;
            }
            if (primaryIndex != -1) {
                Log.i(TAG, "onDoubleTap: " + getKeyboard().getKeys().get(primaryIndex).label);
//                currentKeyboard.stickKey(primaryIndex);
            }

            return true;
        }
    };

    private void switchKeyboard(int level) {
        if (level < 0) {
            level = 0;
        }
        if (level > mKeyboardList.size() - 1) {
            level = mKeyboardList.size() - 1;
        }
        setKeyboard(mKeyboardList.get(level));
    }
}
