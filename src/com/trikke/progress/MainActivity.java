package com.trikke.progress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.trikke.progress.widget.ProgressButton;

public class MainActivity extends Activity
{
	private Handler handler = new Handler();

	private ProgressButton mProgressButton;
	private ProgressButton mUltraButton;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );
		mProgressButton = (ProgressButton) findViewById( R.id.button );

		mProgressButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				switchStatesAndLoad( (ProgressButton) view );
			}
		} );

		mUltraButton = (ProgressButton) findViewById( R.id.utlrabutton );

		mUltraButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				switchStatesAndLoad( (ProgressButton) view );
			}
		} );
	}

	private void switchStatesAndLoad( final ProgressButton view)
	{
		if (view.isLoading())
		{
			view.disableLoadingState();
		}
		else
		{
			view.enableLoadingState();

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					view.disableLoadingState();
					Toast.makeText( getApplicationContext(), "Something was loaded!", Toast.LENGTH_LONG ).show();
				}
			}, 2000);
		}
	}
}
