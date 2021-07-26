import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckPackageAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final String BAD_PACKAGE_ERROR = "Unable to get resources from path.\n"
                + "Are you sure the package exists?\n";

        FindAllFilesInPackage findAllFilesInPackage = new FindAllFilesInPackage();
        List<String> allFiles = null;
        try {
            allFiles = findAllFilesInPackage.findAllFiles(e);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

        Report report = new Report();
        createTeacherreport(e, allFiles, report);
        createStudentReport(e, allFiles, report);

        assert allFiles != null;
        if (allFiles.size() > 0) {
            Messages.showInfoMessage(report.showReport(), "Code Review");
        } else {
            Messages.showInfoMessage(BAD_PACKAGE_ERROR, "Code Review");
        }
    }

    private void createTeacherreport(@NotNull AnActionEvent e, List<String> allFiles, Report report) {
        TeacherCodeReviewAnalysis teacherCodeReviewAnalysis = new TeacherCodeReviewAnalysis();

        try {
            assert allFiles != null;
            teacherCodeReviewAnalysis.getAnalysis(allFiles, report, e);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    private void createStudentReport(@NotNull AnActionEvent e, List<String> allFiles, Report report) {
        StudentCodeReviewAnalysis studentCodeReviewAnalysis = new StudentCodeReviewAnalysis();
        try {
            studentCodeReviewAnalysis.getAnalysis(allFiles, report, e);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
}