package com.techawarenessma.app.navigation

/** One route per website page, plus the member profile and certification sections. */
object Routes {
    const val HOME = "home"
    const val PROGRAMS = "programs"
    const val CHAPTERS = "chapters"
    const val MEMBERS = "members"
    const val MORE = "more"

    const val START_CHAPTER = "start-chapter"
    const val GET_INVOLVED = "get-involved"
    const val DONATE = "donate"
    const val ABOUT = "about"
    const val PARTNERS = "partners"
    const val RESEARCH = "research"
    const val PROJECTS = "projects"
    const val CONTACT = "contact"
    const val PRIVACY = "privacy"
    const val CERTIFICATION = "certification"

    const val ARG_SECTION = "section"
    const val ARG_ID = "id"

    /** Programs can open scrolled to a section, like the site's `Programs.dc.html#seniors`. */
    const val PROGRAMS_PATTERN = "$PROGRAMS?$ARG_SECTION={$ARG_SECTION}"
    const val MEMBER_PATTERN = "member/{$ARG_ID}"
    const val CERT_SECTION_PATTERN = "$CERTIFICATION/{$ARG_SECTION}"

    fun programs(section: String) = "$PROGRAMS?$ARG_SECTION=$section"
    fun member(id: String) = "member/$id"
    fun certSection(id: String) = "$CERTIFICATION/$id"
}

enum class Tab(val route: String, val pattern: String, val label: String) {
    Home(Routes.HOME, Routes.HOME, "Home"),
    Programs(Routes.PROGRAMS, Routes.PROGRAMS_PATTERN, "Programs"),
    Chapters(Routes.CHAPTERS, Routes.CHAPTERS, "Chapters"),
    Members(Routes.MEMBERS, Routes.MEMBERS, "Members"),
    More(Routes.MORE, Routes.MORE, "More"),
    ;

    companion object {
        /** The tab a route belongs to when it is a tab root, e.g. "programs?section=ewaste". */
        fun forRoute(route: String?): Tab? {
            if (route == null) return null
            val base = route.substringBefore('?')
            return entries.firstOrNull { it.route == base }
        }
    }
}

/** Screens drawn without the bottom bar: the certification platform is its own space. */
fun isImmersive(routePattern: String?): Boolean =
    routePattern == Routes.CERTIFICATION || routePattern == Routes.CERT_SECTION_PATTERN
