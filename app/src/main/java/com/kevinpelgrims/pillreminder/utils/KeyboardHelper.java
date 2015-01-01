package com.kevinpelgrims.pillreminder.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardHelper {
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
