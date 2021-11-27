- Please read (this)[https://github.com/schwabe/ics-openvpn/blob/master/doc/README.txt]
- Install NDK 21.0.6113669 and CMake following the instructions (here)[https://developer.android.com/studio/projects/install-ndk]
- Ensure you're installing `NDK 21.0.6113669` by   selecting `show package details` on the SDK manager.
- Download and install CMake version 3.6.4 using the SDK manager. Make sure to install 3.6.4 by   selecting `show package details` on the SDK manager.
- Install (swig)[http://www.swig.org/] using homebrew by running ```brew install swig@3```
  - Swig can be quite weird on Mac OS. If after installation you keep getting a `file not found` error while trying to build the project. A likely issue is that gradle can't find swig in the enviroment. You can fix by guiding gradle to the directory where swig is installed or just copying swig to the directory gradle looks at (easier).
  - Run `brew info swig@30`. Copy the contents of the path printed (it'll probably be ```/opt/homebrew/Cellar/swig@3/3.0.12```).
    - `3.0.12\bin\swig` to `/usr/local/bin/`
    - `3.0.12\share` to `/usr/local/`

- Add the swig executable to path and ensure it can be found by typing `swig -version` into terminal. This should print out the installed version of swig. Idealy 3.0.12.

- Restart your IDE and try to run the project
