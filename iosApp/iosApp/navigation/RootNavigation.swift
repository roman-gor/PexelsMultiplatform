import SwiftUI

struct RootNavigationView: View {
    @State private var selectedTab: AppTab = .home
    var body: some View {
        TabView(selection: $selectedTab) {
            HomeScreen()
                .tabItem {
                    Label(.homeTitle, systemImage: "house.fill")
                }
                .tag(AppTab.home)
            BookmarksScreen(selectedTab: $selectedTab)
                .tabItem {
                    Label(.bookmarksTitle, systemImage: "bookmark.fill")
                }
                .tag(AppTab.bookmarks)
        }
    }
}
