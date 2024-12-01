package com.example.billgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Toolbar myToolbar;

    TextView txtOutput;
    EditText input_kwH, input_Rebate;
    Button btnGenerator, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        txtOutput = findViewById(R.id.txtOutput);
        input_kwH = findViewById(R.id.input_kwH);
        input_Rebate = findViewById(R.id.input_Rebate);
        btnGenerator = findViewById(R.id.btnGenerator);
        btnClear = findViewById(R.id.btnClear);

        // Calculate button functionality
        btnGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Taking values from EditText
                String inputkwh = input_kwH.getText().toString();
                String inputRebate = input_Rebate.getText().toString();

                // Check if inputs are empty
                if (inputkwh.isEmpty() || inputRebate.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both KWh and Rebate values.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert to double
                double KWh = Double.parseDouble(inputkwh);
                double Rebate = Double.parseDouble(inputRebate);

                // Calculate total charges
                double totalCharges = 0.0;
                double afterRebate = 0.0;
                if (KWh <= 200) {
                    totalCharges = KWh * 0.218;
                } else if (KWh > 200 && KWh <= 300) {
                    totalCharges = ((KWh - 200) * 0.334) + (200 * 0.218);
                } else if (KWh > 300 && KWh <= 600) {
                    totalCharges = ((KWh - 300) * 0.516) + (200 * 0.218) + (100 * 0.334);
                } else if (KWh > 600) {
                    totalCharges = (KWh - 600) * 0.546 + (200 * 0.218) + (100 * 0.334) + (300 * 0.516);
                }
                afterRebate = totalCharges - (totalCharges * (Rebate/100));

                // Display the result
                txtOutput.setText("RM"+Double.toString(afterRebate));
            }
        });

        // Clear button functionality
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtOutput.setText("0.0");
                input_kwH.setText("");
                input_Rebate.setText("");
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnUrl), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "You can download my application at - https://...");
            startActivity(Intent.createChooser(shareIntent, null));

            return true;
        } else if (item.getItemId() == R.id.item_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }

        return false;

    }
}