package models;

import models.Mission;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PDFGenerator {

    /**
     * Génère un PDF avec les détails de la mission.
     *
     * @param mission  La mission à inclure dans le PDF.
     * @param filePath Le chemin où enregistrer le fichier PDF.
     */
    public static void generateMissionPDF(Mission mission, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Définir la police et la taille du texte
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Détails de la Mission");
                contentStream.endText();

                // Ajouter les détails de la mission
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Titre : " + mission.getTitre());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Description : " + mission.getDescription());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Compétences : " + mission.getCompetance());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Durée : " + mission.getDuree() + " jours");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Budget : " + mission.getBudget() + " €");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Date de publication : " + mission.getDatePub().toString());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Questions : " + mission.getQuestions());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Nom de l'entreprise : " + mission.getNomEntreprise());
                contentStream.endText();
            }

            // Sauvegarder le document PDF
            document.save(filePath);
            System.out.println("PDF généré avec succès : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Génère un PDF pour la lettre de motivation.
     *
     * @param lettreMotivation Le contenu de la lettre de motivation.
     * @param filePath         Le chemin où enregistrer le fichier PDF.
     */
    public static void generateLettreMotivationPDF(String lettreMotivation, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Définir la police et la taille du texte pour le titre
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Lettre de Motivation");
                contentStream.endText();

                // Ajouter le contenu de la lettre de motivation
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 700);
                for (String line : lettreMotivation.split("\n")) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15); // Espacement entre les lignes
                }
                contentStream.endText();
            }

            // Sauvegarder le document PDF
            document.save(filePath);
            System.out.println("PDF généré avec succès : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}