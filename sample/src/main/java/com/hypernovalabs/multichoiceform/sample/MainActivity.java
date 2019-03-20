package com.hypernovalabs.multichoiceform.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.MultiChoiceForm;
import com.hypernovalabs.multichoiceform.OptionsActivity;
import com.hypernovalabs.multichoiceform.ValidateAnimation;
import com.hypernovalabs.multichoiceform.config.MCFConfig;
import com.hypernovalabs.multichoiceform.form.MCFDateStep;
import com.hypernovalabs.multichoiceform.form.MCFSingleSelectStep;
import com.hypernovalabs.multichoiceform.form.MCFStep;
import com.hypernovalabs.multichoiceform.form.MCFTextInputStep;
import com.hypernovalabs.multichoiceform.model.Regex;
import com.hypernovalabs.multichoiceform.view.MCFStepView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MCFSingleSelectStep.OnSearchTappedListener {

    private MainActivity mContext = this;
    private MultiChoiceForm mForm;
    private ArrayList<MCFStep> mSteps;
    private MCFStep mDependentStep, mDependentStep2;
    private MCFSingleSelectStep customStep;
    private OptionsActivity mOptionsContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        DataManager.setBaseContext(getApplicationContext());
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupForm();
        setOnClickListeners();
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);

        if (tvTitle != null) {
            tvTitle.setText(getTitle());
            tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.toolbar_text));
        }

        setSupportActionBar(toolbar);
    }

    private void setupForm() {
        //Create your steps
        ArrayList<String> data = getDummyData("Test", 3);
        MCFSingleSelectStep step = new MCFSingleSelectStep(data, (MCFStepView) findViewById(R.id.form_test), false);
        step.setSearchable(true); //to enable the SearchView
        step.setTag(100); //this will be used instead of resId to identify the step.

        String[] data2 = {"Yes", "No"};
        MCFSingleSelectStep step2 = new MCFSingleSelectStep(
                data2,
                (MCFStepView) findViewById(R.id.form_test2),
                true);

        MCFTextInputStep textInputStep = new MCFTextInputStep(
                (MCFStepView) findViewById(R.id.form_test3),
                true,
                new Regex("^[a-zA-Z0-9]*$", "Only alphanumeric characters"));
        textInputStep.setExplanatoryText("Please enter your name");
        textInputStep.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mDependentStep = new MCFSingleSelectStep(new ArrayList<String>(), (MCFStepView) findViewById(R.id.form_dependent));

        mDependentStep2 = new MCFSingleSelectStep(new ArrayList<String>(), (MCFStepView) findViewById(R.id.form_dependent2));

        MCFSingleSelectStep emptyStep = new MCFSingleSelectStep(new ArrayList<String>(), (MCFStepView) findViewById(R.id.form_empty));

        MCFDateStep dateStep = new MCFDateStep((MCFStepView) findViewById(R.id.form_date), true);
        dateStep.setPositiveButton("Accept");
        dateStep.setNegativeButton("Cancel");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//        dateStep.setDateFormat(sdf);
        try {
            dateStep.setMinDate(sdf.parse("01-01-2010"));
            dateStep.setMaxDate(sdf.parse("01-01-2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //2943 is the max number of elements supported
        //520156 bytes exceed the max size of intent extra
        //1820000 new max (Test with Pixel XL 4GB RAM)
        ArrayList<CustomModel> customModels = getCustomDummyData("Display Text", 1);

        customStep = new MCFSingleSelectStep(true,
                (MCFStepView) findViewById(R.id.custom_step),
                customModels);

        customStep.setSearchable(true);
        customStep.setOnSearchTappedListener(mContext);

        //Adds them all into an ArrayList
        mSteps = new ArrayList<>();
        mSteps.add(step);
        mSteps.add(step2);
        mSteps.add(textInputStep);
        mSteps.add(mDependentStep);
        mSteps.add(mDependentStep2);
        mSteps.add(emptyStep);
        mSteps.add(dateStep);
        mSteps.add(customStep);

        //Create your MultiChoiceForm instance
        MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext, mSteps)
                .setToolbarColors(
                        ContextCompat.getColor(mContext, R.color.toolbar),
                        ContextCompat.getColor(mContext, R.color.toolbar_text)) //optional
                .setRequiredText("Fill out everything, please") //optional
                .setValidationColor(ContextCompat.getColor(mContext, R.color.bluet)) //optional
                .setValidationAnimation(ValidateAnimation.SHAKE_HORIZONTAL) //optional
                .setValidationDuration(ValidateAnimation.SHORT) //optional
                .setEmptyViewTexts("Attention!", "Fill out all of the required fields, please") //optional
                .setSearchViewHint("Search here...") //optional
                .setToolbarIconTint(Color.BLACK) //optional
                .setHasAutoFocus(true); //optional

        mForm = builder.build(); //build your instance

        //Creates the form
        mForm.setupForm();
    }

    private ArrayList<String> getDummyData(String prefix, int qty) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < qty; i++) {
            list.add(prefix + (i + 1));
        }

        return list;
    }

    private ArrayList<CustomModel> getCustomDummyData(String prefix, int qty) {
        ArrayList<CustomModel> list = new ArrayList<>();

        for (int i = 0; i < qty; i++) {
            String obj = prefix + (i + 1);
            String id = "id" + (i + 1);
            list.add(i, new CustomModel(id, obj));
        }

        return list;
    }

    private ArrayList<String> getDataFromSelection(String selection, int qty) {
        ArrayList<String> list = new ArrayList<>();

        int sum = 0;
        for (int i = 0; i < selection.toCharArray().length; i++) {
            sum += selection.toCharArray()[i];
        }

        for (int i = 0; i < qty; i++) {
            list.add(String.valueOf(sum) + " - " + qty);
        }

        return list;
    }

    private void setOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.btn_validate:
                        mForm.validate(); //validate all of the required MCFSteps
                        break;
                }
            }
        };

        findViewById(R.id.btn_validate).setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Obligatory call
        mForm.handleActivityResult(requestCode, resultCode, data);

        //In case a step depends on another step
        if (resultCode == RESULT_OK && requestCode == MultiChoiceForm.REQUEST_SELECTION) {
            int tag = data.getIntExtra(MCFConfig.EXTRA_TAG_KEY, 0); //gets the tag of the selected MCFStep

            MCFStep currentStep = MCFStep.getStepFromTag(mForm.getSteps(), tag);

            //If you need to handle several dependent steps
            switch (tag) {
                case R.id.form_test3:
                    if (currentStep != null) {
                        mDependentStep.setData(getDataFromSelection(currentStep.getView().getSelection(), 5));
                        // Removes any previous selection from both of the dependent MCFSteps fields
                        // and enables them
                        mDependentStep.getView().deselect(true); // direct child, enables it
                        mDependentStep2.getView().deselect(); // only deselects, still has a parent deselected
                    }

                    break;
                case R.id.form_dependent:
                    if (currentStep != null) {
                        mDependentStep2.setData(getDataFromSelection(currentStep.getView().getSelection(), 5));
                        //Removes any previous selection and enables it.
                        mDependentStep2.getView().deselect(true);
                    }

                    break;
            }
        }
    }

    @Override
    public void OnSearchTapped(@NonNull OptionsActivity context, String query) {
        mOptionsContext = context;
        mOptionsContext.showLoading(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mOptionsContext.showLoading(false);
                Random r = new Random();
                int qty = r.nextInt(11 - 1) + 1;//between 1 and 10
                mOptionsContext.populateAdapter(getCustomDummyData("Display Text", qty));
            }
        }, 4000);
    }
}
