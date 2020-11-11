package com.epam.jwd.core_final.strategy.impl;

import com.epam.jwd.core_final.strategy.InputReadStrategy;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultiLineReadStrategy implements InputReadStrategy {

    private static final Logger logger = Logger.getLogger(MultiLineReadStrategy.class);
    public static final MultiLineReadStrategy INSTANCE = new MultiLineReadStrategy();

    private MultiLineReadStrategy() {
    }

    @Override
    public List<String> readInfoFromFile(File fileToRead) throws FileNotFoundException {
        List<String> infoFromFile = new ArrayList<>();
        Reader reader = null;

        try {
            reader = new FileReader(fileToRead);
            Scanner scanner = new Scanner(reader);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.charAt(0) != '#') {
                    infoFromFile.add(line);
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return infoFromFile;
    }
}
