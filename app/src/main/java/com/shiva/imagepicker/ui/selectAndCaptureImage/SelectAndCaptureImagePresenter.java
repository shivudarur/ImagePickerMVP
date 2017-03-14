package com.shiva.imagepicker.ui.selectAndCaptureImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.shiva.imagepicker.ui.base.Presenter;
import com.shiva.imagepicker.usecase.SaveImageFileUseCase;

import javax.inject.Inject;

import static com.shiva.imagepicker.util.ObjectUtil.isNull;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public class SelectAndCaptureImagePresenter extends Presenter<SelectAndCaptureImageView> {

    private final String PICASSO_LOCAL_FILE_PATH_APPEND_FORMAT = "file://";
    private final String FILE_PATH_FORMAT = "%s%s";
    private final SaveImageFileUseCase saveImageFileUseCase;

    @Inject
    public SelectAndCaptureImagePresenter(SaveImageFileUseCase saveImageFileUseCase) {
        this.saveImageFileUseCase = saveImageFileUseCase;
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        view.verifyStoragePermissions();
    }

    public void onSelectImageClick() {
        view.openSelectImageView();
    }

    public void onCaptureImageClick() {
        view.openCaptureImageView();
    }

    public void onSelectFromGalleryResult(Intent data) {
        if (!isNull(data) && !isNull(data.getData())) {
            view.notifyImageSelectedFromGallery(data.getData());
        } else {
            view.onError();
        }
    }

    public void onCaptureImageResult(Bitmap bitmap) {
        // saveImageFileUseCase.saveImageFile(bitmap, saveImageCallback);
    }
//
//    private final SaveImageCallback saveImageCallback = imagePath -> {
//        view.broadcastFileToGallery(imagePath);
//        view.notifyImageCaptured(format(FILE_PATH_FORMAT, PICASSO_LOCAL_FILE_PATH_APPEND_FORMAT, imagePath));
//    };
}
