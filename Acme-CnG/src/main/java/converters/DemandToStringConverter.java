package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Demand;


@Component
@Transactional
public class DemandToStringConverter implements Converter<Demand, String>{

	@Override
	public String convert(Demand demand) {
		String result;

		if (demand == null)
			result = null;
		else
			result = String.valueOf(demand.getId());

		return result;
	}
}
