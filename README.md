# MikuPush

Share screenshots or whatever.

## Build installer

### Windows

⚠️ **The installer can be built only on Windows.** ⚠️

Install [Inno Setup](https://jrsoftware.org/isdl.php) and add It to the `PATH` environment variable.

Create `.exe` of the `.bat` launch scripts with **Bat To Exe Coverter**, it is in the `bin` directory.
If the launch scripts are converted to `.exe` and you don't make any changes on the scripts you don't need
to convert then again.

Open it and select the `app/launcher/windows/launcher.bat` script and covert it to `.exe`, then convert the 
`app/launcher/windows/upload-request.bat` to `.exe` too.

Now you will have the following directory tree:

```
app
└───launcher
    └───windows
        ├───icon.ico
        ├───launcher.bat
        ├───MikuPush.exe <-- Converted from launcher.bat
        ├───MikuPush-Requester.exe <-- Converted from upload-request.bat
        └───upload-request.bat
```

Launch the `distWindows` Gradle task.

```sh
$ ./gradlew :app:distWindows
```

Then you have to create the installer with Inno Setup

TODO: inno setup from the command line