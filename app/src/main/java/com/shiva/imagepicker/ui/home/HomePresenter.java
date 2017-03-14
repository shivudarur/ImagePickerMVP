package com.shiva.imagepicker.ui.home;

import android.net.Uri;
import android.os.Bundle;

import com.shiva.imagepicker.ui.base.Presenter;
import com.shiva.imagepicker.ui.selectAndCaptureImage.SelectAndCaptureImageFragment.SelectDocumentAndCaptureImageListener;

import javax.inject.Inject;

/**
 * Created by shivananda.darura on 13/03/17.
 */

public class HomePresenter extends Presenter<HomeView> {

    @Inject
    public HomePresenter() {
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
        view.addSelectAndCaptureImageView(selectDocumentAndCaptureImageListener);
    }

    private final SelectDocumentAndCaptureImageListener selectDocumentAndCaptureImageListener = new SelectDocumentAndCaptureImageListener() {

        @Override
        public void onImageCaptured(String imageFilePath) {
            //notifyDocumentSelection(new DocumentModel(imageFilePath, true));
        }

        @Override
        public void onImageSelectedFromGallery(Uri uri) {
            //notifyDocumentSelection(new DocumentModel(uri, true));
        }

        @Override
        public void onError() {
            view.showError("ERROR_MESSAGE_UNKNOWN");
        }
    };
}
