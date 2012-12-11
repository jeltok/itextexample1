package com.ots.jsp1;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadPdfFile extends HttpServlet {

    private int pdfType = 0;
    private String location;
    private File uploadFile;
    private static final Logger log1 = Logger.getLogger(UploadPdfFile.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FileNotFoundException {
        pdfType = 0;
        File outputFile = null;


        PdfDocumentManipulator pdfController = new PdfDocumentManipulator(getServletContext());


        // upload the file
        outputFile = uploadFile(request);

        log1.log(Level.INFO, "File uloaded to {0}", outputFile.getAbsolutePath());

        // if pdftype==0 we will insert ADA and then sign the document
        if (pdfType == 0) {
            try {
                // insert ADA and sign the file
                outputFile = pdfController.signAndAddADA(outputFile);
            } catch (KeyStoreException ex) {
                Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CertificateException ex) {
                Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GeneralSecurityException ex) {
                Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            log1.log(Level.INFO, "Signed file: {0}", outputFile.getAbsolutePath());
        } else { // if pdftype > 0 we will add placeholders
            // put signature placeholders
            outputFile = pdfController.addSignaturePlaceholder(uploadFile, pdfType);
        }

        // output pdf document back to client
        returnResponseFile(response, outputFile);
    }

    public File uploadFile(HttpServletRequest request) {
        try {

            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload fileUpload = new ServletFileUpload(factory);

            // Parse the request
            List items = fileUpload.parseRequest(request);

            // Process the uploaded items
            Iterator ir = items.iterator();
            while (ir.hasNext()) {
                FileItem item = (FileItem) ir.next();


                // Process a regular form field
                if (!item.isFormField()) { //Determines whether or not a FileItem instance represents a simple form field.
                    // load storage directory
                    location = PropertiesUtil.loadProperties(getServletContext()).getProperty(PropertiesUtil.Keys.FILE_DIRECTORY);
                    // create file
                    uploadFile = new File(location, item.getName());

                    log1.log(Level.INFO, "File will be uploaded to:{0}", uploadFile.getPath());
                    
                    // get file size
                    long size = item.getSize();

                    // Process a file upload
                    item.write(uploadFile);

                } else {
                    // get pdf document type in order to indicate how many placeholders
                    // will be create
                    if (item.getFieldName().equals("pdftypeform")) {
                        try {
                            pdfType = Integer.parseInt(item.getString());
                            log1.log(Level.INFO, "Field: {0} with value: {1}", new Object[]{item.getFieldName(), item.getString()});
                        } catch (NumberFormatException exc) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, e);
        }


        return uploadFile;
    }

    /**
     * Returns a pdf file wrapped in a http message. The pdf file is opened
     * inside the browser
     *
     * @param resp The servlet response
     * @param outputFile The file we want to output
     */
    public void returnResponseFile(HttpServletResponse resp, File outputFile) {
        OutputStream os = null;

        try {
            // set response headers
            resp.setHeader("Expires", "0");
            resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            resp.setHeader("Pragma", "public");
            resp.setContentType("application/pdf");

            // get response output stream
            os = resp.getOutputStream();
            
            // get pdf file in a byte array
            byte[] result = PdfDocumentManipulator.returnPdfInBytes(outputFile);
            
            // set Content-Length header
            resp.setContentLength(result.length);
            
            // write file to output
            for (int i = 0; i < result.length; i++) {
                os.write(result[i]);
            }
        } catch (IOException ex) {
            Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(UploadPdfFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
