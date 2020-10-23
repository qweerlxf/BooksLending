package com.example.bookslending.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageUtil {
    public static String imagePath;

    @TargetApi(19)
    public static Bitmap handleImageOnKitKat(Context context, Intent data) {
        imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是Document类型的Uri，则通过Document Id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; //解析出数字格式的Id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果不是Document类型的Uri，则使用普通方式处理
            imagePath = getImagePath(context, uri, null);
        }

        return getImage(imagePath);
    }

    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;

        //通过Uri和Selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    //对Bitmap进行质量压缩
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        int options = 100;
        //循环判断如果压缩后图片是否大于100kb，大于则继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset(); //重置baos，即清空baos
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10; //每次都减少10
        }

        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

        return bitmap;
    }

    //传入图片路径，返回压缩后的Bitmap
    public static Bitmap getImage(String srcPath) {
        if (TextUtils.isEmpty(srcPath)) { //如果图片路径为空，直接返回
            return null;
        }

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts); //此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 400f; //这里设置高度为800f
        float ww = 300f; //这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1; //be = 1表示不缩放

        if (w > h && w > ww) { //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { //如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }

        if (be <= 0) {
            be = 1;
        }

        newOpts.inSampleSize = be; //设置缩放比例
        //重新读入图片，此时已经把options.inJustDecodeBounds设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //压缩好比例大小后再进行质量压缩
        return compressImage(bitmap);
    }
}
