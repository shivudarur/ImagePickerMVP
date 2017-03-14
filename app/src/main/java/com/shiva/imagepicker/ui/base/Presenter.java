package com.shiva.imagepicker.ui.base;

import android.os.Bundle;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public abstract class Presenter<T extends Presenter.View> {

    protected T view;

    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public void initialize(Bundle extras) {
    }

    public interface View {
    }
}