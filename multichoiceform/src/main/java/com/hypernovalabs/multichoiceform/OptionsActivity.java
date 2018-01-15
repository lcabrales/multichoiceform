package com.hypernovalabs.multichoiceform;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity containing a list of a FormStep's options
 */
public class OptionsActivity extends AppCompatActivity {

    protected static final String EXTRA_MODEL_KEY = "ExtraModelKey";
    protected static final String SELECTION_KEY = "SelectionKey";
    public static final String ID_KEY = "IdKey";

    private OptionsActivity mContext = this;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ExtraModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        getIntent().setExtrasClassLoader(ExtraModel.class.getClassLoader());
        mModel = getIntent().getParcelableExtra(EXTRA_MODEL_KEY);

        setupToolbar();
        setupListView();
    }

    /**
     * Defines and customize the Activity's Toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);

        if (tvTitle != null) {
            tvTitle.setText(mModel.title);
            if (mModel.toolbarTitleColor != 0)
                tvTitle.setTextColor(mModel.toolbarTitleColor);
        }

        if (mModel.toolbarBackgroundColor != 0)
            toolbar.setBackgroundColor(mModel.toolbarBackgroundColor);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Defines the ListView containing all of the data options.
     * Includes an OnItemClickListener, returns to home activity with the selection
     */
    private void setupListView() {
        mListView = findViewById(R.id.list_view);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setEmptyView(findViewById(R.id.empty_view));
        setEmptyViewTexts(mModel.emptyViewTitle, mModel.emptyViewMsg);

        mAdapter = new ArrayAdapter<>(mContext, R.layout.simple_list_item_checked,
                mModel.data);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(SELECTION_KEY, String.valueOf(mAdapter.getItem(i)));
                intent.putExtra(ID_KEY, mModel.id);

                setResult(RESULT_OK, intent);
                finish();

                overrideTransition();
            }
        });

        checkSelection();
    }

    private void setEmptyViewTexts(String title, String msg) {
        ((TextView) findViewById(R.id.tv_empty_title)).setText(title);
        ((TextView) findViewById(R.id.tv_empty_msg)).setText(msg);
    }

    private void overrideTransition() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * If there is already a selection for the current FormStep, selects it
     */
    private void checkSelection() {
        if (mModel.selection != null && mModel.selection.length() > 0)
            mListView.setItemChecked(mAdapter.getPosition(mModel.selection), true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overrideTransition();
    }
}
