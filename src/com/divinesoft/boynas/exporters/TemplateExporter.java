package com.divinesoft.boynas.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.divinesoft.boynas.model.ConfigEntry;

public class TemplateExporter implements Exporter{
	private String templatePath, extTemplatePath, macTemplatePath;
	private VelocityEngine velocityEngine = new VelocityEngine();
	private Template extTemplate, macTemplate;
	
	public TemplateExporter(){
		setupVelocityEngine();
	}
	
	private void setupVelocityEngine(){
		try {
			velocityEngine.init();
			
			//Setup the templates
			extTemplate = velocityEngine.getTemplate(templatePath +
					File.separator + extTemplatePath);
			macTemplate = velocityEngine.getTemplate(templatePath +
					File.separator + macTemplatePath);
			
		} catch (Exception e) {
			System.err.println("Error initializing the Velocity engine!");
			System.err.println("Here's some useful information...");
			System.err.println("extTemplatePath\t"+extTemplatePath);
			System.err.println("macTemplatePath\t"+macTemplatePath);
			e.printStackTrace();
		}
	}
	
	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public Template getExtTemplate() {
		return extTemplate;
	}

	public void setExtTemplate(Template extTemplate) {
		this.extTemplate = extTemplate;
	}

	public Template getMacTemplate() {
		return macTemplate;
	}

	public void setMacTemplate(Template macTemplate) {
		this.macTemplate = macTemplate;
	}

	@Override
	public void writeDocument(List<ConfigEntry> entries) throws IOException{	
		
		VelocityContext context = new VelocityContext();

		for(ConfigEntry c : entries){
			//Put the context info
			context.put("macAddress", c.getMacAddress());
			context.put("extension", c.getExtension());
			context.put("password", c.getPassword());
			context.put("lines", c.getLines());
			
			//Render the template
			StringWriter extWriter = new StringWriter();
			StringWriter macWriter = new StringWriter();
			
			extTemplate.merge(context, extWriter);
			writeToDisk(extWriter, "ext-"+c.getExtension()+".cfg");
			macTemplate.merge(context, macWriter);
			writeToDisk(macWriter, c.getMacAddress()+".cfg");
			
		}
	}
	
	private void writeToDisk(StringWriter writer, String filename) throws IOException{
		File exportDir = new File("export");
		if(!exportDir.exists() || !exportDir.isDirectory()){
			exportDir.mkdir();
		}
		File currentDir = new File(".");
		
		BufferedWriter bw;
		File file = new File(currentDir.getCanonicalPath()+"/export/"+filename);
		bw = new BufferedWriter(new FileWriter(file));
		bw.write(writer.toString());
		bw.close();
	}
	
	public String getExtTemplatePath() {
		return extTemplatePath;
	}

	public void setExtTemplatePath(String extTemplatePath) {
		this.extTemplatePath = extTemplatePath;
	}

	public String getMacTemplatePath() {
		return macTemplatePath;
	}

	public void setMacTemplatePath(String macTemplatePath) {
		this.macTemplatePath = macTemplatePath;
	}
	
}