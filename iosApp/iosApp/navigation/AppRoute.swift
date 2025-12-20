enum AppRoute: Hashable {
    case home
    case bookmarks
    case photoDetail(
        id: Int?,
        url: String?,
        name: String?
    )
}
