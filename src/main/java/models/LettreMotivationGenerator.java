package models;

import models.Candidature;

public class LettreMotivationGenerator {

    /**
     * Génère une lettre de motivation à partir des informations de la candidature.
     *
     * @param candidature La candidature contenant les informations nécessaires.
     * @return La lettre de motivation générée.
     */
    public static String genererLettreMotivation(Candidature candidature) {
        String utilisateurNom = candidature.getUtilisateur().getNom();
        String missionTitre = candidature.getMission().getTitre();
        String reponseQuestions = candidature.getReponseQuestions();

        // Exemple de lettre de motivation
        return "Objet : Candidature pour le poste de " + missionTitre + "\n\n" +
                "Madame, Monsieur,\n\n" +
                "Je souhaite postuler pour le poste de " + missionTitre + ".\n\n" +
                "En réponse à vos questions :\n" +
                reponseQuestions + "\n\n" +
                "Je reste à votre disposition pour un entretien.\n\n" +
                "Cordialement,\n" +
                utilisateurNom;
    }
}