package com.sam_chordas.android.stockhawk.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.widget.StockWidgetDataProvider;

public class StockWidgetRemoteViewsService extends RemoteViewsService {
  private static final String LOG_TAG = "StockWidgetRemoteViewsService";

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new StockWidgetDataProvider(this.getApplicationContext(),intent);
  }
}