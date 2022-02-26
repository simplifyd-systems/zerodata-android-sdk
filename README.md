# Zerodata

This app allows users to connect to the closest Edge network to browse apps and websites Data-Free.
You can find more information [here](https://zerodata.app/)


## Get started
Read about [OpenVPN](https://github.com/schwabe/ics-openvpn/blob/master/doc/README.txt)

## Prerequisites
To successfully build and run this project on an Android device or emulator kindly do the following:

1. Install NDK version **21.0.6113669**, follow the following steps to ensure you download the right version:
      * SDK Manager -> SDK Tools -> Select Show Package Details -> Select NDK -> version 21.0.6113669 -> Click Apply to install
      * If that doesn’t work do the following:
           * Download Zip file [here](https://dl.google.com/android/repository/android-ndk-r21-darwin-x86_64.zip )
           * Unzip it and rename it to *21.0.6113669*
           * Copy and paste the package to Macintosh HD/Users/<your username>/library/android/sdk/ndk
 2. Install CMake version  3.6.4, follow the following steps to ensure you download the right version:
     * SDK Manager -> SDK Tools -> Select Show Package Details -> Select CMake -> version  3.6.4 -> Click Apply to install
     * If that doesn’t work do the following:
          * Download Zip file [here](https://dl.google.com/android/repository/cmake-3.6.4111459-darwin-x86_64.zip  )
          * Unzip it and rename it to *3.6.4111459*
          * Copy and paste the package to Macintosh HD/Users/<your username>/library/android/sdk/cmake
3. Install [Swig](http://www.swig.org/]) using [homebrew](https://brew.sh/)
    * Open terminal run `ruby-e"$(curl-fsSLhttps://raw.githubusercontent.com/Homebrew/install/master/install)"2>/dev/null` & press enter, wait for the command to finish
    * Run `brew install swig@3`

If after installation you keep getting a file not found error while trying to build the project. A likely issue is that gradle can't find swig in the environment. You can fix it by guiding gradle to the directory where swig is installed or just copying swig to the directory gradle looks at (easier):
* Run `brew info swig@3`
* Copy the contents of the path printed (it'll probably be like `/opt/homebrew/Cellar/swig@3/3.0.12`).
* On Mac use **CMD+Shift+G** paste the path to open in Finder where Swig is installed
* Open  `/usr/local/bin/` on another finder window
* Then copy `3.0.12\bin\swig` to `/usr/local/bin/`
* Also copy `3.0.12\share` to `/usr/local/`
* Restart your IDE and try to run the project

You *should* be able to build and run the project in the Android device or emulator.
