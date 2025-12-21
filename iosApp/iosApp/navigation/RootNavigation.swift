import SwiftUI

struct RootNavigationView: View {
    var body: some View {
        TabView {
            HomeScreen()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
            BookmarksScreen()
                .tabItem {
                    Label("Bookmarks", systemImage: "bookmark.fill")
                }
        }
        .tint(.black)
    }
}
