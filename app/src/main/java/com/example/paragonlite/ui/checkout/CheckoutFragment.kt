package com.example.paragonlite.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.paragonlite.R
import com.example.paragonlite.databinding.CheckoutFragmentBinding
import kotlinx.android.synthetic.main.checkout_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutFragment : Fragment() {

    private val checkoutViewModel: CheckoutViewModel by viewModel()
    private lateinit var binding: CheckoutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.checkout_fragment, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }

    private fun bindUI() {
        val checkoutAdapter = CheckoutAdapter()

        checkoutViewModel.articleData.observe(this@CheckoutFragment, Observer {
            checkoutAdapter.submitList(it)
        })

        rvCheckoutList.adapter = checkoutAdapter
    }

}
