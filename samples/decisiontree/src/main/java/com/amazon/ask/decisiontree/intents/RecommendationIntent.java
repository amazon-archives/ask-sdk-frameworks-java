package com.amazon.ask.decisiontree.intents;

import com.amazon.ask.decisiontree.slots.*;
import com.amazon.ask.models.annotation.data.SlotProperty;
import com.amazon.ask.models.annotation.data.IntentResource;
import com.amazon.ask.models.annotation.type.Intent;

@Intent("RecommendationIntent")
@IntentResource("recommendation_intent")
public class RecommendationIntent {
    @SlotProperty
    private SalaryImportanceTypeSlot salaryImportance;

    @SlotProperty
    private PersonalityTypeSlot personality;

    @SlotProperty
    private PreferredSpeciesTypeSlot preferredSpecies;

    @SlotProperty
    private BloodToleranceTypeSlot bloodTolerance;

    @SlotProperty
    private ISubjectTypeSlot iWant;

    @SlotProperty
    private ISubjectTypeSlot iLike;

    @SlotProperty
    private ArticleTypeSlot article;

    @SlotProperty
    private IAmTypeSlot iAm;

    public SalaryImportanceTypeSlot getSalaryImportance() {
        return salaryImportance;
    }

    public void setSalaryImportance(SalaryImportanceTypeSlot salaryImportance) {
        this.salaryImportance = salaryImportance;
    }

    public PersonalityTypeSlot getPersonality() {
        return personality;
    }

    public void setPersonality(PersonalityTypeSlot personality) {
        this.personality = personality;
    }

    public PreferredSpeciesTypeSlot getPreferredSpecies() {
        return preferredSpecies;
    }

    public void setPreferredSpecies(PreferredSpeciesTypeSlot preferredSpecies) {
        this.preferredSpecies = preferredSpecies;
    }

    public BloodToleranceTypeSlot getBloodTolerance() {
        return bloodTolerance;
    }

    public void setBloodTolerance(BloodToleranceTypeSlot bloodTolerance) {
        this.bloodTolerance = bloodTolerance;
    }

    public ISubjectTypeSlot getiWant() {
        return iWant;
    }

    public void setiWant(ISubjectTypeSlot iWant) {
        this.iWant = iWant;
    }

    public ISubjectTypeSlot getiLike() {
        return iLike;
    }

    public void setiLike(ISubjectTypeSlot iLike) {
        this.iLike = iLike;
    }

    public ArticleTypeSlot getArticle() {
        return article;
    }

    public void setArticle(ArticleTypeSlot article) {
        this.article = article;
    }

    public IAmTypeSlot getiAm() {
        return iAm;
    }

    public void setiAm(IAmTypeSlot iAm) {
        this.iAm = iAm;
    }
}
