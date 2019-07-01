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
import kotlinx.android.synthetic.main.history_fragment.*
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
        val fragmentTransaction = fragmentManager?.beginTransaction()?.addToBackStack(null)
        dialog.show(fragmentTransaction!!, null)
    }

    private fun bindUI() {
        val receiptAdapter = ReceiptAdapter()

        historyViewModel.receiptData.observe(this@HistoryFragment, Observer {
            if (it.isEmpty())
                showEmptyScreenFields()
            else
                hideEmptyScreensFields()

            receiptAdapter.submitList(it)
            handleFabVisibility(it)
        })

        fabPrint.setOnClickListener {
            buildDialog()
            fabPrint.shrinkFabIfPossible()
        }

        listenToRecyclerScroll()

        rvReceiptList.adapter = receiptAdapter
    }

    private fun showEmptyScreenFields() {
        noReceiptGroup.visibility = View.VISIBLE
    }

    private fun hideEmptyScreensFields() {
        noReceiptGroup.visibility = View.GONE
    }

    private fun listenToRecyclerScroll() {
        rvReceiptList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    fabPrint.hide(true)
                else if (dy < 0)
                    fabPrint.show()
            }
        })
    }

    private fun handleFabVisibility(receiptList: List<Receipt>) {
        if (receiptList.isEmpty())
            fabPrint.hide()
        else
            fabPrint.show()
    }

    private fun buildDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.printing))
            .setMessage(getString(R.string.print_checkout))
            .setPositiveButton("OK", DialogInterface.OnClickListener(positiveDialogClick))
            .setOnDismissListener { fabPrint.extendFabIfPossible() }
            .show()
    }

    private val positiveDialogClick = { dialog: DialogInterface, _: Int ->
        fabPrint.extendFabIfPossible()

        historyViewModel.prepareDataForPrint()
        dialog.dismiss()
    }

    override fun onDateRangeSelected(startDate: String, endDate: String) {
        historyViewModel.fetchReceiptsFromTheSelectedDateRange(startDate, endDate)
    }
}
