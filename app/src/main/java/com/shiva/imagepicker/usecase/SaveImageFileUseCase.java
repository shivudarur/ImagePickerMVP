package com.shiva.imagepicker.usecase;

import android.graphics.Bitmap;

import javax.inject.Inject;

/**
 * Created by shivananda.darura on 09/02/17.
 */

public class SaveImageFileUseCase {

    @Inject
    public SaveImageFileUseCase() {
    }

    public void saveImageFile(Bitmap bitmap, final SaveImageCallback saveImageCallback) {
//        new Thread(() -> {
//            final String imagePath = saveBitmap(bitmap);
//            new Handler(Looper.getMainLooper()).post(() -> {
//                if (saveImageCallback != null) {
//                    saveImageCallback.onImageSaved(imagePath);
//                }
//            });
//        }).start();
    }

    public interface SaveImageCallback {
        void onImageSaved(String imagePath);
    }
}
