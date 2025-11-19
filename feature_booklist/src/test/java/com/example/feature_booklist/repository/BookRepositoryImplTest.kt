package com.example.feature_booklist.repository

import app.cash.turbine.test
import com.example.core.database.BookDao
import com.example.core.database.BookEntity
import com.example.core.util.UiState
import com.example.feature_booklist.data.Author
import com.example.feature_booklist.data.Book
import com.example.feature_booklist.data.BookApi
import com.example.feature_booklist.data.GutendexBook
import com.example.feature_booklist.data.GutendexBookResponse
import com.example.feature_booklist.data.toBook
import com.example.feature_booklist.data.toDomain
import com.example.feature_booklist.domain.repository.BookRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BookRepositoryImplTest {
    private val mockApi = mockk<BookApi>()
    private val mockDao = mockk<BookDao>()
    private lateinit var repository: BookRepositoryImpl

    private val sampleBookEntity = BookEntity(
        id = 1,
        title = "1984",
        author = "George Orwell",
        description = "Dystopian novel"
    )

    private val sampleBook = sampleBookEntity.toDomain()

    @Before
    fun setup() {
        repository = BookRepositoryImpl(mockApi, mockDao)
    }

    @Test
    fun `getBooks returns cached data if available`() = runTest {
        // Given: DAO already has data
        every { mockDao.getBooks() } returns flowOf(listOf(sampleBookEntity))
        coEvery { mockApi.getBooks() } returns mockk {
            every { results } returns emptyList()
        }

        // When
        repository.getBooks().test {
            // Skip Loading
            val loading = awaitItem()
            assertTrue(loading is UiState.Loading)

            // Now Success
            val emission = awaitItem()
            assertTrue(emission is UiState.Success)
            assertEquals(sampleBook.title, (emission as UiState.Success).data.first().title)

            cancelAndIgnoreRemainingEvents()
        }

        // Verify API not called because shouldFetch = false
        coVerify(exactly = 0) { mockApi.getBooks() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getBooks fetches remote data when cache is empty`() = runTest {
        // --- Given: setup mocks and initial state ---

        // Simulate a reactive DAO using MutableStateFlow
        val bookFlow = MutableStateFlow<List<BookEntity>>(emptyList())
        every { mockDao.getBooks() } returns bookFlow

        // Mock a remote GutendexBook
        val remoteBook = GutendexBook(
            id = 1,
            title = "Brave New World",
            authors = listOf(Author("Aldous Huxley")),
            subjects = listOf("A dystopian novel")
        )
        val remoteResponse = GutendexBookResponse(results = listOf(remoteBook))

        // When the API is called, return remoteResponse
        coEvery { mockApi.getBooks() } returns remoteResponse

        // When data is saved to DB, update the DAO’s Flow to simulate new data
        coEvery { mockDao.insertAll(any()) } answers {
            val entities = firstArg<List<BookEntity>>()
            bookFlow.value = entities // emit updated data
        }

        // --- When: collect emissions from repository ---
        repository.getBooks().test {
            // 1️⃣ First emission: Loading
            val loading = awaitItem()
            assertTrue(loading is UiState.Loading)

            // 2️⃣ Second emission: Success with fetched data
            val emission = awaitItem()
            assertTrue(emission is UiState.Success)

            val success = emission as UiState.Success<List<Book>>
            assertEquals(1, success.data.size)
            assertEquals("Brave New World", success.data.first().title)

            // Stop collecting further emissions
            cancelAndIgnoreRemainingEvents()
        }

        // --- Then: verify interactions ---
        coVerify(exactly = 1) { mockApi.getBooks() }
        coVerify(exactly = 1) { mockDao.insertAll(any()) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getBookById returns cached book if exists`() = runTest {
        // Given
        every { mockDao.getBookById(1) } returns flowOf(sampleBookEntity)
        coEvery { mockApi.getBookById(any()).toBook() } returns sampleBook

        // When
        repository.getBookById(1).test {
            // 1️⃣ Expect loading first
            val loading = awaitItem()
            assertTrue(loading is UiState.Loading)

            // 2️⃣ Expect success next
            val emission = awaitItem()
            assertTrue(emission is UiState.Success)

            val success = emission as UiState.Success<Book>
            assertEquals("1984", success.data.title)

            cancelAndIgnoreRemainingEvents()
        }

        // Then: API should not be called since cache exists
        coVerify(exactly = 0) { mockApi.getBookById(any()) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getBookById fetches remote if not cached`() = runTest {
    // DAO simulated database
    val dbFlow = MutableStateFlow<BookEntity?>(null)

    every { mockDao.getBookById(1) } returns dbFlow

    // 1️⃣ Create a fake API DTO that your API would return
    val apiDto = GutendexBook(
        id = 1,
        title = "Animal Farm",
        authors = listOf(Author("George Orwell")),
        subjects = listOf("Political satire")
    )

    // 2️⃣ Domain model expected after conversion
    //val remoteBook = apiDto.toBook()   // your real mapper

    // 3️⃣ Mock API
    coEvery { mockApi.getBookById(1) } returns apiDto

    // 4️⃣ When repository saves to DB, update the flow
    coEvery { mockDao.insertAll(any()) } answers {
        dbFlow.value = BookEntity(
            id = 1,
            title = "Animal Farm",
            author = "George Orwell",
            description = "Political satire"   // non-null
        )
    }

    // When
        repository.getBookById(1).test {
        assertTrue(awaitItem() is UiState.Loading)

            val success = awaitItem() as UiState.Success<Book>
        assertEquals("Animal Farm", success.data.title)

            cancelAndIgnoreRemainingEvents()
        }

    // Verify DB + API calls
    coVerify { mockApi.getBookById(1) }
    coVerify { mockDao.insertAll(any()) }
    }

}