import SwiftUI
import Shared

private let koinKt = IOSKoinHelper()

struct HomeScreen: View {
    @State private var showContent = false
    @StateObject private var homeViewModelHolder = ViewModelHolder(viewModel: koinKt.getHomeViewModel)
    @State private var uiState: HomeUiState =
    HomeUiState(
        photos: [],
        collections: [],
        loadState: PhotoLoadStateIdle(),
        selectedCollectionTitle: nil,
        currentQuery: nil,
        noResults: false)
    @State private var searchText = ""
    var body: some View {
        NavigationStack {
            ZStack {
                Color("colorBackground")
                VStack(spacing: 0) {
                    SearchBar(
                        text: $searchText,
                        onSearch: { query in
                            homeViewModelHolder.viewModel.onSearch(query: query)
                        }
                    )

                    CollectionsRow(
                        collections: uiState.collections,
                        selected: uiState.selectedCollectionTitle,
                        onSelect: { title in
                            homeViewModelHolder.viewModel.onCollectionSelected(title: title!)
                            searchText = title!
                        }
                    )

                    PhotosGrid(
                        photos: uiState.photos,
                        onLoadMore: {
                            homeViewModelHolder.viewModel.loadMore()
                        }
                    )
                    .frame(maxWidth: .infinity, maxHeight: .infinity) // ← ВАЖНО
                }
            }
            .onAppear {
                homeViewModelHolder.viewModel.onSearch(query: nil)
            }
            .task {
                for await state in homeViewModelHolder.viewModel.uiState {
                    self.uiState = state
                }
                NSLog(uiState.photos.description)
            }
            .navigationDestination(for: AppRoute.self) { route in
                routeView(route)
            }
        }
    }
}

struct SearchBar: View {
    @Binding var text: String
    let onSearch: (String?) -> Void
    var body: some View {
        HStack {
            Image("search_icon")
                .padding(.leading, 20)
                .padding(.trailing, 5)
            TextField("Search", text: $text)
                .onChange(of: text) { _,newValue in
                    onSearch(newValue)
                }
                .padding(.vertical, 10)
            if (!text.isEmpty) {
                Button {
                    text = ""
                    onSearch("")
                } label: {
                    Image("close_icon").foregroundColor(Color("searchHintColor"))
                }
                .padding(.leading, 5)
                .padding(.trailing, 20)
            }
        }
        .frame(height: 55)
        .background(
            RoundedRectangle(cornerRadius: 36)
                .fill(Color("main"))
        )
        .padding(.horizontal, 20)
        .padding(.top, 10)
    }
}

struct CollectionsRow: View {
    let collections: [Collection]
    let selected: String?
    let onSelect: (String?) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 15) {
                ForEach(collections, id: \.title) { collection in
                    CollectionChip(
                        title: collection.title,
                        selected: collection.title == selected,
                        onClick: { onSelect(collection.title) }
                    )
                }
            }
            .padding(.horizontal, 20)
        }
        .frame(height: 30)
        .padding(.vertical, 20)
    }
}


struct CollectionChip: View {
    let title: String
    let selected: Bool
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            Text(title)
                .font(.system(size: 14))
                .frame(height: 30)
                .foregroundColor(selected ? .white : Color("collectionsText"))
                .padding(.horizontal, 16)
                .padding(.vertical, 6)
        }
        .background(
            RoundedRectangle(cornerRadius: 36)
                .fill(selected ? Color("choose") : Color("main"))
        )
        .buttonStyle(.plain)
    }
}


struct PhotosGrid: View {
    let photos: [Photo]
    let onLoadMore: () -> Void

    var body: some View {
        ScrollView(showsIndicators: false) {
            HStack(alignment: .top, spacing: 12) {
                ForEach(0..<2, id: \.self) { column in
                    LazyVStack(spacing: 12) {
                        ForEach(columnPhotos(column), id: \.id) { photo in
                            PhotoItem(photo: photo)
                                .onAppear {
                                    if photo.id == photos.last?.id {
                                        onLoadMore()
                                    }
                                }
                        }
                    }
                }
            }
            .padding(.horizontal, 20)
        }
    }

    private func columnPhotos(_ column: Int) -> [Photo] {
        photos.enumerated()
            .filter { $0.offset % 2 == column }
            .map { $0.element }
    }
}

struct PhotoItem: View {
    let photo: Photo

    var body: some View {
        let encodedUrl = photo.src.large2x.addingPercentEncoding(
            withAllowedCharacters: .urlQueryAllowed
        ) ?? photo.src.large2x
        NavigationLink(value: AppRoute.detailsFromHome(url: encodedUrl, name: photo.photographer)) {
            VStack {
                AsyncImage(url: URL(string: photo.src.medium)) { phase in
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
