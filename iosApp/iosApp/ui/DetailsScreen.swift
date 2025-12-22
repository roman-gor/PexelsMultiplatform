import SwiftUI
import Shared

private let koinKt = IOSKoinHelper()

struct DetailsScreen: View {
    let passedId: Int?
    let passedUrl: String?
    let passedName: String?
    @StateObject private var detailsViewModelHolder = ViewModelHolder(viewModel: koinKt.getDetailsViewModel)
    @State private var bookmarkFromDb: Bookmark? = nil
    @State private var uiState: DetailsUiState = DetailsUiState.Idle.shared
    private var isBookmarked: Bool {
        if let successState = uiState as? DetailsUiState.Success {
            return successState.isBookmark
        }
        return false
    }
    @State private var currentUrl = ""
    @State private var currentName = ""
    var body: some View {
        ZStack {
            Color(.colorBackground)
            VStack {
                Spacer()
                AsyncImage(url: URL(string: currentUrl)) { phase in
                    switch phase {
                    case .empty:
                        ProgressView()
                            .frame(height: 300)
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFit()
                    case .failure:
                        Image(.placeholder)
                            .resizable()
                            .scaledToFit()
                            .frame(height: 300)
                    @unknown default:
                        Color.clear
                    }
                }
                .frame(maxWidth: .infinity)
                .background(Color.gray.opacity(0.3))
                .clipShape(RoundedRectangle(cornerRadius: 14))
                Spacer()
                HStack {
                    Spacer()
                    Button {
                        if !currentUrl.isEmpty {
                            if isBookmarked {
                                detailsViewModelHolder.viewModel.deleteByUrl(url: currentUrl)
                            } else {
                                detailsViewModelHolder.viewModel.addBookmark(imageUrl: currentUrl, name: currentName)
                            }
                        }
                    } label: {
                        Image(uiImage: isBookmarked ? .bookmarkDetailsActive : .bookmarkDetailsInactive)
                    }
                    .frame(width: 60, height: 60)
                }
                .padding(.trailing, 10)
            }
            .padding(.horizontal, 25)
        }
        .task {
            if let url = passedUrl { self.currentUrl = url }
            if let name = passedName { self.currentName = name }
            if let id = passedId {
                detailsViewModelHolder.viewModel.findBookmarkById(imageId: Int32(id))
                Task {
                    for await bookmark in detailsViewModelHolder.viewModel.bookmark {
                        if let b = bookmark {
                            self.bookmarkFromDb = b
                            self.currentUrl = b.imageUrl ?? ""
                            self.currentName = b.phName ?? ""
                        }
                    }
                }
                NSLog("\(currentUrl), \(currentName)")
            } else if let url = passedUrl {
                detailsViewModelHolder.viewModel.searchInDBOnce(url: url)
                Task {
                    for await bookmark in detailsViewModelHolder.viewModel.bookmark {
                        if let b = bookmark {
                            self.bookmarkFromDb = b
                            self.currentName = b.phName ?? self.currentName
                        } else {
                            self.bookmarkFromDb = nil
                        }
                    }
                }
                NSLog("\(isBookmarked)")
            }
            Task {
                for await state in detailsViewModelHolder.viewModel.uiState {
                    self.uiState = state
                    if let errorState = state as? DetailsUiState.Error {
                        print("Error received: \(errorState.e)")
                    }
                }
            }
        }
        .navigationTitle(!currentName.isEmpty ? currentName : String(localized: .detailsTitle))
        .toolbar(.hidden, for: .tabBar)
    }
}
