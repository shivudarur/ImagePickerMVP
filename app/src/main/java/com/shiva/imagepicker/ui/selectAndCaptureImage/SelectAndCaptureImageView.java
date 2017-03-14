package com.shiva.imagepicker.ui.selectAndCaptureImage;

import android.net.Uri;

import com.shiva.imagepicker.ui.base.Presenter;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public interface SelectAndCaptureImageView extends Presenter.View {
    void verifyStoragePermissions();

    void openCaptureImageView();

    void openSelectImageView();

    void notifyImageCaptured(String imageFilePath);

    void notifyImageSelectedFromGallery(Uri uri);

    void onError();

    void broadcastFileToGallery(String imageFilePath);
}
