package com.juzer.controller.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.juzer.controller.ControllerActivity;
import com.juzer.controller.R;
import com.juzer.controller.conn.Bot;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	public static final String EXTRA_IP = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mIP;
	private String mPassword;

	// UI references.
	private EditText mIPView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Log in");
		setContentView(R.layout.activity_login);

		// Set up the login form.
		mIP = getIntent().getStringExtra(EXTRA_IP);
		mIPView = (EditText) findViewById(R.id.ipaddress);
		mIPView.setText(mIP);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		Bot.setProgressHandler(new ProgressHandler());
	}

	@SuppressLint("HandlerLeak")
	class ProgressHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			String str = (String) msg.obj;
			mLoginStatusMessageView.setText(str);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private EditText mUserView, mSSHPortView, mTelnetPortView;
	private int sshPort, telnetPort;
	private String user;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.advopt:

			View content = getLayoutInflater().inflate(R.layout.dialog_login,
					null);

			mUserView = (EditText) content.findViewById(R.id.usr);
			mSSHPortView = (EditText) content.findViewById(R.id.sshport);
			mTelnetPortView = (EditText) content.findViewById(R.id.telnetport);
			

			if (sshPort != 0 && user != null) {
				mUserView.setText(String.valueOf(this.user));
				mSSHPortView.setText(String.valueOf(this.sshPort));
				mTelnetPortView.setText(String.valueOf(this.telnetPort));
				
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setView(content);
			builder.setTitle(R.string.dialog_options_title);
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					try {
						sshPort = Integer.parseInt(mSSHPortView.getText().toString());
					} catch (Exception e) {
						sshPort = 0;
					}
					try {
						telnetPort = Integer.parseInt(mTelnetPortView.getText().toString());
					} catch (Exception e) {
						telnetPort = 0;
					}
					
					user = mUserView.getText().toString();
				}

			});

			builder.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}

			});

			builder.create().show();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Attempts to sign in the account specified by the login form.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		// Reset errors.
		mIPView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mIP = mIPView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			cancel = true;
		}

		// Check for a valid IP address.
		if (TextUtils.isEmpty(mIP)) {
			mIPView.setError(getString(R.string.error_field_required));
			cancel = true;
		}

		if (!cancel) {
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Exception> {

		@Override
		protected Exception doInBackground(Void... params) {
			// try {
			// Thread.sleep(100);
			// } catch (InterruptedException e) {
			// return null;
			// }
			try {
				Bot.connect(mIP, mPassword, user, sshPort, telnetPort);
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				return e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(final Exception error) {
			mAuthTask = null;
			showProgress(false);

			if (error == null) {
				LoginActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Intent intent = new Intent(LoginActivity.this,
								ControllerActivity.class);
						try {
							LoginActivity.this.startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(LoginActivity.this,
									"Could not load controller at this time!",
									Toast.LENGTH_LONG).show();
						}
					}

				});
			} else {
				if (error.getMessage()
						.contains("java.net.UnknownHostException")) {
					mIPView.setError("Not a valid IP address!");
					mIPView.requestFocus();
				} else if (error.getMessage().contains(
						"java.net.ConnectException")) {
					mIPView.setError("Could not find host!");
					mIPView.requestFocus();
				} else {
					mPasswordView.setError("Password is incorrect!");
					mPasswordView.requestFocus();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(LoginActivity.this,
									error.getMessage(), Toast.LENGTH_LONG)
									.show();
						}

					});
				}
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
