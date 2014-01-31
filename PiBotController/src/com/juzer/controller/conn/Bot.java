package com.juzer.controller.conn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.telnet.*;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Bot {

	public static final int SSH = 22;
	public static final int TelNet = 7777;

	public static final String USER = "pi";

	private static final String CON_COM = "python Connect.py -p /dev/ttyACM0 -b 9600\r\n";
	private static final String SHUTDOWN = "sudo shutdown -h now\r\n";

	private static Handler progress;

	private static Channel shell;
	private static TelnetClient tClient;
	private static Session session;

	private static InputStream sshIS, telnetIS;
	private static OutputStream sshOS, telnetOS;
	private static DataOutputStream sshOut, telnetOut;

	public static void setProgressHandler(Handler progress) {
		Bot.progress = progress;
	}

	public static void connect(String host, String passwd, String user,
			int SSH, int telNet) throws JSchException, IOException,
			InterruptedException {

		// Decide whether to use default addresses or use user specified ones.
		if (user == null || user.equals("")) {
			user = USER;
		}
		if (SSH == 0) {
			SSH = Bot.SSH;
		}
		if (telNet == 0) {
			telNet = TelNet;
		}

		if (progress == null)
			throw new RuntimeException("Progress handler not set!");

		Message status;

		status = new Message();
		status.obj = "Connecting to SSH Server...";
		progress.sendMessage(status);

		JSch ssh = new JSch();
		session = ssh.getSession(user, host, SSH);
		java.util.Properties config = new java.util.Properties(); // Temporary
																	// workaround
																	// of
																	// missing
																	// RSA key.
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(passwd.getBytes());
		session.connect();

		status = new Message();
		status.obj = "Openning Shell Channel...";
		progress.sendMessage(status);

		shell = session.openChannel("shell");
		shell.connect();

		sshIS = shell.getInputStream();
		sshOS = shell.getOutputStream();

		sshOut = new DataOutputStream(sshOS);

		status = new Message();
		status.obj = "Running Python Script...";
		progress.sendMessage(status);

		// THe following makes sure the server is ready for input before we send
		// anything. There is probably a simpler way to do this i.e isReady().
		StringBuilder str = new StringBuilder();
		while (true) {
			try {
				char c = (char) sshIS.read();
				str.append(c);
				if ((c == ':' || c == '\n') && str.length() > 1) {
					String s = str.toString();
					if (s.contains(user + "@raspberry"))
						break;
					str = new StringBuilder();
				}
			} catch (IOException e) {
				throw e;
			}
		}

		sshOut.writeBytes(CON_COM); // Send the command to the Pi to get serial
									// interface started over telnet.
		sshOut.flush();

		Thread.sleep(1000); // Allow some time for the script to run. It is
							// likely we don't need this but just it protects
							// from prematurely connecting to telnet on a slow
							// connection.

		status = new Message();
		status.obj = "Connecting to TelNet...";
		progress.sendMessage(status);

		tClient = new TelnetClient();
		tClient.connect(host, telNet);

		telnetIS = tClient.getInputStream();
		telnetOS = tClient.getOutputStream();

		telnetOut = new DataOutputStream(telnetOS);
	}

	public static void start(Handler console) {
		// Probably not a good idea to output both SSH and Telnet to same View.
		// As long as telnet does not actually print anything we are fine.
		BotReader SSHReader = new BotReader(sshIS, console);
		(new Thread(SSHReader)).start();
		BotReader TelNetReader = new BotReader(telnetIS, console);
		(new Thread(TelNetReader)).start();
	}

	// Use this to send commands over Telnet.
	public static void sendBotCommand(String command) throws IOException {
		telnetOut.writeBytes(command);
		telnetOut.flush();
	}

	// Use this to send commands over SSH.
	public static void sendSysCommand(String command) throws IOException {
		sshOut.writeBytes(command);
		sshOut.flush();
	}

	public static void stop() {
		
		Log.d("Bot-Stop", "Shutdown process");
		
		try {
			sendSysCommand("BREAK\r\n"); // Breaks the Python script; Can't proceed
										// until script stops listening over SSH
										// and Telnet.
			sendSysCommand(SHUTDOWN); // Shutdown command
			
			
			
			sshIS.close();
			telnetIS.close();
			sshOS.close();
			telnetOS.close();

			tClient.disconnect();
			shell.disconnect();
			session.disconnect();

			sshIS = null;
			telnetIS = null;
			sshOS = null;
			telnetOS = null;

			tClient = null;
			shell = null;
			session = null;

		} catch (Exception e) {
			Log.e("Bot-Stop", e.getMessage());
		}

	}
}
