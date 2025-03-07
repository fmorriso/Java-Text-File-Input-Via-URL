import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Main {

    /** Convert a shared, view-only, public link to a text file stored on Google Drive to one that can be read
     one line at a time through a combination of BufferedReader and InputStreamReader.
     * @param link - the original shared, view-only, public link to a text file stored on Google Drive.
     * @return a URL that will allow a Java BufferedReader to read a text file one line at a time.
     */
    private static String convertGoogleDrivePublicLinkToUrl(String link) {

        final String startingToken = "file/d/";
        final int startingTokenLength = startingToken.length();

        final String endingToken = "/view?usp";
        final int endingTokenLength = endingToken.length();

        // find start of ID
        int idxStart = link.indexOf(startingToken) + startingTokenLength;

        // find end of ID
        int idxEnd = link.indexOf(endingToken);

        // "https://drive.google.com/uc?export=download&id=1Ul-vu8XCKEUkCIrnAGLNOvOvpCN6ypna";
        return String.format("https://drive.google.com/uc?export=download&id=%s", link.substring(idxStart, idxEnd) );
    }


    /** Convert a shared, view-only, public link to a text file stored on DropBox to one that can be read
     * @param link - raw link to a public, shared, view-only text file stored on DropBox
     * @return A link that will allow the text file to be read.
     * @implNote IMPORTANT NOTE: When reading a text file from DropBox, you MUST change the end of the generated
     * public link from &dl=0 to &dl=1; otherwise, you get HTML instead of text.
     * WORKS: final String fileUrl = "https://www.dropbox.com/s/t8vh8a1rgq41d86/StateData.csv?st=p2b1ypzw&dl=1";
     */
    private static String convertDropBoxPublicLinkToUrl(String link) {
        return link;
    }

    private static int tryParseInt(String possibleInt) {
        int result;
        try {
            result = Integer.parseInt(possibleInt);
        } catch (NumberFormatException e) {
            result = 0;
        }
        return result;
    }

    private static OneItem extractItemFromText(String line) {
        String[] tokens = line.split(","); // Split line by commas
        String location = tokens[0];
        int year = tryParseInt(tokens[1]);
        String month = tokens[2];
        String period = tokens[3];
        String indicator = tokens[4];
        int dataValue = tryParseInt(tokens[5]);
        return new OneItem(location, year, month, period, indicator, dataValue);
    }


    public static void main(String[] args) {
        // FAILS: final String fileUrl = "https://1drv.ms/x/s!Ash3pFpgn-Cnyr18zLwmbT6q_S0Psg?e=mk0aeT";
        // FAILS: final String fileUrl = "https://onedrive.live.com/download?id=A7E09F605AA477C8!1220348&resid=A7E09F605AA477C8!1220348&ithint=file%2cxlsx&authkey=!AMy8Jm0-qv0tD7I&wdo=2&cid=a7e09f605aa477c8";
        // FAILS: final String fileUrl = "https://onedrive.live.com/download.aspx?download=1&resid=A7E09F605AA477C8!1220348&ithint=file%2cxlsx&authkey=!AMy8Jm0-qv0tD7I&wdo=2&cid=a7e09f605aa477c8";
        // FAILS: final String fileUrl = "https://1drv.ms/u/s!Ash3pFpgn-Cnyr4Hz9sZ-se-SK3K6g?e=bo9400";
        // FAILS: final String fileUrl = "https://onedrive.live.com/view.aspx?resid=A7E09F605AA477C8!1220359&authkey=!AM_bGfrHvkityuo";
        // FAILS: final String fileUrl = "https://onedrive.live.com/download.aspx?resid=A7E09F605AA477C8!1220359&authkey=!AM_bGfrHvkityuo";
        // FAILS: final String fileUrl = "https://onedrive.live.com/download.aspx?download=1&resid=A7E09F605AA477C8!1220359&authkey=!AM_bGfrHvkityuo";
        // FAILS: final String fileUrl = "https://onedrive.live.com?download=1&resid=A7E09F605AA477C8!1220359&authkey=!AM_bGfrHvkityuo";
        // FAILS: final String fileUrl = "https://onedrive.live.com/download&resid=A7E09F605AA477C8!1220359";
        // FAILS: final String fileUrl = "https://onedrive.live.com/download&resid=A7E09F605AA477C8!1220359&authkey=!AM_bGfrHvkityuo";
        // FAILS: final String fileUrl = "https://1drv.ms/x/s!Ash3pFpgn-Cnyr18zLwmbT6q_S0Psg&download=1";
        // https://1drv.ms/u/s!Ash3pFpgn-Cnyr4Hz9sZ-se-SK3K6g?e=gfqjSp
        // FAILS: final String fileUrl = "https://1drv.ms/u/s!Ash3pFpgn-Cnyr4Hz9sZ-se-SK3K6g?download=1";

        // OneDrive does not work - have to use DropBox which does work with only a small modification to the URL
        // IMPORTANT NOTE: When reading a text file from DropBox, you MUST change the end of the generated
        // public link from &dl=0 to &dl=1; otherwise, you get HTML instead of text.
        // raw url: https://www.dropbox.com/home/aaTemp
        // raw link: https://www.dropbox.com/s/t8vh8a1rgq41d86/StateData.csv?st=oyu4eadr&dl=0
        // WORKS: final String fileUrl = "https://www.dropbox.com/s/t8vh8a1rgq41d86/StateData.csv?st=p2b1ypzw&dl=1";

        String link = "https://drive.google.com/file/d/1Ul-vu8XCKEUkCIrnAGLNOvOvpCN6ypna/view?usp=sharing";
        String filename = "args[0]";
        String fileUrl = convertGoogleDrivePublicLinkToUrl(link);
        System.out.format("Google Drive url=%s%n", fileUrl);

        // NOTICE how the Google Drive shared public view-only link needs to be modified below.
        // Without this change, you get HTML instead of the actual text:
        //final String fileUrl = "https://drive.google.com/uc?export=download&id=1Ul-vu8XCKEUkCIrnAGLNOvOvpCN6ypna";
        //assert test.equals(fileUrl);
        try {
            URI fileUri = new URI(fileUrl);
            URL url = fileUri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                OneItem oneItem = extractItemFromText(line);
                System.out.println(oneItem);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
