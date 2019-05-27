package com.example.paragonlite.ui.checkout

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.domain.model.CheckoutArticle
import com.example.paragonlite.R
import com.example.paragonlite.databinding.CheckoutFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val checkoutAdapter = CheckoutAdapter { checkoutArticle: CheckoutArticle -> onDeleteClick(checkoutArticle) }

        checkoutViewModel.articleData.observe(this@CheckoutFragment, Observer {
            checkoutAdapter.submitList(it)
        })

        rvCheckoutList.adapter = checkoutAdapter

        checkoutViewModel.checkoutValue.observe(this@CheckoutFragment, Observer {

            when (it) {
                "0.0" -> hidePrintFab()
                else -> showPrintFab()
            }

            val stringToDisplay = "Ukupno: $it kn"
            tvCheckoutPrice.text = stringToDisplay
        })

        fabPrint.setOnClickListener {
            buildDialog()
        }
    }

    private fun buildDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.printing))
            .setMessage(getString(R.string.print_checkout))
            .setPositiveButton("OK", DialogInterface.OnClickListener(positiveDialogClick))
            .show()
    }

    private val positiveDialogClick = { dialog: DialogInterface, _: Int ->
        checkoutViewModel.printCheckout()
        dialog.dismiss()
    }

    private fun showPrintFab() = fabPrint.show()

    private fun hidePrintFab() = fabPrint.hide()

    private fun onDeleteClick(checkoutArticle: CheckoutArticle) = checkoutViewModel.deleteArticle(checkoutArticle)

}
