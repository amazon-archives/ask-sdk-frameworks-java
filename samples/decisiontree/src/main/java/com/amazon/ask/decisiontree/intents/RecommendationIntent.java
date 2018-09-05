/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.decisiontree.intents;

import com.amazon.ask.decisiontree.slots.*;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.type.Intent;

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
