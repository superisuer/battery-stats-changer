package com.huker667.bsc;

import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.content.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Vibrator;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.CompoundButton;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.huker667.bsc.databinding.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;
import rikka.shizuku.aidl.*;
import rikka.shizuku.api.*;
import rikka.shizuku.provider.*;
import rikka.shizuku.shared.*;
import java.lang.Process;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import rikka.shizuku.Shizuku;
import android.content.pm.PackageManager;
import android.content.ClipboardManager;
import rikka.shizuku.ShizukuRemoteProcess;

public class MainActivity extends AppCompatActivity {
	
	private MainBinding binding;
	private boolean rooted = false;
	private boolean via_shizuku = false;
	private rikka.shizuku.Shizuku.OnRequestPermissionResultListener shizukuPermissionResultListener;
	private boolean shizukuReady = false;
	private rikka.shizuku.Shizuku.OnBinderReceivedListener shizukuBinderReceivedListener;
	private String hui = "";
	
	private Vibrator vibrator;
	private ObjectAnimator oa = new ObjectAnimator();
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		binding = MainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		binding.radiobutton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				binding.textview10.setVisibility(View.GONE);
				rooted = true;
				try {
					
					Runtime.getRuntime().exec("su"); 
					
					if (_get("echo 123").contains("123")) {
						rooted = true;
						binding.edittext1.setText(_get("cat /sys/class/power_supply/battery/capacity"));
						binding.edittext2.setText(_get("cat /sys/class/power_supply/battery/charge_counter"));
						binding.seekbar1.setProgress((int)Math.round(Double.parseDouble(_get("cat /sys/class/power_supply/battery/temp")) / 10));
						if (_get("dumpsys battery").contains("OPLUS")) {
							binding.textview9.setText(getString(R.string.oplu_battery));
						}
						binding.linear1.setVisibility(View.VISIBLE);
						binding.linear2.setVisibility(View.VISIBLE);
						binding.linear4.setVisibility(View.VISIBLE);
						binding.linear5.setVisibility(View.VISIBLE);
						binding.linear6.setVisibility(View.VISIBLE);
						binding.linear8.setVisibility(View.GONE);
						vibrator.vibrate((long)(50));
						if (_get("dumpsys battery").contains("UPDATES STOPPED")) {
							binding.textview11.setVisibility(View.VISIBLE);
						} else {
							binding.textview11.setVisibility(View.INVISIBLE);
						}
					} else {
						rooted = false;
						binding.textview10.setVisibility(View.VISIBLE);
					}
					
				} catch (Exception e ) {
					
					binding.textview10.setVisibility(View.VISIBLE);
					rooted = false;
					
				}
			}
		});
		
		binding.radiobutton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_requestShizukuPermission();
			}
		});
		
		binding.button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				vibrator.vibrate((long)(50));
				hui = _get("dumpsys battery set level ".concat(binding.edittext1.getText().toString()));
				hui = _get("dumpsys battery set counter ".concat(binding.edittext2.getText().toString()));
				hui = _get("dumpsys battery set temperature ".concat(String.valueOf((long)(binding.seekbar1.getProgress() * 10))));
				if (_get("dumpsys battery").contains("UPDATES STOPPED")) {
					binding.textview11.setVisibility(View.VISIBLE);
				} else {
					binding.textview11.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		binding.button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				vibrator.vibrate((long)(50));
				binding.checkbox2.setChecked(_get("cmd battery get usb").contains("true"));
				hui = _get("dumpsys battery reset");
				binding.edittext1.setText(_get("cmd battery get level"));
				binding.edittext2.setText(_get("cmd battery get counter"));
				binding.seekbar1.setProgress((int)Math.round(Double.parseDouble(_get("cmd battery get temp")) / 10));
				if (_get("dumpsys battery").contains("OPLUS")) {
					binding.textview9.setText(getString(R.string.oplu_battery));
				}
				if (_get("dumpsys battery").contains("UPDATES STOPPED")) {
					binding.textview11.setVisibility(View.VISIBLE);
				} else {
					binding.textview11.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		binding.checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
				final boolean _isChecked = _param2;
				vibrator.vibrate((long)(50));
				if (_isChecked) {
					hui = _get("dumpsys battery set usb 1");
				} else {
					hui = _get("dumpsys battery set usb 0");
				}
				if (_get("dumpsys battery").contains("UPDATES STOPPED")) {
					binding.textview11.setVisibility(View.VISIBLE);
				} else {
					binding.textview11.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		binding.seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				binding.textview7.setText(String.valueOf((long)(_progressValue)).concat(" °C"));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
		
		binding.linear17.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		binding.settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				vibrator.vibrate((long)(20));
				oa.setTarget(binding.settings);
				oa.setPropertyName("rotation");
				oa.setFloatValues((float)(0), (float)(720));
				oa.setDuration((int)(1500));
				oa.setInterpolator(new DecelerateInterpolator());
				oa.start();
				i.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(i);
			}
		});
	}
	
	private void initializeLogic() {
		binding.textview11.setVisibility(View.INVISIBLE);
		binding.textview1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/jetbrainsmono.ttf"), 0);
		binding.textview11.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/jetbrainsmono.ttf"), 0);
		binding.textview9.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/jetbrainsmono.ttf"), 0);
		binding.linear1.setVisibility(View.GONE);
		binding.linear2.setVisibility(View.GONE);
		binding.linear4.setVisibility(View.GONE);
		binding.linear5.setVisibility(View.GONE);
		binding.linear6.setVisibility(View.GONE);
		binding.textview10.setVisibility(View.GONE);
		
		
		_initShizuku();
	}
	
	public String _get(final String _command) {
		StringBuilder output = new StringBuilder();
		
		if (via_shizuku) {
			// Shizuku
			ShizukuRemoteProcess process = null;
			String currentDir = "/";
			try {
				process = Shizuku.newProcess(new String[]{"sh", "-c", _command}, null, currentDir);
				try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
					
					String line;
					while ((line = inputReader.readLine()) != null) {
						output.append(line).append("\n");
					}
					
					while ((line = errorReader.readLine()) != null) {
						output.append("ERROR: ").append(line).append("\n");
					}
				}
				process.waitFor();
			} catch (Exception e) {
				output.append("Exception: ").append(e.getMessage()).append("\n");
			} finally {
				if (process != null) process.destroy();
			}
		} else {
			// Runtime.exec через su -c без экранирования
			try {
				java.lang.Process process = Runtime.getRuntime().exec("su -c " + _command);
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						output.append(line).append("\n");
					}
				}
				process.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
				return "Error: " + e.getMessage();
			}
		}
		
		return output.toString().trim();
	}
	
	
	public void _requestShizukuPermission() {
		try {
			if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
				
				via_shizuku = true;
				
			} else {
				
				Shizuku.requestPermission(123);
				
			}
		}
		catch (Exception e) {
			Log.e("error", "NO SHIZUKU FOUND.");
		}
		if (_isShizukuWorking()) {
			via_shizuku = true;
		}
		if (via_shizuku) {
			binding.edittext1.setText(_get("cmd battery get level"));
			binding.edittext2.setText(_get("cmd battery get counter"));
			binding.checkbox2.setChecked(_get("cmd battery get usb").contains("true"));
			binding.seekbar1.setProgress((int)Math.round(Double.parseDouble(_get("cmd battery get temp")) / 10));
			if (_get("dumpsys battery").contains("OPLUS")) {
				binding.textview9.setText(getString(R.string.oplu_battery));
			}
			binding.linear1.setVisibility(View.VISIBLE);
			binding.linear2.setVisibility(View.VISIBLE);
			binding.linear4.setVisibility(View.VISIBLE);
			binding.linear5.setVisibility(View.VISIBLE);
			binding.linear6.setVisibility(View.VISIBLE);
			binding.linear8.setVisibility(View.GONE);
			vibrator.vibrate((long)(50));
			if (_get("dumpsys battery").contains("UPDATES STOPPED")) {
				binding.textview11.setVisibility(View.VISIBLE);
			} else {
				binding.textview11.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	
	public void _initShizuku() {
		Shizuku.addBinderReceivedListener(() -> {
			Log.d("Shizuku", "Binder received!");
		});
		
		if (Shizuku.pingBinder()) {
			Log.d("Shizuku", "Shizuku is running");
		}
	}
	
	
	public boolean _isShizukuWorking() {
		try {
			if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e) {
			Log.e("error", "NO SHIZUKU FOUND.");
			return false;
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}