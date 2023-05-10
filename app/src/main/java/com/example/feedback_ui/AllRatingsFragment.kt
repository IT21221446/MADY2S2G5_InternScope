package com.example.feedback_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedback_ui.Adapters.AllRatingsAdapter
import com.example.feedback_ui.databinding.FragmentAllRatingBinding
import com.example.feedback_ui.model.FeedbackData
import com.google.firebase.database.*

class AllRatingsFragment : Fragment() {
    private lateinit var binding: FragmentAllRatingBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<FeedbackData>()
    private lateinit var adapter: AllRatingsAdapter
    private lateinit var ratings: MutableList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllRatingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseRef = FirebaseDatabase.getInstance().reference.child("feedback")

        //configure rv
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity);

        val query = databaseRef.orderByChild("companyID").equalTo("x123")


        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ratings = mutableListOf()
                mList.clear()
                for ( snapshot in snapshot.children){
                    val fed = snapshot.getValue(FeedbackData::class.java)!!
                    if( fed != null){
                        fed?.let { ratings.add(it.rating.toInt()) }
                        mList.add(fed)
                    }
                }
                // Calculate the average rating
                val averageRating = ratings.average().toFloat()
                val formattedRating = String.format("%.1f", averageRating)

                // Set the rating of the RatingBar to the average rating
                binding.averageVal.text = formattedRating
                binding.AvgRatingBar.rating = averageRating

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
        adapter = AllRatingsAdapter(mList)
        recyclerView.adapter = adapter


        //Setting onclick on recyclerView each item
        adapter.setOnItemClickListner(object: AllRatingsAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {

            }

        })




    }


}