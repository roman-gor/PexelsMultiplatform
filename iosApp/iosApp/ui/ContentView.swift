import SwiftUI
import Shared

private let koinKt = IOSKoinHelper()

struct ContentView: View {
    @State private var showContent = false
    private let homeViewModel = koinKt.getHomeViewModel
    @State private var uiState: HomeUiState =
    HomeUiState(
        photos: [],
        collections: [],
        loadState: PhotoLoadStateIdle(),
        selectedCollectionTitle: nil,
        currentQuery: nil,
        noResults: false)
    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation {
                    showContent = !showContent
                }
            }
            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    Text("SwiftUI: \(Greeting().greet())")
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
        .onAppear {
            homeViewModel.onSearch(query: nil)
        }
        .task {
            for await state in homeViewModel.uiState {
                self.uiState = state
            }
            NSLog(uiState.photos.description)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
