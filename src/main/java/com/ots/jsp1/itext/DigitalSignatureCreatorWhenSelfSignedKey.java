package com.ots.jsp1.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Dimitra Konstantinidou <dkonstantinidou@ots.gr>
 */
public class DigitalSignatureCreatorWhenSelfSignedKey implements DigitalSignatureCreator {

    public DigitalSignatureCreatorWhenSelfSignedKey() {
    }

    public String initializeProvider() {
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        return provider.getName();

    }

    public KeyStore initializeKeyStore(String password, InputStream inStream) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(inStream, password.toCharArray());

        return ks;
    }

    //support for CAdES is very new. Don’t expect versions older than Acrobat/Reader X to be able to validate 
    //CAdES signatures! Acrobat 9 only supports signatures as described in the specification for PDF 1.7, 
    //and CAdES is new in PDF 2.0.
    //The tmp variable in this code sample can be a path to a specific file or to a directory. 
    //In case a directory is chosen, iText will create a file with a unique name in that directory
    public void signDocument(InputStream source,
            OutputStream destination,
            File tempFile,
            KeyStore ks,
            String keyPassword,
            //   String digestAlgorithm,
            String provider)
            throws IOException, DocumentException, GeneralSecurityException {

        PdfReader reader = new PdfReader(source);
        PdfStamper stamper = PdfStamper.createSignature(reader, destination, '\0', tempFile, true);
        String alias = ks.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);

        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.NAME_AND_DESCRIPTION);


        //  appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING_AND_ANNOTATIONS);

        Rectangle pageSize = reader.getPageSize(1);
        Rectangle signatureLocation = new Rectangle(pageSize.getRight() - 110, //lower-left coordinate
                pageSize.getHeight() - 78, //upper-right coordinate
                pageSize.getRight() - 70,
                pageSize.getHeight() - 35);
        //1->pageNumber
        appearance.setVisibleSignature(signatureLocation, 1, "signature");

        // set signature reason and location
        appearance.setReason("No reason! Just testing iText capabilities!");
        appearance.setLocation("Thessaloniki, Greece");
        // appearance.setCrypto(privateKey, chain, null, PdfSignatureAppearance.SELF_SIGNED);

        //  stamper.close();

        ExternalDigest digest = new BouncyCastleDigest();
        ExternalSignature signature = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA512, provider);
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);


    }
}
