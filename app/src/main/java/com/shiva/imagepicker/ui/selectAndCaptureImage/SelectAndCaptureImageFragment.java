package com.shiva.imagepicker.ui.selectAndCaptureImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiva.imagepicker.App;
import com.shiva.imagepicker.R;
import com.shiva.imagepicker.ui.base.BaseFragment;
import com.shiva.imagepicker.ui.base.Presenter;
import com.shiva.imagepicker.util.ImagePickerUtil;

import java.io.File;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.createChooser;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.net.Uri.fromFile;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.shiva.imagepicker.util.ObjectUtil.isNull;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public class SelectAndCaptureImageFragment extends BaseFragment implements Presenter.View, SelectAndCaptureImageView {

    @Inject
    SelectAndCaptureImagePresenter presenter;
    @Inject
    ImagePickerUtil imagePickerUtil;

    // Storage Permissions
    private final int REQUEST_EXTERNAL_STORAGE = 51230;
    private final String[] PERMISSIONS_STORAGE = {
        READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
    };
    private final int OPEN_NATIVE_CAMERA_REQUEST_CODE = 51232;
    private final int OPEN_GALLERY_REQUEST_CODE = 51233;

    private SelectDocumentAndCaptureImageListener selectDocumentAndCaptureImageListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_and_capture_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void openCaptureImageView() {
        Intent intent = imagePickerUtil.getCaptureImageIntent(getContext());
        checkAndStartActivityForResult(intent, OPEN_NATIVE_CAMERA_REQUEST_CODE);
    }

    @Override
    public void openSelectImageView() {
        Intent intent = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
        checkAndStartActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
    }

    @Override
    public void notifyImageCaptured(String imageFilePath) {
        selectDocumentAndCaptureImageListener.onImageCaptured(imageFilePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_GALLERY_REQUEST_CODE:
                    presenter.onSelectFromGalleryResult(data);
                    break;
                case OPEN_NATIVE_CAMERA_REQUEST_CODE:
                    Bitmap bitmap = imagePickerUtil.getImageFromResult(getActivity(), resultCode, data);
                    presenter.onCaptureImageResult(bitmap);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.ll_image_picker_action_gallery_container, R.id.ll_image_picker_action_camera_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_image_picker_action_gallery_container:
                presenter.onSelectImageClick();
                break;
            case R.id.ll_image_picker_action_camera_container:
                presenter.onCaptureImageClick();
                break;
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     */

    @Override
    public void verifyStoragePermissions() {
        Activity activity = getActivity();
        // Check if we have read and write permissions
        int readPermission = checkSelfPermission(activity, READ_EXTERNAL_STORAGE);
        int writePermission = checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);

        if (readPermission != PERMISSION_GRANTED && writePermission != PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void notifyImageSelectedFromGallery(Uri uri) {
        selectDocumentAndCaptureImageListener.onImageSelectedFromGallery(uri);
    }

    @Override
    public void onError() {
        selectDocumentAndCaptureImageListener.onError();
    }

    @Override
    public void broadcastFileToGallery(String imageFilePath) {
        getContext().sendBroadcast(new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, fromFile(new File(imageFilePath))));
    }

    public void setSelectDocumentAndCaptureImageListener(SelectDocumentAndCaptureImageListener selectDocumentAndCaptureImageListener) {
        this.selectDocumentAndCaptureImageListener = selectDocumentAndCaptureImageListener;
    }

    private void checkAndStartActivityForResult(Intent intent, int requestCode) {
        //Open only if any intent can be handled from some component
        if (!isNull(intent.resolveActivity(getActivity().getPackageManager()))) {
            startActivityForResult(createChooser(intent, ""), requestCode);
        }
    }

    @Override
    protected void initializeDagger() {
        App app = (App) getActivity().getApplication();
        app.getMainComponent().inject(this);
    }

    @Override
    protected void initializePresenter() {
        super.presenter = presenter;
        presenter.setView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_and_capture_image;
    }

    public interface SelectDocumentAndCaptureImageListener {
        void onImageCaptured(String imageFilePath);

        void onImageSelectedFromGallery(Uri uri);

        void onError();

    }
}
