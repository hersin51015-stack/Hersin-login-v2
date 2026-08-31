package com.example

import com.example.model.AuthStatus
import com.example.viewmodel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `authViewModel tests wrong and right credentials`() {
    val viewModel = AuthViewModel()

    // Test wrong credentials
    viewModel.login("invalidUser", "wrongPass")
    assertTrue(viewModel.authStatus.value is AuthStatus.Error)

    // Test right credentials (admin)
    viewModel.login("admin", "password123")
    assertTrue(viewModel.authStatus.value is AuthStatus.Success)
    assertEquals("admin", viewModel.currentUser.value?.username)
  }
}

