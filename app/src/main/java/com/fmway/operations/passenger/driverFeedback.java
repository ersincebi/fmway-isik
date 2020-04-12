package com.fmway.operations.passenger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fmway.R;
import com.fmway.libs.*;

public class driverFeedback extends AppCompatActivity {

    // Library declarations
    private Commons commons = new Commons();

    private TextView feedbackText;
    private RatingBar qualityRating;
    private RatingBar communicationRating;
    private RatingBar priceRating;
    private Button feedbackSend;
    private EditText commentText;

    /**
     * before call this activity
     * this activity requires two parameter via intent
     * @param personName is drivers name
     * @param personId is drivers unique id
     * function will create a json formatted data like:
     *                 {
     *                      "driverId": driverId => "who is rated"
     *                      ,"passengerId": passengerId => "who is been rated"
     *                      ,"status": float => "the rating can be 0.0 to 5.0"
     *                      ,"comment": text => "this is the passenger thoughts about the driver"
     *                      ,"date": date => "when is rated"
     *                 }
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_feedback);

        String driverName = getIntent().getStringExtra("personName");
        final String personId = getIntent().getStringExtra("personId");

        feedbackText = (TextView)findViewById(R.id.feedbackText);
        feedbackText.setText("How was your trip with " + driverName + "?");

        qualityRating = (RatingBar)findViewById(R.id.quality);
        communicationRating = (RatingBar)findViewById(R.id.communication);

        commentText = (EditText)findViewById(R.id.commentText);

        feedbackSend = (Button)findViewById(R.id.send);
        feedbackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float qualityValue = qualityRating.getRating();
                float communicationValue = communicationRating.getRating();
                String comment = String.valueOf(commentText.getText());


                /**
                 * TODO: will be deleted after implementation
                 * dummy display of ration value
                 */
                System.out.println("\nCurrent Date: "
                        + commons.currentDate("yyyy-MM-dd")
                        +"\nid of driver :"
                        + personId
                        + "\nRating of the driver qualityRating: "
                        + qualityValue
                        + "\nRating of the driver communicationRating: "
                        + communicationValue
                        + "\nPassenger comment to driver: "
                        + comment);
            }
        });
    }
}