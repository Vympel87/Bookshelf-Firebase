package com.example.bookshelfadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookshelfadmin.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val title = binding.inpTitle.text.toString()
            val author = binding.inpAuthor.text.toString()
            val priceStr = binding.inpPrice.text.toString()
            val price = try {
                priceStr.toInt()
            } catch (e: NumberFormatException) {
                0
            }
            val releaseStr = binding.inpRelease.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val release: Date? = try {
                dateFormat.parse(releaseStr)
            } catch (e: ParseException) {
                null
            }

            if (title.isNotEmpty() && author.isNotEmpty() && release != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Book Information")
                val bookData = BookData(title, author, price, release)
                databaseReference.child(author).setValue(bookData)
                    .addOnSuccessListener {
                        clearInputFields()

                        Toast.makeText(this, "Data saved succesfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@UploadActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data Failed to save", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        // Handle completion here
                    }
            } else {
                Toast.makeText(this, "Please input correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputFields() {
        binding.inpTitle.text?.clear()
        binding.inpAuthor.text?.clear()
        binding.inpPrice.text?.clear()
        binding.inpRelease.text?.clear()
    }
}
