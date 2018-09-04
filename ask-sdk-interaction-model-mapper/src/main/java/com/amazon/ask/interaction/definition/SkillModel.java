package com.amazon.ask.interaction.definition;

import java.util.*;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Programmatically model a skill.
 */
public class SkillModel {
    private final Map<Locale, String> invocationNames;
    private final Model model;

    public SkillModel(Map<Locale, String> invocationNames, Model model) {
        this.invocationNames = assertNotNull(invocationNames, "invocationNames");
        this.model = assertNotNull(model, "moduleDefinition");
    }

    public Map<Locale, String> getInvocationNames() {
        return invocationNames;
    }

    public Model getModel() {
        return model;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Model.Builder modelBuilder;
        private Map<Locale, String> invocationNames;
        private List<Model> models;

        public Builder() {
            this(Model.builder());
        }

        public Builder(Model.Builder modelBuilder) {
            this.modelBuilder = assertNotNull(modelBuilder, "modelBuilder");
        }

        public Builder withModels(List<Model> models) {
            this.models = models;
            return this;
        }

        public Builder addModels(List<Model> models) {
            models.forEach(this::addModel);
            return this;
        }
        public Builder addModel(Model model) {
            if (this.models == null) {
                this.models = new ArrayList<>();
            }
            this.models.add(model);
            return this;
        }

        public Builder withInvocationNames(Map<Locale, String> invocationNames) {
            this.invocationNames = invocationNames;
            return this;
        }

        public Builder addInvocationNames(Map<Locale, String> invocationNames) {
            this.invocationNames = invocationNames;
            return this;
        }

        /**
         * Specify an invocation name for a locale
         *
         * @param locale
         * @param name invocation name for this locale
         * @return this
         */
        public Builder withInvocationName(String locale, String name) {
            return withInvocationName(Locale.forLanguageTag(locale), name);
        }

        public Builder withInvocationName(Locale locale, String name) {
            if (this.invocationNames == null) {
                this.invocationNames = new HashMap<>();
            }
            this.invocationNames.put(locale, name);
            return this;
        }

        public SkillModel build() {
            if (models != null) {
                models.forEach(modelBuilder::merge); // TODO: isolate associated global ... merge is technically a leak
            }
            return new SkillModel(invocationNames, modelBuilder.build());
        }
    }
}
