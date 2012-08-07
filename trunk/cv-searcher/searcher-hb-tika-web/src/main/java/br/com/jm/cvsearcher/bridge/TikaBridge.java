package br.com.jm.cvsearcher.bridge;

import java.util.Map;

import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.StringBridge;

public class TikaBridge implements StringBridge, ParameterizedBridge {
	private enum SUPPORTED_TYPES {
		DOC, PDF
	}

	private static final String TYPE_PROPERTY = "type";
	private String type;

	@Override
	public String objectToString(Object object) {
		
		switch (SUPPORTED_TYPES.valueOf(type)) {
		case DOC:
			System.out.println("Indexing DOC....");
			break;
		case PDF:
			System.out.println("Indexing PDF....");
			break;
		default:
			break;
		}
		return "bridge";
	}

	@Override
	public void setParameterValues(Map<String, String> parameters) {
		type = parameters.get(TYPE_PROPERTY);

	}

}
