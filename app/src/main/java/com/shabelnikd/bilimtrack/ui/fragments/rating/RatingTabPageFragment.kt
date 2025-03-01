package com.shabelnikd.bilimtrack.ui.fragments.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shabelnikd.bilimtrack.adapters.RatingGroupsAdapter
import com.shabelnikd.bilimtrack.adapters.RatingUsersAdapter
import com.shabelnikd.bilimtrack.databinding.FragmentRatingTabPageBinding
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import com.shabelnikd.bilimtrack.ui.fragments.rating.RatingTabFragment.Companion.ARG_ON_USERS_TOP_POSITION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RatingTabPageFragment : Fragment() {

    private val viewModel: RatingTabPageViewModel by viewModels()

    private var _binding: FragmentRatingTabPageBinding? = null
    private val binding get() = _binding!!

    private val userRatingAdapter = RatingUsersAdapter()
    private val groupsRatingAdapter = RatingGroupsAdapter()

    private val searchScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingTabPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        observeStudentsData()
        observeGroupsData()

        userRatingAdapter.setOnClickListener { username ->
            findNavController().navigate(
                RatingTabFragmentDirections.actionRatingTabPageFragmentToSomeProfileFragment(
                    username
                )
            )
        }

        binding.etSearchStudents.editText?.doOnTextChanged { inputText, _, _, _ ->
            searchScope.coroutineContext.cancelChildren()

            searchScope.launch {
                delay(300)
                observeStudentsData(inputText.toString())
            }
        }

        when (arguments?.getInt(ARG_ON_USERS_TOP_POSITION)) {
            0 -> {
                binding.rvTop.adapter = userRatingAdapter
                binding.rvTop.layoutManager = LinearLayoutManager(requireContext())
                binding.etSearchStudents.visibility = View.VISIBLE
            }

            1 -> {
                binding.rvTop.adapter = groupsRatingAdapter
                binding.rvTop.layoutManager = LinearLayoutManager(requireContext())
                binding.etSearchStudents.visibility = View.GONE
            }
        }

    }


    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUsersRating()
                viewModel.getGroupsRating()
            }
        }
    }

    private fun observeStudentsData(q: String = "") {
        checkEditText(
            viewModel.usersRatingResponse,
            q
        ).observe(viewLifecycleOwner) { usersResponse ->
            if (usersResponse.isNullOrEmpty()) {
                showError("Ошибка загрузки списка студентов")
            }

            var sortedList: List<RatingUsersResponse>

            when (q.isEmpty()) {
                true -> {
                    sortedList = usersResponse.filter { it.points!! > 0 }.sortedBy { it.rating }
                    sortedList.take(3).mapIndexed { i, user -> user.isTopWinnerPosition = i + 1 }
                }

                else -> {
                    sortedList = usersResponse.sortedBy { it.rating }
                }
            }

            userRatingAdapter.submitList(sortedList)
        }
    }


    private fun observeGroupsData() {
        viewModel.groupsRatingResponse.observe(viewLifecycleOwner) { groupsResponse ->
            if (groupsResponse.isNullOrEmpty()) {
                showError("Ошибка загрузки списка групп")
            }
            var sortedList = groupsResponse.sortedBy { it.points }.reversed()
            sortedList.take(3).mapIndexed { i, group -> group.isTopWinnerPosition = i + 1 }
            groupsRatingAdapter.submitList(sortedList)
        }
    }


    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun checkEditText(
        studentList: LiveData<List<RatingUsersResponse>>,
        q: String
    ): LiveData<List<RatingUsersResponse>> {
        return when (!q.isEmpty()) {
            true -> {
                studentList.map { students ->
                    students.filter { student ->
                        student.username?.contains(q) == true
                                || student.firstName?.contains(q) == true
                                || student.lastName?.contains(q) == true
                    }
                }
            }

            else -> studentList
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}