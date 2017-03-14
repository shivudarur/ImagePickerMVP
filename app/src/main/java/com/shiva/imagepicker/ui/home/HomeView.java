package com.shiva.imagepicker.ui.home;

import com.shiva.imagepicker.ui.base.Presenter;
import com.shiva.imagepicker.ui.selectAndCaptureImage.SelectAndCaptureImageFragment.SelectDocumentAndCaptureImageListener;

/**
 * Created by shivananda.darura on 13/03/17.
 */

public interface HomeView extends Presenter.View {
    void addSelectAndCaptureImageView(SelectDocumentAndCaptureImageListener selectDocumentAndCaptureImageListener);

    void showError(String errorMessage);
}
