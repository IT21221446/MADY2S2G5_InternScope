package com.example.feedback_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Toast
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import com.example.feedback_ui.databinding.FragmentFeedbackBinding
import com.example.feedback_ui.model.FeedbackData
import com.google.firebase.database.FirebaseDatabase


class Feedback : Fragment() {


    private var _binding: FragmentFeedbackBinding? = null;
    private val binding get() = _binding!!;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        val view = binding.root;


        // Set the maximum number of stars to 5
        binding.stars.numStars = 5

        // Set the step size to 1 (i.e., each star represents 1 unit)
        binding.stars.stepSize = 1.0f

        binding.stars.setOnRatingBarChangeListener { ratingBar: RatingBar?, ratingVariable: Float, fromUser: Boolean ->
            when (ratingVariable.toInt()) {
                1 -> binding.rating.text = "Dissatisfied"
                2 -> binding.rating.text = "OK"
                3 -> binding.rating.text = "Good"
                4 -> binding.rating.text = "Satisfied"
                5 -> binding.rating.text = "Very Satisfied"
                else -> {
                }
            }



            binding.btnSendFeedback.setOnTouchListener(object : View.OnTouchListener {

                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {

                            val rating = binding.stars.rating
                            val feedback = binding.editTextTextMultiLine3.text.toString().trim()

                            Log.d("Feedback", "rating:" + rating)

                            // Get a reference to your Firebase Realtime Database node
                            val database = FirebaseDatabase.getInstance()
                            val feedbackRef = database.getReference("feedback")

                            // Create a new Feedback object with the user's rating and feedback
                            val newFeedback = FeedbackData(rating, feedback,"x123")

                            // Save the feedback to the database
                            val newFeedbackRef = feedbackRef.push()
                            newFeedbackRef.setValue(newFeedback).addOnSuccessListener {

                                // Get the id of the added data
                                val feedbackId = newFeedbackRef.key

                                //fragment transaction
                                val viewFeedback = ViewFeedback()
                                val bundle = Bundle()
                                bundle.putFloat("rating", rating)
                                bundle.putString("feedback", feedback)
                                bundle.putString("feedbackID", feedbackId)
                                viewFeedback.arguments = bundle

                                val fragmentManager = requireActivity().supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.frame_layout, viewFeedback)
                                fragmentTransaction.commit()

                                 Toast.makeText(requireContext(), "Feedback sent!", Toast.LENGTH_SHORT).show()
                            }
                                .addOnFailureListener {
                                    // Failed to save feedback to database
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed to send feedback",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }


                        }
                    }

                    return v?.onTouchEvent(event) ?: true
                }

            })
        }
        return view;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}










