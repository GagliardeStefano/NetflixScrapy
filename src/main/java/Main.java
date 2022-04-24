import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static String UrlNetflix = "https://www.netflix.com/it/title/";
    private static String checkSerie = "tvSeries";


    public static void main(String[] args) throws IOException {

        File netflixCsv = new File("C:\\Users\\stefa\\Documents\\scuola\\TPSIT\\servlet\\NetScrapy\\archive\\netflix_list.csv");
        BufferedReader br = new BufferedReader(new FileReader(netflixCsv));

        String regex = (",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

        String[] arraySerie = new String[0];
        int readLine = 0;
        String row;

        while ((row = br.readLine()) != null) {

            arraySerie = row.split(regex);

            if (arraySerie[8].equals(checkSerie)) {

                readLine++;
                System.out.println("RIGA######" + readLine);

                String urlGoogle = "https://www.google.com/search?q=netflix+" + arraySerie[1].replace(" ", "+")
                        .replace(":", "%3A")
                        .replace("%", "%25")
                        .replace("'", "%27")
                        .replace(",", "%2C")
                        .replace("?", "%3F")
                        .replace("&", "%26")
                        .replace("\"", "")
                        .replace("¿", "")
                        .replace("è", "e")
                        .replace("é", "e")
                        .replace("ì", "i")
                        .replace("ò", "o")
                        .replace("ù", "u")
                        .replace("ô", "o")
                        .replace("û", "u")
                        .replace("î", "i")
                        .replace("í", "i")
                        .replace("ñ", "n")
                        .replace("â", "a")
                        .replace("ó", "o");


//                url del sito da rubare :)
                System.out.println("URL@@@@@" + urlGoogle);
                Document google = Jsoup.connect(urlGoogle).get();

                Elements link = google.select("div.yuRUbf");
                Element cite = link.select("cite").first();

                Elements a = link.select("a");
                String href = a.attr("href");
                String linkTagliato = href.substring(0, 33);

                if (linkTagliato.equals(UrlNetflix)) {

                    Document netflix = Jsoup.connect(href).get();
                    System.out.println("DOPO IF: nome del link: "+href);


                    String path = ".serieTV/imgEpisodi/"+arraySerie[1].replace(" ","") //per immagini episodi
                            .replace("&","")
                            .replace("'","");


                    File destinazione = new File( "C:\\xampp\\htdocs\\CTVS\\serieTv\\imgEpisodi\\"+arraySerie[1].replace(" ","")
                            .replace("&","")    //destinazione per salvare le immagini degli episodi
                            .replace("'","")
                            .replace(",",""));
                    if (!destinazione.exists()){
                        destinazione.mkdir();
                    }
                    File destinazioneAttributo = destinazione;

                    File percorsoImg = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\"+arraySerie[1].replace(" ","")
                            .replace("&","")
                            .replace("'","")
                            .replace(",",""));  //destinazione per salvare le immagini orizzontali
                    if (!percorsoImg.exists()){
                        percorsoImg.mkdir();
                    }

                    String nomeImgOrizzontale = arraySerie[1].replace(" ","")  //nome dell'immagine orizzontale
                            .replace("&","")
                            .replace("'","")
                            .replace(",","")+"Orizzontale"+".jpg";

                    File fileSerie = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\DatiSerie.csv");   //file per le serie
                    File fileStagioni = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\DatiSerieStagioni.csv");         //Creo file per le stagioni
                    File fileEpisodi = new File("C:\\xampp\\htdocs\\CTVS\\serieTv\\DatiSerieEpisodi.csv");    //Creo file per gli episodi
                    FileWriter writerSerie = new FileWriter(fileSerie);
                    FileWriter writerStagioni = new FileWriter(fileStagioni);   //oggetto per scrivere nel file delle stagioni
                    FileWriter writerEpisodi = new FileWriter(fileEpisodi); //oggetto per scrivere nel file degli episodi

                    writerSerie.write("IdSerie"+",");
                    writerSerie.write("Nome"+",");
                    writerSerie.write("Trama"+",");
                    writerSerie.write("Locandina"+",");
                    writerSerie.write("Voto"+",");
                    writerSerie.write("AnnoInizio"+",");
                    writerSerie.write("imgOrizzontale"+"\n");


                    writerStagioni.write("IdSerie" + ",");        //
                    writerStagioni.write("NumStagione" + ",");    //  Creo colonne
                    writerStagioni.write("NumEpisodi" + ",");     //  per file stagioni
                    writerStagioni.write("AnnoStagione" + "\n");  //


                    writerEpisodi.write("Titolo" + ",");      //
                    writerEpisodi.write("Durata" + ",");      //
                    writerEpisodi.write("Descrizione" + ","); // Creo colonne per file episodi
                    writerEpisodi.write("Img" + ",");         //
                    writerEpisodi.write("idStagione" + "\n"); //

                    writerSerie.write(href.substring(33, 40)+",");

                    Elements containerDati = netflix.select("div.details-container");
                    Elements titleInfo = containerDati.select("div.title-info");
                    Element nomeSerie = titleInfo.select("h1").first();
                    writerSerie.write(nomeSerie+","); //nome

                    Elements titleDescrizione = containerDati.select("div.title-info-synopsis-talent");
                    Element trama = titleDescrizione.select("div").first();
                    writerSerie.write(trama+","); //trama

                    String nomeLocandina = arraySerie[1].replace(" ","")  //nome dell'immagine orizzontale
                            .replace("&","")
                            .replace("'","")+"Locandina"+".jpg";

                    String locandina = arraySerie[18];
                    downloadImageOrizzontale(locandina,nomeLocandina, String.valueOf(percorsoImg));
                    String pathImg = percorsoImg + nomeLocandina;
                    writerSerie.write(pathImg+",");
                    writerSerie.write(arraySerie[13]+","); //voto

                    Elements titleMetadata = containerDati.select("div.title-info-metadata-wrapper");
                    Element dataInizio = titleMetadata.select("span").first();
                    writerSerie.write(dataInizio+","); //data Inizio

                    Elements imgContainer = netflix.select("div.hero-image-container");
                    Element imgStyle = imgContainer.select("div[style]").first();
                    String style = imgStyle.attr("style");
                    String imgValue = style.split("url")[1].replace("(","").replace("\"","");

                    downloadImageOrizzontale(imgValue,nomeImgOrizzontale, String.valueOf(percorsoImg));
                    String pathOrizzontale = percorsoImg + nomeImgOrizzontale;
                    writerSerie.write(pathOrizzontale+"\n");


                    Elements container = netflix.select("div#seasons-and-episodes-list-container");
                    Elements season = container.select("div.season");


                    for (int i=0; i<season.size(); i++){    //ciclo for per le stagioni

                        System.out.println("DOPO FOR STAGIONI");
                        //STAGIONI
                        writerStagioni.write(href.substring(33, 40)+",");  //scrivo nel file l'id della Serie TV
                        writerStagioni.write(1+i+",");  //scrivo nel file il numero della stagione

                        Elements episodi = season.get(i).select("div.episodes-container");  //seleziono l'elemento eiesimo di div con classe episodes-container
                        Elements nEpisodi = episodi.select("div.episode");  //seleziono l'elemento div con classe episode
                        writerStagioni.write(nEpisodi.size()+",");  //scrivo nel file il numero di episodi per ogni stagione

                        //seleziono l'elemento eiesimo di div con classe season-release-year
                        Element anni = season.get(i).select("div.season-release-year").first();
                        writerStagioni.write(anni.text()+"\n"); //scrivo nel file l'anno in cui è uscita la stagione

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
                            downloadImage(img.attr("src"), nome, String.valueOf(destinazioneAttributo)); //chiamo la funzione per salvare le immagini, passandogli come attributi
                            //il link dell'immagine + il nome

                            String pathComplete = path + "/" + nome;    //concateno il path + il nome dell'immagine

                            writerEpisodi.write(pathComplete+","); //scrivo nel file il path

                            writerEpisodi.write(1+i+"\n");  //scrivo nel file il numero della stagione associato all'episodio

                            writerEpisodi.flush();
                        }
                        writerStagioni.flush();
                    }
                    writerSerie.flush();

                    writerEpisodi.close();
                    writerStagioni.close();
                    writerSerie.close();
                }

           }
        }


    }

    private static void downloadImage(String strImageURL, String nome, String destinazione) {

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


    private static void downloadImageOrizzontale(String strImageURL, String nome, String destinazione) {

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




