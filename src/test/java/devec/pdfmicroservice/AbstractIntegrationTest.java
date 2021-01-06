package devec.pdfmicroservice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractIntegrationTest {

    protected boolean testConnection(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();

            return HttpURLConnection.HTTP_OK != urlConn.getResponseCode();
        } catch (IOException e) {
            System.err.println("Error creating HTTP connection");
            e.printStackTrace();
        }

        return false;
    }
}
