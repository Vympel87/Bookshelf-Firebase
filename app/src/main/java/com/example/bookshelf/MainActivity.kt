package com.example.bookshelf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookshelf.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val searchAuthor: String = binding.searchSearching.text.toString()
            if (searchAuthor.isNotEmpty()) {
                readData(searchAuthor)
            } else {
                Toast.makeText(this, "Please input some author name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun readData(bookAuthor: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Book Information")
        databaseReference.child(bookAuthor).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val title = dataSnapshot.child("title").value.toString()
                val author = dataSnapshot.child("author").value.toString()
                val price = dataSnapshot.child("price").value.toString().toIntOrNull()
                val releaseString = dataSnapshot.child("release").value.toString()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                val release: Date? = dateFormat.parse(releaseString)

                Toast.makeText(this, "Result found", Toast.LENGTH_SHORT).show()
                binding.searchSearching.text?.clear()
                binding.rsltTitle.text = title
                binding.rsltAuthor.text = author
                binding.rsltPrice.text = price?.toString() ?: "N/A"
                binding.rsltRelease.text = release?.toString() ?: "N/A"
            } else {
                Toast.makeText(this, "Author does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}
