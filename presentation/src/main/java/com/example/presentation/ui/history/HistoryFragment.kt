package com.example.presentation.ui.history

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.presentation.R
import com.example.presentation.databinding.HistoryFragmentBinding
import kotlinx.android.synthetic.main.history_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), HistoryFragmentDialog.HistoryCalendarListener {

    private val receiptViewModel: HistoryViewModel by viewModel()
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

        receiptViewModel.receiptData.observe(this@HistoryFragment, Observer {
            // if the previous list is empty and we don't clear it we'd receive a fatal error from rv saying
            // "view-inconsistency-detected", and we'd crash. To prevent that on update we check if the list is empty
            // and invalidate the data since a new type of viewholder will get inflated if the list is not empty.
            if (receiptAdapter.currentList.isEmpty()){
                receiptAdapter.currentList.clear()
                receiptAdapter.notifyDataSetChanged()
            }

            receiptAdapter.submitList(it)
        })

        rvReceiptList.adapter = receiptAdapter
    }

    override fun onDateRangeSelected(startDate: String, endDate: String) {
        Toast.makeText(
            context,
            "Start Date: $startDate End date: $endDate",
            Toast.LENGTH_SHORT
        ).show()

    }
}
