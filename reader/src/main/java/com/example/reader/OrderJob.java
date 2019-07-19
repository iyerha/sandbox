package com.example.reader;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
@EnableBatchProcessing
public class OrderJob {
	private static final int rows=5;
	
	@Autowired
	private JobBuilderFactory jobs;
	@Autowired
	private StepBuilderFactory steps;
	
	@Bean
	public CompositeJdbcPagingItemReader<Order> reader(DataSource dataSource) {
		CompositeJdbcPagingItemReader<Order> reader = new CompositeJdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(rows);
		reader.setPageSize(rows);
		reader.setQueryProvider(queryProvider(dataSource));
		reader.setRowMapper(new BeanPropertyRowMapper<>(Order.class));
		OrderPageProcessor processor = new OrderPageProcessor();
		processor.setDataSource(dataSource);
		reader.setPageProcessor(processor);
		return reader;
	}
	
	private PagingQueryProvider queryProvider(DataSource dataSource) {
		SqlPagingQueryProviderFactoryBean bean = new SqlPagingQueryProviderFactoryBean();
		bean.setDataSource(dataSource);
		bean.setSelectClause("select id, order_dt");
		bean.setFromClause("from orders");
		bean.setSortKey("order_dt");
		try {
			return bean.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Bean
	ItemWriter<Order> writer() {
		return new FlatFileItemWriterBuilder<Order>().
				name("OrderWriter").
				lineAggregator(new PassThroughLineAggregator<>()).
				resource(new FileSystemResource("target/test-outputs/output.txt")).build();
	}
	
	@Bean
	public Job job(@Qualifier("step1") Step step1) {
		return jobs.get("orderJob").start(step1).build();
	}
	
	@Bean
	protected Step step1(ItemReader<Order> reader,
	ItemWriter<Order> writer) {
		return steps.get("step1")
				.<Order, Order>chunk(rows).
				reader(reader).
				writer(writer).build();
	}
}
