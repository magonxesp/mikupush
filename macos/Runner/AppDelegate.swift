import Cocoa
import FlutterMacOS

@main
class AppDelegate: FlutterAppDelegate {
    override func applicationShouldTerminateAfterLastWindowClosed(_ sender: NSApplication) -> Bool {
        return false
    }
    
    override func applicationSupportsSecureRestorableState(_ app: NSApplication) -> Bool {
        return true
    }
    
    override func applicationShouldHandleReopen(_ sender: NSApplication, hasVisibleWindows flag: Bool) -> Bool {
        if let window = sender.windows.first {
            if flag {
                window.orderFront(nil)
            } else {
                window.makeKeyAndOrderFront(nil)
            }
        }
        
        return true
    }
    
    override func applicationDidFinishLaunching(_ aNotification: Notification) {
        super.applicationDidFinishLaunching(aNotification)
        NSUpdateDynamicServices()
    }

    @objc func openFileFromService(_ pboard: NSPasteboard, userData: String?, error: NSErrorPointer) {
        NSLog("File request upload received")
        
        guard let files = pboard.propertyList(forType: NSPasteboard.PasteboardType.fileURL) as? [String] else {
            NSLog("Files not found for request upload")
            return
        }

        guard let file = files.first else {
            NSLog("File not found for request upload")
            return
        }

        guard let flutterViewController = self.mainFlutterWindow?.contentViewController as? FlutterViewController else {
            NSLog("Failed file upload request through service triggered for \(file): flutter view controller is not available")
            return
        }

        let messenger = flutterViewController.engine.binaryMessenger
        let channel = FlutterMethodChannel(
            name: "io.mikupush.app/upload_file_service",
            binaryMessenger: messenger
        )

        channel.invokeMethod("onUploadRequestReceived", arguments: file)
        NSLog("File request upload channel invoked")
    }
}


