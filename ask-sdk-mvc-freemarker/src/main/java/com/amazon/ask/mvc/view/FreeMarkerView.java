/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Template;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Renders the JSON response from a FreeMarker template.
 */
public class FreeMarkerView extends BaseView {
    private final Template template;

    public FreeMarkerView(ObjectMapper mapper, Template template) {
        super(mapper);
        this.template = assertNotNull(template, "template");
    }

    @Override
    protected String renderInternal(Map<String, Object> model) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
        OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        try {
            template.process(model, writer);
            writer.close();
            return new String(stream.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            writer.close();
        }
    }
}
