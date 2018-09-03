package com.amazon.ask.mvc.mapper;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.RequestHandlerChain;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultRequestHandlerChain;
import com.amazon.ask.dispatcher.request.interceptor.RequestInterceptor;
import com.amazon.ask.dispatcher.request.interceptor.ResponseInterceptor;
import com.amazon.ask.dispatcher.request.mapper.RequestMapper;
import com.amazon.ask.dispatcher.request.mapper.impl.DefaultRequestMapper;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.mapper.guard.*;
import com.amazon.ask.mvc.plugin.Resolver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 *
 */
public class ControllerRequestMapper implements RequestMapper {
    protected static final Predicate<HandlerInput> TRUE = input -> true;

    protected final SkillContext skillContext;
    protected final Object controller;
    protected final Predicate<HandlerInput> predicate;
    protected final RequestMapper requestMapper;

    public ControllerRequestMapper(SkillContext skillContext, Object controller) {
        this.skillContext = assertNotNull(skillContext, "skillContext");
        this.controller = assertNotNull(controller, "controller");

        List<RequestInterceptor> requestInterceptors = findRequestInterceptors(controller);
        List<ResponseInterceptor> responseInterceptors = findResponseInterceptors(controller);
        List<ExceptionHandler> exceptionHandlers = findExceptionHandlers(controller);

        List<DefaultRequestHandlerChain> requestHandlerChains = findRequestHandlers(controller)
            .map(handler -> DefaultRequestHandlerChain.builder()
                .withRequestHandler(handler)
                .withExceptionHandlers(exceptionHandlers)
                .withRequestInterceptors(requestInterceptors)
                .withResponseInterceptor(responseInterceptors)
                .build())
            .collect(Collectors.toList());

        // resolve the controller class's predicate
        this.predicate = findPredicates(ControllerContext.builder()
            .withSkillContext(skillContext)
            .withController(controller)
            .build()).orElse(TRUE);

        this.requestMapper = DefaultRequestMapper.builder()
            .withRequestHandlerChains(requestHandlerChains)
            .build();
    }

    @Override
    public Optional<RequestHandlerChain> getRequestHandlerChain(HandlerInput input) {
        if (predicate.test(input)) {
            // Both the CONTROLLER & METHOD predicates must match, so we can slightly
            // optimize a scan by short-circuiting it if the controller's predicate
            // yields false.
            return requestMapper.getRequestHandlerChain(input);
        } else {
            return Optional.empty();
        }
    }

    protected Optional<Predicate<HandlerInput>> findPredicates(AnnotationContext context) {
        return skillContext.getPredicateResolvers().stream()
            .map(resolver -> resolver.resolve(context))
            .filter(Optional::isPresent).map(Optional::get)
            .reduce(Predicate::and);
    }

    protected Stream<? extends RequestHandler> findRequestHandlers(Object controller) {
        return find(controller, skillContext.getRequestHandlerResolvers(), RequestHandlerGuard::builder);
    }

    protected List<ExceptionHandler> findExceptionHandlers(Object controller) {
        return find(controller, skillContext.getExceptionHandlerResolvers(), ExceptionHandlerGuard::builder).collect(Collectors.toList());
    }

    protected List<RequestInterceptor> findRequestInterceptors(Object controller) {
        return find(controller, skillContext.getRequestInterceptorResolvers(), RequestInterceptorGuard::builder).collect(Collectors.toList());
    }

    protected List<ResponseInterceptor> findResponseInterceptors(Object controller) {
        return find(controller, skillContext.getResponseInterceptorResolvers(), ResponseInterceptorGuard::builder).collect(Collectors.toList());
    }

    /**
     * Generic procedure for discovering request/exception handlers and request/response interceptors
     * from a controller's methods.
     *
     * Applies method discovery logic consistently for all mapping types:
     * <ul>
     *     <li>resolve a {@link Predicate <HandlerInput>} from the method</li>
     *     <li>delegate responsibility to the underlying handler, but guard it with the {@link Predicate<HandlerInput>}</li>
     *     <li>look for the {@link Priority} annotation on each method</li>
     *     <li>order each handler in the controller by its priority</li>
     *     <li>methods not annotated with {@link Priority} are considered to have priority '0'</li>
     * </ul>
     *
     * @param controller scanned for methods with mappers
     * @param resolvers set of resolvers
     * @param guardBuilder supplies a {@link Guard.Builder} for this mapping type
     * @param <T> type of handler/interceptor/etc. being discovered, e.g. {@link RequestInterceptor}
     * @param <G> type of guard, e.g. {@link RequestInterceptorGuard}
     * @param <B> type of guard builder, e.g. {@link RequestInterceptorGuard#builder()}
     * @return stream of constructed delegates, ordered by priority
     */
    protected <T, G extends Guard<T>, B extends Guard.Builder<B, T, G>> Stream<G> find(
        Object controller,
        Set<? extends Resolver<ControllerMethodContext, T>> resolvers,
        Supplier<B> guardBuilder) {

        return Arrays.stream(controller.getClass().getMethods())
            .map(method -> ControllerMethodContext.builder()
                .withSkillContext(skillContext)
                .withController(controller)
                .withMethod(method)
                .build())
            .flatMap(context -> {
                Predicate<HandlerInput> predicate = findPredicates(context).orElse(TRUE);

                return resolvers.stream()
                    .flatMap(resolver -> resolver.resolve(context).map(Stream::of).orElse(Stream.empty()))
                    .map(delegate -> guardBuilder.get()
                        .withDelegate(delegate)
                        .withPredicate(predicate)
                        .withPriority(Optional.ofNullable(context.getMethod().getAnnotation(Priority.class))
                            .map(Priority::value)
                            .orElse(0))) // default to the '0' bucket for methods not annotated with Priority
                    .map(B::build);
            })
            // sort in descending order, so "higher priority" is more intuitive
            .sorted((a, b) -> -1 * Integer.compare(a.getPriority(), b.getPriority()));
    }

    public SkillContext getSkillContext() {
        return skillContext;
    }

    public Object getController() {
        return controller;
    }

    public Predicate<HandlerInput> getPredicate() {
        return predicate;
    }

    public RequestMapper getRequestMapper() {
        return requestMapper;
    }
}
