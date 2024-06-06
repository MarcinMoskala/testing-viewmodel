package academy.kt.usermanagementapp.presentation

import academy.kt.usermanagementapp.MainCoroutineRule
import academy.kt.usermanagementapp.fakes.FakeDelayedUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelMocksTest {

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var userRepository: FakeDelayedUserRepository
    private lateinit var viewModel: UserListViewModel

    @Before
    fun setUp() {
        userRepository = FakeDelayedUserRepository()
        viewModel = UserListViewModel(userRepository)
    }

    @Test
    fun `should load and display users during initialization`() {
        // given repository has users
        // when process has started
        // then loading is displayed and there are no errors
        // when advance until coroutine is finished
        // then users list is displayed
    }

    @Test
    fun `should show error and empty list when loading failed`() {
        // given repository is failing
        // when the process ends
        // then exception is displayed
    }

    @Test
    fun `when asked to refresh, should load new users`() {
        // given users list loaded
        // when users has changed
        // and we ask to refresh
        // when process is started
        // then is loading
        // when advance until coroutine is finished
        // then users list is displayed
    }

    @Test
    fun `when adding user, should add user to repository and load new users`() {
    }

    @Test
    fun `when adding user, should not display loading`() {
    }

    @Test
    fun `when adding user, should show errors`() {
    }

    @Test
    fun `when removing user, should add user to repository and load new users`() {
    }

    @Test
    fun `when removing user, should show errors`() {
    }

    @Test
    fun `when removing user, should not display loading`() {
    }

    @Test
    fun `should hide error`() {
    }
}