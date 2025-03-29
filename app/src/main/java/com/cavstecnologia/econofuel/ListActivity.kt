package com.cavstecnologia.econofuel

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cavstecnologia.econofuel.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private lateinit var lvFuels : ListView;
    private lateinit var binding : ActivityListBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvFuels)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
        binding.lvFuels.setOnItemClickListener { parent, view, position, id ->
            val selectedItemId = position + 1;
            val gasolineAutonomy = 10.12;
            val ethanolAutonomy = 8.98;
            intent.putExtra("returnCod", selectedItemId);
            intent.putExtra("gasolineAutonomy", gasolineAutonomy);
            intent.putExtra("ethanolAutonomy", ethanolAutonomy)
            setResult(RESULT_OK, intent);
            finish();
        }*/

        lvFuels = findViewById(R.id.lvFuels);
        lvFuels.setOnItemClickListener {parent, view, position, id ->
            val selectedItemId = position + 1;
            val gasolineAutonomy = 10.12;
            val ethanolAutonomy = 8.98;
            intent.putExtra("returnCod", selectedItemId);
            intent.putExtra("gasolineAutonomy", gasolineAutonomy);
            intent.putExtra("ethanolAutonomy", ethanolAutonomy)
            setResult(RESULT_OK, intent);
            finish();
        }

    }
}