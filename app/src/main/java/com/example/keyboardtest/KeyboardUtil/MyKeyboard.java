package com.example.keyboardtest.KeyboardUtil;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.keyboardtest.KeyboardView.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyKeyboard extends Keyboard {
    public static final int KeyCode_Enter = 0x28;
    public static final int KeyCode_Esc = 0x29;
    public static final int KeyCode_Backspace = 0x2A;
    public static final int KeyCode_Tab = 0x2B;
    public static final int KeyCode_Space = 0x2C;
    public static final int KeyCode_CapsLock = 0X39;
    public static final int KeyCode_PrintScreen = 0x46;
    public static final int KeyCode_ScrollLock = 0x47;
    public static final int KeyCode_Pause = 0x48;
    public static final int KeyCode_Insert = 0x49;
    public static final int KeyCode_Home = 0x4A;
    public static final int KeyCode_PageUp = 0x4B;
    public static final int KeyCode_Delete = 0x4C;
    public static final int KeyCode_End = 0x4D;
    public static final int KeyCode_PageDown = 0x4E;
    public static final int KeyCode_Right = 0x4F;
    public static final int KeyCode_Left = 0X50;
    public static final int KeyCode_Down = 0X51;
    public static final int KeyCode_Up = 0X52;
    public static final int KeyCode_LeftCtrl = 0xE0;
    public static final int KeyCode_LeftShift = 0xE1;
    public static final int KeyCode_LeftAlt = 0xE2;
    public static final int KeyCode_Win = 0xE3;
    public static final int KeyCode_RightCtrl = 0xE4;
    public static final int KeyCode_RightShift = 0xE5;
    public static final int KeyCode_RightAlt = 0xE6;
    public static final int KeyCode_RightGUI = 0xE7;

    public static final int KeyCode_Vice = 0x00;
    public static final int KeyCode_Fn = 0xE8;
    public static final int KeyCode_Hold = 0xE9;

    public static class ModifierKey {
        public final int keyCode;
        public boolean isPressed;

        public ModifierKey(int keyCode) {
            this.keyCode = keyCode;
            this.isPressed = false;
        }
    }

    public static final String mName = "Faulty Chow's Keyboard";

    public static final byte[] ReportMap = {
            0x05, 0x01,     // Usage Pg (Generic Desktop)
            0x09, 0x06,     // Usage (Keyboard)
            (byte) 0xA1, 0x01,     // Collection: (Application)
            0x05, 0x07,     // Usage Pg (Key Codes)
            0x19, (byte) 0xE0,     // Usage Min (224)
            0x29, (byte) 0xE7,     // Usage Max (231)
            0x15, 0x00,     // Log Min (0)
            0x25, 0x01,     // Log Max (1)
            // Modifier byte
            0x75, 0x01,     // Report Size (1)
            (byte) 0x95, 0x08,     // Report Count (8)
            (byte) 0x81, 0x02,     // Input: (Data, Variable, Absolute)
            // Reserved byte
            (byte) 0x95, 0x01,     // Report Count (1)
            0x75, 0x08,     // Report Size (8)
            (byte) 0x81, 0x01,     // Input: (Constant)
            // LED report
            (byte) 0x95, 0x05,     // Report Count (5)
            0x75, 0x01,     // Report Size (1)
            0x05, 0x08,     // Usage Pg (LEDs)
            0x19, 0x01,     // Usage Min (1)
            0x29, 0x05,     // Usage Max (5)
            (byte) 0x91, 0x02,     // Output: (Data, Variable, Absolute)
            // LED report padding
            (byte) 0x95, 0x01,     // Report Count (1)
            0x75, 0x03,     // Report Size (3)
            (byte) 0x91, 0x01,     // Output: (Constant)
            // Key arrays (6 bytes)
            (byte) 0x95, 0x06,     // Report Count (6)
            0x75, 0x08,     // Report Size (8)
            0x15, 0x00,     // Log Min (0)
            0x25, 0x65,     // Log Max (101)
            0x05, 0x07,     // Usage Pg (Key Codes)
            0x19, 0x00,     // Usage Min (0)
            0x29, 0x65,     // Usage Max (101)
            (byte) 0x81, 0x00,     // Input: (Data, Array)
            (byte) 0xC0          // End Collection
    };

    public static enum KeyboardType {
        KEYBOARD_DEFAULT,
        KEYBOARD_NUMBER,
        KEYBOARD_SYMBOL,
        KEYBOARD_FUNCTION
    }

    public static ModifierKey leftCtrl, leftShift, leftAlt, leftGUI, rightCtrl, rightShift, rightAlt, rightGUI;
    public static List<ModifierKey> modifierKeys;

    public static boolean NumLockKey, CapsLockKey, ScrollLockKey;

    {
        leftCtrl = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_LeftCtrl);
        leftShift = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_LeftShift);
        leftAlt = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_LeftAlt);
        leftGUI = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_Win);
        rightCtrl = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightCtrl);
        rightShift = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightShift);
        rightAlt = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightAlt);
        rightGUI = new com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey(MyKeyboard.KeyCode_Win);
        modifierKeys = new ArrayList<>();
        modifierKeys.add(leftCtrl);
        modifierKeys.add(leftShift);
        modifierKeys.add(leftAlt);
        modifierKeys.add(leftGUI);
        modifierKeys.add(rightCtrl);
        modifierKeys.add(rightShift);
        modifierKeys.add(rightAlt);
        modifierKeys.add(rightGUI);
        NumLockKey = false;
        CapsLockKey = false;
        ScrollLockKey = false;
    }

    public String TAG = "MyKeyboard";
    public final KeyboardType mType;

    public Key[] mKeys;
    public Map<Key, Boolean> stickykeys;

    public MyKeyboard(Context context, int xmlLayoutResId, KeyboardType type) {
        super(context, xmlLayoutResId);
        mType = type;
        mKeys = getKeys().toArray(new Key[getKeys().size()]);
        stickykeys = new ConcurrentHashMap<>();
        for (Key key : mKeys) {
            if (key.modifier)
                stickykeys.put(key, false);
        }
    }

    public KeyboardType getType() {
        return mType;
    }

    public void setTAG(String tag) {
        this.TAG = tag;
    }

    public void OutputReport(byte[] report) {
        byte outputReport = report[0];
        NumLockKey = outputReport >> 7 == 1;
        CapsLockKey = (outputReport >> 6 & 1) == 1;
        ScrollLockKey = (outputReport >> 5 & 1) == 1;
    }

    public void getPressedKeys() {
        for (Keyboard.Key key : getKeys()) {
            if (key.pressed) {
                Log.i(TAG, "key.label = " + key.label);
            }
        }
    }

    public void pressKey(int keyCode){
        Key key=getKeyByCode(keyCode);
        if(key!=null){
            key.pressed=true;
            // TODO: send Input Report
        }
    }

    public void releaseKey(int keyCode) {
        Key key = getKeyByCode(keyCode);
        if (key != null) {
            key.pressed = false;
            // TODO: send Input Report
        }
    }

    public Key getKeyByCode(int code) {
        for (Key key : mKeys) {
            if (key.codes[0] == code) {
                return key;
            }
        }
        return null;
    }

    public int getIndexByCode(int code) {
        for (int i = 0; i < mKeys.length; i++) {
            if (mKeys[i].codes[0] == code) {
                return i;
            }
        }
        return -1;
    }

    public boolean isModifierKey(Keyboard.Key key) {
        return stickykeys.containsKey(key);
    }

    public void stickKey(int keyIndex) {
        if (isModifierKey(mKeys[keyIndex])) {
            mKeys[keyIndex].pressed = true;
            stickykeys.put(mKeys[keyIndex], true);
        }
    }

    public void unstickKey(int keyIndex) {
        if (isModifierKey(mKeys[keyIndex])) {
            mKeys[keyIndex].pressed = false;
            stickykeys.put(mKeys[keyIndex], false);
        }
    }

    public interface OnKeyboardActionListener {
        void keyPressed(int primaryCode);

        void keyReleased(int primaryCode);
    }

    OnKeyboardActionListener mListener;

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        mListener = listener;
    }
}
