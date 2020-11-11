package com.epam.jwd.core_final.strategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface InputReadStrategy {

    List<String> readInfoFromFile(File fileToRead) throws FileNotFoundException;
}
