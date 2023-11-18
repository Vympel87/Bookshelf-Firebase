package com.example.bookshelfadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookshelfadmin.databinding.ActivityDeleteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDelete.setOnClickListener {
            val authorName = binding.deleteData.text.toString()
            if (authorName.isNotEmpty()) {
                deleteData(authorName)
            } else {
                Toast.makeText(this, "Please give parameter based on author name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteData(authorName: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Book Information")
        databaseReference.child(authorName).removeValue().addOnSuccessListener {
            binding.deleteData.text?.clear()
            Toast.makeText(this, "Data succesfully deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Unable to delete data", Toast.LENGTH_SHORT).show()
        }
    }
}