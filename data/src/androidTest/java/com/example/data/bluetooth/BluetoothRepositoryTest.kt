package com.example.data.bluetooth

import android.content.Context
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.shared.DispatcherProvider
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class BluetoothRepositoryTest {
    private val context: Context = mockk(relaxed = true)
    private val dispatcherProvider: DispatcherProvider = mockk()
    private val bluetoothDao: BluetoothDao = mockk()

    private lateinit var bluetoothRepository: IBluetoothRepository

    @ExperimentalCoroutinesApi
    @BeforeEach
    fun setUp() {
        clearAllMocks()

        every { dispatcherProvider.provideIOContext() } returns Dispatchers.Unconfined

        bluetoothRepository = BluetoothRepositoryImpl(
            context, dispatcherProvider, bluetoothDao
        )
    }

    @Test
    fun getNearbyDevicesFailedToFindAnyDevices() {
        assert(true)
    }
}

