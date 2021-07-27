import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.List;

public class StudentCodeReviewAnalysis implements CodeReviewAnalyzer {
    private final List<String> classesCodeReviewAnswers = new ArrayList<>();
    private final Util util = new Util();
    private int codeReviewAnswers = 0;

    @Override
    public void getAnalysis(List<String> classes, Report report, AnActionEvent e) throws ClassNotFoundException {
        ClassService classService = new ClassService();

        report.addStringToReport("Student answers analysis for package\n");

        for (String className : classes) {
            PsiClass cls = classService.getClass(className, e);

            PsiAnnotation codeReviewAnnotation = null;
            if (cls != null) {
                PsiAnnotation[] classAnnotation = cls.getAnnotations();
                codeReviewAnnotation = util.findCodeReviewAnnotation(classAnnotation, "CodeReviewStudentAnswer");
            }

            if (codeReviewAnnotation != null) {
                PsiAnnotationMemberValue val = util.getAnnotationAttributeValue(codeReviewAnnotation);
                assert val != null;
                if (util.isCodeReviewTrue(val.getText())) {
                    classesCodeReviewAnswers.add(cls.getQualifiedName() + "." + className);
                    codeReviewAnswers++;
                }
            }
        }
        createReport(report);
    }

    private void createReport(Report report) {
        report.addStringToReport("Student make answers: " + codeReviewAnswers + "\n");
        report.addStudentAnswersCodeReview(classesCodeReviewAnswers);
    }
}