package de.hock.batch.processing;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static de.hock.batch.processing.JobProperties.PARTITION_FILENAME;

@Named("FilePartitionMapper")
public class FilePartitionMapper implements PartitionMapper {

    @Inject
    private JobProperties jobProperties;

    @Inject
    private Logger logger;


    @Override
    public PartitionPlan mapPartitions() throws Exception {
        PartitionPlan partitionPlan = new PartitionPlanImpl();

        List<File> files = getFileList();
        partitionPlan.setThreads(jobProperties.getThreadCount());
        partitionPlan.setPartitions(files.size());
        partitionPlan.setPartitionProperties(createPartitionProperties(files));

        logger.log(Level.INFO, "Partition plan: NumberOfPartitions={0}, NumberOfThreads={1}, NumberOfProperties={2}", new Object[]{partitionPlan.getPartitions(), partitionPlan.getThreads(), partitionPlan.getPartitionProperties().length});
        return partitionPlan;
    }

    private List<File> getFileList() throws IOException {

        Path path = Paths.get(jobProperties.getInputDirectory());
        return Files.list(path).map(Path::toFile).collect(Collectors.toList());
//        return Files.list(Paths.get(inputDirectory)).peek(System.out::println).map(Path::toFile).peek(System.out::println).filter(File::isFile)
//                .collect(Collectors.toList());

    }

    private Properties[] createPartitionProperties(List<File> files) {
        Properties[] partitionProperties = new Properties[files.size()];

        for(int indexPartition = 0; indexPartition < files.size(); indexPartition++) {
            Properties properties = new Properties();
            properties.setProperty(PARTITION_FILENAME,
                    files.get(indexPartition).getPath());
            partitionProperties[indexPartition] = properties;
        }

        return partitionProperties;
    }

}
