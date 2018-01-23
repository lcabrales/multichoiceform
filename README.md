# MultiChoiceForm
Android Library [MultiChoiceForm](https://github.com/lcabrales/multichoiceform)

Easy implementation of a multi selection form element.
It runs on [API level 16](https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels)
and upwards.

# Features

Here's a list of the MultiChoiceForm library core features as of the current version.

  * Include any amount of `MCFStep`s in your layout.
  * Two types of fields: single selection and date.
  * Support for dependent fields.
  * Change any `MCFStep`'s options data in runtime.
  * Set required fields (with validation animations).
  * Customize validation animation.
  * Customize `OptionsActivity` toolbar.
  * Customize options empty view text.
  * Customize `MCFStepView` colors and drawable.

# Demo

Check out the videos of our sample app:

1. [Sample](https://raw.githubusercontent.com/lcabrales/multichoiceform/master/screenshots/sample.mp4)
2. [Validation only](https://raw.githubusercontent.com/lcabrales/multichoiceform/master/screenshots/validation_animation.mp4)

# Import

Using Gradle, import the dependency into your project, add this into your project's build.gradle file:

```java
allprojects {
  repositories {
      ...
      maven {
          url  'https://dl.bintray.com/hypernovalabs/maven'
      }
  }
}
```

Then, in your app's build.gradle file:
```java
compile 'com.hypernovalabs:multichoiceform:1.1.1@aar'
```

# Usage

You can access the full javadoc [here](https://lcabrales.github.io/multichoiceform/).

Here is a simple implementation of the MultiChoiceForm library:

1. Add a `MCFStepView `into your layout as follows:

```xml
<com.hypernovalabs.multichoiceform.view.MCFStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:mcf_title="Test" />
```

2. Define your `MCFStep`s in your activity class. The parameters are as follows:
  * data - your ArrayList<String> containing the MCFStep's options
  * formStepView - your MCFStepView
  * required (optional) - default is false
  
Example:
```java
ArrayList<String> data = ... //your data
MCFSingleSelectStep step = new MCFSingleSelectStep(data, (MCFStepView) findViewById(R.id.form_test), true);
```

3. Create an `ArrayList<MCFStep>` and add all of your `MCFStep`s into it:
```java
mSteps = new ArrayList<>();
mSteps.add(step);
mSteps.add(step2);
mSteps.add(step3);
...
```

4. Instantiate a `MultiChoiceForm` helper using the class' `Builder`:

```java
MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext, mSteps);
mForm = builder.build(); //build your instance

mForm.setupForm(); //required in order to enable the MCFSteps
```

5. Add this in your `onActivityResult`:
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Obligatory call
    mForm.handleActivityResult(requestCode, resultCode, data);
}
```

6. Add the definition of `OptionsActivity` in your `AndroidManifest.xml`:
```xml
<activity android:name="com.hypernovalabs.multichoiceform.OptionsActivity"
            android:screenOrientation="portrait"/>
```

Optionally, to validate your data, call `mForm.validate();` in your "Submit" button,
if it returns true, all your required fields are filled.

That's it, you are all set!

## MCFSteps Types

As of the current version, these are the types of `MCFStep`s you can use (for more information, check
the [javadoc](https://lcabrales.github.io/multichoiceform/):

### MCFSingleSelectStep

Provides a list of options and returns only one selected option. Example:

```java
ArrayList<String> data = ... //your data
FormSingleSelectStep step = new FormSingleSelectStep(data, (MCFStepView) findViewById(R.id.form_test), true);
```

### MCFDateStep

Provides an `AlertDialog` containing a `DatePicker` to select a date.
Date format is customizable, along with min and max date, and the dialog buttons.

```java
MCFDateStep dateStep = new MCFDateStep((MCFStepView) findViewById(R.id.form_date), true);
SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US)
dateStep.setDateFormat(sdf); //optional
```

## Extra Customization

This is all optional, adds increased customization to your form.

Customize the `OptionsActivity` and other `MultiChoiceForm` settings:

```java
MultiChoiceForm.Builder builder = new MultiChoiceForm.Builder(mContext, mSteps);
builder.setToolbarColors(
    ContextCompat.getColor(mContext, R.color.toolbar),
    ContextCompat.getColor(mContext, R.color.toolbar_text)); //optional
builder.setRequiredText("Fill out everything, please"); //optional
builder.setValidationColor(ContextCompat.getColor(mContext, R.color.bluet)); //optional
builder.setValidationAnimation(ValidationAnim.SHAKE); //optional
builder.setValidationDuration(Duration.SHORT); //optional
builder.setEmptyViewTexts("Attention!", "Fill out all of the required fields, please"); //optional
mForm = builder.build(); //build your instance
```

You can fully customize the view in XML:

```xml
<com.hypernovalabs.multichoiceform.view.MCFStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:mcf_arrowDrawable="@drawable/ic_arrow"
            app:mcf_enabled="false"
            app:mcf_titleColor="@color/blue"
            app:mcf_separatorColor="@color/gray"
            app:mcf_selectionColor="@color/blue"
            app:mcf_title="Empty" />
```
  
You can also customize it in your Java class:
  
```java
MCFStepView view = (MCFStepView) findViewById(R.id.form_test);

view.setTitle("Title");
view.setTitleColor(Color.BLACK);
view.setSelection("Selection");
view.setSelectionColor(Color.RED);
view.setSeparatorColor(ContextCompat.getColor(mContext, R.color.red));
view.setArrowImageView(ContextCompat.getDrawable(mContext, R.drawable.ic_action_arrow));
view.setEnabled(false);

MCFSingleSelectStep formStep = new MCFSingleSelectStep(data, view, true);
```

## Dependent MCFSteps

If you need to set any dependent step (the data depends on the selection of a previous step),
you can change the `MCFStep` data at runtime on your `onActivityResult` method.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Obligatory call
    mForm.handleActivityResult(requestCode, resultCode, data);

    //In case a step depends on another step
    if (resultCode == RESULT_OK && requestCode == MultiChoiceForm.REQUEST_SELECTION) {
        int id = data.getIntExtra(MultiChoiceFormConfig.EXTRA_ID_KEY, 0); //gets the resId of the selected MCFStep


        MCFStep currentStep = MCFStep.getStepFromId(mForm.getSteps(), id);

        //If you need to handle several dependent steps
        switch (id) {
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
```

# Changelog

## [1.1.1] - 2018-01-22
### Fixed
- ScrollView issue


## [1.1.0] - 2018-01-22
### Added
- `getSteps()` method from `MultiChoiceForm`


## [1.0.1] - 2018-01-22
### Fixed
- Bug with MCFDateStep


## [1.0.0] - 2018-01-22
### Added
- Resource prefixes.

### Changed
- OptionsActivity EXTRA constants.
- MCFStep and derived classes prefixes.
- MCFStepView's `enable(boolean)` to `setEnabled(boolean)`.

### Removed
- `MultiChoiceForm.Builder.setSteps(ArrayList<MCFStep>)`


## [0.5.0] - 2018-01-18
### Added
- onLongItemClickListener to clear the selection.

### Changed
- General improvements on the FormSteps.


## [0.4.1] - 2018-01-17
### Added
- FormDateStep and FormSingleSelectStep.
- This CHANGELOG section.

### Fixed
- FormDateStep SimpleDateFormat.
