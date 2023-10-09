package model;

import java.io.File;
import java.util.List;


public record FileState (String currentDirectory, List<File> directoryContents, boolean showHidden){
}
