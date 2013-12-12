package hu.mobilefriends.kepp;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private int imgCount;
	
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
              
    // which image properties are we querying
       String[] projection = new String[]{
               MediaStore.Images.Media._ID,
               MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
               MediaStore.Images.Media.DATE_TAKEN,
               MediaStore.Images.Media.DATA
       };
       
    // Get the base URI for the People table in the Contacts content provider.
       Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

       // Make the query.
       final Cursor cur = this.getContentResolver().query(images,
               projection, // Which columns to return
               "",         // Which rows to return (all rows)
               null,       // Selection arguments (none)
               ""          // Ordering
               );
       
       imgCount = cur.getCount();

       if (cur.moveToFirst()) {
    	   
    	   getRandomImg(cur);
    	   
       }
       
       ImageView imageView = (ImageView) findViewById(R.id.imgView);
       imageView.setOnClickListener(new View.OnClickListener() {
           
           @Override
           public void onClick(View arg0) {                        	          	  
        	   
        	   getRandomImg(cur);
           }
       });
   }
   
   @Override
   public void onBackPressed() {
   }    
   
   public void onWindowFocusChanged(boolean hasFocus) {
	    super.onWindowFocusChanged(hasFocus);


	    Log.d("Focus debug", "Focus changed !");

	if(!hasFocus) {
	    Log.d("Focus debug", "Lost focus !");

	    Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	    sendBroadcast(closeDialog);
	}
	}
    
   private void getRandomImg(Cursor cur) 
   {
	   Random r = new Random();
	   int stepCount = r.nextInt(imgCount-1) + 1;
	   
	   for (int i=1; i<=stepCount; i++)
	   {
		   cur.moveToNext();
		   if (cur.isAfterLast())
		   {
			   cur.moveToFirst();
		   }
	   } 
	   
	   int columnIndex = cur.getColumnIndex(MediaStore.Images.Media.DATA);
       String picPath = cur.getString(columnIndex);
       
       ImageView imageView = (ImageView) findViewById(R.id.imgView);
       
       Bitmap img = BitmapFactory.decodeFile(picPath);
    		   //Bitmap.createScaledBitmap(, 200, 200, false);
       
       Point size = new Point();
       
       Display display = getWindowManager().getDefaultDisplay(); 
       display.getSize(size);
       
       float factorH = size.y / (float) img.getWidth();
       float factorW = size.x / (float) img.getWidth();
       float factorToUse = (factorH > factorW) ? factorW : factorH;
       img = Bitmap.createScaledBitmap(img, (int) (img.getWidth() * factorToUse), (int) (img.getHeight() * factorToUse), false);
       
       imageView.setImageBitmap(img);
   }
}