# mrft (mlp real function trainer)
Small GUI used for training MLP with real functions.

Libraries used
--------------
- [`libai`](https://github.com/kronenthaler/libai): MLP implementation
- [`exp4j`](https://github.com/dktcoding/exp4j): Populating datasets,
transformations and noise
- [`jDrawingLib`](https://github.com/dktcoding/jdrawinglib): Plotting functions,
and creating images and animations
- [`FileDrop`](http://iharder.sourceforge.net/current/java/filedrop/): Drag & Drop support
- [`Java ECG Generator`](http://www.mit.edu/~gari/CODE/ECGSYN/JAVA/APPLET2/ecgsyn/ecg-java/source.html): Synthetic ECG (training example)
- Others (that I will name in time)

Objectives
----------
The main reason for this program was to allow students to play with MLPs with
real functions, tests different configurations, etc.

There have been some real life applications (some even "cool"), that will be
properly described later on.

Building
--------
Building is as simple as (assuming you have a JDK properly installed):
- `git clone https://github.com/dktcoding/mrft.git`
- `cd mrft`
- `ant build-all`
