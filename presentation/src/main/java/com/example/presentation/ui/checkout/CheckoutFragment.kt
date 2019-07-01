package com.example.presentation.ui.checkout

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.CheckoutArticle
import com.example.presentation.R
import com.example.presentation.databinding.CheckoutFragmentBinding
import com.example.presentation.ext.extendFabIfPossible
import com.example.presentation.ext.shrinkFabIfPossible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
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
            if (it.isEmpty())
                showEmptyScreenFields()
            else
                hideEmptyScreensFields()

            checkoutAdapter.submitList(it)
        })

        checkoutViewModel.checkoutValue.observe(this@CheckoutFragment, Observer {
            when (it) {
                "0.0" -> fabPrint.hide()
                else -> fabPrint.show()
            }

            val stringToDisplay = "Ukupno: $it kn"
            tvCheckoutPrice.text = stringToDisplay
        })

        checkoutViewModel.getBluetoothAddressError.observe(this@CheckoutFragment, Observer {
            if (it)
                showBluetoothMacAddressError()
        })

        checkoutViewModel.checkoutBadgeCount.observe(this@CheckoutFragment, Observer {
            when (it) {
                0 -> hideCheckoutBadge()
                else -> updateCheckoutBadgeCount(it)
            }
        })

        fabPrint.setOnClickListener {
            buildDialog()
            fabPrint.shrinkFabIfPossible()

        }

        listenToRecyclerScroll()

        rvCheckoutList.adapter = checkoutAdapter
    }

    private fun showEmptyScreenFields() {
        noCheckoutGroup.visibility = View.VISIBLE
    }

    private fun hideEmptyScreensFields() {
        noCheckoutGroup.visibility = View.GONE
    }

    private fun listenToRecyclerScroll() {
        rvCheckoutList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    fabPrint.hide(true)
                else if (dy < 0)
                    fabPrint.show()
            }
        })
    }

    private fun hideCheckoutBadge() = activity?.bottom_nav?.removeBadge(R.id.navigation_checkout)

    private fun updateCheckoutBadgeCount(badgeCount: Int) {
        activity?.bottom_nav?.showBadge(R.id.navigation_checkout)?.number = badgeCount
    }

    private fun showBluetoothMacAddressError() {
        val snack = Snackbar.make(
            checkoutRoot,
            getString(R.string.no_saved_bluetooth_address_warning),
            Snackbar.LENGTH_LONG
        )
        snack.show()
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

        checkoutViewModel.printCheckout()
        dialog.dismiss()
    }

    private fun onDeleteClick(checkoutArticle: CheckoutArticle) = checkoutViewModel.deleteArticle(checkoutArticle)

}
