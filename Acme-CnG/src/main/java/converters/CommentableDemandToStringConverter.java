package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CommentableDemand;


@Component
@Transactional
public class CommentableDemandToStringConverter implements Converter<CommentableDemand, String>{

	@Override
	public String convert(CommentableDemand commentable) {
		String result;

		if (commentable == null)
			result = null;
		else
			result = String.valueOf(commentable.getId());

		return result;
	}
}
