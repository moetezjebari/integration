package services;

import models.Candidature;

import java.util.List;

public interface ICandidature {
    boolean add(Candidature candidature);
    void update(Candidature candidature);
    void delete(int id);
    Candidature getById(int id);
    List<Candidature> getAll();
}