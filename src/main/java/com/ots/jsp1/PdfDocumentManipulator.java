package com.ots.jsp1;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;

import com.ots.jsp1.itext.ADAStamper;
import com.ots.jsp1.itext.DigitalSignatureCreatorWhenSelfSignedKey;
import com.ots.jsp1.itext.PlaceHolderCreator;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author cmanios
 */
public class PdfDocumentManipulator {

    private static final String KEY_AND_PASS = "loukas";
    private ServletContext context;

    public PdfDocumentManipulator() {
    }

    public PdfDocumentManipulator(ServletContext sc) {
        this.context = sc;
    }

    public File signDocument(File inputPdfFile) throws FileNotFoundException, IOException {
        // load storage directory
        String outputDirectory = PropertiesUtil.loadProperties(context).getProperty(PropertiesUtil.Keys.FILE_DIRECTORY);

        // create output file object
        File outputFile = new File(outputDirectory, inputPdfFile.getName().replace(".pdf", "") + "_signed.pdf");

        DigitalSignatureCreatorWhenSelfSignedKey cre1 = new DigitalSignatureCreatorWhenSelfSignedKey();
        String provider = cre1.initializeProvider();

        // TODO Handle private key initialisation
        InputStream keyInputStream = context.getResourceAsStream("/WEB-INF/loukas.keystore");
        FileInputStream fis = new FileInputStream(inputPdfFile);
        FileOutputStream fos = new FileOutputStream(outputFile);

        KeyStore ks;
        try {

            ks = cre1.initializeKeyStore(KEY_AND_PASS, keyInputStream);
            cre1.signDocument(fis, fos, new File(outputDirectory, "tmp.pdf"), ks, KEY_AND_PASS, null);

        } catch (DocumentException ex) {
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputFile;
    }

    public File signAndAddADA(File inputPdfFile) throws FileNotFoundException, IOException, KeyStoreException, DocumentException, NoSuchAlgorithmException, CertificateException, GeneralSecurityException {
        // load storage directory
        String outputDirectory = PropertiesUtil.loadProperties(context).getProperty(PropertiesUtil.Keys.FILE_DIRECTORY);

        // create output file object
        File outputFile = new File(outputDirectory, inputPdfFile.getName().replace(".pdf", "") + "_ada.pdf");
        File outputFile2 = new File(outputDirectory, inputPdfFile.getName().replace(".pdf", "") + "_ada_signed.pdf");

        InputStream source1 = new FileInputStream(inputPdfFile);
        OutputStream destination1 = new FileOutputStream(outputFile);




        ADAStamper stamper = new ADAStamper(context.getRealPath("/WEB-INF/" + ADAStamper.ARIAL_BOLD));
        stamper.addADAAsWatermark(source1, destination1, ADAStamper.generateADA());

        source1.close();
        destination1.close();

        InputStream source2 = new FileInputStream(outputFile);
        OutputStream destination2 = new FileOutputStream(outputFile2);

        DigitalSignatureCreatorWhenSelfSignedKey instance = new DigitalSignatureCreatorWhenSelfSignedKey();
        KeyStore ks = instance.initializeKeyStore("loukas", context.getResourceAsStream("/WEB-INF/loukas.keystore"));
        File tempFile = null;

        String keyPassword = "loukas";
        // String digestAlgorithm = DigestAlgorithms.SHA256;
        // String provider = instance.initializeProvider();
        instance.signDocument(source2, destination2, tempFile, ks, keyPassword, null);



        source2.close();
        destination2.close();

        return outputFile2;
    }

    /**
     * Adds A.D.A. code as annotation in a pdf document and returns a new one
     * with _ada appended in its name
     */
    public File addAda(File inputPdfFile, String adaCode) throws IOException {
        // load storage directory
        String outputDirectory = PropertiesUtil.loadProperties(context).getProperty(PropertiesUtil.Keys.FILE_DIRECTORY);

        String fontPath = context.getRealPath("/WEB-INF/" + ADAStamper.ARIAL_BOLD);

        File outputFile = null;
        try {
            // create output file object
            outputFile = new File(outputDirectory, inputPdfFile.getName().replace(".pdf", "") + "_ada.pdf");

            // create i/o streams
            FileInputStream fis = new FileInputStream(inputPdfFile);
            FileOutputStream fos = new FileOutputStream(outputFile);

            ADAStamper adaStamper = new ADAStamper(fontPath);
            //add ADA
            adaStamper.addADAAsWatermark(fis, fos, adaCode);

            fis.close();
            fos.close();
        } catch (DocumentException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputFile;
    }

    public File addSignaturePlaceholder(File inputPdfFile, int placeHolderCount) throws IOException {
        // load storage directory
        String outputDirectory = PropertiesUtil.loadProperties(context).getProperty(PropertiesUtil.Keys.FILE_DIRECTORY);

        File outputFile = null;

        try {
            // create output file object
            outputFile = new File(outputDirectory, inputPdfFile.getName().replace(".pdf", "") + "_placeholders.pdf");

            // create i/o streams
            FileInputStream fis = new FileInputStream(inputPdfFile);
            FileOutputStream fos = new FileOutputStream(outputFile);


            Rectangle rect[] = {new Rectangle(70, 710, 140, 770),
                new Rectangle(150, 710, 190, 770),
                new Rectangle(200, 710, 240, 770)};

            Rectangle rec1[] = java.util.Arrays.copyOfRange(rect, 0, placeHolderCount);


            new PlaceHolderCreator().createPlaceHolder(fis, fos, rec1, "xabos");

        } catch (DocumentException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DigitalSignatureCreatorWhenSelfSignedKey.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputFile;
    }

    /**
     * Reads a pdf file from the file system and returns it as a byte array
     *
     * @param pdfFile The file we want to read
     * @return the actual bytes of the file
     */
    public static byte[] returnPdfInBytes(File pdfFile) throws FileNotFoundException, IOException {
        FileInputStream fis = null;

        byte[] byteChunk = new byte[1024];

        ByteArrayOutputStream baos = null;

        fis = new FileInputStream(pdfFile);

        baos = new ByteArrayOutputStream();

        // read the file and write it to byte array
        while (fis.read(byteChunk) != -1) {
            baos.write(byteChunk);
        }

        // close stream
        fis.close();

        byteChunk = baos.toByteArray();
        baos.close();
        baos = null;

        return byteChunk;
    }
}
