package uk.co.ianfield.devstat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uk.co.ianfield.devstat.ui.components.InformationPage
import uk.co.ianfield.devstat.ui.theme.DevStatTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevStatTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val helper = remember { StatHelper(context) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Optimization: remember the tabs list to avoid expensive computation on every recomposition.
    // This prevents the "hang" or lag during UI interactions like swiping or clicking.
    val tabs = remember(helper) {
        listOf(
            R.string.title_screen_metrics to helper.screenList,
            R.string.title_software to helper.softwareList,
            R.string.title_hardware to helper.hardwareList,
            R.string.title_features to helper.featureList,
            R.string.title_crypto to helper.cryptoList
        )
    }

    val pagerState = rememberPagerState(pageCount = { tabs.size })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Implement share functionality
                val shareText = tabs.joinToString("\n\n") { (titleRes, items) ->
                    "${context.getString(titleRes)}:\n" + items.joinToString("\n") { it.toString() }
                }
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }) {
                Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share_icon))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 16.dp
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = { Text(stringResource(tab.first)) }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                InformationPage(
                    items = tabs[pageIndex].second,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
