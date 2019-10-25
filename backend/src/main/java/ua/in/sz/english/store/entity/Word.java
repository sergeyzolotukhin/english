package ua.in.sz.english.store.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Word {
	private String word;
	private String description;
}
