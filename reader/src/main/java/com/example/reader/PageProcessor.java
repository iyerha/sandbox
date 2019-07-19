package com.example.reader;

import java.util.List;

/**
 * Enrich the supplied page data
 */
public interface PageProcessor<T> {
	void process(List<T> page);
}
