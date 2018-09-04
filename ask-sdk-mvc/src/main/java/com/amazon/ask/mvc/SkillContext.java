package com.amazon.ask.mvc;

import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.mvc.mapper.ControllerMethodContext;
import com.amazon.ask.mvc.plugin.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Set;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * A MVC skill's context. Contains resolvers, the skill model, controllers and an object mapper.
 *
 * @see ControllerMethodContext
 */
public class SkillContext {
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    private final Set<Object> controllers;
    private final Model model;
    private final ObjectMapper objectMapper;

    private final Set<RequestHandlerResolver> requestHandlerResolvers;
    private final Set<ArgumentResolver> argumentResolvers;
    private final Set<PredicateResolver> predicateResolvers;
    private final Set<RequestInterceptorResolver> requestInterceptorResolvers;
    private final Set<ResponseInterceptorResolver> responseInterceptorResolvers;
    private final Set<ExceptionHandlerResolver> exceptionHandlerResolvers;
    private final Set<ViewResolver> viewResolvers;

    private SkillContext(Set<Object> controllers,
                         Model model,
                         ObjectMapper objectMapper,
                         Set<RequestHandlerResolver> requestHandlerResolvers,
                         Set<ArgumentResolver> argumentResolvers,
                         Set<PredicateResolver> predicateResolvers,
                         Set<RequestInterceptorResolver> requestInterceptorResolvers,
                         Set<ResponseInterceptorResolver> responseInterceptorResolvers,
                         Set<ExceptionHandlerResolver> exceptionHandlerResolvers,
                         Set<ViewResolver> viewResolvers) {
        this.controllers = Collections.unmodifiableSet(assertNotNull(controllers, "controllers"));
        this.model = model == null ? Model.empty() : model;
        this.objectMapper = objectMapper == null ? DEFAULT_MAPPER : objectMapper;

        this.requestHandlerResolvers = emptyOrImmutable(requestHandlerResolvers);
        this.argumentResolvers = emptyOrImmutable(argumentResolvers);
        this.predicateResolvers = emptyOrImmutable(predicateResolvers);
        this.requestInterceptorResolvers = emptyOrImmutable(requestInterceptorResolvers);
        this.responseInterceptorResolvers = emptyOrImmutable(responseInterceptorResolvers);
        this.exceptionHandlerResolvers = emptyOrImmutable(exceptionHandlerResolvers);
        this.viewResolvers = emptyOrImmutable(viewResolvers);
    }

    private static <T> Set<T> emptyOrImmutable(Set<T> set) {
        return set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
    }

    public Set<Object> getControllers() {
        return controllers;
    }

    public Model getModel() {
        return model;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public Set<RequestHandlerResolver> getRequestHandlerResolvers() {
        return requestHandlerResolvers;
    }

    public Set<ArgumentResolver> getArgumentResolvers() {
        return argumentResolvers;
    }

    public Set<PredicateResolver> getPredicateResolvers() {
        return predicateResolvers;
    }

    public Set<RequestInterceptorResolver> getRequestInterceptorResolvers() {
        return requestInterceptorResolvers;
    }

    public Set<ResponseInterceptorResolver> getResponseInterceptorResolvers() {
        return responseInterceptorResolvers;
    }

    public Set<ExceptionHandlerResolver> getExceptionHandlerResolvers() {
        return exceptionHandlerResolvers;
    }

    public Set<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Set<Object> controllers;
        private Model model;
        private ObjectMapper objectMapper;

        private Set<ArgumentResolver> argumentResolvers;
        private Set<ExceptionHandlerResolver> exceptionHandlerResolvers;
        private Set<RequestHandlerResolver> requestHandlerResolvers;
        private Set<RequestInterceptorResolver> requestInterceptorResolvers;
        private Set<ResponseInterceptorResolver> responseInterceptorResolvers;
        private Set<PredicateResolver> predicateResolvers;
        private Set<ViewResolver> viewResolvers;

        private Builder() {
        }

        public Builder withPredicateResolvers(Set<PredicateResolver> predicateResolvers) {
            this.predicateResolvers = predicateResolvers;
            return this;
        }

        public Builder withArgumentResolvers(Set<ArgumentResolver> argumentResolvers) {
            this.argumentResolvers = argumentResolvers;
            return this;
        }

        public Builder withExceptionHandlerResolvers(Set<ExceptionHandlerResolver> exceptionHandlerResolvers) {
            this.exceptionHandlerResolvers = exceptionHandlerResolvers;
            return this;
        }

        public Builder withRequestHandlerResolvers(Set<RequestHandlerResolver> requestHandlerResolvers) {
            this.requestHandlerResolvers = requestHandlerResolvers;
            return this;
        }

        public Builder withRequestInterceptorResolvers(Set<RequestInterceptorResolver> requestInterceptorResolvers) {
            this.requestInterceptorResolvers = requestInterceptorResolvers;
            return this;
        }

        public Builder withResponseInterceptorResolvers(Set<ResponseInterceptorResolver> responseInterceptorResolvers) {
            this.responseInterceptorResolvers = responseInterceptorResolvers;
            return this;
        }

        public Builder withViewResolvers(Set<ViewResolver> viewResolvers) {
            this.viewResolvers = viewResolvers;
            return this;
        }

        public Builder withModel(Model model) {
            this.model = model;
            return this;
        }

        public Builder withControllers(Set<Object> controllers) {
            this.controllers = controllers;
            return this;
        }

        public Builder withObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public SkillContext build() {
            return new SkillContext(controllers, model, objectMapper, requestHandlerResolvers, argumentResolvers,
                predicateResolvers, requestInterceptorResolvers, responseInterceptorResolvers, exceptionHandlerResolvers,
                viewResolvers);
        }
    }
}
