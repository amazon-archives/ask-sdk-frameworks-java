/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.decisiontree.service;


import java.util.HashMap;
import java.util.Map;

public class RecommendationService {
    private final String[] professions = getOptions();
    private final Map<String, Integer> professionTraitMap = getProfessionTraitMap();

    public String getRecommendation(String salaryImportance, String personality, String bloodTolerance, String preference) {
        int professionIndex = getProfessionFromTraits(salaryImportance, personality, bloodTolerance, preference);
        return getRecommendationFromIndex(professionIndex);
    }

    private int getProfessionFromTraits(String salaryImportance, String personality, String bloodTolerance, String preference) {
        String key = salaryImportance + "-" + personality + "-" + bloodTolerance + "-" + preference;

        return professionTraitMap.get(key);
    }

    private String getRecommendationFromIndex(int index) {
        return professions[index];
    }

    private Map<String, Integer> getProfessionTraitMap() {
        Map<String, Integer> slotToOption = new HashMap<>();
        slotToOption.put("unimportant-introvert-low-animals", 20);
        slotToOption.put("unimportant-introvert-low-people", 8);
        slotToOption.put("unimportant-introvert-high-animals", 1);
        slotToOption.put("unimportant-introvert-high-people", 4);
        slotToOption.put("unimportant-extrovert-low-animals", 10);
        slotToOption.put("unimportant-extrovert-low-people", 3);
        slotToOption.put("unimportant-extrovert-high-animals", 11);
        slotToOption.put("unimportant-extrovert-high-people", 13);
        slotToOption.put("somewhat-introvert-low-animals", 20);
        slotToOption.put("somewhat-introvert-low-people", 6);
        slotToOption.put("somewhat-introvert-high-animals", 19);
        slotToOption.put("somewhat-introvert-high-people", 14);
        slotToOption.put("somewhat-extrovert-low-animals", 2);
        slotToOption.put("somewhat-extrovert-low-people", 12);
        slotToOption.put("somewhat-extrovert-high-animals", 17);
        slotToOption.put("somewhat-extrovert-high-people", 16);
        slotToOption.put("very-introvert-low-animals", 9);
        slotToOption.put("very-introvert-low-people", 15);
        slotToOption.put("very-introvert-high-animals", 17);
        slotToOption.put("very-introvert-high-people", 7);
        slotToOption.put("very-extrovert-low-animals", 17);
        slotToOption.put("very-extrovert-low-people", 0);
        slotToOption.put("very-extrovert-high-animals", 1);
        slotToOption.put("very-extrovert-high-people", 5);

        return slotToOption;
    }

    private String[] getOptions() {
        return new String[]{
            "Actor",
            "Animal Control Worker",
            "Animal Shelter Manager",
            "Artist",
            "Court Reporter",
            "Doctor",
            "Geoscientist",
            "Investment Banker",
            "Lighthouse Keeper",
            "Marine Ecologist",
            "Park Naturalist",
            "Pet Groomer",
            "Physical Therapist",
            "Security Guard",
            "Social Media Engineer",
            "Software Engineer",
            "Teacher",
            "Veterinary",
            "Veterinary Dentist",
            "Zookeeper",
            "Zoologist",
        };
    }
}
