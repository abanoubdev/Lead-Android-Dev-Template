package net.compose.leadandroiddevprep.products

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.compose.leadandroiddevprep.products.presentation.ProductListUiState
import net.compose.leadandroiddevprep.products.presentation.ProductsViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductsViewModelTest {

    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test view model initializes with loading state with success response`() = runTest {
        val fakeRepository = FakeProductRepositoryImpl()
        val viewModel = ProductsViewModel(fakeRepository)

        viewModel.productsState.test {
            val initialState = awaitItem()
            assertEquals(initialState, ProductListUiState.Loading)
            val finalState = awaitItem()
            assertEquals(
                finalState, ProductListUiState.Success(
                    ProductSampleDataSource.generateSampleProducts().toPersistentList(),
                    message = null
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test view model initializes with loading state with error response`() = runTest {
        val fakeRepository = FakeErrorProductRepositoryImpl()
        val viewModel = ProductsViewModel(fakeRepository)

        viewModel.productsState.test {
            val initialState = awaitItem()
            assertEquals(initialState, ProductListUiState.Loading)
            val finalState = awaitItem()
            assertEquals(
                finalState, ProductListUiState.Error(R.string.unknown_error_title)
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}