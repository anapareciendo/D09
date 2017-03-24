package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CommentableActor;


@Component
@Transactional
public class CommentableActorToStringConverter implements Converter<CommentableActor, String>{

	@Override
	public String convert(CommentableActor commentable) {
		String result;

		if (commentable == null)
			result = null;
		else
			result = String.valueOf(commentable.getId());

		return result;
	}
}
