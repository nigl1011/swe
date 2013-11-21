package de.shop.lieferverwaltung.domain;

import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;

public class TransportTypeConverter implements AttributeConverter<TransportType, String> {
	@Override
	public String convertToDatabaseColumn(TransportType transportType) {
		return transportType.getDbString();
	}
	
	@Override
	public TransportType convertToEntityAttribute(String dbString) {
		return TransportType.build(dbString);
	}

}
