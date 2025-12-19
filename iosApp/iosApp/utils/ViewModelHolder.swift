import Shared
import SwiftUI

class ViewModelHolder<VM: BaseViewModel>: ObservableObject {
    let viewModel: VM
    init(viewModel: VM) {
        self.viewModel = viewModel
    }
    deinit {
        viewModel.clear()
    }
}
