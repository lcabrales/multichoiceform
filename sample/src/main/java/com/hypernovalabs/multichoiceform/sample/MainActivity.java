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
import com.hypernovalabs.multichoiceform.SelectionsActivity;
import com.hypernovalabs.multichoiceform.model.FormStep;
import com.hypernovalabs.multichoiceform.view.FormStepView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivity mContext = this;
    private MultiChoiceForm mForm;
    private ArrayList<FormStep> mSteps;
    private FormStep mEmptyStep;

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
        ArrayList<String> data = getDummyData("Test", 3);
        FormStep step = new FormStep(data, (FormStepView) findViewById(R.id.form_test), true);

        ArrayList<String> data2 = getDummyData("Value", 5);
        FormStep step2 = new FormStep(data2, (FormStepView) findViewById(R.id.form_test2), true);

        ArrayList<String> data3 = getDummyData("LK", 20);
        FormStep step3 = new FormStep(data3, (FormStepView) findViewById(R.id.form_test3), true);

        mEmptyStep = new FormStep(new ArrayList<String>(), (FormStepView) findViewById(R.id.form_empty), false);

        mSteps = new ArrayList<>();
        mSteps.add(step);
        mSteps.add(step2);
        mSteps.add(step3);
        mSteps.add(mEmptyStep);

        MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext);
        builder.setSteps(mSteps); //required
        builder.setToolbarColors(
                ContextCompat.getColor(mContext, R.color.toolbar),
                ContextCompat.getColor(mContext, R.color.toolbar_text)); //optional
        builder.setRequiredText("Fill out everything, please"); //optional
        builder.setValidationColor(ContextCompat.getColor(mContext, R.color.bluet)); //optional
        builder.setValidationDuration(Duration.SHORT); //optional
        mForm = builder.build(); //build your instance

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
                        mForm.validate();
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
            int id = data.getIntExtra(SelectionsActivity.ID_KEY, 0);
            switch (id) {
                case R.id.form_test3:
                    FormStep formStep = FormStep.getStepFromId(mSteps, id);

                    if (formStep != null) {
                        mEmptyStep.setData(getDataFromSelection(formStep.getView().getSelection(), 5));
                        mEmptyStep.getView().enable(true);
                    }

                    break;
            }
        }
    }
}
