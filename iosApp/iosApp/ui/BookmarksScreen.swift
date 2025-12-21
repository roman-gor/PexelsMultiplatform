import SwiftUI
import Shared

private let koinKt = IOSKoinHelper()

struct BookmarksScreen: View {
    @State private var showContent = false
    @StateObject private var bookmarksViewModelHolder = ViewModelHolder(viewModel: koinKt.getBookmarksViewModel)
    @State private var bookmarks: [Bookmark] = []
    @Binding var selectedTab: AppTab
    var body: some View {
        NavigationStack {
            VStack {
                if !bookmarks.isEmpty {
                    BookmarksGrid(bookmarks: bookmarks)
                }
                else {
                    VStack(spacing: 5) {
                        Text(.emptyBookmarks)
                        Button(action: {
                            selectedTab = .home
                        }) {
                            Text(.explore)
                                .foregroundStyle(.choose)
                                .fontWeight(.medium)
                        }
                    }
                }
            }
            .navigationTitle(.bookmarksTitle)
            .navigationDestination(for: AppRoute.self) { route in
                routeView(route)
            }
            .onAppear() {
                bookmarksViewModelHolder.viewModel.loadBookmarks()
            }
            .task {
                for await bookmarks in bookmarksViewModelHolder.viewModel.bookmarks {
                    self.bookmarks = bookmarks
                }
                NSLog(bookmarks.description)
            }
        }
    }
}

struct BookmarksGrid: View {
    let bookmarks: [Bookmark]
    var body: some View {
        ScrollView(showsIndicators: false) {
            HStack {
                ForEach(0..<2, id: \.self) { column in
                    LazyVStack(spacing: 12) {
                        ForEach(columnBookmarks(column), id: \.id) { bookmark in
                            BookmarkItem(bookmark: bookmark)
                        }
                    }
                }
            }
        }
    }
    private func columnBookmarks(_ column: Int) -> [Bookmark] {
        bookmarks.enumerated()
            .filter { $0.offset % 2 == column }
            .map { $0.element }
    }
}

struct BookmarkItem: View {
    let bookmark: Bookmark
    var body: some View {
        if let rawId = bookmark.id,
           let imageUrlString = bookmark.imageUrl {
            let photoId = Int(truncating: bookmark.id!)
            NavigationLink(value: AppRoute.detailsFromBookmarks(id: photoId)) {
                VStack {
                    AsyncImage(url: URL(string: bookmark.imageUrl!)) { phase in
                        switch phase {
                        case .empty:
                            ProgressView()
                            
                        case .success(let image):
                            image
                                .resizable()
                                .scaledToFill()
                            
                        case .failure:
                            Image(.placeholder)
                                .cornerRadius(16)
                            
                        @unknown default:
                            Color.clear
                        }
                    }
                    .frame(maxWidth: .infinity)
                    .clipped()
                }
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color(.systemBackground))
                )
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .shadow(radius: 4)
                .padding(6)
            }
            .buttonStyle(.plain)
        }
    }
}
