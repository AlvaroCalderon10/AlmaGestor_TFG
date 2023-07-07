package com.example.almagestor.Facture;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.pdf417.PDF417Writer;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FacturePDF {
    private static int grade=00000;
    public void createPdf(Date date) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String namePDF="Factura_Nº:"+grade+".pdf";
        File myFile= new File(pdfPath,namePDF);
        OutputStream outputStream = new FileOutputStream(myFile);

        PdfWriter writer = new PdfWriter(myFile);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document=new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(0,0,0,0);


        Paragraph title=new Paragraph("Nombre tienda").setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER);
        Paragraph adress=new Paragraph("Avenida de prueba Nº89 S/N").setFontSize(10).setTextAlignment(TextAlignment.CENTER);
        Paragraph nif=new Paragraph("NIF:585698985668").setFontSize(10).setTextAlignment(TextAlignment.CENTER);
        Paragraph separator=new Paragraph("------------------------------------------------------").setFontSize(10).setTextAlignment(TextAlignment.CENTER);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        Paragraph factura=new Paragraph(format.format(calendar.getTime())+ "  Nº Factura simplicada: "+grade).setFontSize(10).setTextAlignment(TextAlignment.CENTER);
        float[] width={550f,100f,75f,100f};
        Table table= new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //Inicialize Table
        table.addCell(new Cell().add(new Paragraph("Articulos").setBold().setFontSize(12)));
        table.addCell(new Cell().add(new Paragraph("PVP").setBold().setFontSize(12)));
        table.addCell(new Cell().add(new Paragraph("UD").setBold().setFontSize(12)));
        table.addCell(new Cell().add(new Paragraph("Total").setBold().setFontSize(12)));
        //Products
        table.addCell(new Cell().add(new Paragraph("Producto Prueba nº5")));
        table.addCell(new Cell().add(new Paragraph("10.00")));
        table.addCell(new Cell().add(new Paragraph("2")));
        table.addCell(new Cell().add(new Paragraph("20")));
        //total
        Paragraph total=new Paragraph("TOTAL: "+20).setBold().setFontSize(15).setTextAlignment(TextAlignment.RIGHT).setMarginRight(20).setMarginTop(10);

        //Desglose de IVA

        float[] width2={100f,100f,100f,100f};
        Table table2= new Table(width2);
        table2.setHorizontalAlignment(HorizontalAlignment.CENTER);
        table2.setMargin(20);
        //inicializar cabeceras
        table2.addCell(new Cell().add(new Paragraph("IVA").setBold().setFontSize(10)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("BASE").setBold().setFontSize(10)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("T.IVA").setBold().setFontSize(10)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("Total").setBold().setFontSize(10)).setBorder(Border.NO_BORDER));
        //Values Iva
        table2.addCell(new Cell().add(new Paragraph("21%")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("15.94")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("4.24")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("20")).setBorder(Border.NO_BORDER));

        //BARCODE
        BarcodeQRCode qrCode = new BarcodeQRCode("1256325638");
        PdfFormXObject qrCodeObject=qrCode.createFormXObject(ColorConstants.BLACK,pdfDocument);
        Image qrCodeImage= new Image(qrCodeObject).setWidth(70).setHorizontalAlignment(HorizontalAlignment.CENTER);
        //EAN
        Paragraph ean_sufix=new Paragraph("1256325638").setBold().setFontSize(8).setTextAlignment(TextAlignment.CENTER);

        document.add(title);
        document.add(adress);
        document.add(nif);
        document.add(separator);
        document.add(factura);
        document.add(table);
        document.add(total);
        document.add(table2);
        document.add(qrCodeImage);
        document.add(ean_sufix);

        document.close();

        this.grade++;
    }

}
