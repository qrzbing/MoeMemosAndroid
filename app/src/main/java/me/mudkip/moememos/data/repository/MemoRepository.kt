package me.mudkip.moememos.data.repository

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapSuccess
import me.mudkip.moememos.data.api.CreateMemoInput
import me.mudkip.moememos.data.api.MemosApiService
import me.mudkip.moememos.data.api.PatchMemoInput
import me.mudkip.moememos.data.api.UpdateMemoOrganizerInput
import me.mudkip.moememos.data.model.Memo
import me.mudkip.moememos.data.model.MemosRowStatus
import javax.inject.Inject

class MemoRepository @Inject constructor(private val memosApiService: MemosApiService) {
    suspend fun loadMemos(rowStatus: MemosRowStatus? = null): ApiResponse<List<Memo>> = memosApiService.call { api ->
        api.listMemo(rowStatus = rowStatus).mapSuccess { data }
    }

    suspend fun createMemo(content: String): ApiResponse<Memo> = memosApiService.call { api ->
        api.createMemo(CreateMemoInput(content)).mapSuccess { data }
    }

    suspend fun getTags(): ApiResponse<List<String>> = memosApiService.call { api ->
        api.getTags().mapSuccess { data }
    }

    suspend fun updatePinned(memoId: Long, pinned: Boolean): ApiResponse<Memo> = memosApiService.call { api ->
        api.updateMemoOrganizer(memoId, UpdateMemoOrganizerInput(pinned = pinned)).mapSuccess { data }
    }

    suspend fun archiveMemo(memoId: Long): ApiResponse<Memo> = memosApiService.call { api ->
        api.patchMemo(memoId, PatchMemoInput(id = memoId, rowStatus = MemosRowStatus.ARCHIVED)).mapSuccess { data }
    }

    suspend fun restoreMemo(memoId: Long): ApiResponse<Memo> = memosApiService.call { api ->
        api.patchMemo(memoId, PatchMemoInput(id = memoId, rowStatus = MemosRowStatus.NORMAL)).mapSuccess { data }
    }

    suspend fun deleteMemo(memoId: Long): ApiResponse<Unit> = memosApiService.call { api ->
        api.deleteMemo(memoId)
    }

    suspend fun editMemo(memoId: Long, content: String): ApiResponse<Memo> = memosApiService.call { api ->
        api.patchMemo(memoId, PatchMemoInput(id = memoId, content = content)).mapSuccess { data }
    }
}