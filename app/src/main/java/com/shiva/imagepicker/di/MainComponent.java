package com.shiva.imagepicker.di;

import com.shiva.imagepicker.ui.home.HomeActivity;
import com.shiva.imagepicker.ui.selectAndCaptureImage.SelectAndCaptureImageFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by shivananda.darura on 23/02/17.
 */

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(HomeActivity activity);

    void inject(SelectAndCaptureImageFragment fragment);
}
