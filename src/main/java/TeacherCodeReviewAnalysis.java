import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.List;

public class TeacherCodeReviewAnalysis implements CodeReviewAnalyzer {
    private final List<String> failedCodeReview = new ArrayList<>();
    private final Util util = new Util();
    private int classesCount = 0;
    private int codeReviewPassed = 0;
    private int codeReviewFailed = 0;

    @Override
    public void getAnalysis(List<String> classes, Report report, AnActionEvent e) throws ClassNotFoundException {
        ClassService classService = new ClassService();

        report.addStringToReport("Code Review report:\n");

        for (String className : classes) {
            PsiClass cls = classService.getClass(className, e);

            PsiAnnotation codeReviewAnnotation = null;
            if (cls != null) {
                PsiAnnotation[] classAnnotation = cls.getAnnotations();
                codeReviewAnnotation = util.findCodeReviewAnnotation(classAnnotation, "CodeReview");
            }

            if (codeReviewAnnotation != null) {
                PsiAnnotationMemberValue val = util.getAnnotationAttributeValue(codeReviewAnnotation);
                assert val != null;
                if (util.isCodeReviewTrue(val.getText())) {
                    codeReviewPassed++;
                } else {
                    failedCodeReview.add(cls.getQualifiedName() + "." + className);
                    codeReviewFailed++;
                }
                classesCount++;
            }
        }
        createReport(report);
    }

    private void createReport(Report report) {
        report.addStringToReport("Total classes with Annotation: " + classesCount + "\n");
        report.addStringToReport("Code Review Passed: " + codeReviewPassed + "\n");
        report.addStringToReport("Code Review Failed: " + codeReviewFailed + "\n");
        report.addFailedCodeReview(failedCodeReview);
    }
}