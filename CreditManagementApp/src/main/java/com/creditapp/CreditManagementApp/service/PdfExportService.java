package com.creditapp.CreditManagementApp.service;

import com.creditapp.CreditManagementApp.entity.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfExportService {

    public static byte[] generateTransactionsPdf(List<Transaction> transactions) throws DocumentException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Transaction Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add Table
        PdfPTable table = new PdfPTable(5); // 5 columns
        table.setWidthPercentage(100);

        // Table Headers
        Stream.of("Date", "Item", "Amount", "Status", "Customer")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setBackgroundColor(new BaseColor(63, 81, 181)); // Material indigo
                    cell.setPhrase(new Phrase(header));
                    table.addCell(cell);
                });

        // Table Data
        transactions.forEach(txn -> {
            table.addCell(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(txn.getDate()));
            table.addCell(txn.getItemName());
            table.addCell("₹" + String.format("%.2f", txn.getAmount()));
            table.addCell(String.valueOf(txn.getStatus()));
            table.addCell(txn.getCustomer().getName());
        });

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }
}
