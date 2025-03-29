package com.cavstecnologia.econofuel

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cavstecnologia.econofuel.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding;
    private var pressedButton : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater); //adicionamos a inicializacao do binding aqui
        setContentView(binding.root) //e trocamos a inicializacao do layout abaixo pelo binding
        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btSelectFuel1.setOnClickListener {
            pressedButton = 1;
            btSelectFuelOnClick();
        }
        binding.btSelectFuel2.setOnClickListener {
            pressedButton = 2;
            btSelectFuelOnClick();
        }

    }

    fun btSelectFuelOnClick() {
        val intent = Intent(this, ListActivity::class.java);
        startActivity(intent);

        getResult.launch(intent);
    }

    //listener para esperar o resultado de retorno da tela activity_list (gasoline = 1, ethanol = 2)
    private val getResult = registerForActivityResult ( ActivityResultContracts.StartActivityForResult() ) {

        if (it.resultCode == RESULT_OK) {
            val returnCod = it.data?.getIntExtra("returnCod", 0)
            val gasolineAutonomy = it.data?.getDoubleExtra("gasolineAutonomy", 0.0);
            val ethanolAutonomy = it.data?.getDoubleExtra("ethanolAutonomy", 0.0);

            if (pressedButton == 1) { //se foi pressionado o primeiro botão
                if (returnCod == 1) { //se o selecionado foi o 1º item (gasolina)
                    binding.etFuelConsumption1?.setText(gasolineAutonomy.toString());
                    binding.etFuelConsumption2?.setText(ethanolAutonomy.toString());
                } else {
                    binding.etFuelConsumption1?.setText(ethanolAutonomy.toString());
                    binding.etFuelConsumption2?.setText(gasolineAutonomy.toString());
                }
                //binding.etFuelConsumption1.setText(returnCod)
            } else {//caso tenha sido pressionado o segundo botão
                if (returnCod == 1) { //faz o inverso do anterior, veja que o etFuelConsumption foi trocado
                    binding.etFuelConsumption2?.setText(gasolineAutonomy.toString());
                    binding.etFuelConsumption1?.setText(ethanolAutonomy.toString());
                } else {
                    binding.etFuelConsumption2?.setText(ethanolAutonomy.toString());
                    binding.etFuelConsumption1?.setText(gasolineAutonomy.toString());
                }
            }

        }
    }//termina getResult



}