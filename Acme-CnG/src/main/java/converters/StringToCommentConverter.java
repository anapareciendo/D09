package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Administrator;
import domain.Comment;

@Component
@Transactional
public class StringToCommentConverter implements Converter<String, Comment>{

	@Autowired
	CommentRepository commentRepository;

	@Override
	public Comment convert(String text) {
		Administrator result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = commentRepository.findOne(id);
		} catch (Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
