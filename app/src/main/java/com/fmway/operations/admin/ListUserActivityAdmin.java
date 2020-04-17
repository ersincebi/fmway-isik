

package com.fmway.operations.admin;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.fmway.R;
        import com.fmway.operations.commonActivities.SignUpLoginActivity;
        import com.parse.FindCallback;
        import com.parse.LogOutCallback;
        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;
        import com.parse.ParseUser;

        import java.util.ArrayList;
        import java.util.List;

        import androidx.appcompat.app.AppCompatActivity;

public class ListUserActivityAdmin extends AppCompatActivity {

    ListView listView;
    ArrayList<String> objectIdFromParse;
    ArrayList<String> usernameFromParse;
    ArrayList<String> emailFromParse;
    ArrayList<String> nameFromParse;
    ArrayList<String> surnameFromParse;
    String selected=null;


    PostActivityAdminUser PostActivityAdminUser ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SignUpLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listuser_activity);

        ParseUser user = new ParseUser();  //kullanılmıyor.

        listView = findViewById(R.id.listUserList);

        objectIdFromParse= new ArrayList<>();
        usernameFromParse= new ArrayList<>();
        emailFromParse= new ArrayList<>();
        nameFromParse=new ArrayList<>();
        surnameFromParse=new ArrayList<>();





        PostActivityAdminUser= new PostActivityAdminUser(objectIdFromParse,usernameFromParse,emailFromParse,nameFromParse,surnameFromParse,this);

        download();
        listView.setAdapter(PostActivityAdminUser);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



           //     selected = ((TextView) view.findViewById(R.id.customView_objectId)).getText().toString();




                Intent myIntent= new Intent(ListUserActivityAdmin.this, UserDetailsAdminActivity.class);
                myIntent.putExtra("objectID", selected);
                startActivity(myIntent);
            }
        });


    }
    public void download(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }else{

                    if(objects.size()>0){
                        for(ParseObject object: objects){


                            objectIdFromParse.add(object.getObjectId());
                            usernameFromParse.add(object.getString("username"));
                            emailFromParse.add(object.getString("Email"));
                            nameFromParse.add(object.getString("Name"));
                            surnameFromParse.add(object.getString("Surname"));



                            PostActivityAdminUser.notifyDataSetChanged();
                        }
                    }
                }
            }


        });
    }

}
