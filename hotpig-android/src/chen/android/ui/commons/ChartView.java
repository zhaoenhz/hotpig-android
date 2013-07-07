package chen.android.ui.commons;

import java.util.List;

import chen.android.R;
import chen.android.data.CheckinStatus;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChartView extends LinearLayout{

	public ChartView(Context context) {
		super(context);
		init();
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		if(Build.VERSION.SDK_INT >= 16){
		    setBackground(getResources().getDrawable(R.drawable.chart_bg));
		} else{
		    setBackgroundDrawable(getResources().getDrawable(R.drawable.chart_bg));
		}
		setOrientation(LinearLayout.HORIZONTAL);
	}

	public void setCheckinStatus(List<CheckinStatus> list){
		this.removeAllViews();
		for(CheckinStatus status : list){
			TextView view = null;
			if(status.isEmpty()){
				view = generate(R.drawable.chart_empty);
			} else if(status.failed){
				view = generate(R.drawable.chart_failed);	
			} else {
				switch(status.star){
				case one : view = generate(R.drawable.chart_one);break;
				case two : view = generate(R.drawable.chart_two);break;
				case three : view = generate(R.drawable.chart_three);break;
				case four : view = generate(R.drawable.chart_four);break;
				case five : view = generate(R.drawable.chart_five);break;
				}
			}
			view.setText(String.valueOf(status.progress));
			view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			view.setTextColor(Color.WHITE);
			view.setIncludeFontPadding(false);
			view.setTypeface(null, Typeface.BOLD);
			view.setTextSize(UiUtils.getRawSize(TypedValue.COMPLEX_UNIT_SP, 12));
			this.addView(view);
		}
	}
	
	private TextView generate(int drawable){
		TextView view = new TextView(getContext());
		view.setLayoutParams(new LayoutParams((int) UiUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, 48), LayoutParams.MATCH_PARENT));
		if(Build.VERSION.SDK_INT >= 16){
			view.setBackground(getResources().getDrawable(drawable));
		} else{
			view.setBackgroundDrawable(getResources().getDrawable(drawable));
		}
		return view;
	}
}
