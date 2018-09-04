package com.amazon.ask.interaction.definition;

import com.amazon.ask.interaction.TypeReflector;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.annotation.data.AlexaIgnore;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.data.SlotTypeResource;
import com.amazon.ask.interaction.annotation.plugin.AutoIntentData;
import com.amazon.ask.interaction.annotation.plugin.AutoSlotTypeData;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.data.IntentDataResolver;
import com.amazon.ask.interaction.data.IntentDataSource;
import com.amazon.ask.interaction.data.SlotTypeDataResolver;
import com.amazon.ask.interaction.data.SlotTypeDataSource;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.interaction.renderer.ModelRenderer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Pattern;

import static com.amazon.ask.interaction.Utils.*;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;


/**
 * Defines a grouping of intents and slot types, encapsulating their interaction model and parsing logic.
 *
 * @see IntentMapper
 * @see ModelRenderer
 */
@SuppressWarnings("unchecked")
public class Model {
    private static final Model EMPTY = Model.builder().build();

    private final Map<String, IntentDefinition> intentDefinitions;
    private final Map<String, SlotTypeDefinition> slotTypes;

    private final Set<IntentDataResolver> intentDataResolvers;
    private final Set<SlotTypeDataResolver> slotTypeDataResolvers;

    private final Map<IntentDefinition, Set<IntentDataSource>> intentDataSources;
    private final Map<SlotTypeDefinition, Set<SlotTypeDataSource>> slotTypeDataSources;

    public Model(Map<String, IntentDefinition> intentDefinitions,
                 Map<String, SlotTypeDefinition> slotTypes,
                 Set<IntentDataResolver> intentDataResolvers,
                 Set<SlotTypeDataResolver> slotTypeDataResolvers,
                 Map<IntentDefinition, Set<IntentDataSource>> intentDataSources,
                 Map<SlotTypeDefinition, Set<SlotTypeDataSource>> slotTypeDataSources) {

        this.intentDefinitions = Collections.unmodifiableMap(assertNotNull(intentDefinitions, "intentDefinitions"));
        this.slotTypes = Collections.unmodifiableMap(assertNotNull(slotTypes, "slotTypes"));
        this.intentDataResolvers = intentDataResolvers == null ? Collections.emptySet() : Collections.unmodifiableSet(intentDataResolvers);
        this.slotTypeDataResolvers = slotTypeDataResolvers == null ? Collections.emptySet() : Collections.unmodifiableSet(slotTypeDataResolvers);
        this.intentDataSources = intentDataSources == null ? Collections.emptyMap() : unmodifiableMapOfSets(intentDataSources);
        this.slotTypeDataSources = slotTypeDataSources == null ? Collections.emptyMap() : unmodifiableMapOfSets(slotTypeDataSources);
    }

    public Map<String, IntentDefinition> getIntentDefinitions() {
        return intentDefinitions;
    }

    public Map<String, SlotTypeDefinition> getSlotTypes() {
        return slotTypes;
    }

    public Set<IntentDataResolver> getIntentDataResolvers() {
        return intentDataResolvers;
    }

    public Set<SlotTypeDataResolver> getSlotTypeDataResolvers() {
        return slotTypeDataResolvers;
    }

    public Map<IntentDefinition, Set<IntentDataSource>> getIntentDataSources() {
        return intentDataSources;
    }

    public Map<SlotTypeDefinition, Set<SlotTypeDataSource>> getSlotTypeDataSources() {
        return slotTypeDataSources;
    }

    public static Model empty() {
        return EMPTY;
    }

    /**
     * Constructs a standard builder with support for {@link AutoIntentData} and {@link AutoSlotTypeData}.
     *
     * By default, this includes {@link IntentResource} and {@link SlotTypeResource}.
     *
     * @return a standard builder with default data resolvers.
     */
    public static Builder builder() {
        return emptyBuilder()
            .addIntentDataResolver(new AutoIntentData.Scanner())
            .addSlotTypeDataResolver(new AutoSlotTypeData.Scanner());
    }

    /**
     * @return a builder with no configured data resolvers.
     */
    public static Builder emptyBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Set<IntentDataResolver> intentDataResolvers;
        private Set<SlotTypeDataResolver> slotTypeDataResolvers;

        private final Map<IntentDefinition, Set<IntentDataSource>> intentDataSources = new TreeMap<>(Comparator.comparing(IntentDefinition::getName));
        private final Map<SlotTypeDefinition, Set<SlotTypeDataSource>> slotTypeDataSources = new TreeMap<>(Comparator.comparing(SlotTypeDefinition::getName));

        private final Map<String, IntentDefinition> intentDefinitions = new TreeMap<>();
        private final Map<String, SlotTypeDefinition> slotTypes = new TreeMap<>();

        public Model build() {
            return new Model(intentDefinitions, slotTypes, intentDataResolvers, slotTypeDataResolvers, intentDataSources, slotTypeDataSources);
        }

        public Builder merge(Model other) {
            this.intentDefinitions.putAll(other.intentDefinitions);
            this.slotTypes.putAll(other.slotTypes);
            other.intentDataResolvers.forEach(this::addIntentDataResolver);
            other.slotTypeDataResolvers.forEach(this::addSlotTypeDataResolver);
            mergeMapOfSets(other.intentDataSources, this.intentDataSources);
            mergeMapOfSets(other.slotTypeDataSources, this.slotTypeDataSources);

            return this;
        }

        /**
         *
         * @param resolvers
         * @return
         */
        public Builder withIntentDataResolvers(Collection<IntentDataResolver> resolvers) {
            this.intentDataResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }

        public Builder addIntentDataResolvers(Collection<IntentDataResolver> resolvers) {
            resolvers.forEach(this::addIntentDataResolver);
            return this;
        }

        public Builder addIntentDataResolver(IntentDataResolver resolver) {
            if (intentDataResolvers == null) {
                this.intentDataResolvers = new LinkedHashSet<>();
            }
            this.intentDataResolvers.add(resolver);
            return this;
        }

        /**
         *
         * @param resolvers
         * @return
         */
        public Builder withSlotTypeDataResolvers(Collection<SlotTypeDataResolver> resolvers) {
            this.slotTypeDataResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }

        public Builder addSlotTypeDataResolvers(Collection<SlotTypeDataResolver> resolvers) {
            resolvers.forEach(this::addSlotTypeDataResolver);
            return this;
        }

        public Builder addSlotTypeDataResolver(SlotTypeDataResolver resolver) {
            if (this.slotTypeDataResolvers == null) {
                this.slotTypeDataResolvers = new LinkedHashSet<>();
            }
            this.slotTypeDataResolvers.add(resolver);
            return this;
        }

        /**
         * Register a simple intent this module
         *
         * @param intentClass intent class
         * @return this
         * @see Builder#addIntent(JavaType)
         */
        public Builder intent(Class<?> intentClass) {
            return addIntent(toJavaType(intentClass));
        }

        /**
         * Add an intent and a data supplier
         *
         * @param intentClass
         * @param data
         * @return
         */
        public Builder intent(Class<?> intentClass, IntentDataSource data) {
            return addIntent(toJavaType(intentClass), data);
        }

        /**
         * Add an intent along add a
         * @param intentClass
         * @param data
         * @return
         */
        public Builder intent(Class<?> intentClass, Collection<IntentDataSource> data) {
            JavaType javaType = toJavaType(intentClass);
            data.forEach(d -> addIntent(javaType, d));
            return this;
        }

        /**
         * Register a generic intent add this module.
         *
         * @param intentClass parameterized intent class
         * @param slotTypes classes of slots to parameterize the intent add
         * @return this
         * @see Builder#addIntent(JavaType)
         */
        public Builder genericIntent(Class<?> intentClass, Class<?>... slotTypes) {
            return addIntent(TypeFactory.defaultInstance().constructParametricType(intentClass, slotTypes));
        }

        /**
         * Associate data with a generic intent.
         */
        public Builder genericIntent(Class<?> intentClass, Collection<Class<?>> slotTypes, IntentDataSource data) {
            return addIntent(toJavaType(intentClass, slotTypes), data);
        }

        /**
         * Register an intent with this module. The Class must adhere to the java bean standard and its properties
         * must be valid {@link SlotTypeDefinition} types.
         */
        protected Builder addIntent(JavaType javaType) {
            return addIntent(getIntentDefinition(javaType));
        }

        /**
         * Associate data with an addIntent.
         */
        protected Builder addIntent(JavaType javaType, IntentDataSource data) {
            IntentDefinition intent = getIntentDefinition(javaType);
            this.intentDataSources.computeIfAbsent(intent, k -> new LinkedHashSet<>()).add(data);
            return addIntent(intent);
        }

        /**
         * Register an intent definition with this module
         *
         * TODO: Check for overwrite?
         *
         * @param intentDefinition
         * @return this
         */
        protected Builder addIntent(IntentDefinition intentDefinition) {
            this.intentDefinitions.put(intentDefinition.getName(), intentDefinition);
            for (SlotTypeDefinition slotType: intentDefinition.getSlots().values()) {
                this.slotType(slotType, null);
            }
            return this;
        }

        protected IntentDefinition getIntentDefinition(JavaType javaType) {
            Class intentClass = javaType.getRawClass();
            TypeReflector<?> reflector = new TypeReflector<>(javaType);

            Map<String, SlotTypeDefinition> slots = resolveSlots(reflector);

            return IntentDefinition.builder()
                .withIntentType(javaType)
                .withName(validateTypeName(getIntentName(javaType)))
                .withSlots(slots)
                .withCustom(findAnnotation(intentClass, BuiltIn.class) == null)
                .build();
        }

        protected String getIntentName(JavaType javaType) {
            Intent intent = getIntentAnnotation(javaType);
            StringBuilder name = new StringBuilder(intent.value().isEmpty() ? javaType.getRawClass().getSimpleName() : intent.value());
            if (javaType.hasGenericTypes()) {
                for (JavaType slotType : javaType.getBindings().getTypeParameters()) {
                    name.append("_").append(resolveSlotType(slotType.getRawClass())
                        .getName()
                        .replaceAll("\\.", "_"));
                }
            }
            return name.toString();
        }

        protected Intent getIntentAnnotation(JavaType javaType) {
            Intent intent = findAnnotation(javaType.getRawClass(), Intent.class);
            if (intent == null) {
                throw new IllegalArgumentException(String.format("Class %s is missing %s annotation", javaType.getTypeName(), Intent.class.getName()));
            }
            return intent;
        }

        /**
         * Explicitly with a slot type to the interaction model.
         *
         * @param slotClass class of the slot
         * @return this
         */
        public Builder slotType(Class<?> slotClass) {
            return slotType(resolveSlotType(slotClass), null);
        }

        /**
         * Add more interaction model data for a slot type.
         *
         * @param slotClass type of slot
         * @param data associateSource of slot data
         * @return this
         */
        public Builder slotType(Class<?> slotClass, SlotTypeDataSource data) {
            return slotType(resolveSlotType(slotClass), data);
        }

        public Builder slotType(SlotTypeDefinition slotType, SlotTypeDataSource data) {
            SlotTypeDefinition existingSlotType = this.slotTypes.get(slotType.getName());
            if (existingSlotType != null && !existingSlotType.equals(slotType)) {
                throw new IllegalArgumentException(String.format("Attempted to register two different slot types with name '%s': %s and %s", slotType.getName(), slotType, existingSlotType));
            }

            this.slotTypes.put(slotType.getName(), slotType);
            if (data != null) {
                this.slotTypeDataSources.computeIfAbsent(slotType, k -> new LinkedHashSet<>()).add(data);
            }
            return this;
        }

        protected Map<String, SlotTypeDefinition> resolveSlots(TypeReflector<?> reflector) {
            Map<String, SlotTypeDefinition> slots = new LinkedHashMap<>();

            // TODO: order in some consistent way?
            for (PropertyDescriptor property : reflector.getPropertyDescriptors()) {
                if (reflector.getAnnotation(property, AlexaIgnore.class) != null) {
                    continue;
                }

                SlotProperty slotProperty = reflector.getAnnotation(property, SlotProperty.class);
                if (slotProperty != null) {
                    String name = slotProperty.name().isEmpty() ? property.getName() : slotProperty.name();

                    if (slots.containsKey(name)) {
                        throw new IllegalArgumentException("Slot '" + name + "' defined multiple times on intent '" + reflector.getJavaType().toString());
                    }

                    Class<?> slotClass = property.getPropertyType();
                    if (slotProperty.type() != Object.class) {
                        slotClass = slotProperty.type();
                    } else if (slotClass == Object.class) {
                        slotClass = reflector.reifyPropertyType(property);
                    }

                    slots.put(name, resolveSlotType(slotClass));
                }
            }

            return slots;
        }

        protected SlotTypeDefinition resolveSlotType(Class<?> slotClass) {
            SlotType slot = Utils.findAnnotation(slotClass, SlotType.class);
            boolean builtIn = slotClass.getAnnotation(BuiltIn.class) != null;
            if (slot == null) {
                throw new IllegalArgumentException(String.format("Slot class '%s' is missing '%s' annotation", slotClass.getName(), SlotTypeDefinition.class.getName()));
            }

            return SlotTypeDefinition.builder()
                .withSlotTypeClass(slotClass)
                .withName(validateTypeName(slot.value().isEmpty() ? slotClass.getSimpleName() : slot.value()))
                .withCustom(!builtIn)
                .build();
        }

        // https://developer.amazon.com/docs/custom-skills/create-intents-utterances-and-slots.html#intent-name
        private static final Pattern VALID_TYPE_REGEX = Pattern.compile("^(AMAZON\\.[A-Za-z_]+|[A-Za-z][A-Za-z_]*[A-Za-z])$");
        protected String validateTypeName(String name) {
            if (!VALID_TYPE_REGEX.matcher(name).matches()) {
                throw new IllegalArgumentException("Type name '" + name + "' does not match regex: " + VALID_TYPE_REGEX.pattern());
            }
            return name;
        }

        private static JavaType toJavaType(Class<?> clazz) {
            return TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[]{});
        }

        private static JavaType toJavaType(Class<?> clazz, Collection<Class<?>> slotTypes) {
            return TypeFactory.defaultInstance().constructParametricType(clazz, slotTypes.toArray(new Class<?>[]{}));
        }
    }
}
