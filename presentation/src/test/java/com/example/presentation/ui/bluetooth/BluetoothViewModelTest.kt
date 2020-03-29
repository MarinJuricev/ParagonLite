package com.example.presentation.ui.bluetooth

import InstantExecutorExtension
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_KEY
import com.example.domain.shared.ISharedPrefsService
import com.example.domain.usecase.bluetooth.GetBluetoothData
import com.example.domain.usecase.bluetooth.UnregisterBluetoothReceiver
import com.example.domain.usecase.bluetooth.UpdateBluetoothData
import com.example.mockfactory.bluetoothEntry
import com.example.mockfactory.macAddressTestData
import com.example.presentation.ui.getOrAwaitValue
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
internal class BluetoothViewModelTest {

    private val unregisterBluetoothReceiver: UnregisterBluetoothReceiver = mockk()
    private val updateBluetoothData: UpdateBluetoothData = mockk()
    private val getBluetoothData: GetBluetoothData = mockk()
    private val sharedPrefsService: ISharedPrefsService = mockk()

    private lateinit var bluetoothViewModel: BluetoothViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        bluetoothViewModel =
            BluetoothViewModel(
                unregisterBluetoothReceiver,
                updateBluetoothData,
                getBluetoothData,
                sharedPrefsService
            )
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `should update _isBluetoothAvailable with false when isBluetoothPresent is false`() =
        runBlockingTest {

            bluetoothViewModel.isBluetoothAvailable(isBluetoothPresent = false, enabled = true)

            assert(bluetoothViewModel.isBluetoothAvailable.getOrAwaitValue() == false)
        }

    @Test
    fun `should update _isBluetoothAvailable with true when isBluetoothPresent is true`() =
        runBlockingTest {

            bluetoothViewModel.isBluetoothAvailable(isBluetoothPresent = true, enabled = true)

            assert(bluetoothViewModel.isBluetoothAvailable.getOrAwaitValue() == true)
        }

    @Test
    fun `should update _isBluetoothEnabled with BluetoothStatusEnabled when enabled is true`() =
        runBlockingTest {
            bluetoothViewModel.isBluetoothAvailable(isBluetoothPresent = true, enabled = true)

            assert(bluetoothViewModel.isBluetoothEnabled.getOrAwaitValue() == BluetoothStatus.Enabled)
        }

    @Test
    fun `should update _isBluetoothEnabled with BluetoothStatusDisabled when enabled is false`() =
        runBlockingTest {
            bluetoothViewModel.isBluetoothAvailable(isBluetoothPresent = true, enabled = false)

            assert(bluetoothViewModel.isBluetoothEnabled.getOrAwaitValue() == BluetoothStatus.Disabled)
        }

    @Test
    fun `should update _isBluetoothEnabled with BluetoothStatusEnabled when request code is BLUETOOTH_REQUEST_ACCEPTED`() =
        runBlockingTest {
            bluetoothViewModel.handleBluetoothRequest(BLUETOOTH_REQUEST_ACCEPTED)

            assert(bluetoothViewModel.isBluetoothEnabled.getOrAwaitValue() == BluetoothStatus.Enabled)
        }

    @Test
    fun `should update _isBluetoothEnabled with BluetoothStatusDismissed when request code is BLUETOOTH_REQUEST_DISMISSED`() =
        runBlockingTest {
            bluetoothViewModel.handleBluetoothRequest(BLUETOOTH_REQUEST_DISMISSED)

            assert(bluetoothViewModel.isBluetoothEnabled.getOrAwaitValue() == BluetoothStatus.Dismissed)
        }

    @Test
    fun `should update _bluetoothData with new data when getBluetoothData is a success`() =
        runBlockingTest {
            val useCaseResult = flow {
                emit(listOf(bluetoothEntry))
            }

            coEvery {
                getBluetoothData.execute()
            } coAnswers { Result.build { useCaseResult } }

            bluetoothViewModel.getData()

            coVerify { getBluetoothData.execute() }

            assert(bluetoothViewModel.bluetoothData.getOrAwaitValue() == listOf(bluetoothEntry))
        }

    @Test
    fun `should update _bluetoothData with empty list when getBluetoothData is a failure`() =
        runBlockingTest {
            coEvery {
                getBluetoothData.execute()
            } coAnswers { Result.build { throw Exception() } }

            bluetoothViewModel.getData()

            coVerify { getBluetoothData.execute() }

            assert(bluetoothViewModel.bluetoothData.getOrAwaitValue() == listOf<BluetoothEntry>())
        }

    @Test
    fun `should update _bluetoothData with empty list when updateBluetoothData is a failure`() =
        runBlockingTest {

            coEvery {
                updateBluetoothData.execute()
            } coAnswers { Result.build { throw Exception() } }

            bluetoothViewModel.updateBluetoothData()

            coVerify { updateBluetoothData.execute() }

            assert(bluetoothViewModel.bluetoothData.getOrAwaitValue() == listOf<BluetoothEntry>())
        }

    @Test
    fun `should update _bluetoothData with new data when updateBluetoothData is a success`() =
        runBlockingTest {
            val useCaseResult = listOf(bluetoothEntry)

            coEvery {
                updateBluetoothData.execute()
            } coAnswers { Result.build { useCaseResult } }

            bluetoothViewModel.updateBluetoothData()

            coVerify { updateBluetoothData.execute() }

            assert(bluetoothViewModel.bluetoothData.getOrAwaitValue() == listOf(bluetoothEntry))
        }

    @Test
    fun `should call sharedPrefsService saveValue when saveMacAddress is called`() =
        runBlockingTest {
            coEvery {
                sharedPrefsService.saveValue(BLUETOOTH_MAC_ADDRESS_KEY, macAddressTestData)
            } just Runs

            bluetoothViewModel.saveMacAddress(macAddressTestData)
            sharedPrefsService.saveValue(BLUETOOTH_MAC_ADDRESS_KEY, macAddressTestData)
        }
}