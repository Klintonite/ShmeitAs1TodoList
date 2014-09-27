package ca.ualberta.cs.shmeittodolist;

import java.io.Serializable;

public class ListItems implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ListItems serializable ID
	 */
	private boolean isChecked = false;
	private String todoText;
	
	ListItems(String todo) {
		this.todoText = todo;
	}
	
	public boolean getChecked(){
		return this.isChecked;
	}
	
	public void setChecked(boolean check){
		this.isChecked = check;
	}
	public String getText(){
		return this.todoText;
	}
	public String toString() {
		return this.todoText;
}

}
