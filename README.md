![Pie Menu logo](https://raw.githubusercontent.com/payne911/PieMenu/master/pie_menu_logo.png "Logo Title Text 1")

# PieMenu
[![jitpack](https://jitpack.io/v/payne911/PieMenu.svg)](https://jitpack.io/#payne911/PieMenu)
[![license](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/payne911/PieMenu/blob/master/LICENSE)

---

A library for [libGDX](https://libgdx.badlogicgames.com/), an open-source game development application framework written in Java.

Aims at providing users with the so-called `RadialGroup`: it is a simple container that places its children `Actors` in a circular fashion.

The more interesting feature might be the `PieMenu` class : it wraps the `RadialGroup` with a bunch of functionalities that allow highlight and selection of items within the Group.

In terms of User Interface, circular context menu "are faster and more reliable to select from than linear menus, because selection depends on direction instead of distance." ([Wikipedia source](https://en.wikipedia.org/wiki/Pie_menu#Comparison_with_other_interaction_techniques)). That is the whole motivation behind this library.

## Table of Content
* [Demo](#demo)
  * [Basic widgets](#basic-widgets)
    * [Controls](#controls)
  * [Custom-animated widgets](#custom-animated-widgets)
* [Including in Project](#including-in-project)
* [Usage](#usage)
  * [Wiki](#wiki)
* [Final word](#final-word)
  * [Contributing](#contributing)
  * [Thanks to](#thanks-to)
    * [Credits](#credits)

---

## Demo
First, let us demonstrate what you might be able to get out of this library (those are just examples and the variety of possiblities is much larger, if not endless).

### Basic widgets
![early_demo](https://raw.githubusercontent.com/payne911/PieMenu/master/media/early_demo.gif)

#### Controls
**An [online demo](https://payne911.github.io/PieMenu/) is available.** The controls are:
* ``RIGHT-CLICK`` : opens a PieMenu meant for selection through dragging (don't release the right-click until you're done with your selection). It was configured to let you preview the selection's effect.
* ``MIDDLE-CLICK`` : opens a PieMenu meant for "normal" selection. You can release the button and select as you wish with a left-click.
* ``S`` : toggles the visibility of the "permaPie" at the bottom of the screen.
* ``C`` : toggles the style used for the "middle-click" menu.
* ``L`` : removes a Child from a few RadialWidgets.
* ``M`` : adds a Child to a few of the RadialWidgets.
* ``R`` : "restarts" the application. Everything will be back to what it was initially.

If you want to check out the same demo, but within a `desktop` setup, check out [the Demonstration class](https://github.com/payne911/PieMenu/blob/master/src/test/java/com/payne/games/piemenu/Demonstration.java). 

### Custom-animated widgets
You can also create your own animations:
![custom_animation](https://github.com/payne911/PieMenu/blob/master/media/custom_animation.gif?raw=true)

It's surprisingly easy. Check out [the Animated Widget wiki page](https://github.com/payne911/PieMenu/wiki/Animated-widget/) to find out!

---

## Including in Project
To use this in your gradle project, add the version number and jitpack repository information to your root `build.gradle` file:

```groovy
allprojects {

    ext {
    	...
        pieMenuVersion = '3.0.0'
    }
    
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```

And  in your `core` project (still inside the root `build.gradle`) add the dependency:

```groovy
project(":core") {
    apply plugin: "java-library"

    dependencies {
        ...
        api "com.github.payne911:PieMenu:$pieMenuVersion"
    }
}
```

See the [jitpack website](https://jitpack.io/#payne911/PieMenu) for more info.

**If you plan on releasing your project with an html module ("HTML5/GWT"), check out the [Wiki page on integrating gwt](https://github.com/payne911/PieMenu/wiki/GWT-integration).**

---

## Usage
The basic idea looks like this:

```java
/* Setting up and creating the widget. */
PieMenu.PieMenuStyle style = new PieMenu.PieMenuStyle();
style.sliceColor = new Color(.33f,.33f,.33f,1); // "style" variables affect the way the widget looks
PieMenu menu = new PieMenu(batch, skin.getRegion("white"), style, 80); // "white" would be a 1x1 white pixel

/* Adding a listener. */
menu.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        System.out.println("The selected index is: " + menu.getSelectedIndex());
    }
});

/* Populating the widget. */
for (int i = 0; i < 8; i++) {
    Label label = new Label(Integer.toString(i), skin);
    menu.addActor(label);
}

/* Including the Widget in the Stage. */
stage.addActor(menu);
```

And *voilà*!

### [Wiki](https://github.com/payne911/PieMenu/wiki)
This library offers you many types of behaviors related to pie menus. Many of those are well-documented in the Wiki (with description, code and gif), so make sure to check it out.

More specifically, you might be interested in:
* [Complete examples of code](https://github.com/payne911/PieMenu/wiki/Examples), along with textual descriptions of the expected behavior
* [Understanding how to customize](https://github.com/payne911/PieMenu/wiki/Customizing-the-widget) your widget's look and behavior.

---

## Final word
Very well: you've made it this far in the README! If you ever end up integrating this library into your cool projects, feel free to send a Pull Request of a GIF showcasing this library and with the name of your game; just make sure it's pushed in [the ``media`` folder](https://github.com/payne911/PieMenu/tree/master/media)!

### Contributing
If you feel like helping this library grow, make sure to check out [the Contributing](https://github.com/payne911/PieMenu/wiki/Contributing) Wiki page.

### Thanks to
* [EarlyGrey](https://github.com/earlygrey) (PieMenu is actually entirely dependent on his library: [ShapeDrawer](https://github.com/earlygrey/shapedrawer))
* [raeleus](https://github.com/raeleus)
* [mgsx](https://github.com/mgsx-dev)
* [TEttinger](https://github.com/tommyettinger)
* Albert Pétoncle

For their sustained help through the libGDX discord channel. Their extensive knowledge was greatly appreciated.

#### Credits
I used some images from [Game-Icons.net](https://game-icons.net/), more specifically the 5 icons displayed when clicking the "Toggle Radial" button. To be even more specific, the credits go to [Lorc](http://lorcblog.blogspot.com/). Those are under the [CC BY 3.0 license](https://creativecommons.org/licenses/by/3.0/) license.

Also, [raeleus](https://github.com/raeleus) made the background image for the "middle-click menu" (which is also used in this library's logo), and the test application uses the [Plain James UI Skin](https://github.com/raeleus/Plain-James-UI) created by Raymond "Raeleus" Buckley under the [CC BY 4.0 license](https://creativecommons.org/licenses/by/4.0/). [Check out the others!](https://ray3k.wordpress.com/artwork/)

The structure and build scripts of this repository were strongly inspired by [RafaSKB](https://github.com/rafaskb)'s [typing-label](https://github.com/rafaskb/typing-label) library.

Parts of this README were lazily copied and adapted from EarlyGrey's library (with his agreement). Cheers!
