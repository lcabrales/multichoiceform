package com.hypernovalabs.multichoiceform;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.config.MCFConfig;
import com.hypernovalabs.multichoiceform.config.MCFTextInputConfig;
import com.hypernovalabs.multichoiceform.form.MCFTextInputStep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used for {@link TextInputEditText}. Displays an input field and optionally an explanatory text.
 */
public class TextInputActivity extends AppCompatActivity implements TextWatcher {

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
    protected static final String EXTRA_CONFIG = EXTRA_PREFIX + ".Config";
    protected static final String EXTRA_SELECTION = EXTRA_PREFIX + ".Selection";

    private TextInputActivity mContext = this;
    private MCFTextInputConfig mModel;
    private TextInputLayout mTextInputLayout;
    private TextView mExplanatoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcf_activity_text_input);

        getIntent().setExtrasClassLoader(MCFTextInputStep.class.getClassLoader());
        mModel = getIntent().getParcelableExtra(EXTRA_CONFIG);

        mTextInputLayout = findViewById(R.id.input_layout);
        mExplanatoryTextView = findViewById(R.id.tv_explanatory_text);

        setupToolbar();
        setupInputLayout();
        setExplanatoryText();
    }

    /**
     * Defines and customize the Activity's Toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.mcf_toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.mcf_toolbar_title);

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
     * Defines the behavior of the input layout, including validations.
     */
    private void setupInputLayout() {
        mTextInputLayout.setHint(mModel.title);

        EditText editText = mTextInputLayout.getEditText();

        if (editText == null) throw new NullPointerException();

        editText.setInputType(mModel.inputType);

        if (mModel.hasAutoFocus) {
            editText.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        if (!mModel.selection.isEmpty()) {
            editText.setText(mModel.selection);
            editText.setSelection(mModel.selection.length());
        }

        if (mModel.maxLength != 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mModel.maxLength)});
        }

        if (mModel.isPassword) {
            mTextInputLayout.setPasswordVisibilityToggleEnabled(true);
            mTextInputLayout.setPasswordVisibilityToggleTintList(ColorStateList.valueOf(mModel.saveIconTint));
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    computeStep();
                }

                return false;
            }
        });
    }

    private void setExplanatoryText() {
        mExplanatoryTextView.setText(mModel.explanatoryText);
    }

    /**
     * Verifies whether the step needs to be validated, then perform the proper actions.
     */
    private void computeStep() {
        if (mModel.regex != null) {
            validateStep();
        } else {
            completeStep();
        }
    }

    /**
     * Validates the {@link MCFTextInputStep#mRegex}.
     */
    private void validateStep() {
        EditText editText = mTextInputLayout.getEditText();

        if (editText == null) throw new NullPointerException();

        Pattern pattern = Pattern.compile(mModel.regex.getRegex());

        Matcher matcher = pattern.matcher(editText.getText().toString());
        if (!matcher.matches()) {
            mTextInputLayout.setErrorEnabled(true);
            mTextInputLayout.setError(mModel.regex.getErrorMessage());
            editText.addTextChangedListener(mContext);

        } else {

            completeStep();
        }
    }

    /**
     * Returns to the calling activity with the input text
     */
    private void completeStep() {
        EditText editText = mTextInputLayout.getEditText();

        if (editText == null) throw new NullPointerException();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTION, editText.getText().toString());
        intent.putExtra(MCFConfig.EXTRA_ID_KEY, mModel.id);

        setResult(RESULT_OK, intent);
        finish();

        overrideTransition();
    }

    private void overrideTransition() {
        overridePendingTransition(R.anim.mcf_slide_in_left, R.anim.mcf_slide_out_right);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mTextInputLayout.getEditText().removeTextChangedListener(mContext);
        mTextInputLayout.setError(null);
        mTextInputLayout.setErrorEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overrideTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mcf_menu_save, menu);

        final MenuItem searchItem = menu.findItem(R.id.save);

        Drawable iconDrawable = ContextCompat.getDrawable(mContext, R.drawable.mcf_ic_save);
        iconDrawable.setColorFilter(mModel.saveIconTint, PorterDuff.Mode.MULTIPLY);
        searchItem.setIcon(iconDrawable);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.save) {
            hideKeyboard();

            computeStep();
        }

        return super.onOptionsItemSelected(item);
    }
}
