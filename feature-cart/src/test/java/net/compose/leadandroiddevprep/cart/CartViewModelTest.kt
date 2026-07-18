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

        val fakeRepository = FakeCartRepositoryImpl()
        val viewModel = CartViewModel(fakeRepository)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(initialState, CartUiState.Loading)
            val finalState = awaitItem()
            assertEquals(finalState, CartUiState.Success(CartItemFactory.createCartItems(15)))
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