package com.amazon.ask.mvc;

import com.amazon.ask.dispatcher.request.handler.impl.DefaultHandlerAdapter;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.module.SdkModule;
import com.amazon.ask.module.SdkModuleContext;
import com.amazon.ask.mvc.annotation.plugin.*;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;
import com.amazon.ask.mvc.argument.*;
import com.amazon.ask.mvc.mapper.ControllerRequestMapper;
import com.amazon.ask.mvc.annotation.mapping.RequestMapping;
import com.amazon.ask.mvc.plugin.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * {@link SdkModule} adding support for MVC mapping annotations, slot argument resolvers and view resolvers.
 *
 * @see IntentMapping
 * @see RequestMapping
 * @see com.amazon.ask.mvc.annotation.mapping.ExceptionHandler
 * @see com.amazon.ask.mvc.annotation.mapping.RequestInterceptor
 * @see com.amazon.ask.mvc.annotation.mapping.ResponseInterceptor
 * @see ArgumentResolver
 * @see ViewResolver
 * @see SkillContext
 */
public class MvcSdkModule implements SdkModule {
    /**
     * Default set of {@link ArgumentResolver}s
     */
    protected static final Collection<ArgumentResolver> DEFAULT_ARGUMENT_RESOLVERS =
        Collections.unmodifiableList(Arrays.asList(
            new SlotValuesArgumentResolver(),
            new SlotValueArgumentResolver(),
            new SlotArgumentResolver(),
            new RequestArgumentResolver(),
            new IntentArgumentResolver(),
            new RequestEnvelopeArgumentResolver(),
            new SessionArgumentResolver(),
            new SessionAttributesMapArgumentResolver(),
            new AttributesManagerArgumentResolver(),
            new ResponseBuilderArgumentResolver(),
            new ServiceClientFactoryArgumentResolver(),
            new LocaleArgumentResolver()));

    protected static final String MVC_USER_AGENT = "ask-mvc/1.0-beta";
    protected static final String MODELS_USER_AGENT = "ask-models/1.0-beta";

    protected final SkillContext skillContext;

    public MvcSdkModule(SkillContext skillContext) {
        this.skillContext = assertNotNull(skillContext, "context");
    }

    /**
     * Scans all controllers for methods, set up handlers and interceptors, and append user agent.
     *
     * @param moduleContext sdk module context
     */
    @Override
    public void setupModule(SdkModuleContext moduleContext) {
        moduleContext.addHandlerAdapter(new DefaultHandlerAdapter());

        moduleContext.appendCustomUserAgent(MVC_USER_AGENT);
        if (!skillContext.getModel().equals(Model.empty())) {
            moduleContext.appendCustomUserAgent(MODELS_USER_AGENT);
        }

        for (Object controller : skillContext.getControllers()) {
            moduleContext.addRequestMapper(new ControllerRequestMapper(skillContext, controller));
        }
    }

    /**
     * @return a builder with default features enabled
     */
    public static Builder builder() {
        return emptyBuilder()
            .addArgumentResolvers(DEFAULT_ARGUMENT_RESOLVERS)
            .addArgumentResolver(new AutoArgumentResolver.Scanner())
            .addRequestHandlerResolver(new AutoRequestHandler.Scanner())
            .addResponseInterceptorResolver(new AutoResponseInterceptor.Scanner())
            .addRequestInterceptorResolver(new AutoRequestInterceptor.Scanner())
            .addPredicateResolver(new AutoPredicate.Scanner())
            .addExceptionHandlerResolver(new AutoExceptionHandler.Scanner());
    }

    /**
     * @return a builder with no features enabled
     */
    public static Builder emptyBuilder() {
        return new Builder();
    }
    
    public static class Builder {
        protected Set<ArgumentResolver> argumentResolvers;
        protected Set<ExceptionHandlerResolver> exceptionHandlerResolvers;
        protected Set<RequestHandlerResolver> requestHandlerResolvers;
        protected Set<RequestInterceptorResolver> requestInterceptorResolvers;
        protected Set<ResponseInterceptorResolver> responseInterceptorResolvers;
        protected Set<PredicateResolver> predicateResolvers;
        protected Set<ViewResolver> viewResolvers;

        protected Set<Object> controllers;
        protected Model model;
        protected ObjectMapper objectMapper;

        public Builder withControllers(Set<Object> controllers) {
            this.controllers = controllers;
            return this;
        }

        public Builder addControllers(Object... controllers) {
            return addControllers(Arrays.asList(controllers));
        }

        public Builder addControllers(List<Object> controllers) {
            controllers.forEach(this::addController);
            return this;
        }

        public Builder addController(Object controller) {
            if(this.controllers == null) {
                this.controllers = new LinkedHashSet<>();
            }
            this.controllers.add(controller);
            return this;
        }

        public Builder withViewResolvers(List<ViewResolver> viewResolvers) {
            this.viewResolvers = new LinkedHashSet<>(viewResolvers);
            return this;
        }

        public Builder addViewResolvers(ViewResolver... viewResolver) {
            return addViewResolvers(Arrays.asList(viewResolver));
        }

        public Builder addViewResolvers(List<ViewResolver> viewResolvers) {
            viewResolvers.forEach(this::addViewResolver);
            return this;
        }

        public Builder addViewResolver(ViewResolver viewResolver) {
            if (this.viewResolvers == null) {
                this.viewResolvers = new LinkedHashSet<>();
            }
            this.viewResolvers.add(viewResolver);
            return this;
        }

        public Builder withArgumentResolvers(Set<ArgumentResolver> argumentResolvers) {
            this.argumentResolvers = new LinkedHashSet<>(argumentResolvers);
            return this;
        }

        public Builder addArgumentResolvers(ArgumentResolver... argumentResolver) {
            return addArgumentResolvers(Arrays.asList(argumentResolver));
        }

        public Builder addArgumentResolvers(Collection<ArgumentResolver> argumentResolvers) {
            argumentResolvers.forEach(this::addArgumentResolver);
            return this;
        }

        public Builder addArgumentResolver(ArgumentResolver argumentResolver) {
            if (this.argumentResolvers == null) {
                this.argumentResolvers = new LinkedHashSet<>();
            }
            this.argumentResolvers.add(argumentResolver);
            return this;
        }

        public Builder withRequestHandlerResolvers(Collection<RequestHandlerResolver> resolvers) {
            this.requestHandlerResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }
        public Builder addRequestHandlerResolvers(Collection<RequestHandlerResolver> resolvers) {
            resolvers.forEach(this::addRequestHandlerResolver);
            return this;
        }
        public Builder addRequestHandlerResolver(RequestHandlerResolver resolver) {
            if (this.requestHandlerResolvers == null) {
                this.requestHandlerResolvers = new LinkedHashSet<>();
            }
            this.requestHandlerResolvers.add(resolver);
            return this;
        }

        public Builder withPredicateResolvers(Collection<PredicateResolver> resolvers) {
            this.predicateResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }
        
        public Builder addPredicateResolvers(Collection<PredicateResolver> resolvers) {
            resolvers.forEach(this::addPredicateResolver);
            return this;
        }

        public Builder addPredicateResolver(PredicateResolver resolver) {
            if (this.predicateResolvers == null) {
                this.predicateResolvers = new LinkedHashSet<>();
            }
            this.predicateResolvers.add(resolver);
            return this;
        }

        public Builder withRequestInterceptorResolvers(Collection<RequestInterceptorResolver> resolvers) {
            this.requestInterceptorResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }
        
        public Builder addRequestInterceptorResolvers(Collection<RequestInterceptorResolver> resolvers) {
            resolvers.forEach(this::addRequestInterceptorResolver);
            return this;
        }

        public Builder addRequestInterceptorResolver(RequestInterceptorResolver resolver) {
            if (this.requestInterceptorResolvers == null) {
                this.requestInterceptorResolvers = new LinkedHashSet<>();
            }
            this.requestInterceptorResolvers.add(resolver);
            return this;
        }
        
        public Builder withResponseInterceptorResolvers(Collection<ResponseInterceptorResolver> resolvers) {
            this.responseInterceptorResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }
        
        public Builder addResponseInterceptorResolvers(Collection<ResponseInterceptorResolver> resolvers) {
            resolvers.forEach(this::addResponseInterceptorResolver);
            return this;
        }

        public Builder addResponseInterceptorResolver(ResponseInterceptorResolver resolver) {
            if (this.responseInterceptorResolvers == null) {
                this.responseInterceptorResolvers = new LinkedHashSet<>();
            }
            this.responseInterceptorResolvers.add(resolver);
            return this;
        }
        
        public Builder withExceptionHandlerResolvers(Collection<ExceptionHandlerResolver> resolvers) {
            this.exceptionHandlerResolvers = new LinkedHashSet<>(resolvers);
            return this;
        }
        
        public Builder addExceptionHandlerResolvers(Collection<ExceptionHandlerResolver> resolvers) {
            resolvers.forEach(this::addExceptionHandlerResolver);
            return this;
        }

        public Builder addExceptionHandlerResolver(ExceptionHandlerResolver resolver) {
            if (this.exceptionHandlerResolvers == null) {
                this.exceptionHandlerResolvers = new LinkedHashSet<>();
            }
            this.exceptionHandlerResolvers.add(resolver);
            return this;
        }

        public Builder withObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public Builder withModel(Model model) {
            this.model = model;
            return this;
        }

        public MvcSdkModule build() {
            Set<ArgumentResolver> argumentResolvers = this.argumentResolvers != null
                ? this.argumentResolvers : new LinkedHashSet<>(DEFAULT_ARGUMENT_RESOLVERS);

            if (model != null && !model.equals(Model.empty())) {
                IntentMapper intentMapper = IntentMapper.fromModel(model);
                argumentResolvers.add(new IntentModelArgumentResolver(intentMapper));
                argumentResolvers.add(new SlotModelArgumentResolver(intentMapper));
            }
            if (this.argumentResolvers != null) {
                argumentResolvers.addAll(this.argumentResolvers);
            }

            return new MvcSdkModule(SkillContext.builder()
                .withControllers(controllers)
                .withModel(model)
                .withObjectMapper(objectMapper)
                .withArgumentResolvers(argumentResolvers)
                .withExceptionHandlerResolvers(exceptionHandlerResolvers)
                .withPredicateResolvers(predicateResolvers)
                .withRequestHandlerResolvers(requestHandlerResolvers)
                .withRequestInterceptorResolvers(requestInterceptorResolvers)
                .withResponseInterceptorResolvers(responseInterceptorResolvers)
                .withViewResolvers(viewResolvers)
                .build());
        }
    }
}
