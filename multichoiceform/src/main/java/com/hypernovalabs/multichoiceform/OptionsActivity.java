package com.hypernovalabs.multichoiceform;

import android.app.Notification;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.adapter.MCFStepAdapter;
import com.hypernovalabs.multichoiceform.config.MCFConfig;
import com.hypernovalabs.multichoiceform.config.MCFOptionsConfig;
import com.hypernovalabs.multichoiceform.form.MCFStepObj;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity containing a list of a MCFStep's options
 */
public class OptionsActivity extends AppCompatActivity {

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
    protected static final String EXTRA_CONFIG = EXTRA_PREFIX + ".Config";
    protected static final String EXTRA_SELECTION = EXTRA_PREFIX + ".Selection";
    protected static final String EXTRA_CUSTOM_SELECTION = EXTRA_PREFIX + ".CustomSelection";
    protected static final String EXTRA_CUSTOM_SELECTION_POSITION = EXTRA_PREFIX + ".CustomSelectionPosition";

    private OptionsActivity mContext = this;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private MCFStepAdapter mCustomAdapter;
    private ArrayList<String> mOptions;
    private ArrayList<MCFStepObj> mCustomOptions;
    static MCFOptionsConfig model;
    private ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcf_activity_options);
        if (model != null) {
            mProgressBar = findViewById(R.id.mcf_loading);
            setupToolbar();
            setupListView();
        }
    }

    /**
     * Defines and customize the Activity's Toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.mcf_toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.mcf_toolbar_title);

        if (tvTitle != null) {
            tvTitle.setText(model.title);
            if (model.toolbarTitleColor != 0)
                tvTitle.setTextColor(model.toolbarTitleColor);
        }

        if (model.toolbarBackgroundColor != 0)
            toolbar.setBackgroundColor(model.toolbarBackgroundColor);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Defines the {@link ListView} containing all of the data options.
     * Includes an {@link ListView.OnItemClickListener}, returns to home activity with the selection.
     */
    private void setupListView() {
        mListView = findViewById(R.id.mcf_list_view);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setEmptyView(findViewById(R.id.mcf_empty_view));
        setEmptyViewTexts(model.emptyViewTitle, model.emptyViewMsg);

        if (model.data != null) {
            mOptions = new ArrayList<>(model.data);
            mAdapter = new ArrayAdapter<>(mContext, R.layout.mcf_simple_list_item_checked, mOptions);
        } else {
            mCustomOptions = new ArrayList<>(model.customData);
            mCustomAdapter = new MCFStepAdapter(mContext, model.customData);
        }
        mListView.setAdapter(mAdapter != null ? mAdapter : mCustomAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                hideKeyboard();
                Intent intent = new Intent();
                if (mAdapter != null)
                    intent.putExtra(EXTRA_SELECTION, mAdapter.getItem(position));
                else {
                    MCFStepObj obj = mCustomAdapter.getItem(position);
                    intent.putExtra(EXTRA_CUSTOM_SELECTION, obj);
                    intent.putExtra(EXTRA_CUSTOM_SELECTION_POSITION, model.customData.indexOf(obj));
                }
                intent.putExtra(MCFConfig.EXTRA_TAG_KEY, model.tag);
                setResult(RESULT_OK, intent);
                finish();
                overrideTransition();
            }
        });

        checkSelection();
    }

    private void setEmptyViewTexts(String title, String msg) {
        ((TextView) findViewById(R.id.mcf_tv_empty_title)).setText(title);
        ((TextView) findViewById(R.id.mcf_tv_empty_msg)).setText(msg);
    }

    private void overrideTransition() {
        overridePendingTransition(R.anim.mcf_slide_in_left, R.anim.mcf_slide_out_right);
    }

    /**
     * If there is already a selection for the current {@link com.hypernovalabs.multichoiceform.form.MCFStep}, selects it
     */
    private void checkSelection() {
        if (model.data != null) {
            if (model.selection != null && model.selection.length() > 0)
                mListView.setItemChecked(mAdapter.getPosition(model.selection), true);
        } else if (model.customSelection != null) {
            int position = model.selectedPosition;
            mListView.setItemChecked(position, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (model.isSearchable) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mcf_menu_search, menu);

            final MenuItem searchItem = menu.findItem(R.id.search);

            Drawable iconDrawable = ContextCompat.getDrawable(mContext, R.drawable.mcf_ic_search);
            iconDrawable.setColorFilter(model.searchViewIconTint, PorterDuff.Mode.MULTIPLY);
            searchItem.setIcon(iconDrawable);

            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(model.searchViewHint);
            searchView.setIconifiedByDefault(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    hideKeyboard();
                    if (model.searchCallback != null && mContext != null)
                        model.searchCallback.OnSearchTapped(mContext, query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    updateQueriedData(newText);
                    return true;
                }
            });

        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * Update the options based on the SearchView query
     */
    private void updateQueriedData(String query) {
        if (model.data != null) {
            mOptions = new ArrayList<>();
            for (String option : model.data) {
                if (option.toLowerCase().contains(query.toLowerCase())) {
                    mOptions.add(option);
                }
            }
            mAdapter.clear();
            mAdapter.addAll(mOptions);
            mAdapter.notifyDataSetChanged();
        } else {
            mCustomOptions = new ArrayList<>();
            for (MCFStepObj option : model.customData) {
                String displayText = option.getDisplayText().toLowerCase();
                if (displayText.contains(query.toLowerCase())) {
                    mCustomOptions.add(option);
                }
            }
            mCustomAdapter.setData(mCustomOptions);
        }

    }

    /**
     * It works only if customData has previously been used
     *
     * @param newData
     */
    public void populateAdapter(ArrayList<? extends MCFStepObj> newData) {
        if (mCustomAdapter != null) {
            mCustomAdapter.setData(newData);
        }
    }

    /**
     * Shows or hides an intermittent loading view
     *
     * @param play
     */
    public void showLoading(boolean play) {
        if (play) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.show();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.hide();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        model = null;
//        DataManager.cancelRequest();
        overrideTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model = null;
        mContext = null;
//        DataManager.cancelRequest();
    }

    /**
     * Tries to hide the Soft Input Keyboard. Should be used in a form activity on button click.
     */
    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
