package de.hock.batch.processing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Inject;

public class FilePartitionMapper implements PartitionMapper {

	@Inject
	@BatchProperty(name = "filenamePattern")
	private String filenamePattern;

	@Inject
	@BatchProperty(name = "inputDirectory")
	private String inputDirectory;

	@Inject
	@BatchProperty(name = "numberOfThreads")
	private Integer threadCount;

	private static final String PROPERTY_FILENAME = "fileName";

	@Override
	public PartitionPlan mapPartitions() throws Exception {
		return createPartitionPlan(getFileList());

	}

	private List<File> getFileList() throws IOException {

		return Files.list(Paths.get(inputDirectory)).map(Path::toFile).filter(File::isFile)
				.filter(file -> file.getName().matches(filenamePattern))
				.collect(Collectors.toList());

	}

	private PartitionPlan createPartitionPlan(List<File> files) {

		PartitionPlan partitionPlan = new PartitionPlanImpl();

		partitionPlan.setThreads(threadCount);

		int partitionen = files.size();
		partitionPlan.setPartitions(partitionen);

		Properties[] partitionProperties = new Properties[partitionen];
		partitionPlan.setPartitionProperties(partitionProperties);

		for (int indexPartition = 0; indexPartition < partitionen; indexPartition++) {
			Properties properties = new Properties();
			properties.setProperty(PROPERTY_FILENAME,
					files.get(indexPartition).getPath());
			partitionProperties[indexPartition] = properties;
		}

		return partitionPlan;
	}
}
