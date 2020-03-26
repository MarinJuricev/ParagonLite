package com.example.presentation.ui.checkout

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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

const val CHECKOUT_VALUE_INITIAL_VALUE = "0.0"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()

    }

    private fun bindUI() {
        val checkoutAdapter = CheckoutAdapter(
            { checkoutArticle: CheckoutArticle -> onDeleteClick(checkoutArticle) },
            { checkoutArticle: CheckoutArticle -> updateQuantityCount(checkoutArticle) }
        )

        checkoutViewModel.articleData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty())
                showEmptyScreenFields()
            else
                hideEmptyScreensFields()

            checkoutAdapter.submitList(it)
        })

        checkoutViewModel.checkoutValue.observe(viewLifecycleOwner, Observer {
            when (it) {
                CHECKOUT_VALUE_INITIAL_VALUE -> fabPrint.hide()
                else -> fabPrint.show()
            }

            val stringToDisplay = getString(R.string.total_amount, it)

            animateCheckoutPriceChange(stringToDisplay)
        })

        checkoutViewModel.getBluetoothAddressError.observe(viewLifecycleOwner, Observer {
            if (it)
                showSnackBar(getString(R.string.no_saved_bluetooth_address_warning))
        })

        checkoutViewModel.checkoutBadgeCount.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> hideCheckoutBadge()
                else -> updateCheckoutBadgeCount(it)
            }
        })

        checkoutViewModel.articleUpdate.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showSnackBar(getString(R.string.article_saved_successfully))
                else -> showSnackBar(getString(R.string.article_save_error))
            }
        })



        fabPrint.setOnClickListener {
            buildDialog()
            fabPrint.shrinkFabIfPossible()

        }

        listenToRecyclerScroll()

        rvCheckoutList.adapter = checkoutAdapter
    }

    private fun animateCheckoutPriceChange(stringToDisplay: String) {
        val inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up)
        inAnimation.duration = 450
        tvCheckoutPrice.inAnimation = inAnimation


        val outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_up)
        outAnimation.duration = 450
        tvCheckoutPrice.outAnimation = outAnimation

        tvCheckoutPrice.setText(stringToDisplay)
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
                    fabPrint.hide()
                else if (dy < 0)
                    fabPrint.show()
            }
        })
    }

    private fun hideCheckoutBadge() = activity?.bottom_nav?.removeBadge(R.id.navigation_checkout)

    private fun updateCheckoutBadgeCount(badgeCount: Int) {
        activity?.bottom_nav?.getOrCreateBadge(R.id.navigation_checkout)?.number = badgeCount
    }

    private fun showSnackBar(messageToShow: String) =
        Snackbar.make(
            checkoutRoot,
            messageToShow,
            Snackbar.LENGTH_SHORT
        ).show()

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

    private fun onDeleteClick(checkoutArticle: CheckoutArticle) =
        checkoutViewModel.deleteArticle(checkoutArticle)

    private fun updateQuantityCount(checkoutArticle: CheckoutArticle) {
        checkoutViewModel.updateArticle(checkoutArticle)
    }
}
