package io.github.sachithariyathilaka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.sachithariyathilaka.AuthPreference;
import com.github.sachithariyathilaka.resource.Header;
import com.github.sachithariyathilaka.resource.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity java class.
 *
 * @author  Sachith Ariyathilaka
 * @version 1.0.0
 * @since   2024/05/28
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUserStatus();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(onSubmit());
    }

    /**
     * Set user status dropdown spinner.
     */
    private void setUserStatus() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner userStatus = findViewById(R.id.userStatus);
        userStatus.setAdapter(adapter);
    }

    /**
     * Handle click event of submit button.
     *
     * @return  the on click listener
     */
    private View.OnClickListener onSubmit() {
        return v -> {
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
            v.setVisibility(View.GONE);

            String message;
            EditText userId = findViewById(R.id.userId);
            Spinner userStatus = findViewById(R.id.userStatus);
            EditText header1Title = findViewById(R.id.header1_title);
            EditText header2Title = findViewById(R.id.header2_title);
            EditText header1Value = findViewById(R.id.header1_value);
            EditText header2Value = findViewById(R.id.header2_value);

            try {

                if (validateRequest(userId, userStatus, header1Title, header2Title, header1Value, header2Value))
                {
                    UserDetail userDetail = new UserDetail(userId.getText().toString(), userStatus.getSelectedItem().toString().equals("Active"));
                    AuthPreference.saveUserDetails(this, userDetail);

                    List<Header> headers = new ArrayList<>();
                    headers.add(new Header(header1Title.getText().toString(), header1Value.getText().toString()));
                    headers.add(new Header(header2Title.getText().toString(), header2Value.getText().toString()));
                    AuthPreference.saveHeaders(this, headers);

                    resetForm(userId, userStatus, header1Title, header2Title, header1Value, header2Value);
                    message = "Data saved successfully!";
                } else
                    message = "Please fill all required fields.";

            } catch (Exception ex) {
                message = "Error occurred while saving the data. " + ex.getMessage();
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            v.setVisibility(View.VISIBLE);
        };
    }

    /**
     * Reset form after submission.
     *
     * @param   userId the user id edit text
     * @param   userStatus the user status spinner
     * @param   header1Title the header 1 title edit text
     * @param   header2Title the header 2 title edit text
     * @param   header1Value the header 1 value edit text
     * @param   header2Value the header 2 value edit text
     */
    private void resetForm(EditText userId, Spinner userStatus, EditText header1Title, EditText header2Title, EditText header1Value, EditText header2Value) {
        userId.setText("");
        userStatus.setSelection(0);
        header1Title.setText("");
        header1Value.setText("");
        header2Title.setText("");
        header2Value.setText("");
    }

    /**
     * Validate request.
     *
     * @param   userId the user id edit text
     * @param   userStatus the user status spinner
     * @param   header1Title the header 1 title edit text
     * @param   header2Title the header 2 title edit text
     * @param   header1Value the header 1 value edit text
     * @param   header2Value the header 2 value edit text
     *
     * @return  the boolean
     */
    private boolean validateRequest(EditText userId, Spinner userStatus, EditText header1Title, EditText header2Title, EditText header1Value, EditText header2Value) {

        if (userId.getText().toString().isEmpty() || userId.getText().toString().isBlank())
            return false;

        if (userStatus.getSelectedItem().toString().equals("Select User Status"))
            return false;

        if (header1Title.getText().toString().isEmpty() || header1Title.getText().toString().isBlank())
            return false;

        if (header2Title.getText().toString().isEmpty() || header2Title.getText().toString().isBlank())
            return false;

        if (header1Value.getText().toString().isEmpty() || header1Value.getText().toString().isBlank())
            return false;

        if (header2Value.getText().toString().isEmpty() || header2Value.getText().toString().isBlank())
            return false;

        return true;
    }
}