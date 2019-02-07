package de.ba.egov.batch.processing;

import java.io.Serializable;

import javax.batch.api.chunk.ItemReader;

/**
 *
 *
 * @author <a href="mailto:Mojammal.Hock@gmail.com">Mojammal Hock</a>
 */
public class SimpleFileReader implements ItemReader {

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemReader#open(java.io.Serializable)
	 */
	@Override
	public void open(Serializable checkpoint) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemReader#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemReader#readItem()
	 */
	@Override
	public Object readItem() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.batch.api.chunk.ItemReader#checkpointInfo()
	 */
	@Override
	public Serializable checkpointInfo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
