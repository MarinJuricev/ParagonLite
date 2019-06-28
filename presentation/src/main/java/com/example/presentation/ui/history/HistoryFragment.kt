package com.example.presentation.ui.history

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.presentation.R
import com.example.presentation.databinding.HistoryFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), HistoryFragmentDialog.HistoryCalendarListener {

    private val viewModel: HistoryViewModel by viewModel()
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
    }

    override fun onDateRangeSelected(startDate: String, endDate: String) {
        Toast.makeText(
            context,
            "Start Date: $startDate End date: $endDate",
            Toast.LENGTH_SHORT
        ).show()

    }
}
