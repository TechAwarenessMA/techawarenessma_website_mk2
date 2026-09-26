package com.techawarenessma.app.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techawarenessma.app.R
import com.techawarenessma.app.certification.CertificationStore
import com.techawarenessma.app.navigation.AndroidAppActions
import com.techawarenessma.app.navigation.Routes
import com.techawarenessma.app.navigation.Tab
import com.techawarenessma.app.navigation.isImmersive
import com.techawarenessma.app.ui.certification.CertificationHomeScreen
import com.techawarenessma.app.ui.certification.CertificationSectionScreen
import com.techawarenessma.app.ui.components.PillButton
import com.techawarenessma.app.ui.components.PillStyle
import com.techawarenessma.app.ui.components.TaaIcons
import com.techawarenessma.app.ui.screens.AboutScreen
import com.techawarenessma.app.ui.screens.ChaptersScreen
import com.techawarenessma.app.ui.screens.ContactScreen
import com.techawarenessma.app.ui.screens.DonateScreen
import com.techawarenessma.app.ui.screens.GetInvolvedScreen
import com.techawarenessma.app.ui.screens.HomeScreen
import com.techawarenessma.app.ui.screens.MemberProfileScreen
import com.techawarenessma.app.ui.screens.MembersScreen
import com.techawarenessma.app.ui.screens.MoreScreen
import com.techawarenessma.app.ui.screens.PartnersScreen
import com.techawarenessma.app.ui.screens.PrivacyScreen
import com.techawarenessma.app.ui.screens.ProgramsScreen
import com.techawarenessma.app.ui.screens.ProjectsScreen
import com.techawarenessma.app.ui.screens.ResearchScreen
import com.techawarenessma.app.ui.screens.StartChapterScreen
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaType

/** Title shown in the top bar for pushed (non-tab) screens. */
private fun titleFor(routePattern: String?): String = when (routePattern) {
    Routes.START_CHAPTER -> "Start a Chapter"
    Routes.GET_INVOLVED -> "Get Involved"
    Routes.DONATE -> "Donate"
    Routes.ABOUT -> "About & Why Repair"
    Routes.PARTNERS -> "Partners & Sponsors"
    Routes.RESEARCH -> "Research"
    Routes.PROJECTS -> "Our apps"
    Routes.CONTACT -> "Contact"
    Routes.PRIVACY -> "Privacy policy"
    Routes.MEMBER_PATTERN -> "Members"
    else -> ""
}

@Composable
fun TaaApp(certificationStore: CertificationStore) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val actions = remember(navController, context) { AndroidAppActions(context, navController) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val pattern = backStackEntry?.destination?.route
    val immersive = isImmersive(pattern)

    // Pushed screens stay under the tab they were opened from, so only tab roots move it.
    var selectedTab by rememberSaveable { mutableStateOf(Tab.Home) }
    LaunchedEffect(pattern) { Tab.forRoute(pattern)?.let { selectedTab = it } }

    // The certification platform has a dark header; flip status bar icons to stay legible.
    val view = LocalView.current
    LaunchedEffect(immersive) {
        val window = view.context.findActivity()?.window ?: return@LaunchedEffect
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !immersive
    }

    CompositionLocalProvider(LocalAppActions provides actions) {
        Scaffold(
            containerColor = TaaColors.Cream,
            contentColor = TaaColors.Slate,
            // Bars apply their own insets; immersive screens handle theirs.
            contentWindowInsets = WindowInsets(0),
            topBar = {
                if (!immersive) {
                    TaaTopBar(
                        routePattern = pattern,
                        onBack = actions::back,
                        onHome = { actions.navigate(Routes.HOME) },
                        onDonate = { actions.navigate(Routes.DONATE) },
                    )
                }
            },
            bottomBar = {
                if (!immersive) {
                    TaaBottomBar(selected = selectedTab) { tab ->
                        if (tab == selectedTab) {
                            // Re-tapping the current tab returns to its root.
                            navController.popBackStack(tab.pattern, inclusive = false)
                        } else {
                            actions.navigate(tab.route)
                        }
                    }
                }
            },
        ) { innerPadding ->
            // Content sits between the (opaque) bars rather than scrolling beneath them, so
            // anything scrolled into view — by touch or by TalkBack — is actually visible.
            TaaNavHost(
                navController = navController,
                certificationStore = certificationStore,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
            )
        }
    }
}

@Composable
private fun TaaNavHost(
    navController: NavHostController,
    certificationStore: CertificationStore,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier,
        enterTransition = { fadeIn(tween(180)) },
        exitTransition = { fadeOut(tween(180)) },
    ) {
        composable(Routes.HOME) { HomeScreen() }
        composable(
            Routes.PROGRAMS_PATTERN,
            arguments = listOf(
                navArgument(Routes.ARG_SECTION) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { entry -> ProgramsScreen(initialSection = entry.arguments?.getString(Routes.ARG_SECTION)) }
        composable(Routes.CHAPTERS) { ChaptersScreen() }
        composable(Routes.MEMBERS) { MembersScreen() }
        composable(Routes.MORE) { MoreScreen() }

        composable(Routes.START_CHAPTER) { StartChapterScreen() }
        composable(Routes.GET_INVOLVED) { GetInvolvedScreen() }
        composable(Routes.DONATE) { DonateScreen() }
        composable(Routes.ABOUT) { AboutScreen() }
        composable(Routes.PARTNERS) { PartnersScreen() }
        composable(Routes.RESEARCH) { ResearchScreen() }
        composable(Routes.PROJECTS) { ProjectsScreen() }
        composable(Routes.CONTACT) { ContactScreen() }
        composable(Routes.PRIVACY) { PrivacyScreen() }
        composable(
            Routes.MEMBER_PATTERN,
            arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.StringType }),
        ) { entry ->
            MemberProfileScreen(
                memberId = entry.arguments?.getString(Routes.ARG_ID),
                onOpenMember = { id ->
                    // "Next:" replaces the profile, so Back still returns to the grid.
                    navController.navigate(Routes.member(id)) {
                        popUpTo(Routes.MEMBER_PATTERN) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.CERTIFICATION) {
            CertificationHomeScreen(
                store = certificationStore,
                onOpenSection = { section -> navController.navigate(Routes.certSection(section.id)) },
                onClose = { navController.popBackStack() },
            )
        }
        composable(
            Routes.CERT_SECTION_PATTERN,
            arguments = listOf(navArgument(Routes.ARG_SECTION) { type = NavType.StringType }),
        ) { entry ->
            CertificationSectionScreen(
                store = certificationStore,
                sectionId = entry.arguments?.getString(Routes.ARG_SECTION),
                onOpenSection = { section ->
                    navController.navigate(Routes.certSection(section.id)) {
                        popUpTo(Routes.CERT_SECTION_PATTERN) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun TaaTopBar(
    routePattern: String?,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onDonate: () -> Unit,
) {
    val isTabRoot = Tab.forRoute(routePattern) != null
    Surface(color = TaaColors.Cream, contentColor = TaaColors.Slate) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isTabRoot) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(role = Role.Button, onClickLabel = "Go to home", onClick = onHome)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo_taa),
                        contentDescription = "Tech Awareness Association logo",
                        modifier = Modifier.size(38.dp),
                    )
                    Column {
                        Text("TECH AWARENESS", style = TaaType.Label.copy(letterSpacing = TaaType.Label.letterSpacing * 1.4f))
                        Text("ASSOCIATION", style = TaaType.Label.copy(letterSpacing = TaaType.Label.letterSpacing * 1.4f))
                    }
                }
            } else {
                IconButton(onClick = onBack) {
                    Icon(TaaIcons.ArrowBack, contentDescription = "Back")
                }
                Text(
                    titleFor(routePattern),
                    style = TaaType.Title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).semantics { heading() },
                )
            }
            if (isTabRoot) Spacer(Modifier.weight(1f))
            if (routePattern != Routes.DONATE) {
                PillButton("Donate", onClick = onDonate, style = PillStyle.Dark, modifier = Modifier.padding(end = 8.dp))
            }
        }
    }
}

private val Tab.icon: ImageVector
    get() = when (this) {
        Tab.Home -> TaaIcons.Home
        Tab.Programs -> TaaIcons.Build
        Tab.Chapters -> TaaIcons.Place
        Tab.Members -> TaaIcons.Group
        Tab.More -> TaaIcons.Menu
    }

@Composable
private fun TaaBottomBar(selected: Tab, onSelect: (Tab) -> Unit) {
    Column {
        HorizontalDivider(color = TaaColors.Sand)
        NavigationBar(containerColor = TaaColors.Cream, tonalElevation = 0.dp) {
            Tab.entries.forEach { tab ->
                NavigationBarItem(
                    selected = tab == selected,
                    onClick = { onSelect(tab) },
                    icon = { Icon(tab.icon, contentDescription = null) },
                    label = { Text(tab.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TaaColors.Ink,
                        selectedTextColor = TaaColors.Ink,
                        indicatorColor = TaaColors.Yellow,
                        unselectedIconColor = TaaColors.Slate,
                        unselectedTextColor = TaaColors.Slate,
                    ),
                )
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
