# MultiChoiceForm
Android Library MultiChoiceForm

Easy implementation of a multi selection form element (like a spinner).

# Features

Here's a list of the MultiChoiceForm library core features as of v0.0.2.

  * Include any amount of FormSteps in your layout.
  * Single selection only.
  * Change any FormStep's data in runtime.
  * Set as a required field (with validation animations).
  * Support for dependent fields.
  * Customize SelectionsActivity toolbar.
  * Customize options EmptyView.
  * Customize FormStepView colors and drawable.
  
# Usage

Add a FormStepView into your layout as follows:

```java
<com.hypernovalabs.multichoiceform.view.FormStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:title="Test" />
```

Next, you have to define your FormSteps in your activity class. The parameters are as follows:
  * data - your ArrayList<String> containing the FormStep's options
  * formStepView - your FormStepView
  * required (optional) - default is false
  
Example:

```java
ArrayList<String> data = getDummyData("Test", 3); //your data
FormStep step = new FormStep(data, (FormStepView) findViewById(R.id.form_test), true);
```

Then, create an `ArrayList<FormStep>` and add all of your FormSteps into it:
```java
mFormSteps = new ArrayList<>();
mFormSteps.add(step);
mFormSteps.add(step2);
mFormSteps.add(step3);
...
```

Next, you have to instantiate a MultiChoiceForm helper using the class' Builder:

```java
MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext);
builder.setSteps(mSteps); //required, ArrayList<FormStep> containing all of your FormSteps
builder.setToolbarColors(
    ContextCompat.getColor(mContext, R.color.toolbar),
    ContextCompat.getColor(mContext, R.color.toolbar_text)); //optional
builder.setRequiredText("Fill out everything, please"); //optional
builder.setValidationColor(ContextCompat.getColor(mContext, R.color.bluet)); //optional
builder.setValidationDuration(Duration.SHORT); //optional
mForm = builder.build(); //build your instance

mForm.setupForm(); //required in order to enable the FormSteps
```

Then, add this in your `onActivityResult`:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Obligatory call
    mForm.handleActivityResult(requestCode, resultCode, data);
}
```

Finally, add the definition of `SelectionsActivity` in your `AndroidManifest.xml`:
```java
<activity android:name="com.hypernovalabs.multichoiceform.SelectionsActivity"
            android:screenOrientation="portrait"/>
```

Optionally, to validate your data, call `mForm.validate();` in your "Submit" button, if it returns true, all your required fields are filled.

## Extra Customization

This is all optional, adds increased customization to your form.

You can fully customize the view in XML:

```java
<com.hypernovalabs.multichoiceform.view.FormStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:arrowDrawable="@drawable/ic_arrow"
            app:enabled="false"
            app:titleColor="@color/blue"
            app:separatorColor="@color/gray"
            app:selectionColor="@color/blue"
            app:title="Empty" />
```
  
You can also customize it in your Java class:
  
```java
formStep.getView().setTitle("Title");
formStep.getView().setTitleColor(Color.BLACK);
formStep.getView().setSelection("Selection");
formStep.getView().setSelectionColor(Color.RED);
formStep.getView().setSeparatorColor(ContextCompat.getColor(mContext, R.color.red));
formStep.getView().setArrowImageView(ContextCompat.getDrawable(mContext, R.drawable.ic_action_arrow));
formStep.getView().enable(false);
```

## Dependent FormSteps

If you need to set any dependent step (the data depends on the selection of a previous step), you can change the FormStep data at runtime. Here is one simple approach:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Obligatory call
    mForm.handleActivityResult(requestCode, resultCode, data);

    //In case a step depends on another step
    if (resultCode == RESULT_OK && requestCode == MultiChoiceForm.REQ_SELECTION) {
        int id = data.getIntExtra(SelectionsActivity.ID_KEY, 0); //gets the resId of the selected FormStep
        switch (id) {
            case R.id.form_test3:
                FormStep formStep = FormStep.getStepFromId(mSteps, id); //gets your selected FormStep

                if (formStep != null) {
                    //sets the data of the dependent FormStep
                    mEmptyStep.setData(getDataFromSelection(formStep.getView().getSelection(), 5));
                    mEmptyStep.getView().enable(true); //if it was disabled
                }

                break;
        }
    }
}
```
