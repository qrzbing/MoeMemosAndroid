package me.mudkip.moememos.ui.page.memos

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import me.mudkip.moememos.ui.component.MemosCard
import me.mudkip.moememos.viewmodel.LocalMemos
import timber.log.Timber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MemosList(
    contentPadding: PaddingValues,
    swipeEnabled: Boolean = true,
    tag: String? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = LocalMemos.current
    val refreshState = rememberSwipeRefreshState(viewModel.refreshing)
    val filteredMemos = remember(viewModel.memos.toList()) {
        val pinned = viewModel.memos.filter { it.pinned }
        val nonPinned = viewModel.memos.filter { !it.pinned }
        var fullList = pinned + nonPinned

        tag?.let { tag ->
            fullList = fullList.filter { memo ->
                memo.content.contains("#$tag") ||
                    memo.content.contains("#$tag/")
            }
        }

        fullList
    }

    SwipeRefresh(
        indicatorPadding = contentPadding,
        state = refreshState,
        swipeEnabled = swipeEnabled,
        onRefresh = {
            coroutineScope.launch {
                viewModel.refresh()
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.consumedWindowInsets(contentPadding),
            contentPadding = contentPadding
        ) {
            items(filteredMemos, key = { it.id }) { memo ->
                MemosCard(memo)
            }
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Timber.d(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMemos()
    }
}