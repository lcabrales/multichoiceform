package com.hypernovalabs.multichoiceform.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hypernovalabs.multichoiceform.Duration;
import com.hypernovalabs.multichoiceform.MultiChoiceForm;
import com.hypernovalabs.multichoiceform.MultiChoiceFormConfig;
import com.hypernovalabs.multichoiceform.ValidationAnim;
import com.hypernovalabs.multichoiceform.form.FormDateStep;
import com.hypernovalabs.multichoiceform.form.FormSingleSelectStep;
import com.hypernovalabs.multichoiceform.form.FormStep;
import com.hypernovalabs.multichoiceform.view.FormStepView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MainActivity mContext = this;
    private MultiChoiceForm mForm;
    private ArrayList<FormStep> mSteps;
    private FormStep mDependentStep, mDependentStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupForm();
        setOnClickListeners();
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(com.hypernovalabs.multichoiceform.R.id.toolbar);
        TextView tvTitle = toolbar.findViewById(com.hypernovalabs.multichoiceform.R.id.toolbar_title);

        if (tvTitle != null) {
            tvTitle.setText(getTitle());
            tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.toolbar_text));
        }

        setSupportActionBar(toolbar);
    }

    private void setupForm() {
        //Create your steps
        ArrayList<String> data = getDummyData("Test", 3);
        FormSingleSelectStep step = new FormSingleSelectStep(data, (FormStepView) findViewById(R.id.form_test), false);

        ArrayList<String> data2 = getDummyData("Value", 5);
        FormSingleSelectStep step2 = new FormSingleSelectStep(data2, (FormStepView) findViewById(R.id.form_test2), true);

        ArrayList<String> data3 = getDummyData("LK", 20);
        FormSingleSelectStep step3 = new FormSingleSelectStep(data3, (FormStepView) findViewById(R.id.form_test3), true);

        mDependentStep = new FormSingleSelectStep(new ArrayList<String>(), (FormStepView) findViewById(R.id.form_dependent));

        mDependentStep2 = new FormSingleSelectStep(new ArrayList<String>(), (FormStepView) findViewById(R.id.form_dependent2));

        FormSingleSelectStep emptyStep = new FormSingleSelectStep(new ArrayList<String>(), (FormStepView) findViewById(R.id.form_empty));

        FormDateStep dateStep = new FormDateStep((FormStepView) findViewById(R.id.form_date), true);
        dateStep.setPositiveButton("Accept");
        dateStep.setNegativeButton("Cancel");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
//        dateStep.setDateFormat(sdf);
        try {
            dateStep.setMinDate(sdf.parse("01-01-2010"));
            dateStep.setMaxDate(sdf.parse("01-01-2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Adds them all into an ArrayList
        mSteps = new ArrayList<>();
        mSteps.add(step);
        mSteps.add(step2);
        mSteps.add(step3);
        mSteps.add(mDependentStep);
        mSteps.add(mDependentStep2);
        mSteps.add(emptyStep);
        mSteps.add(dateStep);

        //Create your MultiChoiceForm instance
        MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext, mSteps);
        builder.setSteps(mSteps); //required
        builder.setToolbarColors(
                ContextCompat.getColor(mContext, R.color.toolbar),
                ContextCompat.getColor(mContext, R.color.toolbar_text)); //optional
        builder.setRequiredText("Fill out everything, please"); //optional
        builder.setValidationColor(ContextCompat.getColor(mContext, R.color.bluet)); //optional
        builder.setValidationAnimation(ValidationAnim.SHAKE_HORIZONTAL); //optional
        builder.setValidationDuration(Duration.SHORT); //optional
        builder.setEmptyViewTexts("Attention!", "Fill out all of the required fields, please"); //optional
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
                        mForm.validate(); //validate all of the required FormSteps
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
        if (resultCode == RESULT_OK && requestCode == MultiChoiceForm.REQ_SELECTION) {
            int id = data.getIntExtra(MultiChoiceFormConfig.ID_KEY, 0);

            //If you need to handle several dependent steps
            FormStep formStep;
            switch (id) {
                case R.id.form_test3:
                    formStep = FormStep.getStepFromId(mSteps, id);

                    if (formStep != null) {
                        mDependentStep.setData(getDataFromSelection(formStep.getView().getSelection(), 5));
                        // Removes any previous selection from both of the dependent FormSteps fields
                        // and enables them
                        mDependentStep.getView().deselect(true); // direct child, enables it
                        mDependentStep2.getView().deselect(); // only deselects, still has a parent deselected
                    }

                    break;
                case R.id.form_dependent:
                    formStep = FormStep.getStepFromId(mSteps, id);

                    if (formStep != null) {
                        mDependentStep2.setData(getDataFromSelection(formStep.getView().getSelection(), 5));
                        //Removes any previous selection and enables it.
                        mDependentStep2.getView().deselect(true);
                    }

                    break;
            }
        }
    }
}
