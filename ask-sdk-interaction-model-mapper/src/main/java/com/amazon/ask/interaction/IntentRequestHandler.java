/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.mapper.IntentMapper;
import com.amazon.ask.interaction.mapper.IntentParseException;
import com.amazon.ask.interaction.mapper.intent.IntentReader;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * A {@link RequestHandler} for an {@link IntentRequest} and a known intent type.
 *
 * Makes use of the {@link IntentMapper} or {@link IntentReader} to read a raw
 * intent request into an intent instance.
 */
public abstract class IntentRequestHandler<T> implements RequestHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final IntentReader<T> intentReader;
    protected final Class<T> intentClass;
    protected final Intent intentAnnotation;

    public IntentRequestHandler(Class<T> intentClass, IntentMapper intentMapper) {
        this(intentClass, intentMapper.intentReaderFor(intentClass));
    }

    public IntentRequestHandler(Class<T> intentClass, IntentReader<T> intentReader) {
        this.intentClass = assertNotNull(intentClass, "intentClass");
        this.intentReader = assertNotNull(intentReader, "intentReader");

        this.intentAnnotation = Utils.findAnnotation(intentClass, Intent.class);
        if (this.intentAnnotation == null) {
            throw new IllegalArgumentException(String.format("Class '%s' must be annotated with '%s'", intentClass.getName(), Intent.class.getName()));
        }
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        if (input.getRequestEnvelope().getRequest() instanceof IntentRequest) {
            IntentRequest request = (IntentRequest) input.getRequestEnvelope().getRequest();
            return request.getIntent().getName().equals(intentAnnotation.value());
        }
        return false;
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        try {
            return handle(input, intentReader.read((IntentRequest) input.getRequestEnvelope().getRequest()));
        } catch (IntentParseException ex) {
            String msg = String.format("Failed to read a '%s' from intent request", intentClass.getName());
            logger.error(msg);
            throw new RuntimeException(msg, ex);
        }
    }

    /**
     * Handles the request. A {@link Response} should be returned by this method when processing an in-session request,
     * but may be omitted for certain out of session requests.
     *
     * @param input handler input containing the {@link com.amazon.ask.model.RequestEnvelope}, {@link com.amazon.ask.attributes.AttributesManager},
     *              {@link com.amazon.ask.model.services.ServiceClientFactory}, {@link com.amazon.ask.response.ResponseBuilder},
     *              and other utilities.
     * @param intent instance of the intent parsed from the {@link IntentRequest}
     * @return an {@link Optional} that may contain a {@link Response} to be sent back by the skill
     */
    protected abstract Optional<Response> handle(HandlerInput input, T intent);
}
