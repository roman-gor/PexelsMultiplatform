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
        ZStack {
            Color("colorBackground")
                .ignoresSafeArea()
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
                    onPhotoClick: { _, _ in },
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
                    Image("close_icon")
                }
                .padding(.leading, 5)
                .padding(.trailing, 20)
            }
        }
        .background(
            RoundedRectangle(cornerRadius: 36)
                .fill(Color("main"))
        )
        .padding(.horizontal, 20)
        .padding(.vertical, 20)
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
        .frame(height: 47)
    }
}

struct CollectionChip: View {
    let title: String
    let selected: Bool
    let onClick: () -> Void
    var body: some View {
        Button(action: {
            onClick()
        }) {
            Text(title)
                .fontWeight(.semibold)
                .foregroundColor(selected ? .white : Color("collectionsText"))
                .padding(.horizontal, 16)
                .padding(.vertical, 6)
                .background(
                    RoundedRectangle(cornerRadius: 20)
                        .fill(selected ? Color("choose") : Color("main"))
                )
        }
        .buttonStyle(.plain)
    }
}

struct PhotosGrid: View {
    let photos: [Photo]
    let onPhotoClick: (String, String) -> Void
    let onLoadMore: () -> Void

    var body: some View {
        ScrollView(showsIndicators: false) {
            HStack(alignment: .top, spacing: 12) {
                ForEach(0..<2, id: \.self) { column in
                    LazyVStack(spacing: 12) {
                        ForEach(columnPhotos(column), id: \.id) { photo in
                            PhotoItem(photo: photo, onPhotoClick: onPhotoClick)
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
            .padding(.top, 8)
            .padding(.bottom, 72)
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
    let onPhotoClick: (String, String) -> Void

    var body: some View {
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
                    Color.gray

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
        .onTapGesture {
            let encodedUrl = photo.src.large2x.addingPercentEncoding(
                withAllowedCharacters: .urlQueryAllowed
            ) ?? photo.src.large2x

            onPhotoClick(encodedUrl, photo.photographer)
        }
    }
}
