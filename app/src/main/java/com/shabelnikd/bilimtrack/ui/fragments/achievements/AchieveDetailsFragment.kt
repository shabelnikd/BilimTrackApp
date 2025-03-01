package com.shabelnikd.bilimtrack.ui.fragments.achievements

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.databinding.FragmentAchieveDetailsBinding
import com.shabelnikd.bilimtrack.model.models.BackgroundAchieve
import com.shabelnikd.bilimtrack.ui.MainActivity


class AchieveDetailsFragment : Fragment() {

    private var _binding: FragmentAchieveDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: AchieveDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAchieveDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setNavigationVisibility(false)
        (activity as MainActivity).hideSystemUI()


        fun getResIdByApiId(apiRarityTypeId: Int?): BackgroundAchieve? {
            return BackgroundAchieve.entries.find { it.apiRarityTypeId == apiRarityTypeId }
        }

        getResIdByApiId(args.achieve.rarity?.id)?.let {
            binding.storyConstraint.setBackgroundResource(it.resId)
            binding.progressBar.progressDrawable =
                ResourcesCompat.getDrawable(resources, it.progressResId, null)
            Log.e("AllSD", "${R.drawable.progress_exclusive} / ${it.progressResId}")
        }

        binding.storyAchieveTitle.text = args.achieve.name
        binding.storyAchieveDescription.text = args.achieve.description

        args.achieve.photo?.let {
            Glide.with(binding.storyShow).load(it).into(binding.storyImage)
        }

        binding.progressBar.max = STORY_TIME
        binding.storyShow.visibility = View.VISIBLE

        var progressUntil = STORY_TIME

        fun createTimer(progressUntil: Int): CountDownTimer {
            return object : CountDownTimer(progressUntil.toLong(), 40) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.progressBar.progress = (STORY_TIME - millisUntilFinished).toInt()
                }

                override fun onFinish() {
                    binding.storyShow.visibility = View.GONE
                    (activity as MainActivity).setNavigationVisibility(true)
                    (activity as MainActivity).showSystemUI()
                    findNavController().popBackStack()
                }
            }
        }

        var timer = createTimer(progressUntil)
        timer.start()

        var clickTime = 0L

        binding.storyShow.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    clickTime = System.currentTimeMillis()
                    timer.cancel()
                    progressUntil = STORY_TIME - binding.progressBar.progress
                }

                MotionEvent.ACTION_UP -> {
                    clickTime = System.currentTimeMillis() - clickTime
                    when {
                        clickTime > 300 -> {
                            timer = createTimer(progressUntil)
                            timer.start()
                        }

                        clickTime < 300 -> {
                            timer.onFinish()
                        }
                    }
                    clickTime = 0
                }
            }
            true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val STORY_TIME = 10000
    }

}