package net.compose.leadandroiddevprep.cart

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.compose.leadandroiddevprep.cart.presentation.CartUiState
import net.compose.leadandroiddevprep.cart.presentation.CartViewModel
import net.compose.leadandroiddevprep.data.repository.CartRepositoryImpl
import net.compose.leadandroiddevprep.domain.model.CartItem
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi

class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initializes with loading state with success`() = runTest {

        val fakeRepository = CartRepositoryImpl()
        val viewModel = CartViewModel(fakeRepository)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(initialState, CartUiState.Loading)
            val finalState = awaitItem()
            assertEquals(
                finalState, CartUiState.Success(
                    listOf(
                        CartItem(id = "1", name = "Product 1", price = 10.0),
                        CartItem(id = "2", name = "Product 2", price = 20.0),
                        CartItem(id = "3", name = "Product 3", price = 30.0),
                        CartItem(id = "4", name = "Product 4", price = 40.0),
                        CartItem(id = "5", name = "Product 5", price = 50.0),
                        CartItem(id = "6", name = "Product 6", price = 60.0),
                        CartItem(id = "7", name = "Product 7", price = 70.0),
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `view model initializes with loading state with failure`() = runTest {

        val fakeRepository = FakeErrorCartImplementation()
        val viewModel = CartViewModel(fakeRepository)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(initialState, CartUiState.Loading)
            val finalState = awaitItem()
            assertEquals(
                finalState, CartUiState.Error("Error")
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}