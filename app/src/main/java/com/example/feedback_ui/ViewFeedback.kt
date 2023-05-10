package com.example.feedback_ui


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.feedback_ui.databinding.FragmentViewFeedbackBinding
import com.google.firebase.database.FirebaseDatabase

class ViewFeedback : Fragment() {

    private var _binding: FragmentViewFeedbackBinding? = null;
    private val binding get() = _binding!!;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewFeedbackBinding.inflate(inflater, container, false);
        val view = binding.root;

        val args = arguments
        val rating = args?.getFloat("rating")
        setRating(rating)
        val feedback = args?.getString("feedback")
        val feedbackID = args?.getString("feedbackID")

        binding.editTextTextMultiLine8.setText(feedback)

        //redirect to EditFeedback
        binding.btnEdit.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {

                        val editFeedback = EditFeedback()
                        val bundle = Bundle()
                        if (rating != null) {
                            bundle.putFloat("rating", rating)
                        }
                        bundle.putString("feedback", feedback)
                        bundle.putString("feedbackID", feedbackID)
                        editFeedback.arguments = bundle

                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_layout, editFeedback)
                        fragmentTransaction.commit()
                    }


                }
                return v?.onTouchEvent(event) ?: true
            }

        })

        //delete
        binding.btnDelete.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val dialogBuilder = AlertDialog.Builder(requireContext())
                        dialogBuilder.setMessage("Are you sure you want to delete this feedback?")
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, _ ->
                                dialog.dismiss()

                                val database = FirebaseDatabase.getInstance()
                                val feedbackRef = database.getReference("feedback")

                                if (feedbackID != null) {
                                    feedbackRef.child(feedbackID).removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                requireContext(),
                                                "Feedback deleted!",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            val feedBack = Feedback()

                                            val fragmentManager =
                                                requireActivity().supportFragmentManager
                                            val fragmentTransaction =
                                                fragmentManager.beginTransaction()
                                            fragmentTransaction.replace(
                                                R.id.frame_layout,
                                                feedBack
                                            )
                                            fragmentTransaction.commit()
                                        }
                                        .addOnFailureListener {
                                            // Handle failure
                                        }
                                }
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }

                        val alertDialog = dialogBuilder.create()
                        alertDialog.show()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })


        return view;
    }

    private fun setRating(rating: Float?) {
        if (rating != null) {
            when (rating.toInt()) {
                1 -> binding.ratingBar.rating = 1F
                2 -> binding.ratingBar.rating = 2F
                3 -> binding.ratingBar.rating = 3F
                4 -> binding.ratingBar.rating = 4F
                5 -> binding.ratingBar.rating = 5F
                else -> {
                }
            }
        }
    }

}

