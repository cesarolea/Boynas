package com.divinesoft.boynas.importers;

import java.util.List;
import java.util.Map;

import com.divinesoft.boynas.model.ConfigEntry;

public interface Importer {
	public void setup(Map<String,String> params);
	public List<ConfigEntry> getList();
}
