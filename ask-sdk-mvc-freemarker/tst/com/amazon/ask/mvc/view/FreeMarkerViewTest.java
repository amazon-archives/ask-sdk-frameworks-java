package com.amazon.ask.mvc.view;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class FreeMarkerViewTest {
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private FreeMarkerViewResolver view = FreeMarkerViewResolver.builder()
        .withPrefix("/views/")
        .withObjectMapper(objectMapper)
        .build();

    private RequestEnvelope envelope = RequestEnvelope.builder()
        .withRequest(IntentRequest.builder().build())
        .build();

    private Map<String, ?> model = Collections.singletonMap("hero", Collections.singletonMap("name", "Deadpool"));

    /**
     * Verify that the view handles any view when no pattern is specified
     */
    @Test
    public void testSupportWithoutViewNamePattern() throws Exception {
        assertTrue(view.resolve(mav("simple"), envelope).isPresent());
        assertTrue(view.resolve(mav("simple"), envelope).isPresent());
        // TODO: Figure out if we still want this functionality.
        // TODO: Do we want to inspect for absolute paths vs relative?
//        assertTrue(view.resolve(mav("/some/path/simple"), envelope).isPresent());
    }

    /**
     * Verify that the view handles only the matching pattern
     */
    @Test
    public void testSupportWithViewNamePattern() throws Exception {
        view = FreeMarkerViewResolver.builder()
            .withViewNamePatterns(Arrays.asList(
                Pattern.compile("/views/foo/.*?"),
                Pattern.compile("/views/bar/.*?")))
            .withObjectMapper(objectMapper)
            .build();

        assertTrue(view.resolve(mav("/views/foo/simple"), envelope).isPresent());
        assertFalse(view.resolve(mav("/views/foo/simple.xml"), envelope).isPresent());
        // TODO: What happened to this case?
//        assertTrue(view.resolve(mav("/views/bar/simple"), envelope).isPresent());
    }

    /**
     * Render a view without prefix or suffix
     */
    @Test
    public void testRenderViewDefaultConfig() throws Exception {
        Response response = view.resolve(mav("simple"), envelope).get().render(mav("simple"), envelope);
        assertNotNull(response);

        assertResponse(response, "hello Deadpool");
    }

    /**
     * Verify that if there is a locale specified in the request, and no view matches the locale, the default locale is used
     */
    @Test
    public void testRenderViewWithoutLocaleMatch() throws Exception {
        RequestEnvelope envelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("en-CA")
                .build())
            .build();

        Response response = view.resolve(mav("simple"), envelope).get().render(mav("simple"), envelope);
        assertNotNull(response);

        assertResponse(response, "hello Deadpool");
    }

    /**
     * Verify that if there is a locale specified in the request, and there is a file for just the language, without country code, it will be used
     */
    @Test
    public void testRenderViewWithJustLanguageMatch() throws Exception {
        FreeMarkerViewResolver view = FreeMarkerViewResolver.builder()
            .withPrefix("/other/foo/")
            .withObjectMapper(objectMapper)
            .build();

        RequestEnvelope envelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("en-CA")
                .build())
            .build();

        Response response = view.resolve(mav("simple"), envelope).get().render(mav("simple"), envelope);
        assertNotNull(response);

        assertResponse(response, "hello en Deadpool");
    }


    /**
     * Verify that if there is a locale specified in the request, and no view matches the locale, the default locale is used
     */
    @Test
    public void testRenderViewWithLocaleMatch() throws Exception {
        RequestEnvelope envelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("de-DE")
                .build())
            .build();

        Response response = view.resolve(mav("simple"), envelope).get().render(mav("simple"), envelope);
        assertNotNull(response);

        assertResponse(response, "Hallo Deadpool!");
    }

    /**
     * Render a view without prefix and suffix
     */
    @Test
    public void testRenderViewWithPrefixAndSuffix() throws Exception {
        FreeMarkerViewResolver view = FreeMarkerViewResolver.builder()
            .withPrefix("/views/foo/")
            .withObjectMapper(objectMapper)
            .build();

        Response response = view.resolve(mav("simple"), envelope).get().render(mav("simple"), envelope);
        assertNotNull(response);
        assertResponse(response, "hello Deadpool");

        //try it with a locale too
        view = FreeMarkerViewResolver.builder()
            .withPrefix("/other/foo/")
            .withObjectMapper(objectMapper)
            .build();

        RequestEnvelope envelope = RequestEnvelope.builder()
            .withRequest(IntentRequest.builder()
                .withLocale("de-DE")
                .build())
            .build();

        response = view.resolve(mav("simple"), envelope).get().render(mav("simple"), envelope);
        assertNotNull(response);
        assertResponse(response, "Hallo Deadpool!");

    }

    private ModelAndView mav(String viewName) {
        return new ModelAndView(viewName, model);
    }


    private void assertResponse(Response response, String text) {
        assertEquals(text, ((PlainTextOutputSpeech) response.getOutputSpeech()).getText());
    }
}
