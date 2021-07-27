import java.util.List;

public class Report {
    private String report="";

    public void addStringToReport(String newString) {
        report += newString;
    }

    public String showReport() {
        return report;
    }

    public void addFailedCodeReview(List<String> failedCodeReview) {
        failedCodeReview.forEach(failed -> report += failed + "\n");
    }

    public void addStudentAnswersCodeReview(List<String> classesCodeReviewAnswers) {
        classesCodeReviewAnswers.forEach(answers -> report += answers + "\n");
    }
}