package com.juzer.controller.conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Handler;
import android.os.Message;

public class BotReader implements Runnable {

	private BufferedReader input;
	private Handler console;
	
	public BotReader(InputStream is, Handler console) {
		input = new BufferedReader(new InputStreamReader(is));
		this.console = console;
	}

	@Override
	public void run() {
		StringBuilder str = new StringBuilder();
		while (true) {
			
			try {
				char c = (char) input.read();
				str.append(c);
				if ((c == ':' || c == '\n') && str.length() > 1) {
					Message msg = new Message();
					msg.obj = str.toString();
					console.sendMessage(msg);
					str = new StringBuilder();
				}
			} catch (IOException e) {
				break;
			}

		}
		
	}

}
