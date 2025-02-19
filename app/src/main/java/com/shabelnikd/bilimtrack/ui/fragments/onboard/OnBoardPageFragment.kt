package com.shabelnikd.bilimtrack.ui.fragments.onboard

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieDrawable
import com.shabelnikd.bilimtrack.R
import com.shabelnikd.bilimtrack.databinding.FragmentOnBoardPageBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnBoardPageFragment : Fragment() {

    private var _binding: FragmentOnBoardPageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        changeDotsIndicatorY()

    }

    private fun initialize() = with(binding) {
        when (arguments?.getInt(ARG_ON_BOARD_POSITION)) {
            0 -> {
                tvOnBoard.text = "Обучение"
                tvOnBoardDetail.text =
                    "Ускорьте процесс своего обучения! Легко отслеживайте свои оценки и выполняйте задачи мгновенно."
                lottieOnBoard.setAnimation(R.raw.board_learn)
            }

            1 -> {
                tvOnBoard.text = "Доступность"
                tvOnBoardDetail.text =
                    "Получите доступ к статьям от наших преподователей. Получайте материал урока благодаря нашей платформе."
                lottieOnBoard.setAnimation(R.raw.board_work)
            }

            2 -> {
                tvOnBoard.text = "Рейтинг"
                tvOnBoardDetail.text =
                    "Выбивайтесь в топы, благодаря нашей системе рейтинга. Станьте лучшим студентом или группой на курсе!"
                lottieOnBoard.setAnimation(R.raw.board_stats)
                lottieOnBoard.repeatMode = LottieDrawable.RESTART
            }
        }
    }

    private fun changeDotsIndicatorY() = with(binding.tvOnBoardDetail) {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                val pxToDp = fun(px: Int): Int {
                    return TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        px.toFloat(),
                        resources.displayMetrics
                    ).toInt()
                }

                val location = IntArray(2)
                binding.tvOnBoardDetail.getLocationOnScreen(location)

                val y = location[1] + binding.tvOnBoardDetail.height + pxToDp(20)

                val dotsIndicatorNewY = y
                val dotsIndicator =
                    requireActivity().findViewById<DotsIndicator>(R.id.dotsIndicator)
                dotsIndicator.y = dotsIndicatorNewY.toFloat()


            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_ON_BOARD_POSITION = "onBoard"
    }
}