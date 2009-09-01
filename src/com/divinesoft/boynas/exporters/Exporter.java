package com.divinesoft.boynas.exporters;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.divinesoft.boynas.model.ConfigEntry;

public interface Exporter {
	public void writeDocument(List<ConfigEntry> entries) throws IOException;
}
