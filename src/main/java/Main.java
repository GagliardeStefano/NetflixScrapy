import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static  String path = ".serieTV/dark/imgStagioni";
    private static String destinazione = "C:\\xampp\\htdocs\\CTVS\\serieTv\\dark\\imgStagioni";
    private static int serieTV = 2;

    public static void main(String[] args) throws IOException {

        Document document = Jsoup.connect("https://www.netflix.com/it/title/80100172").get(); //url del sito da rubare :)

        Elements season = document.select("div.season"); //prendo l'elemento div con classe season

        File file = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\dark\\SeasonDark.csv");         //Creo file per le stagioni
        File fileEpisodi = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\dark\\DatiDark.csv");    //Creo file per gli episodi
        FileWriter writer = new FileWriter(file);   //oggetto per scrivere nel file delle stagioni
        FileWriter writerEpisodi = new FileWriter(fileEpisodi); //oggetto per scrivere nel file degli episodi

        writer.write("IdSerie"+",");        //
        writer.write("NumStagione"+",");    //  Creo colonne
        writer.write("NumEpisodi"+",");     //  per file stagioni
        writer.write("AnnoStagione"+"\n");  //


        writerEpisodi.write("Titolo"+",");      //
        writerEpisodi.write("Durata"+",");      //
        writerEpisodi.write("Descrizione"+","); // Creo colonne per file episodi
        writerEpisodi.write("Img"+",");         //
        writerEpisodi.write("idStagione"+"\n"); //

        for (int i=0; i<season.size(); i++){    //ciclo for per le stagioni

            //STAGIONI
            writer.write(serieTV+",");  //scrivo nel file l'id della Serie TV
            writer.write(1+i+",");  //scrivo nel file il numero della stagione

            Elements episodi = season.get(i).select("div.episodes-container");  //seleziono l'elemento eiesimo di div con classe episodes-container
            Elements nEpisodi = episodi.select("div.episode");  //seleziono l'elemento div con classe episode
            writer.write(nEpisodi.size()+",");  //scrivo nel file il numero di episodi per ogni stagione

            //seleziono l'elemento eiesimo di div con classe season-release-year
            Element anni = season.get(i).select("div.season-release-year").first();
            writer.write(anni.text()+"\n"); //scrivo nel file l'anno in cui è uscita la stagione

            for (int j=0; j<nEpisodi.size(); j++) { //ciclo for per gli episodi
                //EPISODI

                //seleziono l'elemento eiesimo di div con classe episode-metadata
                Elements metadata = nEpisodi.get(j).select("div.episode-metadata");
                Element titolo = metadata.select("h3").first(); //seleziono il tag <h3>
                writerEpisodi.write(titolo.text() + ",");   //scrivo nel file il testo (titolo) contenente in <h3>

                Element durata = metadata.select("span").first();   //seleziono il tag <span>
                writerEpisodi.write(durata.text() + ",");   //scrivo nel file il testo (durata episodio) contenente in <span>

                Element descrizione = nEpisodi.get(j).select("p.epsiode-synopsis").first(); //seleziono il tag <p> con classe epsiode-synopsis
                writerEpisodi.write(descrizione.text() + ",");  //scrivo nel file il testo (riassunto episodio) contenente in <p>

                Elements locandine = nEpisodi.get(j).select("figure.episode-thumbnail");//seleziono il tag <figure> con classe epsiode-thumbnail
                Element img = locandine.select("img").first();//seleziono il tag <img>
                String nome = "S"+(1+i)+"E"+(1+j)+".jpg";//scrivo il nome da dare per ogni immagine
                downloadImage(img.attr("src"), nome); //chiamo la funzione per salvare le immagini, passandogli come attributi
                    //il link dell'immagine + il nome

                String pathComplete = path + "/" + nome;    //concateno il path + il nome dell'immagine

                writerEpisodi.write(pathComplete+","); //scrivo nel file il path

                writerEpisodi.write(1+i+"\n");  //scrivo nel file il numero della stagione associato all'episodio

                writerEpisodi.flush();
            }
            writer.flush();
        }
        writerEpisodi.close();
        writer.close();
    }


    private static void downloadImage(String strImageURL, String nome) {

        //get file name from image path




        System.out.println("Saving: " + nome + ", from: " + strImageURL);

        try {

            //open the stream from URL
            URL urlImage = new URL(strImageURL);
            InputStream in = urlImage.openStream();

            byte[] buffer = new byte[4096];
            int n = -1;

            OutputStream os =
                    new FileOutputStream(destinazione + "/" + nome);

            //write bytes to the output stream
            while ((n = in.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }

            //close the stream
            os.close();

            System.out.println("Image saved");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}