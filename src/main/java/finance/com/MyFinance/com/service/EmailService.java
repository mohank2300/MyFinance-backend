package finance.com.MyFinance.com.service;

import finance.com.MyFinance.com.transaction.Transaction;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReport(String toEmail, String userName, List<Transaction> transactions, String period) throws Exception {

        long totalIncome = transactions.stream()
                .filter(t -> t.getType().name().equals("INCOME"))
                .mapToLong(Transaction::getAmountCents).sum();

        long totalExpense = transactions.stream()
                .filter(t -> t.getType().name().equals("EXPENSE"))
                .mapToLong(Transaction::getAmountCents).sum();

        long net = totalIncome - totalExpense;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        StringBuilder rows = new StringBuilder();
        for (Transaction t : transactions) {
            String color = t.getType().name().equals("INCOME") ? "#6fcf97" : "#eb5757";
            String sign = t.getType().name().equals("INCOME") ? "+" : "−";
            String desc = t.getDescription() != null ? t.getDescription() : "—";
            rows.append("<tr>")
                    .append("<td style=\"padding:10px 12px;border-bottom:1px solid #1e1e1e;color:#aaa;\">").append(t.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(fmt)).append("</td>")
                    .append("<td style=\"padding:10px 12px;border-bottom:1px solid #1e1e1e;color:#ccc;\">").append(t.getCategory()).append("</td>")
                    .append("<td style=\"padding:10px 12px;border-bottom:1px solid #1e1e1e;color:#666;\">").append(desc).append("</td>")
                    .append("<td style=\"padding:10px 12px;border-bottom:1px solid #1e1e1e;color:").append(color).append(";font-weight:700;\">").append(sign).append(String.format("$%.2f", t.getAmountCents() / 100.0)).append("</td>")
                    .append("</tr>");
        }

        String netSign = net >= 0 ? "+" : "−";
        String netColor = net >= 0 ? "#e8c97e" : "#eb5757";
        String today = java.time.LocalDate.now().format(fmt);

        String html = "<div style=\"background:#0a0a0a;padding:40px 0;font-family:'Segoe UI',sans-serif;\">"
                + "<div style=\"max-width:600px;margin:0 auto;background:#111;border-radius:16px;overflow:hidden;border:1px solid #1e1e1e;\">"

                + "<div style=\"padding:32px 40px;border-bottom:1px solid #1e1e1e;\">"
                + "<div style=\"font-size:1.4rem;font-weight:800;color:#e8c97e;\">MyFinance</div>"
                + "<div style=\"font-size:1rem;color:#555;margin-top:4px;\">Financial Summary Report</div>"
                + "</div>"

                + "<div style=\"padding:28px 40px;border-bottom:1px solid #1e1e1e;\">"
                + "<div style=\"color:#888;font-size:0.9rem;\">Hi <strong style=\"color:#f0ede6;\">" + userName + "</strong>,</div>"
                + "<div style=\"color:#555;font-size:0.82rem;margin-top:6px;\">Here is your financial summary for: <strong style=\"color:#e8c97e;\">" + period + "</strong></div>"
                + "</div>"

                + "<div style=\"padding:28px 40px;border-bottom:1px solid #1e1e1e;\">"
                + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">"
                + "<tr>"
                + "<td style=\"padding:16px;background:#0b2318;border-radius:10px;text-align:center;\">"
                + "<div style=\"font-size:0.7rem;color:#3a3a3a;letter-spacing:0.1em;text-transform:uppercase;margin-bottom:6px;\">Total Income</div>"
                + "<div style=\"font-size:1.4rem;font-weight:800;color:#6fcf97;\">+" + String.format("$%.2f", totalIncome / 100.0) + "</div>"
                + "</td>"
                + "<td width=\"12\"></td>"
                + "<td style=\"padding:16px;background:#230b0b;border-radius:10px;text-align:center;\">"
                + "<div style=\"font-size:0.7rem;color:#3a3a3a;letter-spacing:0.1em;text-transform:uppercase;margin-bottom:6px;\">Total Expenses</div>"
                + "<div style=\"font-size:1.4rem;font-weight:800;color:#eb5757;\">−" + String.format("$%.2f", totalExpense / 100.0) + "</div>"
                + "</td>"
                + "<td width=\"12\"></td>"
                + "<td style=\"padding:16px;background:#1a1608;border-radius:10px;text-align:center;\">"
                + "<div style=\"font-size:0.7rem;color:#3a3a3a;letter-spacing:0.1em;text-transform:uppercase;margin-bottom:6px;\">Net Balance</div>"
                + "<div style=\"font-size:1.4rem;font-weight:800;color:" + netColor + ";\">" + netSign + String.format("$%.2f", Math.abs(net) / 100.0) + "</div>"
                + "</td>"
                + "</tr>"
                + "</table>"
                + "</div>"

                + "<div style=\"padding:28px 40px;\">"
                + "<div style=\"font-size:0.72rem;font-weight:700;letter-spacing:0.12em;text-transform:uppercase;color:#3a3a3a;margin-bottom:16px;\">Transactions (" + transactions.size() + ")</div>"
                + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;\">"
                + "<thead><tr>"
                + "<th style=\"padding:8px 12px;text-align:left;font-size:0.68rem;color:#2a2a2a;letter-spacing:0.1em;text-transform:uppercase;border-bottom:1px solid #1e1e1e;\">Date</th>"
                + "<th style=\"padding:8px 12px;text-align:left;font-size:0.68rem;color:#2a2a2a;letter-spacing:0.1em;text-transform:uppercase;border-bottom:1px solid #1e1e1e;\">Category</th>"
                + "<th style=\"padding:8px 12px;text-align:left;font-size:0.68rem;color:#2a2a2a;letter-spacing:0.1em;text-transform:uppercase;border-bottom:1px solid #1e1e1e;\">Note</th>"
                + "<th style=\"padding:8px 12px;text-align:left;font-size:0.68rem;color:#2a2a2a;letter-spacing:0.1em;text-transform:uppercase;border-bottom:1px solid #1e1e1e;\">Amount</th>"
                + "</tr></thead>"
                + "<tbody>" + rows + "</tbody>"
                + "</table>"
                + "</div>"

                + "<div style=\"padding:20px 40px;border-top:1px solid #1e1e1e;text-align:center;color:#2a2a2a;font-size:0.75rem;\">"
                + "Generated by MyFinance • " + today
                + "</div>"

                + "</div></div>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject("MyFinance Report — " + period);
        helper.setText(html, true);
        mailSender.send(message);
    }


    public void sendEmail(String toEmail, String subject, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }

}