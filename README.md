# keyboardSimulator

## Pre-requisites

Download the `fr_ngrams.bin` and `fr_words.bin` files from the [predict4all repo](https://github.com/mthebaud/predict4all#installation) and add them to the `resources` folder.


## IDE configuration

Maven is used to manage dependencies on the project, so a bit of configuration is required to get your IDE set up with it.

### Eclipse

1. Install the [eclipse-m2e](https://github.com/eclipse-m2e/m2e-core/blob/master/README.md#-installation) plugin.

2. Open eclipse and click `File > Import`
3. Select `Maven > Existing Maven projects`
4. Select path to project as Root Directory then click `Finish`
5. Right-click project and select `Run as > 2 Maven build`

### Intellij

1. Click `File > Open` and select path to project
2. Richt-click project and select `Maven > Reload project`