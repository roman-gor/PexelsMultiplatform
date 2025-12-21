import SwiftUI
import Shared

struct DetailsScreen: View {
    let passedId: Int?
    let passedUrl: String?
    let passedName: String?
    var body: some View {
        VStack {
            if let url = passedUrl {
                Text(passedUrl! + " " + passedName!)
            } else if let id = passedId {
                Text("Id")
            }
        }
        .navigationTitle(passedName ?? String(localized: .detailsTitle))
        .toolbar(.hidden, for: .tabBar)
    }
}
