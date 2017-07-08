package com.ogbongefriends.com.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ogbongefriends.com.R;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;





/**
 * @author Arvind
 */
public class Validation{

	private boolean bringFoccus=false;
	private boolean showAlerts=false;
	private boolean setError=false;

	private Context c=null;

	//
	public Validation(Context c){

		this.c=c;
	}

	//

	public void ShowAlerts(boolean show){

		this.showAlerts=show;
	}

	//

	public void setFoccusToElements(boolean foccus){

		this.bringFoccus=foccus;
	}

	//

	public void setErrorToElements(boolean error){

		this.setError=error;
	}

	//
	/**<p>Returns <b>true</b> if editText is empty, else returns <b>false</b></p>*/
	//
	@SuppressWarnings("unused")
	public boolean checkIfEmpty(final EditText edit,final String elementName){

		try{
			AlertDialog g;
			String str=edit.getText().toString();
			if(str.equals("")){

				if(this.showAlerts){

					if(c!=null)
						displayDialog(c.getString(R.string.please_enter, elementName));
				}

				if(this.bringFoccus)
					edit.requestFocus();
				if(this.setError)
					edit.setError(c.getString(R.string.please_enter, elementName));

				return true;
			}

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkIfEmpty",e.toString());
		}
		return false;
	}

	/** Validated if contains only a-z, A-Z, ., _, 0-9, @, space, */
	//
	public boolean noSpecialCharacters(final EditText edit,final String elementName){

		try{

			String str=edit.getText().toString();
			char[] arr_str=str.toCharArray();

			if(str.length()>0){


				for(int i=0;i<arr_str.length;i++){

					if((arr_str[i]<='z' && arr_str[i]>='a') || (arr_str[i]<='Z' && arr_str[i]>='A') || arr_str[i]=='.'
							|| arr_str[i]=='_' || (arr_str[i]<='9' && arr_str[i]>='0') || arr_str[i]=='@' || arr_str[i]==' ' )
					{}
					else{

						if(this.showAlerts){

							if(c!=null)
								displayDialog(c.getString(R.string.invalid, elementName));
						}

						if(this.bringFoccus)
							edit.requestFocus();
						if(this.setError)
							edit.setError(c.getString(R.string.invalid, elementName));

						return false;
					}
				}

				return true;
			}
			else{

				if(this.showAlerts){

					if(c!=null)
						displayDialog(c.getString(R.string.invalid, elementName));
				}

				if(this.bringFoccus)
					edit.requestFocus();
				if(this.setError)
					edit.setError(c.getString(R.string.invalid, elementName));

				return false;
			}

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkForSpecialCharacters",e.toString());
		}

		return false;
	}

	//
	/** In default validated if contains only a-z, A-Z, ., _, 0-9, @, space.<br>
	 * For other characters you can use  allowedChar, notAllowed */
	public boolean noSpecialCharacters(final EditText edit,final String elementName, final String allowedChar, final String notAllowed){

		try{

			String str=edit.getText().toString();
			char[] arr_str=str.toCharArray();

			if(str.length()>0){


				for(int i=0;i<arr_str.length;i++){

					if((((arr_str[i]<='z' && arr_str[i]>='a') || (arr_str[i]<='Z' && arr_str[i]>='A') || (arr_str[i]<='9' && arr_str[i]>='0')) || allowedChar.contains(arr_str[i]+"")) && !notAllowed.contains(arr_str[i]+""))
					{}
					else{

						if(this.showAlerts){

							if(c!=null)
								displayDialog(c.getString(R.string.invalid, elementName));
						}

						if(this.bringFoccus)
							edit.requestFocus();
						if(this.setError)
							edit.setError(c.getString(R.string.invalid, elementName));

						return false;
					}
				}

				return true;
			}
			else{

				if(this.showAlerts){

					if(c!=null)
						displayDialog(c.getString(R.string.invalid, elementName));
				}

				if(this.bringFoccus)
					edit.requestFocus();
				if(this.setError)
					edit.setError(c.getString(R.string.invalid, elementName));

				return false;
			}

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkForSpecialCharacters",e.toString());
		}

		return false;
	}

	//
	/** Return true if space found */
	public boolean checkForSpaces(final EditText edit,final String elementName){

		try{

			String str=edit.getText().toString();
			char[] arr_str=str.toCharArray();

			if(str.length()>0){

				for(int i=0;i<arr_str.length;i++){

					if(arr_str[i]!=' ' ){

					}
					else{

						//==== space found
								if(this.showAlerts){

									if(c!=null)
										displayDialog(c.getString(R.string.invalid, elementName));
								}

								if(this.bringFoccus)
									edit.requestFocus();
								if(this.setError)
									edit.setError(c.getString(R.string.invalid, elementName));

								return true;
					}
				}

				return false;
			}
			else
				return false;

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkForSpecialCharacters",e.toString());
		}

		return false;
	}

	//
	/** Return true if min length satisfied */
	public boolean checkMinLength(final EditText edit,int minlength,final String elementName){

		try{

			String str=edit.getText().toString();
			if(str.length()<minlength){

				if(c!=null){

					if(this.showAlerts)
						displayDialog(c.getString(R.string.atlease_digits_required, elementName, minlength));

					if(this.bringFoccus)
						edit.requestFocus();
					if(this.setError)
						edit.setError(c.getString(R.string.invalid, elementName));
				}
				else{

					//Log.i("checked minimum length","context not provided");
				}

				return false;
			}

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkMinLength",e.toString());
		}
		return true;
	}

	//
	/** Return true if equal. */
	public boolean checkIfEqual(final EditText edit1,final String elementName1,final EditText edit2,
			final String elementName2,final EditText foccuson){

		if(!edit1.getText().toString().equals(edit2.getText().toString())){

			if(c!=null){

				displayDialog(c.getString(R.string.both_element_should_be_same, elementName1 ,elementName2));

				if(this.bringFoccus)
					foccuson.requestFocus();
				if(this.setError)
					foccuson.setError(c.getString(R.string.both_element_should_be_same, elementName1, elementName2));
			}
			else{

				//Log.i("checked for equal","context not provided");
			}
			return false;
		}
		return true;
	}

	//
	/** True if it has integer else return false */
	public boolean checkIfInteger(final EditText edit,final String elementName){

		try{

			String str=edit.getText().toString();
			char[] arr_str=str.toCharArray();

			if(str.length()>0){

				for(int i=0;i<arr_str.length;i++){

					if(arr_str[i]<='9' && arr_str[i]>='0'){

					}
					else{

						//==== space found
								if(this.showAlerts){

									if(c!=null)
										displayDialog(c.getString(R.string.only_integer_required, elementName));
								}

								if(this.bringFoccus)
									edit.requestFocus();
								if(this.setError)
									edit.setError(c.getString(R.string.only_integer_required, elementName));

								return false;
					}
				}

				return true;
			}
			else{

				if(this.showAlerts){

					if(c!=null)
						displayDialog(c.getString(R.string.invalid, elementName));
				}

				if(this.bringFoccus)
					edit.requestFocus();
				if(this.setError)
					edit.setError(c.getString(R.string.invalid, elementName));

				return false;
			}

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkForSpecialCharacters",e.toString());
		}

		return true;
	}

	//
	/** True if it has integer else return false */
	@SuppressWarnings("unused")
	public boolean checkIfFloat(final EditText edit,final String elementName){

		try{

			boolean floatVar = true;

			try{

				Float.parseFloat(elementName);

			}catch(Exception e){

				floatVar = false;
			}
			finally{


			}

			String str=edit.getText().toString();
			char[] arr_str=str.toCharArray();

			if(str.length()>0){

				for(int i=0;i<arr_str.length;i++){

					if(arr_str[i]<='9' && arr_str[i]>='0'){

					}
					else{

						//==== space found
								if(this.showAlerts){

									if(c!=null)
										displayDialog(c.getString(R.string.only_integer_required, elementName));
								}

						if(this.bringFoccus)
							edit.requestFocus();
						if(this.setError)
							edit.setError(c.getString(R.string.only_integer_required, elementName));

						return false;
					}
				}

				return true;
			}
			else{

				if(this.showAlerts){

					if(c!=null)
						displayDialog(c.getString(R.string.invalid, elementName));
				}

				if(this.bringFoccus)
					edit.requestFocus();
				if(this.setError)
					edit.setError(c.getString(R.string.invalid, elementName));

				return false;
			}

		}catch(Exception e){

			//Log.i("Exception ValidateField-basic_tasks.java funct-checkForSpecialCharacters",e.toString());
		}

		return true;
	}

	//
	/** Return true if email valid. */
	public boolean validEmail(EditText edit, String elementName){

		try{

			String input = edit.getText().toString();

			boolean valid = true;

			//Checks for email addresses starting with
			//inappropriate symbols like dots or @ signs.
			Pattern p = Pattern.compile("^\\.|^\\@");
			Matcher m = p.matcher(input);
			if (m.find())
				valid = false;
			//System.err.println("Email addresses don't start" + " with dots or @ signs.");

			//Checks for email addresses that start with
			//www. and prints a message if it does.
			p = Pattern.compile("^www\\.");
			m = p.matcher(input);
			if (m.find()) {

				valid = false;
				//System.out.println("Email addresses don't start" + " with \"www.\", only web pages do.");
			}

			p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
			m = p.matcher(input);
			StringBuffer sb = new StringBuffer();
			boolean result = m.find();
			boolean deletedIllegalChars = false;

			while(result) {
				deletedIllegalChars = true;
				m.appendReplacement(sb, "");
				result = m.find();
			}

			// Add the last segment of input to the new String
			m.appendTail(sb);

			input = sb.toString();

			//p=Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*");
			p=Pattern.compile("^[a-zA-Z][a-zA-Z0-9._%+-]*@[a-zA-Z0-9.]+\\.[a-zA-Z0-9]+");
			m=p.matcher(input);
			valid = m.matches();

			if (deletedIllegalChars || !valid) {

				//Log.i("= ", "It contained incorrect characters" + " , such as spaces or commas.");
				if(this.showAlerts){

					if(c!=null)
						displayDialog(c.getString(R.string.invalid, elementName));
				}

				if(this.bringFoccus)
					edit.requestFocus();
				if(this.setError)
					edit.setError(c.getString(R.string.invalid, elementName));

				return false;
			}

		}catch(Exception e){e.printStackTrace();}

		return true;
	}

	//
	public void displayDialog(String message){

		AlertDialog err = new AlertDialog.Builder(c)
		//.setIcon(R.drawable.error)
		.setTitle(R.string.error)
		.setMessage(message)   
		.create();

		err.setCanceledOnTouchOutside(true);
		err.show();
	}
}
