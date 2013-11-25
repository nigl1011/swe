package de.shop.artikelverwaltung.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
	 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
*/

@Converter(autoApply = true)
public class KategorieTypeConverter implements AttributeConverter<KategorieType, String> {

		@Override
		public String convertToDatabaseColumn(KategorieType kategorieType) {
			if (kategorieType == null) {
				return null;
			}
			return kategorieType.getInternal();
		}

		@Override
		public KategorieType convertToEntityAttribute(String internal) {
			return KategorieType.build(internal);
		}
}
