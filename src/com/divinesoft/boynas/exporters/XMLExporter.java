package com.divinesoft.boynas.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.divinesoft.boynas.model.ConfigEntry;

public class XMLExporter implements Exporter{


	@Override
	public void writeDocument(List<ConfigEntry> entries) throws IOException{
		//For each entry, write a set of ext and mac cfg files
		for(ConfigEntry c : entries){
			//ext-$extension.cfg
			Document ext = createExtDoc(c);
			writeToDisk(ext,"ext-"+c.getExtension()+".cfg");
			
			//$mac.cfg
			Document mac = createMacDoc(c);
			writeToDisk(mac,c.getMacAddress()+".cfg");
		}
	}
	
	private void writeToDisk(Document d, String filename)throws IOException{
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		
		File exportDir = new File("export");
		if(!exportDir.exists() || !exportDir.isDirectory()){
			exportDir.mkdir();
		}
		File currentDir = new File(".");
	
		BufferedWriter bw;
		File file = new File(currentDir.getCanonicalPath()+"/export/"+filename);
		bw = new BufferedWriter(new FileWriter(file));
		outputter.output(d, bw);
		bw.close();
	}
	
	private Document createMacDoc(ConfigEntry c){
		Element root = new Element("APPLICATION");
		Document macDoc = new Document(root);
		
		root.setAttribute(new Attribute("APP_FILE_PATH","sip.ld"));
		root.setAttribute(new Attribute("CONFIG_FILES","ext-"+c.getExtension()+".cfg, phone1.cfg, server.cfg, phone.cfg, sip.cfg"));
		root.setAttribute(new Attribute("MISC_FILES",""));
		root.setAttribute(new Attribute("LOG_FILE_DIRECTORY","/polycom/logs/"));
		root.setAttribute(new Attribute("OVERRIDES_DIRECTORY","/polycom/overrides/"));
		root.setAttribute(new Attribute("CONTACTS_DIRECTORY","/polycom/contacts/"));
		
		return macDoc;
	}
	
	private Document createExtDoc(ConfigEntry c){
		Element root = new Element("reginfo");
		Document extDoc = new Document(root);
		
		Element reg = new Element("reg");
		reg.setAttribute(new Attribute("reg.1.displayName",c.getExtension()));
		reg.setAttribute(new Attribute("reg.1.address",c.getExtension()));
		reg.setAttribute(new Attribute("reg.1.label",c.getExtension()));
		reg.setAttribute(new Attribute("reg.1.auth.userId",c.getExtension()));
		reg.setAttribute(new Attribute("reg.1.auth.password",c.getPassword()));
		reg.setAttribute(new Attribute("reg.1.lineKeys",c.getLines()));
		reg.setAttribute(new Attribute("reg.1.callsPerLineKey","1"));
		reg.setAttribute(new Attribute("reg.1.server.1.expires.lineSeize","30"));
		reg.setAttribute(new Attribute("reg.1.server.1.port","5060"));
		reg.setAttribute(new Attribute("reg.1.server.1.register","1"));
		reg.setAttribute(new Attribute("reg.1.server.1.transport","DNSnaptr"));
		reg.setAttribute(new Attribute("reg.1.server.2.transport","DNSnaptr"));
		reg.setAttribute(new Attribute("reg.1.type","private"));
		root.addContent(reg);
		
		Element msg = new Element("msg");
		msg.setAttribute(new Attribute("msg.bypassInstantMessage","1"));
		root.addContent(msg);
		
		Element mwi = new Element("mwi");
		mwi.setAttribute(new Attribute("msg.mwi.1.subscribe",""));
		mwi.setAttribute(new Attribute("msg.mwi.1.callBackMode","contact"));
		mwi.setAttribute(new Attribute("msg.mwi.1.callBack","*97"));
		mwi.setAttribute(new Attribute("msg.mwi.2.callBackMode","disabled"));
		mwi.setAttribute(new Attribute("msg.mwi.3.callBackMode","disabled"));
		mwi.setAttribute(new Attribute("msg.mwi.4.callBackMode","disabled"));
		mwi.setAttribute(new Attribute("msg.mwi.5.callBackMode","disabled"));
		mwi.setAttribute(new Attribute("msg.mwi.6.callBackMode","disabled"));
		msg.addContent(mwi);
		
		return extDoc;
	}
}