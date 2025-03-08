package models;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatistiqueAvis {

    private List<Avis> avisList;

    public StatistiqueAvis(List<Avis> avisList) {
        this.avisList = avisList;
    }

    // Méthode pour compter le nombre d'avis par type
    public Map<String, Long> nombreAvisParType() {
        return avisList.stream()
                .collect(Collectors.groupingBy(Avis::getTypeAvis, Collectors.counting()));
    }
}
