package de.hock.batch.processing;

import java.io.Serializable;
import java.util.List;

import javax.batch.api.chunk.ItemWriter;

/**
 *
 *
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class SimpleFileWriter implements ItemWriter {

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemWriter#open(java.io.Serializable)
	 */
	@Override
	public void open(Serializable checkpoint) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemWriter#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemWriter#writeItems(java.util.List)
	 */
	@Override
	public void writeItems(List<Object> items) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemWriter#checkpointInfo()
	 */
	@Override
	public Serializable checkpointInfo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
