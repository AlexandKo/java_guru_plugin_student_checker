import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ClassService {
    public PsiClass getClass(String className, AnActionEvent e) {
        GlobalSearchScope globalSearchScope = GlobalSearchScope.everythingScope(Objects.requireNonNull(e.getProject()));
        PsiClass[] psiClass = PsiShortNamesCache.getInstance(Objects.requireNonNull(e.getProject()))
                .getClassesByName(className, globalSearchScope);

        return chooseCorrectClass(psiClass, e);
    }

    @Nullable
    private PsiClass chooseCorrectClass(PsiClass[] psiClass, AnActionEvent e) {
        String filePath = Objects.requireNonNull(Objects.requireNonNull(e.getData(PlatformDataKeys.VIRTUAL_FILE))
                .getUrl()).replace("/", ".");

        PsiClass cls = null;
        for (PsiClass p : psiClass) {
            int packageIndex = Objects.requireNonNull(p.getQualifiedName()).indexOf(".");
            String searchPackage = p.getQualifiedName().substring(0, packageIndex);
            if (filePath.contains(searchPackage)) {
                cls = p;
                break;
            }
        }
        return cls;
    }
}