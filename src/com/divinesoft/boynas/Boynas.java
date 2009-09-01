/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.divinesoft.boynas;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.divinesoft.boynas.exporters.Exporter;
import com.divinesoft.boynas.exporters.XMLExporter;
import com.divinesoft.boynas.importers.Importer;
import com.divinesoft.boynas.model.ConfigEntry;
import com.divinesoft.boynas.persistence.StoreUtil;

public class Boynas {
	Importer importer;
	Exporter exporter;
	
	@SuppressWarnings("unchecked")
	private void getAllConfigEntries(){
		List<ConfigEntry> configEntries = StoreUtil.getStore().find("find configentry");
		if(configEntries.size() > 0){
			System.out.println("-- Start List Config Entries --");
			for(ConfigEntry c : configEntries){
				System.out.println("Config Entry: " + c);
			}
			System.out.println("-- End List Config Entries --");
		}else{
			System.out.println("-- No Config Entries Found! --");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void clean(){
		List<ConfigEntry> configEntries = StoreUtil.getStore().find("find configentry");
		if(configEntries.size() > 0){
			System.out.println("-- Start Clean --");
			for(ConfigEntry c : configEntries){
				System.out.println("Removing: " + c);
				StoreUtil.getStore().remove(c);
			}
			System.out.println("-- End Clean--");
			getAllConfigEntries();
		}else{
			System.out.println("-- No Config Entries Found! --");
		}
	}
	
	private void importCSV(){
		//Get config entries
		List<ConfigEntry> entries = importer.getList();
		
		if(entries == null){
			System.out.println("An error occurred when trying to import from CSV");
			System.out.println("Please verify the following:");
			System.out.println("* The file to be imported exists.");
			System.out.println("* It's a CSV with no more, no less than 4 fields.");
			System.out.println("* For example: fieldA,fieldB,fieldC,fieldD");
			System.out.println("* The system can read the specified file.");
		}else{
			System.out.println("-- Start Import --");
			//Insert each config entry
			for(ConfigEntry c : entries){
				System.out.println("-- Imported: "+c+" --");
				StoreUtil.getStore().save(c);
			}
			System.out.println("-- End Import --");
			getAllConfigEntries();	
		}
	}
	
	@SuppressWarnings("unchecked")
	private void exportXML(){
		Exporter exporter = new XMLExporter();
		
		System.out.println("-- Start Export --");
		List<ConfigEntry> entries = StoreUtil.getStore().find("find configentry");
		
		if(entries.size() <= 0){
			System.out.println("-- No Config Entries Found! --");
			return;
		}
		
		try {
			exporter.writeDocument(entries);
		} catch (IOException e) {
			System.err.println("Error writing XML documents to disk!");
			e.printStackTrace();
		}
		System.out.println("-- End Export --");
 	}
	
	private void exportTemplate(){
		System.out.println("-- Start Export --");
		List<ConfigEntry> entries = StoreUtil.getStore().find("find configentry");
		
		if(entries.size() <= 0){
			System.out.println("-- No Config Entries Found! --");
			return;
		}
		
		try{
			exporter.writeDocument(entries);
		}catch(IOException ioe){
			System.err.println("Error writing XML documents to disk from template!");
			ioe.printStackTrace();
		}
		System.out.println("-- End Export --");
	}
	
	private void quickExport(){
		//Build the config entries
		List<ConfigEntry> entries = importer.getList();
		
		if(entries.size() <= 0){
			System.out.println("-- Couldn't find any valid entries in the input file! --");
			return;
		}
		
		System.out.println("-- Start Export --");
		
		try{
			exporter.writeDocument(entries);
		}catch(IOException ioe){
			System.err.println("Error writing XML documents to disk from template!");
			ioe.printStackTrace();
		}
		System.out.println("-- End Export --");
		
	}
	
	private void printVersion(){
		System.out.println("-- Start Print Version --");
		System.out.println("boynas 1.2");
		System.out.println("Author:\t\tCesar Olea");
		System.out.println("Company:\tDivinesoft");
		System.out.println("Web:\t\thttp://www.cesarolea.com/");
		System.out.println("This is Free Libre Open Source Software. GPLv3.");
	}
	
	public static void main(String args[]){
		//TODO: Move Options to a different method
		Options options = new Options();
		
		//Create options
		Option list = OptionBuilder
							.withDescription("List all config entries in database")
							.create("list");
		
		Option clean = OptionBuilder
							.withDescription("Remove all config entries in database")
							.create("clean");
		
		Option importCSV = OptionBuilder.withArgName("file")
							.hasArg()
							.withDescription("Import config entries from a CSV file")
							.create("importCSV");
		
		Option exportXML = OptionBuilder
							.withDescription("Export all config entries to XML CFG files")
							.create("exportXML");
		
		Option exportTemplate = OptionBuilder
							.withArgName("ext template,mac template")
							.hasArgs(2)
							.withValueSeparator(',')
							.withDescription("Export all config entries from a set of template files")
							.create("exportTemplate");
		
		Option version = OptionBuilder
							.withDescription("Print the version number")
							.create("version");
		
		Option quickGen = OptionBuilder
							.withArgName("import file,templates folder")
							.withDescription("Use a one-shot template based config generator")
							.hasArgs(2)
							.withValueSeparator(',')
							.create("quickExport");
		
		//Add options
		options.addOption(list);
		options.addOption(clean);
		options.addOption(importCSV);
		options.addOption(exportXML);
		options.addOption(exportTemplate);
		options.addOption(quickGen);
		options.addOption(version);
		
		CommandLineParser parser = new GnuParser();
		
		//The main Boynas object
		Boynas boynas;
		
		//Start dealing with application context
		XmlBeanFactory appContext = new XmlBeanFactory(
				new ClassPathResource("applicationContext.xml"));
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.hasOption("list")){
				boynas = (Boynas)appContext.getBean("boynasList");
				boynas.getAllConfigEntries();
			}else if(cmd.hasOption("clean")){
				boynas = (Boynas)appContext.getBean("boynasList");
				boynas.clean();
			}else if(cmd.hasOption("importCSV")){
				boynas = (Boynas)appContext.getBean("bynImportCSV", 
						new Object[]{cmd.getOptionValue("importCSV")});
				boynas.importCSV();
			}else if(cmd.hasOption("exportXML")){
				boynas = (Boynas)appContext.getBean("bynExportXML");
				boynas.exportXML();
			}else if(cmd.hasOption("version")){
				boynas = (Boynas)appContext.getBean("boynasList");
				boynas.printVersion();
			}else if(cmd.hasOption("exportTemplate")){
				String[] paths = cmd.getOptionValues("exportTemplate");
				
				if(paths.length < 2){
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("boynas", options);		
				}
				
				boynas = (Boynas)appContext.getBean("bynExportTemplate", paths);
				
				boynas.exportTemplate();
			}else if(cmd.hasOption("quickExport")){
				String[] paths = cmd.getOptionValues("quickExport");
				
				if(paths.length < 2){
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("boynas", options);
				}
				
				boynas = (Boynas)appContext.getBean("bynQuickExport", paths);
				
				boynas.quickExport();
			}
			
			else{
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("boynas", options);				
			}
			
		} catch (ParseException e) {
			System.err.println("Parsing failed. Reason: "+e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("boynas", options);
		}
	}
}