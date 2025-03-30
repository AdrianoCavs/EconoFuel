package com.cavstecnologia.econofuel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cavstecnologia.econofuel.databinding.ActivityMainBinding
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding;
    private var pressedButton : Int = 0;
    private var firstFuel : Int = 0;


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

        binding.btCalculateFuel.setOnClickListener {
            var fuelConsumption1 : String = binding.etFuelConsumption1?.text.toString().trim();
            var fuelConsumption2 : String = binding.etFuelConsumption2?.text.toString().trim();
            var fuelCost1 : String = binding.etFuelCost1.text.toString().trim();
            var fuelCost2 : String = binding.etFuelCost2.text.toString().trim();

            if (fuelConsumption1.isEmpty() || fuelConsumption2.isEmpty() || fuelCost1.isEmpty() || fuelCost2.isEmpty()){
                Toast.makeText(this, R.string.empty_field, Toast.LENGTH_LONG).show();
            }else{
                if(firstFuel <= 1){ //se o primeiro combustível for gasolina ou não tiver sido selecionado
                    val gasolinePrice : Double = binding.etFuelCost1.text.toString().trim().toDouble();
                    val ethanolPrice : Double = binding.etFuelCost2.text.toString().trim().toDouble();
                    val gasolineAutonomy : Double = binding.etFuelConsumption1?.text.toString().trim().toDouble();
                    val ethanolAutonomy : Double = binding.etFuelConsumption2?.text.toString().trim().toDouble();
                    calculateBestFuel(gasolinePrice, ethanolPrice,gasolineAutonomy, ethanolAutonomy);
                }else if (firstFuel == 2){ //se o primeiro combustível for etanol
                    val gasolinePrice : Double = binding.etFuelCost2.text.toString().trim().toDouble();
                    val ethanolPrice : Double = binding.etFuelCost1.text.toString().trim().toDouble();
                    val gasolineAutonomy : Double = binding.etFuelConsumption2?.text.toString().trim().toDouble();
                    val ethanolAutonomy : Double = binding.etFuelConsumption1?.text.toString().trim().toDouble();
                    calculateBestFuel(gasolinePrice, ethanolPrice,gasolineAutonomy, ethanolAutonomy);
                }
            }
        }

    }

    fun btSelectFuelOnClick() {
        val intent = Intent(this, ListActivity::class.java);
        //startActivity(intent);
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

                    setFuelTextGasolineFirst();
                } else {
                    binding.etFuelConsumption1?.setText(ethanolAutonomy.toString());
                    binding.etFuelConsumption2?.setText(gasolineAutonomy.toString());

                    setFuelTextEthanolFirst();
                }
                //binding.etFuelConsumption1.setText(returnCod)
            } else {//caso tenha sido pressionado o segundo botão
                if (returnCod == 1) { //faz o inverso do anterior, veja que o etFuelConsumption foi trocado
                    binding.etFuelConsumption2?.setText(gasolineAutonomy.toString());
                    binding.etFuelConsumption1?.setText(ethanolAutonomy.toString());

                    setFuelTextEthanolFirst();
                } else {
                    binding.etFuelConsumption2?.setText(ethanolAutonomy.toString());
                    binding.etFuelConsumption1?.setText(gasolineAutonomy.toString());

                    setFuelTextGasolineFirst();
                }
            }

        }
    }//termina getResult

    private fun setFuelTextGasolineFirst(){
        binding.tvFuelConsumption1.setText(R.string.gasoline_consumption);
        binding.tvFuelConsumption2.setText(R.string.ethanol_consumption);
        binding.tvFuelValue1.setText(R.string.gasoline_cost);
        binding.tvFuelValue2.setText(R.string.ethanol_cost)

        firstFuel = 1;
        binding.etFuelCost1.requestFocus();
    }

    private fun setFuelTextEthanolFirst(){
        binding.tvFuelConsumption2.setText(R.string.gasoline_consumption);
        binding.tvFuelConsumption1.setText(R.string.ethanol_consumption);
        binding.tvFuelValue2.setText(R.string.gasoline_cost);
        binding.tvFuelValue1.setText(R.string.ethanol_cost)

        firstFuel = 2;
        binding.etFuelCost1.requestFocus();
    }



    private fun calculateBestFuel(gasolinePrice : Double, ethanolPrice : Double, gasolineAutonomy : Double, ethanolAutonomy : Double) {
        val fuelAutonomyRatio : Double = ethanolAutonomy/gasolineAutonomy;
        val fuelPriceRatio : Double = ethanolPrice/gasolinePrice;
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        val efficiency : Int = ((Math.abs(fuelAutonomyRatio - fuelPriceRatio)*100)/fuelAutonomyRatio).roundToInt();

        var resultMsg : String;

        if (firstFuel == 0){ //se não foi utilizado o botão "select" pra selecionar gasolina ou etanol
            builder.setMessage(R.string.no_fuel_specified).setTitle(R.string.notice).setPositiveButton(R.string.ok) { dialog, which ->
                var internDialog : AlertDialog = builder.create();
                if (fuelPriceRatio <= fuelAutonomyRatio){  //Se o custo do etanol for de aproximadamente 0,7 ou 70% do valor da gasolina, saberá com o calculo anterior
                    if (efficiency == 0) resultMsg = getString(R.string.no_difference);
                    else resultMsg = getString(R.string.fuel_2_better, efficiency.toString());

                    builder.setMessage(resultMsg).setTitle(R.string.result).setPositiveButton("OK"){dialog, which ->};
                    internDialog = builder.create();
                    internDialog.show();

                }else{
                    if (efficiency == 0) resultMsg = getString(R.string.no_difference);
                    else resultMsg = getString(R.string.fuel_1_better, efficiency.toString());

                    builder.setMessage(resultMsg).setTitle(R.string.result).setPositiveButton("OK"){dialog, which ->};
                    internDialog = builder.create();
                    internDialog.show();
                }

            };
            val dialog: AlertDialog = builder.create();
            dialog.show();




        }else{
            if (fuelPriceRatio <= fuelAutonomyRatio){  //Se o custo do etanol for de aproximadamente 0,7 ou 70% do valor da gasolina, saberá com o calculo anterior
                if (efficiency == 0) resultMsg = getString(R.string.no_difference);
                else resultMsg = getString(R.string.ethanol_better, efficiency.toString());

                builder.setMessage(resultMsg).setTitle(R.string.result).setPositiveButton("OK") { dialog, which -> };
                val dialog: AlertDialog = builder.create();
                dialog.show();

            }else{
                if (efficiency == 0) resultMsg = getString(R.string.no_difference);
                else resultMsg  = getString(R.string.gasoline_better, efficiency.toString());
                builder.setMessage(resultMsg).setTitle(R.string.result).setPositiveButton("OK") { dialog, which -> };
                val dialog: AlertDialog = builder.create();
                dialog.show();
            }
        }





    }






}