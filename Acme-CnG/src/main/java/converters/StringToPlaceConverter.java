package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.PlaceRepository;
import domain.Place;

@Component
@Transactional
public class StringToPlaceConverter implements Converter<String, Place>{

	@Autowired
	PlaceRepository placeRepository;

	@Override
	public Place convert(String text) {
		Place result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = placeRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
