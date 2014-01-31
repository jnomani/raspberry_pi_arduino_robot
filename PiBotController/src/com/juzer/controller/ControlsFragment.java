package com.juzer.controller;

import java.io.IOException;
import com.juzer.controller.conn.Bot;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ControlsFragment.OnItemClickListener} interface to handle
 * interaction events.
 * 
 */
public class ControlsFragment extends Fragment implements OnClickListener, OnCheckedChangeListener {

	public static final String TITLE = "CONTROLS";
	
	private EditText console;
	private boolean upLock, downLock;
	
	private Button leftUp, leftDown, rightUp, rightDown;
	
	public ControlsFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		

		
		View v =  inflater.inflate(R.layout.fragment_controls, container, false);
		console = (EditText) v.findViewById(R.id.console);
		leftUp = (Button) v.findViewById(R.id.leftUp);
		leftDown = (Button) v.findViewById(R.id.leftDown);
		rightUp = (Button) v.findViewById(R.id.rightUp);
		rightDown = (Button) v.findViewById(R.id.rightDown);
		setOnTouchListeners();
		((RadioButton) v.findViewById(R.id.upLock)).setOnClickListener(this);
		((RadioButton) v.findViewById(R.id.downLock)).setOnClickListener(this);
		((Switch) v.findViewById(R.id.hlSwitch)).setOnCheckedChangeListener(this);
		Bot.start(new Console());
		return v;
	}

	public void onClick(View view){
		switch(view.getId()){
		case R.id.upLock :
			if(upLock){
				((RadioButton)view).setChecked(false);
				upLock = false;
			}else{
				((RadioButton)view).setChecked(true);
				upLock = true;
			}
			break;
		case R.id.downLock :
			if(downLock){
				((RadioButton)view).setChecked(false);
				downLock = false;
			}else{
				((RadioButton)view).setChecked(true);
				downLock = true;
			}
			break;
		
		}
	}
	
	private void setOnTouchListeners(){
		leftUp.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					leftUp.setPressed(true);
					if(upLock){
						rightUp.setPressed(true);
						console.append("Both up buttons pressed!\n");
						try{
							Bot.sendBotCommand("LR\r\n");
						}catch(IOException e){
							console.append(e.getMessage() + "\n");
						}
					}else{
						console.append("Left up button pressed!\n");
						try{
							Bot.sendBotCommand("L\r\n");
						}catch(IOException e){
							console.append(e.getMessage() + "\n");
						}
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					leftUp.setPressed(false);
					if(upLock){
						rightUp.setPressed(false);
						console.append("Both up buttons released!\n");
						try{
							Bot.sendBotCommand("lr\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Left up button released!\n");
						try{
							Bot.sendBotCommand("l\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}
				return false;
			}
			
		});
		
		leftDown.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					leftDown.setPressed(true);
					if(downLock){
						rightDown.setPressed(true);
						console.append("Both down buttons pressed!\n");
						try{
							Bot.sendBotCommand("DT\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Left down button pressed!\n");
						try{
							Bot.sendBotCommand("T\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					leftDown.setPressed(false);
					if(downLock){
						rightDown.setPressed(false);
						console.append("Both down buttons released!\n");
						try{
							Bot.sendBotCommand("dt\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Left down button released!\n");
						try{
							Bot.sendBotCommand("t\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}
				return false;
			}
			
		});
		
		rightUp.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					rightUp.setPressed(true);
					if(upLock){
						leftUp.setPressed(true);
						console.append("Both up buttons pressed!\n");
						try{
							Bot.sendBotCommand("LR\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Right up button pressed!\n");
						try{
							Bot.sendBotCommand("R\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					rightUp.setPressed(false);
					if(upLock){
						leftUp.setPressed(false);
						console.append("Both up buttons released!\n");
						try{
							Bot.sendBotCommand("lr\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Right up button released!\n");
						try{
							Bot.sendBotCommand("r\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}
				return false;
			}
			
		});
		
		rightDown.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					rightDown.setPressed(true);
					if(downLock){
						leftDown.setPressed(true);
						console.append("Both down buttons pressed!\n");
						try{
							Bot.sendBotCommand("DT\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Right down button pressed!\n");
						try{
							Bot.sendBotCommand("D\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					rightDown.setPressed(false);
					if(downLock){
						leftDown.setPressed(false);
						console.append("Both down buttons released!\n");
						try{
							Bot.sendBotCommand("dt\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}else{
						console.append("Right down button released!\n");
						try{
							Bot.sendBotCommand("d\r\n");
						}catch(IOException e){
							console.append(e.getMessage());
						}
					}
				}
				return false;
			}
			
		});
		
	}
	
	@SuppressLint("HandlerLeak")
	public class Console extends Handler{

		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;
			console.append(message + "\n");
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			try{
				Bot.sendBotCommand("H\r\n");
			}catch(IOException e){
				console.append(e.getMessage());
			}
		}else{
			try{
				Bot.sendBotCommand("h\r\n");
			}catch(IOException e){
				console.append(e.getMessage());
			}
		}
		
	}
}
