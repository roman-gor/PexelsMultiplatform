import SwiftUI

struct HomeTab: View {
    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            HomeScreen(
                onPhotoClick: { url, name in
                    path.append(
                        AppRoute.photoDetail(
                            id: nil,
                            url: url,
                            name: name
                        )
                    )
                }
            )
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
