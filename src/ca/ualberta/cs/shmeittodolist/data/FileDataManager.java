package ca.ualberta.cs.shmeittodolist.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cs.shmeittodolist.ListItems;
import android.content.Context;
import android.util.Log;

public class FileDataManager {
	
	private static final String FILENAME = "file.sav";
	String fileName;
	
	private Context ctx;
	
	public FileDataManager(Context ctx, String file) {
		this.ctx = ctx;
		this.fileName = file;
	}
	
	public ArrayList<ListItems> loadTodos() {
		ArrayList<ListItems> lts = new ArrayList<ListItems>();

		try {
			FileInputStream fis = ctx.openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			lts = (ArrayList<ListItems>) ois.readObject();

		} catch (Exception e) {
			Log.i("shmeit Todolist", "Error casting");
			e.printStackTrace();
		} 

		return lts;
	}
	
	public void saveTodos(List<ListItems> lts) {
		try {
			FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(lts);
			fos.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
