package com.divinesoft.boynas.importers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.divinesoft.boynas.model.ConfigEntry;

public class CSVImporter implements Importer{
	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public CSVImporter(String filePath){
		this.filePath = filePath;
	}

	@Override
	public List<ConfigEntry> getList() {
		//CSV file is 4 fields per line: mac,extension,password,lines
		List<ConfigEntry> entries = new ArrayList<ConfigEntry>();
		//Read the file
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			//Iterate the file, line by line
			String line;
			while((line = br.readLine()) != null){
				String[] tokens = line.split(",");
				if(tokens.length == 4){
					ConfigEntry cE = new ConfigEntry();
					cE.setMacAddress(tokens[0]);
					cE.setExtension(tokens[1]);
					cE.setPassword(tokens[2]);
					cE.setLines(tokens[3]);
					entries.add(cE);
				}else{
					br.close();
					return null;
				}
			}
			br.close();
			return entries;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}
}
