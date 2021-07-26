import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindAllFilesInPackage {
    public List<String> findAllFiles(AnActionEvent e) throws ClassNotFoundException {
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        String packagePath = "";
        if (file != null) {
            packagePath = file.getCanonicalPath();
        }

        List<String> allFiles = new ArrayList<>();
        try {
            assert packagePath != null;
            allFiles = Files.walk(Paths.get(packagePath))
                    .filter(Files::isRegularFile).map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return allFiles.stream().map(this::prepareClassName).collect(Collectors.toList());
    }

    private String prepareClassName(String fullPath) {
        final String NO_JAVA_CLASS = "";
        int packageStartIndex = fullPath.lastIndexOf("\\");
        if (fullPath.contains(".java")) {
            int checkExtension = fullPath.indexOf(".java");
            return fullPath.substring(packageStartIndex + 1, checkExtension);
        }
        return NO_JAVA_CLASS;
    }
}