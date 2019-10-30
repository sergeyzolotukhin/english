package ua.in.sz.english.dict2json.v2;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Word {
	private final String text;

	private String word;
	private List<Definition> definitions = Lists.newArrayList();
}
