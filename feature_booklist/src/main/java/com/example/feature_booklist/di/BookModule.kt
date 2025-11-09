package com.example.feature_booklist.di

import com.example.core.database.BookDao
import com.example.feature_booklist.data.BookApi
import com.example.feature_booklist.domain.repository.BookRepository
import com.example.feature_booklist.domain.repository.BookRepositoryImpl
import com.example.feature_booklist.domain.repository.usecase.GetBookDetailsUseCase
import com.example.feature_booklist.domain.repository.usecase.GetBooksUseCase
import com.example.feature_booklist.presentation.viewmodel.BookDetailsViewModel
import com.example.feature_booklist.presentation.viewmodel.BookListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookModule {
    @Provides
    @Singleton
    fun provideBookRepository(bookApi: BookApi, bookDao: BookDao):
            BookRepository = BookRepositoryImpl(bookApi,bookDao)

    @Provides
    @Singleton
    fun provideBookApi(retrofit: Retrofit): BookApi =
        retrofit.create(BookApi::class.java)
    @Provides
    @Singleton
    fun provideGetBooksUseCase(repository: BookRepository): GetBooksUseCase =
        GetBooksUseCase(repository)

    @Provides
    @Singleton
    fun provideBookListViewModel(getBooksUseCase: GetBooksUseCase): BookListViewModel = BookListViewModel(getBooksUseCase)

    @Provides
    @Singleton
    fun provideBookDetailsViewModel(getBookDetailsUseCase: GetBookDetailsUseCase): BookDetailsViewModel =
        BookDetailsViewModel(getBookDetailsUseCase)

}