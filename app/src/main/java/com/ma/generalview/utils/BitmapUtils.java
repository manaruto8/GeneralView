package com.ma.generalview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class BitmapUtils {

    private static final String folderName = "app";
    private static final String bitmapName = ".jpg";
    private static final int bitmapSize = 200;// kb

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 99;
        while (baos.toByteArray().length / 1024 > bitmapSize) { // 循环判断如果压缩后图片是否大于size,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options <= 5) {
                break;
            }
            options -= 5;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    public static Bitmap compressImgByScale(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 150f;// 这里设置高度为1920f
        float ww = 150f;// 这里设置宽度为1080f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    public static Bitmap compressImgByScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > bitmapSize) {// 判断如果图片大于size,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 150f;// 这里设置高度为1920f
        float ww = 150f;// 这里设置宽度为1080f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 按原比例压缩图片到指定尺寸
     *
     * @param context
     * @param inputUri
     * @param outputUri
     * @param maxLenth  最长边长
     */
    public static void reducePicture(Context context, Uri inputUri,
                                     Uri outputUri, int maxLenth, int compress) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(inputUri);
            BitmapFactory.decodeStream(is, null, options);
            is.close();
            int sampleSize = 1;
            int longestSide = 0;
            int longestSideLenth = 0;
            if (options.outWidth > options.outHeight) {
                longestSideLenth = options.outWidth;
                longestSide = 0;
            } else {
                longestSideLenth = options.outHeight;
                longestSide = 1;
            }
            if (longestSideLenth > maxLenth) {
                sampleSize = longestSideLenth / maxLenth;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = sampleSize;

            is = context.getContentResolver().openInputStream(inputUri);
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            is.close();

            if (bitmap == null) {
                return;
            }

            Bitmap srcBitmap = bitmap;
            float scale = 0;
            if (longestSide == 0) {
                scale = (float) maxLenth / (float) (srcBitmap.getWidth());
            } else {
                scale = (float) maxLenth / (float) (srcBitmap.getHeight());
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
            // 如果尺寸不变会返回本身，所以需要判断是否是统一引用来确定是否需要回收
            if (srcBitmap != bitmap) {
                srcBitmap.recycle();
                srcBitmap = null;
            }

            saveBitmapToUri(bitmap, outputUri, compress);
            bitmap.recycle();
            bitmap = null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 检测图片符合尺寸大小
     *
     * @param width
     * @param height
     * @return
     */
    public static boolean checkBitmapPixel(Bitmap bitmap, int width, int height) {
        if (bitmap == null) {
            return false;
        }
        int mHeight = bitmap.getHeight();
        int mWidth = bitmap.getWidth();
        if (mHeight < height || mWidth < width) {
            return false;
        }
        return true;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    private static boolean saveBitmapToUri(Bitmap bitmap, Uri uri, int compress) {
        try {
            File file = new File(uri.getPath());
            if (file.exists()) {
                if (file.delete()) {
                    if (!file.createNewFile()) {
                        return false;
                    }
                }
            }
            BufferedOutputStream outStream = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, compress, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 相机预览转成Bitmap
     *
     * @param nv21
     * @param width
     * @param height
     * @param context
     * @return
     */
    public static Bitmap nv21ToBitmap(byte[] nv21, int width, int height, Context context){
        RenderScript rs;
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
        Type.Builder yuvType=null;
        Type.Builder rgbaType;
        Allocation in=null;
        Allocation out=null;
        rs = RenderScript.create(context);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        if (yuvType == null){
            yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);
            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
        }
        in.copyFrom(nv21);
        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        Bitmap bmpout = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        out.copyTo(bmpout);
        return adjustPhotoRotation(bmpout,90);
    }

    /**
     * 旋转Bitmap
     */
    private static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);


        return bm1;
    }


    public static boolean saveImg(Context context, Bitmap image) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), folderName);
            String fileName = System.currentTimeMillis() + bitmapName;
            File newFile = new File(file, fileName);
            FileOutputStream outputStream = new FileOutputStream(newFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveImg(Activity activity, File originFile){
        File file = new File(Environment.getExternalStorageDirectory(), folderName);
        String fileName = System.currentTimeMillis() +bitmapName;
        File newFile = new File(file, fileName);

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(originFile).getChannel();
            outputChannel = new FileOutputStream(newFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputChannel.close();
            outputChannel.close();

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(newFile);
            mediaScanIntent.setData(contentUri);
            activity.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            return false;
        } finally {
        }
        return true;
    }

}
