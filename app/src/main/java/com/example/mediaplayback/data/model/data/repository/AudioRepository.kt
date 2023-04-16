package com.example.mediaplayback.data.model.data.repository

import com.example.mediaplayback.data.model.Audio
import com.example.mediaplayback.data.model.data.ContentResolverHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioRepository @Inject constructor(private val  contentResolverHelper: ContentResolverHelper){
    suspend fun  getAudioData():List<Audio> = withContext(Dispatchers.IO){
      contentResolverHelper.getAudioData()
    }
}