package com.ots.jsp1.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author cmanios
 */
public class PlaceHolderCreator {

    public void createPlaceHolder(InputStream source, OutputStream destination, Rectangle rect, String placeholderName) throws IOException, DocumentException {
        final String SIGNAME = placeholderName;
        PdfReader reader = new PdfReader(source);

        PdfStamper stamper = new PdfStamper(reader, destination);

        // create a signature form field
        PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
        field.setFieldName(SIGNAME);

        // set the widget properties
        //field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_OUTLINE);
        field.setWidget(rect, PdfAnnotation.HIGHLIGHT_OUTLINE);
        field.setFlags(PdfAnnotation.FLAGS_PRINT);

        // add the annotation
        stamper.addAnnotation(field, 1);
        // close the stamper
        stamper.close();
    }

    public void createPlaceHolder(InputStream source, OutputStream destination, Rectangle rect[], String placeholderName) throws IOException, DocumentException {
        final String SIGNAME = placeholderName;
        PdfReader reader = new PdfReader(source);

        PdfStamper stamper = new PdfStamper(reader, destination);

        for (int i = 0; i < rect.length; i++) {
            // create a signature form field
            PdfFormField field = PdfFormField.createSignature(stamper.getWriter());
            field.setFieldName(SIGNAME+(i+1));

            // set the widget properties
            //field.setWidget(new Rectangle(72, 732, 144, 780), PdfAnnotation.HIGHLIGHT_OUTLINE);
            field.setWidget(rect[i], PdfAnnotation.HIGHLIGHT_OUTLINE);
            field.setFlags(PdfAnnotation.FLAGS_PRINT);

            // add the annotation
            stamper.addAnnotation(field, 1);
        }

        // close the stamper
        stamper.close();
    }
}
