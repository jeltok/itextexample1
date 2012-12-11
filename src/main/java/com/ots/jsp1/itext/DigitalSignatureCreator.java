package com.ots.jsp1.itext;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 *
 * @author Dimitra Konstantinidou <dkonstantinidou@ots.gr>
 */
public interface DigitalSignatureCreator {

    public String initializeProvider();

    public KeyStore initializeKeyStore(String password, InputStream inStream) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException;

    public void signDocument(InputStream source,
            OutputStream destination,
            File tempFile,
            KeyStore ks,
            String keyPassword,
            String provider) throws IOException, DocumentException, GeneralSecurityException;
}
