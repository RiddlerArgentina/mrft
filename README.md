# mrft (mlp real function trainer) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/df5728447ed54c0392f26eeb2e1f2d9c)](https://www.codacy.com/app/RiddlerArgentina/mrft?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=RiddlerArgentina/mrft&amp;utm_campaign=Badge_Grade)
Small GUI used for training MLP with real functions.

Libraries used
--------------
- [`libai`](https://github.com/kronenthaler/libai): MLP implementation
- [`exp4j`](https://github.com/RiddlerArgentina/exp4j): Populating datasets,
transformations and noise
- [`jDrawingLib`](https://github.com/RiddlerArgentina/jdrawinglib): Plotting functions,
and creating images and animations
- [`FileDrop`](http://iharder.sourceforge.net/current/java/filedrop/): Drag & Drop support
- [`Java ECG Generator`](http://www.mit.edu/~gari/CODE/ECGSYN/JAVA/APPLET2/ecgsyn/ecg-java/source.html): Synthetic ECG (training example)
- [`Spline`](https://source.android.com/): Error smoothing
- [`GNU Octave`](https://www.gnu.org/software/octave/): Not directly used, but lot's of testing were done thanks to 
  GNU Octave
- Others...

Objectives
----------
The main reason for this program was to allow students to play with MLPs with
real functions, tests different configurations, etc.

There have been some real life applications (some even "cool"), that will be
properly described later on.

Building
--------
Building is as simple as (assuming you have a JDK properly installed):
- `git clone https://github.com/RiddlerArgentina/mrft.git`
- `cd mrft`
- `ant build-all`
