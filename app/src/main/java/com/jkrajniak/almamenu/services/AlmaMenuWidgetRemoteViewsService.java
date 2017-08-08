package com.jkrajniak.almamenu.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by jakub on 28.01.17.
 */

public class AlmaMenuWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new AlmaMenuWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
