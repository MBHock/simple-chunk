package de.hock.batch.processing;

import javax.annotation.PostConstruct;
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
import java.util.stream.Collectors;

@Named("FilePartitionMapper")
public class FilePartitionMapper implements PartitionMapper {

    private String filenamePattern;
    private String inputDirectory;
    private Integer numberOfPartitions;
    private static final String PROPERTY_FILENAME = "fileName";

    @Inject
    private JobAndStepPropertyReader jobAndStepPropertyReader;

    @PostConstruct
    void initPorperties() {
        filenamePattern = jobAndStepPropertyReader.getProperty("filenamePattern");
        inputDirectory = jobAndStepPropertyReader.getProperty("inputDirectory");
        numberOfPartitions = jobAndStepPropertyReader.getPropertyAsInteger("numberOfThreads");
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        PartitionPlan partitionPlan = new PartitionPlanImpl();

        List<File> files = getFileList();
        partitionPlan.setThreads(numberOfPartitions);
        partitionPlan.setPartitions(files.size());
        partitionPlan.setPartitionProperties(createPartitionProperties(files));

        return partitionPlan;
    }

    private List<File> getFileList() throws IOException {

        Path path = Paths.get(inputDirectory);
        return Files.list(path).peek(System.out::println).map(Path::toFile).collect(Collectors.toList());
//        return Files.list(Paths.get(inputDirectory)).map(Path::toFile).filter(File::isFile)
//                .collect(Collectors.toList());

    }

    private Properties[] createPartitionProperties(List<File> files) {
        Properties[] partitionProperties = new Properties[files.size()];

        for(int indexPartition = 0; indexPartition < files.size(); indexPartition++) {
            Properties properties = new Properties();
            properties.setProperty(PROPERTY_FILENAME,
                    files.get(indexPartition).getPath());
            partitionProperties[indexPartition] = properties;
        }

        return partitionProperties;
    }

}
