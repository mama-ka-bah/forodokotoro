package com.foro.forordokotoro.Utils.Configurations;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;



public class ConfigImages {

    public static String localhost = "http://127.0.0.1/";
    public static String serveruser = localhost + "forodokotoro/images/profils/";
    public static String serverproduitagicole = localhost + "forodokotoro/images/produitsAgricoles/";
    public static String serverchamp = localhost + "forodokotoro/images/champs/";
    public static String serverstocks = localhost + "forodokotoro/images/stocks/";
    public static String servervarietes = localhost + "forodokotoro/images/varietes/";
    public static String serverphases = localhost + "forodokotoro/images/phases/";
    public static String serverpermis = localhost + "forodokotoro/images/permis/";

    public static String Userlocation = "C:/xampp/htdocs/forodokotoro/images/profils/";
    public static String Produitagicolelocation = "C:/xampp/htdocs/forodokotoro/images/produitsAgricoles/";
    public static String Champlocation = "C:/xampp/htdocs/forodokotoro/images/champs/";
    public static String Stockslocation = "C:/xampp/htdocs/forodokotoro/images/stocks/";
    public static String Varieteslocation = "C:/xampp/htdocs/forodokotoro/images/varietes/";
    public static String Phaseslocation = "C:/xampp/htdocs/forodokotoro/images/phases/";
    public static String Permislocation = "C:/xampp/htdocs/forodokotoro/images/permis/";


    //pour l'image de l'entite
    //public static void saveimg(String uploaDir, String nomfile, MultipartFile multipartFile) throws IOException{
    public static String saveimg(String typeImage, String fileName, MultipartFile file) throws IOException{

    /*
        Path UploadPath = Paths.get(uploaDir);

        if(!Files.exists(UploadPath)) {
            Files.createDirectories(UploadPath);
        }
            try(InputStream inputStream = multipartFile.getInputStream()){
                Path fichierPath = UploadPath.resolve(nomfile);

                Files.copy(inputStream, fichierPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe){
                throw new IOException("Impossible d'enregistrer le fichier image:" + nomfile, ioe);
            }

     */


        String src = "";
        String server = "";
        String location = "";
        if (typeImage == "user") {
            location = Userlocation;
            server = serveruser;
        } else if (typeImage == "champs") {
            location = Champlocation;
            server = serverchamp;
        }
        else if(typeImage == "produitsAgicoles"){
            location = Produitagicolelocation;
            server = serverproduitagicole;
        }else if(typeImage == "varietes"){
        location = Varieteslocation;
        server = servervarietes;
        }else if(typeImage == "phases"){
            location = Phaseslocation;
            server = serverphases;
        }else if(typeImage == "permis"){
            location = Permislocation;
            server = serverpermis;
        }
        else{
            location = Stockslocation;
            server = serverstocks;
        }
        try {
            //Path filePath = Paths.get(location + fileName);
            Path filePath = Paths.get(location + fileName);

            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.copy(file.getInputStream(), filePath);
                src = server + fileName ;
            } else {
                Files.delete(filePath);
                Files.copy(file.getInputStream(), filePath);
                src = server + fileName ;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle exception
            src = null;
        }

        return src;
    }

}






    /*
    public static void saveimgA(String uploaDira, String nomfilea, MultipartFile multipartFile) throws IOException{

        Path UploadPatha = Paths.get(uploaDira);

        if(!Files.exists(UploadPatha)) {
            Files.createDirectories(UploadPatha);
        }
        try(InputStream inputStream = multipartFile.getInputStream()){
            Path fichierPatha = UploadPatha.resolve(nomfilea);

            Files.copy(inputStream, fichierPatha, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe){
            throw new IOException("Impossible d'enregistrer le fichier image:" + nomfilea, ioe);
        }
    }

     */








