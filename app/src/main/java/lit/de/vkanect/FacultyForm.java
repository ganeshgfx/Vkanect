package lit.de.vkanect;

import static lit.de.vkanect.data.CONSTANTS.Firebase.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FacultyForm extends AppCompatActivity {


    EditText add_branch;
    ChipGroup chipGroup;
    Set<String> branchList = new HashSet<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);
        chipGroup = findViewById(R.id.branches);
        add_branch = findViewById(R.id.add_branch);
        findViewById(R.id.button_add_branch).setOnClickListener(v->{
            String branch = add_branch.getText().toString();
            if(!branch.isEmpty() && !branchList.contains(branch)){
                Chip chip = new Chip(this);
                chip.setText(branch);
                chip.setChipIconResource(R.drawable.ic_baseline_close_24);
                chip.setChipIconTintResource(R.color.icon);
                chip.setOnClickListener(c->{
                    chip.setVisibility(View.GONE);
                    branchList.remove(chip.getText().toString());
                    //Toast.makeText(this, branchList.toString(), Toast.LENGTH_SHORT).show();
                });
                chipGroup.addView(chip);
                add_branch.setText("");
                branchList.add(branch);
            }
        });
        findViewById(R.id.button_next_form).setOnClickListener(v->{
            if(!branchList.isEmpty()){

            }else {
                Toast.makeText(this, "Add Branches first.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}