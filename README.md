# PieMenu
[![](https://jitpack.io/v/payne911/PieMenu.svg)](https://jitpack.io/#payne911/PieMenu)

---

A library for [LibGDX](https://libgdx.badlogicgames.com/), an open-source game development application framework written in Java.

Aims at providing users with `WidgetGroup` called `RadialGroup`: it is a simple container that places its children `Actors` in a circular fashion.

The more interesting feature might be the `PieMenu` class : it wraps the `RadialGroup` with a bunch of functionalities that allow highlight and selection of items within the Group.

In terms of User Interface, circular context menu "are faster and more reliable to select from than linear menus, because selection depends on direction instead of distance." ([Wikipedia source](https://en.wikipedia.org/wiki/Pie_menu#Comparison_with_other_interaction_techniques)). That is the whole motivation behind this library.

## Table of Content
* [Demo](#demo)
* [Including in Project](#including-in-project)
* [Usage](#usage)
* [Wiki](#wiki)
* [Thanks to](#thanks-to)

---

## Demo
![early_demo](https://raw.githubusercontent.com/payne911/PieMenu/master/android/assets/early_demo.gif)

An [online demo](https://payne911.github.io/PieMenu/) is available, but very buggy for now. The actual Java code works fine, though!

Generally, the controls are:
* ``RIGHT-CLICK`` : opens a PieMenu meant for selection through dragging (don't release the right-click until you want to select the highlighted item).
* ``MIDDLE-CLICK`` : opens a PieMenu meant for "normal" selection. You can release the button and select as you wish with a left-click.
* ``CTRL`` (the left one): toggles the visibility of the "permaPie" at the bottom of the screen.
* ``TAB`` : toggles the style used for the "middle-click" menu.
* ``BACKSPACE`` : removes a Child from a few RadialWidgets.
* ``SPACE`` : adds a Child to a few of the RadialWidgets.
* ``F5`` : "restarts" the application. Everything will be back to what it was initially.

---

## Including in Project
To use this in your gradle project, add the version number and jitpack repository information to your root `build.gradle` file:

```groovy
allprojects {

    ext {
    	...
        pieMenuVersion = '0.1.0-alpha'
    }
    
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

And  in your `core` project add the dependency:

```groovy
project(":core") {
    apply plugin: "java-library"


    dependencies {
        ...
        implementation 'com.github.payne911:PieMenu:$radialGroupVersion'
    }
}
```

See the [jitpack website](https://jitpack.io/#payne911/PieMenu) for more info.

---

## Usage
A decently basic set up you could do would look like this:

```java
/* Setting up and creating the widget. */
PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
style.radius = 80;
style.selectedChildRegionColor = new Color(.7f,.3f,.5f,1);
style.childRegionColor = new Color(0,.7f,0,1);
style.alternateChildRegionColor = new Color(.7f,0,0,1);
PieMenu dragPie = new PieMenu(shape, style);

/* Customizing the behavior. */
dragPie.setHighlightIsSelection(false);
dragPie.setInfiniteSelectionRange(true);
dragPie.setManualControlOfVisibility(false);

/* Adding the listeners. */
dragPie.addListener(dragPie.getSuggestedClickListener());
dragPie.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        System.out.println("ChangeListener - selected index: " + dragPie.getSelectedIndex());
    }
});

/* Populating the widget with labels. */
int dragPieAmount = 0;
for (int i = 0; i < INITIAL_CHILDREN_AMOUNT; i++) {
    Label label = new Label(Integer.toString(dragPieAmount++), skin);
    dragPie.addActor(label);
}

/* Including the Widget in the Stage. */
stage.addActor(middleMousePie);
```

And then in the `render()` method:

```java
if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
    dragPie.centerOnMouse();
    dragPie.resetSelection();
    dragPie.setVisible(true);
}
```

That would give you a PieMenu with a behavior similar to the ones that contained images in the demo GIF.

### Wiki
However, this library offers you many more types of behaviors. They will all be documented in [the Wiki](https://github.com/payne911/PieMenu/wiki).

---

## Thanks to
* EarlyGrey (I'm actually entirely dependent on his library: [ShapeDrawer](https://github.com/earlygrey/shapedrawer))
* raelus
* mgsx

For their sustained help through the LibGDX `#scene2d` discord channel. Their extensive knowledge of the scene2d API was greatly appreciated.

### Credits
I used some images from [Game-Icons.net](https://game-icons.net/), more specifically the 5 icons displayed when clicking the "Toggle Radial" button. To be even more specific, the credits go to [Lorc](http://lorcblog.blogspot.com/). Those are under the [CC BY 3.0 license](https://creativecommons.org/licenses/by/3.0/) license.

Also, raelus made the background image for the "middle-click menu", and the test application uses the [Plain James UI Skin](https://ray3k.wordpress.com/artwork/plain-james-ui-skin-for-libgdx/) created by Raymond "Raeleus" Buckley under the [CC BY 4.0 license](https://creativecommons.org/licenses/by/4.0/). [Check out the others!](https://ray3k.wordpress.com/artwork/)

Parts of this README were lazily copied and adapted from EarlyGrey's library (on his recommandation). Cheers!
