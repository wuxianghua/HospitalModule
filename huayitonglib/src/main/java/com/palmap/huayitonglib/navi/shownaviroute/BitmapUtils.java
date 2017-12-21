package com.palmap.huayitonglib.navi.shownaviroute;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by yibo.liu on 2017/12/20 16:49.
 */

public class BitmapUtils {

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, option);
        option.inSampleSize = calcuteInSapmleSize(option, reqWidth, reqHeight);
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, option);
    }

    public static int calcuteInSapmleSize(BitmapFactory.Options option, int reqWidth, int reqHeight) {

        int height;
        int width;
        int inSample;
        int halfHeight;
        int halfWidth;

        height = option.outHeight;
        width = option.outWidth;
        inSample = 1;
        if (height > reqHeight || width > reqWidth) {
            halfHeight = height / 2;
            halfWidth = width / 2;
            while (halfHeight / inSample >= reqHeight && halfWidth / inSample >= reqWidth) {
                inSample *= 2;
            }
        }
        return inSample;
    }

}
