package com.nicholas.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addbtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create hooks for the view
        addbtn=(FloatingActionButton)findViewById(R.id.sharenote);
        recyclerView=(RecyclerView)findViewById(R.id.recycler);

        //when you click the add note button you will see a new note dialog popup
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogAddNotes();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void showDialogAddNotes() {
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_note);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        dialog.setCancelable(true);
        WindowManager.LayoutParams Ip=new WindowManager.LayoutParams();
        Ip.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        Ip.width=WindowManager.LayoutParams.MATCH_PARENT;
        Ip.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(Ip);

        //code for closing the dialog once the close button is clicked
        ImageButton imageButton=dialog.findViewById(R.id.close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //create variables and hooks for views in the the addnotes dialog
        EditText editText=dialog.findViewById(R.id.editnotes);
        Button addbtn=dialog.findViewById(R.id.add_btn);

        //set on an onclicklistener on the add button that will add it to firebase
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first check the notes area is empty and if so then throew an error
                if(TextUtils.isEmpty(editText.getText())){
                    editText.setError("Can not add an empty note");
                }else{
                    addNoteToFirebase(editText.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private void addNoteToFirebase(String text) {
        //this function is going text on to firebase database
        //go to tools firebase and check on the write to your database and copy the scripts of code here
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Notes");
        String id=myRef.push().getKey();


        //create an object for the notesmodel class
        NotesModel notesModel=new NotesModel(id,text);

        myRef.push().setValue("notesModel").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"note successfully added",Toast.LENGTH_SHORT).show();

            }
        });

    }
}