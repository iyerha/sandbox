package com.example.reader;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.util.Assert;

/**
 * Allows additional processing on the read page via the page processor.
 * Unlike an item processor, this gives access to the page of items. This allows
 * for additional use-cases such as making a single query to get additional data related
 * to all items in the page.
 * @param <T>
 */
public class CompositeJdbcPagingItemReader<T> extends JdbcPagingItemReader<T> {
	private PageProcessor<T> pageProcessor;

	public void setPageProcessor(PageProcessor<T> pageProcessor) {
		this.pageProcessor = pageProcessor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(pageProcessor, "Page processor may not be null");
	}

	@Override
	protected void doReadPage() {
		super.doReadPage();
		if (!results.isEmpty()) {
			pageProcessor.process(results);
		}
	}
}
