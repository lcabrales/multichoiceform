package com.hypernovalabs.multichoiceform.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hypernovalabs.multichoiceform.MultiChoiceForm;
import com.hypernovalabs.multichoiceform.model.FormStep;
import com.hypernovalabs.multichoiceform.view.FormStepView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivity mContext = this;
    private MultiChoiceForm mForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupForm();
        setOnClickListeners();
    }

    private void setupForm() {
        ArrayList<String> data = new ArrayList<>();
        data.add("Prueba1");
        data.add("Prueba2");
        data.add("Prueba3");
        FormStep step = new FormStep(data, (FormStepView) findViewById(R.id.form_test), true);

        ArrayList<String> data2 = new ArrayList<>();
        data2.add("Valor1");
        data2.add("Valor2");
        data2.add("Valor3");
        FormStep step2 = new FormStep(data2, (FormStepView) findViewById(R.id.form_test2), true);

        ArrayList<String> data3 = new ArrayList<>();
        data3.add("LK1");
        data3.add("LK2");
        data3.add("LK3");
        FormStep step3 = new FormStep(data3, (FormStepView) findViewById(R.id.form_test3), true);

        ArrayList<FormStep> steps = new ArrayList<>();
        steps.add(step);
        steps.add(step2);
        steps.add(step3);

        MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext);
        builder.setSteps(steps);
        builder.setToolbarColors(
                ContextCompat.getColor(mContext, R.color.toolbar),
                ContextCompat.getColor(mContext, R.color.toolbar_text));
        mForm = builder.build();

//        mForm = new MultiChoiceForm<>(mContext, steps);
        mForm.setupForm();

        /*OnSelectedListener selectedListener = new OnSelectedListener() {
            @Override
            public void onSelected(FormStepView view, String selection) {
                view.setSelection(selection);
            }
        };*/
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

    /*@Override
    protected void onStart() {
        super.onStart();

        mForm.handleActivityResult();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mForm.handleActivityResult(requestCode, resultCode, data);
    }
}
