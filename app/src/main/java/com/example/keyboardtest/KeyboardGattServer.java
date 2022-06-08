package com.example.keyboardtest;

import com.example.keyboardtest.KeyboardUtil.MyKeyboard;
import com.example.keyboardtest.KeyboardView.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyboardGattServer {

    public com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey leftCtrl, leftShift, leftAlt, leftGUI, rightCtrl, rightShift, rightAlt, rightGUI;
    public List<com.example.keyboardtest.KeyboardUtil.MyKeyboard.ModifierKey> modifierKeys;

    private void initModifierKeyList() {
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
    }

    private byte Keyboard_InputReport_Modifier() {
        byte ret = 0;
        for (int i = 0; i < modifierKeys.size(); i++) {
            if (modifierKeys.get(i).isPressed) {
                ret |= (1 << i);
            }
        }
        return ret;
    }

    public static boolean isModifierKey(Keyboard.Key key) {
        return key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_LeftCtrl ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_LeftShift ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_LeftAlt ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_Win ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightCtrl ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightShift ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightAlt ||
                key.codes[0] == com.example.keyboardtest.KeyboardUtil.MyKeyboard.KeyCode_RightGUI;
    }
}
