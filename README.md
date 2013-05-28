Color Recognition
========
About
-----
Color Recognition is a class built in Java that will extract the 4 major colors out of an image (background, primary, secondary, detail). These colors can then be used to enhance your application's graphical user interface, by having it adapt to images that the user has loaded or selected. Other uses include when you have to simply find the major colors of an image for various reasons.

Usage
-----
Usage is overall simple, with few advanced features. To load an image and retrieve the major colors, simply do the following:

	// create our object
	ColorRecognition rec = new ColorRecognition("PATH_TO_IMAGE");

	// actually analyze the pixels in the image
	// speeds are fairly consistent with varying file sizes
	rec.analyzeImage();

	// retrieve our colors
	// no calculations are done in the getters, it simply returns what it has already analyzed
	EnhancedColor backgroundColor = rec.getBackgroundColor();
	EnhancedColor primaryColor = rec.getPrimaryColor();
	EnhancedColor secondaryColor = rec.getSecondaryColor();
	EnhancedColor detailColor = rec.getDetailColor();

Colors that were not caught due to little variation in the image are simply null.

### Color Distinction
You can increase the chance of all colors being caught by modifying the `disctinctMin` property (a setter and getter is present). The default value for this property is 50. This property is what is used to avoid similar colors being caught. This value should *not* exceed 255(0xFF), nor should it be negative. Changing this value will often change all colors *besides* the background color. The lower the `disctinctMin` value, the lower variation there will be in the caught colors as well as vice versa. The reason you wouldn't constantly have the `disctinctMin` value maximized is because the higher the value, the lower the frequencies of the caught colors. Having the `disctinctMin` value to 0 (or 1) will give you the 4 most popular colors in an image, even if the colors are very similar.

### Smart Analyze
Smart analyze is a method that will simply get the greatest possible distinction value while still succeeding in finding all 4 colors. While this often will give you the most appealing colors, the downside is that these colors will not necessarily have high frequencies within the image. To use it, simply replace `rec.analyzeImage()` from the above example with `rec.smartAnalyze()`.

**This method will change and ignore your current `disctinctMin` value**

Dependencies
------------
All dependencies are included in this repository besides the `CountedLinkedList` class. All classes required for the `CountedLinkedList` class can be found [here](https://gist.github.com/hetelek/5587135).