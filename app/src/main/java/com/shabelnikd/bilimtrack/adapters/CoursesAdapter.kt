package com.shabelnikd.bilimtrack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.bilimtrack.databinding.CourseItemBinding
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse


class CoursesAdapter(
) : ListAdapter<SubjectsMeResponse, CoursesAdapter.ViewHolder>(GenericDiffUtil<SubjectsMeResponse>()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val courseSubject = getItem(position)
        with(holder) {
            binding.tvCourseName.text = courseSubject.name
            binding.tvCourseDescription.text = courseSubject.description

            courseSubject.photo?.let { photoUrl ->
                Glide.with(binding.root).load(photoUrl).into(binding.imageCourse)
            }

        }


    }

    class ViewHolder(val binding: CourseItemBinding) : RecyclerView.ViewHolder(binding.root)

}