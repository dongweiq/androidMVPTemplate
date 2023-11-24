package com.cmri.uhf.mvp;

import android.os.Bundle;
import android.view.View;

public interface IView<P> {
    void bindEvent();

    void initData(Bundle savedInstanceState);

    int getOptionsMenuId();

    View getRootView();

    boolean useEventBus();

    P newP();
}