/*
 * @(#)ADAStamper.java  1.00  23/11/2012
 * 
 * Copyright 2010 OTS SA All rights reserved.
 * OTS SA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ots.jsp1.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author Dimitra Konstantinidou <dkonstantinidou@ots.gr>
 */
public class ADAStamper {

    public static final String ARIAL_BOLD = "arialbd.ttf";
    private String fontFilePath;

    public ADAStamper() {
    }

    public ADAStamper(String fontFilePath) {
        this.fontFilePath = fontFilePath;
    }

    public void addADAAsWatermark(InputStream inStream, OutputStream outputStream, String ADA) throws DocumentException, IOException {
        PdfStamper stamper = null;
        PdfReader reader = null;
        try {

            reader = new PdfReader(inStream);
            //The zero byte means we don’t want to change the version number of the PDF file.
            //true->not to change any of the original bytes
            stamper = new PdfStamper(reader, outputStream, '\0', true);
            int numberOfPages = reader.getNumberOfPages();


            for (int currentPage = 1; currentPage <= numberOfPages; currentPage++) {

                PdfAppearance canvas = PdfAppearance.createAppearance(stamper.getWriter(), 100, 30);
                canvas.setFontAndSize(BaseFont.createFont(fontFilePath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, true), 11);
                Rectangle pageSize = reader.getPageSizeWithRotation(currentPage);
                Rectangle watermarkPosition = new Rectangle(pageSize.getRight() - 150, pageSize.getTop() - 30, pageSize.getRight() - 50, pageSize.getTop() - 10, 0);
                PdfAnnotation annotation = PdfAnnotation.createFreeText(stamper.getWriter(), watermarkPosition, ADA, canvas);
                annotation.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_READONLY));



                //  annotation.put(PdfName.FONT, canvas);
                //  PdfAnnotation annotation = PdfAnnotation.createText(stamper.getWriter(), watermarkPosition, "ΑΔΑ", ADA, true, "Key");
                // 

                PdfBorderDictionary borderDictionary = new PdfBorderDictionary(0, PdfBorderDictionary.STYLE_SOLID);

                annotation.setBorderStyle(borderDictionary);
                stamper.addAnnotation(annotation, currentPage);
            }
        } finally {
            stamper.close();
            reader.close();
        }


    }

    /**
     * Generates a pseudo ADA code
     */
    public static String generateADA() {
        String adas[] = {"Α", "Β", "Γ", "Δ", "Ε"};

        return "ΑΔΑ: " + adas[new Random().nextInt(4)] + UUID.randomUUID().toString().substring(0, 7);
    }
}
