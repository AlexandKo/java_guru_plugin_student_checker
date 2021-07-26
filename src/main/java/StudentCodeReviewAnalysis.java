import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentCodeReviewAnalysis implements CodeReviewAnalyzer {
    private final List<String> classesCodeReviewAnswers = new ArrayList<>();
    private int codeReviewAnswers = 0;

    @Override
    public void getAnalysis(List<String> classes, Report report, AnActionEvent e) throws ClassNotFoundException {

        report.addStringToReport("Student answers analysis for package\n");

        for (String className : classes) {
            PsiClass[] psiClass = PsiShortNamesCache.getInstance(Objects.requireNonNull(e.getProject()))
                    .getClassesByName(className, GlobalSearchScope.everythingScope(e.getProject()));

            PsiClass cls = chooseCorrectClass(psiClass);

            PsiAnnotation codeReviewAnnotation = null;
            if (cls != null) {
                PsiAnnotation[] classAnnotation = cls.getAnnotations();
                codeReviewAnnotation = findCodeReviewAnnotation(classAnnotation);
            }

            if (codeReviewAnnotation != null) {
                PsiAnnotationMemberValue val = codeReviewAnnotation.findAttributeValue("approved");
                assert val != null;
                if (isCodeReviewTrue(val.getText())) {
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

    @Nullable
    private PsiAnnotation findCodeReviewAnnotation(PsiAnnotation[] classAnnotation) {
        PsiAnnotation codeReviewAnnotation = null;
        for (PsiAnnotation annotation : classAnnotation) {
            boolean isCodeReviewAnnotation = annotation.getText().contains("CodeReviewStudentAnswer");
            if (isCodeReviewAnnotation) {
                codeReviewAnnotation = Objects.requireNonNull(annotation.getOwner()).findAnnotation(Objects.requireNonNull(annotation.getQualifiedName()));
                break;
            }
        }
        return codeReviewAnnotation;
    }

    @Nullable
    private PsiClass chooseCorrectClass(PsiClass[] psiClass) {
        PsiClass cls = null;
        for (PsiClass p : psiClass) {
            if (p.getClass().toString().contains("PsiClassImpl")) {
                cls = p;
                break;
            }
        }
        return cls;
    }

    private boolean isCodeReviewTrue(String annotationValue) {
        return annotationValue.equals("true");
    }
}