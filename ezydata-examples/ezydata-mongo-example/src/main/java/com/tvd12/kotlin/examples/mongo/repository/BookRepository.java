package com.tvd12.kotlin.examples.mongo.repository;

import java.util.List;

import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.util.Next;
import com.tvd12.kotlin.examples.mongo.entity.Book;
import com.tvd12.kotlin.examples.mongo.result.SumBookPriceResult;

@EzyAutoImpl
public interface BookRepository extends EzyMongoRepository<Long, Book> {

    Book findByNameAndAuthorId(String name, Long authorId);

    @EzyQuery("{$orderby:{name:1}}")
    List<Book> findBooks(Next next);

    @EzyQuery("{$query:{name:{$lt:?0}}, $orderby:{name:1}}")
    List<Book> findByNameLt(String name, Next next);

    @EzyQuery("{$query:{name:{$gt:?0}}, $orderby:{name:1}}")
    List<Book> findByNameGt(String name, Next next);

    @EzyQuery("[{ $group: { _id : 'sum', sum : { $sum: {$toDecimal: '$price'}} } }]")
    SumBookPriceResult sumPrice();
}