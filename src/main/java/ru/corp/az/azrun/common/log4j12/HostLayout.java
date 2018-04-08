package ru.corp.az.azrun.common.log4j12;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

import ru.corp.az.azrun.common.GlobalConfig;

public class HostLayout extends PatternLayout {


	@Override
	protected PatternParser createPatternParser(String pattern) {
		return new PatternParser(pattern) {

			@Override
			protected void finalizeConverter(char c) {
				PatternConverter pc = null;

				switch (c) {
				case 'h':
					pc = new PatternConverter() {
						@Override
						protected String convert(LoggingEvent event) {
							return GlobalConfig.HOST_HAME;
						}
					};
					break;
				}

				if (pc == null)
					super.finalizeConverter(c);
				else
					addConverter(pc);
			}
		};
	}

}
