package au.com.new1step.herbalist.common;

import java.util.ArrayList;
import java.util.List;

public enum ProcessingStatus {
	UNPROCESSED("unprocessed"),
	PROCESSED("processed", "processing completed"),
	UNRESOLVED("unresolved",  "processing later"),
	REJECT("reject", "not want processing");


	private String shortDescription;
	private String description;

	private ProcessingStatus(String description) {
		this.shortDescription = description;
		this.description = description;
	}

	private ProcessingStatus(String shortDescription, String description) {
		this.shortDescription = shortDescription;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public String getShortDescription() {
		return shortDescription;
	}
	
	public static List<String> getDescriptions() {
		List<String> descriptions = new ArrayList<String>();
		for (ProcessingStatus status : values()) {
			descriptions.add(status.getDescription());
		}
		return descriptions;
	}

}
