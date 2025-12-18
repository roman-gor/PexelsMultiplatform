import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        InitKoinKt.doInitKoin(config: nil)
        NSLog("Koin init")
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
