package net.nbt.ckok.vaadin.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.vaadin.data.util.converter.StringToDateConverter;

public class CustomStringToDateConverter extends StringToDateConverter {

	@Override
	protected DateFormat getFormat(Locale locale) {
		return new SimpleDateFormat("dd.MM.yyyy");
	}

}
