package com.mirz.storyapp.domain.usecase

import com.mirz.storyapp.domain.contract.GetStoryDetailUseCaseContract
import com.mirz.storyapp.domain.entity.StoryEntity
import com.mirz.storyapp.domain.interfaces.StoryRepository
import com.mirz.storyapp.domain.mapper.map
import com.mirz.storyapp.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetStoryDetailUseCase(private val storyRepository: StoryRepository) :
    GetStoryDetailUseCaseContract {
    override operator fun invoke(id: String): Flow<ResultState<StoryEntity>> = flow {
        emit(ResultState.Loading())
        storyRepository.getStory(id).map {
            it.story.map()
        }.catch {
            emit(ResultState.Error(message = it.message.toString()))
        }.collect {
            emit(ResultState.Success(it))
        }
    }
}
