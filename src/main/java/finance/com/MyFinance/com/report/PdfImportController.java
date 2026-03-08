package finance.com.MyFinance.com.report;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/import")
public class PdfImportController {

    record ParsedTransaction(String description, double amount, String type) {}

    @PostMapping("/pdf")
    public ResponseEntity<?> importPdf(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            PDDocument doc = Loader.loadPDF(bytes);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            doc.close();

            List<ParsedTransaction> transactions = parseChaseStatement(text);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to parse PDF: " + e.getMessage());
        }
    }

    private List<ParsedTransaction> parseChaseStatement(String text) {
        List<ParsedTransaction> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(
                "^(\\d{2}/\\d{2})\\s+(.+?)\\s+(-?\\d{1,3}(?:,\\d{3})*(?:\\.\\d{2})?)$",
                Pattern.MULTILINE
        );

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String description = matcher.group(2).trim();
            String amountStr   = matcher.group(3).replace(",", "");

            if (description.toLowerCase().matches(".*(balance|minimum payment|credit limit|opening|closing|payment due).*")) {
                continue;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                String type = amount < 0 ? "EXPENSE" : "INCOME";
                double absAmount = Math.abs(amount);
                if (absAmount < 0.01) continue;
                result.add(new ParsedTransaction(description, absAmount, type));
            } catch (NumberFormatException ignored) {}
        }

        return result;
    }
}