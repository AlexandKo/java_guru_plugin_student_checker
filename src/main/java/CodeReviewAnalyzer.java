import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.List;

public interface CodeReviewAnalyzer {

    void getAnalysis(List<String> classesPath, Report report, AnActionEvent e) throws ClassNotFoundException;
}