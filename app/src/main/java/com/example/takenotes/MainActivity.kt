package com.example.takenotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.takenotes.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity(), NotesAdapter.NotesitemclickListner, PopupMenu.OnMenuItemClickListener {
    lateinit var viewModal: NoteViewModal
    private lateinit var database: NoteDatabase
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote : Note
    private lateinit var binding: ActivityMainBinding
    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

        if (result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){

                viewModal.updateNote(note)
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        viewModal = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModal::class.java)

        viewModal.allNotes.observe(this){list ->

            list?.let{
                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI() {

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = NotesAdapter(this,this)
        binding.rv.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if (result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null){

                    viewModal.insertNote(note)
                }
            }
        }

        binding.floatingActionButton.setOnClickListener{

            val intent= Intent(this,AddNotes::class.java)
            getContent.launch((intent))

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){

                    adapter.filterList(newText)

                }
                return true
            }

        })
    }

    override fun onitemClicked(note: Note) {
        val intent= Intent(this@MainActivity,AddNotes::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onlongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {

        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
      if (item?.itemId == R.id.delete_note){

          viewModal.deleteNote(selectedNote)
          return true

      }
        return false
    }
}
