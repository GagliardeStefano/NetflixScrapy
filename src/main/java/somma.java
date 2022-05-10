import java.io.*;

public class somma {
    public static void main(String[] args) throws IOException {

        File Episodi = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\DatiSerieEpisodi.csv");
        BufferedReader br = new BufferedReader(new FileReader(Episodi));

        String regex = (",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

        int readline = -1;
        String [] array;
        String row;
        while ((row = br.readLine()) != null){

            array = row.split(regex);

            readline++;

            System.out.println("READLINE##########àà "+readline);
            System.out.println("ARRAY@@@@@@@@@@@@ò"+array[0]);

            if (array[0].equals(readline)){
                //
            }else {
                System.out.println("Manca il numero: "+readline);
            }
        }
    }
}
