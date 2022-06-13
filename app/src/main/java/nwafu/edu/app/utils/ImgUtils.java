package nwafu.edu.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ImageWriter;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.*;

public class ImgUtils {

    /**
     *
     * @description 将图片从本地读到内存时，即图片从File形式变为Bitmap形式
     * 特点：通过设置采样率，减少图片的像素，达到对内存中的Bitmao进行压缩
    方法说明: 该方法就是对Bitmap形式的图片进行压缩, 也就是通过设置采样率, 减少Bitmap的像素, 从而减少了它所占用的内存
     * @param srcPath
     * @return
     */
    public static String compressBmpFromBmp(String srcPath){
        //srcPathStr=srcPath;
        BitmapFactory.Options newOptions = new BitmapFactory.Options();
        newOptions.inJustDecodeBounds = true;//只读边，不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOptions);

        newOptions.inJustDecodeBounds = false;
        int w = newOptions.outWidth;
        int h = newOptions.outHeight;
        float hh = 800f;
        float ww = 480f;
        int be = 1;
        if(w > h && w >ww){
            be = (int)(newOptions.outWidth/ww);
        }else if (w < h && h > hh) {
            be = (int)(newOptions.outHeight/hh);
        }
        if(be <= 0)
            be = 1;
        newOptions.inSampleSize = be;//设置采样率
        newOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的，可不设
        newOptions.inPurgeable = true;//同时设置才会有效
        newOptions.inInputShareable = true;//当系统内存不够时候图片会自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOptions);
        int degree = readPictureDegree(srcPath);
        bitmap = rotateBitmap(bitmap, degree);
        return compressBmpToFile(bitmap);
    }


    /**
     *
     * @description 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     * 所谓的质量压缩，即为改变其图像的位深和每个像素的透明度，也就是说JPEG格式压缩后，原来图片中透明的元素将消失，所以这种格式很可能造成失真
     * @param bmp
     */
    public static String compressBmpToFile(Bitmap bmp) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ygxy/compressImgs/";

        File file = new File(path + System.currentTimeMillis() + ".jpg");
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while(baos.toByteArray().length/1024 > 100){
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options,baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
//            copyExif(srcPathStr,file.getAbsolutePath());
            return file.getAbsolutePath();
        }
        catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获取图片旋转角度
     * @param srcPath
     * @return
     */
    private static int readPictureDegree(String srcPath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(srcPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //处理图片旋转
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

}
