import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                codeReviewAnnotation = findCodeReviewAnnotation(classAnnotation);
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

    @Nullable
    private PsiAnnotation findCodeReviewAnnotation(PsiAnnotation[] classAnnotation) {
        PsiAnnotation codeReviewAnnotation = null;
        for (PsiAnnotation annotation : classAnnotation) {
            boolean isCodeReviewAnnotation = annotation.getText().contains("CodeReview");
            if (isCodeReviewAnnotation) {
                codeReviewAnnotation = Objects.requireNonNull(annotation.getOwner()).findAnnotation(Objects.requireNonNull(annotation.getQualifiedName()));
                break;
            }
        }
        return codeReviewAnnotation;
    }
}