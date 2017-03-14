package com.shiva.imagepicker.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Bitmap.createBitmap;
import static android.graphics.BitmapFactory.decodeFileDescriptor;
import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.TAG_ORIENTATION;
import static android.net.Uri.fromFile;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static android.provider.MediaStore.EXTRA_OUTPUT;
import static android.provider.MediaStore.Images.ImageColumns.ORIENTATION;
import static com.shiva.imagepicker.util.ObjectUtil.isNull;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public class ImagePickerUtil {

    private final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private final String TEMP_IMAGE_NAME = "tempImage";
    private final String CAPTURE_IMAGE_RETURN_DATA = "return-data";
    private final String FILE_DESCRIPTOR_INDICATOR = "r";
    private int[] SAMPLE_SIZES = new int[]{5, 3, 2, 1};


    @Inject
    public ImagePickerUtil() {
    }

    public Intent getCaptureImageIntent(Context context) {
        Intent takePhotoIntent = new Intent(ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(CAPTURE_IMAGE_RETURN_DATA, true);
        takePhotoIntent.putExtra(EXTRA_OUTPUT, fromFile(getTempFile(context)));
        return takePhotoIntent;
    }

    public Bitmap getImageFromResult(Context context, int resultCode, Intent imageReturnedIntent) {
        Bitmap bitmap = null;
        File imageFile = getTempFile(context);
        if (resultCode == RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (isNull(imageReturnedIntent) || isNull(imageReturnedIntent.getData())
                || imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = fromFile(imageFile);
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }

            bitmap = getImageResized(context, selectedImage);
            int rotation = getRotation(context, selectedImage, isCamera);
            bitmap = rotate(bitmap, rotation);
        }
        return bitmap;
    }

    private File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private Bitmap decodeBitmap(Context context, Uri uri, int sampleSize) {
        Options options = new Options();
        options.inSampleSize = sampleSize;
        AssetFileDescriptor fileDescriptor;

        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, FILE_DESCRIPTOR_INDICATOR);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (isNull(fileDescriptor)) {
            return null;
        }

        return decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bitmap;
        int samplingIndex = 0;
        do {
            bitmap = decodeBitmap(context, selectedImage, SAMPLE_SIZES[samplingIndex]);
            samplingIndex++;
        }
        while (!isNull(bitmap) && bitmap.getWidth() < DEFAULT_MIN_WIDTH_QUALITY && samplingIndex < SAMPLE_SIZES.length);
        return bitmap;
    }

    private int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        return rotation;
    }

    private int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        context.getContentResolver().notifyChange(imageFile, null);
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return rotate;
        }
        int orientation = exif.getAttributeInt(
            TAG_ORIENTATION, ORIENTATION_NORMAL);

        switch (orientation) {
            case ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        return rotate;
    }

    private int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        String[] columns = {ORIENTATION};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
            if (!isNull(cursor) && cursor.moveToFirst()) {
                int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                result = cursor.getInt(orientationColumnIndex);
            }
        } finally {
            if (!isNull(cursor)) {
                cursor.close();
            }
        }
        return result;
    }

    private Bitmap rotate(Bitmap bitmap, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }
}