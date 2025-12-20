import SwiftUI

struct BookmarksTab: View {
    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            BookmarksScreen(
                onNavigateToDetail: { id in
                    path.append(
                        AppRoute.photoDetail(
                            id: id,
                            url: nil,
                            name: nil
                        )
                    )
                }
            )
            .toolbarBackground(.visible, for: .tabBar)
            .toolbarBackground(Color.red, for: .tabBar)
            .navigationDestination(for: AppRoute.self) { route in
                switch route {
                case let .photoDetail(id, url, name):
                    DetailsScreen(
                        passedId: id,
                        passedUrl: url,
                        passedName: name,
                        onBackClick: {
                            path.removeLast()
                        }
                    )
                default:
                    EmptyView()
                }
            }
        }
    }
}
