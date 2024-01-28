package com.playgrounds.sitescraper.di

import com.playgrounds.sitescraper.models.LoadConfiguration
import com.playgrounds.sitescraper.models.TaskConfiguration
import com.playgrounds.sitescraper.repos.HtmlRepository
import com.playgrounds.sitescraper.repos.HtmlRepositoryImpl
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.PeriodicCharTextProcessorImpl
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessor
import com.playgrounds.sitescraper.repos.processors.SingleCharTextProcessorImpl
import com.playgrounds.sitescraper.repos.processors.WordCounterTextProcessor
import com.playgrounds.sitescraper.repos.processors.WordCounterTextProcessorImpl
import com.playgrounds.sitescraper.repos.providers.PageProvider
import com.playgrounds.sitescraper.repos.providers.WebPageProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

@Module
@InstallIn(ViewModelComponent::class)
class MainModule {
    @Provides
    fun provideWebPageProvider(httpClient: HttpClient): PageProvider {
        return WebPageProviderImpl(httpClient)
    }

    @Provides
    fun provideHtmlRepository(
        pageProvider: PageProvider,
        singleCharTextProcessor: SingleCharTextProcessor,
        periodicCharTextProcessor: PeriodicCharTextProcessor,
        wordCounterTextProcessor: WordCounterTextProcessor
    ): HtmlRepository {
        return HtmlRepositoryImpl(
            pageProvider,
            singleCharTextProcessor,
            periodicCharTextProcessor,
            wordCounterTextProcessor
        )
    }

    @Provides
    fun provideSingleCharTextProcessor(configuration: LoadConfiguration): SingleCharTextProcessor =
        SingleCharTextProcessorImpl(configuration.singleTaskConfiguration.index)

    @Provides
    fun providePeriodicCharTextProcessor(configuration: LoadConfiguration): PeriodicCharTextProcessor =
        PeriodicCharTextProcessorImpl(configuration.periodicTaskConfiguration.index)

    @Provides
    fun provideWordCountTextProcessor(): WordCounterTextProcessor =
        WordCounterTextProcessorImpl()

    @Provides
    fun provideMainPageConfiguration(): LoadConfiguration {
        return LoadConfiguration(
            TaskConfiguration.SingleCharTask(
                url = "https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer",
                10
            ),
            TaskConfiguration.PeriodicCharTask(
                url = "https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer",
                10
            ),
            TaskConfiguration.WordSplitTask(url = "https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer")
        )
    }

    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }
            install(Logging) {
                logger = Logger.ANDROID
            }
        }
    }
}