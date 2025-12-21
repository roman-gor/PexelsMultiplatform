import SwiftUI

@ViewBuilder
func routeView(_ route: AppRoute) -> some View {
    switch(route) {
    case .detailsFromHome(let url, let name):
        DetailsScreen(passedId: nil, passedUrl: url, passedName: name)
    case .detailsFromBookmarks(let id):
        DetailsScreen(passedId: id, passedUrl: nil, passedName: nil)
    case .homeFromBookmarks:
        HomeScreen()
    }
}
