enum AppRoute: Hashable {
    case detailsFromHome(url: String, name: String)
    case detailsFromBookmarks(id: Int)
    case homeFromBookmarks
}
