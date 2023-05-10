package com.example.feedback_ui.model

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedback_ui.R
import com.example.feedback_ui.databinding.FragmentAllRatingBinding
import com.example.feedback_ui.databinding.FragmentEditBinding
import com.example.feedback_ui.databinding.FragmentFeedbackBinding
import com.google.firebase.database.*

class AllRating : Fragment() {

    private var _binding: FragmentAllRatingBinding? = null;
    private val binding get() = _binding!!;

    private lateinit var databaseReference: DatabaseReference

    private lateinit var ratings: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAllRatingBinding.inflate(inflater, container, false);
        val view = binding.root;

        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("feedback")
        val query = databaseReference.orderByChild("companyID").equalTo("x123")


        // Fetch ratings from Firebase Realtime Database
        ratings = mutableListOf()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val feedback = snapshot.getValue(FeedbackData::class.java)
                    feedback?.let { ratings.add(it.rating.toInt()) }
                }

                // Calculate the average rating
                val averageRating = ratings.average().toFloat()
                val formattedRating = String.format("%.1f", averageRating)

                // Set the rating of the RatingBar to the average rating
                binding.averageVal.text = formattedRating
                binding.AvgRatingBar.rating = averageRating

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })


        return view;
    }

}
