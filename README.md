# MultiChoiceForm
Android Library [MultiChoiceForm](https://github.com/lcabrales/multichoiceform)

Easy implementation of a multi selection form element.
It runs on [API level 16](https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels)
and upwards.

# Features

Here's a list of the MultiChoiceForm library core features as of the current version.

  * Include any amount of `MCFStep`s in your layout.
  * Three types of fields: single selection, text input and date.
  * Support for dependent fields.
  * Supports searchable single select fields.
  * Supports regex validation for text input fields.
  * Change any `MCFStep`'s options data in runtime.
  * Accepts `MCFStep` data as `ArrayList<String>` or `String[]`.
  * Set required fields (with validation animations).
  * Customize validation animation.
  * Customize activity's toolbar.
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
implementation 'com.hypernovalabs:multichoiceform:1.10.0@aar'
```

# Usage

You can access the full javadoc [here](https://lcabrales.github.io/multichoiceform/).

Here is a simple implementation of the MultiChoiceForm library:

1. Add a `MCFStepView` into your layout as follows:

```xml
<com.hypernovalabs.multichoiceform.view.MCFStepView
            android:id="@+id/form_test"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:mcf_title="Test" />
```

2. Define your `MCFStep`s in your activity class. The parameters are as follows:
  * data - your ArrayList<String> containing the MCFStep's options
  * view - your MCFStepView
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

6. Add the definition of `OptionsActivity` and `TextInputActivity` in your `AndroidManifest.xml`:
```xml
<activity android:name="com.hypernovalabs.multichoiceform.OptionsActivity"
            android:screenOrientation="portrait"/>

<activity android:name="com.hypernovalabs.multichoiceform.TextInputActivity"
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
MCFSingleSelectStep step = new MCFSingleSelectStep(data, (MCFStepView) findViewById(R.id.form_test), true);
step.setSearchable(true); //to enable the SearchView
```

### MCFTextInputStep

Provides a single text input field with regex validation. Example:

```java
MCFTextInputStep textInputStep = new MCFTextInputStep(
                (MCFStepView) findViewById(R.id.form_test),
                true,
                new Regex("^[a-zA-Z0-9]*$", "Only alphanumeric characters"));
textInputStep.setExplanatoryText("Please enter your name"); //text below the EditText
textInputStep.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //treat it as the regular InputType
```

### MCFDateStep

Provides an `AlertDialog` containing a `DatePicker` to select a date.
Date format is customizable, along with min and max date, and the dialog buttons.

```java
MCFDateStep dateStep = new MCFDateStep((MCFStepView) findViewById(R.id.form_date), true);
SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US)
dateStep.setDateFormat(sdf); //optional
```

### MCFButtonStep

Works as a visual step only, the `onClickListener` should be manually set on the `MCFStepView`.
This step allows you to add a required step to `MultiChoiceForm.validate()` without needing to use it
as a `MCFSingleSelectStep`

```java
MCFButtonStep step2 = new MCFButtonStep((MCFStepView) findViewById(R.id.form_test), true);
step2.getView().setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //do stuff
    }
});
```

## Extra Customization

This is all optional, adds increased customization to your form.

Customize the `OptionsActivity` and other `MultiChoiceForm` settings:

```java
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
        int tag = data.getIntExtra(MCFConfig.EXTRA_TAG_KEY, 0); //gets the tag of the selected MCFStep

        MCFStep currentStep = MCFStep.getStepFromTag(mForm.getSteps(), id);

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

## [1.10.0] - 2018-07-07
### Added
- `MCFButtonStep`.

## [1.9.0] - 2018-07-04
### Added
- `ValidateAnimation` class.
- `@IntDef` magic constants for `MCFStepType`, `ValidationAnim` and `Duration` instead of Enum classes.

### Removed
- Enum classes.
- Deprecated methods.

## [1.8.0] - 2018-06-08
### Added
- `getDisabledTitleColor(int)`.
- `setDisabledTitleColor(int)`.
- `getDisabledSelectionColor(int)`.
- `setDisabledSelectionColor(int)`.

### Deprecated
- `getDisabledTextColor(int)`.
- `setDisabledTextColor(int)`.

## [1.7.0] - 2018-06-04
### Added
- `MCFStep.tag` parameter, which has a default of `MCFStep.getView().getId()`.
You can now use this parameter instead of the view id to identify the `MCFStep`. This is preferable when
working with dynamic lists of `MCFStep` where the view id is the same for all of the `MCFSteps`, such as
`ViewHolder`s.

### Modified
- Method to retrieve a certain `MCFStep` from the list of `MCFStep`s, it is now based on its `tag` parameter.

### Deprecated
- `getStepFromId(@IdRes int)`.

### Improved
- `MCFStep` constructors.

## [1.6.0] - 2018-05-28
### Added
- Text size parameters.
- Disabled color parameters.

## [1.5.4] - 2018-05-24
### Improved
- Adaptability of the step views to adjust their size according to the text length.
- Reduced the text size by `2dp`.

## [1.5.3] - 2018-05-24
### Fixed
- Crash when there was not any `Regex` specified.

## [1.5.2] - 2018-05-24
### Added
- Support for `InputType` password variants for `MCFTextInputStep`

## [1.5.1] - 2018-05-23
### Added
- `hasAutoFocus` parameter of `MultiChoiceForm` to control whether the `EditText` will have focus on `TextInputActivity` creation.

## [1.5.0] - 2018-05-23
### Added
- `MCFTextInputStep`.

### Renamed
- `MultiChoiceFormConfig` to `MCFOptionsConfig`.

## [1.4.4] - 2018-05-16
### Fixed
- Implementation of `setTitleMaxLines` and `setSelectionMaxLines`

### Improved
- Performance on the step views when the texts are too long.

## [1.4.3] - 2018-05-15
### Added
- Builder method to set the `SearchView`'s hint.
- Builder method to set the `SearchView`'s icon tint color.

### Fixed
- Android Oreo issue with the SearchView where it would not collapse the ActionView.

## [1.4.2] - 2018-05-15
### Added
- Definition of `setSelection(@StringRes int)` on any `MCFStep`.

## [1.4.1] - 2018-05-04
### Added
- `SearchView` on `OptionsActivity` based on the parameter `isSearchable`

## [1.3.4] - 2018-03-20
### Fixed
- Toast appearing multiple times.

## [1.3.3] - 2018-03-16
### Fixed
- Validation error causing the app to crash.

## [1.3.2] - 2018-03-14
### Removed
- Space next to the `MCFStepView` value when the step is disabled.

## [1.3.1] - 2018-03-05
### Fixed
- Arrow image dimensions

## [1.3.0] - 2018-02-07
### Added
- Support multilines on title and selection
- The title is shown completely in the number of lines established if nothing has been selected
- Default selectableItemBackground

### Removed
- Title Toast functionality.

## [1.2.1] - 2018-01-26
### Fixed
- `getSelection()` returning null issue


## [1.2.0] - 2018-01-24
### Added
- Support of `MCFStep` options data as `String[]`.


## [1.1.1] - 2018-01-22
### Fixed
- `ScrollView` issue


## [1.1.0] - 2018-01-22
### Added
- `getSteps()` method from `MultiChoiceForm`


## [1.0.1] - 2018-01-22
### Fixed
- Bug with `MCFDateStep`


## [1.0.0] - 2018-01-22
### Added
- Resource prefixes.

### Changed
- OptionsActivity EXTRA constants.
- `MCFStep` and derived classes prefixes.
- MCFStepView's `enable(boolean)` to `setEnabled(boolean)`.

### Removed
- `MultiChoiceForm.Builder.setSteps(ArrayList<MCFStep>)`


## [0.5.0] - 2018-01-18
### Added
- `onLongItemClickListener` to clear the selection.

### Changed
- General improvements on the FormSteps.


## [0.4.1] - 2018-01-17
### Added
- `FormDateStep` and `MCFSingleSelectStep`.
- This CHANGELOG section.

### Fixed
- `FormDateStep` SimpleDateFormat.
