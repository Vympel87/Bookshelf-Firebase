package com.example.bookshelfadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookshelfadmin.databinding.ActivityUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Date
import com.google.firebase.database.ServerValue
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpdate.setOnClickListener {
            val updateAuthor = binding.updateAuthor.text.toString()
            val updateTitle = binding.updateTitle.text.toString()
            val updatePrice = binding.updatePrice.text.toString()
            val updateRelease = binding.updateRelease.text.toString()

            if (updateAuthor.isNotEmpty() && updateTitle.isNotEmpty() && updatePrice.isNotEmpty() && updateRelease.isNotEmpty()) {
                val priceAmount = updatePrice.toIntOrNull()
                if (priceAmount != null) {
                    val releaseDate = convertToDate(updateRelease)
                    if (releaseDate != null) {
                        updateData(updateAuthor, updateTitle, priceAmount, releaseDate)
                    } else {
                        Toast.makeText(this, "Invalid release date format", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid price amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertToDate(releaseDate: String): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            dateFormat.parse(releaseDate)
        } catch (e: Exception) {
            null
        }
    }

    private fun updateData(authorName: String, titleName: String, priceAmount: Int, releaseDate: Date) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Book Information")
        val bookData = mapOf<String, Any>(
            "title" to titleName,
            "author" to authorName,
            "price" to priceAmount,
            "release" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(releaseDate),
            "updatedAt" to ServerValue.TIMESTAMP
        )
        databaseReference.child(authorName).updateChildren(bookData).addOnSuccessListener {
            binding.updateAuthor.text?.clear()
            binding.updateTitle.text?.clear()
            binding.updatePrice.text?.clear()
            binding.updateRelease.text?.clear()
            Toast.makeText(this, "Data succesfully updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Unable to update data", Toast.LENGTH_SHORT).show()
        }
    }
}