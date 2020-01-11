package com.iyuba.pushlib;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastFactory {

    public static void showShort(Context context, String message) {
        if (InitPush.getInstance().isShowToast)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, @StringRes int messageId) {
        if (InitPush.getInstance().isShowToast)
            Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message) {
        if (InitPush.getInstance().isShowToast)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, @StringRes int messageId) {
        if (InitPush.getInstance().isShowToast)
            Toast.makeText(context, messageId, Toast.LENGTH_LONG).show();
    }
}
