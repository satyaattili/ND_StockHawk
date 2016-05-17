package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;
import java.util.Collections;

public class StockGraphActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Cursor>{

  private static final int CURSOR_LOADER_ID = 0;
  private static final String LOG_TAG = "StockGraphActivity";
  private Cursor mCursor;
  private LineChart mChart;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_stock_graph);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initChart();

    Intent intent = getIntent();
    Bundle args = new Bundle();
    toolbar.setTitle(intent.getStringExtra(getResources().getString(R.string.string_symbol)));
    args.putString(getResources().getString(R.string.string_symbol), intent.getStringExtra(getResources().getString(R.string.string_symbol)));
    getLoaderManager().initLoader(CURSOR_LOADER_ID, args, this);
  }

  private void initChart() {
    mChart = (LineChart) findViewById(R.id.stockGraph);
    if(mChart != null){
      mChart.setDragDecelerationFrictionCoef(0.9f);
      mChart.setDragEnabled(true);
      mChart.setScaleEnabled(true);
      mChart.setDrawGridBackground(false);
      mChart.setHighlightPerDragEnabled(true);
      mChart.animateX(800);
    }

  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, QuoteProvider.Quotes.CONTENT_URI,
        new String[]{ QuoteColumns.BIDPRICE,QuoteColumns.CREATED},
        QuoteColumns.SYMBOL + " = ?",
        new String[]{args.getString(getResources().getString(R.string.string_symbol))},
        null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mCursor = data;;
    setData();
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

  }

  private void setData() {

    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<Entry> yVals = new ArrayList<Entry>();

    mCursor.moveToFirst();
    for (int i = 0; i < mCursor.getCount(); i++){
      float price = Float.parseFloat(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
      String time = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CREATED));
      if(time != null){
        xVals.add(time);
        yVals.add(new Entry(price, i));
        Log.d(LOG_TAG, "Time : "+time);
        Log.d(LOG_TAG, "price : "+price);
      }

      mCursor.moveToNext();
    }

    LineDataSet set1;

    if (mChart.getData() != null &&
        mChart.getData().getDataSetCount() > 0) {
      set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
      //set1.setYVals(yVals);
      //mChart.getData().setXVals(xVals);
      mChart.notifyDataSetChanged();
    } else {
      // create a dataset and give it a type
      set1 = new LineDataSet(yVals, getString(R.string.txt_stockvalue));

      // set1.setFillAlpha(110);
      // set1.setFillColor(Color.RED);

      // set the line to be drawn like this "- - - - - -"
      set1.enableDashedLine(10f, 5f, 0f);
      set1.enableDashedHighlightLine(10f, 5f, 0f);
      set1.setColor(Color.BLACK);
      set1.setCircleColor(getResources().getColor(R.color.accent));
      set1.setLineWidth(1f);
      set1.setCircleRadius(2f);
      set1.setDrawCircleHole(false);
      set1.setValueTextSize(9f);
      set1.setDrawFilled(true);

      set1.setFillColor(getResources().getColor(R.color.primary));

      ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
      dataSets.add(set1); // add the datasets

      // create a data object with the datasets
      LineData data = new LineData(xVals, dataSets);

      // set data
      mChart.setData(data);
    }
  }

}
