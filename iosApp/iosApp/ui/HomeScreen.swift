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
            VStack {
                SearchBar(
                    text: $searchText,
                    onSearch: {query in
                        homeViewModelHolder.viewModel.onSearch(query: query)
                    }
                )
                CollectionsRow(
                    collections: homeViewModelHolder.viewModel.uiState.value.collections,
                    selected: homeViewModelHolder.viewModel.uiState.value.selectedCollectionTitle,
                    onSelect: { title in
                        homeViewModelHolder.viewModel.onCollectionSelected(title: title!)
                        searchText = title!
                    })
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
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
            TextField("Search", text: $text)
                .onChange(of: text) { _,newValue in
                    onSearch(newValue)
                }
            if (!text.isEmpty) {
                Button {
                    text = ""
                    onSearch("")
                } label: {
                    Image(systemName: "close_icon")
                }
            }
        }
        .background(Color("main"))
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
