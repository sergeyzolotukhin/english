package ua.in.sz.english.dict2json.v2.model;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Definition {
	private int no;
	private String text;

	private String transcription;
	private String partOfSpeech;
	private List<String> meanings = Lists.newArrayList();
}
