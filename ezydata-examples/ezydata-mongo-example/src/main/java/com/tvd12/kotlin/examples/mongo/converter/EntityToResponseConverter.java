package com.tvd12.kotlin.examples.mongo.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tvd12.ezydata.example.common.DateConverter;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.kotlin.examples.mongo.entity.Author;
import com.tvd12.kotlin.examples.mongo.entity.Book;
import com.tvd12.kotlin.examples.mongo.entity.Category;
import com.tvd12.kotlin.examples.mongo.response.BookResponse;

@EzySingleton
public class EntityToResponseConverter {
public BookResponse toBookResponse(
	Book book,
	Author author,
	Category category
) {
    return new BookResponse(
        book.getId(),
        category,
        author,
        book.getName(),
        book.getPrice(),
        DateConverter.toDate(book.getReleaseTime())
    );
}

	public List<BookResponse> toBooksResponse(
		List<Book> books,
		Map<Long, Author> authors,
		Map<Long, Category> categories
	) {
		return books.stream().map(it ->
	        toBookResponse(
	        	it,
	            authors.get(it.getAuthorId()),
	            categories.get(it.getCategoryId())
	        )
		).collect(Collectors.toList());
    }
}
