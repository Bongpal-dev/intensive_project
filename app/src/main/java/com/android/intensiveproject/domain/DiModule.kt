package com.android.intensiveproject.domain

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Im
import com.android.intensiveproject.data.ImageLDSImpl
import com.android.intensiveproject.data.ImageRDSImpl.Kakao
import com.android.intensiveproject.data.ImageRDSImpl.Naver
import com.android.intensiveproject.data.ImageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {
    @Binds
    abstract fun provideImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository

    @Binds
    @Named("kakao")
    abstract fun provideKaKaoAPI(kakaoAPI: Kakao): ImageRDS

    @Binds
    @Named("naver")
    abstract fun provideNaverAPI(naverAPI: Naver): ImageRDS

    @Binds
    abstract fun provideImageLDS(imageLds: ImageLDSImpl): ImageLDS


}

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    @Singleton
    @Named("image_prefs")
    fun provideImageSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("image_prefs", Context.MODE_PRIVATE)
    }
}

