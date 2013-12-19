Progress Button
===========

A Button which show an animated drawable when it is in a loading state. Could be abused for a lot of other things, but it was mainly created as a way to have a button show it is waiting for something asynchronously. You define loadingdrawables (which can be anything) and when the button enters the loading state, the drawables are switched, until the button leaves this state.

This library contains a few examples and also a < rotate > drawable as an example on how to create some kind of spinner.

<img src="https://dl.dropboxusercontent.com/u/1114261/progressbutton.png"  width="500px" />

Define it in your layout:

```xml
<com.trikke.progress.widget.ProgressButton
			android:id="@+id/button"
			android:layout_width="wrap_content"
			android:drawablePadding="15dp"
			android:drawableLeft="@drawable/icon"
			android:layout_height="wrap_content"
			android:text="@string/pressme"
			app:loadingText="@string/loading"
			app:autoDisableClickable="true"
			app:loadingDrawableRight="@drawable/spinner"/>
```

Don't forget to add the stylable element to attrs.xml

```xml
<declare-styleable name="ProgressButton">
		<attr name="loadingDrawableLeft" format="reference" />
		<attr name="loadingDrawableTop" format="reference" />
		<attr name="loadingDrawableRight" format="reference" />
		<attr name="loadingDrawableBottom" format="reference" />
		<attr name="loadingText" format="reference|string" />
		<attr name="loading" format="reference|boolean" />
		<attr name="autoDisableClickable" format="reference|boolean"/>
	</declare-styleable>
```

You define it as a normal button, except there are extra parameters:

| parameter        | purpose           |
| ------------- |:-------------:|
| loadingText      | text to show while in loading state |
| autoDisableClickable      | automatically disable "clickable" when entering the loading state |
| loadingDrawable[position] | define a loading drawable at this position ( left, top, right, bottom) | 
| loading | immediately set the button in a state | 

Enable / disable the progress animation:

```java
...
// switch to the loading state, changes the text and shows the loading drawables
progressButton.enableLoadingState();

// switch to the default state, with default drawables and text
progressButton.disableLoadingState();
...
```

Download
--------

Just clone this repository and use as you see fit. If you've fixed something or added a feature, be so kind to make a pull request.