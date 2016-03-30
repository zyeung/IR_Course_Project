package webStat;

import java.util.Iterator;
import java.util.Map.Entry;

public class HtmlDocIte implements Iterable<HtmlDoc> {
	private Iterator<Entry<String, String>> iterator;

	public HtmlDocIte(Iterator<Entry<String, String>> iterator) {
		this.iterator = iterator;
	}
	
	@Override
	public Iterator<HtmlDoc> iterator() {
		return new HtmlDocumentIterator(iterator);
	}
	
	public class HtmlDocumentIterator implements Iterator<HtmlDoc> {
		private Iterator<Entry<String, String>> iterator;

		public HtmlDocumentIterator(Iterator<Entry<String, String>> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public HtmlDoc next() {
			Entry<String, String> entry = iterator.next();
			return new HtmlDoc(entry.getKey(), entry.getValue());			
		}

		@Override
		public void remove() {
			iterator.remove();
		}
	}
}
