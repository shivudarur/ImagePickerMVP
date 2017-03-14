package com.shiva.imagepicker.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

import static com.shiva.imagepicker.util.ObjectUtil.isNull;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements Presenter.View {

    protected Presenter presenter;

    protected abstract void initializeDagger();

    protected abstract void initializePresenter();

    public abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initializeButterKnife();
        initializeDagger();
        initializePresenter();

        if (!isNull(presenter)) {
            presenter.initialize(getIntent().getExtras());
        }
    }

    private void initializeButterKnife() {
        ButterKnife.bind(this);
    }
}
