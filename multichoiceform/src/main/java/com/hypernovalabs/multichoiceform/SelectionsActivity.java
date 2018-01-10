package com.hypernovalabs.multichoiceform;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.model.ExtraModel;

public class SelectionsActivity extends AppCompatActivity {

    public static final String EXTRA_MODEL_KEY = "ExtraModelKey";
    public static final String SELECTION_KEY = "SelectionKey";
    public static final String ID_KEY = "IdKey";

    private SelectionsActivity mContext = this;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ExtraModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selections);

        mModel = getIntent().getParcelableExtra(EXTRA_MODEL_KEY);

        setupToolbar();
        setupListView();

        //TODO CHANGE START ANIMATION
        //TODO ADD VALIDATE METHOD
        //TODO ADD OTHER METHODS
    }

    public void setupToolbar() {
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

        /*activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable upArrow = ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(activity, R.color.toolbar_text), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(upArrow);*/
    }

    private void setupListView() {
        mListView = findViewById(R.id.list_view);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
            }
        });

        checkSelection();
    }

    /**
     * Comprueba y deja seleccionado una opción de un step del formulario que ya haya sido seleccionado
     */
    private void checkSelection() {
        if (!mModel.selection.equals(getString(R.string.form_select)) && !mModel.selection.equals(""))
            mListView.setItemChecked(mAdapter.getPosition(mModel.selection), true);
    }
}
