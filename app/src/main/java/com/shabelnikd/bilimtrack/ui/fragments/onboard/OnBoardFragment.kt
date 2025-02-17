package com.shabelnikd.bilimtrack.ui.fragments.onboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.shabelnikd.bilimtrack.adapters.OnBoardAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentOnBoardBinding
import com.shabelnikd.bilimtrack.ui.MainActivity
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardFragment : Fragment() {

    private var _binding: FragmentOnBoardBinding? = null
    private val binding get() = _binding!!

    val sharedPreferences = PreferenceHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        sharedPreferences.initialize(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.visibility = View.INVISIBLE
        initialization()
        setupListeners()
    }

    private fun initialization() {
        binding.onBoardViewPager.adapter = OnBoardAdapter(this)
        binding.dotsIndicator.attachTo(binding.onBoardViewPager)

    }

    private fun setupListeners() = with(binding.onBoardViewPager) {
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int): Unit = with(binding) {
                super.onPageSelected(position)
                if (position == adapter?.itemCount?.minus(1)) {
                    tvClickableSkip.animate()
                        .alpha(0f)
                        .translationY(-tvClickableSkip.height.toFloat())
                        .translationX(tvClickableSkip.right.toFloat())
                        .setDuration(300)
                        .setInterpolator(AnticipateOvershootInterpolator())
                        .withEndAction {
                            tvClickableSkip.visibility = View.INVISIBLE
                        }

                    btnStart.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(300)
                        .setInterpolator(OvershootInterpolator())
                        .withStartAction {
                            btnStart.visibility = View.VISIBLE
                        }

                } else {
                    tvClickableSkip.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .translationX(0f)
                        .setDuration(300)
                        .setInterpolator(OvershootInterpolator())
                        .withStartAction {
                            tvClickableSkip.visibility = View.VISIBLE
                        }

                    btnStart.animate()
                        .alpha(0f)
                        .translationY(btnStart.height.toFloat())
                        .setDuration(300)
                        .setInterpolator(OvershootInterpolator())
                        .withEndAction {
                            btnStart.visibility = View.INVISIBLE
                        }

                    tvClickableSkip.setOnClickListener {
                        currentItem = adapter?.itemCount!!
                    }
                }
            }
        })

        binding.btnStart.setOnClickListener {
            sharedPreferences.isFirstLaunch = false
            activity?.finish()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.onBoardViewPager.adapter = null
        _binding = null
    }
}