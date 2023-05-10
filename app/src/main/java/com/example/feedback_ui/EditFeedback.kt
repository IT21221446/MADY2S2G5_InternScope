package com.example.feedback_ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.feedback_ui.databinding.FragmentEditBinding
import com.google.firebase.database.FirebaseDatabase


class EditFeedback : Fragment() {

    private var _binding: FragmentEditBinding? = null;
    private val binding get() = _binding!!;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false);
        val view = binding.root;

        val args = arguments
        val rating = args?.getFloat("rating")
        setRating(rating)
        val feedback = args?.getString("feedback")
        val feedbackID = args?.getString("feedbackID")
        val companyID = args?.getString("companyID")

        binding.editTextTextMultiLine9.setText(feedback)



        binding.btnSave.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {

                        val rating = binding.ratingBarEdit.rating
                        val feedback = binding.editTextTextMultiLine9.text.toString().trim()


                        // Get a reference to Firebase Realtime Database node
                        val database = FirebaseDatabase.getInstance()

                        val feedbackRef = database.getReference("feedback")

                        //val updatedFeedback = FeedbackData(rating, feedback)
                        // Create a HashMap to hold the updated values
                        val feedbackValues = HashMap<String, Any>()
                        feedbackValues["rating"] = rating
                        feedbackValues["feedback"] = feedback

                        // Update the values in the database
                        if (feedbackID != null) {
                            feedbackRef.child(feedbackID).updateChildren(feedbackValues)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(requireContext(), "Feedback Updated!", Toast.LENGTH_SHORT).show()
                                        //fragment transaction
                                        val viewFeedback = ViewFeedback()
                                        val bundle = Bundle()
                                        bundle.putFloat("rating", rating)
                                        bundle.putString("feedback", feedback)
                                        viewFeedback.arguments = bundle

                                        val fragmentManager =
                                            requireActivity().supportFragmentManager
                                        val fragmentTransaction = fragmentManager.beginTransaction()
                                        fragmentTransaction.replace(R.id.frame_layout, viewFeedback)
                                        fragmentTransaction.commit()
                                    } else {

                                    }
                                }
                        }

                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })

        //not delete
        binding.btnCancel.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {

                        val viewFeedback = ViewFeedback()
                        val bundle = Bundle()
                        if (rating != null) {
                            bundle.putFloat("rating", rating)
                        }
                        bundle.putString("feedback", feedback)
                        viewFeedback.arguments = bundle

                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_layout, viewFeedback)
                        fragmentTransaction.commit()
                    }


                }
                return v?.onTouchEvent(event) ?: true
            }

        })

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRating(rating: Float?) {
        if (rating != null) {
            when (rating.toInt()) {
                1 -> binding.ratingBarEdit.rating = 1F
                2 -> binding.ratingBarEdit.rating = 2F
                3 -> binding.ratingBarEdit.rating = 3F
                4 -> binding.ratingBarEdit.rating = 4F
                5 -> binding.ratingBarEdit.rating = 5F
                else -> {
                }
            }
        }
    }

}