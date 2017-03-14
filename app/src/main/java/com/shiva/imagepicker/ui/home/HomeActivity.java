package com.shiva.imagepicker.ui.home;

import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.shiva.imagepicker.App;
import com.shiva.imagepicker.R;
import com.shiva.imagepicker.ui.base.BaseActivity;
import com.shiva.imagepicker.ui.selectAndCaptureImage.SelectAndCaptureImageFragment;
import com.shiva.imagepicker.ui.selectAndCaptureImage.SelectAndCaptureImageFragment.SelectDocumentAndCaptureImageListener;

import javax.inject.Inject;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public class HomeActivity extends BaseActivity implements HomeView {

    @Inject
    HomePresenter homePresenter;

    @Override
    public void addSelectAndCaptureImageView(SelectDocumentAndCaptureImageListener selectDocumentAndCaptureImageListener) {
        SelectAndCaptureImageFragment selectAndCaptureImageFragment = SelectAndCaptureImageFragment.newInstance();
        selectAndCaptureImageFragment.setSelectDocumentAndCaptureImageListener(selectDocumentAndCaptureImageListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_select_and_capture_image_container, selectAndCaptureImageFragment);
        transaction.commit();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, LENGTH_LONG).show();
    }

    @Override
    protected void initializeDagger() {
        App app = (App) getApplication();
        app.getMainComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = homePresenter;
        presenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }
}
