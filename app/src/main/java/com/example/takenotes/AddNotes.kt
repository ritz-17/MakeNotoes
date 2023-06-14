package com.example.takenotes
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import com.example.takenotes.databinding.ActivityAddNotesBinding
import com.example.takenotes.databinding.ActivityMainBinding
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter

class AddNotes : AppCompatActivity() {
    private lateinit var binding: ActivityAddNotesBinding

    private lateinit var note: Note
    private lateinit var old_note: Note

    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_note = intent.getSerializableExtra("current_note")as Note
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            var note = binding.etNote.text.toString()

            if (title.isNotEmpty() || note.isNotEmpty()) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val date = java.util.Date()
                val noteObject = if (isUpdate) {
                    Note(
                        old_note.id,
                        title,
                        note,
                        formatter.format(date)
                    )
                } else {
                    Note(
                        null,
                        title,
                        note,
                        formatter.format(date)
                    )
                }

                val intent = Intent()
                intent.putExtra("note", noteObject)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@AddNotes, "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}