import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Util {
    public PsiAnnotationMemberValue getAnnotationAttributeValue(PsiAnnotation codeReviewAnnotation) {
        return codeReviewAnnotation.findAttributeValue("approved");
    }

    @Nullable
    public PsiAnnotation findCodeReviewAnnotation(PsiAnnotation[] classAnnotation, String annotationName) {
        PsiAnnotation codeReviewAnnotation = null;
        for (PsiAnnotation annotation : classAnnotation) {
            boolean isCodeReviewAnnotation = annotation.getText().contains(annotationName);
            if (isCodeReviewAnnotation) {
                codeReviewAnnotation = Objects.requireNonNull(annotation.getOwner()).findAnnotation(Objects.requireNonNull(annotation.getQualifiedName()));
                break;
            }
        }
        return codeReviewAnnotation;
    }

    public boolean isCodeReviewTrue(String annotationValue) {
        return annotationValue.equals("true");
    }
}