package com.example.presentation.ui.history

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Receipt
import com.example.presentation.R
import com.example.presentation.databinding.HistoryFragmentBinding
import com.example.presentation.ext.extendFabIfPossible
import com.example.presentation.ext.shrinkFabIfPossible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), HistoryFragmentDialog.HistoryCalendarListener {

    private val historyViewModel: HistoryViewModel by viewModel()
    private lateinit var binding: HistoryFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.history_fragment, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_history_calendar -> startCalendarDialogFragment()
        }

        return false
    }

    private fun startCalendarDialogFragment() {
        val dialog = HistoryFragmentDialog(this)
        val fragmentTransaction = requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)

        dialog.show(fragmentTransaction, null)
    }

    private fun bindUI() {
        val receiptAdapter = ReceiptAdapter()

        historyViewModel.receiptData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty())
                showEmptyScreenFields()
            else
                hideEmptyScreensFields()

            receiptAdapter.submitList(it)
            handleFabVisibility(it)
        })

        historyViewModel.getBluetoothAddressError.observe(
            viewLifecycleOwner,
            Observer { errorOccurred ->
                if (errorOccurred)
                    showSnackBar(getString(R.string.no_saved_bluetooth_address_warning))
            })

        binding.fabPrint.setOnClickListener {
            buildDialog()
            binding.fabPrint.shrinkFabIfPossible()
        }

        listenToRecyclerScroll()

        binding.rvReceiptList.adapter = receiptAdapter
    }

    private fun showSnackBar(messageToShow: String) =
        Snackbar.make(
            binding.historyRoot,
            messageToShow,
            Snackbar.LENGTH_SHORT
        ).show()

    private fun showEmptyScreenFields() {
        binding.tvEmptyReceipts.text = getString(R.string.no_receipts_in_range)
        binding.noReceiptGroup.visibility = View.VISIBLE
    }

    private fun hideEmptyScreensFields() {
        binding.noReceiptGroup.visibility = View.GONE
    }

    private fun listenToRecyclerScroll() {
        binding.rvReceiptList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    binding.fabPrint.hide()
                else if (dy < 0)
                    binding.fabPrint.show()
            }
        })
    }

    private fun handleFabVisibility(receiptList: List<Receipt>) {
        if (receiptList.isEmpty())
            binding.fabPrint.hide()
        else
            binding.fabPrint.show()
    }

    private fun buildDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.printing))
            .setMessage(getString(R.string.print_checkout))
            .setPositiveButton(
                getString(R.string.ok),
                DialogInterface.OnClickListener(positiveDialogClick)
            )
            .setOnDismissListener { binding.fabPrint.extendFabIfPossible() }
            .show()
    }

    private val positiveDialogClick = { dialog: DialogInterface, _: Int ->
        binding.fabPrint.extendFabIfPossible()

        historyViewModel.prepareDataForPrint()
        dialog.dismiss()
    }

    override fun onDateRangeSelected(startDate: Long, endDate: Long) {
        historyViewModel.fetchReceiptsFromTheSelectedDateRange(startDate, endDate)
    }
}
