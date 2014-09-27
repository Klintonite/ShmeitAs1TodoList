/*
This application lets you create a TODO list that allows you to check,
uncheck, archive, email, and delete the TODOs.

Copyright (C) 2014 Klinton Shmeit

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

*/


package ca.ualberta.cs.shmeittodolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import ca.ualberta.cs.shmeitas1todolist.R;
import ca.ualberta.cs.shmeittodolist.data.FileDataManager;



public class TodoActivity extends Activity {

	private FileDataManager dataManager, dataManager2;

	private EditText bodyText;

	private ListView todoList;

	private ArrayList<ListItems> todos;
	
	private ArrayList<ListItems> archived;

	private CustomAdapter todoViewAdapter, archivedViewAdapter;
	
	private static final String FILENAME = "file.sav";
	
	private static final String FILENAME2 = "file2.sav";
	
	private Boolean todoOrArchived;

	//Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		dataManager = new FileDataManager(this, FILENAME);
		dataManager2 = new FileDataManager(this, FILENAME2);

		bodyText = (EditText) findViewById(R.id.body);
		todoList = (ListView) findViewById(R.id.oldTweetsList);
		
		
		//on long click brings up dialogue to archive todo
		todoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(TodoActivity.this);
				if(todoOrArchived){
				adb.setMessage("Archive TODO?");
				}
				else{
					adb.setMessage("UnArchive TODO?");
				}
				adb.setCancelable(true);
				final int finalPosition = position;
				
				adb.setPositiveButton("Yes", new OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if(todoOrArchived){
						ListItems temp = todos.get(finalPosition);
						todos.remove(finalPosition);
						archived.add(temp);
						dataManager2.saveTodos(archived);
						todoViewAdapter.notifyDataSetChanged();
						archivedViewAdapter.notifyDataSetChanged();
						dataManager.saveTodos(todos);
						//ToDoListController.getTodoList().removeTodo(todo);
						}
						else {
							ListItems temp = archived.get(finalPosition);
							archived.remove(finalPosition);
							dataManager2.saveTodos(archived);
							todos.add(temp);
							todoViewAdapter.notifyDataSetChanged();
							archivedViewAdapter.notifyDataSetChanged();
						}
					}
				});
				
				adb.setNegativeButton("No", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
					
				});
				
				adb.show();
			}
			
		});
		
		//on long click brings up dialogue to delete todo
		todoList.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				AlertDialog.Builder adb = new AlertDialog.Builder(TodoActivity.this);
				adb.setMessage("Delete TODO?");
				adb.setCancelable(true);
				final int finalPosition = position;
				
				adb.setPositiveButton("Yes", new OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if(todoOrArchived){
						todos.remove(finalPosition);
						todoViewAdapter.notifyDataSetChanged();
						dataManager.saveTodos(todos);
						}
						else{
							archived.remove(finalPosition);
							dataManager2.saveTodos(archived);
							archivedViewAdapter.notifyDataSetChanged();
						}

					}
					
				});
				
				adb.setNegativeButton("No", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
					
				});

				adb.show();
				return false;
			}
			
		});
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		todoOrArchived=true;
		todos = dataManager.loadTodos();
		archived=dataManager2.loadTodos();
		todoViewAdapter = new CustomAdapter(this, todos);
		archivedViewAdapter = new CustomAdapter(this,archived);
		todoList.setAdapter(todoViewAdapter);
		//ArrayList<ListItems> archived;

	}
	public void archive(View v) {
		Toast.makeText(this, "Archived ToDo List", Toast.LENGTH_SHORT).show();
		//sends from main activity to archived to do list.
		todoList.setAdapter(archivedViewAdapter);
		todoOrArchived=false;

	}
	
	public void todos(View v) {
		Toast.makeText(this, "TODO List", Toast.LENGTH_SHORT).show();
		//sends from archive list to todo list.
		todoList.setAdapter(todoViewAdapter);
		todoOrArchived=true;

	}

	public void save(View v) {
		String text = bodyText.getText().toString();
		ListItems tweet = new ListItems(text);
		todos.add(tweet);
		todoViewAdapter.notifyDataSetChanged();
		bodyText.setText("");
		dataManager.saveTodos(todos);
	}
	
	public void summary(View v) {
		int checksTodo = 0;
		int checksArchived = 0;
		int todoSize = todos.size();
		int archSize = archived.size();
		
		for(int i = 0; i < todos.size(); i ++){
			if (todos.get(i).getChecked()){
				checksTodo ++;
			}
		}
		for(int i = 0; i < archived.size(); i ++){
			if (archived.get(i).getChecked()){
				checksArchived ++;
			}
		}

		Toast.makeText(this, "Number of Todos = " + todoSize + "\nNumber of Archived Todos = " + archSize
				+ " \nNumber of Checked Todos = " + checksTodo + " \nNumber of checked Archived Todos = "
				+ checksArchived, Toast.LENGTH_LONG).show();
	}

	public void email(View v) {
		String emailedItems="";
		for (int i = 0; i < (todos.size()); i++)
			if (todos.get(i).getChecked()){
			emailedItems = emailedItems + todos.get(i).getText() + "\n";
			}
		for (int i = 0; i < (archived.size()); i++)
			if (archived.get(i).getChecked()){
			emailedItems = emailedItems + archived.get(i).getText() + "\n";
			}

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
		i.putExtra(Intent.EXTRA_TEXT   , emailedItems);
		try {
		    startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(TodoActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	public void onStop(){
		super.onStop();
		dataManager2.saveTodos(archived);
		dataManager.saveTodos(todos);
	}
	
}
	



