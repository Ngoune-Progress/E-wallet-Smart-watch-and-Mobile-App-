package com.se3.payme.BottomSheet

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import com.chaos.view.PinView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.se3.payme.R


class ModalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var pinView: PinView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_modal_bottom_sheet, container, false)
        getDialog()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        pinView = view.findViewById(R.id.firstPinView)

        VerifyPin(view)
        return view
    }


    companion object {
        const val TAG = "ModalBottomSheet"

    }

    private fun VerifyPin(view: View) {

        pinView.setOnClickListener {
            if (pinView.text.toString().equals("11111")) {
                val drawable = resources.getDrawable(R.drawable.done_icon)
                val iv: ImageView = view.findViewById(R.id.securityKey) as ImageView
                iv.setRotationY(0f)
                iv.animate().rotationY(90f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        iv.setImageDrawable(drawable)
                        iv.setRotationY(270f)
                        iv.animate().rotationY(360f).setListener(null)
                    }
                })
            } else {
                val drawable = resources.getDrawable(R.drawable.red_cross)
                val iv: ImageView = view.findViewById(R.id.securityKey) as ImageView
                iv.setRotationY(0f)
                iv.animate().rotationY(90f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        iv.setImageDrawable(drawable)
                        iv.setRotationY(270f)
                        iv.animate().rotationY(360f).setListener(null)
                    }
                })
            }

        }
    }


    override fun onResume() {
        pinView.setText("")

        super.onResume()

    }

    override fun onStart() {
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        super.onStart()
    }
}
