/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.mapper;

import com.amazon.ask.mvc.SkillContext;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

/**
 *
 */
public interface AnnotationContext {
    SkillContext getSkillContext();

    Stream<Annotation> scanAnnotations();
}
