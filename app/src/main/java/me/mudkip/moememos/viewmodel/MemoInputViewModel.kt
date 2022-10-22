package me.mudkip.moememos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import me.mudkip.moememos.data.model.Memo
import me.mudkip.moememos.data.repository.MemoRepository
import javax.inject.Inject

@HiltViewModel
class MemoInputViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {
    suspend fun createMemo(content: String): ApiResponse<Memo> {
        return viewModelScope.async(Dispatchers.IO) {
            return@async memoRepository.createMemo(content)
        }.await()
    }
}