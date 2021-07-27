import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;

public class Util {
    public PsiAnnotationMemberValue getAnnotationAttributeValue(PsiAnnotation codeReviewAnnotation) {
        return codeReviewAnnotation.findAttributeValue("approved");
    }

    public boolean isCodeReviewTrue(String annotationValue) {
        return annotationValue.equals("true");
    }
}