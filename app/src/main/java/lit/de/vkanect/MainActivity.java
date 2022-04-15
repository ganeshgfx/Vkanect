package lit.de.vkanect;

import static lit.de.vkanect.data.CONSTANTS.Firebase.FAC_getInstitude;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import lit.de.vkanect.data.User;
import lit.de.vkanect.faculty.faculty_activity;
import lit.de.vkanect.student.student_activity;

public class MainActivity extends AppCompatActivity {
    LinearLayout login_layout,sign_up_layout;
    boolean login_signup_flip = true;
    FirebaseUser user;
    FirebaseFirestore db;
    View view;

    //loading
    PopupWindow loadingPopup;

    //TextBoxs
    EditText s_email,user_name,new_password,new_password_confirm,user_email_id,password;
    //Button
    Button sign_up,login;
    RadioButton student,faculty;
    String uType;

    //auth
    FirebaseAuth mAuth;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        context = this.context;


        if(user == null){
            Log.d("TAG","Not in");
        }
        else{
            if (user.isEmailVerified())
            {
                letLogin();
                // user is verified, so you can finish this activity or send user to activity which you want.
            }
            else
            {
                Log.d("TAG", "isEmailVerified:false");
//                verifyEmailpop();
                // email is not verified, so just prompt the message to the user and restart this activity.
                // NOTE: don't forget to log out the user.
                FirebaseAuth.getInstance().signOut();

                //restart this activity

            }
        }
        setContentView(R.layout.activity_main);

        s_email = findViewById(R.id.s_email);
        new_password = findViewById(R.id.new_password);
        new_password_confirm = findViewById(R.id.new_password_confirm);
        user_name = findViewById(R.id.user_name);
        student = findViewById(R.id.student);
        faculty = findViewById(R.id.faculty);
        user_email_id = findViewById(R.id.user_email_id);
        password = findViewById(R.id.password);

        login_layout = findViewById(R.id.login_layout);
        sign_up_layout = findViewById(R.id.sign_up_layout);
        //test
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d("TAG","OK");
        }else{
            Log.d("TAG","notOK");
        }
        sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                if(!isValidEmail(s_email.getText().toString()))
                    setTextBoxErr(s_email,"Invalid Email");
//                    s_email.setHint("Invalid Email");
                else if(user_name.getText().toString().isEmpty())
                    setTextBoxErr(user_name,"Input Name");
//                    user_name.setHint("Input Name");
                else if(!new_password.getText().toString().equals(new_password_confirm.getText().toString()))
                    setTextBoxErr(new_password_confirm,"Didn't Match");
//                    new_password_confirm.setHint("Didn't Match");
                else if(new_password_confirm.getText().toString().isEmpty())
                    setTextBoxErr(new_password,"Input Password");
//                    new_password.setHint("Input Password");
                else if(!faculty.isChecked() && !student.isChecked())
                    findViewById(R.id.radio_err).setVisibility(View.VISIBLE);
//                    Log.d("TAG", "SELECT RAdio Station/////!");
                else
                    signup(s_email.getText().toString(), new_password_confirm.getText().toString(),v);
            }
        });
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if()
                showLoading(v);
                login(user_email_id.getText().toString(),password.getText().toString(),v);
            }
        });
        //
        FAC_getInstitude();
    }
    void showLoading(View view){
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.loading, null);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            loadingPopup = new PopupWindow(popupView, width, height, focusable);
            loadingPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    void hideLoading(){
        loadingPopup.dismiss();
    }
    void login(String email,String pass,View view){

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        hideLoading();
                        if (!task.isSuccessful()) {
                            //Log.d("TAG", "signInWithEmail:failed", task.getException());
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidUserException e) {
                                setTextBoxErr(user_email_id,"The password is invalid or the user does not have a password");
                                setTextBoxErr(password,"");
                            } catch(Exception e) {
                                Log.e("TAG", e.getMessage());
                                setTextBoxErr(user_email_id,"Invalid password or maybe Invalid email");
                                setTextBoxErr(password,"");
                            }

                        } else {
                            //checkIfEmailVerified();
                            Log.d("TAG", "signInWithEmail:true");
                            user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified())
                            {
                                // user is verified, so you can finish this activity or send user to activity which you want.
                                Log.d("TAG", "isEmailVerified:true");
                                letLogin();
//                                startActivity(new Intent(MainActivity.this, student_activity.class));
//                                finish();
                            }
                            else
                            {
                                // email is not verified, so just prompt the message to the user and restart this activity.
                                // NOTE: don't forget to log out the user.
                                verifyEmailpop(view);
                                FirebaseAuth.getInstance().signOut();

                                //restart this activity

                            }
                        }
                        // ...
                    }
                });
    }
    void letLogin(){
        //showLoading(v);
        DocumentReference userData = db.collection("users").document(user.getUid());
        userData
                //.whereEqualTo("type","Faculty")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
//                        for(DocumentSnapshot doc:task.getResult().){
//
//                        }
                        User user = task.getResult().toObject(User.class);
                        if(user.getType().equals("Student"))
                        {
                            startActivity(new Intent(MainActivity.this, student_activity.class));
                        }
                        else
                        {
                            if(false){
                                startActivity(new Intent(MainActivity.this, FacultyForm.class));
                            }else {
                                startActivity(new Intent(MainActivity.this, faculty_activity.class));
                            }

                        }
                        hideLoading();
                        finish();
                        //Log.d("TAG", user.getType());
                    }
                });
    }
    void setTextBoxErr(EditText editText, String Error){
        editText.setHint(Error);
        editText.setHintTextColor(Color.parseColor("#F50057"));
        editText.setText("");
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void change_to_sign_up(View view){
        Switch();
    }
    void Switch(){
        if(login_signup_flip){
            login_layout.setVisibility(View.GONE);
            sign_up_layout.setVisibility(View.VISIBLE);
            login_signup_flip = false;
        }else{
            login_layout.setVisibility(View.VISIBLE);
            sign_up_layout.setVisibility(View.GONE);
            login_signup_flip = true;
        }
    }
    void verifyEmailpop(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.verify_mail_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        final Button popDismis = popupView.findViewById(R.id.popDismis);
        final Button popDismis_sendAgain = popupView.findViewById(R.id.popDismis_sendAgain);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popDismis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popDismis_sendAgain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.sendEmailVerification();
                popupWindow.dismiss();
            }
        });
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void verifyEmail(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("TAG", "verifyEmail: "+user.isEmailVerified()+"\n"+user.getEmail());
        showLoading(view);
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            // email sent

                            // after email is sent just logout the user and finish this activity

                            FirebaseAuth.getInstance().signOut();

                            Switch();
                            verifyEmailpop(view);

//                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());

                        }
                    }
                });
        //s_email.getText().toString()

//        user.sendEmailVerification().addOnCompleteListener(task -> mAuth.addAuthStateListener(firebaseAuth -> {
//            Log.d("TAG", "Verrifying");
//            if(user.isEmailVerified()){
//                login_signup_flip = false;
//
//                CollectionReference collectionReference = db.collection("users");
//                collectionReference.document(user.getUid()).set(new User(
//                        s_email.getText().toString(),
//                        user_name.getText().toString(),
//                        "NULL"
//                ));
//                Log.d("TAG", "Verrifyed");
//                Switch();
//            }
//        }));
        
        //mAuth.addAuthStateListener();
    }
    public void signup(String email,String password,View view){
        showLoading(view);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoading();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName( user_name.getText().toString()).build();

                            user.updateProfile(profileUpdates);

                            // TODO: 22/08/21 Do firestore after email is veryfied 
                            if(faculty.isChecked())
                                uType = "Faculty";
                            if(student.isChecked())
                                uType = "Student";
                            CollectionReference collectionReference = db.collection("users");
                            collectionReference.document(user.getUid()).set(new User(
                                    s_email.getText().toString(),
                                    user_name.getText().toString(),
                                    uType
                            ));

                            verifyEmail(view);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w("TAG", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);

                                try {
                                    throw task.getException();
                                } catch(FirebaseAuthWeakPasswordException e) {
                                    Toast.makeText(context, "Week Password", Toast.LENGTH_SHORT).show();
                                    new_password.setHint("Week Password");
                                } catch(FirebaseAuthInvalidCredentialsException e) {
                                    Toast.makeText(context, "Invalid Email", Toast.LENGTH_SHORT).show();
                                    s_email.setHint("Invalid Email");
                                } catch(FirebaseAuthUserCollisionException e) {
                                    Toast.makeText(context, "Already exits", Toast.LENGTH_SHORT).show();
                                    s_email.setHint("Email Already exits");
                                } catch(Exception e) {
                                    Toast.makeText(getApplicationContext(), "Week Password", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", e.getMessage());
                                }

                        }
                    }
                });
    }
}