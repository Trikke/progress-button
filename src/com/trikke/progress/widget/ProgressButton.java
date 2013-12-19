package com.trikke.progress.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import com.trikke.progress.R;

import java.util.Arrays;

public class ProgressButton extends Button
{

	private static final int MAX_LEVEL = 10000;
	private Animation animation;
	private boolean autoDisableClickable;

	private boolean loading;
	private Drawable[] loadingDrawables; // L T R B
	private boolean mShouldStartAnimationDrawable;

	private Drawable[] oldCompoundDrawables;
	private Transformation transformation;

	private String loadingText;
	private String defaultText;

	public ProgressButton( Context context )
	{
		super( context );
		init( context );
	}

	public ProgressButton( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		init( context, attrs );
	}

	public ProgressButton( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		init( context, attrs );
	}

	public void disableLoadingState()
	{
		if ( oldCompoundDrawables != null )
		{
			setCompoundDrawablesWithIntrinsicBounds( oldCompoundDrawables[0], oldCompoundDrawables[1], oldCompoundDrawables[2], oldCompoundDrawables[3] );
		}
		if ( autoDisableClickable )
		{
			setClickable( true );
		}
		loading = false;
		checkState();
	}

	public void enableLoadingState()
	{
		oldCompoundDrawables = Arrays.copyOf( getCompoundDrawables(), 4 );
		setCompoundDrawables( loadingDrawables[0] == null ? oldCompoundDrawables[0] : loadingDrawables[0],
							  loadingDrawables[1] == null ? oldCompoundDrawables[1] : loadingDrawables[1],
							  loadingDrawables[2] == null ? oldCompoundDrawables[2] : loadingDrawables[2],
							  loadingDrawables[3] == null ? oldCompoundDrawables[3] : loadingDrawables[3]);
		if ( autoDisableClickable )
		{
			setClickable( false );
		}
		loading = true;
		checkState();
	}

	private void init( Context context )
	{
		loadingDrawables = new Drawable[]{};
	}

	private void init( Context context, AttributeSet attrs )
	{
		TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.ProgressButton );

		Drawable loadingDrawableLeft = null;
		Drawable loadingDrawableTop = null;
		Drawable loadingDrawableRight = null;
		Drawable loadingDrawableBottom = null;

		if ( a.hasValue( R.styleable.ProgressButton_loadingDrawableLeft ) )
		{
			loadingDrawableLeft = a.getDrawable( R.styleable.ProgressButton_loadingDrawableLeft );
		}

		if ( a.hasValue( R.styleable.ProgressButton_loadingDrawableTop ) )
		{
			loadingDrawableTop = a.getDrawable( R.styleable.ProgressButton_loadingDrawableTop );
		}

		if ( a.hasValue( R.styleable.ProgressButton_loadingDrawableRight ) )
		{
			loadingDrawableRight = a.getDrawable( R.styleable.ProgressButton_loadingDrawableRight );
		}

		if ( a.hasValue( R.styleable.ProgressButton_loadingDrawableBottom ) )
		{
			loadingDrawableBottom = a.getDrawable( R.styleable.ProgressButton_loadingDrawableBottom );
		}

		loadingDrawables = new Drawable[]{loadingDrawableLeft, loadingDrawableTop, loadingDrawableRight, loadingDrawableBottom};

		loadingText = "";
		defaultText = getText().toString();

		if ( a.hasValue( R.styleable.ProgressButton_loadingText ) )
		{
			loadingText = (a.getString( R.styleable.ProgressButton_loadingText ));
		}

		if ( a.hasValue( R.styleable.ProgressButton_loading ) )
		{
			loading = a.getBoolean( R.styleable.ProgressButton_loading, false );
		}

		if ( a.hasValue( R.styleable.ProgressButton_autoDisableClickable ) )
		{
			autoDisableClickable = a.getBoolean( R.styleable.ProgressButton_autoDisableClickable, false );
		}

		a.recycle();

		if ( hasAloadingDrawable() )
		{
			configureLoadingDrawables();

			if ( loading )
			{
				enableLoadingState();
			} else
			{
				disableLoadingState();
			}
		}
	}

	private void checkState()
	{
		if ( !loading )
		{
			setText( defaultText );
		} else
		{
			setText( loadingText );
		}
	}

	private boolean hasAloadingDrawable()
	{
		int i;
		for (i=0; i < loadingDrawables.length; i++)
		{
			if (loadingDrawables[i] != null)
				return true;
		}

		return false;
	}

	private void configureLoadingDrawables()
	{
		int i;
		Drawable d;
		for (i=0; i < loadingDrawables.length; i++)
		{
			d = loadingDrawables[i];
			if (d != null)
			{
				d.setBounds( 0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight() );
				if ( !(d instanceof Animatable) )
				{
					animation = new AlphaAnimation( 0f, 1f );
					animation.setRepeatMode( Animation.RESTART );
					animation.setRepeatCount( Animation.INFINITE );
					animation.setDuration( 1000 );
					animation.setInterpolator( new LinearInterpolator() );
					animation.setStartTime( Animation.START_ON_FIRST_FRAME );
				}
			}
		}
		transformation = new Transformation();
	}

	/**
	 * @return true if a loading animation is shown
	 */
	public boolean isLoading()
	{
		return loading;
	}

	@Override
	protected void onDraw( Canvas canvas )
	{
		super.onDraw( canvas );

		if ( loading )
		{
			long time = getDrawingTime();
			int i;
			Drawable d;
			for (i=0; i < loadingDrawables.length; i++)
			{
				d = loadingDrawables[i];
				if (d != null)
				{
					if ( !(d instanceof Animatable) )
					{
						animation.getTransformation( time, transformation );
						float scale = transformation.getAlpha();
						try
						{
							d.setLevel( (int) (scale * MAX_LEVEL) );
						} finally
						{
						}
						ViewCompat.postInvalidateOnAnimation( this );
					}
					if ( mShouldStartAnimationDrawable && d instanceof Animatable )
					{
						((Animatable) d).start();
						mShouldStartAnimationDrawable = false;
					}
				}
			}
		}
	}

	/**
	 * Enables disables loading, and sets the selected / unselected state.
	 * Enables loading when state equals null, and selects / unselects the
	 * button based on the states value.
	 *
	 * @param state , null - enables loading, true/ false - disables loading, sets
	 *              selected/ unselected
	 */
	public void setLoadingState( Boolean state )
	{
		if ( state )
		{
			enableLoadingState();
		} else
		{
			disableLoadingState();
		}
	}

	@Override
	public void setSelected( boolean selected )
	{
		super.setSelected( selected );
		checkState();
	}
}