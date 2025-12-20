import SwiftUI

struct RootNavigationView: View {
    var body: some View {
        TabView {
            HomeTab()
                .tabItem {
                    Image(.homeButtonInactive)
                    Text("Home")
                }
            BookmarksTab()
                .tabItem {
                    Image(.bookmarkButtonInactive)
                    Text("Bookmarks")
                }
        }
    }
}
